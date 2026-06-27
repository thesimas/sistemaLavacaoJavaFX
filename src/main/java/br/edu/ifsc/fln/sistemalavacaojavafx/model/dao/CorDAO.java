package br.edu.ifsc.fln.sistemalavacaojavafx.model.dao;

import br.edu.ifsc.fln.sistemalavacaojavafx.model.database.Database;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.database.DatabaseFactory;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.domain.Cor;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.exceptions.DAOException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CorDAO {

    private Connection connection;
    
    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void inserir(Cor cor) throws DAOException {
        String sql = "INSERT INTO cor(nome) VALUES(?)";
        Database database = DatabaseFactory.getDatabase("mysql");
        Connection connection = database.conectar();
        try {
            connection.setAutoCommit(false);
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, cor.getNome());
            stmt.execute();
            connection.commit();
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(CorDAO.class.getName()).log(Level.SEVERE, null, ex);
            try{
                connection.rollback();
            }catch(SQLException ex1){
                throw new RuntimeException(ex1);
            }
            throw new DAOException("Não foi possível inserir a cor no banco de dados!\nMotivo: ",ex);
        }
        finally {
            database.desconectar(connection);
        }
    }

    public void alterar(Cor cor) throws DAOException {
        String sql = "UPDATE cor SET nome=? WHERE id=?";
        Database database = DatabaseFactory.getDatabase("mysql");
        Connection connection = database.conectar();
        try {
            connection.setAutoCommit(false);
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, cor.getNome());
            stmt.setInt(2, cor.getId());
            stmt.execute();
            connection.commit();
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(CorDAO.class.getName()).log(Level.SEVERE, null, ex);
            try{
                connection.rollback();
            }catch(SQLException ex1){
                throw new RuntimeException(ex1);
            }
            throw new DAOException("Não foi possível alterar a cor no banco de dados!\nMotivo: ",ex);
        }finally {
            database.desconectar(connection);
        }
    }

    public void remover(Cor cor) throws DAOException {
        String sql = "DELETE FROM cor WHERE id=?";
        Database database = DatabaseFactory.getDatabase("mysql");
        Connection connection = database.conectar();
        try {
            connection.setAutoCommit(false);
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, cor.getId());
            stmt.execute();
            connection.commit();
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(CorDAO.class.getName()).log(Level.SEVERE, null, ex);
            try {
                connection.rollback();
            }catch(SQLException ex1){
                throw new RuntimeException(ex1);
            }
            throw new DAOException("Não foi possível excluir a cor no banco de dados!\nMotivo: ",ex);
        }finally {
            database.desconectar(connection);
        }
    }

    public List<Cor> listar() throws DAOException {
        String sql = "SELECT * FROM cor ORDER BY cor.nome ASC;";
        List<Cor> CoresRetornada = new ArrayList<>();

        Database database = DatabaseFactory.getDatabase("mysql");
        Connection connection = database.conectar();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                Cor cor = new Cor();
                cor.setId(resultado.getInt("id"));
                cor.setNome(resultado.getString("nome"));
                CoresRetornada.add(cor);
            }
            stmt.close();
            resultado.close();
        } catch (SQLException ex) {
            Logger.getLogger(CorDAO.class.getName()).log(Level.SEVERE, null, ex);

            throw new DAOException("Não foi possível listar as cores no banco de dados!\nMotivo: ",ex);
        }finally {
            database.desconectar(connection);
        }
        return CoresRetornada;
    }

    public Cor buscar(Cor cor) throws DAOException {
        Cor CorRetornada = buscar(cor.getId());
        return CorRetornada;
    }
    
    public Cor buscar(int id) throws DAOException {
        String sql = "SELECT * FROM cor WHERE id=?";

        Database database = DatabaseFactory.getDatabase("mysql");
        Connection connection = database.conectar();
        
        Cor CorRetornada = new Cor();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                CorRetornada.setId(resultado.getInt("id"));
                CorRetornada.setNome(resultado.getString("nome"));
            }
            stmt.close();
            resultado.close();
        } catch (SQLException ex) {
            Logger.getLogger(CorDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Não foi possível buscar a cor no banco de dados!\nMotivo: ",ex);
        }finally {
            database.desconectar(connection);
        }

        return CorRetornada;
    }
}
