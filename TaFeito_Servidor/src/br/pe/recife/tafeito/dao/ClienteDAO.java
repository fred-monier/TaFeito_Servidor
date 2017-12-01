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
import br.pe.recife.tafeito.negocio.Cliente;
import br.pe.recife.tafeito.util.DBTaFeito;

public class ClienteDAO implements IDAOson<Cliente> {
	
    private static ClienteDAO instancia;
    
    public static ClienteDAO getInstancia() {

        if (instancia == null) {
            instancia = new ClienteDAO();
        }

        return instancia;
    }

    private ClienteDAO() {        
    }
    
    public void salvar(Cliente cliente, boolean novo) throws InfraException {
        if (novo) {
            this.inserir(cliente);
        } else {
            this.atualizar(cliente);
        }
    }
     
    private long inserir(Cliente cliente) throws InfraException {    	
    	
    	PreparedStatement pst = null;
    	ResultSet rs = null;
    	
    	long id = 0;
    	
    	Contexto contexto = new Contexto();    	
    	
    	try {    		
    	
    		DBTaFeito.beginTransacao(contexto);
    		
        	Connection con = contexto.getConexao();
        	        	
        	String sql = "INSERT INTO " + DBTaFeito.TABELA_CLIENTE + " VALUES (?, ?)";
        	pst = con.prepareStatement(sql, 
        			new String[] {"" + DBTaFeito.TABELA_CLIENTE_COLUNA_ID + ""});
        	
        	pst.setLong(1, cliente.getId());
        	pst.setString(2, cliente.getCpf());
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
    
    private void atualizar(Cliente cliente) throws InfraException {    
    	
    	PreparedStatement pst = null;
    	
    	Contexto contexto = new Contexto();    	
    	
    	try {
    	
    		DBTaFeito.beginTransacao(contexto);
    		
        	Connection con = contexto.getConexao();
        	
        	String sql = "UPDATE " + DBTaFeito.TABELA_CLIENTE + " SET ";
        	sql = sql + DBTaFeito.TABELA_CLIENTE_COLUNA_CPF + " = ?, ";
        	sql = sql + "WHERE " + DBTaFeito.TABELA_CLIENTE_COLUNA_ID + " = ?";
    	
        	pst = con.prepareStatement(sql);
        	        	
        	pst.setString(1, cliente.getCpf());
        	pst.setLong(2, cliente.getId());
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
        
    public Cliente consultar(long id) throws InfraException {

    	PreparedStatement pst = null;
    	ResultSet rs = null;

    	Cliente res = null;
        
        Contexto contexto = new Contexto();
        
        try {
        	
    		DBTaFeito.beginTransacao(contexto);
    		
        	Connection con = contexto.getConexao();

	        String sql = "SELECT * FROM " + DBTaFeito.TABELA_CLIENTE;
	
	        sql = sql + " INNER JOIN " + DBTaFeito.TABELA_USUARIO;
	        sql = sql + " ON " + DBTaFeito.TABELA_CLIENTE + "." + DBTaFeito.TABELA_CLIENTE_COLUNA_ID;
	        sql = sql + " = " + DBTaFeito.TABELA_USUARIO + "." + DBTaFeito.TABELA_USUARIO_COLUNA_ID;
	
	        sql = sql + " WHERE " + DBTaFeito.TABELA_CLIENTE + "." + DBTaFeito.TABELA_CLIENTE_COLUNA_ID + " = ?";
	        
	        pst = con.prepareStatement(sql);
	    	
	    	pst.setLong(1, id);    	
	    	
	    	rs = pst.executeQuery(sql);
            
	    	if (rs.next()) {
	    		
	            //
	            long idCol = rs.getLong(0);
	            String cpfCol = rs.getString(1);

	            //From USUARIO
	            boolean habUsu = rs.getBoolean(3);
	            String nomeUsu = rs.getString(4);
	            String endUsu = rs.getString(5);
	            String emailCol = rs.getString(6);
	            int telCol = rs.getInt(7);

	            Cliente cliente = new Cliente();
	            cliente.setId(idCol);
	            cliente.setHabilitado(habUsu);
	            cliente.setNome(nomeUsu);
	            cliente.setEndereco(endUsu);
	            cliente.setEmail(emailCol);
	            cliente.setTelefone(telCol);
	            cliente.setCpf(cpfCol);

	            res = cliente;	    			    	
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
       
    public void excluir(Cliente cliente) throws InfraException  {
    
		PreparedStatement pst = null;	
	    
	    Contexto contexto = new Contexto();
	    
	    try {
	    	
			DBTaFeito.beginTransacao(contexto);
			
	    	Connection con = contexto.getConexao();
	
	        String sql = "DELETE FROM " + DBTaFeito.TABELA_CLIENTE;
	
	        sql = sql + " WHERE " + DBTaFeito.TABELA_CLIENTE + "." + DBTaFeito.TABELA_CLIENTE_COLUNA_ID + " = ?";
	        
	        pst = con.prepareStatement(sql); 
	        
	        pst.setLong(1, cliente.getId());
	        
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
    
    public List<Cliente> listar() throws InfraException {
		
    	Statement st = null;
    	ResultSet rs = null;

		List<Cliente> res = new ArrayList<Cliente>();

		Contexto contexto = new Contexto();

		try {

			DBTaFeito.beginTransacao(contexto);

			Connection con = contexto.getConexao();

	        String sql = "SELECT * FROM " + DBTaFeito.TABELA_CLIENTE;

	        sql = sql + " INNER JOIN " + DBTaFeito.TABELA_USUARIO;
	        sql = sql + " ON " + DBTaFeito.TABELA_CLIENTE + "." + DBTaFeito.TABELA_CLIENTE_COLUNA_ID;
	        sql = sql + " = " + DBTaFeito.TABELA_USUARIO + "." + DBTaFeito.TABELA_USUARIO_COLUNA_ID;

	        sql = sql + " ORDER BY " + "5";

			st = con.createStatement();	        		        

			rs = st.executeQuery(sql);

			while (rs.next()) {

	            long idCol = rs.getLong(0);
	            String cpfCol = rs.getString(1);

	            //From USUARIO
	            boolean habUsu = rs.getBoolean(3);
	            String nomeUsu = rs.getString(4);
	            String endUsu = rs.getString(5);
	            String emailCol = rs.getString(6);
	            int telCol = rs.getInt(7);

	            Cliente cliente = new Cliente();
	            cliente.setId(idCol);
	            cliente.setHabilitado(habUsu);
	            cliente.setNome(nomeUsu);
	            cliente.setEndereco(endUsu);
	            cliente.setEmail(emailCol);
	            cliente.setTelefone(telCol);
	            cliente.setCpf(cpfCol);

	            res.add(cliente);
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
