package sample.models;

import javafx.scene.Node;

import java.util.Date;

public class Modul extends Node {

    private String codiModul;
    private String nomModul;
    private int duradaMinModul;
    private int duradaMaxModul;
    private Date dataFiModul;
    private Date dataIniciModul;

    public Modul(){}

    public Modul(String codiModul, String nomModul, int duradaMinModul, int duradaMaxModul, Date dataFiModul, Date dataIniciModul) {
        this.codiModul = codiModul;
        this.nomModul = nomModul;
        this.duradaMinModul = duradaMinModul;
        this.duradaMaxModul = duradaMaxModul;
        this.dataFiModul = dataFiModul;
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

    public int getDuradaMinModul() {
        return duradaMinModul;
    }

    public void setDuradaMinModul(int duradaMinModul) {
        this.duradaMinModul = duradaMinModul;
    }

    public int getDuradaMaxModul() {
        return duradaMaxModul;
    }

    public void setDuradaMaxModul(int duradaMaxModul) {
        this.duradaMaxModul = duradaMaxModul;
    }

    public Date getDataFiModul() {
        return dataFiModul;
    }

    public void setDataFiModul(Date dataFiModul) {
        this.dataFiModul = dataFiModul;
    }

    public Date getDataIniciModul() {
        return dataIniciModul;
    }

    public void setDataIniciModul(Date dataIniciModul) {
        this.dataIniciModul = dataIniciModul;
    }

    @Override
    public String toString() {
        return nomModul;
    }
}
