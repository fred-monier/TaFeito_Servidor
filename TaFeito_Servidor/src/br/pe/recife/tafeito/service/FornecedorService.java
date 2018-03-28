package br.pe.recife.tafeito.service;

import java.util.List;

import br.pe.recife.tafeito.dao.FornecedorDAO;
import br.pe.recife.tafeito.excecao.InfraException;
import br.pe.recife.tafeito.excecao.NegocioException;
import br.pe.recife.tafeito.negocio.Acesso;
import br.pe.recife.tafeito.negocio.Fornecedor;
import br.pe.recife.tafeito.negocio.ServicoCategoria;

public class FornecedorService {

    private static FornecedorService instancia;

    private FornecedorDAO fornecedorDao;

    private UsuarioService usuarioService;    
    private AcessoService acessoService;

    public static FornecedorService getInstancia() {

        if (instancia == null) {
            instancia = new FornecedorService();
        }

        return instancia;
    }

    private FornecedorService() {
        this.fornecedorDao = FornecedorDAO.getInstancia();
        
        this.usuarioService = UsuarioService.getInstancia();
        this.acessoService = AcessoService.getInstancia();
    }

    //REVISADO OK
    public void salvar(Fornecedor fornecedor, Acesso acesso) throws InfraException, NegocioException {

        if (fornecedor == null) {
            throw new NegocioException("excecao_objeto_nulo");
        }

        try {

            boolean novo = false;
            if (fornecedor.getId() == 0) {
                novo = true;
            }

            usuarioService.salvar(fornecedor);
            fornecedorDao.salvar(fornecedor, novo);
            acesso.setId(fornecedor.getId());
            acessoService.salvar(acesso, novo);

        } catch (Exception e) {
            throw new InfraException(e.getMessage(), e);
        }
    }

    public Fornecedor consultar(long id) throws InfraException, NegocioException {

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

            fornecedorDao.excluir(fornecedor);                        
            usuarioService.excluir(fornecedor);
            acessoService.excluir(acessoService.consultar(fornecedor.getId()));
            
        } catch (Exception e) {
            throw new InfraException(e.getMessage(), e);
        }

    }
 
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
