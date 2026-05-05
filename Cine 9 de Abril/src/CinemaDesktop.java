import java.util.ArrayList;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class CinemaDesktop extends JFrame implements ActionListener{
    JList<String> jListaFilmes;
    JList<String> jListaSessoes;
    public static ArrayList<Filme> filmes;
    public static ArrayList<Sessao> sessoes;
    public static int posicao = 0;
    JDesktopPane jdpPanel;

    JMenuBar menuBar;
    JMenu aparenciaMenu, lafMenu, filmeMenu, corFundoMenu, sistemaMenu, sessaoMenu;
    JMenuItem cadastrarFilme, visualizarFilmes, excluirFilme, editarFilme;
    JMenuItem cadastrarSessao, visualizarSessoes, excluirSessao, editarSessao, apresentarPromocoes;
    JMenuItem informacoesItem, sairItem;

    JCheckBoxMenuItem corBranco, corPreto;

    CheckBoxCorFundo cbCor;

    CinemaDesktop(){
        setTitle("Cine 9 de Abril");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(this);
        setResizable(true);
        int bordas = 50;
        Dimension tela = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(bordas, bordas, tela.width - bordas*2, tela.height - bordas*2);
        setExtendedState(Frame.MAXIMIZED_BOTH);

        cbCor = new CheckBoxCorFundo();
        
        filmes = new ArrayList<>();
        sessoes = new ArrayList<>();

        jdpPanel = new JDesktopPane();
        setContentPane(jdpPanel);

        // instala look and feels
        UIManager.installLookAndFeel(
            new UIManager.LookAndFeelInfo("FlatLaf Light", "com.formdev.flatlaf.FlatLightLaf")
        );
        UIManager.installLookAndFeel(
            new UIManager.LookAndFeelInfo("FlatLaf Dark", "com.formdev.flatlaf.FlatDarkLaf")
        );

        UIManager.installLookAndFeel(
            new UIManager.LookAndFeelInfo("Tiny", "net.sf.tinylaf.TinyLookAndFeel")
        );
        
        // cria e seta a barra de menus
        setJMenuBar(criarMenuBar());

        // carrega os dados do arquivo ao abrir o programa
        carregarDados();

        setVisible(true);
    }

    public static void carregarDados(){
        try {
            File arquivo = new File("sessoes.txt");
            BufferedReader br = new BufferedReader(new FileReader(arquivo));
            CinemaDesktop.sessoes = new ArrayList<>();
            
            String linha;
            while((linha = br.readLine()) != null){
                String[] linhaSplit = linha.split(";");
                CinemaDesktop.sessoes.add(new Sessao(linhaSplit[0], linhaSplit[1], linhaSplit[2], linhaSplit[3]));
            }
            br.close();
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(null, "Não foi possível carregar os dados.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    public static JList<String> criarJListDeFilmes() {
        DefaultListModel<String> modelo = new DefaultListModel<>();

        for (Filme f : CinemaDesktop.filmes) {
            modelo.addElement(f.getTitulo() + " - " + f.getGenero());
        }

        JList<String> jlista = new JList<>(modelo);
        jlista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        return jlista;
    }


    private JMenuBar criarMenuBar() {
        menuBar = new JMenuBar();

        // Aparência
        aparenciaMenu = new JMenu("Aparência");

        // Look and Feels (submenu)
        lafMenu = new JMenu("Look and Feels");

        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                JMenuItem item = new JMenuItem(info.getName());
                item.setActionCommand(info.getClassName());
                item.addActionListener(new LookAndFeelHandler());
                lafMenu.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        aparenciaMenu.add(lafMenu);

        // Cor de fundo (submenu)
        corFundoMenu = new JMenu("Cor de fundo");
        corBranco = new JCheckBoxMenuItem("Branco");
        corPreto = new JCheckBoxMenuItem("Preto");

        ButtonGroup bgCorFundo = new ButtonGroup();
        bgCorFundo.add(corBranco);
        bgCorFundo.add(corPreto);
        corFundoMenu.add(corBranco);
        corFundoMenu.add(corPreto);
        aparenciaMenu.add(corFundoMenu);

        menuBar.add(aparenciaMenu);

        // Filme
        filmeMenu = new JMenu("Filme");
        cadastrarFilme = new JMenuItem("Cadastrar filme");
        visualizarFilmes = new JMenuItem("Visualizar filmes");
        excluirFilme = new JMenuItem("Excluir filme");
        editarFilme = new JMenuItem("Editar filme");
        apresentarPromocoes = new JMenuItem("Apresentar promoções");

        cadastrarFilme.setActionCommand("cadastrarFilme");
        visualizarFilmes.setActionCommand("visualizarFilmes");
        excluirFilme.setActionCommand("excluirFilme");
        editarFilme.setActionCommand("editarFilme");
        apresentarPromocoes.setActionCommand("apresentarPromocoes");

        cadastrarFilme.addActionListener(this);
        visualizarFilmes.addActionListener(this);
        excluirFilme.addActionListener(this);
        editarFilme.addActionListener(this);
        apresentarPromocoes.addActionListener(this);

        filmeMenu.add(cadastrarFilme);
        filmeMenu.add(visualizarFilmes);
        filmeMenu.add(excluirFilme);
        filmeMenu.add(editarFilme);
        filmeMenu.add(apresentarPromocoes);
        menuBar.add(filmeMenu);

        // Sessão
        sessaoMenu = new JMenu("Sessão");
        cadastrarSessao = new JMenuItem("Cadastrar sessão");
        visualizarSessoes = new JMenuItem("Visualizar sessão");
        excluirSessao = new JMenuItem("Excluir sessão");
        editarSessao = new JMenuItem("Editar sessão");

        cadastrarSessao.addActionListener(this);
        visualizarSessoes.addActionListener(this);
        excluirSessao.addActionListener(this);
        editarSessao.addActionListener(this);

        cadastrarSessao.setActionCommand("cadastrarSessao");
        visualizarSessoes.setActionCommand("visualizarSessoes");
        excluirSessao.setActionCommand("excluirSessao");
        editarSessao.setActionCommand("editarSessao");

        sessaoMenu.add(cadastrarSessao);
        sessaoMenu.add(visualizarSessoes);
        sessaoMenu.add(excluirSessao);
        sessaoMenu.add(editarSessao);
        menuBar.add(sessaoMenu);

        // Sistema
        sistemaMenu = new JMenu("Sistema");
        informacoesItem = new JMenuItem("Informações");
        sairItem = new JMenuItem("Sair");
        sistemaMenu.add(informacoesItem);
        sistemaMenu.addSeparator();
        sistemaMenu.add(sairItem);
        menuBar.add(sistemaMenu);

        corBranco.setSelected(true);
        corBranco.addItemListener(cbCor);
        corPreto.addItemListener(cbCor);

        sairItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                System.exit(0);
            }
        });

        informacoesItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(CinemaDesktop.this,
                        "Cine 9 de Abril\nVersão: 1.0\nJavaLab.",
                        "Informações", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        return menuBar;
    }

    

    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getActionCommand() == "cadastrarFilme"){
            if (JInternalFrameCadastroFilme.formulario==0) {
                JInternalFrameCadastroFilme frame = new JInternalFrameCadastroFilme();
                jdpPanel.add(frame);
                try {
                    frame.setSelected(true);
                } catch (Exception ex) {
                }
            }else{
                System.out.println("Já há uma aba de cadastro aberta");
            }
            
        }
        if(e.getActionCommand() == "visualizarFilmes"){
            if (JInternalFrameVisualizarFilme.formulario==0) {
                JInternalFrameVisualizarFilme frame = new JInternalFrameVisualizarFilme();
                jdpPanel.add(frame);
                try {
                    frame.setSelected(true);
                } catch (Exception ex) {
                }
            }else{
                System.out.println("Já há uma aba de visualização aberta");
            }
            
        }

        if(e.getActionCommand() == "editarFilme"){
            if (JInternalFrameEditarFilme.formulario==0) {
                JInternalFrameEditarFilme frame = new JInternalFrameEditarFilme();
                jdpPanel.add(frame);
                try {
                    frame.setSelected(true);
                } catch (Exception ex) {
                }
            }else{
                System.out.println("Já há uma aba de edição aberta");
            }
            
        }

        if(e.getActionCommand() == "excluirFilme"){
            if (JInternalFrameExcluirFilme.formulario==0) {
                JInternalFrameExcluirFilme frame = new JInternalFrameExcluirFilme();
                jdpPanel.add(frame);
                try {
                    frame.setSelected(true);
                } catch (Exception ex) {
                }
            }else{
                System.out.println("Já há uma aba de exclusão aberta");
            }
            
        }

        if(e.getActionCommand() == "apresentarPromocoes"){
            if (JInternalFramePromocao.formulario==0) {
                if(filmes.size()==0){
                    JOptionPane.showMessageDialog(CinemaDesktop.this,
                        "Nenhum filme cadastrado. Sem promoções.",
                        "Aviso", JOptionPane.ERROR_MESSAGE);
                }else{
                    JInternalFramePromocao frame = new JInternalFramePromocao();
                    jdpPanel.add(frame);
                    try {
                        frame.setSelected(true);
                    } catch (Exception ex) {
                    }
                }
            }else{
                System.out.println("Já há uma aba de promoções aberta");
            }
            
        }

        if(e.getActionCommand() == "cadastrarSessao"){
            if (JInternalFrameCadastroSessao.formulario==0) {
                if(filmes.size()==0){
                    JOptionPane.showMessageDialog(CinemaDesktop.this,
                        "Nenhum filme cadastrado ainda.",
                        "Aviso", JOptionPane.ERROR_MESSAGE);
                }else{
                    JInternalFrameCadastroSessao frame = new JInternalFrameCadastroSessao();
                    jdpPanel.add(frame);
                    try {
                        frame.setSelected(true);
                    } catch (Exception ex) {
                    }
                }
            }else{
                System.out.println("Já há uma aba de cadastro de sessão aberta");
            }
            
        }

        if(e.getActionCommand() == "editarSessao"){
            if (JInternalFrameEditarSessao.formulario==0) {
                if(sessoes.size()==0){
                    JOptionPane.showMessageDialog(CinemaDesktop.this,
                        "Nenhuma sessão cadastrada ainda.",
                        "Aviso", JOptionPane.ERROR_MESSAGE);
                }else{
                    JInternalFrameEditarSessao frame = new JInternalFrameEditarSessao();
                    jdpPanel.add(frame);
                    try {
                        frame.setSelected(true);
                    } catch (Exception ex) {
                    }
                }
            }else{
                System.out.println("Já há uma aba de edição de sessão aberta");
            }
            
        }

        if(e.getActionCommand() == "excluirSessao"){
            if (JInternalFrameExcluirSessao.formulario==0) {
                if(sessoes.size()==0){
                    JOptionPane.showMessageDialog(CinemaDesktop.this,
                        "Nenhuma sessão cadastrada ainda.",
                        "Aviso", JOptionPane.ERROR_MESSAGE);
                }else{
                    JInternalFrameExcluirSessao frame = new JInternalFrameExcluirSessao();
                    jdpPanel.add(frame);
                    try {
                        frame.setSelected(true);
                    } catch (Exception ex) {
                    }
                }
            }else{
                System.out.println("Já há uma aba de exclusão de sessão aberta");
            }
            
        }

        if(e.getActionCommand() == "visualizarSessoes"){
            if (JInternalFrameVisualizarSessao.formulario==0) {
                if(sessoes.size()==0){
                    JOptionPane.showMessageDialog(CinemaDesktop.this,
                        "Nenhuma sessão cadastrada ainda.",
                        "Aviso", JOptionPane.ERROR_MESSAGE);
                }else{
                    JInternalFrameVisualizarSessao frame = new JInternalFrameVisualizarSessao();
                    jdpPanel.add(frame);
                    try {
                        frame.setSelected(true);
                    } catch (Exception ex) {
                    }
                }
            }else{
                System.out.println("Já há uma aba de exclusão de sessão aberta");
            }
            
        }


    }

    private class CheckBoxCorFundo implements ItemListener{
        public void itemStateChanged(ItemEvent it){
            if(corBranco.isSelected()){
                CinemaDesktop.this.jdpPanel.setBackground(Color.white);
                CinemaDesktop.this.jdpPanel.repaint();
            }else if(corPreto.isSelected()){
                CinemaDesktop.this.jdpPanel.setBackground(Color.black);
                CinemaDesktop.this.jdpPanel.repaint();
            }
        }
    }

    private class LookAndFeelHandler implements ActionListener{
        public void actionPerformed(ActionEvent e){
            try {
                UIManager.setLookAndFeel(e.getActionCommand());
                SwingUtilities.updateComponentTreeUI(CinemaDesktop.this);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

}
