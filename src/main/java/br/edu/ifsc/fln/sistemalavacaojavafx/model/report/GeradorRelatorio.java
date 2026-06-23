package br.edu.ifsc.fln.sistemalavacaojavafx.model.report;

import br.edu.ifsc.fln.sistemalavacaojavafx.model.database.Database;
import br.edu.ifsc.fln.sistemalavacaojavafx.model.database.DatabaseFactory;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

public class GeradorRelatorio {

    public void imprimirRelatorio(String caminhoRelatorio, String tituloJanela, HashMap<String, Object> parametros) {
        Database database = DatabaseFactory.getDatabase("mysql");
        Connection connection = database.conectar();

        try {
            URL url = getClass().getResource(caminhoRelatorio);
            if (url == null) {
                System.out.println("ERRO: Arquivo Jasper não encontrado no caminho: " + caminhoRelatorio);
                return;
            }
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(url);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, connection);

            JasperViewer viewer = new JasperViewer(jasperPrint, false);
            viewer.setTitle(tituloJanela);
            viewer.setVisible(true);
        } catch (JRException ex) {
            System.err.println("Erro ao gerar o relatório Jasper: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            database.desconectar(connection);
        }
    }

    public void imprimirRelatorio(String caminhoRelatorio, String tituloJanela) {
        Database database = DatabaseFactory.getDatabase("mysql");
        Connection connection = database.conectar();

        try {
            // 1. Busca a URL do arquivo compilado na pasta resources
            URL url = getClass().getResource(caminhoRelatorio);

            if (url == null) {
                System.out.println("ERRO: Arquivo Jasper não encontrado no caminho: " + caminhoRelatorio);
                return;
            }

            // 2. Carrega o relatório na memória, idêntico à lógica do professor
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(url);

            // 3. Preenche o relatório passando a conexão do banco de dados (o 'null' significa que não há filtros externos)
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, connection);

            // 4. Cria o visualizador (o 'false' impede que o sistema inteiro feche ao fechar a aba do relatório)
            JasperViewer viewer = new JasperViewer(jasperPrint, false);
            viewer.setTitle(tituloJanela);
            viewer.setVisible(true);

        } catch (JRException ex) {
            System.err.println("Erro ao gerar o relatório Jasper: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            database.desconectar(connection);
        }
    }
}