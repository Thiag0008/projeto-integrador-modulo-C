
package locadora.model;

/**
 *
 * @author contr
 */

public class Filme {
    private int id;
    private String titulo;
    private String genero;
    private int ano;
    private String sinopse;
    private String caminhoImagem; // Novo campo para armazenar o caminho da imagem

    // Construtor completo
    public Filme(int id, String titulo, String genero, int ano, String sinopse, String caminhoImagem) {
        this.id = id;
        this.titulo = titulo;
        this.genero = genero;
        this.ano = ano;
        this.sinopse = sinopse;
        this.caminhoImagem = caminhoImagem;
    }

    // Construtor sem ID (para inserção)
    public Filme(String titulo, String genero, int ano, String sinopse, String caminhoImagem) {
        this(0, titulo, genero, ano, sinopse, caminhoImagem);
    }

    // Construtor de compatibilidade (sem sinopse e imagem)
    public Filme(int id, String titulo, String genero, int ano) {
        this(id, titulo, genero, ano, "", "");
    }

    // Construtor de compatibilidade sem ID (sem sinopse e imagem)
    public Filme(String titulo, String genero, int ano) {
        this(0, titulo, genero, ano, "", "");
    }

    // Construtor de compatibilidade (sem imagem)
    public Filme(int id, String titulo, String genero, int ano, String sinopse) {
        this(id, titulo, genero, ano, sinopse, "");
    }

    // Construtor de compatibilidade sem ID (sem imagem)
    public Filme(String titulo, String genero, int ano, String sinopse) {
        this(0, titulo, genero, ano, sinopse, "");
    }

    // Getters
    public int getId() { 
        return id; 
    }
    
    public String getTitulo() { 
        return titulo; 
    }
    
    public String getGenero() { 
        return genero; 
    }
    
    public int getAno() { 
        return ano; 
    }

    public String getSinopse() {
        return sinopse;
    }

    public String getCaminhoImagem() {
        return caminhoImagem;
    }

    // Setters
    public void setId(int id) { 
        this.id = id; 
    }
    
    public void setTitulo(String titulo) { 
        this.titulo = titulo; 
    }
    
    public void setGenero(String genero) { 
        this.genero = genero; 
    }
    
    public void setAno(int ano) { 
        this.ano = ano; 
    }

    public void setSinopse(String sinopse) {
        this.sinopse = sinopse;
    }

    public void setCaminhoImagem(String caminhoImagem) {
        this.caminhoImagem = caminhoImagem;
    }

    @Override
    public String toString() {
        return "Filme{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", genero='" + genero + '\'' +
                ", ano=" + ano +
                ", sinopse='" + (sinopse != null && sinopse.length() > 50 ? 
                    sinopse.substring(0, 50) + "..." : sinopse) + '\'' +
                ", caminhoImagem='" + caminhoImagem + '\'' +
                '}';
    }
}