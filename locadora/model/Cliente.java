
package locadora.model;

/**
 *
 * @author contr
 */



public class Cliente {
    private int id;
    private String nome;
    private String email;
    private String telefone;

    
    public Cliente(int id, String nome, String email, String telefone) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
    }

    
    public Cliente(String nome, String email, String telefone) {
        this(0, nome, email, telefone);
    }

    
    public int getId() { 
        return id; 
    }
    
    public String getNome() { 
        return nome; 
    }
    
    public String getEmail() { 
        return email; 
    }
    
    public String getTelefone() { 
        return telefone; 
    }

    
    public void setId(int id) { 
        this.id = id; 
    }
    
    public void setNome(String nome) { 
        this.nome = nome; 
    }
    
    public void setEmail(String email) { 
        this.email = email; 
    }
    
    public void setTelefone(String telefone) { 
        this.telefone = telefone; 
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", telefone='" + telefone + '\'' +
                '}';
    }
}
