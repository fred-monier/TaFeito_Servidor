package br.pe.recife.tafeito.service;

import java.util.List;

import br.pe.recife.tafeito.dao.ClienteDAO;
import br.pe.recife.tafeito.excecao.InfraException;
import br.pe.recife.tafeito.excecao.NegocioException;
import br.pe.recife.tafeito.negocio.Acesso;
import br.pe.recife.tafeito.negocio.Cliente;

public class ClienteService {

    private static ClienteService instancia;

    private ClienteDAO clienteDao;

    private UsuarioService usuarioService;
    private AcessoService acessoService;

    public static ClienteService getInstancia() {

        if (instancia == null) {
            instancia = new ClienteService();
        }

        return instancia;
    }

    private ClienteService() {
        this.clienteDao = ClienteDAO.getInstancia();
        
        this.usuarioService = UsuarioService.getInstancia();
        this.acessoService = AcessoService.getInstancia();       
    }

    //REVISADO OK
    public void salvar(Cliente cliente, Acesso acesso) throws InfraException, NegocioException {

        if (cliente == null) {
            throw new NegocioException("excecao_objeto_nulo");
        }

        try {

            boolean novo = false;
            if (cliente.getId() == 0) {
                novo = true;
            }

            usuarioService.salvar(cliente);
            clienteDao.salvar(cliente, novo);
            acesso.setId(cliente.getId());
            acessoService.salvar(acesso, novo);

        } catch (Exception e) {
            throw new InfraException(e.getMessage(), e);
        }
    }

    public Cliente consultar(long id) throws InfraException, NegocioException {

        Cliente res = null;

        try {

            res = clienteDao.consultar(id);

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

    public void excluir(Cliente cliente) throws InfraException {

        try {

        	clienteDao.excluir(cliente);
        	usuarioService.excluir(cliente); 
        	acessoService.excluir(acessoService.consultar(cliente.getId()));
        	

        } catch (Exception e) {
            throw new InfraException(e.getMessage(), e);
        }

    }
    
    public List<Cliente> listar() throws InfraException{

        try {
            return clienteDao.listar();
        }catch (Exception e){
            throw new InfraException(e.getMessage(),e);
        }
    }
}
