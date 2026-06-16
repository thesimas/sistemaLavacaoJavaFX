package br.edu.ifsc.fln.sistemalavacaojavafx.model.dao;

import br.edu.ifsc.fln.sistemalavacaojavafx.model.database.Database;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.database.DatabaseFactory;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.domain.*;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.exceptions.DAOException;

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

    public void inserir(Cliente cliente) throws DAOException {
        String sqlCliente = "INSERT INTO cliente(nome, email, celular, data_cadastro) VALUES(?, ?, ?, ?);";
        String sqlPessoaFisica = "INSERT INTO pessoa_fisica(id_cliente, cpf, data_nascimento) VALUES((SELECT max(id) FROM cliente), ?, ?);";
        String sqlPessoaJuridica = "INSERT INTO pessoa_juridica(id_cliente, cnpj, inscricao_estadual) VALUES((SELECT max(id) FROM cliente), ?, ?);";
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
            stmt.setInt(1, cliente.getPontuacao().getQuantidade());
            stmt.execute();
            connection.commit();
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
            try{
                connection.rollback();
            }catch(SQLException ex1){
                throw new RuntimeException(ex1);
            }
            throw new DAOException("Não foi possível inserir o cliente no banco de dados!\nMotivo: ", ex);
        }finally {
            database.desconectar(connection);
        }
    }

    public void alterar(Cliente cliente) throws DAOException {
        String sqlCliente = "UPDATE cliente SET nome=?, celular=?, email=?, data_cadastro=?  WHERE id=?";
        String sqlPessoaFisica = "UPDATE pessoa_fisica SET cpf=?, data_nascimento=?  WHERE id_cliente=?";
        String sqlPessoaJuridica = "UPDATE pessoa_juridica SET cnpj=?, inscricao_estadual=?  WHERE id_cliente=?";
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
            stmt.setInt(1, cliente.getPontuacao().getQuantidade());
            stmt.setInt(2, cliente.getId());
            stmt.execute();
            connection.commit();
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
            try{
                connection.rollback();
            }catch(SQLException ex1){
                throw new RuntimeException(ex1);
            }
            throw new DAOException("Não foi possível alterar o cliente no banco de dados!\nMotivo: ", ex);
        }finally {
            database.desconectar(connection);
        }
    }

    public void remover(Cliente cliente) throws DAOException {
        String sql = "DELETE FROM cliente WHERE id=?";
        Database database = DatabaseFactory.getDatabase("mysql");
        Connection connection = database.conectar();
        try {
            connection.setAutoCommit(false);
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, cliente.getId());
            stmt.execute();
            connection.commit();
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
            try{
                connection.rollback();
            }catch(SQLException ex1){
                throw new RuntimeException(ex1);
            }
            throw new DAOException("Não foi possível remover o cliente no banco de dados!\nMotivo: ", ex);
        }finally {
            database.desconectar(connection);
        }
    }

    public List<Cliente> listar() throws DAOException {
        String sql = "SELECT cliente.id AS id_principal, cliente.*, pessoa_fisica.*, pessoa_juridica.*, pontuacao.* FROM cliente " +
                "LEFT JOIN pessoa_fisica ON cliente.id = pessoa_fisica.id_cliente " +
                "LEFT JOIN pessoa_juridica ON cliente.id = pessoa_juridica.id_cliente " +
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
            stmt.close();
            resultado.close();
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Não foi possível listar os clientes no banco de dados!\nMotivo: ", ex);
        }finally {
            database.desconectar(connection);
        }
        return retorno;
    }

    public Cliente buscar(Cliente cliente) throws DAOException {
        Cliente retorno = buscar(cliente.getId());
        return retorno;
    }

    public Cliente buscar(int id) throws DAOException {
        String sql = "SELECT cliente.id AS id_principal, cliente.*, pessoa_fisica.*, pessoa_juridica.*, pontuacao.* FROM cliente " +
                "LEFT JOIN pessoa_fisica ON pessoa_fisica.id_cliente = cliente.id " +
                "LEFT JOIN pessoa_juridica ON pessoa_juridica.id_cliente = cliente.id " +
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
            stmt.close();
            resultado.close();
            return clienteRetorno;
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Não foi possível buscar o cliente no banco de dados!\nMotivo: ", ex);
        }finally {
            database.desconectar(connection);
        }
    }

    public Cliente povoarDados(ResultSet rs) throws SQLException {
        Cliente cliente;
        int id =  rs.getInt("id_principal");
        String nome = rs.getString("nome");
        String celular = rs.getString("celular");
        String email = rs.getString("email");
        Date dataCadastro = rs.getDate("data_cadastro");
        int pontuacao = rs.getInt("quantidade");

        if(rs.getString("cnpj") == null || rs.getString("cnpj").isEmpty()){

            String cpf = rs.getString("cpf");
            Date dataNascimento = rs.getDate("data_nascimento");
            cliente = new PessoaFisica(id, nome, celular, email, dataCadastro.toLocalDate(), cpf, dataNascimento.toLocalDate());
        }else {
            String cnpj = rs.getString("cnpj");
            String inscricaoEstadual = rs.getString("inscricao_estadual");
            cliente = new PessoaJuridica(id, nome, celular, email, dataCadastro.toLocalDate(), cnpj, inscricaoEstadual);
        }
        //Associando os pontos ao cliente.
        cliente.getPontuacao().setQuantidade(pontuacao);

        return cliente;
    }
}
