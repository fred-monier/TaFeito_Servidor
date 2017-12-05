package br.pe.recife.tafeito.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import br.pe.recife.tafeito.dao.AcessoDAO;
import br.pe.recife.tafeito.excecao.InfraException;
import br.pe.recife.tafeito.excecao.NegocioException;
import br.pe.recife.tafeito.negocio.Acesso;
import br.pe.recife.tafeito.negocio.Autenticacao;
import br.pe.recife.tafeito.negocio.Cliente;
import br.pe.recife.tafeito.negocio.Fornecedor;
import br.pe.recife.tafeito.negocio.Usuario;
@Path("/AcessoService")

public class AcessoService {

	private static final String SUCCESS_RESULT = "<result>success</result>";
	private static final String FAILURE_RESULT = "<result>failure</result>";	
	private static final String RESULT_BEGIN = "<result>";
	private static final String RESULT_END = "</result>";
	
    private static AcessoService instancia;
    private AcessoDAO acessoDao = AcessoDAO.getInstancia();

    private FornecedorService fornecedorService = FornecedorService.getInstancia();
    private ClienteService clienteService = ClienteService.getInstancia();

    public static AcessoService getInstancia() {

        if (instancia == null) {
            instancia = new AcessoService();
        }

        return instancia;
    }

//    private AcessoService() {
//        this.acessoDao = AcessoDAO.getInstancia();
//        this.fornecedorService = FornecedorService.getInstancia();
//        this.clienteService = ClienteService.getInstancia();
//    }

    public Autenticacao inserir(Acesso acesso, Usuario usuario) throws InfraException, NegocioException {

        Autenticacao res = null;

        if (acesso == null || usuario == null) {
            throw new NegocioException("excecao_objeto_nulo");
        }

        try {

            if (usuario instanceof Fornecedor) {
                fornecedorService.salvar((Fornecedor) usuario);
            } else {
                clienteService.salvar((Cliente) usuario);
            }

            acesso.setId(usuario.getId());
            acessoDao.inserir(acesso);

            res = new Autenticacao();
            res.setIdAcesso(acesso.getId());
            res.setToken("");

        } catch (Exception e) {
            throw new InfraException(e.getMessage(), e);
        }

        return res;
    }
    
