package br.edu.ifsc.fln.sistemalavacaojavafx.model.dao;

import br.edu.ifsc.fln.sistemalavacaojavafx.model.database.Database;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.database.DatabaseFactory;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.domain.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ModeloDAO {

    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean inserir(Modelo modelo) {
        String sql = "INSERT INTO modelo(descricao, categoria, marca) VALUES(?, ?, ?);";
        String sqlMotor = "INSERT INTO motor(id, potencia, tipo_combustivel) VALUES(SELECT MAX(id) FROM modelo), ? ?);";
        Database database = DatabaseFactory.getDatabase("mysql");
        Connection connection = database.conectar();
        try {
            connection.setAutoCommit(false);
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, modelo.getDescricao());
            stmt.setString(2, modelo.getCategoria().getDescricao());
            stmt.setString(3, modelo.getMarca().getNome());
            stmt.execute();
            // Garantindo a composição, devemos registrar o motor ao criar um modelo;
            stmt = connection.prepareStatement(sqlMotor);
            stmt.setInt(2, modelo.getMotor().getPotencia());
            stmt.setString(3, String.valueOf(modelo.getMotor().getTipoCombustivel()));
            stmt.execute();
            connection.commit();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(MarcaDAO.class.getName()).log(Level.SEVERE, null, ex);
            try{
                connection.rollback();
            }catch(SQLException ex1){
                throw new RuntimeException(ex1);
            }
            return false;
        }finally {
            database.desconectar(connection);
        }
    }

    public boolean alterar(Modelo modelo) {
        String sql = "UPDATE modelo SET nome=? WHERE id=?";
        Database database = DatabaseFactory.getDatabase("mysql");
        Connection connection = database.conectar();
        try {
            connection.setAutoCommit(false);
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, modelo.getDescricao());
            stmt.setInt(2, modelo.getId());
            stmt.execute();
            connection.commit();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(MarcaDAO.class.getName()).log(Level.SEVERE, null, ex);
            try{
                connection.rollback();
            }catch(SQLException ex1){
                throw new RuntimeException(ex1);
            }
            return false;
        }finally {
            database.desconectar(connection);
        }
    }

    public boolean remover(Modelo modelo) {
        String sql = "DELETE FROM modelo WHERE id=?";
        Database database = DatabaseFactory.getDatabase("mysql");
        Connection connection = database.conectar();
        try {
            connection.setAutoCommit(false);
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, modelo.getId());
            stmt.execute();
            connection.commit();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(MarcaDAO.class.getName()).log(Level.SEVERE, null, ex);
            try{
                connection.rollback();
            }catch(SQLException ex1){
                throw new RuntimeException(ex1);
            }
            return false;
        }finally {
            database.desconectar(connection);
        }
    }

    public List<Modelo> listar() {
        String sql = "SELECT modelo.id as id_modelo, modelo.descricao as descricao, marca.nome as nome_marca, marca.id as id_marca, modelo.categoria as categoria , motor.potencia as potencia, motor.tipo_combustivel as tipo_combustivel FROM modelo " +
                "join marca on modelo.id_marca = marca.id " +
                "join motor on modelo.id = motor.id_modelo";
        Database database = DatabaseFactory.getDatabase("mysql");
        Connection connection = database.conectar();

        List<Modelo> retorno = new ArrayList<>();

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();

            while (resultado.next()) {
                //Criando o modelo e passando os atributos de motor nele.
                Modelo modelo = new Modelo(resultado.getInt("potencia"), ETipoCombustivel.valueOf(resultado.getString("tipo_combustivel")));

                // Passando os demais atributos de modelo
                modelo.setId(resultado.getInt("id_modelo"));
                modelo.setDescricao(resultado.getString("descricao"));
                modelo.setCategoria(ECategoria.valueOf(resultado.getString("categoria")));

                // Associando marca ao Modelo
                Marca marca = new Marca();
                marca.setId(resultado.getInt("id_marca"));
                marca.setNome(resultado.getString("nome_marca"));
                modelo.setMarca(marca);

                retorno.add(modelo);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MarcaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
            database.desconectar(connection);
        }
        return retorno;
    }

    public Modelo buscar(Modelo modelo) {
        Modelo retorno = buscar(modelo.getId());
        return retorno;
    }

    public Modelo buscar(int id) {
        String sql = "SELECT * FROM modelo WHERE id=?";
        Database database = DatabaseFactory.getDatabase("mysql");
        Connection connection = database.conectar();

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet resultado = stmt.executeQuery();
            Modelo modeloRetorno = new Modelo(resultado.getInt("potencia"), ETipoCombustivel.valueOf(resultado.getString("tipo_combustivel")));
            if (resultado.next()) {
                modeloRetorno.setId(resultado.getInt("id"));
                modeloRetorno.setDescricao(resultado.getString("descricao"));
                modeloRetorno.setCategoria(ECategoria.valueOf(resultado.getString("categoria")));

                // Associando marca ao Modelo
                Marca marca = new Marca();
                marca.setId(resultado.getInt("id_marca"));
                marca.setNome(resultado.getString("nome_marca"));
                modeloRetorno.setMarca(marca);
            }
            return modeloRetorno;
        } catch (SQLException ex) {
            Logger.getLogger(MarcaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
            database.desconectar(connection);
        }
        return null;
    }
}
