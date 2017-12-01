package br.pe.recife.tafeito.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.pe.recife.tafeito.connectionpool.Contexto;
import br.pe.recife.tafeito.excecao.ConexaoBDException;
import br.pe.recife.tafeito.excecao.InfraException;
import br.pe.recife.tafeito.negocio.Fornecedor;
import br.pe.recife.tafeito.negocio.ServicoCategoria;
import br.pe.recife.tafeito.util.DBTaFeito;

public class FornecedorDAO implements IDAOson<Fornecedor> {

    private static FornecedorDAO instancia;
    
    public static FornecedorDAO getInstancia() {

        if (instancia == null) {
            instancia = new FornecedorDAO();
        }

        return instancia;
    }

    private FornecedorDAO() {        
    }
    
    public void salvar(Fornecedor fornecedor, boolean novo) throws InfraException {
        if (novo) {
            this.inserir(fornecedor);
        } else {
            this.atualizar(fornecedor);
        }
    }
     
    private long inserir(Fornecedor fornecedor) throws InfraException {    	
    	
    	PreparedStatement pst = null;
    	ResultSet rs = null;
    	
    	long id = 0;
    	
    	Contexto contexto = new Contexto();    	
    	
    	try {    		
    	
    		DBTaFeito.beginTransacao(contexto);
    		
        	Connection con = contexto.getConexao();
        	        	
        	String sql = "INSERT INTO " + DBTaFeito.TABELA_FORNECEDOR + " VALUES (?, ?)";
        	pst = con.prepareStatement(sql, 
        			new String[] {"" + DBTaFeito.TABELA_FORNECEDOR_COLUNA_ID + ""});
        	
        	pst.setLong(1, fornecedor.getId());
        	pst.setString(2, fornecedor.getCnpj());
        	pst.executeUpdate();
        	
        	rs = pst.getGeneratedKeys();
        	if (rs.next()) {
        	   id = rs.getLong(1);
        	}     
        	
        	DBTaFeito.commitTransacao(contexto);
    	
    	} catch (ConexaoBDException e) {    		
    		throw new InfraException(e.getMessage(), e);
    		
    	} catch (SQLException e) {
    		throw new InfraException(e.getMessage(), e);
    		
    	} finally {			
			this.liberarRecursoBanco(pst);
			this.liberarRecursoBanco(rs);					
		}	
    	
    	return id;    	
    }
    
    private void atualizar(Fornecedor fornecedor) throws InfraException {    
    	
    	PreparedStatement pst = null;
    	
    	Contexto contexto = new Contexto();    	
    	
    	try {
    	
    		DBTaFeito.beginTransacao(contexto);
    		
        	Connection con = contexto.getConexao();
        	
        	String sql = "UPDATE " + DBTaFeito.TABELA_FORNECEDOR + " SET ";
        	sql = sql + DBTaFeito.TABELA_FORNECEDOR_COLUNA_CNPJ + " = ?, ";
        	sql = sql + "WHERE " + DBTaFeito.TABELA_FORNECEDOR_COLUNA_ID + " = ?";
    	
        	pst = con.prepareStatement(sql);
        	        	
        	pst.setString(1, fornecedor.getCnpj());
        	pst.setLong(2, fornecedor.getId());
        	pst.executeUpdate();
        	        	
        	DBTaFeito.commitTransacao(contexto);
        	
    	} catch (ConexaoBDException e) {    		
    		throw new InfraException(e.getMessage(), e);
    		
    	} catch (SQLException e) {
    		throw new InfraException(e.getMessage(), e);
    		
    	} finally {			
			this.liberarRecursoBanco(pst);
		}        	
    	    
    }        	     
        
