package br.pe.recife.tafeito.service;

import java.util.List;
import javax.ws.rs.GET; 
import javax.ws.rs.Path; 
import javax.ws.rs.Produces; 
import javax.ws.rs.core.MediaType; 

import br.pe.recife.tafeito.dao.UsuarioDAO;
import br.pe.recife.tafeito.excecao.InfraException;
import br.pe.recife.tafeito.excecao.NegocioException;
import br.pe.recife.tafeito.negocio.Usuario;
@Path("/UsuarioService")
 
public class UsuarioService {

    private static UsuarioService instancia;
    private UsuarioDAO usuarioDao = UsuarioDAO.getInstancia();

    public static UsuarioService getInstancia() {

        if (instancia == null) {
            instancia = new UsuarioService();
        }

        return instancia;
    }
    
//    public UsuarioService() {
//        this.usuarioDao = UsuarioDAO.getInstancia();
//    }

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

    
    @GET 
    @Path("/usuarios") 
    @Produces(MediaType.APPLICATION_XML)      
    public List<Usuario> listar() throws InfraException{

        try {
            return usuarioDao.listar();
        }catch (Exception e){
            throw new InfraException(e.getMessage(),e);
        }
    }
}
