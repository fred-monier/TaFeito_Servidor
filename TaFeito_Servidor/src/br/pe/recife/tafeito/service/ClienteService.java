package br.pe.recife.tafeito.service;

import java.util.List;

import br.pe.recife.tafeito.dao.ClienteDAO;
import br.pe.recife.tafeito.excecao.InfraException;
import br.pe.recife.tafeito.excecao.NegocioException;
import br.pe.recife.tafeito.negocio.Cliente;

public class ClienteService {

    private static ClienteService instancia;

    private ClienteDAO clienteDao;

    private UsuarioService usuarioService;

    public static ClienteService getInstancia() {

        if (instancia == null) {
            instancia = new ClienteService();
        }

        return instancia;
    }

    private ClienteService() {
        this.clienteDao = ClienteDAO.getInstancia();
        this.usuarioService = UsuarioService.getInstancia();
    }

    public void salvar(Cliente cliente) throws InfraException, NegocioException {

        if(cliente == null) {
            throw new NegocioException("excecao_objeto_nulo");
        }

        try {

            boolean clienteNovo = false;
            if (cliente.getId() == 0) {
                clienteNovo = true;
            }

            usuarioService.salvar(cliente);
            clienteDao.salvar(cliente, clienteNovo);

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

            //res = clienteDao.excluir(cliente);
            //if (res <= 0) {
            //    throw new NegocioException("excecao_objeto_nao_excluido");
            //}
        	clienteDao.excluir(cliente);

            //res = usuarioService.excluir(cliente);
            //if (res <= 0) {
            //    throw new NegocioException("excecao_objeto_nao_excluido");
            //}
        	usuarioService.excluir(cliente);

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
