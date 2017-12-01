package br.pe.recife.tafeito.service;

import java.util.List;

import br.pe.recife.tafeito.dao.AcessoDAO;
import br.pe.recife.tafeito.excecao.InfraException;
import br.pe.recife.tafeito.excecao.NegocioException;
import br.pe.recife.tafeito.negocio.Acesso;
import br.pe.recife.tafeito.negocio.Autenticacao;
import br.pe.recife.tafeito.negocio.Cliente;
import br.pe.recife.tafeito.negocio.Fornecedor;
import br.pe.recife.tafeito.negocio.Usuario;

public class AcessoService {

    private static AcessoService instancia;
    private AcessoDAO acessoDao;

    private FornecedorService fornecedorService;
    private ClienteService clienteService;

    public static AcessoService getInstancia() {

        if (instancia == null) {
            instancia = new AcessoService();
        }

        return instancia;
    }

    private AcessoService() {
        this.acessoDao = AcessoDAO.getInstancia();
        this.fornecedorService = FornecedorService.getInstancia();
        this.clienteService = ClienteService.getInstancia();
    }

    public Autenticacao inserir(Acesso acesso, Usuario usuario) throws InfraException, NegocioException {

        Autenticacao res = null;

        if (acesso == null || usuario == null) {
            throw new NegocioException("excecao_objeto_nulo");
        }

        try {

            if (usuario instanceof Fornecedor) {
                fornecedorService.salvar((Fornecedor) usuario);
            } else {
                clienteService.salvar((Cliente) usuario);
            }

            acesso.setId(usuario.getId());
            acessoDao.inserir(acesso);

            res = new Autenticacao();
            res.setIdAcesso(acesso.getId());
            res.setToken("");

        } catch (Exception e) {
            throw new InfraException(e.getMessage(), e);
        }

        return res;
    }

    public Autenticacao atualizar(Acesso acesso, Usuario usuario) throws InfraException, NegocioException {

        Autenticacao res = null;

        if (acesso == null || usuario == null) {
            throw new NegocioException("excecao_objeto_nulo");
        }

        try {

            if (usuario instanceof Fornecedor) {
                fornecedorService.salvar((Fornecedor) usuario);
            } else {
                clienteService.salvar((Cliente) usuario);
            }

            acessoDao.atualizar(acesso);

            res = new Autenticacao();
            res.setIdAcesso(acesso.getId());
            res.setToken("");

        } catch (Exception e) {
            throw new InfraException(e.getMessage(), e);
        }

        return res;
    }

    public Autenticacao buscarPorLoginPorSenhaFornecedor(String login, String senha) throws InfraException, NegocioException {

        Autenticacao res = null;

        try {

            long id = acessoDao.buscarPorLoginPorSenhaFornecedor(login, senha);

            if (id <= 0) {
                throw new NegocioException("excecao_objeto_nao_encontrado");
            }

            res = new Autenticacao();
            res.setIdAcesso(id);
            res.setToken("");

        } catch (NegocioException e) {
            throw  e;
        } catch (Exception e) {
            throw new InfraException(e.getMessage(), e);
        }

        return res;

    }

    public Autenticacao buscarPorLoginPorSenhaCliente(String login, String senha) throws InfraException, NegocioException {

        Autenticacao res = null;

        try {

            long id = acessoDao.buscarPorLoginPorSenhaCliente(login, senha);

            if (id <= 0) {
                throw new NegocioException("excecao_objeto_nao_encontrado");
            }

            res = new Autenticacao();
            res.setIdAcesso(id);
            res.setToken("");

        } catch (NegocioException e) {
            throw  e;
        } catch (Exception e) {
            throw new InfraException(e.getMessage(), e);
        }

        return res;

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

    public List<Acesso> listar() throws InfraException {

        try {
            return acessoDao.listar();
        }catch (Exception e){
            throw new InfraException(e.getMessage(),e);
        }
    }
}
