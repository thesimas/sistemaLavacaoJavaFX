package br.edu.ifsc.fln.sistemalavacaojavafx.model.dao;

import br.edu.ifsc.fln.sistemalavacaojavafx.model.database.Database;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.database.DatabaseFactory;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.domain.*;
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
        String sqlItemOs = "INSERT INTO item_os (numero_ordem_de_servico, valor_servico, observacoes, id_servico) VALUES (?, ?, ?, ?)";
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
            stmt.execute();

            // Inserindo os itens da OS no banco.
            for(ItemOS item : ordemServico.getItensOS()){
                stmt = connection.prepareStatement(sqlItemOs);
                stmt.setLong(1, item.getOrdemServico().getNumero());
                stmt.setDouble(2, item.getValorServico());
                stmt.setString(3, item.getObservacoes());
                stmt.setInt(4, item.getServico().getId());
                stmt.execute();
            }
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
        String sqlDeleteItens = "DELETE FROM item_os WHERE numero_ordem_de_servico = ?";
        String sqlItemOs = "INSERT INTO item_os (numero_ordem_de_servico, valor_servico, observacoes, id_servico) VALUES (?, ?, ?, ?)";
        Database database = DatabaseFactory.getDatabase("mysql");
        Connection connection = database.conectar();
        try {
            connection.setAutoCommit(false);
            PreparedStatement stmt = connection.prepareStatement(sqlDeleteItens);
            stmt.setLong(1, ordemServico.getNumero());
            stmt.execute();

            stmt = connection.prepareStatement(sql);
            stmt.setLong(1, ordemServico.getNumero());
            try {
                stmt.setDouble(2, ordemServico.getTotal());
            } catch (ExceptionLavacao e) {
                throw new RuntimeException(e);
            }
            stmt.setDate(3, Date.valueOf(ordemServico.getDataAgendamento()));
            stmt.setDouble(4, ordemServico.getDesconto());
            stmt.setString(5, ordemServico.getStatus().name());
            stmt.setInt(6, ordemServico.getVeiculo().getId());
            stmt.setLong(7, ordemServico.getNumero());
            stmt.execute();

            // Inserindo os itens da OS no banco.
            for(ItemOS item : ordemServico.getItensOS()){
                stmt = connection.prepareStatement(sqlItemOs);
                stmt.setLong(1, item.getOrdemServico().getNumero());
                stmt.setDouble(2, item.getValorServico());
                stmt.setString(3, item.getObservacoes());
                stmt.setInt(4, item.getServico().getId());
                stmt.execute();
            }

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
        String sql = "SELECT os.*, v.placa, v.id_cliente, c.nome AS nome_cliente, pf.cpf, m.descricao AS modelo_descricao, ma.nome as marca_nome, m.categoria as modelo_categoria, p.quantidade as pontuacao FROM ordem_de_servico os " +
                "INNER JOIN veiculo v ON os.id_veiculo = v.id " +
                "INNER JOIN cliente c ON v.id_cliente = c.id " +
                "INNER JOIN modelo m ON v.id_modelo = m.id " +
                "INNER JOIN marca ma ON m.id_marca = ma.id " +
                "LEFT JOIN pessoa_fisica pf ON c.id = pf.id_cliente " +
                "LEFT JOIN pessoa_juridica pj ON c.id = pj.id_cliente " +
                "LEFT JOIN pontuacao p ON c.id = p.id_cliente";

        Database database = DatabaseFactory.getDatabase("mysql");
        Connection connection = database.conectar();
        try {
            connection.setAutoCommit(false);
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            List<OrdemServico> ordensServicos = new ArrayList<>();
            while (resultado.next()) {
                OrdemServico ordemServico = new OrdemServico();
                ordemServico.setNumero(resultado.getLong("numero"));
                ordemServico.setTotal(resultado.getDouble("total"));
                ordemServico.setStatus(EStatus.valueOf(resultado.getString("status")));
                ordemServico.setDataAgendamento(resultado.getDate("data_agendamento").toLocalDate());
                ordemServico.setDesconto(resultado.getDouble("desconto"));
                // Associando o Veiculo a Ordem de Serviço;
                Veiculo veiculo = new Veiculo();
                veiculo.setId(resultado.getInt("id_veiculo"));
                veiculo.setPlaca(resultado.getString("placa"));
                // Associando o Cliente ao Veiculo;
                Cliente cliente;
                if(resultado.getString("cpf") != null){
                    // CPF é apenas uma flag para saber qual tipo de objeto é.
                    cliente = new PessoaFisica();
                    cliente.setId(resultado.getInt("id_cliente"));
                    cliente.setNome(resultado.getString("nome_cliente"));
                }else {
                    cliente = new PessoaJuridica();
                    cliente.setId(resultado.getInt("id_cliente"));
                    cliente.setNome(resultado.getString("nome_cliente"));
                }
                try {
                    cliente.getPontuacao().adicionar(resultado.getInt("pontuacao"));
                } catch (ExceptionLavacao e) {
                    throw new RuntimeException(e);
                }
                // Associando Marca ao modelo;
                Marca marca = new Marca();
                marca.setNome(resultado.getString("marca_nome"));
                // Associando o Modelo ao Veiculo;
                Modelo modelo = new Modelo(0, ETipoCombustivel.GASOLINA); // Dados ficticios;
                modelo.setDescricao(resultado.getString("modelo_descricao"));
                modelo.setCategoria(ECategoria.valueOf(resultado.getString("modelo_categoria")));
                modelo.setMarca(marca);
                veiculo.setModelo(modelo);
                veiculo.setCliente(cliente);
                ordemServico.setVeiculo(veiculo);

                ordensServicos.add(ordemServico);
            }
            stmt.execute();
            connection.commit();
            return ordensServicos;
        }catch(SQLException ex){
            Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
            try{
                connection.rollback();
            }catch(SQLException ex1){
                throw new RuntimeException(ex1);
            }
            return new ArrayList<>();
        }finally{
            database.desconectar(connection);
        }
    }

    public void carregarItemOS(OrdemServico ordemServico){
        String sql = "SELECT ios.*, s.descricao as servico_descricao, s.valor as valor_servico, s.categoria " +
                "FROM item_os ios " +
                "INNER JOIN servico s ON ios.id_servico = s.id " +
                "WHERE ios.numero_ordem_de_servico = ?";

        Database database = DatabaseFactory.getDatabase("mysql");
        Connection connection = database.conectar();

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setLong(1, ordemServico.getNumero());
            ResultSet resultado = stmt.executeQuery();
            // Limpando a lista de ordem de servico, para evitar erros de duplicação, pois os dados serão pegos do banco.
            ordemServico.getItensOS().clear();
            while (resultado.next()) {
                Servico servico = new Servico();
                servico.setId(resultado.getInt("id_servico"));
                servico.setDescricao(resultado.getString("servico_descricao"));
                servico.setValor(resultado.getDouble("valor_servico"));
                servico.setCategoria(ECategoria.valueOf(resultado.getString("categoria")));

                double valorServico = resultado.getDouble("valor_servico");
                String observacoes = resultado.getString("observacoes");

                ItemOS item;
                if(valorServico != 0){
                    item = new ItemOS(valorServico, observacoes, ordemServico, servico);
                } else if (observacoes != null) {
                    item = new ItemOS(observacoes, ordemServico, servico);
                } else {
                    item = new ItemOS(ordemServico, servico);
                }

                ordemServico.getItensOS().add(item);
            }
        }catch(SQLException ex){
            Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
            try{
                connection.rollback();
            }catch(SQLException ex1){
                throw new RuntimeException(ex1);
            }
        }finally{
            database.desconectar(connection);
        }
    }
}