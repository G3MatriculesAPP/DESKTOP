package sample.interfaces.impl;

import org.json.JSONArray;
import org.json.JSONObject;
import sample.interfaces.IUnitatFormativa;
import sample.models.UnitatFormativa;
import sample.utils.ConnAPI;

import java.util.ArrayList;

public class UnitatFormativaImpl implements IUnitatFormativa {

    @Override
    public ArrayList<UnitatFormativa> getAllUFSFromCicleByModul(String idCicle, int posModul) {

        // getAllUFSFromCicleByModul()
        // Esté metodo llama a la API a través de ConnAPI pasandole la ID del CICLO y la posición del
        // MODULO que queramos obtener, una vez llamada a la API se obtienen los datos y se transforman en un
        // objeto UnitatFormativa, finalmente se añade al ArrayList y se devuelve.

        ArrayList<UnitatFormativa> arrayUFS = new ArrayList<>();

        JSONObject requestJSON = new JSONObject();
        requestJSON.put("id", idCicle);
        requestJSON.put("pos", posModul);

        ConnAPI connAPI = new ConnAPI("/api/ufs/readAll", "POST", false);
        connAPI.setData(requestJSON);
        connAPI.establishConn();

        JSONObject responseJSON = connAPI.getDataJSON();
        JSONArray arrayJSON = responseJSON.getJSONArray("data");
        for (int i = 0; i < arrayJSON.length(); i++){
            JSONObject rawJSON = arrayJSON.getJSONObject(i);
            UnitatFormativa uf = new UnitatFormativa();
            uf.setCodiUF(rawJSON.getString("codiUnitatFormativa"));
            uf.setNomUF(rawJSON.getString("nomUnitatFormativa"));
            uf.setDuradaUF(rawJSON.getInt("duradaUnitatFormativa"));
            uf.setTipusUF(setTipusUF(rawJSON));
            arrayUFS.add(uf);
        }

        return arrayUFS;
    }


    @Override
    public String getTipusUF(int tipusUF) {

        // getTipusUF()
        // Método auxiliar para obtner en formato String el tipo de UF

        switch (tipusUF){
            case 0:
                return "esNormal";
            case 1:
                return "esProjecte";
            case 2:
                return "esIdioma";
            case 3:
                return "esSintesis";
            case 4:
                return "esFCT";
        }

        return null;
    }

    @Override
    public int setTipusUF(JSONObject rawData) {

        // setTipusUF()
        // Si alguno de los booleanos de la UF es true, se devuelve su clave valor en int.

        if (rawData.getBoolean("esProjecte"))
            return 1;

        if (rawData.getBoolean("esIdioma"))
            return 2;

        if (rawData.getBoolean("esSintesis"))
            return 3;

        if (rawData.getBoolean("esFCT"))
            return 4;

        return 0;
    }
}
