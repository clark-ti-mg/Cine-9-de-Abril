import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

public class JInternalFrameExcluirFilme extends JInternalFrame implements ActionListener {
    private JComboBox<String> comboFilmes;
    private JButton btExcluir;
    private JButton btCancelar;

    static int formulario = 0;

    public JInternalFrameExcluirFilme() {
        super("Excluir Filme", // título
              false, // redimensionável
              true,  // fechável
              false, // maximizável
              true); // minimizável

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        addInternalFrameListener(new InternalHandler());

        ++formulario;
        CinemaDesktop.posicao += 30;

        // Painel principal
        JPanel painelPrincipal = new JPanel(new GridLayout(2, 1, 8, 8));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Linha 1: ComboBox de filmes
        comboFilmes = new JComboBox<>();
        atualizarCombo();
        JPanel linhaCombo = new JPanel(new BorderLayout(5, 5));
        linhaCombo.add(new JLabel("Selecione um filme para excluir:"), BorderLayout.NORTH);
        linhaCombo.add(comboFilmes, BorderLayout.CENTER);
        painelPrincipal.add(linhaCombo);

        // Linha 2: Botões
        btExcluir = new JButton("Excluir");
        btCancelar = new JButton("Cancelar");

        btExcluir.addActionListener(this);
        btCancelar.addActionListener(this);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        painelBotoes.add(btExcluir);
        painelBotoes.add(btCancelar);
        painelPrincipal.add(painelBotoes);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(painelPrincipal, BorderLayout.CENTER);

        setPreferredSize(new Dimension(400, 180));
        pack();
        setLocation(CinemaDesktop.posicao, CinemaDesktop.posicao * formulario);
        setVisible(true);
    }

    // Popula o combo com os títulos dos filmes.
    private void atualizarCombo() {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        model.addElement("-- Selecione --");
        for (Filme f : CinemaDesktop.filmes) {
            model.addElement(f.getTitulo());
        }
        comboFilmes.setModel(model);
        comboFilmes.setSelectedIndex(0);
    }

    // Exclui o filme selecionado da lista.
    private void excluirSelecionado() {
        int idx = comboFilmes.getSelectedIndex() - 1; // -1 por causa do placeholder
        if (idx >= 0 && idx < CinemaDesktop.filmes.size()) {
            Filme f = CinemaDesktop.filmes.get(idx);
            int confirmacaoOpt = JOptionPane.showConfirmDialog(this,
                    "Deseja realmente excluir o filme \"" + f.getTitulo() + "\"?",
                    "Confirmação", JOptionPane.YES_NO_OPTION);
            if (confirmacaoOpt == JOptionPane.YES_OPTION) {
                CinemaDesktop.filmes.remove(idx);
                atualizarCombo();
                JOptionPane.showMessageDialog(this, "Filme excluído com sucesso.", "Confirmação",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Nenhum filme selecionado.", "Aviso",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == btCancelar) {
            this.dispose();
        } else if (src == btExcluir) {
            excluirSelecionado();
        }
    }

    private class InternalHandler extends InternalFrameAdapter {
        @Override
        public void internalFrameClosed(InternalFrameEvent e) {
            if (JInternalFrameExcluirFilme.formulario > 0) {
                JInternalFrameExcluirFilme.formulario--;
                CinemaDesktop.posicao -= 30;
            }
        }
    }
}
