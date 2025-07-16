
package locadora.util;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


public class ImageUtil {
    
    private static final String IMAGES_DIR = "imagens/filmes/";
    private static final String[] EXTENSOES_PERMITIDAS = {".jpg", ".jpeg", ".png", ".gif", ".bmp"};
    private static final int MAX_WIDTH = 300;
    private static final int MAX_HEIGHT = 450;
    
    
    public static void inicializarDiretorio() {
        try {
            Path path = Paths.get(IMAGES_DIR);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
                System.out.println("Diretório de imagens criado: " + IMAGES_DIR);
            }
        } catch (IOException e) {
            System.err.println("Erro ao criar diretório de imagens: " + e.getMessage());
        }
    }
    
   
    public static String copiarImagem(File arquivoOrigem, String nomeFilme) throws IOException {
        if (!isImagemValida(arquivoOrigem)) {
            throw new IOException("Arquivo não é uma imagem válida");
        }
        
        // Cria nome único para o arquivo
        String extensao = getExtensaoArquivo(arquivoOrigem.getName());
        String nomeArquivo = sanitizarNomeArquivo(nomeFilme) + "_" + System.currentTimeMillis() + extensao;
        
        Path destino = Paths.get(IMAGES_DIR + nomeArquivo);
        
        // Copia o arquivo
        Files.copy(arquivoOrigem.toPath(), destino, StandardCopyOption.REPLACE_EXISTING);
        
        return destino.toString();
    }
    
    /**
     * Verifica se o arquivo é uma imagem válida
     */
    public static boolean isImagemValida(File arquivo) {
        if (arquivo == null || !arquivo.exists()) {
            return false;
        }
        
        String nome = arquivo.getName().toLowerCase();
        for (String extensao : EXTENSOES_PERMITIDAS) {
            if (nome.endsWith(extensao)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Carrega uma imagem e redimensiona se necessário
     */
    public static ImageIcon carregarImagem(String caminhoImagem) {
        if (caminhoImagem == null || caminhoImagem.trim().isEmpty()) {
            return getImagemPadrao();
        }
        
        try {
            File arquivo = new File(caminhoImagem);
            if (!arquivo.exists()) {
                return getImagemPadrao();
            }
            
            BufferedImage img = ImageIO.read(arquivo);
            if (img == null) {
                return getImagemPadrao();
            }
            
            // Redimensiona mantendo proporção
            Image imgRedimensionada = redimensionarImagem(img, MAX_WIDTH, MAX_HEIGHT);
            return new ImageIcon(imgRedimensionada);
            
        } catch (IOException e) {
            System.err.println("Erro ao carregar imagem: " + e.getMessage());
            return getImagemPadrao();
        }
    }
    
    /**
     * Redimensiona uma imagem mantendo a proporção
     */
    private static Image redimensionarImagem(BufferedImage imagemOriginal, int maxWidth, int maxHeight) {
        int width = imagemOriginal.getWidth();
        int height = imagemOriginal.getHeight();
        
        // Calcula as novas dimensões mantendo proporção
        double aspectRatio = (double) width / height;
        int newWidth = maxWidth;
        int newHeight = (int) (newWidth / aspectRatio);
        
        if (newHeight > maxHeight) {
            newHeight = maxHeight;
            newWidth = (int) (newHeight * aspectRatio);
        }
        
        return imagemOriginal.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
    }
    
    /**
     * Cria uma imagem padrão quando não há imagem disponível
     */
    private static ImageIcon getImagemPadrao() {
        BufferedImage img = new BufferedImage(200, 300, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = img.createGraphics();
        
        // Configurações para melhor qualidade
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        // Fundo gradiente
        GradientPaint gradient = new GradientPaint(0, 0, new Color(220, 220, 220), 0, 300, new Color(180, 180, 180));
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, 200, 300);
        
        // Borda
        g2d.setColor(new Color(100, 100, 100));
        g2d.drawRect(0, 0, 199, 299);
        
        // Texto
        g2d.setColor(new Color(80, 80, 80));
        g2d.setFont(new Font("Arial", Font.BOLD, 14));
        String texto = "Sem Imagem";
        FontMetrics fm = g2d.getFontMetrics();
        int x = (200 - fm.stringWidth(texto)) / 2;
        int y = 150;
        g2d.drawString(texto, x, y);
        
        // Ícone de filme
        g2d.setColor(new Color(120, 120, 120));
        g2d.fillRoundRect(75, 100, 50, 40, 5, 5);
        g2d.setColor(new Color(80, 80, 80));
        g2d.drawRoundRect(75, 100, 50, 40, 5, 5);
        
        g2d.dispose();
        
        return new ImageIcon(img);
    }
    
    /**
     * Remove uma imagem do sistema
     */
    public static void removerImagem(String caminhoImagem) {
        if (caminhoImagem == null || caminhoImagem.trim().isEmpty()) {
            return;
        }
        
        try {
            File arquivo = new File(caminhoImagem);
            if (arquivo.exists() && arquivo.getParent().contains("imagens/filmes")) {
                arquivo.delete();
                System.out.println("Imagem removida: " + caminhoImagem);
            }
        } catch (Exception e) {
            System.err.println("Erro ao remover imagem: " + e.getMessage());
        }
    }
    
    /**
     * Obtém a extensão do arquivo
     */
    private static String getExtensaoArquivo(String nomeArquivo) {
        int ponto = nomeArquivo.lastIndexOf('.');
        if (ponto > 0) {
            return nomeArquivo.substring(ponto);
        }
        return "";
    }
    
    /**
     * Sanitiza o nome do arquivo removendo caracteres especiais
     */
    private static String sanitizarNomeArquivo(String nome) {
        return nome.replaceAll("[^a-zA-Z0-9_-]", "_").toLowerCase();
    }
    
    /**
     * Obtém as extensões permitidas
     */
    public static String[] getExtensoesPermitidas() {
        return EXTENSOES_PERMITIDAS.clone();
    }
    
    /**
     * Cria um filtro de arquivo para imagens
     */
    public static javax.swing.filechooser.FileFilter criarFiltroImagem() {
        return new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                }
                return isImagemValida(f);
            }
            
            @Override
            public String getDescription() {
                return "Imagens (*.jpg, *.jpeg, *.png, *.gif, *.bmp)";
            }
        };
    }
}
