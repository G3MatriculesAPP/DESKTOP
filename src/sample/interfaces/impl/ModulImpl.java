package sample.interfaces.impl;

import org.json.JSONArray;
import org.json.JSONObject;
import sample.interfaces.IModul;
import sample.models.Modul;
import sample.utils.ConnAPI;
import sample.utils.Data;
import java.util.ArrayList;
import java.util.Date;

public class ModulImpl implements IModul {

    @Override
    public ArrayList<Modul> getAllModulsByCicle(String idCicle) {

        // getAllModulsByCicle()
        // Dependiendo de la ObjectID del CICLE, obtendrá todos los MODULOS y toda su información excepto las UFs,
        // la añade a un objeto Modul y lo guarda en un ArrayList para luego devolverlo

        ArrayList<Modul> arrayModul = new ArrayList<>();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", idCicle);

        ConnAPI connAPI = new ConnAPI("/api/moduls/readAll", "POST", false);
        connAPI.setData(jsonObject);
        connAPI.establishConn();

        JSONObject responseJSON = connAPI.getDataJSON();
        JSONArray arrayJSON = responseJSON.getJSONArray("data");
        for (int i = 0; i < arrayJSON.length(); i++){
            try {
                JSONObject rawJSON = arrayJSON.getJSONObject(i);
                Modul m = new Modul();
                m.setDuradaMinModul(rawJSON.getInt("duradaMinModul"));
                m.setDuradaMaxModul(rawJSON.getInt("duradaMaxModul"));
                m.setNomModul(rawJSON.getString("nomModul"));
                m.setCodiModul(rawJSON.getString("codiModul"));
                Date date = null;

                if (!rawJSON.isNull("dataIniciModul")){
                    JSONObject dateJSON = rawJSON.getJSONObject("dataIniciModul");
                    date = Data.format.parse(dateJSON.getString("date"));
                    m.setDataIniciModul(date);
                }

                if (!rawJSON.isNull("dataFiModul")) {
                    JSONObject dateJSON = rawJSON.getJSONObject("dataFiModul");
                    date = Data.format.parse(dateJSON.getString("date"));
                    m.setDataFiModul(date);
                }

                arrayModul.add(m);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        connAPI.closeConn();

        return arrayModul;
    }
}
