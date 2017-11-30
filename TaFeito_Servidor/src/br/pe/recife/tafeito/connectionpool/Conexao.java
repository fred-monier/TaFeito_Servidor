package br.pe.recife.tafeito.connectionpool;

import java.sql.Connection;
import java.util.GregorianCalendar;


public class Conexao {

	private Connection aConexao;
	private GregorianCalendar aDataInicioConexao;
	
	
	/**
	 * @return
	 */
	public Connection getConexao() {
		return aConexao;
	}

	/**
	 * @return
	 */
	public GregorianCalendar getDataInicioConexao() {
		return aDataInicioConexao;
	}

	/**
	 * @param connection
	 */
	public void setConexao(Connection connection) {
		aConexao = connection;
	}

	/**
	 * @param calendar
	 */
	public void setDataInicioConexao(GregorianCalendar calendar) {
		aDataInicioConexao = calendar;
	}

}
