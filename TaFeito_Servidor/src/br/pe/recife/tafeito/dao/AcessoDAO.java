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
import br.pe.recife.tafeito.negocio.Acesso;
import br.pe.recife.tafeito.util.DBTaFeito;

public class AcessoDAO {

    private static AcessoDAO instancia;
    
    //private DBTaFeito bd;   
    
    private Statement st;
    private PreparedStatement pst;
	private ResultSet rs;

    public static AcessoDAO getInstancia() {

        if (instancia == null) {
            instancia = new AcessoDAO();
        }

        return instancia;
    }

    private AcessoDAO() {
        //this.bd = DBTaFeito.getInstancia();
    }
    
    public long inserir(Acesso acesso) throws InfraException {
    	
    	long id = 0;
    	
    	Contexto contexto = new Contexto();    	
    	
    	try {
    	
    		DBTaFeito.beginTransacao(contexto);
    		
        	Connection con = contexto.getConexao();
        	        	
        	String sql = "INSERT INTO " + DBTaFeito.TABELA_ACESSO + " VALUES (?, ?, ?)";
        	pst = con.prepareStatement(sql, 
        			new String[] {"" + DBTaFeito.TABELA_ACESSO_COLUNA_ID + ""});
        	
        	pst.setLong(1, acesso.getId());
        	pst.setString(2, acesso.getLogin());
        	pst.setString(3, acesso.getSenha());
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
    
    public void atualizar(Acesso acesso) throws InfraException {    
    	
    	Contexto contexto = new Contexto();    	
    	
    	try {
    	
    		DBTaFeito.beginTransacao(contexto);
    		
        	Connection con = contexto.getConexao();
        	
        	String sql = "UPDATE " + DBTaFeito.TABELA_ACESSO + " SET ";
        	sql = sql + DBTaFeito.TABELA_ACESSO_COLUNA_LOGIN + " = ?, ";
        	sql = sql + DBTaFeito.TABELA_ACESSO_COLUNA_SENHA + " = ? ";
        	sql = sql + "WHERE " + DBTaFeito.TABELA_ACESSO_COLUNA_ID + " = ?";
    	
        	pst = con.prepareStatement(sql);
        	        	
        	pst.setString(1, acesso.getLogin());
        	pst.setString(2, acesso.getSenha());
        	pst.setLong(1, acesso.getId());
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
    
    public long buscarPorLoginPorSenhaFornecedor(String login, String senha) throws InfraException {

        long res = 0;
        
        Contexto contexto = new Contexto();
        
        try {
        	
    		DBTaFeito.beginTransacao(contexto);
    		
        	Connection con = contexto.getConexao();
        	
            String sql = "SELECT * FROM " + DBTaFeito.TABELA_ACESSO;

            sql = sql + " INNER JOIN " + DBTaFeito.TABELA_FORNECEDOR;
            sql = sql + " ON " + DBTaFeito.TABELA_ACESSO + "." + DBTaFeito.TABELA_ACESSO_COLUNA_ID;
            sql = sql + " = " + DBTaFeito.TABELA_FORNECEDOR + "." + DBTaFeito.TABELA_FORNECEDOR_COLUNA_ID;

            sql = sql + " WHERE " + DBTaFeito.TABELA_ACESSO_COLUNA_LOGIN + " = ?";
            sql = sql + " AND " + DBTaFeito.TABELA_ACESSO_COLUNA_SENHA + " = ?";
        
        	pst = con.prepareStatement(sql);
        	
        	pst.setString(1, login);
        	pst.setString(2, senha);
        	
        	ResultSet rs = pst.executeQuery(sql);
                
        	if (rs.next()) {
        		res = rs.getLong(0);
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
            
	public long buscarPorLoginPorSenhaCliente(String login, String senha) throws InfraException {

        long res = 0;
        
        Contexto contexto = new Contexto();
        
        try {
        	
    		DBTaFeito.beginTransacao(contexto);
    		
        	Connection con = contexto.getConexao();
        	
            String sql = "SELECT * FROM " + DBTaFeito.TABELA_ACESSO;

            sql = sql + " INNER JOIN " + DBTaFeito.TABELA_CLIENTE;
            sql = sql + " ON " + DBTaFeito.TABELA_ACESSO + "." + DBTaFeito.TABELA_ACESSO_COLUNA_ID;
            sql = sql + " = " + DBTaFeito.TABELA_CLIENTE + "." + DBTaFeito.TABELA_CLIENTE_COLUNA_ID;

            sql = sql + " WHERE " + DBTaFeito.TABELA_ACESSO_COLUNA_LOGIN + " = ?";
            sql = sql + " AND " + DBTaFeito.TABELA_ACESSO_COLUNA_SENHA + " = ?";
        
        	pst = con.prepareStatement(sql);
        	
        	pst.setString(1, login);
        	pst.setString(2, senha);
        	
        	ResultSet rs = pst.executeQuery(sql);
                
        	if (rs.next()) {
        		res = rs.getLong(0);
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
	
	public boolean existePorLogin(String login) throws InfraException {
		
		boolean res = false;
		
        Contexto contexto = new Contexto();
        
        try {
        	
    		DBTaFeito.beginTransacao(contexto);
    		
        	Connection con = contexto.getConexao();
        	
            String sql = "SELECT * FROM " + DBTaFeito.TABELA_ACESSO;

            sql = sql + " WHERE " + DBTaFeito.TABELA_ACESSO_COLUNA_LOGIN + " = ?";
        
        	pst = con.prepareStatement(sql);
        	
        	pst.setString(1, login);
        	
        	ResultSet rs = pst.executeQuery(sql);
                
        	if (rs.next()) {
        		res = true;
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
	
	public List<Acesso> listar() throws InfraException {

		List<Acesso> res = new ArrayList<Acesso>();

		Contexto contexto = new Contexto();

		try {

			DBTaFeito.beginTransacao(contexto);

			Connection con = contexto.getConexao();

			String sql = "SELECT * FROM " + DBTaFeito.TABELA_ACESSO;

			sql = sql + " ORDER BY " + DBTaFeito.TABELA_ACESSO_COLUNA_ID;

			st = con.createStatement();	        		        

			ResultSet rs = st.executeQuery(sql);

			while (rs.next()) {

				Acesso acesso = new Acesso();
				acesso.setId(rs.getLong(rs.findColumn(DBTaFeito.TABELA_ACESSO_COLUNA_ID)));
				acesso.setLogin(rs.getString(rs.findColumn(DBTaFeito.TABELA_ACESSO_COLUNA_LOGIN)));
				acesso.setSenha(rs.getString(rs.findColumn(DBTaFeito.TABELA_ACESSO_COLUNA_SENHA)));	        			        		

				res.add(acesso);
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