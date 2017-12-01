package br.pe.recife.tafeito.connectionpool;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/**
 * Esta classe eh do tipo Singleton que prove acessoa a um ou mais
 * pool's de conexoes definido em um arquivo de propriedade. Um cliente
 * obtem acesso a instancia unica atrves do metodo estatico getInstance()
 * e pode então recuperar ou liberar conexoes de um pool.
 * Quando o cliente finaliza a aplicacao, deve-se chamar o metodo release() para fechar 
 * todas as conexoes abertas e para outras que estejam limpas.
 */
public class DBConnectionManager {
	
	private static DBConnectionManager instance; 
	
	private static int clients;

	private Vector drivers = new Vector();
	
	private Hashtable pools = new Hashtable();
	
	private boolean modo_debug = false;
	
	private PrintWriter log;
	
	public static synchronized DBConnectionManager getInstance(List<String> jdbcDrivers, List<DBConexao> jdbcConnections) {
		
		if (instance == null) {
			instance = new DBConnectionManager(jdbcDrivers, jdbcConnections);
		}
		
		clients++;
		
		return instance;
	}

	/**
	 * Um construtor privado do tipo Singleton
	 */
	private DBConnectionManager(List<String> jdbcDrivers, List<DBConexao> jdbcConnections) {
		
		init(jdbcDrivers, jdbcConnections);
	}

	private void init(List<String> jdbcDrivers, List<DBConexao> jdbcConnections) {
		
		URL url = getClass().getClassLoader().getResource("classes");
		
		String logFile;
		if (url != null) {
			logFile = url.getPath() + "/DBConnectionManagerTaFeito.log";
		} else {
			logFile = "C:/tmp/DBConnectionManagerTaFeito.log";
		}
		
		try {
			log = new PrintWriter(new FileWriter(logFile, true), true);
			
		} catch (IOException e) {
			System.err.println("Nao consigo abrir o arquivo de log: " + logFile);
			log = new PrintWriter(System.err);
		}
		
		System.out.println("Configuracao do log DBConnectionManager: " + modo_debug);
		
		loadDrivers(jdbcDrivers);
		createPools(jdbcConnections);
	}

	private void loadDrivers(List<String> jdbcDrivers) {
		
		Iterator<String> iterator = jdbcDrivers.iterator();
		
		while (iterator.hasNext()) {
			
			String driverClassName = iterator.next();
			try {
				Driver driver = (Driver)
					Class.forName(driverClassName).newInstance();
				DriverManager.registerDriver(driver);
				drivers.addElement(driver);
				log("Registrado o driver JDBC: " + driverClassName);
			}
			catch (Exception e) {
				log("Nao posso registrar o driver JDBC: " +
					driverClassName + ", Exception: " + e);
			}
		}
	}

	private void createPools(List<DBConexao> jdbcConecctions) {
		
		Iterator<DBConexao> iterator = jdbcConecctions.iterator();
		
		while (iterator.hasNext()) {
			
			DBConexao dbConn = iterator.next();
			
			DBConnectionPool pool = new DBConnectionPool(dbConn.getFonte(), dbConn.getUrl(), 
					dbConn.getLogin(), dbConn.getSenha(), dbConn.getMaxConexoes());
			
				pools.put(dbConn.getFonte(), pool);
				
				log("Pool de conexoes inicializado: " + dbConn.getFonte());			
		}
	}	
		
	/**
	 * Retorna uma conexao aberta. Se nenhum esta disponivel, e o numero
	 * maximo de conexoes tem sido superado, uma nova conexao eh criada.
	 *
	 * @param name O nome do pool como definido no arquivo de propriedades
	 * @return Connection A conexao ou nulo
	 */
	public Connection getConnection(String name) {
		DBConnectionPool pool = (DBConnectionPool) pools.get(name);
		if (pool != null) {
			return pool.getConnection();
		}
		return null;
	}
	
	/**
	 * Retorna uma conexao para o pool nomeado.
	 *
	 * @param name O nome do pool como definido no arquivo de propriedades
	 * @param con A conexao
	 */
	public void freeConnection(String name, Connection con) {
		DBConnectionPool pool = (DBConnectionPool) pools.get(name);
		if (pool != null) {
			pool.freeConnection(con);
		}
	}

