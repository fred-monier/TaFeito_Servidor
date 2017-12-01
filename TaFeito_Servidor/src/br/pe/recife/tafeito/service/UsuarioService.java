package br.pe.recife.tafeito.service;

import java.util.List;

import br.pe.recife.tafeito.dao.UsuarioDAO;
import br.pe.recife.tafeito.excecao.InfraException;
import br.pe.recife.tafeito.excecao.NegocioException;
import br.pe.recife.tafeito.negocio.Usuario;

public class UsuarioService {

    private static UsuarioService instancia;
    private UsuarioDAO usuarioDao;

    public static UsuarioService getInstancia() {

        if (instancia == null) {
            instancia = new UsuarioService();
        }

        return instancia;
    }

    private UsuarioService() {
        this.usuarioDao = UsuarioDAO.getInstancia();
    }

    public void salvar(Usuario usuario) throws InfraException, NegocioException {

        if(usuario == null) {
            throw new NegocioException("excecao_objeto_nulo");
        }

        try {
            usuarioDao.salvar(usuario);
        } catch (Exception e) {
            throw new InfraException(e.getMessage(), e);
        }
    }

    public Usuario consultar(long id) throws InfraException, NegocioException {

        Usuario res = null;

        try {

            res = usuarioDao.consultar(id);

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

    public void excluir(Usuario usuario) throws InfraException {

        try {

            usuarioDao.excluir(usuario);

            //if (res <= 0) {
            //    throw new NegocioException("excecao_objeto_nao_excluido");
            //}
            
        } catch (Exception e) {
            throw new InfraException(e.getMessage(), e);
        }

    }

    public List<Usuario> listar() throws InfraException{

        try {
            return usuarioDao.listar();
        }catch (Exception e){
            throw new InfraException(e.getMessage(),e);
        }
    }
}
