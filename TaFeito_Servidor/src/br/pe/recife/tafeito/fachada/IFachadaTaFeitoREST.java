package br.pe.recife.tafeito.fachada;

import java.io.IOException;
import java.util.List;

import br.pe.recife.tafeito.excecao.InfraException;
import br.pe.recife.tafeito.excecao.NegocioException;
import br.pe.recife.tafeito.negocio.Autenticacao;
import br.pe.recife.tafeito.negocio.Cliente;
import br.pe.recife.tafeito.negocio.Fornecedor;
import br.pe.recife.tafeito.negocio.ServicoCategoria;

public interface IFachadaTaFeitoREST {
	
	//AcessoService	
	//*** I
    //Autenticacao inserirAcesso(Acesso acesso, Usuario usuario) throws InfraException, NegocioException;    
    String inserirAcessoFornecedor(String login, String senha, 
    		String email, String endereco, String habilitado, String nome, int telefone, String cnpj) throws IOException;//OK - /acessosFornecedor
    	//POST @Path("/acessosFornecedor")
    String inserirAcessoCliente(String login, String senha, 
    		String email, String endereco, String habilitado, String nome, int telefone, String cpf) throws IOException;//OK - /acessosCliente
    	//POST @Path("/acessosCliente")
    //*** F       
    
    //Autenticacao atualizarAcesso(Acesso acesso, Usuario usuario) throws InfraException, NegocioException;
    
    //*** I
    //Autenticacao buscarPorLoginPorSenhaFornecedorAcesso(String login, String senha) throws InfraException, NegocioException;
    Autenticacao buscarPorLoginPorSenhaForn(String login, String senha) throws InfraException, NegocioException;
    	//GET @Path("/acessosLoginSenhaFornecedor/{login}/{senha}")
    //*** F
    
    // *** I
    //Autenticacao buscarPorLoginPorSenhaClienteAcesso(String login, String senha) throws InfraException, NegocioException;
    Autenticacao buscarPorLoginPorSenhaClient(String login, String senha) throws InfraException, NegocioException;
    	//GET @Path("/acessosLoginSenhaCliente/{login}/{senha}")
    //*** F
    
    //*** I
    //boolean existePorLoginAcesso(String login) throws InfraException;
    String liberadoLogin(String login) throws IOException;
    	//POST @Path("/liberadoLogin")
    //*** F
    
    //Acesso consultarAcesso(long id, Autenticacao autenticacao) throws InfraException, NegocioException;
    //void excluirAcessoCliente(Acesso acesso, Autenticacao autenticacao) throws InfraException;
    //void excluirAcessoFornecedor(Acesso acesso, Autenticacao autenticacao) throws InfraException;
    //List<Acesso> listarAcesso() throws InfraException;

    //FornecedorService
    //void salvarFornecedor(Fornecedor fornecedor, Autenticacao autenticacao) throws InfraException, NegocioException;
    Fornecedor consultarFornecedor(long id, Autenticacao autenticacao) throws InfraException, NegocioException;//OK - /fornecedores/{id}
    //void excluirFornecedor(Fornecedor fornecedor, Autenticacao autenticacao) throws InfraException;
    List<Fornecedor> listarFornecedor(Autenticacao autenticacao) throws InfraException;//OK - NP - /fornecedores
    List<Fornecedor> listarPorServicoCategoriaFornecedor(ServicoCategoria servicoCategoria, Autenticacao autenticacao) throws InfraException;//********

    
    //ClienteService
    //void salvarCliente(Cliente cliente, Autenticacao autenticacao) throws InfraException, NegocioException;
    Cliente consultarCliente(long id, Autenticacao autenticacao) throws InfraException, NegocioException;//OK - /clientes/{id}
    //void excluirCliente(Cliente cliente, Autenticacao autenticacao) throws InfraException;
    List<Cliente> listarCliente(Autenticacao autenticacao) throws InfraException;//OK - NP - /clientes
	

}
