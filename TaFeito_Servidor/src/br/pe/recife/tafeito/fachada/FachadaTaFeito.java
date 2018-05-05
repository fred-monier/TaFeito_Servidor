package br.pe.recife.tafeito.fachada;

import br.pe.recife.tafeito.excecao.InfraException;
import br.pe.recife.tafeito.excecao.NegocioException;
import br.pe.recife.tafeito.negocio.Acesso;
import br.pe.recife.tafeito.negocio.Cliente;
import br.pe.recife.tafeito.negocio.Fornecedor;
import br.pe.recife.tafeito.service.AcessoService;
import br.pe.recife.tafeito.service.ClienteService;
import br.pe.recife.tafeito.service.FornecedorService;

public class FachadaTaFeito {
	
	private static FachadaTaFeito instancia;
	
    private FornecedorService fornecedorService;
    private ClienteService clienteService;
    private AcessoService acessoService;
	
    private FachadaTaFeito() {

        this.fornecedorService = FornecedorService.getInstancia();
        this.clienteService = ClienteService.getInstancia(); 
        this.acessoService = AcessoService.getInstancia();
    }       
    
    public static FachadaTaFeito getInstancia(){

        if(instancia == null) {
            instancia = new FachadaTaFeito();
        }

        return instancia;
    }
    
    //FornecedorService
	public void inserirUsuarioFornecedorAcesso(Fornecedor fornecedor, Acesso acesso) throws InfraException, NegocioException {
		
		this.fornecedorService.salvar(fornecedor, acesso);
	}    
	
	//ClienteService
	public void inserirUsuarioClienteAcesso(Cliente cliente, Acesso acesso) throws InfraException, NegocioException {
		
		this.clienteService.salvar(cliente, acesso);
	}  	
	
	//AcessoService
	public boolean existeAcessoPorLogin(String login) throws InfraException {
		
		return this.acessoService.existePorLogin(login);
	}
	
	public long autenticarClienteAcesso(Acesso acesso) throws InfraException, NegocioException {
		return this.acessoService.buscarPorLoginPorSenhaCliente(acesso.getLogin(), acesso.getSenha());		
	}
	
	public long autenticarFornecedorAcesso(Acesso acesso) throws InfraException, NegocioException {
		return this.acessoService.buscarPorLoginPorSenhaFornecedor(acesso.getLogin(), acesso.getSenha());		
	}

}
