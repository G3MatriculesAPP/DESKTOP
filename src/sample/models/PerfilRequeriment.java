package sample.models;

import org.json.JSONObject;

public class PerfilRequeriment {

    private String idPerfil;
    private String nomPerfil;
    private String descrPerfil;
    private JSONObject requisits;

    public PerfilRequeriment(){}

    public PerfilRequeriment(String idPerfil, String nomPerfil, String descrPerfil, JSONObject requisits) {
        this.idPerfil = idPerfil;
        this.nomPerfil = nomPerfil;
        this.descrPerfil = descrPerfil;
        this.requisits = requisits;
    }

    public String getIdPerfil() {
        return idPerfil;
    }

    public void setIdPerfil(String idPerfil) {
        this.idPerfil = idPerfil;
    }

    public String getNomPerfil() {
        return nomPerfil;
    }

    public void setNomPerfil(String nomPerfil) {
        this.nomPerfil = nomPerfil;
    }

    public String getDescrPerfil() {
        return descrPerfil;
    }

    public void setDescrPerfil(String descrPerfil) {
        this.descrPerfil = descrPerfil;
    }

    public JSONObject getRequisits() {
        return requisits;
    }

    public void setRequisits(JSONObject requisits) {
        this.requisits = requisits;
    }

    @Override
    public String toString() {
        return nomPerfil;
    }
}
