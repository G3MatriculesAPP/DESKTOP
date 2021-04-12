package sample.interfaces.impl;

import javafx.scene.control.Alert;
import org.json.JSONArray;
import org.json.JSONObject;
import sample.interfaces.IPerfilRequeriment;
import sample.models.PerfilRequeriment;
import sample.utils.ConnAPI;
import sample.utils.Data;

import java.util.ArrayList;

public class PerfilRequerimentImpl implements IPerfilRequeriment {

    @Override
    public ArrayList<PerfilRequeriment> getAllPerfils() {

        ArrayList<PerfilRequeriment> arrayPerfils = new ArrayList<>();
        ConnAPI connAPI = new ConnAPI("/api/reqPerfils/readAll", "GET", false);
        connAPI.establishConn();

        JSONObject responseJSON = connAPI.getDataJSON();
        JSONArray arrayJSON = responseJSON.getJSONArray("data");
        for (int i = 0; i < arrayJSON.length(); i++){
            JSONObject rawJSON = arrayJSON.getJSONObject(i);
            PerfilRequeriment perfil = new PerfilRequeriment();
            perfil.setIdPerfil(rawJSON.getString("_id"));
            perfil.setNomPerfil(rawJSON.getString("nom"));
            perfil.setDescrPerfil(rawJSON.getString("descripcio"));
            arrayPerfils.add(perfil);
        }
        return arrayPerfils;
    }

    @Override
    public PerfilRequeriment getPerfilRequeriments(PerfilRequeriment perfilRequeriment) {

        JSONObject requestJSON = new JSONObject();
        requestJSON.put("id", perfilRequeriment.getIdPerfil());

        ConnAPI connAPI = new ConnAPI("/api/reqPerfils/readOne", "POST", false);
        connAPI.setData(requestJSON);
        connAPI.establishConn();

        JSONObject responseJSON = connAPI.getDataJSON();
        JSONArray arrayJSON = responseJSON.getJSONArray("data");
        for (int i = 0; i < arrayJSON.length(); i++){
            perfilRequeriment.setRequisits(arrayJSON);
        }

        return perfilRequeriment;
    }

    @Override
    public boolean createPerfil(JSONObject dataJSON) {

        JSONObject requestJSON = new JSONObject();
        requestJSON.put("data", dataJSON.toString());
        
        ConnAPI connAPI = new ConnAPI("/api/reqPerfils/insertOne", "POST", false);
        connAPI.setData(requestJSON);
        connAPI.establishConn();

        int status = connAPI.getStatus();
        switch (status){
            case 0:
                System.out.println("[DEBUG] - Imposible obtener STATUS...");
                return false;

            case 200:
                System.out.println("[DEBUG] - Perfil añadido correctamente!");
                return true;

            case 500:
                System.out.println("[DEBUG] - Error al añadir el perfil...");
                return false;
        }

        return false;
    }
}
