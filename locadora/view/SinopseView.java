
package locadora.view;



import locadora.controller.FilmeController;
import locadora.model.Filme;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SinopseView extends JFrame {
    private FilmeController controller;
    private JTextArea txtSinopse;
    private JButton btnSalvar, btnCancelar, btnLimpar;
    private JLabel lblTituloFilme, lblGenero, lblAno;
    private int filmeId;
    
    public SinopseView(FilmeController controller) {
        this.controller = controller;
        initComponents();
        setupEventListeners();
    }
    
    private void initComponents() {
        setTitle("Editar Sinopse - Locadora");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        
        
        // Painel de informações do filme
        JPanel painelInfo = new JPanel(new GridBagLayout());
        painelInfo.setBorder(BorderFactory.createTitledBorder("Informações do Filme"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Título do filme
        gbc.gridx = 0; gbc.gridy = 0;
        painelInfo.add(new JLabel("Título:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        lblTituloFilme = new JLabel();
        lblTituloFilme.setFont(new Font("Arial", Font.BOLD, 14));
        lblTituloFilme.setForeground(new Color(52, 152, 219));
        painelInfo.add(lblTituloFilme, gbc);
        
        // Gênero
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        painelInfo.add(new JLabel("Gênero:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        lblGenero = new JLabel();
        painelInfo.add(lblGenero, gbc);
        
        // Ano
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        painelInfo.add(new JLabel("Ano:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        lblAno = new JLabel();
        painelInfo.add(lblAno, gbc);
        
        // Painel da sinopse
        JPanel painelSinopse = new JPanel(new BorderLayout());
        painelSinopse.setBorder(BorderFactory.createTitledBorder("Sinopse"));
        
        txtSinopse = new JTextArea(10, 50);
        txtSinopse.setLineWrap(true);
        txtSinopse.setWrapStyleWord(true);
        txtSinopse.setFont(new Font("Arial", Font.PLAIN, 12));
        txtSinopse.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        JScrollPane scrollPane = new JScrollPane(txtSinopse);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        painelSinopse.add(scrollPane, BorderLayout.CENTER);
        
        // Label de contador de caracteres
        JLabel lblContador = new JLabel("0 caracteres");
        lblContador.setHorizontalAlignment(SwingConstants.RIGHT);
        lblContador.setFont(new Font("Arial", Font.PLAIN, 10));
        lblContador.setForeground(Color.GRAY);
        painelSinopse.add(lblContador, BorderLayout.SOUTH);
        
        // Atualizar contador em tempo real
        txtSinopse.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { updateCounter(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { updateCounter(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { updateCounter(); }
            
            private void updateCounter() {
                SwingUtilities.invokeLater(() -> {
                    int count = txtSinopse.getText().length();
                    lblContador.setText(count + " caracteres");
                    if (count > 1000) {
                        lblContador.setForeground(Color.RED);
                    } else if (count > 500) {
                        lblContador.setForeground(Color.ORANGE);
                    } else {
                        lblContador.setForeground(Color.GRAY);
                    }
                });
            }
        });
        
        // Painel de botões
        JPanel painelBotoes = new JPanel(new FlowLayout());
        
        btnSalvar = new JButton("Salvar");
        btnSalvar.setBackground(new Color(46, 204, 113));
        btnSalvar.setForeground(Color.WHITE);
        btnSalvar.setFocusPainted(false);
        btnSalvar.setPreferredSize(new Dimension(100, 35));
        
        btnLimpar = new JButton("Limpar");
        btnLimpar.setBackground(new Color(241, 196, 15));
        btnLimpar.setForeground(Color.WHITE);
        btnLimpar.setFocusPainted(false);
        btnLimpar.setPreferredSize(new Dimension(100, 35));
        
        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(new Color(231, 76, 60));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setPreferredSize(new Dimension(100, 35));
        
        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnLimpar);
        painelBotoes.add(btnCancelar);
        
        // Efeitos visuais nos botões
        addHoverEffect(btnSalvar, new Color(46, 204, 113), new Color(39, 174, 96));
        addHoverEffect(btnLimpar, new Color(241, 196, 15), new Color(212, 172, 13));
        addHoverEffect(btnCancelar, new Color(231, 76, 60), new Color(192, 57, 43));
        
        // Layout principal
        add(painelInfo, BorderLayout.NORTH);
        add(painelSinopse, BorderLayout.CENTER);
        add(painelBotoes, BorderLayout.SOUTH);
        
        setSize(600, 500);
        setLocationRelativeTo(null);
        setResizable(true);
    }
    
    private void addHoverEffect(JButton button, Color normalColor, Color hoverColor) {
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverColor);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(normalColor);
            }
        });
    }
    
    private void setupEventListeners() {
        btnSalvar.addActionListener(e -> {
            String sinopse = txtSinopse.getText().trim();
            if (sinopse.length() > 2000) {
                JOptionPane.showMessageDialog(this, 
                    "A sinopse não pode ter mais de 2000 caracteres!", 
                    "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            controller.salvarSinopse(filmeId, sinopse);
            dispose();
        });
        
        btnLimpar.addActionListener(e -> {
            int confirmacao = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja limpar toda a sinopse?",
                "Confirmação",
                JOptionPane.YES_NO_OPTION);
            
            if (confirmacao == JOptionPane.YES_OPTION) {
                txtSinopse.setText("");
                txtSinopse.requestFocus();
            }
        });
        
        btnCancelar.addActionListener(e -> dispose());
        
        // Atalhos de teclado
        KeyStroke saveKeyStroke = KeyStroke.getKeyStroke("ctrl S");
        KeyStroke cancelKeyStroke = KeyStroke.getKeyStroke("ESCAPE");
        
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(saveKeyStroke, "save");
        getRootPane().getActionMap().put("save", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnSalvar.doClick();
            }
        });
        
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(cancelKeyStroke, "cancel");
        getRootPane().getActionMap().put("cancel", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnCancelar.doClick();
            }
        });
    }
    
    public void mostrarSinopse(Filme filme) {
        this.filmeId = filme.getId();
        lblTituloFilme.setText(filme.getTitulo());
        lblGenero.setText(filme.getGenero());
        lblAno.setText(String.valueOf(filme.getAno()));
        txtSinopse.setText(filme.getSinopse() != null ? filme.getSinopse() : "");
        txtSinopse.setCaretPosition(0);
        txtSinopse.requestFocus();
        setVisible(true);
    }
    
    
}
