package br.pe.recife.tafeito.util;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import br.pe.recife.tafeito.http.AcessoHttp;
import br.pe.recife.tafeito.http.LoginHttp;
import br.pe.recife.tafeito.http.RetornoHttp;
import br.pe.recife.tafeito.http.UsuarioHttp;

public class TesteWebREST_II {	

	class ServiceClient {
						
		/**GERENCIA A INFRAESTRUTURA DE COMUNI��O DO LADO 
		 * CLIENTE PARA EXECUTAR AS SOLICITA��ES REALIZADAS*/
		private Client client;
	 
		/**ACESSA UM RECURSO IDENTIFICADO PELO URI(Uniform Resource Identifier/Identificador Uniforme de Recursos)*/
		private WebTarget webTarget;
	 
		/**URL DO SERVI�O REST QUE VAMOS ACESSAR */
		private final String URL_SERVICE = "http://localhost:8080/TaFeito_Servidor/rest/usuario/";
	 
		/**CONSTRUTOR DA NOSSA CLASSE*/
		public ServiceClient() {
	 
			this.client = ClientBuilder.newClient();  
		}
	 
		/**REGISTRA UM USUARIO ATRAV�S DA OPERA��O registrar(M�TODO HTTP: POST) */
		public RetornoHttp registrarUsuario(UsuarioHttp usuarioHttp){
	 
			this.webTarget = this.client.target(URL_SERVICE).path("registrar");
	 
			Invocation.Builder invocationBuilder =  this.webTarget.request("application/json;charset=UTF-8");
	 
			Response response = invocationBuilder.post(Entity.entity(usuarioHttp, "application/json;charset=UTF-8"));
	 
			return response.readEntity(RetornoHttp.class);
	
		}
		
		public RetornoHttp liberarUsuario(LoginHttp loginHttp){
			 
			this.webTarget = this.client.target(URL_SERVICE).path("liberar");
	 
			Invocation.Builder invocationBuilder =  this.webTarget.request("application/json;charset=UTF-8");
	 
			Response response = invocationBuilder.post(Entity.entity(loginHttp, "application/json;charset=UTF-8"));
	 
			return response.readEntity(RetornoHttp.class);
	
		}	
		
		public RetornoHttp autenticarUsuario(AcessoHttp acessoHttp){
			 
			this.webTarget = this.client.target(URL_SERVICE).path("autenticar");
	 
			Invocation.Builder invocationBuilder =  this.webTarget.request("application/json;charset=UTF-8");
	 
			Response response = invocationBuilder.post(Entity.entity(acessoHttp, "application/json;charset=UTF-8"));
	 
			return response.readEntity(RetornoHttp.class);
	
		}		
	}
	
	public static void main(String[] args) {
				
		TesteWebREST_II teste = new TesteWebREST_II();
		ServiceClient client = teste.new ServiceClient();
		
		//Testando /usuario/registrar
		/*
		UsuarioHttp usuarioHttp = new UsuarioHttp();
				
		usuarioHttp.setLogin("jforn2@g.com");
		usuarioHttp.setSenha("1234");		
		usuarioHttp.setHabilitado("true");
		usuarioHttp.setNome("Jason Forn2");
		usuarioHttp.setEndereco("Rua 1");
		usuarioHttp.setEmail("jforn2@g.com");
		usuarioHttp.setTelefone(1234567890);
		usuarioHttp.setTipoUsuario(UsuarioHttp.FORNECEDOR);
		usuarioHttp.setDocumento("22222222222222");
		

		// EFETUA O CADASTRO DE UM NOVO USU�RIO
		RetornoHttp resultado = client.registrarUsuario(usuarioHttp);
 
		// MENSAGEM COM O RESULTADO
		System.out.println("Resultado: " + resultado.getResultado());		
		System.out.println("Descri��o: " + resultado.getDescricao());
		*/
		
		//Testando /usuario/liberar
		/*
		LoginHttp loginHttp = new LoginHttp();
		loginHttp.setLogin("jforn3@g.com");
		
		// VERIFICA SE UM LOGIN EST� DISPON�VEL PARA UM NOVO USU�RIO
		RetornoHttp resultado = client.liberarUsuario(loginHttp);
		
		// MENSAGEM COM O RESULTADO
		System.out.println("Resultado: " + resultado.getResultado());		
		System.out.println("Descri��o: " + resultado.getDescricao());
		*/
		
		//Testando /usuario/autenticar
		AcessoHttp acessoHttp = new AcessoHttp();
		acessoHttp.setLogin("jforn3@g.com");
		acessoHttp.setSenha("1235");
		acessoHttp.setTipoUsuario(UsuarioHttp.FORNECEDOR);
		
		// VERIFICA SE EXISTE UM USUARIO DO TIPO
		RetornoHttp resultado = client.autenticarUsuario(acessoHttp);
		
		// MENSAGEM COM O RESULTADO
		System.out.println("Resultado: " + resultado.getResultado());		
		System.out.println("Descri��o: " + resultado.getDescricao());
				 		
	}

}
