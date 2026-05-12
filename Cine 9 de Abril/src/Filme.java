import java.io.Serializable;

public class Filme implements Serializable{
    String titulo;
    String genero;
    int duracao; // em minutos
    int classificacao;
    String descricao;

    public Filme(String titulo, String genero, int duracao, int classificacao, String descricao) {
        this.titulo = titulo;
        this.genero = genero;
        this.duracao = duracao;
        this.setClassificacao(classificacao);;
        this.descricao = descricao;
    }

    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public String getGenero() {
        return genero;
    }
    public void setGenero(String genero) {
        this.genero = genero;
    }
    public int getDuracao() {
        return duracao;
    }
    public void setDuracao(int duracao) {
        this.duracao = duracao;
    }
    public int getClassificacao() {
        return classificacao;
    }
    public void setClassificacao(int classificacao) {
        if(classificacao < 0){
            this.classificacao = 0;
        }else if(classificacao > 18){
            this.classificacao = 18;
        }else
            this.classificacao = classificacao;
    }
    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
