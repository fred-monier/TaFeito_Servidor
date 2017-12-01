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
import br.pe.recife.tafeito.negocio.Usuario;
import br.pe.recife.tafeito.util.DBTaFeito;

public class UsuarioDAO implements IDAO<Usuario> {
	
    private static UsuarioDAO instancia;

    public static UsuarioDAO getInstancia() {

        if (instancia == null) {
            instancia = new UsuarioDAO();
        }

        return instancia;
    }

    private UsuarioDAO() {        
    }
    
    private long inserir(Usuario usuario) throws InfraException {    	
    	
    	PreparedStatement pst = null;
    	ResultSet rs = null;
    	
    	long id = 0;
    	
    	Contexto contexto = new Contexto();    	
    	
    	try {    		
    	
    		DBTaFeito.beginTransacao(contexto);
    		
        	Connection con = contexto.getConexao();
        	        	
        	String sql = "INSERT INTO " + DBTaFeito.TABELA_USUARIO;
        	sql = sql + "(" + DBTaFeito.TABELA_USUARIO_COLUNA_HABILITADO + ", ";
        	sql = sql + DBTaFeito.TABELA_USUARIO_COLUNA_NOME + ", ";
        	sql = sql + DBTaFeito.TABELA_USUARIO_COLUNA_ENDERECO + ", ";
        	sql = sql + DBTaFeito.TABELA_USUARIO_COLUNA_EMAIL + ", ";
        	sql = sql + DBTaFeito.TABELA_USUARIO_COLUNA_TELEFONE + ") VALUES (?, ?, ?, ?, ?)";
        	pst = con.prepareStatement(sql, 
        			new String[] {"" + DBTaFeito.TABELA_USUARIO_COLUNA_ID_RET + ""});
        	
        	pst.setBoolean(1, usuario.isHabilitado());
        	pst.setString(2, usuario.getNome());
        	pst.setString(3, usuario.getEndereco());
        	pst.setString(4, usuario.getEmail());
        	pst.setInt(5, usuario.getTelefone());
        	
        	pst.executeUpdate();
        	
        	rs = pst.getGeneratedKeys();
        	if (rs.next()) {
        	   id = rs.getLong(1);
        	}     
        	
            if (id != -1) {
                usuario.setId(id);
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
    
    private void atualizar(Usuario usuario) throws InfraException {    
    	
    	PreparedStatement pst = null;
    	
    	Contexto contexto = new Contexto();    	
    	
    	try {
    	
    		DBTaFeito.beginTransacao(contexto);
    		
        	Connection con = contexto.getConexao();
        	
        	String sql = "UPDATE " + DBTaFeito.TABELA_USUARIO + " SET ";
        	sql = sql + DBTaFeito.TABELA_USUARIO_COLUNA_HABILITADO + " = ?, ";
        	sql = sql + DBTaFeito.TABELA_USUARIO_COLUNA_NOME + " = ?, ";
        	sql = sql + DBTaFeito.TABELA_USUARIO_COLUNA_ENDERECO + " = ?, ";
        	sql = sql + DBTaFeito.TABELA_USUARIO_COLUNA_EMAIL + " = ?, ";
        	sql = sql + DBTaFeito.TABELA_USUARIO_COLUNA_TELEFONE + " = ? ";        	
        	
        	sql = sql + "WHERE " + DBTaFeito.TABELA_USUARIO_COLUNA_ID + " = ?";
    	
        	pst = con.prepareStatement(sql);
        	        	
        	pst.setBoolean(1, usuario.isHabilitado());
        	pst.setString(2, usuario.getNome());
        	pst.setString(3, usuario.getEndereco());
        	pst.setString(4, usuario.getEmail());
        	pst.setInt(5, usuario.getTelefone());
        	pst.setLong(6, usuario.getId());
        	
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
        
	@Override
	public void salvar(Usuario usuario) throws InfraException {
        if (usuario.getId() == 0) {
            this.inserir(usuario);
        } else {
            this.atualizar(usuario);
        }		
	}	

	@Override
	public void excluir(Usuario entidade) throws InfraException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Usuario consultar(long id) throws InfraException {
		
    	PreparedStatement pst = null;
    	ResultSet rs = null;

    	Usuario res = null;
        
        Contexto contexto = new Contexto();
        
        try {
        	
    		DBTaFeito.beginTransacao(contexto);
    		
        	Connection con = contexto.getConexao();

	        String sql = "SELECT * FROM " + DBTaFeito.TABELA_USUARIO;
	
	        sql = sql + " WHERE " + DBTaFeito.TABELA_USUARIO + "." + DBTaFeito.TABELA_USUARIO_COLUNA_ID + " = ?";
	        
	        pst = con.prepareStatement(sql);
	    	
	    	pst.setLong(1, id);    	
	    	
	    	rs = pst.executeQuery();
            
	    	if (rs.next()) {
	    		
	            //
	            long idCol = rs.getLong(1);
	            boolean habCol = rs.getBoolean(2);
	            String nomeCol = rs.getString(3);
	            String endCol = rs.getString(4);
	            String emailCol = rs.getString(5);
	            int telCol = rs.getInt(6);

	            Usuario usuario = new Usuario();
	            usuario.setId(idCol);
	            usuario.setHabilitado(habCol);
	            usuario.setNome(nomeCol);
	            usuario.setEndereco(endCol);
	            usuario.setEmail(emailCol);
	            usuario.setTelefone(telCol);

	            res = usuario;    			    	
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

	@Override
	public List<Usuario> listar() throws InfraException {
		
    	Statement st = null;
    	ResultSet rs = null;

		List<Usuario> res = new ArrayList<Usuario>();

		Contexto contexto = new Contexto();

		try {

			DBTaFeito.beginTransacao(contexto);

			Connection con = contexto.getConexao();

			String sql = "SELECT * FROM " + DBTaFeito.TABELA_USUARIO;

			sql = sql + " ORDER BY " + DBTaFeito.TABELA_USUARIO_COLUNA_NOME;

			st = con.createStatement();	        		        

			rs = st.executeQuery(sql);

			while (rs.next()) {

	            //	            
	            long idCol = rs.getLong(1);
	            boolean habCol = rs.getBoolean(2);
	            String nomeCol = rs.getString(3);
	            String endCol = rs.getString(4);
	            String emailCol = rs.getString(5);
	            int telCol = rs.getInt(6);

	            Usuario usuario = new Usuario();
	            usuario.setId(idCol);
	            usuario.setHabilitado(habCol);
	            usuario.setNome(nomeCol);
	            usuario.setEndereco(endCol);
	            usuario.setEmail(emailCol);
	            usuario.setTelefone(telCol);

	            res.add(usuario);
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
