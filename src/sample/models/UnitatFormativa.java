package sample.models;

import java.util.Date;

public class UnitatFormativa {

    private String codiUF;
    private String nomUF;
    private int duradaUF;
    private Date dataIniciUF;
    private int tipusUF;

    public UnitatFormativa(String codiUF, String nomUF, int duradaUF, Date dataIniciUF, int tipusUF) {
        this.codiUF = codiUF;
        this.nomUF = nomUF;
        this.duradaUF = duradaUF;
        this.dataIniciUF = dataIniciUF;
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

    public Date getDataIniciUF() {
        return dataIniciUF;
    }

    public void setDataIniciUF(Date dataIniciUF) {
        this.dataIniciUF = dataIniciUF;
    }

    public int getTipusUF() {
        return tipusUF;
    }

    public void setTipusUF(int tipusUF) {
        this.tipusUF = tipusUF;
    }

    @Override
    public String toString() {
        return "UnitatFormativa{" +
                "codiUF='" + codiUF + '\'' +
                ", nomUF='" + nomUF + '\'' +
                ", duradaUF=" + duradaUF +
                ", dataIniciUF=" + dataIniciUF +
                ", tipusUF=" + tipusUF +
                '}';
    }
}
