package br.pe.recife.tafeito.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.pe.recife.tafeito.connectionpool.Contexto;
import br.pe.recife.tafeito.connectionpool.DBConexao;
import br.pe.recife.tafeito.connectionpool.DBConnectionManager;
import br.pe.recife.tafeito.excecao.ConexaoBDException;


public class DBTaFeito {	
	
	private static DBTaFeito aInstancia = new DBTaFeito();
		
	private static DBConnectionManager connMgr;
			
	private static final String DRIVER = "org.postgresql.Driver";
	
	private static final String FONTE_DADOS = "tafeito";
	private static final String URL = "jdbc:postgresql://127.0.0.1:5432/tafeito";
	private static final String LOGIN = "postgres";
	private static final String SENHA = "admin";
	private static final int CONEXOES = 10;		   

    public static final String TABELA_ACESSO = "ACESSO";
    public static final String TABELA_ACESSO_COLUNA_ID = "ID";
    public static final String TABELA_ACESSO_COLUNA_LOGIN = "LOGIN";
    public static final String TABELA_ACESSO_COLUNA_SENHA = "SENHA";

    public static final String TABELA_USUARIO = "USUARIO";
    public static final String TABELA_USUARIO_COLUNA_ID = "ID";
    public static final String TABELA_USUARIO_COLUNA_HABILITADO = "HABILITADO";
    public static final String TABELA_USUARIO_COLUNA_NOME = "NOME";
    public static final String TABELA_USUARIO_COLUNA_ENDERECO = "ENDERECO";
    public static final String TABELA_USUARIO_COLUNA_EMAIL = "EMAIL";
    public static final String TABELA_USUARIO_COLUNA_TELEFONE = "TELEFONE";

    public static final String TABELA_FORNECEDOR = "FORNECEDOR";
    public static final String TABELA_FORNECEDOR_COLUNA_ID = "ID";
    public static final String TABELA_FORNECEDOR_COLUNA_CNPJ = "CNPJ";

    public static final String TABELA_CLIENTE = "CLIENTE";
    public static final String TABELA_CLIENTE_COLUNA_ID = "ID";
    public static final String TABELA_CLIENTE_COLUNA_CPF = "CPF";

    public static final String TABELA_SERVICO_CATEGORIA = "SERVICO_CATEGORIA";
    public static final String TABELA_SERVICO_CATEGORIA_COLUNA_ID = "ID";
    public static final String TABELA_SERVICO_CATEGORIA_COLUNA_NOME = "NOME";
    public static final String TABELA_SERVICO_CATEGORIA_COLUNA_DESCRICAO = "DESCRICAO";

    public static final String TABELA_SERVICO = "SERVICO";
    public static final String TABELA_SERVICO_COLUNA_ID = "ID";
    public static final String TABELA_SERVICO_COLUNA_ID_SERVICO_CATEGORIA = "ID_SERVICO_CATEGORIA";
    public static final String TABELA_SERVICO_COLUNA_ID_FORNECEDOR = "ID_FORNECEDOR";
    public static final String TABELA_SERVICO_COLUNA_NOME = "NOME";
    public static final String TABELA_SERVICO_COLUNA_DESCRICAO = "DESCRICAO";

    public static final String TABELA_OFERTA = "OFERTA";
    public static final String TABELA_OFERTA_COLUNA_ID = "ID";
    public static final String TABELA_OFERTA_COLUNA_ID_SERVICO = "ID_SERVICO";    
    public static final String TABELA_OFERTA_COLUNA_DATA_HORA_INICIO = "DATA_HORA_INICIO";
    public static final String TABELA_OFERTA_COLUNA_DATA_HORA_FIM = "DATA_HORA_FIM";

    public static final String TABELA_AGENDAMENTO = "AGENDAMENTO";
    public static final String TABELA_AGENDAMENTO_COLUNA_ID = "ID";
    public static final String TABELA_AGENDAMENTO_COLUNA_ID_OFERTA = "ID_OFERTA";
    public static final String TABELA_AGENDAMENTO_COLUNA_ID_CLIENTE = "ID_AGENDAMENTO";
    public static final String TABELA_AGENDAMENTO_COLUNA_DATA_HORA_REALIZADO = "DATA_HORA_REALIZADO";
    public static final String TABELA_AGENDAMENTO_COLUNA_DATA_HORA_CANCELADO = "DATA_HORA_CANCELADO";

