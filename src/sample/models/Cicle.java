package sample.models;

import java.util.Date;

public class Cicle {

    private String codiCicle;
    private String nomCicle;
    private int horesCicle;
    private Date dataIniciCicle;
    private int codiAdaptacioCur;

    public Cicle(String codiCicle, String nomCicle, int horesCicle, Date dataIniciCicle, int codiAdaptacioCur) {
        this.codiCicle = codiCicle;
        this.nomCicle = nomCicle;
        this.horesCicle = horesCicle;
        this.dataIniciCicle = dataIniciCicle;
        this.codiAdaptacioCur = codiAdaptacioCur;
    }

    public String getCodiCicle() {
        return codiCicle;
    }

    public void setCodiCicle(String codiCicle) {
        this.codiCicle = codiCicle;
    }

    public String getNomCicle() {
        return nomCicle;
    }

    public void setNomCicle(String nomCicle) {
        this.nomCicle = nomCicle;
    }

    public int getHoresCicle() {
        return horesCicle;
    }

    public void setHoresCicle(int horesCicle) {
        this.horesCicle = horesCicle;
    }

    public Date getDataIniciCicle() {
        return dataIniciCicle;
    }

    public void setDataIniciCicle(Date dataIniciCicle) {
        this.dataIniciCicle = dataIniciCicle;
    }

    public int getCodiAdaptacioCur() {
        return codiAdaptacioCur;
    }

    public void setCodiAdaptacioCur(int codiAdaptacioCur) {
        this.codiAdaptacioCur = codiAdaptacioCur;
    }

    @Override
    public String toString() {
        return "Cicle{" +
                "codiCicle='" + codiCicle + '\'' +
                ", nomCicle='" + nomCicle + '\'' +
                ", horesCicle=" + horesCicle +
                ", dataIniciCicle=" + dataIniciCicle +
                ", codiAdaptacioCur=" + codiAdaptacioCur +
                '}';
    }
}
