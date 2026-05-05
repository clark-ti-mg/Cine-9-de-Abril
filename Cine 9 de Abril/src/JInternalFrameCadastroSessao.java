import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

public class JInternalFrameCadastroSessao extends JInternalFrame implements ActionListener {
    private JTextField jtfFilme;
    private JTextField jtfSala;
    private JTextField jtfDataInicio;
    private JTextField jtfHoraInicio;
    private JButton btSalvar;
    private JButton btCancelar;
    static int formulario = 0;

    public JInternalFrameCadastroSessao() {
        super("Cadastro de Sessão",
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
        c.insets = new Insets(6, 6, 6, 6);

        // Componentes
        jtfFilme = new JTextField(30);
        jtfSala = new JTextField(10);
        jtfDataInicio = new JTextField(10);
        jtfHoraInicio = new JTextField(6);
        btSalvar = new JButton("Salvar");
        btCancelar = new JButton("Cancelar");
        btSalvar.addActionListener(this);
        btCancelar.addActionListener(this);

        // Linha 1: Filme
        addComponente(painelPrincipal, new JLabel("Filme:"), c, 0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE);
        addComponente(painelPrincipal, jtfFilme, c, 1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL);

        // Linha 2: Sala
        addComponente(painelPrincipal, new JLabel("Sala:"), c, 0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE);
        addComponente(painelPrincipal, jtfSala, c, 1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL);

        // Linha 3: Data de Início (dd/MM/yyyy)
        addComponente(painelPrincipal, new JLabel("Data de Início (dd/MM/yyyy):"), c, 0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE);
        addComponente(painelPrincipal, jtfDataInicio, c, 1, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL);

        // Linha 4: Hora de Início (HH:mm)
        addComponente(painelPrincipal, new JLabel("Hora de Início (HH:mm):"), c, 0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE);
        addComponente(painelPrincipal, jtfHoraInicio, c, 1, 3, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL);

        // Linha 5: Botões alinhados à direita ocupando duas colunas
        JPanel painelBotoes = new JPanel(new GridBagLayout());
        GridBagConstraints bc = new GridBagConstraints();
        bc.insets = new Insets(0, 6, 0, 6);
        bc.gridx = 0;
        bc.gridy = 0;
        painelBotoes.add(btSalvar, bc);
        bc.gridx = 1;
        painelBotoes.add(btCancelar, bc);

        addComponente(painelPrincipal, painelBotoes, c, 0, 4, 2, 1, 1.0, 0.0, GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE);

        setLayout(new BorderLayout());
        add(painelPrincipal, BorderLayout.CENTER);

        setPreferredSize(new Dimension(480, 240));
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

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btCancelar) {
            this.dispose();
            return;
        }

        if (e.getSource() == btSalvar) {
            if (!jtfFilme.getText().trim().equals("") &&
                !jtfSala.getText().trim().equals("") &&
                !jtfDataInicio.getText().trim().equals("") &&
                !jtfHoraInicio.getText().trim().equals("")) {

                String filme = jtfFilme.getText().trim();
                String sala = jtfSala.getText().trim();
                String dataStr = jtfDataInicio.getText().trim();
                String horaStr = jtfHoraInicio.getText().trim();
                    
                SimpleDateFormat sdfData = new SimpleDateFormat("dd/MM/yyyy");
                sdfData.setLenient(false);
                SimpleDateFormat sdfHora = new SimpleDateFormat("HH:mm");
                sdfHora.setLenient(false);

                try {
                    Date data = sdfData.parse(dataStr);
                    Date hora = sdfHora.parse(horaStr);

                    // formatos válidos
                    CinemaDesktop.sessoes.add(new Sessao(filme, sala, dataStr, horaStr));

                    JOptionPane.showMessageDialog(
                        null,
                        "Sessão cadastrada com sucesso",
                        "Confirmação",
                        JOptionPane.INFORMATION_MESSAGE
                    );

                    // limpa os campos após salvar
                    jtfFilme.setText("");
                    jtfSala.setText("");
                    jtfDataInicio.setText("");
                    jtfHoraInicio.setText("");

                } catch (ParseException pe) {
                    JOptionPane.showMessageDialog(
                        null,
                        "Formato de data ou hora inválido.\nData: dd/MM/yyyy  Hora: HH:mm",
                        "Aviso",
                        JOptionPane.WARNING_MESSAGE
                    );
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(
                        null,
                        "Erro ao cadastrar sessão",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE
                    );
                }

            } else {
                JOptionPane.showMessageDialog(
                    null,
                    "Campos obrigatórios não preenchidos",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE
                );
            }
        }
    }

    private class InternalHandler extends InternalFrameAdapter {
        @Override
        public void internalFrameClosed(InternalFrameEvent e) {
            if (JInternalFrameCadastroSessao.formulario > 0) {
                JInternalFrameCadastroSessao.formulario--;
                CinemaDesktop.posicao -= 30;
            }
        }
    }
}
