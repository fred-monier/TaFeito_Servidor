package br.pe.recife.tafeito.controller;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import br.pe.recife.tafeito.fachada.FachadaTaFeito;
import br.pe.recife.tafeito.http.AcessoHttp;
import br.pe.recife.tafeito.http.LoginHttp;
import br.pe.recife.tafeito.http.RetornoHttp;
import br.pe.recife.tafeito.http.UsuarioHttp;
import br.pe.recife.tafeito.negocio.Acesso;
import br.pe.recife.tafeito.negocio.Cliente;
import br.pe.recife.tafeito.negocio.Fornecedor;

@Path("/usuario")
public class UsuarioController {		
			
	private FachadaTaFeito fachadaTaFeito = FachadaTaFeito.getInstancia();
	
	/**
	 * @Consumes - determina o formato dos dados que vamos postar
	 * @Produces - determina o formato dos dados que vamos retornar
	 * 
	 * Esse método autentica um usuario no sistema
	 * */
	@POST	
	@Consumes("application/json; charset=UTF-8")
	@Produces("application/json; charset=UTF-8")
	@Path("/autenticar")
	public RetornoHttp autenticar(AcessoHttp acessoHttp) {
		
		RetornoHttp res = new RetornoHttp(RetornoHttp.FALHA, "");
		
		try {
		
			long id;
			
	    	Acesso acesso = new Acesso();
	    	acesso.setLogin(acessoHttp.getLogin());
	    	acesso.setSenha(acessoHttp.getSenha());
	    	
	    	if (acessoHttp.getTipoUsuario().equals(UsuarioHttp.FORNECEDOR)) {
	    		id = fachadaTaFeito.autenticarFornecedorAcesso(acesso);
	    	} else {
	    		id = fachadaTaFeito.autenticarClienteAcesso(acesso);
	    	}
			
			res.setResultado(RetornoHttp.SUCESSO);
			
			if (id > 0) {
				res.setDescricao(id + "");
			} else {
				res.setDescricao(RetornoHttp.FALHA);
			}
			
		} catch (Exception e) {
			 
			res.setDescricao(e.getMessage());

		}			
		
		return res;
		
	}
	
	/**
	 * @Consumes - determina o formato dos dados que vamos postar
	 * @Produces - determina o formato dos dados que vamos retornar
	 * 
	 * Esse método verifica se um dado login está disponível para um novo usuario
	 * */
	@POST	
	@Consumes("application/json; charset=UTF-8")
	@Produces("application/json; charset=UTF-8")
	@Path("/liberar")	
	public RetornoHttp liberar(LoginHttp loginHttp) {
		
		RetornoHttp res = new RetornoHttp(RetornoHttp.FALHA, "");
		
		try {
		
			boolean retorno = fachadaTaFeito.existeAcessoPorLogin(loginHttp.getLogin());
			
			res.setResultado(RetornoHttp.SUCESSO);
			
			if (!retorno) {
				res.setDescricao(RetornoHttp.SUCESSO);
			} else {
				res.setDescricao(RetornoHttp.FALHA);
			}
						
			
		} catch (Exception e) {
 
			res.setDescricao(e.getMessage());

		}
		
		return res;		
	}
	
	/**
	 * @Consumes - determina o formato dos dados que vamos postar
	 * @Produces - determina o formato dos dados que vamos retornar
	 * 
	 * Esse método registra um novo usuario
	 * */
	@POST	
	@Consumes("application/json; charset=UTF-8")
	@Produces("application/json; charset=UTF-8")
	@Path("/registrar")
	public RetornoHttp registrar(UsuarioHttp usuarioHttp) {
		
		RetornoHttp res = new RetornoHttp(RetornoHttp.FALHA, "");
 		
		try {
 
	    	Acesso acesso = new Acesso();
	    	acesso.setLogin(usuarioHttp.getLogin());
	    	acesso.setSenha(usuarioHttp.getSenha());
			
			if (usuarioHttp.getTipoUsuario().equals(UsuarioHttp.FORNECEDOR)) {
				
		    	Fornecedor fornecedor = new Fornecedor();
		    	boolean h = false;
		    	if (usuarioHttp.getHabilitado() != null && usuarioHttp.getHabilitado().equals("true")) {
		    		h = true;
		    	}
		    	fornecedor.setHabilitado(h);		
		    	fornecedor.setNome(usuarioHttp.getNome());
		    	fornecedor.setEndereco(usuarioHttp.getEndereco());
		    	fornecedor.setEmail(usuarioHttp.getEmail());		    	
		    	fornecedor.setTelefone(usuarioHttp.getTelefone());
		    	fornecedor.setCnpj(usuarioHttp.getDocumento());
		    	
		    	fachadaTaFeito.inserirUsuarioFornecedorAcesso(fornecedor, acesso);
		    	
		    	res.setResultado(RetornoHttp.SUCESSO);
		    	res.setDescricao(fornecedor.getId() + "");		    	
		    	
			} else if (usuarioHttp.getTipoUsuario().equals(UsuarioHttp.CLIENTE)) {
				
				Cliente cliente = new Cliente();
		    	boolean h = false;
		    	if (usuarioHttp.getHabilitado() != null && usuarioHttp.getHabilitado().equals("true")) {
		    		h = true
		    				;
		    	}
		    	cliente.setHabilitado(h);		
		    	cliente.setNome(usuarioHttp.getNome());		    	
		    	cliente.setEndereco(usuarioHttp.getEndereco());
		    	cliente.setEmail(usuarioHttp.getEmail());		    	
		    	cliente.setTelefone(usuarioHttp.getTelefone());
		    	cliente.setCpf(usuarioHttp.getDocumento());	
		    	
		    	fachadaTaFeito.inserirUsuarioClienteAcesso(cliente, acesso);
		    	
		    	res.setResultado(RetornoHttp.SUCESSO);
		    	res.setDescricao(cliente.getId() + "");

			} else {
				
				res.setDescricao("Tipo de usuário desconhecido.");
			}
 
		} catch (Exception e) {
 
			res.setDescricao(e.getMessage());

		}
		
		return res;

	}	

}
