package br.pe.recife.tafeito.service;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import br.pe.recife.tafeito.dao.ClienteDAO;
import br.pe.recife.tafeito.excecao.InfraException;
import br.pe.recife.tafeito.excecao.NegocioException;
import br.pe.recife.tafeito.negocio.Cliente;
@Path("/ClienteService")

public class ClienteService {

    private static ClienteService instancia;

    private ClienteDAO clienteDao = ClienteDAO.getInstancia();

    private UsuarioService usuarioService = UsuarioService.getInstancia();

    public static ClienteService getInstancia() {

        if (instancia == null) {
            instancia = new ClienteService();
        }

        return instancia;
    }

//    private ClienteService() {
//        this.clienteDao = ClienteDAO.getInstancia();
//        this.usuarioService = UsuarioService.getInstancia();        
//    }

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

    @GET
    @Path("/clientes/{id}")
    public Cliente consultar(@PathParam("id") long id) throws InfraException, NegocioException {

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

    @GET 
    @Path("/clientes") 
    @Produces(MediaType.APPLICATION_XML)      
    public List<Cliente> listar() throws InfraException{

        try {
            return clienteDao.listar();
        }catch (Exception e){
            throw new InfraException(e.getMessage(),e);
        }
    }
}
