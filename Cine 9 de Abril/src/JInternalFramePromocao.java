import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Random;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

public class JInternalFramePromocao extends JInternalFrame implements ActionListener{
    static int formulario = 0;

    JTabbedPane jtpPanel;

    JPanel jpPrincipal;
    JButton jbComprar;
    JLabel jlTitulo;
    JLabel jlFilmePromocao1, jlFilmePromocao2, jlFilmePromocao3;

    // #1
    JPanel jpMeiaEntrada;
    JLabel jlMeiaTitulo, jlEstudantes, jlIdosos, jlDeficientes, jlProfessores;
    JButton jbSolicitarMeia;

    JInternalFramePromocao(){
        super("Filmes em Promoção", false, true, false, true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        addInternalFrameListener(new InternalHandler());
        
        setPreferredSize(new Dimension(500,300));
        ++formulario;
        CinemaDesktop.posicao+=30;

        jtpPanel = new JTabbedPane();

        jpPrincipal = new JPanel();
        jpPrincipal.setSize(new Dimension(500, 300));
        jpPrincipal.setLayout(new BoxLayout(jpPrincipal, BoxLayout.Y_AXIS));

        // #2
        ImageIcon tabIcon1 = new ImageIcon(getClass().getResource("./imgs/megafone.png"));
        ImageIcon tabIcon2 = new ImageIcon(getClass().getResource("./imgs/tesoura.png"));

        // Título
        jlTitulo = new JLabel("Lista de Filmes em Promoção");
        jlTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        jlTitulo.setFont(new Font("Serif", Font.BOLD, 16));

        jlTitulo.setPreferredSize(new Dimension(jlTitulo.getPreferredSize().width, 50));
        jlTitulo.setMaximumSize(new Dimension(jlTitulo.getPreferredSize().width, 50));
        jlTitulo.setMinimumSize(new Dimension(jlTitulo.getPreferredSize().width,50));

        jpPrincipal.add(jlTitulo);

        jpPrincipal.add(Box.createRigidArea(new Dimension(0,30))); // espaço entre o título e as promoções

        // Lista de filmes
        Random rnd = new Random();
        int qtdFilmes = CinemaDesktop.filmes.size();

        jlFilmePromocao1 = new JLabel(CinemaDesktop.filmes.get(rnd.nextInt(qtdFilmes)).getTitulo() + " - 50% desconto");
        jlFilmePromocao2 = new JLabel(CinemaDesktop.filmes.get(rnd.nextInt(qtdFilmes)).getTitulo() + " - 30% desconto");
        jlFilmePromocao3 = new JLabel(CinemaDesktop.filmes.get(rnd.nextInt(qtdFilmes)).getTitulo() + " - 40% desconto");

        jlFilmePromocao1.setPreferredSize(new Dimension(jlFilmePromocao1.getPreferredSize().width, 30));
        jlFilmePromocao1.setMaximumSize(new Dimension(jlFilmePromocao1.getPreferredSize().width, 30));
        jlFilmePromocao1.setMinimumSize(new Dimension(jlFilmePromocao1.getPreferredSize().width,30));

        jlFilmePromocao2.setPreferredSize(new Dimension(jlFilmePromocao2.getPreferredSize().width, 30));
        jlFilmePromocao2.setMaximumSize(new Dimension(jlFilmePromocao2.getPreferredSize().width, 30));
        jlFilmePromocao2.setMinimumSize(new Dimension(jlFilmePromocao2.getPreferredSize().width,30));

        jlFilmePromocao3.setPreferredSize(new Dimension(jlFilmePromocao3.getPreferredSize().width, 30));
        jlFilmePromocao3.setMaximumSize(new Dimension(jlFilmePromocao3.getPreferredSize().width, 30));
        jlFilmePromocao3.setMinimumSize(new Dimension(jlFilmePromocao3.getPreferredSize().width,30));

        jlFilmePromocao1.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        jlFilmePromocao2.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        jlFilmePromocao3.setAlignmentX(JLabel.CENTER_ALIGNMENT);

        jpPrincipal.add(jlFilmePromocao1);
        jpPrincipal.add(jlFilmePromocao2);
        jpPrincipal.add(jlFilmePromocao3);

        jpPrincipal.add(Box.createRigidArea(new Dimension(0,20))); // espaço entre os JLabels e o JButton

        // Botão de comprar
        jbComprar = new JButton("Comprar Ingresso");
        jbComprar.setAlignmentX(JButton.CENTER_ALIGNMENT);
        jbComprar.addActionListener(this);
        jpPrincipal.add(jbComprar);

        // JTabbedPane para informações sobre meia-entrada
        jpMeiaEntrada = new JPanel();
        jpMeiaEntrada.setSize(new Dimension(400, 300));
        jpMeiaEntrada.setLayout(new BoxLayout(jpMeiaEntrada, BoxLayout.Y_AXIS));

        // Conteúdo da aba de meia-entrada
        jlMeiaTitulo = new JLabel("Quem tem direito à meia-entrada:");
        jlMeiaTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        jlMeiaTitulo.setFont(new Font("Serif", Font.BOLD, 14));

        jlMeiaTitulo.setPreferredSize(new Dimension(jlMeiaTitulo.getPreferredSize().width, 50));
        jlMeiaTitulo.setMaximumSize(new Dimension(jlMeiaTitulo.getPreferredSize().width, 50));
        jlMeiaTitulo.setMinimumSize(new Dimension(jlMeiaTitulo.getPreferredSize().width,50));

        jpMeiaEntrada.add(jlMeiaTitulo);

        jpMeiaEntrada.add(Box.createRigidArea(new Dimension(0,20)));

        jlEstudantes = new JLabel("-> Estudantes");
        jlIdosos = new JLabel("-> Idosos (acima de 60 anos)");
        jlDeficientes = new JLabel("-> Pessoas com deficiência");
        jlProfessores = new JLabel("-> Professores e doadores de sangue");

        jlEstudantes.setAlignmentX(Component.CENTER_ALIGNMENT);
        jlIdosos.setAlignmentX(Component.CENTER_ALIGNMENT);
        jlDeficientes.setAlignmentX(Component.CENTER_ALIGNMENT);
        jlProfessores.setAlignmentX(Component.CENTER_ALIGNMENT);

        jpMeiaEntrada.add(jlEstudantes);
        jpMeiaEntrada.add(jlIdosos);
        jpMeiaEntrada.add(jlDeficientes);
        jpMeiaEntrada.add(jlProfessores);

        jbSolicitarMeia = new JButton("Solicitar meia-entrada");
        jbSolicitarMeia.setAlignmentX(Component.CENTER_ALIGNMENT);

        jpMeiaEntrada.add(Box.createRigidArea(new Dimension(0,30)));

        jbSolicitarMeia.addActionListener(this);
        jpMeiaEntrada.add(jbSolicitarMeia);

        // Adicionando os painéis ao JTabbedPane
        jtpPanel.addTab("Promoções", tabIcon1, jpPrincipal);
        jtpPanel.insertTab("Meia-Entrada", tabIcon2, jpMeiaEntrada, "Informações sobre Meia-entrada", 1);

        jtpPanel.setTabPlacement(JTabbedPane.LEFT);

        // Adicionando o painel ao frame
        this.add(jtpPanel);
        this.pack();

        setVisible(true);

    }

    private class InternalHandler extends InternalFrameAdapter{
        @Override
        public void internalFrameClosed(InternalFrameEvent e){
            if (JInternalFramePromocao.formulario > 0) {
                JInternalFramePromocao.formulario--;
                CinemaDesktop.posicao-=30;
            }
        }

    }

    public void actionPerformed(ActionEvent e){
        if(e.getSource()==jbComprar){
            JOptionPane.showMessageDialog(this, "Parabéns! Compra realizada.", "Compra bem sucedida", JOptionPane.INFORMATION_MESSAGE);
        }
        // #7
        if(e.getSource()==jbSolicitarMeia){
            JPanel jpSolicitacao = new JPanel();
            jpSolicitacao.setLayout(new BoxLayout(jpSolicitacao, BoxLayout.Y_AXIS));

            jpSolicitacao.add(new JLabel("Escolha sua modalidade:"));

            ButtonGroup bgMeia = new ButtonGroup();

            // Pega todos os componentes da aba, menos o primeiro
            boolean primeiro = true;

            for(Component c : jpMeiaEntrada.getComponents()){
                if(primeiro){
                    primeiro = false;
                    continue;
                }

                // Verifica se o componente é uma instância de JLabel
                if(c instanceof JLabel){
                    // Faz um downcast de Component para JLabel
                    JLabel jl = (JLabel) c;
                    JRadioButton jrb = new JRadioButton(jl.getText());

                    // #10
                    jrb.addItemListener(
                        new ItemListener() {
                            @Override
                            public void itemStateChanged(ItemEvent it){
                                if(jrb.isSelected()){
                                    int opcao = JOptionPane.showConfirmDialog(null, "Solicitar meia-entrada na modalidade " + jrb.getText()+"?", "Confirmação de meia-entrada", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                                    if(opcao==JOptionPane.YES_OPTION){
                                        jtpPanel.removeTabAt(jtpPanel.getComponentCount()-1);
                                    }
                                }
                            }
                        }
                    );

                    bgMeia.add(jrb);
                    jpSolicitacao.add(jrb);
                }
            }

            jpSolicitacao.setVisible(true);
            jtpPanel.addTab("Escolher meia-entrada", jpSolicitacao);
            jtpPanel.setSelectedIndex(jtpPanel.getTabCount()-1);
        }
    }
}
