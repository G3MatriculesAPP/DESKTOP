package sample.models;

import java.util.Date;

public class Cicle {

    private String idCicle;
    private String codiCicle;
    private String nomCicle;
    private int horesCicle;
    private Date dataIniciCicle;
    private String codiAdaptacioCur;

    public Cicle(){}

    public Cicle(String idCicle, String codiCicle, String nomCicle, int horesCicle, Date dataIniciCicle, String codiAdaptacioCur) {
        this.idCicle = idCicle;
        this.codiCicle = codiCicle;
        this.nomCicle = nomCicle;
        this.horesCicle = horesCicle;
        this.dataIniciCicle = dataIniciCicle;
        this.codiAdaptacioCur = codiAdaptacioCur;
    }

    public String getIdCicle() {
        return idCicle;
    }

    public void setIdCicle(String idCicle) {
        this.idCicle = idCicle;
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

    public String getCodiAdaptacioCur() {
        return codiAdaptacioCur;
    }

    public void setCodiAdaptacioCur(String codiAdaptacioCur) {
        this.codiAdaptacioCur = codiAdaptacioCur;
    }

    @Override
    public String toString() {
        return nomCicle;
    }
    
}
