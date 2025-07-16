package locadora.util;

import locadora.model.Conexao;
import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class BackupUtil {
    
    
    public static boolean realizarBackup(java.awt.Component parentComponent) {
        try {
            // Escolher local para salvar o backup
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Salvar Backup");
            fileChooser.setFileFilter(new FileNameExtensionFilter("Arquivos SQL (*.sql)", "sql"));
            
            // Nome padrão com data e hora
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
            String nomeArquivo = "backup_locadora_" + sdf.format(new Date()) + ".sql";
            fileChooser.setSelectedFile(new File(nomeArquivo));
            
            int resultado = fileChooser.showSaveDialog(parentComponent);
            
            if (resultado == JFileChooser.APPROVE_OPTION) {
                File arquivo = fileChooser.getSelectedFile();
                
                // Adiciona extensão .sql se não tiver
                if (!arquivo.getName().toLowerCase().endsWith(".sql")) {
                    arquivo = new File(arquivo.getAbsolutePath() + ".sql");
                }
                
                // Realiza o backup
                boolean sucesso = gerarBackupSQL(arquivo);
                
                if (sucesso) {
                    int opcao = JOptionPane.showConfirmDialog(
                        parentComponent,
                        "Backup realizado com sucesso!\n\nArquivo: " + arquivo.getName() + 
                        "\n\nDeseja criar um arquivo compactado (.zip)?",
                        "Backup Concluído",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                    );
                    
                    if (opcao == JOptionPane.YES_OPTION) {
                        compactarBackup(arquivo);
                    }
                    
                    return true;
                } else {
                    JOptionPane.showMessageDialog(
                        parentComponent,
                        "Erro ao realizar backup!",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                parentComponent,
                "Erro ao realizar backup: " + e.getMessage(),
                "Erro",
                JOptionPane.ERROR_MESSAGE
            );
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Gera arquivo SQL de backup
     */
    private static boolean gerarBackupSQL(File arquivo) {
        try (Connection conn = Conexao.conectar();
             PrintWriter writer = new PrintWriter(new FileWriter(arquivo))) {
            
            // Cabeçalho do backup
            writer.println("-- Backup da Locadora");
            writer.println("-- Data: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            writer.println("-- Arquivo: " + arquivo.getName());
            writer.println();
            
            // Desabilita verificações de chave estrangeira
            writer.println("SET FOREIGN_KEY_CHECKS = 0;");
            writer.println();
            
            // Lista de tabelas para backup (ajustada conforme suas tabelas)
            String[] tabelas = {"clientes", "filmes"};
            
            for (String tabela : tabelas) {
                if (tabelaExiste(conn, tabela)) {
                    fazerBackupTabela(conn, writer, tabela);
                }
            }
            
            // Reabilita verificações de chave estrangeira
            writer.println("SET FOREIGN_KEY_CHECKS = 1;");
            writer.println();
            writer.println("-- Backup concluído!");
            
            return true;
            
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Verifica se uma tabela existe
     */
    private static boolean tabelaExiste(Connection conn, String nomeTabela) {
        try {
            DatabaseMetaData meta = conn.getMetaData();
            ResultSet rs = meta.getTables(null, null, nomeTabela, new String[]{"TABLE"});
            return rs.next();
        } catch (SQLException e) {
            return false;
        }
    }
    
    /**
     * Faz backup de uma tabela específica
     */
    private static void fazerBackupTabela(Connection conn, PrintWriter writer, String nomeTabela) {
        try {
            // Estrutura da tabela
            writer.println("-- Estrutura da tabela " + nomeTabela);
            writer.println("DROP TABLE IF EXISTS `" + nomeTabela + "`;");
            
            // Obter CREATE TABLE
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SHOW CREATE TABLE " + nomeTabela)) {
                
                if (rs.next()) {
                    writer.println(rs.getString(2) + ";");
                }
            }
            
            writer.println();
            
            // Dados da tabela
            writer.println("-- Dados da tabela " + nomeTabela);
            
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT * FROM " + nomeTabela)) {
                
                ResultSetMetaData metaData = rs.getMetaData();
                int numColunas = metaData.getColumnCount();
                
                while (rs.next()) {
                    writer.print("INSERT INTO `" + nomeTabela + "` VALUES (");
                    
                    for (int i = 1; i <= numColunas; i++) {
                        if (i > 1) writer.print(", ");
                        
                        Object valor = rs.getObject(i);
                        if (valor == null) {
                            writer.print("NULL");
                        } else if (valor instanceof String) {
                            writer.print("'" + valor.toString().replace("'", "''") + "'");
                        } else if (valor instanceof Date || valor instanceof Timestamp) {
                            writer.print("'" + valor.toString() + "'");
                        } else {
                            writer.print(valor.toString());
                        }
                    }
                    
                    writer.println(");");
                }
            }
            
            writer.println();
            
        } catch (SQLException e) {
            writer.println("-- Erro ao fazer backup da tabela " + nomeTabela + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Compacta o arquivo de backup
     */
    private static void compactarBackup(File arquivoSQL) {
        try {
            String nomeZip = arquivoSQL.getAbsolutePath().replace(".sql", ".zip");
            File arquivoZip = new File(nomeZip);
            
            try (FileOutputStream fos = new FileOutputStream(arquivoZip);
                 ZipOutputStream zos = new ZipOutputStream(fos);
                 FileInputStream fis = new FileInputStream(arquivoSQL)) {
                
                ZipEntry entry = new ZipEntry(arquivoSQL.getName());
                zos.putNextEntry(entry);
                
                byte[] buffer = new byte[1024];
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    zos.write(buffer, 0, length);
                }
                
                zos.closeEntry();
            }
            
            JOptionPane.showMessageDialog(
                null,
                "Arquivo compactado criado: " + arquivoZip.getName(),
                "Compactação Concluída",
                JOptionPane.INFORMATION_MESSAGE
            );
            
        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                null,
                "Erro ao compactar arquivo: " + e.getMessage(),
                "Erro",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
    /**
     * Realiza backup rápido para pasta padrão
     */
    public static boolean backupRapido(java.awt.Component parentComponent) {
        try {
            // Cria pasta de backup se não existir
            File pastaBackup = new File("backups");
            if (!pastaBackup.exists()) {
                pastaBackup.mkdirs();
            }
            
            // Nome do arquivo com data e hora
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
            String nomeArquivo = "backup_rapido_" + sdf.format(new Date()) + ".sql";
            File arquivo = new File(pastaBackup, nomeArquivo);
            
            boolean sucesso = gerarBackupSQL(arquivo);
            
            if (sucesso) {
                JOptionPane.showMessageDialog(
                    parentComponent,
                    "Backup rápido realizado com sucesso!\n\nArquivo: " + arquivo.getAbsolutePath(),
                    "Backup Concluído",
                    JOptionPane.INFORMATION_MESSAGE
                );
                return true;
            } else {
                JOptionPane.showMessageDialog(
                    parentComponent,
                    "Erro ao realizar backup rápido!",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
                );
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                parentComponent,
                "Erro ao realizar backup rápido: " + e.getMessage(),
                "Erro",
                JOptionPane.ERROR_MESSAGE
            );
        }
        
        return false;
    }
    
    /**
     * Testa a conexão com o banco antes de realizar o backup
     */
    public static boolean testarConexaoBackup() {
        try (Connection conn = Conexao.conectar()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}