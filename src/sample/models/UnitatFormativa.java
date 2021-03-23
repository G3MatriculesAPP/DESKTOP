package sample.models;

import java.util.Date;

public class UnitatFormativa {

    private String codiUF;
    private String nomUF;
    private int duradaUF;
    private int tipusUF;

    public UnitatFormativa(){}

    public UnitatFormativa(String codiUF, String nomUF, int duradaUF, Date dataIniciUF, int tipusUF) {
        this.codiUF = codiUF;
        this.nomUF = nomUF;
        this.duradaUF = duradaUF;
        this.tipusUF = tipusUF;
    }

    public String getCodiUF() {
        return codiUF;
    }

    public void setCodiUF(String codiUF) {
        this.codiUF = codiUF;
    }

    public String getNomUF() {
        return nomUF;
    }

    public void setNomUF(String nomUF) {
        this.nomUF = nomUF;
    }

    public int getDuradaUF() {
        return duradaUF;
    }

    public void setDuradaUF(int duradaUF) {
        this.duradaUF = duradaUF;
    }

    public int getTipusUF() {
        return tipusUF;
    }

    public void setTipusUF(int tipusUF) {
        this.tipusUF = tipusUF;
    }

    @Override
    public String toString() {
        return nomUF + " [" + duradaUF + "H]";
    }
}
