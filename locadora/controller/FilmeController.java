











package locadora.controller;

import locadora.model.Filme;
import locadora.model.FilmeDAO;
import locadora.view.FilmeView;
import locadora.view.SinopseView;
import locadora.util.ImageUtil;
import javax.swing.JOptionPane;
import java.sql.SQLException;
import java.util.List;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import locadora.util.BackupUtil;

public class FilmeController {
    private FilmeDAO filmeDAO;
    private FilmeView filmeView;
    private SinopseView sinopseView;
    
    public FilmeController() {
        this.filmeDAO = new FilmeDAO();
        this.filmeView = new FilmeView(this);
        
        ImageUtil.inicializarDiretorio();
    }
    
    public void mostrarView() {
        filmeView.setVisible(true);
        atualizarTabela();
    }
    
    public void adicionarFilme(String titulo, String genero, int ano, String sinopse, File imagemArquivo) {
        try {
            if (titulo.trim().isEmpty() || genero.trim().isEmpty()) {
                JOptionPane.showMessageDialog(filmeView, "Título e gênero são obrigatórios!", 
                    "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (ano < 1900 || ano > 2030) {
                JOptionPane.showMessageDialog(filmeView, "Ano deve estar entre 1900 e 2030!", 
                    "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String caminhoImagem = "";
            if (imagemArquivo != null) {
                try {
                    caminhoImagem = ImageUtil.copiarImagem(imagemArquivo, titulo);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(filmeView, "Erro ao processar imagem: " + e.getMessage(), 
                        "Aviso", JOptionPane.WARNING_MESSAGE);
                    // Continua sem imagem
                }
            }
            
            Filme filme = new Filme(titulo, genero, ano, sinopse, caminhoImagem);
            filmeDAO.inserir(filme);
            JOptionPane.showMessageDialog(filmeView, "Filme adicionado com sucesso!", 
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            atualizarTabela();
            filmeView.limparCampos();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(filmeView, "Erro ao adicionar filme: " + e.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Método de compatibilidade (sem imagem)
    public void adicionarFilme(String titulo, String genero, int ano, String sinopse) {
        adicionarFilme(titulo, genero, ano, sinopse, null);
    }
    
    // Método de compatibilidade (sem sinopse e imagem)
    public void adicionarFilme(String titulo, String genero, int ano) {
        adicionarFilme(titulo, genero, ano, "", null);
    }
    
    public void atualizarFilme(int id, String titulo, String genero, int ano, String sinopse, File imagemArquivo) {
        try {
            if (id <= 0) {
                JOptionPane.showMessageDialog(filmeView, "Selecione um filme para atualizar!", 
                    "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (titulo.trim().isEmpty() || genero.trim().isEmpty()) {
                JOptionPane.showMessageDialog(filmeView, "Título e gênero são obrigatórios!", 
                    "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (ano < 1900 || ano > 2030) {
                JOptionPane.showMessageDialog(filmeView, "Ano deve estar entre 1900 e 2030!", 
                    "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Busca o filme atual para manter a imagem existente se não for alterada
            Filme filmeAtual = filmeDAO.buscarPorId(id);
            String caminhoImagem = filmeAtual != null ? filmeAtual.getCaminhoImagem() : "";
            
            // Se uma nova imagem foi selecionada
            if (imagemArquivo != null) {
                try {
                    // Remove a imagem anterior se existir
                    if (caminhoImagem != null && !caminhoImagem.isEmpty()) {
                        ImageUtil.removerImagem(caminhoImagem);
                    }
                    caminhoImagem = ImageUtil.copiarImagem(imagemArquivo, titulo);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(filmeView, "Erro ao processar imagem: " + e.getMessage(), 
                        "Aviso", JOptionPane.WARNING_MESSAGE);
                    // Mantém a imagem anterior
                }
            }
            
            Filme filme = new Filme(id, titulo, genero, ano, sinopse, caminhoImagem);
            filmeDAO.atualizar(filme);
            JOptionPane.showMessageDialog(filmeView, "Filme atualizado com sucesso!", 
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            atualizarTabela();
            filmeView.limparCampos();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(filmeView, "Erro ao atualizar filme: " + e.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Método de compatibilidade (sem imagem)
    public void atualizarFilme(int id, String titulo, String genero, int ano, String sinopse) {
        atualizarFilme(id, titulo, genero, ano, sinopse, null);
    }
    
    // Método de compatibilidade (sem sinopse e imagem)
    public void atualizarFilme(int id, String titulo, String genero, int ano) {
        try {
            // Busca a sinopse atual do filme
            Filme filmeAtual = filmeDAO.buscarPorId(id);
            String sinopseAtual = (filmeAtual != null) ? filmeAtual.getSinopse() : "";
            atualizarFilme(id, titulo, genero, ano, sinopseAtual, null);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(filmeView, "Erro ao buscar filme atual: " + e.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void excluirFilme(int id) {
        try {
            if (id <= 0) {
                JOptionPane.showMessageDialog(filmeView, "Selecione um filme para excluir!", 
                    "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            int confirmacao = JOptionPane.showConfirmDialog(filmeView, 
                "Tem certeza que deseja excluir este filme?", 
                "Confirmação", JOptionPane.YES_NO_OPTION);
            
            if (confirmacao == JOptionPane.YES_OPTION) {
                // Remove a imagem associada antes de excluir o filme
                Filme filme = filmeDAO.buscarPorId(id);
                if (filme != null && filme.getCaminhoImagem() != null && !filme.getCaminhoImagem().isEmpty()) {
                    ImageUtil.removerImagem(filme.getCaminhoImagem());
                }
                
                filmeDAO.deletar(id);
                JOptionPane.showMessageDialog(filmeView, "Filme excluído com sucesso!", 
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                atualizarTabela();
                filmeView.limparCampos();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(filmeView, "Erro ao excluir filme: " + e.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void removerImagem(int id) {
        try {
            if (id <= 0) {
                JOptionPane.showMessageDialog(filmeView, "Selecione um filme para remover a imagem!", 
                    "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            int confirmacao = JOptionPane.showConfirmDialog(filmeView, 
                "Tem certeza que deseja remover a imagem deste filme?", 
                "Confirmação", JOptionPane.YES_NO_OPTION);
            
            if (confirmacao == JOptionPane.YES_OPTION) {
                Filme filme = filmeDAO.buscarPorId(id);
                if (filme != null) {
                    // Remove o arquivo de imagem
                    if (filme.getCaminhoImagem() != null && !filme.getCaminhoImagem().isEmpty()) {
                        ImageUtil.removerImagem(filme.getCaminhoImagem());
                    }
                    
                    // Atualiza o banco removendo o caminho da imagem
                    filme.setCaminhoImagem("");
                    filmeDAO.atualizar(filme);
                    
                    JOptionPane.showMessageDialog(filmeView, "Imagem removida com sucesso!", 
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    atualizarTabela();
                    filmeView.atualizarImagemSelecionada();
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(filmeView, "Erro ao remover imagem: " + e.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void buscarPorGenero(String genero) {
        try {
            List<Filme> filmes = filmeDAO.buscarPorGenero(genero);
            filmeView.preencherTabela(filmes);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(filmeView, "Erro ao buscar filmes: " + e.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void buscarPorTitulo(String titulo) {
        try {
            List<Filme> filmes = filmeDAO.buscarPorTitulo(titulo);
            filmeView.preencherTabela(filmes);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(filmeView, "Erro ao buscar filmes: " + e.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void buscarPorSinopse(String termo) {
        try {
            List<Filme> filmes = filmeDAO.buscarPorSinopse(termo);
            filmeView.preencherTabela(filmes);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(filmeView, "Erro ao buscar filmes: " + e.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void atualizarTabela() {
        try {
            List<Filme> filmes = filmeDAO.listar();
            filmeView.preencherTabela(filmes);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(filmeView, "Erro ao carregar filmes: " + e.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Método para abrir a janela de edição de sinopse
    public void editarSinopse(int filmeId) {
        try {
            Filme filme = filmeDAO.buscarPorId(filmeId);
            if (filme != null) {
                if (sinopseView == null) {
                    sinopseView = new SinopseView(this);
                }
                sinopseView.mostrarSinopse(filme);
            } else {
                JOptionPane.showMessageDialog(filmeView, "Filme não encontrado!", 
                    "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(filmeView, "Erro ao buscar filme: " + e.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Método para salvar apenas a sinopse
    public void salvarSinopse(int filmeId, String sinopse) {
        try {
            Filme filme = filmeDAO.buscarPorId(filmeId);
            if (filme != null) {
                filme.setSinopse(sinopse);
                filmeDAO.atualizar(filme);
                JOptionPane.showMessageDialog(sinopseView, "Sinopse salva com sucesso!", 
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                atualizarTabela();
            } else {
                JOptionPane.showMessageDialog(sinopseView, "Filme não encontrado!", 
                    "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(sinopseView, "Erro ao salvar sinopse: " + e.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Método para buscar filme por ID (útil para a view)
    public Filme buscarFilmePorId(int id) {
        try {
            return filmeDAO.buscarPorId(id);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(filmeView, "Erro ao buscar filme: " + e.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
    
    

public void escolherFilmeAleatorio() {
    try {
        List<Filme> filmes = filmeDAO.listar();
        
        if (filmes.isEmpty()) {
            JOptionPane.showMessageDialog(filmeView, "Não há filmes cadastrados!", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Gera um índice aleatório
        Random random = new Random();
        int indiceAleatorio = random.nextInt(filmes.size());
        Filme filmeAleatorio = filmes.get(indiceAleatorio);
        
        // Cria uma mensagem personalizada para mostrar o filme
        StringBuilder mensagem = new StringBuilder();
        mensagem.append("🎬 FILME ESCOLHIDO PARA VOCÊ! 🎬\n\n");
        mensagem.append("Título: ").append(filmeAleatorio.getTitulo()).append("\n");
        mensagem.append("Gênero: ").append(filmeAleatorio.getGenero()).append("\n");
        mensagem.append("Ano: ").append(filmeAleatorio.getAno()).append("\n");
        
        if (filmeAleatorio.getSinopse() != null && !filmeAleatorio.getSinopse().trim().isEmpty()) {
            mensagem.append("\nSinopse:\n").append(filmeAleatorio.getSinopse());
        }
        
        // Mostra o filme escolhido em uma janela de diálogo
        JOptionPane.showMessageDialog(filmeView, mensagem.toString(), 
            "Filme Aleatório", JOptionPane.INFORMATION_MESSAGE);
        
        // Opcional: Selecionar o filme na tabela
        filmeView.selecionarFilmeNaTabela(filmeAleatorio.getId());
        
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(filmeView, "Erro ao buscar filmes: " + e.getMessage(), 
            "Erro", JOptionPane.ERROR_MESSAGE);
    }
}

// Método alternativo que retorna o filme para uso na view
public Filme obterFilmeAleatorio() {
    try {
        List<Filme> filmes = filmeDAO.listar();
        
        if (filmes.isEmpty()) {
            return null;
        }
        
        Random random = new Random();
        int indiceAleatorio = random.nextInt(filmes.size());
        return filmes.get(indiceAleatorio);
        
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(filmeView, "Erro ao buscar filmes: " + e.getMessage(), 
            "Erro", JOptionPane.ERROR_MESSAGE);
        return null;
    }
}
    
    
}