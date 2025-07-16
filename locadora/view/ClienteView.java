package locadora.view;

import locadora.controller.ClienteController;
import locadora.model.Cliente;
import locadora.util.BackupUtil;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ClienteView extends JFrame {
    private ClienteController controller;
    private JTextField txtNome, txtEmail, txtTelefone, txtBusca;
    private JTable tabela;
    private DefaultTableModel modeloTabela;
    private JButton btnAdicionar, btnAtualizar, btnExcluir, btnBuscar, btnLimpar, btnBackup;
    private int idSelecionado = 0;
    
    public ClienteView(ClienteController controller) {
        this.controller = controller;
        initComponents();
        setupEventListeners();
    }
    
    private void initComponents() {
        setTitle("Gerenciar Clientes - Locadora");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Painel de formulário
        JPanel painelFormulario = new JPanel(new GridBagLayout());
        painelFormulario.setBorder(BorderFactory.createTitledBorder("Dados do Cliente"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Nome
        gbc.gridx = 0; gbc.gridy = 0;
        painelFormulario.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtNome = new JTextField(20);
        painelFormulario.add(txtNome, gbc);
        
        // Email
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE;
        painelFormulario.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtEmail = new JTextField(20);
        painelFormulario.add(txtEmail, gbc);
        
        // Telefone
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE;
        painelFormulario.add(new JLabel("Telefone:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtTelefone = new JTextField(20);
        painelFormulario.add(txtTelefone, gbc);
        
        // Botões
        JPanel painelBotoes = new JPanel(new FlowLayout());
        btnAdicionar = new JButton("Adicionar");
        btnAtualizar = new JButton("Atualizar");
        btnExcluir = new JButton("Excluir");
        btnLimpar = new JButton("Limpar");
        btnBackup = new JButton("💾 Backup");
        
        // Estilizar botão de backup
        btnBackup.setBackground(new Color(52, 73, 94));
        btnBackup.setForeground(Color.WHITE);
        btnBackup.setFocusPainted(false);
        btnBackup.setToolTipText("Fazer backup do banco de dados");
        
        painelBotoes.add(btnAdicionar);
        painelBotoes.add(btnAtualizar);
        painelBotoes.add(btnExcluir);
        painelBotoes.add(btnLimpar);
        painelBotoes.add(btnBackup);
        
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        painelFormulario.add(painelBotoes, gbc);
        
        // Painel de busca
        JPanel painelBusca = new JPanel(new FlowLayout());
        painelBusca.setBorder(BorderFactory.createTitledBorder("Buscar Cliente"));
        painelBusca.add(new JLabel("Nome:"));
        txtBusca = new JTextField(15);
        painelBusca.add(txtBusca);
        btnBuscar = new JButton("Buscar");
        painelBusca.add(btnBuscar);
        
        // Tabela
        String[] colunas = {"ID", "Nome", "Email", "Telefone"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabela = new JTable(modeloTabela);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(tabela);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Lista de Clientes"));
        
        // Layout principal
        JPanel painelSuperior = new JPanel(new BorderLayout());
        painelSuperior.add(painelFormulario, BorderLayout.NORTH);
        painelSuperior.add(painelBusca, BorderLayout.SOUTH);
        
        add(painelSuperior, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        
        pack();
        setLocationRelativeTo(null);
    }
    
    private void setupEventListeners() {
        btnAdicionar.addActionListener(e -> {
            String nome = txtNome.getText();
            String email = txtEmail.getText();
            String telefone = txtTelefone.getText();
            controller.adicionarCliente(nome, email, telefone);
        });
        
        btnAtualizar.addActionListener(e -> {
            String nome = txtNome.getText();
            String email = txtEmail.getText();
            String telefone = txtTelefone.getText();
            controller.atualizarCliente(idSelecionado, nome, email, telefone);
        });
        
        btnExcluir.addActionListener(e -> {
            controller.excluirCliente(idSelecionado);
        });
        
        btnLimpar.addActionListener(e -> limparCampos());
        
        btnBuscar.addActionListener(e -> {
            String nome = txtBusca.getText();
            if (nome.trim().isEmpty()) {
                controller.atualizarTabela();
            } else {
                controller.buscarPorNome(nome);
            }
        });
        
        // Event listener para o botão de backup
        btnBackup.addActionListener(e -> {
            int opcao = JOptionPane.showConfirmDialog(
                this,
                "Deseja fazer backup do banco de dados?",
                "Confirmar Backup",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );
            
            if (opcao == JOptionPane.YES_OPTION) {
                // Mostrar opções de backup
                String[] opcoes = {"Backup Completo", "Backup Rápido", "Cancelar"};
                int escolha = JOptionPane.showOptionDialog(
                    this,
                    "Escolha o tipo de backup:",
                    "Tipo de Backup",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opcoes,
                    opcoes[0]
                );
                
                if (escolha == 0) {
                    // Backup completo
                    BackupUtil.realizarBackup(this);
                } else if (escolha == 1) {
                    // Backup rápido
                    BackupUtil.backupRapido(this);
                }
            }
        });
        
        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int linha = tabela.getSelectedRow();
                if (linha >= 0) {
                    idSelecionado = (Integer) modeloTabela.getValueAt(linha, 0);
                    txtNome.setText((String) modeloTabela.getValueAt(linha, 1));
                    txtEmail.setText((String) modeloTabela.getValueAt(linha, 2));
                    txtTelefone.setText((String) modeloTabela.getValueAt(linha, 3));
                }
            }
        });
    }
    
    public void preencherTabela(List<Cliente> clientes) {
        modeloTabela.setRowCount(0);
        for (Cliente cliente : clientes) {
            Object[] linha = {
                cliente.getId(),
                cliente.getNome(),
                cliente.getEmail(),
                cliente.getTelefone()
            };
            modeloTabela.addRow(linha);
        }
    }
    
    public void limparCampos() {
        txtNome.setText("");
        txtEmail.setText("");
        txtTelefone.setText("");
        txtBusca.setText("");
        idSelecionado = 0;
        tabela.clearSelection();
    }
}