	private DBTaFeito() {
		super();
		
		//TODO - Inicializar via Servlet REST API
		inicializarPool();
	}

	public static DBTaFeito getInstancia() {
		if (DBTaFeito.aInstancia == null) {
			DBTaFeito.aInstancia = new DBTaFeito();
		}
		return DBTaFeito.aInstancia;
	}
	
	public static void inicializarPool() {
		if (DBTaFeito.connMgr == null) {
			
			List<String> drivers = new ArrayList<String>();
			drivers.add(DRIVER);
			
			List<DBConexao> conexoes = new ArrayList<DBConexao>();
			conexoes.add(new DBConexao(FONTE_DADOS, URL, LOGIN, SENHA, CONEXOES));
			
			DBTaFeito.connMgr = DBConnectionManager.getInstance(drivers, conexoes);
		}
	}

	public static void finalizarPool() {
		DBTaFeito.connMgr.release();
	}

	public static Connection getConexao() {
		Connection conexao = null;

		conexao = DBTaFeito.connMgr.getConnection(DBTaFeito.FONTE_DADOS);

		return conexao;
	}

	public static void beginTransacao(Contexto pContexto) throws ConexaoBDException {

		Connection conexao = null;

		if (pContexto != null) {

			conexao = pContexto.getConexao();
			if (conexao == null) {
				conexao = DBTaFeito.getConexao();

				if (conexao == null) {
					throw new ConexaoBDException("N�o foi poss�vel obter a conex�o '" + DBTaFeito.FONTE_DADOS + "'.");
				}

				try {
					conexao.setAutoCommit(false);
				} catch (SQLException e) {
					throw new ConexaoBDException("Falha no 'setAutoCommit(false)'.", e);
				}

				pContexto.setConexao(conexao);
			}

		} else {
			throw new ConexaoBDException("Contexto nulo.");
		}
	}

	public static void commitTransacao(Contexto pContexto) throws ConexaoBDException {

		Connection conexao = null;

		if (pContexto != null) {

			conexao = pContexto.getConexao();
			if (conexao != null) {

				try {
					conexao.commit();
					conexao.setAutoCommit(true);
				} catch (Exception e1) {

					try {
						conexao.rollback();
						conexao.setAutoCommit(true);
						throw new ConexaoBDException("Falha no commit.", e1);

					} catch (SQLException e2) {
						throw new ConexaoBDException("Falha grave. A transa��o pode n�o ter sido desfeita.", e2);
					}

				} finally {
					DBTaFeito.connMgr.freeConnection(DBTaFeito.FONTE_DADOS, conexao);
					pContexto.setConexao(null);
				}

			} else {
				throw new ConexaoBDException("N�o existe conex�o para fazer commit.");
			}

		} else {
			throw new ConexaoBDException("Contexto nulo.");
		}
	}

	public static void rollbackTransacao(Contexto pContexto) throws ConexaoBDException {

		Connection conexao = null;

		if (pContexto != null) {

			conexao = pContexto.getConexao();
			if (conexao != null) {

				try {
					conexao.rollback();
					conexao.setAutoCommit(true);
				} catch (Exception e1) {

					try {
						conexao.rollback();
						conexao.setAutoCommit(true);
						throw new ConexaoBDException("Falha no rollback.", e1);

					} catch (SQLException e2) {
						throw new ConexaoBDException("Falha grave. A transa��o pode n�o ter sido desfeita.", e2);
					}

				} finally {
					DBTaFeito.connMgr.freeConnection(DBTaFeito.FONTE_DADOS, conexao);
					pContexto.setConexao(null);
				}
			}

		} else {
			throw new ConexaoBDException("Contexto nulo.");
		}
	}

	public static void liberarStatement(Statement pStatement) {
		if (pStatement != null) {
			try {
				pStatement.close();
			} catch (SQLException e) {
			}
		}
	}
	
	

}
