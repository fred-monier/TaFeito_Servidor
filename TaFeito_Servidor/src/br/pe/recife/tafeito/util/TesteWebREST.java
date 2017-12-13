package br.pe.recife.tafeito.util;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;

public class TesteWebREST {

	private Client client;

	private static final String SUCCESS_RESULT="<result>success</result>";
	
	private static final String PASS = "pass";
	private static final String FAIL = "fail";

	private void init(){
		this.client = ClientBuilder.newClient();
	}	
	
	private void testAddFornecedor(){
			
		String REST_SERVICE_URL = 
				"http://localhost:8080/TaFeito_Servidor/rest/AcessoService/acessosFornecedor";
		
		Form form = new Form();
		form.param("login", "restForn1");
		form.param("senha", "1234");
		form.param("email", "restForn1@g.com");
		form.param("endereco", "restForn1End");
		form.param("habilitado", "true");
		form.param("nome", "restForn1Nome");
		form.param("telefone", "1234567890");
		form.param("cnpj", "1");

		String callResult = client
				.target(REST_SERVICE_URL)
				.request(MediaType.APPLICATION_XML)
				.post(Entity.entity(form,
						MediaType.APPLICATION_FORM_URLENCODED_TYPE),
						String.class);

		String result = PASS;
		if(!SUCCESS_RESULT.equals(callResult)){
			result = FAIL;
		}

		System.out.println("Teste: testAddFornecedor, Result: " + result );
	}
	
	private void testAddCliente(){
		
		String REST_SERVICE_URL = 
				"http://localhost:8080/TaFeito_Servidor/rest/AcessoService/acessosCliente";
		
		Form form = new Form();
		form.param("login", "restClient1");
		form.param("senha", "4321");
		form.param("email", "restClient1@g.com");
		form.param("endereco", "restClient1End");
		form.param("habilitado", "true");
		form.param("nome", "restClient1Nome");
		form.param("telefone", "1234567890");
		form.param("cpf", "1");

		String callResult = client
				.target(REST_SERVICE_URL)
				.request(MediaType.APPLICATION_XML)
				.post(Entity.entity(form,
						MediaType.APPLICATION_FORM_URLENCODED_TYPE),
						String.class);

		String result = PASS;
		if(!SUCCESS_RESULT.equals(callResult)){
			result = FAIL;
		}

		System.out.println("Teste: testAddCliente, Result: " + result );
	}	

	public static void main(String[] args) {

		TesteWebREST tester = new TesteWebREST();
		
		tester.init();
		
		tester.testAddFornecedor();
		
		tester.testAddCliente();

	}

}
