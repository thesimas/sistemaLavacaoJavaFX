package br.edu.ifsc.fln.sistemalavacaojavafx.model.dao;

import br.edu.ifsc.fln.sistemalavacaojavafx.model.database.Database;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.database.DatabaseFactory;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.domain.ItemOS;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.domain.OrdemServico;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.domain.Veiculo;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.exceptions.ExceptionLavacao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrdemServicoDAO {

    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean inserir(OrdemServico ordemServico){
        String sql = "INSERT INTO ordem_de_servico (numero, total, data_agendamento, desconto, status, id_veiculo) VALUES (?, ?, ?, ?, ?, ?)";
        Database database = DatabaseFactory.getDatabase("mysql");
        Connection connection = database.conectar();
        try {
            connection.setAutoCommit(false);
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setLong(1, ordemServico.getNumero());
            try {
                stmt.setDouble(2, ordemServico.getTotal());
                // Chama o metodo calcular Serviço, que já pecorrer todos os itens da OS.
            } catch (ExceptionLavacao e) {
                throw new RuntimeException(e);
            }
            stmt.setDate(3, Date.valueOf(ordemServico.getDataAgendamento()));
            stmt.setDouble(4, ordemServico.getDesconto());
            stmt.setString(5, ordemServico.getStatus().name());
            stmt.setInt(6, ordemServico.getVeiculo().getId());
            // Inserindo os itens da OS no banco.
            ItemOSDAO itemOSDAO = new ItemOSDAO();
            for(ItemOS itemOS : ordemServico.getItensOS()){
                itemOSDAO.inserir(itemOS);
                try {
                    ordemServico.addItemOS(itemOS.getObservacoes(), itemOS.getServico());
                }catch (ExceptionLavacao e) {
                    throw new RuntimeException(e);
                }
            }
            stmt.execute();
            connection.commit();
            return true;
        }catch(SQLException ex){
            Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
            try{
                connection.rollback();
            }catch(SQLException ex1){
                throw new RuntimeException(ex1);
            }
            return false;
        }finally{
            database.desconectar(connection);
        }
    }

    public boolean alterar (OrdemServico ordemServico){
        String sql = "UPDATE ordem_de_servico SET numero=?, total=?, data_agendamento=?, desconto=?, status=?, id_veiculo=? WHERE numero=?";
        Database database = DatabaseFactory.getDatabase("mysql");
        Connection connection = database.conectar();
        try {
            connection.setAutoCommit(false);
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setLong(1, ordemServico.getNumero());
            try {
                stmt.setDouble(2, ordemServico.getTotal());
                // Chama o metodo calcular Serviço, que já pecorrer todos os itens da OS.
            } catch (ExceptionLavacao e) {
                throw new RuntimeException(e);
            }
            stmt.setDate(3, Date.valueOf(ordemServico.getDataAgendamento()));
            stmt.setDouble(4, ordemServico.getDesconto());
            stmt.setString(5, ordemServico.getStatus().name());
            stmt.setInt(6, ordemServico.getVeiculo().getId());
            stmt.setLong(7, ordemServico.getNumero());
            stmt.execute();
            connection.commit();
            return true;
        }catch(SQLException ex){
            Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
            try{
                connection.rollback();
            }catch(SQLException ex1){
                throw new RuntimeException(ex1);
            }
            return false;
        }finally{
            database.desconectar(connection);
        }
    }

    public boolean excluir (OrdemServico ordemServico){
        String sql = "DELETE FROM ordem_de_servico WHERE numero=?";
        Database database = DatabaseFactory.getDatabase("mysql");
        Connection connection = database.conectar();
        try {
            connection.setAutoCommit(false);
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setLong(1, ordemServico.getNumero());
            stmt.execute();
            connection.commit();
            return true;
        }catch(SQLException ex){
            Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
            try{
                connection.rollback();
            }catch(SQLException ex1){
                throw new RuntimeException(ex1);
            }
            return false;
        }finally{
            database.desconectar(connection);
        }
    }

    public List<OrdemServico> listar (){
        String sql = "SELECT * FROM ordem_de_servico;";
        Database database = DatabaseFactory.getDatabase("mysql");
        Connection connection = database.conectar();
        try {
            connection.setAutoCommit(false);
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            List<OrdemServico> ordemServicos = new ArrayList<>();
            while (resultado.next()) {
                OrdemServico ordemServico = new OrdemServico();
                ordemServico.setNumero(resultado.getLong("numero"));
                ordemServico.setTotal(resultado.getDouble("total"));
                // Associando o Veiculo a ordem de serviço.
                Integer idVeiculo = resultado.getInt("id_veiculo");
                VeiculoDAO veiculoDAO = new VeiculoDAO();
                ordemServico.setVeiculo(veiculoDAO.buscar(idVeiculo));
                // Associar os itens da OS.
                ItemOSDAO itemOSDAO = new ItemOSDAO();
                for(ItemOS itemOS : ordemServico.getItensOS()){
                    itemOSDAO.inserir(itemOS);
                }
                ordemServicos.add(ordemServico);
            }
            stmt.execute();
            connection.commit();
            return ordemServicos;
        }catch(SQLException ex){
            Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
            try{
                connection.rollback();
            }catch(SQLException ex1){
                throw new RuntimeException(ex1);
            }
            return null;
        }finally{
            database.desconectar(connection);
        }
    }
}