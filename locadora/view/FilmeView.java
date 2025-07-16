package locadora.view;

import locadora.controller.FilmeController;
import locadora.model.Filme;
import locadora.util.ImageUtil;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import locadora.util.BackupUtil;

public class FilmeView extends JFrame {
    private FilmeController controller;
    private JTextField txtTitulo, txtGenero, txtAno, txtBusca;
    private JComboBox<String> cmbTipoBusca;
    private JTable tabela;
    private DefaultTableModel modeloTabela;
    private JButton btnAdicionar, btnAtualizar, btnExcluir, btnBuscar, btnLimpar, btnEditarSinopse;
    private JButton btnSelecionarImagem, btnRemoverImagem, btnVisualizarImagem;
    private JButton btnFilmeAleatorio; // Novo botão
    private JLabel lblImagem, lblStatusImagem;
    private File arquivoImagemSelecionado;
    private int idSelecionado = 0;
    
    public FilmeView(FilmeController controller) {
        this.controller = controller;
        initComponents();
        setupEventListeners();
    }
    
    private void initComponents() {
        setTitle("Gerenciar Filmes - Locadora");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Painel principal que vai conter o formulário e a imagem
        JPanel painelPrincipal = new JPanel(new BorderLayout());
        
        // Painel de formulário
        JPanel painelFormulario = new JPanel(new GridBagLayout());
        painelFormulario.setBorder(BorderFactory.createTitledBorder("Dados do Filme"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Título
        gbc.gridx = 0; gbc.gridy = 0;
        painelFormulario.add(new JLabel("Título:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtTitulo = new JTextField(20);
        painelFormulario.add(txtTitulo, gbc);
        
        // Gênero
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE;
        painelFormulario.add(new JLabel("Gênero:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtGenero = new JTextField(20);
        painelFormulario.add(txtGenero, gbc);
        
        // Ano
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE;
        painelFormulario.add(new JLabel("Ano:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtAno = new JTextField(20);
        painelFormulario.add(txtAno, gbc);
        
        // Botões principais
        JPanel painelBotoes = new JPanel(new FlowLayout());
        btnAdicionar = new JButton("Adicionar");
        btnAtualizar = new JButton("Atualizar");
        btnExcluir = new JButton("Excluir");
        btnLimpar = new JButton("Limpar");
        btnEditarSinopse = new JButton("Editar Sinopse");
        
        // Estilização dos botões
        estilizarBotao(btnAdicionar, new Color(46, 204, 113));
        estilizarBotao(btnAtualizar, new Color(52, 152, 219));
        estilizarBotao(btnExcluir, new Color(231, 76, 60));
        estilizarBotao(btnLimpar, new Color(149, 165, 166));
        estilizarBotao(btnEditarSinopse, new Color(155, 89, 182));
        btnEditarSinopse.setEnabled(false);
        
        painelBotoes.add(btnAdicionar);
        painelBotoes.add(btnAtualizar);
        painelBotoes.add(btnExcluir);
        painelBotoes.add(btnEditarSinopse);
        painelBotoes.add(btnLimpar);
        
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        painelFormulario.add(painelBotoes, gbc);
        
        // Painel da imagem
        JPanel painelImagem = new JPanel(new BorderLayout());
        painelImagem.setBorder(BorderFactory.createTitledBorder("Imagem do Filme"));
        painelImagem.setPreferredSize(new Dimension(320, 480));
        
        // Label para exibir a imagem
        lblImagem = new JLabel();
        lblImagem.setHorizontalAlignment(JLabel.CENTER);
        lblImagem.setVerticalAlignment(JLabel.CENTER);
        lblImagem.setBorder(BorderFactory.createLoweredBevelBorder());
        lblImagem.setPreferredSize(new Dimension(300, 400));
        lblImagem.setIcon(ImageUtil.carregarImagem(""));
        
        // Painel de botões da imagem
        JPanel painelBotoesImagem = new JPanel(new FlowLayout());
        btnSelecionarImagem = new JButton("Selecionar Imagem");
        btnRemoverImagem = new JButton("Remover Imagem");
        btnVisualizarImagem = new JButton("Visualizar");
        
        estilizarBotao(btnSelecionarImagem, new Color(39, 174, 96));
        estilizarBotao(btnRemoverImagem, new Color(192, 57, 43));
        estilizarBotao(btnVisualizarImagem, new Color(142, 68, 173));
        
        btnRemoverImagem.setEnabled(false);
        btnVisualizarImagem.setEnabled(false);
        
        painelBotoesImagem.add(btnSelecionarImagem);
        painelBotoesImagem.add(btnRemoverImagem);
        painelBotoesImagem.add(btnVisualizarImagem);
        
        // Status da imagem
        lblStatusImagem = new JLabel("Nenhuma imagem selecionada");
        lblStatusImagem.setHorizontalAlignment(JLabel.CENTER);
        lblStatusImagem.setFont(new Font("Arial", Font.ITALIC, 12));
        lblStatusImagem.setForeground(Color.GRAY);
        
        painelImagem.add(lblImagem, BorderLayout.CENTER);
        painelImagem.add(painelBotoesImagem, BorderLayout.SOUTH);
        painelImagem.add(lblStatusImagem, BorderLayout.NORTH);
        
        // Adicionar os painéis ao painel principal
        painelPrincipal.add(painelFormulario, BorderLayout.CENTER);
        painelPrincipal.add(painelImagem, BorderLayout.EAST);
        
        // Painel de busca
        JPanel painelBusca = new JPanel(new FlowLayout());
        painelBusca.setBorder(BorderFactory.createTitledBorder("Buscar Filme"));
        
        painelBusca.add(new JLabel("Buscar por:"));
        cmbTipoBusca = new JComboBox<>(new String[]{"Gênero", "Título", "Sinopse"});
        painelBusca.add(cmbTipoBusca);
        
        txtBusca = new JTextField(15);
        painelBusca.add(txtBusca);
        
        btnBuscar = new JButton("Buscar");
        estilizarBotao(btnBuscar, new Color(241, 196, 15));
        painelBusca.add(btnBuscar);
        
        JButton btnMostrarTodos = new JButton("Mostrar Todos");
        estilizarBotao(btnMostrarTodos, new Color(52, 73, 94));
        painelBusca.add(btnMostrarTodos);
        
        // Novo botão para filme aleatório
        btnFilmeAleatorio = new JButton("🎲 Filme Aleatório");
        estilizarBotao(btnFilmeAleatorio, new Color(230, 126, 34));
        btnFilmeAleatorio.setPreferredSize(new Dimension(150, 30));
        painelBusca.add(btnFilmeAleatorio);
        
        // Botão de backup
        JButton btnBackup = new JButton("💾 Backup");
        estilizarBotao(btnBackup, new Color(44, 62, 80));
        btnBackup.setPreferredSize(new Dimension(100, 30));
        painelBusca.add(btnBackup);
        
        // Event listener para mostrar todos
        btnMostrarTodos.addActionListener(e -> {
            txtBusca.setText("");
            controller.atualizarTabela();
        });
        
        // Event listener para backup
        btnBackup.addActionListener(e -> {
            locadora.util.BackupUtil.realizarBackup(this);
        });
        
        // Tabela
        String[] colunas = {"ID", "Título", "Gênero", "Ano", "Sinopse", "Imagem"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabela = new JTable(modeloTabela);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Configurar larguras das colunas
        tabela.getColumnModel().getColumn(0).setPreferredWidth(50);  // ID
        tabela.getColumnModel().getColumn(1).setPreferredWidth(200); // Título
        tabela.getColumnModel().getColumn(2).setPreferredWidth(100); // Gênero
        tabela.getColumnModel().getColumn(3).setPreferredWidth(60);  // Ano
        tabela.getColumnModel().getColumn(4).setPreferredWidth(250); // Sinopse
        tabela.getColumnModel().getColumn(5).setPreferredWidth(80);  // Imagem
        
        tabela.setRowHeight(40);
        
        // Renderer para a coluna de sinopse
        tabela.getColumnModel().getColumn(4).setCellRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, 
                boolean isSelected, boolean hasFocus, int row, int column) {
                
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (value != null) {
                    String sinopse = value.toString();
                    if (sinopse.length() > 100) {
                        setText(sinopse.substring(0, 100) + "...");
                    } else {
                        setText(sinopse);
                    }
                    setToolTipText(sinopse);
                } else {
                    setText("(Sem sinopse)");
                    setToolTipText("Nenhuma sinopse cadastrada");
                }
                
                return c;
            }
        });
        
        // Renderer para a coluna de imagem
        tabela.getColumnModel().getColumn(5).setCellRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, 
                boolean isSelected, boolean hasFocus, int row, int column) {
                
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (value != null && !value.toString().isEmpty()) {
                    setText("✓ Sim");
                    setForeground(new Color(46, 204, 113));
                } else {
                    setText("✗ Não");
                    setForeground(new Color(231, 76, 60));
                }
                
                return c;
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tabela);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Lista de Filmes"));
        scrollPane.setPreferredSize(new Dimension(800, 300));
        
        // Layout final
        JPanel painelSuperior = new JPanel(new BorderLayout());
        painelSuperior.add(painelPrincipal, BorderLayout.CENTER);
        painelSuperior.add(painelBusca, BorderLayout.SOUTH);
        
        add(painelSuperior, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        
        pack();
        setLocationRelativeTo(null);
    }
    
    private void estilizarBotao(JButton botao, Color cor) {
        botao.setBackground(cor);
        botao.setForeground(Color.WHITE);
        botao.setFocusPainted(false);
        botao.setBorder(BorderFactory.createRaisedBevelBorder());
        botao.setPreferredSize(new Dimension(120, 30));
    }
    
    private void setupEventListeners() {
        btnAdicionar.addActionListener(e -> {
            try {
                String titulo = txtTitulo.getText().trim();
                String genero = txtGenero.getText().trim();
                
                if (titulo.isEmpty() || genero.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Título e gênero são obrigatórios!", 
                        "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                int ano = Integer.parseInt(txtAno.getText().trim());
                controller.adicionarFilme(titulo, genero, ano, "", arquivoImagemSelecionado);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Ano deve ser um número válido!", 
                    "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        btnAtualizar.addActionListener(e -> {
            try {
                if (idSelecionado <= 0) {
                    JOptionPane.showMessageDialog(this, "Selecione um filme para atualizar!", 
                        "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                String titulo = txtTitulo.getText().trim();
                String genero = txtGenero.getText().trim();
                
                if (titulo.isEmpty() || genero.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Título e gênero são obrigatórios!", 
                        "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                int ano = Integer.parseInt(txtAno.getText().trim());
                controller.atualizarFilme(idSelecionado, titulo, genero, ano, "", arquivoImagemSelecionado);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Ano deve ser um número válido!", 
                    "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        btnExcluir.addActionListener(e -> {
            if (idSelecionado <= 0) {
                JOptionPane.showMessageDialog(this, "Selecione um filme para excluir!", 
                    "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            controller.excluirFilme(idSelecionado);
        });
        
        btnLimpar.addActionListener(e -> limparCampos());
        
        btnEditarSinopse.addActionListener(e -> {
            if (idSelecionado <= 0) {
                JOptionPane.showMessageDialog(this, "Selecione um filme para editar a sinopse!", 
                    "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            controller.editarSinopse(idSelecionado);
        });
        
        btnSelecionarImagem.addActionListener(e -> selecionarImagem());
        
        btnRemoverImagem.addActionListener(e -> {
            if (idSelecionado > 0) {
                controller.removerImagem(idSelecionado);
            } else {
                // Remove apenas a imagem selecionada localmente
                arquivoImagemSelecionado = null;
                lblImagem.setIcon(ImageUtil.carregarImagem(""));
                lblStatusImagem.setText("Nenhuma imagem selecionada");
                btnRemoverImagem.setEnabled(false);
                btnVisualizarImagem.setEnabled(false);
            }
        });
        
        btnVisualizarImagem.addActionListener(e -> visualizarImagem());
        
        btnBuscar.addActionListener(e -> {
            String termo = txtBusca.getText().trim();
            if (termo.isEmpty()) {
                controller.atualizarTabela();
                return;
            }
            
            String tipoBusca = (String) cmbTipoBusca.getSelectedItem();
            switch (tipoBusca) {
                case "Gênero":
                    controller.buscarPorGenero(termo);
                    break;
                case "Título":
                    controller.buscarPorTitulo(termo);
                    break;
                case "Sinopse":
                    controller.buscarPorSinopse(termo);
                    break;
            }
        });
        
        // Event listener para filme aleatório
        btnFilmeAleatorio.addActionListener(e -> {
            controller.escolherFilmeAleatorio();
        });
        
        // Event listener para backup
       
        
        txtBusca.addActionListener(e -> btnBuscar.doClick());
        
        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int linha = tabela.getSelectedRow();
                if (linha >= 0) {
                    idSelecionado = (Integer) modeloTabela.getValueAt(linha, 0);
                    txtTitulo.setText((String) modeloTabela.getValueAt(linha, 1));
                    txtGenero.setText((String) modeloTabela.getValueAt(linha, 2));
                    txtAno.setText(modeloTabela.getValueAt(linha, 3).toString());
                    
                    // Carrega a imagem do filme selecionado
                    Filme filme = controller.buscarFilmePorId(idSelecionado);
                    if (filme != null) {
                        atualizarImagemSelecionada(filme.getCaminhoImagem());
                    }
                    
                    btnEditarSinopse.setEnabled(true);
                } else {
                    btnEditarSinopse.setEnabled(false);
                    atualizarImagemSelecionada("");
                }
            }
        });
    }
    
    private void selecionarImagem() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Selecionar Imagem do Filme");
        fileChooser.setFileFilter(ImageUtil.criarFiltroImagem());
        fileChooser.setAcceptAllFileFilterUsed(false);
        
        int resultado = fileChooser.showOpenDialog(this);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File arquivo = fileChooser.getSelectedFile();
            if (ImageUtil.isImagemValida(arquivo)) {
                arquivoImagemSelecionado = arquivo;
                
                // Mostra uma pré-visualização da imagem selecionada
                ImageIcon icon = ImageUtil.carregarImagem(arquivo.getAbsolutePath());
                lblImagem.setIcon(icon);
                lblStatusImagem.setText("Nova imagem selecionada: " + arquivo.getName());
                lblStatusImagem.setForeground(new Color(46, 204, 113));
                
                btnRemoverImagem.setEnabled(true);
                btnVisualizarImagem.setEnabled(true);
            } else {
                JOptionPane.showMessageDialog(this, "Arquivo selecionado não é uma imagem válida!", 
                    "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void visualizarImagem() {
        if (arquivoImagemSelecionado != null) {
            // Visualiza a imagem selecionada localmente
            mostrarImagemCompleta(arquivoImagemSelecionado.getAbsolutePath());
        } else if (idSelecionado > 0) {
            // Visualiza a imagem do filme selecionado
            Filme filme = controller.buscarFilmePorId(idSelecionado);
            if (filme != null && filme.getCaminhoImagem() != null && !filme.getCaminhoImagem().isEmpty()) {
                mostrarImagemCompleta(filme.getCaminhoImagem());
            } else {
                JOptionPane.showMessageDialog(this, "Este filme não possui imagem!", 
                    "Informação", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
    
    private void mostrarImagemCompleta(String caminhoImagem) {
        JDialog dialog = new JDialog(this, "Visualizar Imagem", true);
        dialog.setLayout(new BorderLayout());
        
        ImageIcon icon = ImageUtil.carregarImagem(caminhoImagem);
        JLabel lblImagemCompleta = new JLabel(icon);
        lblImagemCompleta.setHorizontalAlignment(JLabel.CENTER);
        
        JScrollPane scrollPane = new JScrollPane(lblImagemCompleta);
        scrollPane.setPreferredSize(new Dimension(600, 600));
        
        dialog.add(scrollPane, BorderLayout.CENTER);
        
        JButton btnFechar = new JButton("Fechar");
        btnFechar.addActionListener(e -> dialog.dispose());
        JPanel painelBotao = new JPanel(new FlowLayout());
        painelBotao.add(btnFechar);
        dialog.add(painelBotao, BorderLayout.SOUTH);
        
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    
    public void atualizarImagemSelecionada() {
        if (idSelecionado > 0) {
            Filme filme = controller.buscarFilmePorId(idSelecionado);
            if (filme != null) {
                atualizarImagemSelecionada(filme.getCaminhoImagem());
            }
        }
    }
    
    private void atualizarImagemSelecionada(String caminhoImagem) {
        arquivoImagemSelecionado = null;
        
        if (caminhoImagem != null && !caminhoImagem.isEmpty()) {
            lblImagem.setIcon(ImageUtil.carregarImagem(caminhoImagem));
            lblStatusImagem.setText("Imagem do filme");
            lblStatusImagem.setForeground(new Color(52, 152, 219));
            btnRemoverImagem.setEnabled(true);
            btnVisualizarImagem.setEnabled(true);
        } else {
            lblImagem.setIcon(ImageUtil.carregarImagem(""));
            lblStatusImagem.setText("Nenhuma imagem");
            lblStatusImagem.setForeground(Color.GRAY);
            btnRemoverImagem.setEnabled(false);
            btnVisualizarImagem.setEnabled(false);
        }
    }
    
    public void preencherTabela(List<Filme> filmes) {
        modeloTabela.setRowCount(0);
        for (Filme filme : filmes) {
            Object[] linha = {
                filme.getId(),
                filme.getTitulo(),
                filme.getGenero(),
                filme.getAno(),
                filme.getSinopse() != null ? filme.getSinopse() : "",
                filme.getCaminhoImagem()
            };
            modeloTabela.addRow(linha);
        }
    }
    
    public void limparCampos() {
        txtTitulo.setText("");
        txtGenero.setText("");
        txtAno.setText("");
        txtBusca.setText("");
        idSelecionado = 0;
        arquivoImagemSelecionado = null;
        tabela.clearSelection();
        btnEditarSinopse.setEnabled(false);
        atualizarImagemSelecionada("");
    }
    
    // Método para selecionar um filme na tabela (usado pelo filme aleatório)
    public void selecionarFilmeNaTabela(int filmeId) {
        for (int i = 0; i < modeloTabela.getRowCount(); i++) {
            if ((Integer) modeloTabela.getValueAt(i, 0) == filmeId) {
                tabela.setRowSelectionInterval(i, i);
                tabela.scrollRectToVisible(tabela.getCellRect(i, 0, true));
                break;
            }
        }
    }
}