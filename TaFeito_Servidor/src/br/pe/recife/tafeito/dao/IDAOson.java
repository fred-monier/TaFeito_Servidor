package br.pe.recife.tafeito.dao;

import java.util.List;

import br.pe.recife.tafeito.excecao.InfraException;

public interface IDAOson<T> {

    void salvar(T entidade, boolean novo) throws InfraException;
    void excluir(T entidade) throws InfraException;
    T consultar(long id) throws InfraException;
    List<T> listar() throws InfraException;
}
