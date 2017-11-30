package br.pe.recife.tafeito.connectionpool;

import java.sql.Connection;


public class Contexto {
	
	private transient Connection aConexao;

	public Contexto() {
	}

	public void setConexao(Connection pConexao) {
		this.aConexao = pConexao;
	}

	public Connection getConexao() {
		return this.aConexao;
	}

}
