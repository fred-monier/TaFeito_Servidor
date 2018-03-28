package br.pe.recife.tafeito.service;

import java.util.List;

import br.pe.recife.tafeito.dao.AcessoDAO;
import br.pe.recife.tafeito.excecao.InfraException;
import br.pe.recife.tafeito.excecao.NegocioException;
import br.pe.recife.tafeito.negocio.Acesso;

public class AcessoService {
	
    private static AcessoService instancia;
    private AcessoDAO acessoDao;

    public static AcessoService getInstancia() {

        if (instancia == null) {
            instancia = new AcessoService();
        }

        return instancia;
    }

    private AcessoService() {
        this.acessoDao = AcessoDAO.getInstancia();
    }	
    
    public void salvar(Acesso acesso, boolean novo) throws InfraException, NegocioException {

        if(acesso == null) {
            throw new NegocioException("excecao_objeto_nulo");
        }

        try {
            
            acessoDao.salvar(acesso, novo);

        } catch (Exception e) {
            throw new InfraException(e.getMessage(), e);
        }
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
    
    public void excluir(Acesso acesso) throws InfraException {

        try {

            acessoDao.excluir(acesso);

        } catch (Exception e) {
            throw new InfraException(e.getMessage(), e);
        }

    }
    
    public List<Acesso> listar() throws InfraException{

        try {
            return acessoDao.listar();
        }catch (Exception e){
            throw new InfraException(e.getMessage(),e);
        }
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
    
    public long buscarPorLoginPorSenhaFornecedor(String login, String senha) throws InfraException {
    	
    	long id;
    	
    	try {
    	
    		id = acessoDao.buscarPorLoginPorSenhaFornecedor(login, senha);
    			
	    } catch (Exception e) {
	        throw new InfraException(e.getMessage(), e);
	    }    			
    			
    	return id;
    }
    
    public long buscarPorLoginPorSenhaCliente(String login, String senha) throws InfraException {
    	
    	long id;
    	
    	try {
    	
    		id = acessoDao.buscarPorLoginPorSenhaCliente(login, senha);
    			
	    } catch (Exception e) {
	        throw new InfraException(e.getMessage(), e);
	    }    			
    			
    	return id;
    }
     
}
