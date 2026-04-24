
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Sessao {
    String filme;
    String sala;
    Date dataInicio;
    Date horaInicio;

    public Sessao(String filme, String sala, String dataInicio, String horaInicio) {
        this.filme = filme;
        this.sala = sala;
        this.setDataInicio(dataInicio);
        this.setHoraInicio(horaInicio);
    }
    
    public String getFilme() {
        return filme;
    }
    public void setFilme(String filme) {
        this.filme = filme;
    }
    public String getSala() {
        return sala;
    }
    public void setSala(String sala) {
        this.sala = sala;
    }
    public Date getDataInicio() {
        return dataInicio;
    }
    public void setDataInicio(String dataInicio){
        try {
            SimpleDateFormat sdfData = new SimpleDateFormat("dd/MM/yyyy");
            this.dataInicio = sdfData.parse(dataInicio);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
    }
    public Date getHoraInicio() {
        return horaInicio;
    }
    public void setHoraInicio(String horaInicio){
        try {
            SimpleDateFormat sdfHora = new SimpleDateFormat("HH:mm");
            this.horaInicio = sdfHora.parse(horaInicio);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

}
