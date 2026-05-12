import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;

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

public class JInternalFrameExcluirSessao extends JInternalFrame implements ActionListener {
    private JComboBox<String> comboSessoes;
    private JButton btExcluir;
    private JButton btCancelar;

    static int formulario = 0;

    public JInternalFrameExcluirSessao() {
        super("Excluir Sessão", // título
              false, // redimensionável
              true,  // fechável
              false, // maximizável
              true); // minimizável

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        addInternalFrameListener(new InternalHandler());

        ++formulario;
        CinemaDesktop.posicao += 30;

        JPanel painelPrincipal = new JPanel(new GridBagLayout());
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 8, 8, 8);
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;

        // Combo de sessões
        comboSessoes = new JComboBox<>();
        atualizarCombo();

        addComponente(painelPrincipal, new JLabel("Selecione uma sessão para excluir:"), c,
                0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE);
        addComponente(painelPrincipal, comboSessoes, c,
                1, 0, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL);

        // Botões
        btExcluir = new JButton("Excluir");
        btCancelar = new JButton("Cancelar");
        btExcluir.addActionListener(this);
        btCancelar.addActionListener(this);

        JPanel painelBotoes = new JPanel(new GridBagLayout());
        GridBagConstraints bc = new GridBagConstraints();
        bc.insets = new Insets(0, 6, 0, 6);
        bc.anchor = GridBagConstraints.EAST;
        bc.gridx = 0;
        bc.gridy = 0;
        painelBotoes.add(btExcluir, bc);
        bc.gridx = 1;
        painelBotoes.add(btCancelar, bc);

        addComponente(painelPrincipal, painelBotoes, c,
                0, 1, 3, 1, 1.0, 0.0, GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE);

        setLayout(new BorderLayout());
        add(painelPrincipal, BorderLayout.CENTER);

        setPreferredSize(new Dimension(520, 160));
        pack();
        setLocation(CinemaDesktop.posicao, CinemaDesktop.posicao * formulario);
        setVisible(true);
    }

    private void addComponente(JPanel container, Component comp, GridBagConstraints c,
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

    // Popula o combo com as sessões (exibindo filme, sala, data e hora)
    private void atualizarCombo() {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        model.addElement("-- Selecione --");
        SimpleDateFormat sdfData = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdfHora = new SimpleDateFormat("HH:mm");
        for (Sessao s : CinemaDesktop.sessoes) {
            String data = s.getDataInicio() != null ? sdfData.format(s.getDataInicio()) : "??/??/????";
            String hora = s.getHoraInicio() != null ? sdfHora.format(s.getHoraInicio()) : "??:??";
            model.addElement(s.getFilme() + " - " + s.getSala() + " - " + data + " " + hora);
        }
        comboSessoes.setModel(model);
        comboSessoes.setSelectedIndex(0);
    }

    // Exclui a sessão selecionada da lista.
    private void excluirSelecionado() {
        int idx = comboSessoes.getSelectedIndex() - 1; // -1 por causa do placeholder
        if (idx >= 0 && idx < CinemaDesktop.sessoes.size()) {
            Sessao s = CinemaDesktop.sessoes.get(idx);
            int confirmacaoOpt = JOptionPane.showConfirmDialog(this,
                    "Deseja realmente excluir a sessão:\n" +
                    "\"" + s.getFilme() + " - " + s.getSala() + "\" em " +
                    (s.getDataInicio() != null ? new SimpleDateFormat("dd/MM/yyyy").format(s.getDataInicio()) : "??/??/????") +
                    " " +
                    (s.getHoraInicio() != null ? new SimpleDateFormat("HH:mm").format(s.getHoraInicio()) : "??:??") + "?",
                    "Confirmação", JOptionPane.YES_NO_OPTION);
            if (confirmacaoOpt == JOptionPane.YES_OPTION) {
                CinemaDesktop.sessoes.remove(idx);

                SimpleDateFormat sdfData = new SimpleDateFormat("dd/MM/yyyy");
                sdfData.setLenient(false);
                SimpleDateFormat sdfHora = new SimpleDateFormat("HH:mm");
                sdfHora.setLenient(false);

                try {
                    File arquivo = new File("sessoes.txt");
                    arquivo.delete();
                    BufferedWriter bw = new BufferedWriter(new FileWriter(arquivo, true));

                    String linha;
                    for(int i = 0; i < CinemaDesktop.sessoes.size(); i++){
                        linha = CinemaDesktop.sessoes.get(i).getFilme() + ";" + CinemaDesktop.sessoes.get(i).getSala() + ";" + 
                        sdfData.format(CinemaDesktop.sessoes.get(i).getDataInicio()) + ";" + sdfHora.format(CinemaDesktop.sessoes.get(i).getHoraInicio());
                        bw.write(linha);
                        bw.newLine();
                    }
                
                    bw.close();
                } catch (IOException ioe) {
                    JOptionPane.showMessageDialog(this, "Não foi possível excluir os dados.", "Aviso", JOptionPane.WARNING_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erro ao salvar alterações.", "Erro", JOptionPane.ERROR_MESSAGE);
                }

                
                atualizarCombo();
                JOptionPane.showMessageDialog(this, "Sessão excluída com sucesso.", "Confirmação",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Nenhuma sessão selecionada.", "Aviso",
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
            if (JInternalFrameExcluirSessao.formulario > 0) {
                JInternalFrameExcluirSessao.formulario--;
                CinemaDesktop.posicao -= 30;
            }
        }
    }
}
