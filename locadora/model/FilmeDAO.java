
package locadora.model;


import java.sql.*;
import java.util.*;

public class FilmeDAO {

    

    
    
public Filme obterFilmeAleatorio() throws SQLException {
    String sql = "SELECT * FROM filmes ORDER BY RAND() LIMIT 1";
    try (Connection conn = Conexao.conectar();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {
        
        if (rs.next()) {
            return new Filme(
                rs.getInt("id"),
                rs.getString("titulo"),
                rs.getString("genero"),
                rs.getInt("ano"),
                rs.getString("sinopse"),
                rs.getString("caminho_imagem")
            );
        }
    }
    return null; // Retorna null se não houver filmes
}


public Filme obterFilmeAleatorioJava() throws SQLException {
    List<Filme> filmes = listar();
    
    if (filmes.isEmpty()) {
        return null;
    }
    
    Random random = new Random();
    int indiceAleatorio = random.nextInt(filmes.size());
    return filmes.get(indiceAleatorio);
}
    
    public void inserir(Filme filme) throws SQLException {
        String sql = "INSERT INTO filmes (titulo, genero, ano, sinopse, caminho_imagem) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, filme.getTitulo());
            stmt.setString(2, filme.getGenero());
            stmt.setInt(3, filme.getAno());
            stmt.setString(4, filme.getSinopse());
            stmt.setString(5, filme.getCaminhoImagem());
            stmt.executeUpdate();
        }
    }

    public List<Filme> listar() throws SQLException {
        List<Filme> lista = new ArrayList<>();
        String sql = "SELECT * FROM filmes ORDER BY titulo";
        try (Connection conn = Conexao.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Filme(
                    rs.getInt("id"),
                    rs.getString("titulo"),
                    rs.getString("genero"),
                    rs.getInt("ano"),
                    rs.getString("sinopse"),
                    rs.getString("caminho_imagem")
                ));
            }
        }
        return lista;
    }

    public void atualizar(Filme filme) throws SQLException {
        String sql = "UPDATE filmes SET titulo=?, genero=?, ano=?, sinopse=?, caminho_imagem=? WHERE id=?";
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, filme.getTitulo());
            stmt.setString(2, filme.getGenero());
            stmt.setInt(3, filme.getAno());
            stmt.setString(4, filme.getSinopse());
            stmt.setString(5, filme.getCaminhoImagem());
            stmt.setInt(6, filme.getId());
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Filme não encontrado para atualização.");
            }
        }
    }

    public void deletar(int id) throws SQLException {
        String sql = "DELETE FROM filmes WHERE id=?";
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Filme não encontrado para exclusão.");
            }
        }
    }

    public List<Filme> buscarPorGenero(String genero) throws SQLException {
        List<Filme> lista = new ArrayList<>();
        String sql = "SELECT * FROM filmes WHERE genero LIKE ? ORDER BY titulo";
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + genero + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(new Filme(
                    rs.getInt("id"),
                    rs.getString("titulo"),
                    rs.getString("genero"),
                    rs.getInt("ano"),
                    rs.getString("sinopse"),
                    rs.getString("caminho_imagem")
                ));
            }
        }
        return lista;
    }
    
    public List<Filme> buscarPorTitulo(String titulo) throws SQLException {
        List<Filme> lista = new ArrayList<>();
        String sql = "SELECT * FROM filmes WHERE titulo LIKE ? ORDER BY titulo";
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + titulo + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(new Filme(
                    rs.getInt("id"),
                    rs.getString("titulo"),
                    rs.getString("genero"),
                    rs.getInt("ano"),
                    rs.getString("sinopse"),
                    rs.getString("caminho_imagem")
                ));
            }
        }
        return lista;
    }
    
    public Filme buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM filmes WHERE id = ?";
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Filme(
                    rs.getInt("id"),
                    rs.getString("titulo"),
                    rs.getString("genero"),
                    rs.getInt("ano"),
                    rs.getString("sinopse"),
                    rs.getString("caminho_imagem")
                );
            }
        }
        return null;
    }
    
    public List<Filme> buscarPorAno(int ano) throws SQLException {
        List<Filme> lista = new ArrayList<>();
        String sql = "SELECT * FROM filmes WHERE ano = ? ORDER BY titulo";
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, ano);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(new Filme(
                    rs.getInt("id"),
                    rs.getString("titulo"),
                    rs.getString("genero"),
                    rs.getInt("ano"),
                    rs.getString("sinopse"),
                    rs.getString("caminho_imagem")
                ));
            }
        }
        return lista;
    }

    public List<Filme> buscarPorSinopse(String termo) throws SQLException {
        List<Filme> lista = new ArrayList<>();
        String sql = "SELECT * FROM filmes WHERE sinopse LIKE ? ORDER BY titulo";
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + termo + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(new Filme(
                    rs.getInt("id"),
                    rs.getString("titulo"),
                    rs.getString("genero"),
                    rs.getInt("ano"),
                    rs.getString("sinopse"),
                    rs.getString("caminho_imagem")
                ));
            }
        }
        return lista;
    }
}