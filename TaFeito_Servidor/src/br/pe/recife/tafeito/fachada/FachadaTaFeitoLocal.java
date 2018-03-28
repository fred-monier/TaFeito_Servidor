package br.pe.recife.tafeito.fachada;

import java.util.List;

import br.pe.recife.tafeito.excecao.InfraException;
import br.pe.recife.tafeito.excecao.NegocioException;
import br.pe.recife.tafeito.negocio.Acesso;
import br.pe.recife.tafeito.negocio.Autenticacao;
import br.pe.recife.tafeito.negocio.Cliente;
import br.pe.recife.tafeito.negocio.Fornecedor;
import br.pe.recife.tafeito.negocio.ServicoCategoria;
import br.pe.recife.tafeito.negocio.Usuario;
import br.pe.recife.tafeito.service.AcessoService;
import br.pe.recife.tafeito.service.ClienteService;
import br.pe.recife.tafeito.service.FornecedorService;

public class FachadaTaFeitoLocal implements IFachadaTaFeito {
	
    private static FachadaTaFeitoLocal instancia;

    private AcessoService acessoService;
    private FornecedorService fornecedorService;
    private ClienteService clienteService;
    
    public static FachadaTaFeitoLocal getInstancia(){

        if(instancia == null) {
            instancia = new FachadaTaFeitoLocal();
        }

        return instancia;
    }

    private FachadaTaFeitoLocal() {

        this.acessoService = AcessoService.getInstancia();
        this.fornecedorService = FornecedorService.getInstancia();
        this.clienteService = ClienteService.getInstancia();
        
    }    

    //Comentei devido às regras de negócio (além do simples teste do BD local) para inclusão de Acesso, Usuário, Cliente e Fornecedor
	@Override
	public Autenticacao inserirAcesso(Acesso acesso, Usuario usuario) throws InfraException, NegocioException {
		
		//return this.acessoService.inserir(acesso, usuario);
		return null;
	}

	//Comentei devido às regras de negócio (além do simples teste do BD local) para inclusão de Acesso, Usuário, Cliente e Fornecedor
	@Override
	public Autenticacao atualizarAcesso(Acesso acesso, Usuario usuario) throws InfraException, NegocioException {
		
		//return this.acessoService.inserir(acesso, usuario);
		return null;
	}

	//Comentei devido às regras de negócio (além do simples teste do BD local) para inclusão de Acesso, Usuário, Cliente e Fornecedor
	@Override
	public Autenticacao buscarPorLoginPorSenhaFornecedorAcesso(String login, String senha)
			throws InfraException, NegocioException {
		
		//return this.acessoService.buscarPorLoginPorSenhaFornecedor(login, senha);
		return null;
	}

	//Comentei devido às regras de negócio (além do simples teste do BD local) para inclusão de Acesso, Usuário, Cliente e Fornecedor
	@Override
	public Autenticacao buscarPorLoginPorSenhaClienteAcesso(String login, String senha)
			throws InfraException, NegocioException {
		
		//return this.acessoService.buscarPorLoginPorSenhaCliente(login, senha);
		return null;
	}

	@Override
	public boolean existePorLoginAcesso(String login) throws InfraException {
		
		return this.acessoService.existePorLogin(login);
	}
	
	@Override
	public Acesso consultarAcesso(long id, Autenticacao autenticacao) throws InfraException, NegocioException {
		
		return this.acessoService.consultar(id);
	}

	//Comentei devido às regras de negócio (além do simples teste do BD local) para inclusão de Acesso, Usuário, Cliente e Fornecedor
	@Override
	public void excluirAcessoCliente(Acesso acesso, Autenticacao autenticacao) throws InfraException {
		
		//this.acessoService.excluirCliente(acesso);
	}

	//Comentei devido às regras de negócio (além do simples teste do BD local) para inclusão de Acesso, Usuário, Cliente e Fornecedor
	@Override
	public void excluirAcessoFornecedor(Acesso acesso, Autenticacao autenticacao) throws InfraException {
		
		//this.acessoService.excluirFornecedor(acesso);
	}

	@Override
	public List<Acesso> listarAcesso() throws InfraException {
		
		return this.acessoService.listar();
	}

	//Comentei devido às regras de negócio (além do simples teste do BD local) para inclusão de Acesso, Usuário, Cliente e Fornecedor
	@Override
	public void salvarFornecedor(Fornecedor fornecedor, Autenticacao autenticacao)
			throws InfraException, NegocioException {
		
		//this.fornecedorService.salvar(fornecedor);
		
	}

	@Override
	public Fornecedor consultarFornecedor(long id, Autenticacao autenticacao) throws InfraException, NegocioException {
		
		return this.fornecedorService.consultar(id);
	}

	@Override
	public void excluirFornecedor(Fornecedor fornecedor, Autenticacao autenticacao)
			throws InfraException {
		
		this.fornecedorService.excluir(fornecedor);
	}

	@Override
	public List<Fornecedor> listarFornecedor(Autenticacao autenticacao) throws InfraException {
		
		return this.fornecedorService.listar();
	}

	@Override
	public List<Fornecedor> listarPorServicoCategoriaFornecedor(ServicoCategoria servicoCategoria,
			Autenticacao autenticacao) throws InfraException {
		
		return this.fornecedorService.listarPorServicoCategoria(servicoCategoria);
	}

	//Comentei devido às regras de negócio (além do simples teste do BD local) para inclusão de Acesso, Usuário, Cliente e Fornecedor
	@Override
	public void salvarCliente(Cliente cliente, Autenticacao autenticacao) throws InfraException, NegocioException {
		
		//this.clienteService.salvar(cliente);		
	}

	@Override
	public Cliente consultarCliente(long id, Autenticacao autenticacao) throws InfraException, NegocioException {
		
		return this.clienteService.consultar(id);
	}

	@Override
	public void excluirCliente(Cliente cliente, Autenticacao autenticacao) throws InfraException {
		
		this.clienteService.excluir(cliente);		
	}

	@Override
	public List<Cliente> listarCliente(Autenticacao autenticacao) throws InfraException {
		
		return this.clienteService.listar();
	}
	
	
		
}