    public Fornecedor consultar(long id) throws InfraException {

    	PreparedStatement pst = null;
    	ResultSet rs = null;

    	Fornecedor res = null;
        
        Contexto contexto = new Contexto();
        
        try {
        	
    		DBTaFeito.beginTransacao(contexto);
    		
        	Connection con = contexto.getConexao();

	        String sql = "SELECT * FROM " + DBTaFeito.TABELA_FORNECEDOR;
	
	        sql = sql + " INNER JOIN " + DBTaFeito.TABELA_USUARIO;
	        sql = sql + " ON " + DBTaFeito.TABELA_FORNECEDOR + "." + DBTaFeito.TABELA_FORNECEDOR_COLUNA_ID;
	        sql = sql + " = " + DBTaFeito.TABELA_USUARIO + "." + DBTaFeito.TABELA_USUARIO_COLUNA_ID;
	
	        sql = sql + " WHERE " + DBTaFeito.TABELA_FORNECEDOR + "." + DBTaFeito.TABELA_FORNECEDOR_COLUNA_ID + " = ?";
	        
	        pst = con.prepareStatement(sql);
	    	
	    	pst.setLong(1, id);    	
	    	
	    	rs = pst.executeQuery(sql);
            
	    	if (rs.next()) {
	    		
	            //
	            long idCol = rs.getLong(0);
	            String cnpjCol = rs.getString(1);

	            //From USUARIO
	            boolean habUsu = rs.getBoolean(3);
	            String nomeUsu = rs.getString(4);
	            String endUsu = rs.getString(5);
	            String emailCol = rs.getString(6);
	            int telCol = rs.getInt(7);

	            Fornecedor fornecedor = new Fornecedor();
	            fornecedor.setId(idCol);
	            fornecedor.setHabilitado(habUsu);
	            fornecedor.setNome(nomeUsu);
	            fornecedor.setEndereco(endUsu);
	            fornecedor.setEmail(emailCol);
	            fornecedor.setTelefone(telCol);
	            fornecedor.setCnpj(cnpjCol);

	            res = fornecedor;	    			    	
	    	}
	       
	    	DBTaFeito.commitTransacao(contexto);
	    	
		} catch (ConexaoBDException e) {    		
			throw new InfraException(e.getMessage(), e);
			
		} catch (SQLException e) {
			throw new InfraException(e.getMessage(), e);
			
		} finally {			
			this.liberarRecursoBanco(pst);
			this.liberarRecursoBanco(rs);					
		}	
	            
	    return res;
                        
    }    
       
    public void excluir(Fornecedor fornecedor) throws InfraException  {
    
		PreparedStatement pst = null;	
	    
	    Contexto contexto = new Contexto();
	    
	    try {
	    	
			DBTaFeito.beginTransacao(contexto);
			
	    	Connection con = contexto.getConexao();
	
	        String sql = "DELETE FROM " + DBTaFeito.TABELA_FORNECEDOR;
	
	        sql = sql + " WHERE " + DBTaFeito.TABELA_FORNECEDOR + "." + DBTaFeito.TABELA_FORNECEDOR_COLUNA_ID + " = ?";
	        
	        pst = con.prepareStatement(sql); 
	        
	        pst.setLong(1, fornecedor.getId());
	        
	        pst.executeUpdate();
	        
        	DBTaFeito.commitTransacao(contexto);
        	
    	} catch (ConexaoBDException e) {    		
    		throw new InfraException(e.getMessage(), e);
    		
    	} catch (SQLException e) {
    		throw new InfraException(e.getMessage(), e);
    		
    	} finally {			
			this.liberarRecursoBanco(pst);
		}   	 
    }
    
    public List<Fornecedor> listar() throws InfraException {
		
    	Statement st = null;
    	ResultSet rs = null;

		List<Fornecedor> res = new ArrayList<Fornecedor>();

		Contexto contexto = new Contexto();

		try {

			DBTaFeito.beginTransacao(contexto);

			Connection con = contexto.getConexao();

	        String sql = "SELECT * FROM " + DBTaFeito.TABELA_FORNECEDOR;

	        sql = sql + " INNER JOIN " + DBTaFeito.TABELA_USUARIO;
	        sql = sql + " ON " + DBTaFeito.TABELA_FORNECEDOR + "." + DBTaFeito.TABELA_FORNECEDOR_COLUNA_ID;
	        sql = sql + " = " + DBTaFeito.TABELA_USUARIO + "." + DBTaFeito.TABELA_USUARIO_COLUNA_ID;

	        sql = sql + " ORDER BY " + "5";

			st = con.createStatement();	        		        

			rs = st.executeQuery(sql);

			while (rs.next()) {

	            long idCol = rs.getLong(0);
	            String cnpjCol = rs.getString(1);

	            //From USUARIO
	            boolean habUsu = rs.getBoolean(3);
	            String nomeUsu = rs.getString(4);
	            String endUsu = rs.getString(5);
	            String emailCol = rs.getString(6);
	            int telCol = rs.getInt(7);

	            Fornecedor fornecedor = new Fornecedor();
	            fornecedor.setId(idCol);
	            fornecedor.setHabilitado(habUsu);
	            fornecedor.setNome(nomeUsu);
	            fornecedor.setEndereco(endUsu);
	            fornecedor.setEmail(emailCol);
	            fornecedor.setTelefone(telCol);
	            fornecedor.setCnpj(cnpjCol);

	            res.add(fornecedor);
			}

			DBTaFeito.commitTransacao(contexto);

		} catch (ConexaoBDException e) {    		
			throw new InfraException(e.getMessage(), e);

		} catch (SQLException e) {
			throw new InfraException(e.getMessage(), e);

		} finally {			
			this.liberarRecursoBanco(st);
			this.liberarRecursoBanco(rs);					
		}			

		return res;

	}    
    
