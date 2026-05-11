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
        String sql = "INSERT INTO modelo(descricao, categoria, id_marca) VALUES(?, ?, ?);";
        String sqlMotor = "INSERT INTO motor (id_modelo, potencia, tipo_combustivel) VALUES ((SELECT MAX(id) FROM modelo), ?, ?);";
        Database database = DatabaseFactory.getDatabase("mysql");
        Connection connection = database.conectar();
        try {
            connection.setAutoCommit(false);
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, modelo.getDescricao());
            stmt.setString(2, String.valueOf(ECategoria.valueOf(modelo.getCategoria().name())));
            stmt.setInt(3, modelo.getMarca().getId());
            stmt.execute();
            // Garantindo a composição, devemos registrar o motor ao criar um modelo;
            stmt = connection.prepareStatement(sqlMotor);
            stmt.setInt(1, modelo.getMotor().getPotencia());
            stmt.setString(2, String.valueOf(modelo.getMotor().getTipoCombustivel()));
            stmt.execute();
            connection.commit();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ModeloDAO.class.getName()).log(Level.SEVERE, null, ex);
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
        String sql = "UPDATE modelo SET descricao=?, categoria=?, id_marca=? WHERE id=?";
        String sqlMotor = "UPDATE motor SET potencia=?, tipo_combustivel=? WHERE id_modelo=?";
        Database database = DatabaseFactory.getDatabase("mysql");
        Connection connection = database.conectar();
        try {
            connection.setAutoCommit(false);
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, modelo.getDescricao());
            stmt.setString(2, String.valueOf(modelo.getCategoria()));
            stmt.setInt(3, modelo.getMarca().getId());
            stmt.setInt(4, modelo.getId());
            stmt.execute();
            stmt = connection.prepareStatement(sqlMotor);
            stmt.setString(1, String.valueOf(modelo.getMotor().getPotencia()));
            stmt.setString(2, String.valueOf(modelo.getMotor().getTipoCombustivel()));
            stmt.setInt(3, modelo.getId());
            stmt.execute();
            connection.commit();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ModeloDAO.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(ModeloDAO.class.getName()).log(Level.SEVERE, null, ex);
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

        List<Modelo> modelosRetornado = new ArrayList<>();

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

                modelosRetornado.add(modelo);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ModeloDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
            database.desconectar(connection);
        }
        return modelosRetornado;
    }

    public Modelo buscar(Modelo modelo) {
        Modelo modeloRetorno = buscar(modelo.getId());
        return modeloRetorno;
    }

    public Modelo buscar(int id) {
        String sql = "SELECT * FROM modelo JOIN motor ON modelo.id = motor.id_modelo WHERE id=?";
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
            Logger.getLogger(ModeloDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
            database.desconectar(connection);
        }
        return null;
    }

    public List<Modelo> buscarModeloPorMarca(Marca marca) {
        String sql = "SELECT modelo.id as id_modelo, marca.id as id_marca, modelo.*, motor.*, marca.* FROM modelo JOIN motor ON modelo.id = motor.id_modelo JOIN marca ON modelo.id_marca = marca.id WHERE modelo.id_marca =?;";
        Database database = DatabaseFactory.getDatabase("mysql");
        Connection connection = database.conectar();

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, marca.getId());
            ResultSet resultado = stmt.executeQuery();
            List<Modelo> modelos = new ArrayList<>();
            while (resultado.next()) {
                Modelo modelo = new Modelo(resultado.getInt("potencia"), ETipoCombustivel.valueOf(resultado.getString("tipo_combustivel")));
                modelo.setId(resultado.getInt("id_modelo"));
                modelo.setDescricao(resultado.getString("descricao"));
                modelo.setCategoria(ECategoria.valueOf(resultado.getString("categoria")));
                // Associando marca ao Modelo
                Marca marcaReal = new Marca();
                marcaReal.setId(resultado.getInt("id_marca"));
                marcaReal.setNome(resultado.getString("nome"));
                modelo.setMarca(marcaReal);
                modelos.add(modelo);
            }
            return modelos;
        } catch (SQLException ex) {
            Logger.getLogger(ModeloDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
            database.desconectar(connection);
        }
        return null;
    }
}
