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

public class VeiculoDAO {

    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean inserir(Veiculo veiculo) {
        String sql = "INSERT INTO veiculo(placa, observacoes, id_cor, id_modelo, id_cliente) VALUES(?, ?, ?, ?, ?);";

        Database database = DatabaseFactory.getDatabase("mysql");
        Connection connection = database.conectar();
        try {
            connection.setAutoCommit(false);
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, veiculo.getPlaca());
            stmt.setString(2, veiculo.getObservacoes());
            stmt.setInt(3, veiculo.getCor().getId());
            stmt.setInt(4, veiculo.getModelo().getId());
            stmt.setInt(5, veiculo.getCliente().getId());
            stmt.execute();

            connection.commit();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(VeiculoDAO.class.getName()).log(Level.SEVERE, null, ex);
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

    public boolean alterar(Veiculo veiculo) {
        String sql = "UPDATE veiculo SET placa=?, observacoes=?, id_cor=?, id_modelo=?, id_cliente=?  WHERE id=?";
        Database database = DatabaseFactory.getDatabase("mysql");
        Connection connection = database.conectar();
        try {
            connection.setAutoCommit(false);
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, veiculo.getPlaca());
            stmt.setString(2, veiculo.getObservacoes());
            stmt.setInt(3, veiculo.getCor().getId());
            stmt.setInt(4, veiculo.getModelo().getId());
            stmt.setInt(5, veiculo.getCliente().getId());
            stmt.setInt(6, veiculo.getId());
            stmt.execute();
            connection.commit();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(VeiculoDAO.class.getName()).log(Level.SEVERE, null, ex);
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

    public boolean remover(Veiculo veiculo) {
        String sql = "DELETE FROM veiculo WHERE id=?";
        Database database = DatabaseFactory.getDatabase("mysql");
        Connection connection = database.conectar();
        try {
            connection.setAutoCommit(false);
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, veiculo.getId());
            stmt.execute();
            connection.commit();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(VeiculoDAO.class.getName()).log(Level.SEVERE, null, ex);
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

    public List<Veiculo> listar() {
        String sql = "SELECT veiculo.id AS veiculo_id, veiculo.placa AS veiculo_placa, veiculo.observacoes AS veiculo_observacoes, cor.id AS cor_id, cor.nome AS cor_nome, " +
                "marca.id AS marca_id, marca.nome AS marca_nome, modelo.id AS modelo_id, modelo.descricao AS modelo_descricao, modelo.categoria " +
                "AS modelo_categoria, motor.potencia AS motor_potencia, motor.tipo_combustivel AS motor_combustivel, " +
                "cliente.id AS cliente_id, cliente.nome AS cliente_nome, pj.cnpj AS cliente_cnpj FROM veiculo" +
                " INNER JOIN cor ON veiculo.id_cor = cor.id" +
                " INNER JOIN modelo ON veiculo.id_modelo = modelo.id"+
                " INNER JOIN marca ON modelo.id_marca = marca.id" +
                " INNER JOIN motor ON modelo.id = motor.id_modelo" +
                " INNER JOIN cliente ON veiculo.id_cliente = cliente.id" +
                " LEFT JOIN pessoaJuridica pj ON veiculo.id_cliente = pj.id_cliente;";

        Database database = DatabaseFactory.getDatabase("mysql");
        Connection connection = database.conectar();

        List<Veiculo> veiculosRetonados = new ArrayList<>();

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();

            while (resultado.next()) {
                Veiculo veiculo = new Veiculo();
                veiculo.setId(resultado.getInt("veiculo_id"));
                veiculo.setPlaca(resultado.getString("veiculo_placa"));
                veiculo.setObservacoes(resultado.getString("veiculo_observacoes"));

                //Associando Unidirecional com Cor
                Cor cor = new Cor();
                cor.setId(resultado.getInt("cor_id"));
                cor.setNome(resultado.getString("cor_nome"));
                veiculo.setCor(cor);

                //Associando Por Agregação com Modelo, Modelo tem associação com Marca e Composição com Motor.
                Marca marca = new Marca();
                marca.setId(resultado.getInt("marca_id"));
                marca.setNome(resultado.getString("marca_nome"));
                Modelo modelo = new Modelo(resultado.getInt("motor_potencia"), ETipoCombustivel.valueOf(resultado.getString("motor_combustivel")));
                modelo.setId(resultado.getInt("modelo_id"));
                modelo.setDescricao(resultado.getString("modelo_descricao"));
                modelo.setMarca(marca);
                modelo.setCategoria(ECategoria.valueOf(resultado.getString("modelo_categoria")));
                veiculo.setModelo(modelo);

                //Associando o Cliente e identificando qual tipo é atraves da FLAG cnpj:
                String cnpj = resultado.getString("cliente_cnpj");
                Cliente cliente;
                if(cnpj == null || cnpj.isEmpty()){
                    cliente = new PessoaFisica();
                    cliente.setId(resultado.getInt("cliente_id"));
                    cliente.setNome(resultado.getString("cliente_nome"));
                }else {
                    cliente = new PessoaJuridica();
                    cliente.setId(resultado.getInt("cliente_id"));
                    cliente.setNome(resultado.getString("cliente_nome"));
                }
                veiculo.setCliente(cliente);

                veiculosRetonados.add(veiculo);
            }
        } catch (SQLException ex) {
            Logger.getLogger(VeiculoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
            database.desconectar(connection);
        }
        return veiculosRetonados;
    }

    public Veiculo buscar(Veiculo veiculo) {
        Veiculo veiculoRetonado = buscar(veiculo.getId());
        return veiculoRetonado;
    }

    public Veiculo buscar(int id) {
        String sql = "SELECT veiculo.id AS veiculo_id, veiculo.placa AS veiculo_placa, veiculo.observacoes AS veiculo_observacoes, cor.id AS cor_id, cor.nome AS cor_nome, " +
                "marca.id AS marca_id, marca.nome AS marca_nome, modelo.id AS modelo_id, modelo.descricao AS modelo_descricao, modelo.categoria " +
                "AS modelo_categoria, motor.potencia AS motor_potencia, motor.tipo_combustivel AS motor_combustivel, " +
                "cliente.id AS cliente_id, cliente.nome AS cliente nome, pj.cnpj AS cliente_cnpj FROM veiculo " +
                " INNER JOIN cor ON veiculo.id_cor = cor.id" +
                " INNER JOIN modelo ON veiculo.id_modelo = modelo.id"+
                " INNER JOIN marca ON modelo.id_marca = marca.id" +
                " INNER JOIN motor    ON modelo.id = motor.id_modelo" +
                " INNER JOIN cliente  ON veiculo.id_cliente = cliente.id" +
                " LEFT JOIN pessoaJuridica pj ON veiculo.id_cliente = pj.id_cliente WHERE veiculo.id=?;";

        Database database = DatabaseFactory.getDatabase("mysql");
        Connection connection = database.conectar();

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet resultado = stmt.executeQuery();
            Veiculo veiculoRetorno = new Veiculo();

            if (resultado.next()) {
                Veiculo veiculo = new Veiculo();
                veiculo.setId(resultado.getInt("veiculo_id"));
                veiculo.setPlaca(resultado.getString("veiculo_placa"));
                veiculo.setObservacoes(resultado.getString("veiculo_observacoes"));

                //Associando Unidirecional com Cor
                Cor cor = new Cor();
                cor.setId(resultado.getInt("cor_id"));
                cor.setNome(resultado.getString("cor_nome"));
                veiculo.setCor(cor);

                //Associando Por Agregação com Modelo, Modelo tem associação com Marca e Composição com Motor.
                Marca marca = new Marca();
                marca.setId(resultado.getInt("marca_id"));
                marca.setNome(resultado.getString("marca_nome"));
                Modelo modelo = new Modelo(resultado.getInt("motor_potencia"), ETipoCombustivel.valueOf(resultado.getString("motor_combustivel")));
                modelo.setId(resultado.getInt("modelo_id"));
                modelo.setDescricao(resultado.getString("modelo_descricao"));
                modelo.setMarca(marca);
                modelo.setCategoria(ECategoria.valueOf(resultado.getString("modelo_categoria")));
                veiculo.setModelo(modelo);

                //Associando o Cliente e identificando qual tipo é atraves da FLAG cnpj:
                String cnpj = resultado.getString("cliente_cnpj");
                Cliente cliente;
                if(cnpj == null || cnpj.isEmpty()){
                    cliente = new PessoaFisica();
                    cliente.setId(resultado.getInt("cliente_id"));
                    cliente.setNome(resultado.getString("cliente_nome"));
                }else {
                    cliente = new PessoaJuridica();
                    cliente.setId(resultado.getInt("cliente_id"));
                    cliente.setNome(resultado.getString("cliente_nome"));
                }
                veiculo.setCliente(cliente);
            }
            return veiculoRetorno;
        } catch (SQLException ex) {
            Logger.getLogger(VeiculoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
            database.desconectar(connection);
        }
        return null;
    }

    public List<Veiculo> buscarVeiculoCliente(int id) {
        String sql = "SELECT v.id AS veiculo_id, v.placa AS veiculo_placa, v.observacoes AS veiculo_observacoes, " +
                "c.id AS cor_id, c.nome AS cor_nome, ma.id AS marca_id, ma.nome AS marca_nome, m.id AS modelo_id, m.descricao AS modelo_descricao, " +
                "m.categoria AS modelo_categoria, mo.potencia AS motor_potencia, mo.tipo_combustivel AS motor_combustivel " +
                "FROM veiculo v " +
                "INNER JOIN cor c ON v.id_cor = c.id " +
                "INNER JOIN modelo m ON v.id_modelo = m.id " +
                "INNER JOIN marca ma ON m.id_marca = ma.id " +
                "INNER JOIN motor mo ON m.id = mo.id_modelo " +
                "WHERE v.id_cliente = ?;";

        Database database = DatabaseFactory.getDatabase("mysql");
        Connection connection = database.conectar();

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet resultado = stmt.executeQuery();
            List<Veiculo> veiculosRetorno = new ArrayList<>();

            while (resultado.next()) {
                Veiculo veiculo = new Veiculo();
                veiculo.setId(resultado.getInt("veiculo_id"));
                veiculo.setPlaca(resultado.getString("veiculo_placa"));
                veiculo.setObservacoes(resultado.getString("veiculo_observacoes"));

                //Associando Unidirecional com Cor
                Cor cor = new Cor();
                cor.setId(resultado.getInt("cor_id"));
                cor.setNome(resultado.getString("cor_nome"));
                veiculo.setCor(cor);

                //Associando Por Agregação com Modelo, Modelo tem associação com Marca e Composição com Motor.
                Marca marca = new Marca();
                marca.setId(resultado.getInt("marca_id"));
                marca.setNome(resultado.getString("marca_nome"));
                Modelo modelo = new Modelo(resultado.getInt("motor_potencia"), ETipoCombustivel.valueOf(resultado.getString("motor_combustivel")));
                modelo.setId(resultado.getInt("modelo_id"));
                modelo.setDescricao(resultado.getString("modelo_descricao"));
                modelo.setMarca(marca);
                modelo.setCategoria(ECategoria.valueOf(resultado.getString("modelo_categoria")));

                veiculo.setModelo(modelo);

                veiculosRetorno.add(veiculo);
            }
            return veiculosRetorno;
        } catch (SQLException ex) {
            Logger.getLogger(VeiculoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
            database.desconectar(connection);
        }
        return null;
    }
}