    @POST
    @Path("/acessosFornecedor")
    @Produces(MediaType.APPLICATION_XML)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)    
    public String inserirAcessoFornecedor(@FormParam("login") String login, 
    		@FormParam("senha") String senha, @FormParam("email") String email, 
    		@FormParam("endereco") String endereco, @FormParam("habilitado") String habilitado, 
    		@FormParam("nome") String nome, @FormParam("telefone") int telefone, 
    		@FormParam("cnpj") String cnpj, @Context HttpServletResponse servletResponse) throws IOException  {
    	
    	String res = FAILURE_RESULT;
    	
    	//Acesso
    	Acesso acesso = new Acesso();
    	acesso.setLogin(login);
    	acesso.setSenha(senha);
    	
    	//Fornecedor
    	Fornecedor fornecedor = new Fornecedor();
    	fornecedor.setEmail(email);
    	fornecedor.setEndereco(endereco);
    	boolean h = false;
    	if (habilitado != null && habilitado.equals("true")) {
    		h = true;
    	}
    	fornecedor.setHabilitado(h);
    	fornecedor.setNome(nome);
    	fornecedor.setTelefone(telefone);
    	fornecedor.setCnpj(cnpj);
    	
    	try {
    		fornecedorService.salvar(fornecedor);
    			
    		acesso.setId(fornecedor.getId());
            acessoDao.inserir(acesso);
            
            res = RESULT_BEGIN + fornecedor.getId() + RESULT_END;
    			
	    } catch (Exception e) {
	    	
	        //throw new InfraException(e.getMessage(), e);
	    }
    	    	   
    	return res;    	
    }
    
    @POST
    @Path("/acessosCliente")
    @Produces(MediaType.APPLICATION_XML)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)    
    public String inserirAcessoCliente(@FormParam("login") String login, 
    		@FormParam("senha") String senha, @FormParam("email") String email, 
    		@FormParam("endereco") String endereco, @FormParam("habilitado") String habilitado, 
    		@FormParam("nome") String nome, @FormParam("telefone") int telefone, 
    		@FormParam("cpf") String cpf, @Context HttpServletResponse servletResponse) throws IOException {
    	
    	String res = FAILURE_RESULT;
    	
    	//Acesso
    	Acesso acesso = new Acesso();
    	acesso.setLogin(login);
    	acesso.setSenha(senha);
    	
    	//Cliente
    	Cliente cliente = new Cliente();
    	cliente.setEmail(email);
    	cliente.setEndereco(endereco);
    	boolean h = false;
    	if (habilitado != null && habilitado.equals("true")) {
    		h = true;
    	}
    	cliente.setHabilitado(h);
    	cliente.setNome(nome);
    	cliente.setTelefone(telefone);
    	cliente.setCpf(cpf);
    	
    	try {
    		clienteService.salvar(cliente);
    			
    		acesso.setId(cliente.getId());
            acessoDao.inserir(acesso);
            
            res = RESULT_BEGIN + cliente.getId() + RESULT_END;
    			
	    } catch (Exception e) {
	    	
	        //throw new InfraException(e.getMessage(), e);
	    }
    	    	   
    	return res;    	
    }    
    
    public Autenticacao atualizar(Acesso acesso, Usuario usuario) throws InfraException, NegocioException {

        Autenticacao res = null;

        if (acesso == null || usuario == null) {
            throw new NegocioException("excecao_objeto_nulo");
        }

        try {

            if (usuario instanceof Fornecedor) {
                fornecedorService.salvar((Fornecedor) usuario);
            } else {
                clienteService.salvar((Cliente) usuario);
            }

            acessoDao.atualizar(acesso);

            res = new Autenticacao();
            res.setIdAcesso(acesso.getId());
            res.setToken("");

        } catch (Exception e) {
            throw new InfraException(e.getMessage(), e);
        }

        return res;
    }

    public Autenticacao buscarPorLoginPorSenhaFornecedor(String login, String senha) throws InfraException, NegocioException {

        Autenticacao res = null;

        try {

            long id = acessoDao.buscarPorLoginPorSenhaFornecedor(login, senha);

            if (id <= 0) {
                throw new NegocioException("excecao_objeto_nao_encontrado");
            }

            res = new Autenticacao();
            res.setIdAcesso(id);
            res.setToken("");

        } catch (NegocioException e) {
            throw  e;
        } catch (Exception e) {
            throw new InfraException(e.getMessage(), e);
        }

        return res;

    }
    
    @GET
    @Path("/acessosLoginSenhaFornecedor/{login}/{senha}")
    @Produces(MediaType.APPLICATION_JSON)
    public Autenticacao buscarPorLoginPorSenhaForn(@PathParam("login") String login, 
    		@PathParam("senha") String senha) throws InfraException, NegocioException {

    	Autenticacao res = null;

        try {

            long id = acessoDao.buscarPorLoginPorSenhaFornecedor(login, senha);

            if (id <= 0) {
                throw new NegocioException("excecao_objeto_nao_encontrado");
            }

            res = new Autenticacao();
            res.setIdAcesso(id);
            res.setToken("");

        } catch (NegocioException e) {
            throw  e;
        } catch (Exception e) {
            throw new InfraException(e.getMessage(), e);
        }

        return res;

    }    

    public Autenticacao buscarPorLoginPorSenhaCliente(String login, String senha) throws InfraException, NegocioException {

        Autenticacao res = null;

        try {

            long id = acessoDao.buscarPorLoginPorSenhaCliente(login, senha);

            if (id <= 0) {
                throw new NegocioException("excecao_objeto_nao_encontrado");
            }

            res = new Autenticacao();
            res.setIdAcesso(id);
            res.setToken("");

        } catch (NegocioException e) {
            throw  e;
        } catch (Exception e) {
            throw new InfraException(e.getMessage(), e);
        }

        return res;

    }
    
    @GET
    @Path("/acessosLoginSenhaCliente/{login}/{senha}")
    @Produces(MediaType.APPLICATION_JSON)
    public Autenticacao buscarPorLoginPorSenhaClient(@PathParam("login") String login, 
    		@PathParam("senha") String senha) throws InfraException, NegocioException {

        Autenticacao res = null;

        try {

            long id = acessoDao.buscarPorLoginPorSenhaCliente(login, senha);

            if (id <= 0) {
                throw new NegocioException("excecao_objeto_nao_encontrado");
            }

            res = new Autenticacao();
            res.setIdAcesso(id);
            res.setToken("");

        } catch (NegocioException e) {
            throw  e;
        } catch (Exception e) {
            throw new InfraException(e.getMessage(), e);
        }

        return res;

    }    

    public boolean existePorLogin(String login) throws InfraException {

        boolean res = false;

        try {

            res = acessoDao.existePorLogin(login);

        } catch (Exception e) {
            throw new InfraException(e.getMessage(), e);
        }

        return res;
    }
    
    @POST
    @Path("/liberadoLogin")
    @Produces(MediaType.APPLICATION_XML)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)    
    public String liberadoLogin(@FormParam("login") String login, 
    		@Context HttpServletResponse servletResponse) throws IOException {    
    	
    	String res = FAILURE_RESULT;    	   
    	
    	try {

            if (!acessoDao.existePorLogin(login)) {
            	res = SUCCESS_RESULT;
            }

        } catch (Exception e) {
            //throw new InfraException(e.getMessage(), e);
        }
    	    	
    	return res;    	
    }
    
    public Acesso consultar(long id) throws InfraException, NegocioException {

        Acesso res = null;

        try {

            res = acessoDao.consultar(id);

            if (res == null) {
                throw new NegocioException("excecao_objeto_nao_encontrado");
            }
        } catch (NegocioException e) {
            throw  e;
        } catch (Exception e) {
            throw new InfraException(e.getMessage(), e);
        }

        return res;

    }    
    
    public void excluirCliente(Acesso acesso) throws InfraException {

        try {

        	acessoDao.excluir(acesso);
        	        	            
            clienteService.excluir(clienteService.consultar(acesso.getId()));

        } catch (Exception e) {
            throw new InfraException(e.getMessage(), e);
        }

    }
    
    public void excluirFornecedor(Acesso acesso) throws InfraException {

        try {

        	acessoDao.excluir(acesso);
        	
        	fornecedorService.excluir(fornecedorService.consultar(acesso.getId()));      	

        } catch (Exception e) {
            throw new InfraException(e.getMessage(), e);
        }

    }    
    

    public List<Acesso> listar() throws InfraException {

        try {
            return acessoDao.listar();
        }catch (Exception e){
            throw new InfraException(e.getMessage(),e);
        }
    }
}