	/**
	 * Fecha todas as conexoes abertas e limpa o registro de todos os drivers.
	 */
	public synchronized void release() {
		
		// Espera ate chamado pelo ultimo cliente
		log("Numero de clientes: " + DBConnectionManager.clients);
		if (--clients != 0) {
			return;
		}

		Enumeration allPools = pools.elements();
		while (allPools.hasMoreElements()) {
			DBConnectionPool pool = (DBConnectionPool) allPools.nextElement();
			pool.release();
		}
		Enumeration allDrivers = drivers.elements();
		while (allDrivers.hasMoreElements()) {
			Driver driver = (Driver) allDrivers.nextElement();
			try {
				DriverManager.deregisterDriver(driver);
				log("Limpar o registro do driver JDBC: " + driver.getClass().getName());
			}
			catch (SQLException e) {
				log(e, "Nao posso limpar o registro do driver JDBC: " + driver.getClass().getName());
			}
		}
	}

	/**
	 * Escreve uma mensagem no arquivo de log
	 */
	private void log(String msg) {
		if(modo_debug) {
			log.println(new Date() + ": " + msg);
		}
	}

	/**
	 * Escreve uma mensagem com uma Exception no arquivo de log.
	 */
	private void log(Throwable e, String msg) {
		if(modo_debug) {
			log.println(new Date() + ": " + msg);
			e.printStackTrace(log);
		}
	}

	/**
	 * Esta classe interna representa um pool de conexoes. Eh criado novas
	 * conexoes por demanda, ate a um numero maximo especificado.
	 * Eh verificado se a conexao ainda esta aberta antes dele ser
	 * retornado ao cliente.
	 */

	class DBConnectionPool implements Runnable {
		
		private int checkedOut;
		private Vector freeConnections = new Vector();
		
		private Vector createdConnections = new Vector();
		private long mileSeconds = 600000;
		private Thread clockThread = null;
	
		private int maxConn;
		private String name;
		private String password;
		private String URL;
		private String user;
		
		/**
		 * Construtor para criacao de um novo pool de conexoes.
		 *
		 * @param name Nome do pool
		 * @param URL A URL JDBC para o banco de dados
		 * @param user O usuario do banco de dados, ou nulo
		 * @param password A senha do usuario do banco de dados, ou nulo
		 * @param maxConn O numero maximo de conexoes, ou 0 para nenhum limite
		 * 
		 */
		public DBConnectionPool(String name, String URL, String user, String password,
				int maxConn) {
			this.name = name;
			this.URL = URL;
			this.user = user;
			this.password = password;
			this.maxConn = maxConn;
			log("Nome do pool: "+name+", URL: "+URL+", usuario: "+user+", numero maximo de conexoes: "+maxConn);
			if (this.clockThread == null) {
				clockThread = new Thread(this);
				log("Inicializando processo de remoção de conexões antigas.");
				clockThread.start();
			}
		}

		/**
		 * Libera uma conexao para o pool. Notifica outros threads que pode estar
		 * esperando por uma conexao.
		 *
		 * @param con A conexao a ser obtida
		 */
		public synchronized void freeConnection(Connection con) {

			Conexao conexao = this.getConexaoCreated(con);
			if (conexao != null) {
				freeConnections.addElement(conexao);
				checkedOut--;
				log("Liberando Conexao: " + name + ", Conexoes em utilizacao: "+checkedOut);
			} else {
				conexao = new Conexao();
				conexao.setConexao(con);
				conexao.setDataInicioConexao(new GregorianCalendar());
				createdConnections.addElement(conexao);
				freeConnections.addElement(conexao);
				checkedOut--;
				log("Liberando Conexao (nova!!!): " + name + ", Conexoes em utilizacao: "+checkedOut);
			}
			
			notifyAll();
		}

		/**
		 * Obtem uma conexao do pool. Se nenhuma conexao livre esta disponivel,
		 * uma nova conexao eh criada a menos que o numero maximo de conexoes
		 * tenha sido superado. Se uma conexao livre tem sido fechado pelo
		 * banco de dados, ele eh removido do pool e este metodo sera chamado recursivamente.
		 */
		public synchronized Connection getConnection(){
			Connection con = null;
			log("Conexoes livres: " + freeConnections.size());
			
			log("Conexoes criadas em reg: " + createdConnections.size());
			
			if(freeConnections.size() > 0) {

				con = ((Conexao) freeConnections.firstElement()).getConexao();
				
				freeConnections.removeElementAt(0);
				try {
					if (con.isClosed()) {
						log("Conexao ruim removida de " + name);
						
						this.removeConexaoCreated(con, "Conexao (criada) ruim removida de ");
						
						// Tenta recursivamente obter a conexao
						con = getConnection();
					}
					else
					{
						//Testa a conexao
						con.getMetaData();
						log("Teste de conexao, conexão aberta: "+name);
					}
				}
				catch (SQLException e) {
					log("Erro: "+e.getMessage());
					log("Conexao ruim removida de " + name);
					
					// Tenta recursivamente obter a conexao
					con = getConnection();
				}
			}
			else if (maxConn == 0 || checkedOut < maxConn) {
				//Obtendo nova conexao
				con = newConnection();
				
				if (con != null) {
					Conexao conexao = new Conexao();
					conexao.setConexao(con);
					conexao.setDataInicioConexao(new GregorianCalendar());
					createdConnections.addElement(conexao);
				}
				
				log("Conexões abertas: " + checkedOut + ", Numero máximo de conexoes (maxConn): " + maxConn);
			}
			
			if (con != null) {
				checkedOut++;
				log("Obtendo Conexao: " + name + ", Conexoes restantes: "+ (maxConn - checkedOut));
			}
			else {
				log("Nao foi possivel obter conexao: " + name);
			}
			return con;
		}

