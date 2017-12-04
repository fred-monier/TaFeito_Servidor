package br.pe.recife.tafeito.service;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import br.pe.recife.tafeito.dao.FornecedorDAO;
import br.pe.recife.tafeito.excecao.InfraException;
import br.pe.recife.tafeito.excecao.NegocioException;
import br.pe.recife.tafeito.negocio.Fornecedor;
import br.pe.recife.tafeito.negocio.ServicoCategoria;
@Path("/FornecedorService")

public class FornecedorService {

    private static FornecedorService instancia;

    private FornecedorDAO fornecedorDao = FornecedorDAO.getInstancia();

    private UsuarioService usuarioService = UsuarioService.getInstancia();

    public static FornecedorService getInstancia() {

        if (instancia == null) {
            instancia = new FornecedorService();
        }

        return instancia;
    }

//    private FornecedorService() {
//        this.fornecedorDao = FornecedorDAO.getInstancia();
//        this.usuarioService = UsuarioService.getInstancia();
//    }

    public void salvar(Fornecedor fornecedor) throws InfraException, NegocioException {

        if(fornecedor == null) {
            throw new NegocioException("excecao_objeto_nulo");
        }

        try {

            boolean novo = false;
            if (fornecedor.getId() == 0) {
                novo = true;
            }

            usuarioService.salvar(fornecedor);
            fornecedorDao.salvar(fornecedor, novo);

        } catch (Exception e) {
            throw new InfraException(e.getMessage(), e);
        }
    }

    @GET
    @Path("/fornecedores/{id}")
    public Fornecedor consultar(@PathParam("id") long id) throws InfraException, NegocioException {

        Fornecedor res = null;

        try {

            res = fornecedorDao.consultar(id);

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

    public void excluir(Fornecedor fornecedor) throws InfraException {

        try {

            //res = fornecedorDao.excluir(fornecedor);
            //if (res <= 0) {
            //    throw new NegocioException("excecao_objeto_nao_excluido");
            //}
            fornecedorDao.excluir(fornecedor);
                        
            //res = usuarioService.excluir(fornecedor);
            //if (res <= 0) {
            //    throw new NegocioException("excecao_objeto_nao_excluido");
            //}
            usuarioService.excluir(fornecedor);

        } catch (Exception e) {
            throw new InfraException(e.getMessage(), e);
        }

    }

    @GET 
    @Path("/fornecedores") 
    @Produces(MediaType.APPLICATION_XML)      
    public List<Fornecedor> listar() throws InfraException{

        try {
            return fornecedorDao.listar();
        }catch (Exception e){
            throw new InfraException(e.getMessage(),e);
        }
    }


    public List<Fornecedor> listarPorServicoCategoria(ServicoCategoria servCat) throws InfraException{

        try {
            return fornecedorDao.listarPorServicoCategoria(servCat);
        }catch (Exception e){
            throw new InfraException(e.getMessage(),e);
        }
    }
}
