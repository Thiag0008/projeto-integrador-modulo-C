
package locadora.controller;




import locadora.model.Cliente;
import locadora.model.ClienteDAO;
import locadora.view.ClienteView;
import javax.swing.JOptionPane;
import java.sql.SQLException;
import java.util.List;
import locadora.util.BackupUtil;



public class ClienteController {
    private ClienteDAO clienteDAO;
    private ClienteView clienteView;
    
    public ClienteController() {
        this.clienteDAO = new ClienteDAO();
        this.clienteView = new ClienteView(this);
    }
    
    public void mostrarView() {
        clienteView.setVisible(true);
        atualizarTabela();
    }
    
    public void adicionarCliente(String nome, String email, String telefone) {
        try {
            if (nome.trim().isEmpty() || email.trim().isEmpty() || telefone.trim().isEmpty()) {
                JOptionPane.showMessageDialog(clienteView, "Todos os campos são obrigatórios!", 
                    "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Cliente cliente = new Cliente(nome, email, telefone);
            clienteDAO.inserir(cliente);
            JOptionPane.showMessageDialog(clienteView, "Cliente adicionado com sucesso!", 
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            atualizarTabela();
            clienteView.limparCampos();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(clienteView, "Erro ao adicionar cliente: " + e.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void atualizarCliente(int id, String nome, String email, String telefone) {
        try {
            if (id <= 0) {
                JOptionPane.showMessageDialog(clienteView, "Selecione um cliente para atualizar!", 
                    "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (nome.trim().isEmpty() || email.trim().isEmpty() || telefone.trim().isEmpty()) {
                JOptionPane.showMessageDialog(clienteView, "Todos os campos são obrigatórios!", 
                    "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Cliente cliente = new Cliente(id, nome, email, telefone);
            clienteDAO.atualizar(cliente);
            JOptionPane.showMessageDialog(clienteView, "Cliente atualizado com sucesso!", 
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            atualizarTabela();
            clienteView.limparCampos();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(clienteView, "Erro ao atualizar cliente: " + e.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void excluirCliente(int id) {
        try {
            if (id <= 0) {
                JOptionPane.showMessageDialog(clienteView, "Selecione um cliente para excluir!", 
                    "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            int confirmacao = JOptionPane.showConfirmDialog(clienteView, 
                "Tem certeza que deseja excluir este cliente?", 
                "Confirmação", JOptionPane.YES_NO_OPTION);
            
            if (confirmacao == JOptionPane.YES_OPTION) {
                clienteDAO.deletar(id);
                JOptionPane.showMessageDialog(clienteView, "Cliente excluído com sucesso!", 
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                atualizarTabela();
                clienteView.limparCampos();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(clienteView, "Erro ao excluir cliente: " + e.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void buscarPorNome(String nome) {
        try {
            List<Cliente> clientes = clienteDAO.buscarPorNome(nome);
            clienteView.preencherTabela(clientes);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(clienteView, "Erro ao buscar clientes: " + e.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void atualizarTabela() {
        try {
            List<Cliente> clientes = clienteDAO.listar();
            clienteView.preencherTabela(clientes);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(clienteView, "Erro ao carregar clientes: " + e.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
