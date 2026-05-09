package br.edu.ifsc.fln.sistemalavacaojavafx.model.dao;

import br.edu.ifsc.fln.sistemalavacaojavafx.model.database.Database;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.database.DatabaseFactory;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.domain.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClienteDAO {

    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean inserir(Cliente cliente) {
        String sqlCliente = "INSERT INTO cliente(nome, email, celular, email, data_cadastro) VALUES(?, ?, ?, ?);";
        String sqlPessoaFisica = "INSERT INTO pessoaFisica(id_cliente, cpf, data_nascimento) VALUES((SELECT max(id) FROM cliente), ?, ?);";
        String sqlPessoaJuridica = "INSERT INTO pessoaJuridica(id_cliente, cnpj, inscricao_estadual) VALUES((SELECT max(id) FROM cliente), ?, ?);";
        String sqlPontuacao = "INSERT INTO pontuacao(id_cliente, quantidade) VALUES((SELECT max(id) FROM cliente), ?);";

        Database database = DatabaseFactory.getDatabase("mysql");
        Connection connection = database.conectar();
        try {
            connection.setAutoCommit(false);
            PreparedStatement stmt = connection.prepareStatement(sqlCliente);
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getEmail());
            stmt.setString(3, cliente.getCelular());
            stmt.setString(4, String.valueOf(cliente.getDataCadastro()));
            stmt.execute();
            if(cliente instanceof PessoaFisica){
                stmt = connection.prepareStatement(sqlPessoaFisica);
                stmt.setString(1, ((PessoaFisica)cliente).getCpf());
                stmt.setString(2, String.valueOf(((PessoaFisica)cliente).getDataNascimento()));
                stmt.execute();
            }else {
                stmt = connection.prepareStatement(sqlPessoaJuridica);
                stmt.setString(1, ((PessoaJuridica)cliente).getCnpj());
                stmt.setString(2, ((PessoaJuridica)cliente).getInscricaoEstadual());
                stmt.execute();
            }
            stmt =  connection.prepareStatement(sqlPontuacao);
            stmt.setString(1, String.valueOf(cliente.getPontuacao()));
            stmt.execute();
            connection.commit();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
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

    public boolean alterar(Cliente cliente) {
        String sqlCliente = "UPDATE cliente SET nome=?, celular=?, email=?, data_cadastro=?  WHERE id=?";
        String sqlPessoaFisica = "UPDATE pessoaFisica SET cpf=?, data_nascimento=?  WHERE id_cliente=?";
        String sqlPessoaJuridica = "UPDATE pessoaJuridica SET cnpj=?, inscricao_estadual=?  WHERE id_cliente=?";
        String sqlPontuacao =  "UPDATE pontuacao SET quantidade=? WHERE id_cliente=?";

        Database database = DatabaseFactory.getDatabase("mysql");
        Connection connection = database.conectar();
        try {
            connection.setAutoCommit(false);
            PreparedStatement stmt = connection.prepareStatement(sqlCliente);
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getCelular());
            stmt.setString(3, cliente.getEmail());
            stmt.setString(4, String.valueOf(cliente.getDataCadastro()));
            stmt.setInt(5, cliente.getId());
            stmt.execute();
            if(cliente instanceof PessoaFisica){
                stmt = connection.prepareStatement(sqlPessoaFisica);
                stmt.setString(1, ((PessoaFisica)cliente).getCpf());
                stmt.setString(2, String.valueOf(((PessoaFisica)cliente).getDataNascimento()));
                stmt.setInt(3, ((PessoaFisica)cliente).getId());
                stmt.execute();
            }else {
                stmt = connection.prepareStatement(sqlPessoaJuridica);
                stmt.setString(1, ((PessoaJuridica)cliente).getCnpj());
                stmt.setString(2, ((PessoaJuridica)cliente).getInscricaoEstadual());
                stmt.setInt(3, ((PessoaJuridica)cliente).getId());
                stmt.execute();
            }
            stmt = connection.prepareStatement(sqlPontuacao);
            stmt.setString(1, String.valueOf(cliente.getPontuacao().getQuantidade()));
            stmt.setInt(2, cliente.getId());
            stmt.execute();
            connection.commit();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
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

    public boolean remover(Cliente cliente) {
        String sql = "DELETE FROM cliente WHERE id=?";
        Database database = DatabaseFactory.getDatabase("mysql");
        Connection connection = database.conectar();
        try {
            connection.setAutoCommit(false);
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, cliente.getId());
            stmt.execute();
            connection.commit();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
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

    public List<Cliente> listar() {
        String sql = "SELECT cliente.id AS id_principal, cliente.*, pessoaFisica.*, pessoaJuridica.*, pontuacao.* FROM cliente " +
                "LEFT JOIN pessoaFisica ON cliente.id = pessoaFisica.id_cliente " +
                "LEFT JOIN pessoaJuridica ON cliente.id = pessoaJuridica.id_cliente " +
                "LEFT JOIN pontuacao ON cliente.id = pontuacao.id_cliente;";

        Database database = DatabaseFactory.getDatabase("mysql");
        Connection connection = database.conectar();

        List<Cliente> retorno = new ArrayList<>();

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                Cliente cliente = povoarDados(resultado);
                retorno.add(cliente);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
            database.desconectar(connection);
        }
        return retorno;
    }

    public Cliente buscar(Cliente cliente) {
        Cliente retorno = buscar(cliente.getId());
        return retorno;
    }

    public Cliente buscar(int id) {
        String sql = "SELECT cliente.id AS id_principal, cliente.*, pessoaFisica.*, pessoaJuridica.*, pontuacao.* FROM cliente " +
                "LEFT JOIN pessoaFisica ON pessoaFisica.id_cliente = cliente.id " +
                "LEFT JOIN pessoaJuridica ON pessoaJuridica.id_cliente = cliente.id " +
                "LEFT JOIN pontuacao ON pontuacao.id_cliente = cliente.id WHERE id=?;";
        Database database = DatabaseFactory.getDatabase("mysql");
        Connection connection = database.conectar();

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet resultado = stmt.executeQuery();
            Cliente clienteRetorno = null;
            if (resultado.next()) {
                clienteRetorno = povoarDados(resultado);
            }
            return clienteRetorno;
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
            database.desconectar(connection);
        }
        return null;
    }

    public Cliente povoarDados(ResultSet rs) throws SQLException {
        Cliente cliente;
        int id =  rs.getInt("id_principal");
        String nome = rs.getString("nome");
        String celular = rs.getString("celular");
        String email = rs.getString("email");
        Date dataCadastro = rs.getDate("data_cadastro");

        if(rs.getString("cnpj") == null || rs.getString("cnpj").isEmpty()){

            String cpf = rs.getString("cpf");
            Date dataNascimento = rs.getDate("data_nascimento");
            cliente = new PessoaFisica(id, nome, celular, email, dataCadastro.toLocalDate(), cpf, dataNascimento.toLocalDate());
        }else {
            String cnpj = rs.getString("cnpj");
            String inscricaoEstadual = rs.getString("inscricao_estadual");
            cliente = new PessoaJuridica(id, nome, celular, email, dataCadastro.toLocalDate(), cnpj, inscricaoEstadual);
        }
        // Associando o Cliente ao veiculo.
        VeiculoDAO veiculoDAO = new VeiculoDAO();
        List<Veiculo> veiculos = veiculoDAO.listar();

        for (int x = 0; x < veiculos.size(); x++) {
            Veiculo veiculo = veiculos.get(x);
            cliente.addVeiculo(veiculo);
        }

        return cliente;
    }
}
