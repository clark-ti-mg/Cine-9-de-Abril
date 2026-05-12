import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

public class JInternalFrameEditarFilme extends JInternalFrame implements ActionListener {
    private JComboBox<String> comboFilmes;
    private JTextField jtfTitulo;
    private JTextField jtfGenero;
    private JTextField jtfDuracao;
    private JTextField jtfClassificacao;
    private JTextArea jtaDescricao;

    private JButton btSalvar;
    private JButton btCancelar;

    static int formulario = 0;

    // índice do filme atualmente carregado para edição; -1 = nenhum
    private int indiceCarregado = -1;

    public JInternalFrameEditarFilme() {
        super("Editar Filme", // título
              false, // redimensionável
              true,  // fechável
              false, // maximizável
              true); // minimizável

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        addInternalFrameListener(new InternalHandler());

        ++formulario;
        CinemaDesktop.posicao += 30;

        // Painel principal com GridLayout: 6 linhas (combo + 4 campos + descrição)
        JPanel painelPrincipal = new JPanel(new GridLayout(6, 1, 8, 8));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Linha 1: ComboBox de filmes
        comboFilmes = new JComboBox<>();
        atualizarCombo(); // popula o combo inicialmente
        comboFilmes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Carrega automaticamente ao selecionar um filme válido
                int idx = comboFilmes.getSelectedIndex() - 1; // -1 porque modelo tem placeholder
                if (idx >= 0 && idx < CinemaDesktop.filmes.size()) {
                    carregarSelecionado(idx);
                } else {
                    limparCamposEBloquear();
                }
            }
        });
        JPanel linhaCombo = new JPanel(new BorderLayout(5, 5));
        linhaCombo.add(new JLabel("Selecione um filme para editar:"), BorderLayout.WEST);
        linhaCombo.add(comboFilmes, BorderLayout.CENTER);
        painelPrincipal.add(linhaCombo);

        // Linha 2: Título
        jtfTitulo = new JTextField(30);
        JPanel linhaTitulo = new JPanel(new BorderLayout(5, 5));
        linhaTitulo.add(new JLabel("Título:"), BorderLayout.WEST);
        linhaTitulo.add(jtfTitulo, BorderLayout.CENTER);
        painelPrincipal.add(linhaTitulo);

        // Linha 3: Gênero
        jtfGenero = new JTextField(20);
        JPanel linhaGenero = new JPanel(new BorderLayout(5, 5));
        linhaGenero.add(new JLabel("Gênero:"), BorderLayout.WEST);
        linhaGenero.add(jtfGenero, BorderLayout.CENTER);
        painelPrincipal.add(linhaGenero);

        // Linha 4: Duração (em minutos)
        jtfDuracao = new JTextField(8);
        JPanel linhaDuracao = new JPanel(new BorderLayout(5, 5));
        linhaDuracao.add(new JLabel("Duração (min):"), BorderLayout.WEST);
        linhaDuracao.add(jtfDuracao, BorderLayout.CENTER);
        painelPrincipal.add(linhaDuracao);

        // Linha 5: Classificação
        jtfClassificacao = new JTextField(4);
        JPanel linhaClassificacao = new JPanel(new BorderLayout(5, 5));
        linhaClassificacao.add(new JLabel("Classificação:"), BorderLayout.WEST);
        linhaClassificacao.add(jtfClassificacao, BorderLayout.CENTER);
        painelPrincipal.add(linhaClassificacao);

        // Linha 6: Descrição (JTextArea dentro de JScrollPane)
        jtaDescricao = new JTextArea(5, 30);
        jtaDescricao.setLineWrap(true);
        jtaDescricao.setWrapStyleWord(true);
        JScrollPane descricaoScroll = new JScrollPane(jtaDescricao, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        JPanel linhaDescricao = new JPanel(new BorderLayout(5, 5));
        linhaDescricao.add(new JLabel("Descrição:"), BorderLayout.NORTH);
        linhaDescricao.add(descricaoScroll, BorderLayout.CENTER);
        painelPrincipal.add(linhaDescricao);

        // Painel de botões (Salvar, Cancelar)
        btSalvar = new JButton("Salvar");
        btCancelar = new JButton("Cancelar");

        btSalvar.addActionListener(this);
        btCancelar.addActionListener(this);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        painelBotoes.add(btSalvar);
        painelBotoes.add(btCancelar);

        setLayout(new BorderLayout());
        add(painelPrincipal, BorderLayout.CENTER);
        add(painelBotoes, BorderLayout.SOUTH);

        // Inicialmente campos bloqueados e botão salvar desabilitado
        limparCamposEBloquear();

        setPreferredSize(new Dimension(600, 420));
        pack();
        setLocation(CinemaDesktop.posicao, CinemaDesktop.posicao * formulario);
        setVisible(true);
    }

    // Popula o combo com os títulos dos filmes.
    private void atualizarCombo() {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();

        // Adiciona um placeholder na posição 0 ("-- Selecione --") para forçar seleção explícita.
        model.addElement("-- Selecione --");

        for (Filme f : CinemaDesktop.filmes) {
            model.addElement(f.getTitulo());
        }

        comboFilmes.setModel(model);
        comboFilmes.setSelectedIndex(0);
    }

    // Carrega os dados do filme no índice informado e habilita edição.
    private void carregarSelecionado(int idx) {
        if (idx >= 0 && idx < CinemaDesktop.filmes.size()) {
            Filme f = CinemaDesktop.filmes.get(idx);
            indiceCarregado = idx;
            jtfTitulo.setText(f.getTitulo());
            jtfGenero.setText(f.getGenero());
            jtfDuracao.setText(String.valueOf(f.getDuracao()));
            jtfClassificacao.setText(String.valueOf(f.getClassificacao()));
            jtaDescricao.setText(f.getDescricao());

            // Habilita edição
            setCamposEditaveis(true);
            btSalvar.setEnabled(true);
        } else {
            limparCamposEBloquear();
        }
    }

    // Limpa os campos e bloqueia a edição.
    private void limparCamposEBloquear() {
        indiceCarregado = -1;
        jtfTitulo.setText("");
        jtfGenero.setText("");
        jtfDuracao.setText("");
        jtfClassificacao.setText("");
        jtaDescricao.setText("");
        setCamposEditaveis(false);
        btSalvar.setEnabled(false);
    }

    // Ativa ou desativa a edição dos campos.
    private void setCamposEditaveis(boolean editaveis) {
        jtfTitulo.setEditable(editaveis);
        jtfGenero.setEditable(editaveis);
        jtfDuracao.setEditable(editaveis);
        jtfClassificacao.setEditable(editaveis);
        jtaDescricao.setEditable(editaveis);
    }

    // Salva as alterações feitas nos campos no objeto Filme correspondente.
    private void salvarAlteracoes() {
        if (indiceCarregado < 0 || indiceCarregado >= CinemaDesktop.filmes.size()) {
            JOptionPane.showMessageDialog(this, "Nenhum filme carregado para salvar.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String titulo = jtfTitulo.getText().trim();
        String genero = jtfGenero.getText().trim();
        String duracaoStr = jtfDuracao.getText().trim();
        String classificacaoStr = jtfClassificacao.getText().trim();
        String descricao = jtaDescricao.getText().trim();

        if (titulo.isEmpty() || genero.isEmpty() || duracaoStr.isEmpty() || classificacaoStr.isEmpty() || descricao.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Campos obrigatórios não preenchidos.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int duracao;
        int classificacao;
        try {
            duracao = Integer.parseInt(duracaoStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Duração deve ser um número inteiro.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            classificacao = Integer.parseInt(classificacaoStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Classificação deve ser um número inteiro.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Atualiza o objeto Filme na lista
        Filme f = CinemaDesktop.filmes.get(indiceCarregado);
        f.setTitulo(titulo);
        f.setGenero(genero);
        f.setDuracao(duracao);
        f.setClassificacao(classificacao);
        f.setDescricao(descricao);

        try {
            File arquivo = new File("filmes.fi");

            arquivo.delete();
            arquivo.createNewFile();

            FileOutputStream fos = new FileOutputStream(arquivo);
            ObjectOutputStream objo = new ObjectOutputStream(fos);

            objo.writeObject(CinemaDesktop.filmes);

            objo.close();
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(
                null, 
                "Houve problema ao manipular o arquivo de entrada e saída de filmes:" + ioe.getMessage(), 
                "Aviso", 
                JOptionPane.WARNING_MESSAGE
            );
        }
        
        // Atualiza o combo visual (mantém seleção no mesmo índice)
        int selecionado = indiceCarregado;
        atualizarCombo();
        comboFilmes.setSelectedIndex(selecionado + 1); // +1 por causa do placeholder

        CinemaDesktop.carregarDadosFilmes();

        JOptionPane.showMessageDialog(this, "Alterações salvas com sucesso.", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
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
            if (JInternalFrameEditarFilme.formulario > 0) {
                JInternalFrameEditarFilme.formulario--;
                CinemaDesktop.posicao -= 30;
            }
        }
    }
}
