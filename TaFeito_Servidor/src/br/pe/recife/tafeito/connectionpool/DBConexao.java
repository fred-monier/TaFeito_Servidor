package br.pe.recife.tafeito.connectionpool;

public class DBConexao {
		
	private String fonte;
	private String url;
	private String login;
	private String senha;
	private int maxConexoes;
	
	public DBConexao(String fonte, String url, String login, String senha, int maxConexoes) {
		super();
		this.fonte = fonte;
		this.url = url;
		this.login = login;
		this.senha = senha;
		this.maxConexoes = maxConexoes;
	}
		
	public String getFonte() {
		return fonte;
	}

	public void setFonte(String fonte) {
		this.fonte = fonte;
	}

	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public int getMaxConexoes() {
		return maxConexoes;
	}
	public void setMaxConexoes(int maxConexoes) {
		this.maxConexoes = maxConexoes;
	}
		
}