    public List<Fornecedor> listarPorServicoCategoria(ServicoCategoria servCat) throws InfraException {
		
    	PreparedStatement pst = null;	
    	ResultSet rs = null;

		List<Fornecedor> res = new ArrayList<Fornecedor>();

		Contexto contexto = new Contexto();

		try {

			DBTaFeito.beginTransacao(contexto);

			Connection con = contexto.getConexao();

	        String sql = "SELECT * FROM " + DBTaFeito.TABELA_FORNECEDOR;

	        sql = sql + " INNER JOIN " + DBTaFeito.TABELA_USUARIO;
	        sql = sql + " ON " + DBTaFeito.TABELA_FORNECEDOR + "." + DBTaFeito.TABELA_FORNECEDOR_COLUNA_ID;
	        sql = sql + " = " + DBTaFeito.TABELA_USUARIO + "." + DBTaFeito.TABELA_USUARIO_COLUNA_ID;
	        sql = sql + " INNER JOIN " + DBTaFeito.TABELA_SERVICO;
	        sql = sql + " ON " + DBTaFeito.TABELA_SERVICO + "." + DBTaFeito.TABELA_SERVICO_COLUNA_ID_FORNECEDOR;
	        sql = sql + " = " + DBTaFeito.TABELA_FORNECEDOR + "." + DBTaFeito.TABELA_FORNECEDOR_COLUNA_ID;
	        sql = sql + " INNER JOIN " + DBTaFeito.TABELA_SERVICO_CATEGORIA;
	        sql = sql + " ON " + DBTaFeito.TABELA_SERVICO + "." + DBTaFeito.TABELA_SERVICO_COLUNA_ID_SERVICO_CATEGORIA;
	        sql = sql + " = " + DBTaFeito.TABELA_SERVICO_CATEGORIA + "." + DBTaFeito.TABELA_SERVICO_CATEGORIA_COLUNA_ID;

	        sql = sql + " WHERE " + DBTaFeito.TABELA_SERVICO_CATEGORIA + "." + DBTaFeito.TABELA_SERVICO_CATEGORIA_COLUNA_ID + " = ?";	        

	        sql = sql + " ORDER BY " + "5";

	        pst = con.prepareStatement(sql);
	    	
	    	pst.setLong(1, servCat.getId());    	
	    	
	    	rs = pst.executeQuery(sql);

			while (rs.next()) {

	            long idCol = rs.getLong(0);
	            String cnpjCol = rs.getString(1);

	            //From USUARIO
	            boolean habUsu = rs.getBoolean(3);
	            String nomeUsu = rs.getString(4);
	            String endUsu = rs.getString(5);
	            String emailCol = rs.getString(6);
	            int telCol = rs.getInt(7);

	            Fornecedor fornecedor = new Fornecedor();
	            fornecedor.setId(idCol);
	            fornecedor.setHabilitado(habUsu);
	            fornecedor.setNome(nomeUsu);
	            fornecedor.setEndereco(endUsu);
	            fornecedor.setEmail(emailCol);
	            fornecedor.setTelefone(telCol);
	            fornecedor.setCnpj(cnpjCol);

	            res.add(fornecedor);
			}

			DBTaFeito.commitTransacao(contexto);

		} catch (ConexaoBDException e) {    		
			throw new InfraException(e.getMessage(), e);

		} catch (SQLException e) {
			throw new InfraException(e.getMessage(), e);

		} finally {			
			this.liberarRecursoBanco(pst);
			this.liberarRecursoBanco(rs);					
		}			

		return res;

	}      
    
	public void liberarRecursoBanco(Object objeto){						
		if (objeto != null) {		
			try {
				if (objeto instanceof Statement){					
					Statement st = (Statement)objeto;									
					st.close();
					st = null;
				} else if (objeto instanceof PreparedStatement){					
					PreparedStatement pstC = (PreparedStatement)objeto;						
					pstC.close();
					pstC = null;													
				} else if (objeto instanceof ResultSet){					
					ResultSet rsC = (ResultSet)objeto;									
					rsC.close();
					rsC = null;
				}
			} catch(Exception e){
				//Log.setLog(e, " ID: , Excecao: " + e.getMessage());
			} finally{
				objeto = null;				
			}						
		}							
	}    
		
}
