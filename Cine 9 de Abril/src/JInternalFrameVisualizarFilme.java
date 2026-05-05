import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

public class JInternalFrameVisualizarFilme extends JInternalFrame implements ActionListener {

    private JList<String> listaFilmes;
    private JTextArea jtaDetalhes;
    private JButton btFechar;
    private JButton btAtualizar;
    private JButton btMostrar;
    static int formulario = 0;

    public JInternalFrameVisualizarFilme() {
        super("Visualizar Filmes", // título
              false, // redimensionável
              true,  // fechável
              false, // maximizável
              true); // minimizável

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        addInternalFrameListener(new InternalHandler());

        ++formulario;
        CinemaDesktop.posicao += 30;

        // Painel principal
        JPanel painelPrincipal = new JPanel(new BorderLayout(8, 8));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // JList com seleção única
        listaFilmes = CinemaDesktop.criarJListDeFilmes();
        listaFilmes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollLista = new JScrollPane(listaFilmes, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollLista.setPreferredSize(new Dimension(260, 300));
        painelPrincipal.add(scrollLista, BorderLayout.WEST);

        // Painel de detalhes à direita
        JPanel painelDireito = new JPanel(new BorderLayout(6, 6));
        painelDireito.add(new JLabel("Detalhes do Filme:"), BorderLayout.NORTH);

        jtaDetalhes = new JTextArea();
        jtaDetalhes.setEditable(false);
        jtaDetalhes.setLineWrap(true);
        jtaDetalhes.setWrapStyleWord(true);
        JScrollPane scrollDetalhes = new JScrollPane(jtaDetalhes, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollDetalhes.setPreferredSize(new Dimension(300, 260));
        painelDireito.add(scrollDetalhes, BorderLayout.CENTER);

        // Botões
        btMostrar = new JButton("Mostrar");
        btAtualizar = new JButton("Atualizar");
        btFechar = new JButton("Fechar");

        btMostrar.addActionListener(this);
        btAtualizar.addActionListener(this);
        btFechar.addActionListener(this);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        painelBotoes.add(btMostrar);
        painelBotoes.add(btAtualizar);
        painelBotoes.add(btFechar);
        painelDireito.add(painelBotoes, BorderLayout.SOUTH);

        painelPrincipal.add(painelDireito, BorderLayout.CENTER);

        add(painelPrincipal, BorderLayout.CENTER);

        setPreferredSize(new Dimension(600, 380));
        pack();
        setLocation(CinemaDesktop.posicao, CinemaDesktop.posicao * formulario);
        setVisible(true);
    }

    private void mostrarDetalhesSelecionado() {
        int idx = listaFilmes.getSelectedIndex();
        if (idx >= 0 && idx < CinemaDesktop.filmes.size()) {
            Filme f = CinemaDesktop.filmes.get(idx);
            jtaDetalhes.setText(formatarDetalhes(f));
        } else {
            JOptionPane.showMessageDialog(this, "Nenhum filme selecionado.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private String formatarDetalhes(Filme f) {
        try {
            return "Título: " + f.getTitulo() + "\n"
                + "Gênero: " + f.getGenero() + "\n"
                + "Duração (min): " + f.getDuracao() + "\n"
                + "Classificação: " + f.getClassificacao() + "\n\n"
                + "Descrição:\n" + f.getDescricao();
        } catch (Exception ex) {
            return f.toString();
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == btFechar) {
            this.dispose();
        } else if (src == btAtualizar) {
            atualizarLista();
            JOptionPane.showMessageDialog(this, "Lista atualizada.", "Atualizar", JOptionPane.INFORMATION_MESSAGE);
        } else if (src == btMostrar) {
            mostrarDetalhesSelecionado();
        }
    }

    private void atualizarLista() {
        JList<String> nova = CinemaDesktop.criarJListDeFilmes();
        listaFilmes.setModel(nova.getModel());
        jtaDetalhes.setText("");
    }

    private class InternalHandler extends InternalFrameAdapter {
        @Override
        public void internalFrameClosed(InternalFrameEvent e) {
            if (JInternalFrameVisualizarFilme.formulario > 0) {
                JInternalFrameVisualizarFilme.formulario--;
                CinemaDesktop.posicao -= 30;
            }
        }
    }
}
