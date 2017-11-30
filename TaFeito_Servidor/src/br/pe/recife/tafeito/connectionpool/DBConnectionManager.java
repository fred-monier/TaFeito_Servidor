//Verificar o metodo createPools"

package br.pe.recife.tafeito.connectionpool;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
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
import java.util.Properties;
import java.util.StringTokenizer;
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
	static private DBConnectionManager instance;      
	static private int clients;

	private Vector drivers = new Vector();
	private PrintWriter log;
	private Hashtable pools = new Hashtable();
	private boolean modo_debug = false;

	/**
	 * Retorna a unica instancia, criando uma se eh a primeira vez que
	 * este metodo eh chamado.
	 *
	 * @return DBConnectionManager A unica instancia.
	 */
	static synchronized public DBConnectionManager getInstance() {
		if (instance == null) {
			instance = new DBConnectionManager();
		}
		clients++;
		return instance;
	}

	/**
	 * Um construtor privado do tipo Singleton
	 */
	private DBConnectionManager() {
		init();
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
	 * Retorna uma conexao aberta. Se nenhuma esta disponivel, e o numero
	 * maximo de conexoes tem sido superado, uma nova conexao eh criada. Se o
	 * numero maximo de conexoes tem sido superado, espera ate que um esteja
	 * disponivel ou o tempo especificado tenha sido terminado.
	 *
	 * @param name O nomo do pool definido no arquivo de propriedades
	 * @param time O numero de milisegundos para esperar
	 * @return Connection A conexao ou nulo
	 */
//	public Connection getConnection_(String name, long time) {
//		DBConnectionPool pool = (DBConnectionPool) pools.get(name);
//		if (pool != null) {
//			return pool.getConnection_(time);
//		}
//		return null;
//	}

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
	 * Carrega as propriedades e inicializa a instancia com seus valores.
	 */
	private void init() {

		URL url = this.getClass().getClassLoader().getResource("/db.properties");
		InputStream is = this.getClass().getClassLoader().getResourceAsStream("/db.properties");

		Properties dbProps = new Properties();

		try {

		  dbProps.load(is);

		}
		catch (Exception e) {
			System.err.println("Nao consigo ler o arquivo de propriedades. " +
				"Verifique se o arquivo db.properties esta no diretorio WEB-INF/classes.");
			return;
		}

		String logFile = url.getPath().substring(0, url.getPath().lastIndexOf("/")) + "/DBConnectionManagerBOE.log";
		try {
			log = new PrintWriter(new FileWriter(logFile, true), true);
		}
		catch (IOException e) {
			System.err.println("Nao consigo abrir o arquivo de log: " + logFile);
			log = new PrintWriter(System.err);
		}
		System.out.println("Configuracao do log DBConnectionManager: "+(String)dbProps.getProperty("log"));
		if(((String)dbProps.getProperty("log", "false")).equals("true")){
			modo_debug = true;
		}
		loadDrivers(dbProps);
		createPools(dbProps);
	}

	/**
	 * Carrega e registra todos os drivers JDBC. Isto eh feito pelo
	 * DBConnectionManager, oposto ao DBConnectionPool,
	 * para que muitos pools possam compartilhar o mesmo driver.
	 *
	 * @param props Propriedades do pool de conexao
	 */
	private void loadDrivers(Properties props) {
		String driverClasses = props.getProperty("drivers");
		StringTokenizer st = new StringTokenizer(driverClasses);
		while (st.hasMoreElements()) {
			String driverClassName = st.nextToken().trim();
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

	/**
	 * Cria instancias de DBConnectionPool baseado nas propriedades.
	 * Um DBConnectionPool pode ser definido com as seguintes propriedades:
	 * <PRE>
	 * &lt;poolname&gt;.url         A URL do JDBC para o banco de dados
	 * &lt;poolname&gt;.user        Um usuario do banco de dados (opcional)
	 * &lt;poolname&gt;.password    Uma senha para o usuario do banco de dados (se o usuario foi especificado)
	 * &lt;poolname&gt;.maxconn     O numero maximo de conexoes (opcional)
	 * </PRE>
	 *
	 * @param props As propriedades do pool de conexoes
	 */
	private void createPools(Properties props) {
		Enumeration propNames = props.propertyNames();
		while (propNames.hasMoreElements()) {
			String name = (String) propNames.nextElement();
			if (name.endsWith(".url")) {
				String poolName = name.substring(0, name.lastIndexOf("."));
				String url = props.getProperty(poolName + ".url");
				if (url == null) {
					log("Nenhuma URL especificada para " + poolName);
					continue;
				}
				String user = props.getProperty(poolName + ".user");
				String password = props.getProperty(poolName + ".password");
				String maxconn = props.getProperty(poolName + ".maxconn", "0");
				int max;
				try {
					max = Integer.valueOf(maxconn).intValue();
				}
				catch (NumberFormatException e) {
					log("Valor da propriedade maxconn invalido: " + maxconn + ", para " + poolName);
					max = 0;
				}
				DBConnectionPool pool =
					new DBConnectionPool(poolName, url, user, password, max);
				pools.put(poolName, pool);
				log("Pool de conexoes inicializado: " + poolName);
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
		 * Checks out a connection from the pool. If no free connection
		 * is available, a new connection is created unless the max
		 * number of connections has been reached. If a free connection
		 * has been closed by the database, it's removed from the pool
		 * and this method is called again recursively.
		 * <P>
		 * If no connection is available and the max number has been
		 * reached, this method waits the specified time for one to be
		 * checked in.
		 *
		 * @param timeout The timeout value in milliseconds
		 */
//		public synchronized Connection getConnection_(long timeout) {
//			long startTime = new Date().getTime();
//			Connection con;
//			while ((con = getConnection()) == null) {
//				try {
//					wait(timeout);
//				}
//				catch (InterruptedException e) {}
//				if ((new Date().getTime() - startTime) >= timeout) {
//					// Timeout has expired
//					return null;
//				}
//			}
//			return con;
//		}

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

