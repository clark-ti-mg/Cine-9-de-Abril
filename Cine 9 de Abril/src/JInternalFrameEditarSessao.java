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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

public class JInternalFrameEditarSessao extends JInternalFrame implements ActionListener {
    private JComboBox<String> comboSessoes;
    private JTextField jtfFilme;
    private JTextField jtfSala;
    private JTextField jtfDataInicio;
    private JTextField jtfHoraInicio;

    private JButton btSalvar;
    private JButton btCancelar;

    static int formulario = 0;
    private int indiceCarregado = -1;

    public JInternalFrameEditarSessao() {
        super("Editar Sessão", false, true, false, true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        addInternalFrameListener(new InternalHandler());

        ++formulario;
        CinemaDesktop.posicao += 30;

        JPanel painelPrincipal = new JPanel(new GridBagLayout());
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;

        // Componentes
        comboSessoes = new JComboBox<>();
        jtfFilme = new JTextField(30);
        jtfSala = new JTextField(10);
        jtfDataInicio = new JTextField(10); // dd/MM/yyyy
        jtfHoraInicio = new JTextField(6);  // HH:mm

        btSalvar = new JButton("Salvar");
        btCancelar = new JButton("Cancelar");
        btSalvar.addActionListener(this);
        btCancelar.addActionListener(this);

        // Linha 0: Combo de sessões (label + combo)
        addComponente(painelPrincipal, new JLabel("Selecione uma sessão para editar:"), c, 0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE);
        addComponente(painelPrincipal, comboSessoes, c, 1, 0, 3, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL);

        // Popula combo e listener
        atualizarCombo();
        comboSessoes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int idx = comboSessoes.getSelectedIndex() - 1; // placeholder na posição 0
                if (idx >= 0 && idx < CinemaDesktop.sessoes.size()) {
                    carregarSelecionado(idx);
                } else {
                    limparCamposEBloquear();
                }
            }
        });

        // Linha 1: Filme
        addComponente(painelPrincipal, new JLabel("Filme:"), c, 0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE);
        addComponente(painelPrincipal, jtfFilme, c, 1, 1, 3, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL);

        // Linha 2: Sala
        addComponente(painelPrincipal, new JLabel("Sala:"), c, 0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE);
        addComponente(painelPrincipal, jtfSala, c, 1, 2, 1, 1, 0.3, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL);

        // Linha 2 (continuação): Data
        addComponente(painelPrincipal, new JLabel("Data (dd/MM/yyyy):"), c, 2, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE);
        addComponente(painelPrincipal, jtfDataInicio, c, 3, 2, 1, 1, 0.4, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL);

        // Linha 3: Hora
        addComponente(painelPrincipal, new JLabel("Hora (HH:mm):"), c, 0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE);
        addComponente(painelPrincipal, jtfHoraInicio, c, 1, 3, 1, 1, 0.2, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL);

        // Linha 4: Botões (Salvar | Cancelar) alinhados à direita
        JPanel painelBotoes = new JPanel(new GridBagLayout());
        GridBagConstraints bc = new GridBagConstraints();
        bc.insets = new Insets(0, 6, 0, 6);
        bc.gridx = 0;
        bc.gridy = 0;
        painelBotoes.add(btSalvar, bc);
        bc.gridx = 1;
        painelBotoes.add(btCancelar, bc);

        addComponente(painelPrincipal, painelBotoes, c, 0, 4, 4, 1, 1.0, 0.0, GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE);

        setLayout(new BorderLayout());
        add(painelPrincipal, BorderLayout.CENTER);

        // Inicialmente campos bloqueados e botão salvar desabilitado
        limparCamposEBloquear();

        setPreferredSize(new Dimension(640, 260));
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

    private void carregarSelecionado(int idx) {
        if (idx >= 0 && idx < CinemaDesktop.sessoes.size()) {
            Sessao s = CinemaDesktop.sessoes.get(idx);
            indiceCarregado = idx;
            jtfFilme.setText(s.getFilme());
            jtfSala.setText(s.getSala());

            SimpleDateFormat sdfData = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat sdfHora = new SimpleDateFormat("HH:mm");
            jtfDataInicio.setText(s.getDataInicio() != null ? sdfData.format(s.getDataInicio()) : "");
            jtfHoraInicio.setText(s.getHoraInicio() != null ? sdfHora.format(s.getHoraInicio()) : "");

            setCamposEditaveis(true);
            btSalvar.setEnabled(true);
        } else {
            limparCamposEBloquear();
        }
    }

    private void limparCamposEBloquear() {
        indiceCarregado = -1;
        jtfFilme.setText("");
        jtfSala.setText("");
        jtfDataInicio.setText("");
        jtfHoraInicio.setText("");
        setCamposEditaveis(false);
        btSalvar.setEnabled(false);
    }

    private void setCamposEditaveis(boolean editaveis) {
        jtfFilme.setEditable(editaveis);
        jtfSala.setEditable(editaveis);
        jtfDataInicio.setEditable(editaveis);
        jtfHoraInicio.setEditable(editaveis);
    }

    private void salvarAlteracoes() {
        if (indiceCarregado < 0 || indiceCarregado >= CinemaDesktop.sessoes.size()) {
            JOptionPane.showMessageDialog(this, "Nenhuma sessão carregada para salvar.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String filme = jtfFilme.getText().trim();
        String sala = jtfSala.getText().trim();
        String dataStr = jtfDataInicio.getText().trim();
        String horaStr = jtfHoraInicio.getText().trim();

        if (filme.isEmpty() || sala.isEmpty() || dataStr.isEmpty() || horaStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Campos obrigatórios não preenchidos.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        SimpleDateFormat sdfData = new SimpleDateFormat("dd/MM/yyyy");
        sdfData.setLenient(false);
        SimpleDateFormat sdfHora = new SimpleDateFormat("HH:mm");
        sdfHora.setLenient(false);

        try {
            Date data = sdfData.parse(dataStr);
            Date hora = sdfHora.parse(horaStr);

            // Atualiza o objeto Sessao existente usando os setters que recebem String
            Sessao s = CinemaDesktop.sessoes.get(indiceCarregado);
            s.setFilme(filme);
            s.setSala(sala);
            s.setDataInicio(dataStr); // Sessao.setDataInicio espera String
            s.setHoraInicio(horaStr); // Sessao.setHoraInicio espera String

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

            // Atualiza visual do combo mantendo seleção
            int selecionado = indiceCarregado;
            atualizarCombo();
            comboSessoes.setSelectedIndex(selecionado + 1); // +1 por placeholder

            CinemaDesktop.carregarDadosSessoes();

            JOptionPane.showMessageDialog(this, "Alterações salvas com sucesso.", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
        } catch (ParseException pe) {
            JOptionPane.showMessageDialog(this, "Formato de data ou hora inválido.\nData: dd/MM/yyyy  Hora: HH:mm", "Aviso", JOptionPane.WARNING_MESSAGE);
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(this, "Não foi possível editar os dados.", "Aviso", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar alterações.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == btCancelar) {
            this.dispose();
        } else if (src == btSalvar) {
            salvarAlteracoes();
        }
    }

    private class InternalHandler extends InternalFrameAdapter {
        @Override
        public void internalFrameClosed(InternalFrameEvent e) {
            if (JInternalFrameEditarSessao.formulario > 0) {
                JInternalFrameEditarSessao.formulario--;
                CinemaDesktop.posicao -= 30;
            }
        }
    }
}
