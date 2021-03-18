package sample.models;

import java.util.Date;

public class Modul {

    private String codiModul;
    private String nomModul;
    private int duradaModul;
    private Date dataIniciModul;

    public Modul(String codiModul, String nomModul, int duradaModul, Date dataIniciModul) {
        this.codiModul = codiModul;
        this.nomModul = nomModul;
        this.duradaModul = duradaModul;
        this.dataIniciModul = dataIniciModul;
    }

    public String getCodiModul() {
        return codiModul;
    }

    public void setCodiModul(String codiModul) {
        this.codiModul = codiModul;
    }

    public String getNomModul() {
        return nomModul;
    }

    public void setNomModul(String nomModul) {
        this.nomModul = nomModul;
    }

    public int getDuradaModul() {
        return duradaModul;
    }

    public void setDuradaModul(int duradaModul) {
        this.duradaModul = duradaModul;
    }

    public Date getDataIniciModul() {
        return dataIniciModul;
    }

    public void setDataIniciModul(Date dataIniciModul) {
        this.dataIniciModul = dataIniciModul;
    }

    @Override
    public String toString() {
        return codiModul + " "+ nomModul ;
    }
}
