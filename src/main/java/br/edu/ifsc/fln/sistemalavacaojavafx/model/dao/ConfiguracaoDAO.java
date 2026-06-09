package br.edu.ifsc.fln.sistemalavacaojavafx.model.dao;

import br.edu.ifsc.fln.sistemalavacaojavafx.model.database.Database;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.database.DatabaseFactory;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.domain.Configuracao;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.domain.ECategoria;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.exceptions.DAOException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConfiguracaoDAO {

    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void alterar(Configuracao configuracao) throws DAOException {
        String sql = "UPDATE configuracao_sistema SET pontos=?, porcentagem_pequeno=?, porcentagem_medio=?, porcentagem_grande=?, porcentagem_moto=? WHERE id=?";
        Database database = DatabaseFactory.getDatabase("mysql");
        Connection connection = database.conectar();
        try {
            connection.setAutoCommit(false);
            PreparedStatement stmt = connection.prepareStatement(sql);
            HashMap<ECategoria, Double> porcentagens = configuracao.getPorcentagens();
            stmt.setInt(1, configuracao.getPontos());
            stmt.setDouble(2, porcentagens.get(ECategoria.PEQUENO));
            stmt.setDouble(3, porcentagens.get(ECategoria.MEDIO));
            stmt.setDouble(4, porcentagens.get(ECategoria.GRANDE));
            stmt.setDouble(5, porcentagens.get(ECategoria.MOTO));
            stmt.setInt(6, configuracao.getId());
            stmt.execute();
            connection.commit();
        } catch (SQLException ex) {
            Logger.getLogger(ConfiguracaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            try{
                connection.rollback();
            }catch(SQLException ex1){
                throw new RuntimeException(ex1);
            }
            throw new DAOException("Não foi possível alterar as configurações no banco de dados!\nMotivo: ", ex);
        }finally {
            database.desconectar(connection);
        }
    }

    public Configuracao buscar(int id) throws DAOException {
        String sql = "SELECT * FROM configuracao_sistema WHERE id=?";
        Database database = DatabaseFactory.getDatabase("mysql");
        Connection connection = database.conectar();

        Configuracao configuracaoRetornada = new Configuracao();
        try {
            connection.setAutoCommit(false);
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                double porcentagem_pequeno = resultado.getDouble("porcentagem_pequeno");
                double porcentagem_medio = resultado.getDouble("porcentagem_medio");
                double porcentagem_grande = resultado.getDouble("porcentagem_grande");
                double porcentagem_moto = resultado.getDouble("porcentagem_moto");
                int pontos = resultado.getInt("pontos");

                HashMap<ECategoria, Double> map = new HashMap<>();
                map.put(ECategoria.PEQUENO, porcentagem_pequeno);
                map.put(ECategoria.MEDIO, porcentagem_medio);
                map.put(ECategoria.GRANDE, porcentagem_grande);
                map.put(ECategoria.MOTO, porcentagem_moto);

                configuracaoRetornada.setId(id);
                configuracaoRetornada.setPontos(pontos);
                configuracaoRetornada.setPorcentagens(map);
            }
            connection.commit();
        } catch (SQLException ex) {
            Logger.getLogger(ConfiguracaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            try {
                connection.rollback();
            }catch(SQLException ex1){
                throw new RuntimeException(ex1);
            }
            throw new DAOException("Não foi possível buscar as configurações no banco de dados!\nMotivo: ", ex);
        }finally {
            database.desconectar(connection);
        }
        return configuracaoRetornada;
    }

}
