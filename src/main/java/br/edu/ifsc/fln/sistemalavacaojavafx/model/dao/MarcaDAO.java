package br.edu.ifsc.fln.sistemalavacaojavafx.model.dao;

import br.edu.ifsc.fln.sistemalavacaojavafx.model.database.Database;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.database.DatabaseFactory;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.domain.Marca;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.exceptions.DAOException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MarcaDAO {

    private Connection connection;
    
    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void inserir(Marca marca) throws DAOException {
        String sql = "INSERT INTO marca(nome) VALUES(?)";
        Database database = DatabaseFactory.getDatabase("mysql");
        Connection connection = database.conectar();
        try {
            connection.setAutoCommit(false);
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, marca.getNome());
            stmt.execute();
            connection.commit();
        } catch (SQLException ex) {
            Logger.getLogger(MarcaDAO.class.getName()).log(Level.SEVERE, null, ex);
            try{
                connection.rollback();
            }catch(SQLException ex1){
                throw new RuntimeException(ex1);
            }
            throw new DAOException("Não foi possível inserir a marca no banco de dados!\nMotivo: ",ex);
        }finally{
            database.desconectar(connection);
        }
    }

    public void alterar(Marca marca) throws DAOException {
        String sql = "UPDATE marca SET nome=? WHERE id=?";
        Database database = DatabaseFactory.getDatabase("mysql");
        Connection connection = database.conectar();
        try {
            connection.setAutoCommit(false);
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, marca.getNome());
            stmt.setInt(2, marca.getId());
            stmt.execute();
            connection.commit();
        } catch (SQLException ex) {
            Logger.getLogger(MarcaDAO.class.getName()).log(Level.SEVERE, null, ex);
            try{
                connection.rollback();
            }catch(SQLException ex1){
                throw new RuntimeException(ex1);
            }
            throw new DAOException("Não foi possível alterar a marca no banco de dados!\nMotivo: ",ex);
        }finally {

            database.desconectar(connection);
        }
    }

    public void remover(Marca marca) throws DAOException {
        String sql = "DELETE FROM marca WHERE id=?";
        Database database = DatabaseFactory.getDatabase("mysql");
        Connection connection = database.conectar();
        try {
            connection.setAutoCommit(false);
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, marca.getId());
            stmt.execute();
            connection.commit();
        } catch (SQLException ex) {
            Logger.getLogger(MarcaDAO.class.getName()).log(Level.SEVERE, null, ex);
            try{
                connection.rollback();
            }catch(SQLException ex1){
                throw new RuntimeException(ex1);
            }
            throw new DAOException("Não foi possível remover a marca no banco de dados!\nMotivo: ",ex);
        } finally {
            database.desconectar(connection);
        }
    }

    public List<Marca> listar() throws DAOException {
        List<Marca> marcasRetornada = new ArrayList<>();
        String sql = "SELECT * FROM marca";

        Database database = DatabaseFactory.getDatabase("mysql");
        Connection connection = database.conectar();

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();

            while (resultado.next()) {
                Marca marca = new Marca();
                marca.setId(resultado.getInt("id"));
                marca.setNome(resultado.getString("nome"));
                marcasRetornada.add(marca);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MarcaDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Não foi possível listar as marcas no banco de dados!\nMotivo: ",ex);
        } finally {
            database.desconectar(connection);
        }
        return marcasRetornada;
    }

    public Marca buscar(Marca marca) throws DAOException {
        Marca marcaRetornada = buscar(marca.getId());
        return marcaRetornada;
    }
    
    public Marca buscar(int id) throws DAOException {
        String sql = "SELECT * FROM marca WHERE id=?";
        Database database = DatabaseFactory.getDatabase("mysql");
        Connection connection = database.conectar();
        Marca marcaRetornada = new Marca();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                marcaRetornada.setId(resultado.getInt("id"));
                marcaRetornada.setNome(resultado.getString("nome"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(MarcaDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Não foi possível buscar a marca no banco de dados!\nMotivo: ",ex);
        }finally {
            database.desconectar(connection);
        }
        return marcaRetornada;        
    }
}
