import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;

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

public class JInternalFrameVisualizarSessao extends JInternalFrame implements ActionListener {

    private JList<String> listaSessoes;
    private JTextArea jtaDetalhes;
    private JButton btFechar;
    private JButton btAtualizar;
    private JButton btMostrar;
    static int formulario = 0;

    public JInternalFrameVisualizarSessao() {
        super("Visualizar Sessões", false, true, false, true);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        addInternalFrameListener(new InternalHandler());

        ++formulario;
        CinemaDesktop.posicao += 30;

        // carregar os dados
        CinemaDesktop.carregarDados();

        JPanel painelPrincipal = new JPanel(new GridBagLayout());
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.fill = GridBagConstraints.BOTH;

        // Lista de sessões
        listaSessoes = criarJListDeSessoes();
        listaSessoes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollLista = new JScrollPane(listaSessoes);
        scrollLista.setPreferredSize(new Dimension(260, 300));

        addComponente(painelPrincipal, scrollLista, c, 0, 0, 1, 2, 0.3, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH);

        // Painel de detalhes
        JLabel lblDetalhes = new JLabel("Detalhes da Sessão:");
        addComponente(painelPrincipal, lblDetalhes, c, 1, 0, 1, 1, 0.7, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL);

        jtaDetalhes = new JTextArea();
        jtaDetalhes.setEditable(false);
        jtaDetalhes.setLineWrap(true);
        jtaDetalhes.setWrapStyleWord(true);
        JScrollPane scrollDetalhes = new JScrollPane(jtaDetalhes);
        scrollDetalhes.setPreferredSize(new Dimension(300, 260));

        addComponente(painelPrincipal, scrollDetalhes, c, 1, 1, 1, 1, 0.7, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH);

        // Botões
        btMostrar = new JButton("Mostrar");
        btAtualizar = new JButton("Atualizar");
        btFechar = new JButton("Fechar");

        btMostrar.addActionListener(this);
        btAtualizar.addActionListener(this);
        btFechar.addActionListener(this);

        JPanel painelBotoes = new JPanel(new GridBagLayout());
        GridBagConstraints bc = new GridBagConstraints();
        bc.insets = new Insets(0, 6, 0, 6);
        bc.gridx = 0;
        painelBotoes.add(btMostrar, bc);
        bc.gridx = 1;
        painelBotoes.add(btAtualizar, bc);
        bc.gridx = 2;
        painelBotoes.add(btFechar, bc);

        addComponente(painelPrincipal, painelBotoes, c, 1, 2, 1, 1, 0.7, 0.0, GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE);

        add(painelPrincipal, BorderLayout.CENTER);

        setPreferredSize(new Dimension(640, 380));
        pack();
        setLocation(CinemaDesktop.posicao, CinemaDesktop.posicao * formulario);
        setVisible(true);
    }

    private void addComponente(JPanel container, java.awt.Component comp, GridBagConstraints c,
                                    int gridx, int gridy, int gridwidth, int gridheight,
                                    double weightx, double weighty, int anchor, int fill) {
        c.gridx = gridx;
        c.gridy = gridy;
        c.gridwidth = gridwidth;
        c.gridheight = gridheight;
        c.weightx = weightx;
        c.weighty = weighty;
        c.anchor = anchor;
        c.fill = fill;
        container.add(comp, c);
    }

    private JList<String> criarJListDeSessoes() {
        SimpleDateFormat sdfData = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdfHora = new SimpleDateFormat("HH:mm");
        String[] dados = new String[CinemaDesktop.sessoes.size()];
        for (int i = 0; i < CinemaDesktop.sessoes.size(); i++) {
            Sessao s = CinemaDesktop.sessoes.get(i);
            String data = s.getDataInicio() != null ? sdfData.format(s.getDataInicio()) : "??/??/????";
            String hora = s.getHoraInicio() != null ? sdfHora.format(s.getHoraInicio()) : "??:??";
            dados[i] = s.getFilme() + " - " + s.getSala() + " - " + data + " " + hora;
        }
        return new JList<>(dados);
    }

    private void mostrarDetalhesSelecionado() {
        int idx = listaSessoes.getSelectedIndex();
        if (idx >= 0 && idx < CinemaDesktop.sessoes.size()) {
            Sessao s = CinemaDesktop.sessoes.get(idx);
            jtaDetalhes.setText(formatarDetalhes(s));
        } else {
            JOptionPane.showMessageDialog(this, "Nenhuma sessão selecionada.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private String formatarDetalhes(Sessao s) {
        try {
            SimpleDateFormat sdfData = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat sdfHora = new SimpleDateFormat("HH:mm");
            return "Filme: " + s.getFilme() + "\n"
                + "Sala: " + s.getSala() + "\n"
                + "Data: " + (s.getDataInicio() != null ? sdfData.format(s.getDataInicio()) : "") + "\n"
                + "Hora: " + (s.getHoraInicio() != null ? sdfHora.format(s.getHoraInicio()) : "");
        } catch (Exception ex) {
            return s.toString();
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
        JList<String> nova = criarJListDeSessoes();
        listaSessoes.setModel(nova.getModel());
        jtaDetalhes.setText("");
    }

    private class InternalHandler extends InternalFrameAdapter {
        @Override
        public void internalFrameClosed(InternalFrameEvent e) {
            if (JInternalFrameVisualizarSessao.formulario > 0) {
                JInternalFrameVisualizarSessao.formulario--;
                CinemaDesktop.posicao -= 30;
            }
        }
    }
}