		/**
		 * Fechar todas as conexoes disponiveis.
		 */
		public synchronized void release() {
			Enumeration allConnections = freeConnections.elements();
			while (allConnections.hasMoreElements()) {

				Connection con = ((Conexao) allConnections.nextElement()).getConexao();

				try {
					
					this.removeConexaoCreated(con, "Conexao (criada) removida para ser fechado pelo pool ");

					con.close();
					log("Conexao fechada para o pool " + name);
					
				}
				catch (SQLException e) {
					log(e, "Nao consigo fechar conexao para o pool " + name);
				}
			}

			freeConnections.removeAllElements();
			
			log("Removido todas as conexoes livres para o pool " + name);

		}
		
		/**
		 * Metodo que remove conexoes antigas
		 *
		 */
		private synchronized void releaseOldConnections() {
			
			if (this.mileSeconds == 0) {
				return;
			}
			     
			Enumeration freeConnectionsElements = freeConnections.elements();
			while (freeConnectionsElements.hasMoreElements()) {
				
				Conexao conexaoInfo = (Conexao) freeConnectionsElements.nextElement();
				Connection con = conexaoInfo.getConexao();
				GregorianCalendar criacao = conexaoInfo.getDataInicioConexao();
				GregorianCalendar agora = new GregorianCalendar();
				
				//Verifica as conexoes que estao abertas com um tempo maior que a variavel mileSeconds
				if ((agora.getTimeInMillis() - criacao.getTimeInMillis()) > this.mileSeconds) {
					try {
						freeConnections.remove(conexaoInfo);
						log("Removido uma conexao antiga livre em " + name);
						this.removeConexaoCreated(conexaoInfo.getConexao(), "Removido uma conexao antiga (criada) para ser fechada de ");
						
						con.close();
						log("Conexao antiga fechada para pool: " + name);
					}
					catch (SQLException e) {
						log(e, "Nao consigo fechar conexao antiga para o pool: " + name);
					}
				}
			}
			
		}
		//Monier Novo Pool

		/**
		 * Cria uma nova conexao, usando um usuario e senha se especificado.
		 */
		private Connection newConnection() {
			Connection con = null;
			try {
				if (user == null) {
					con = DriverManager.getConnection(URL);
				}
				else {
					con = DriverManager.getConnection(URL, user, password);
				}
				log("Criado uma nova conexao no pool: " + name);
			}
			catch (SQLException e) {
				log(e, "Nao posso criar uma nova conexao para: " + name);
				return null;
			}
			return con;
		}
		
		/**
		 * Novo metodo para obter conexoes criadas.
		 * @param con
		 * @return
		 */
		private Conexao getConexaoCreated(Connection con) {
			Conexao resultado = null;
			for (int i=0; i<createdConnections.size(); i++) {
				if (((Conexao) createdConnections.elementAt(i)).getConexao().equals(con)) {
					resultado = (Conexao) createdConnections.elementAt(i);
				}
			}
			return resultado;		
		}
		
		/**
		 * Novo metodo para remover conexoes criadas.
		 * @param con
		 * @param log
		 */
		private void removeConexaoCreated(Connection con, String log) {
			
			for (int i=0; i<createdConnections.size(); i++) {
				if (((Conexao) createdConnections.elementAt(i)).getConexao().equals(con)) {
					createdConnections.removeElementAt(i);
					log(log + name);
				}
			}
			
		}
		
		/**
		 * Thread infinita que executa em intervalo regulares, variavel mileSeconds,
		 * a liberacao das conexoes mais antigas.
		 */
		public void run() {
			while (true) {
				try {
					Thread.sleep(this.mileSeconds);
				} catch (InterruptedException e) {
					//Instante que inicia a chamda do metodo releaseOldConnections
				}
				
				//Inicia o procedimento de limpar as conexoes mais antigas
				log("Verificando conexões antigas (mais de " + this.mileSeconds + " msegs)");
				this.releaseOldConnections();
			}
		}
		
	}
}

