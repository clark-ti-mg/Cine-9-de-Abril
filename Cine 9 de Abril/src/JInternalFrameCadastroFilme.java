import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameAdapter;

public class JInternalFrameCadastroFilme extends JInternalFrame implements ActionListener{
    JTextField jtfTitulo;
    JTextField jtfGenero;
    JTextField jtfDuracao;
    JTextField jtfClassificacao;
    JTextArea jtaDescricao;
    JButton btSalvar;
    JButton btCancelar;
    static int formulario = 0;

    public JInternalFrameCadastroFilme() {
        super("Cadastro de Cinema", // Título
        false, // Redimensionável
         true, // Fechável
         false, // Maximizável
         true); // Minimizável
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        addInternalFrameListener(new InternalHandler());

        ++formulario;
        CinemaDesktop.posicao+=30;

        // Painel principal com GridLayout: 6 linhas (5 campos + 1 linha de botões)
        JPanel painelPrincipal = new JPanel(new GridLayout(6, 1, 8, 8));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Linha 1: Título
        jtfTitulo = new JTextField(30);
        JPanel linhaTitulo = new JPanel(new BorderLayout(5, 5));
        linhaTitulo.add(new JLabel("Título:"), BorderLayout.WEST);
        linhaTitulo.add(jtfTitulo, BorderLayout.CENTER);
        painelPrincipal.add(linhaTitulo);

        // Linha 2: Gênero
        jtfGenero = new JTextField(20);
        JPanel linhaGenero = new JPanel(new BorderLayout(5, 5));
        linhaGenero.add(new JLabel("Gênero:"), BorderLayout.WEST);
        linhaGenero.add(jtfGenero, BorderLayout.CENTER);
        painelPrincipal.add(linhaGenero);

        // Linha 3: Duração (em minutos)
        jtfDuracao = new JTextField(8);
        JPanel linhaDuracao = new JPanel(new BorderLayout(5, 5));
        linhaDuracao.add(new JLabel("Duração (min):"), BorderLayout.WEST);
        linhaDuracao.add(jtfDuracao, BorderLayout.CENTER);
        painelPrincipal.add(linhaDuracao);

        // Linha 4: Classificação
        jtfClassificacao = new JTextField(4);
        JPanel linhaClassificacao = new JPanel(new BorderLayout(5, 5));
        linhaClassificacao.add(new JLabel("Classificação:"), BorderLayout.WEST);
        linhaClassificacao.add(jtfClassificacao, BorderLayout.CENTER);
        painelPrincipal.add(linhaClassificacao);

        // Linha 5: Descrição (JTextArea dentro de JScrollPane)
        jtaDescricao = new JTextArea(5, 30);
        jtaDescricao.setLineWrap(true);
        jtaDescricao.setWrapStyleWord(true);
        JScrollPane descricaoScroll = new JScrollPane(jtaDescricao, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        JPanel linhaDescricao = new JPanel(new BorderLayout(5, 5));
        linhaDescricao.add(new JLabel("Descrição:"), BorderLayout.NORTH);
        linhaDescricao.add(descricaoScroll, BorderLayout.CENTER);
        painelPrincipal.add(linhaDescricao);

        // Linha 6: Botões
        btSalvar = new JButton("Salvar");
        btCancelar = new JButton("Cancelar");
        btSalvar.addActionListener(this);
        btCancelar.addActionListener(this);

        JPanel linhaBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        linhaBotoes.add(btSalvar);
        linhaBotoes.add(btCancelar);
        painelPrincipal.add(linhaBotoes);

        setLayout(new BorderLayout());
        add(painelPrincipal, BorderLayout.CENTER);

        setPreferredSize(new Dimension(480, 380));
        pack();
        
        setLocation(CinemaDesktop.posicao, CinemaDesktop.posicao * formulario);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource()==btCancelar){
            this.dispose();
        }
        if (e.getSource()==btSalvar) {
            if(!jtfTitulo.getText().equals("") && !jtfGenero.getText().equals("") && !jtfDuracao.getText().equals("") && 
            !jtfClassificacao.getText().equals("") && !jtaDescricao.getText().equals("")){
                int classificacao, duracao;
                try {
                    classificacao = Integer.parseInt(jtfClassificacao.getText());
                    duracao = Integer.parseInt(jtfDuracao.getText());

                    CinemaDesktop.filmes.add(new Filme(jtfTitulo.getText(), jtfGenero.getText(), duracao, 
                    classificacao, jtaDescricao.getText()));

                    JOptionPane.showMessageDialog(
                        null, 
                        "Filme cadastrado com sucesso", 
                        "Confirmação", 
                        JOptionPane.CLOSED_OPTION
                    );
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(
                        null, 
                        "Campo numérico não pode haver texto", 
                        "Aviso", 
                        JOptionPane.WARNING_MESSAGE
                    );
                }
            }else{
                JOptionPane.showMessageDialog(
                    null, 
                    "Campos obrigatórios não preenchidos", 
                    "Aviso", 
                    JOptionPane.WARNING_MESSAGE
                );
            }
            
        }
    }
    
    private class InternalHandler extends InternalFrameAdapter{
        @Override
        public void internalFrameClosed(InternalFrameEvent e){
            if (JInternalFrameCadastroFilme.formulario > 0) {
                JInternalFrameCadastroFilme.formulario--;
                CinemaDesktop.posicao-=30;
            }
        }

    }
    
}
