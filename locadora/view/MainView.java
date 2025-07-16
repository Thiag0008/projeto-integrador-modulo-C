
package locadora.view;




import locadora.controller.ClienteController;
import locadora.controller.FilmeController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainView extends JFrame {
    private ClienteController clienteController;
    private FilmeController filmeController;
    
    public MainView() {
        this.clienteController = new ClienteController();
        this.filmeController = new FilmeController();
        initComponents();
    }
    
    private void initComponents() {
        setTitle("Sistema de Locadora - Gerenciamento");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        
        JPanel painelCabecalho = new JPanel();
        painelCabecalho.setBackground(new Color(51, 51, 51));
        painelCabecalho.setPreferredSize(new Dimension(0, 80));
        painelCabecalho.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 20));
        
        JLabel lblTitulo = new JLabel("SISTEMA DE LOCADORA");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        painelCabecalho.add(lblTitulo);
        
        
        JPanel painelCentral = new JPanel(new GridBagLayout());
        painelCentral.setBackground(new Color(240, 240, 240));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        
        
        JButton btnFilmes = new JButton("Gerenciar Filmes");
        btnFilmes.setPreferredSize(new Dimension(200, 80));
        btnFilmes.setFont(new Font("Arial", Font.BOLD, 16));
        btnFilmes.setBackground(new Color(52, 152, 219));
        btnFilmes.setForeground(Color.WHITE);
        btnFilmes.setFocusPainted(false);
        
        
        
        JButton btnClientes = new JButton("Gerenciar Clientes");
        btnClientes.setPreferredSize(new Dimension(200, 80));
        btnClientes.setFont(new Font("Arial", Font.BOLD, 16));
        btnClientes.setBackground(new Color(46, 204, 113));
        btnClientes.setForeground(Color.WHITE);
        btnClientes.setFocusPainted(false);
        
        
        
        JButton btnSair = new JButton("Sair");
        btnSair.setPreferredSize(new Dimension(200, 80));
        btnSair.setFont(new Font("Arial", Font.BOLD, 16));
        btnSair.setBackground(new Color(231, 76, 60));
        btnSair.setForeground(Color.WHITE);
        btnSair.setFocusPainted(false);
        
        
        // Posicionamento dos botões
        gbc.gridx = 0; gbc.gridy = 0;
        painelCentral.add(btnFilmes, gbc);
        
        gbc.gridx = 1; gbc.gridy = 0;
        painelCentral.add(btnClientes, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        painelCentral.add(btnSair, gbc);
        
        // Painel do rodapé
        JPanel painelRodape = new JPanel();
        painelRodape.setBackground(new Color(51, 51, 51));
        painelRodape.setPreferredSize(new Dimension(0, 40));
        painelRodape.setLayout(new FlowLayout(FlowLayout.CENTER));
        
        JLabel lblRodape = new JLabel("© 2024 Sistema de Locadora - Desenvolvido em Java Swing");
        lblRodape.setForeground(Color.WHITE);
        lblRodape.setFont(new Font("Arial", Font.PLAIN, 12));
        painelRodape.add(lblRodape);
        
        // Event listeners
        btnFilmes.addActionListener(e -> {
            filmeController.mostrarView();
        });
        
        btnClientes.addActionListener(e -> {
            clienteController.mostrarView();
        });
        
        btnSair.addActionListener(e -> {
            int confirmacao = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja sair?",
                "Confirmação",
                JOptionPane.YES_NO_OPTION);
            
            if (confirmacao == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
        
        // Efeitos visuais nos botões
        btnFilmes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnFilmes.setBackground(new Color(41, 128, 185));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnFilmes.setBackground(new Color(52, 152, 219));
            }
        });
        
        btnClientes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnClientes.setBackground(new Color(39, 174, 96));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnClientes.setBackground(new Color(46, 204, 113));
            }
        });
        
        btnSair.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnSair.setBackground(new Color(192, 57, 43));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnSair.setBackground(new Color(231, 76, 60));
            }
        });
        
        // Layout principal
        add(painelCabecalho, BorderLayout.NORTH);
        add(painelCentral, BorderLayout.CENTER);
        add(painelRodape, BorderLayout.SOUTH);
        
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(false);
    }
    
    public static void main(String[] args) {
        try {
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            new MainView().setVisible(true);
        });
    }
}
