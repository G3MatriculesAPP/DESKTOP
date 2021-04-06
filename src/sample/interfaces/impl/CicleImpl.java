package sample.interfaces.impl;

import org.json.JSONArray;
import org.json.JSONObject;
import sample.interfaces.ICicle;
import sample.models.Cicle;
import sample.utils.ConnAPI;
import sample.utils.Data;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

public class CicleImpl implements ICicle {
	/**
	 * getAllCicles()
         Llama a la API mediante ConnAPI y esta le devuelve un JSON bruto de todos los CICLOS con los datos
         necesarios para crear los objetos, esta devuelve los datos SIN sus modulos ni UFs. Una vez obtenidos los datos
         parsea la información y crea un nuevo objeto Cicle por cada objeto en JSON, los añade a un ArrayList y lo devuelve
	 */
    @Override
    public ArrayList<Cicle> getAllCicles() {

         

        ArrayList<Cicle> arrayCicles = new ArrayList<>();

        ConnAPI connAPI = new ConnAPI("/api/cicles/readAll", "GET", false);
        connAPI.establishConn();

        JSONObject responseJSON = connAPI.getDataJSON();
        JSONArray arrayJSON = responseJSON.getJSONArray("data");
        for (int i = 0; i < arrayJSON.length(); i++){
            try {
                JSONObject rawJSON = arrayJSON.getJSONObject(i);
                Cicle c = new Cicle();
                c.setIdCicle(rawJSON.getString("_id"));
                c.setCodiCicle(rawJSON.getString("codi"));
                c.setNomCicle(rawJSON.getString("nom"));
                c.setHoresCicle(rawJSON.getInt("hores"));
                c.setCodiAdaptacioCur(rawJSON.getString("codiAdaptacioCurricular"));
                if (!rawJSON.isNull("dataInici")){
                    JSONObject dateJSON = rawJSON.getJSONObject("dataInici");
                    Date date = Data.format.parse(dateJSON.getString("date"));
                    c.setDataIniciCicle(date);
                }
                arrayCicles.add(c);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        connAPI.closeConn();

        return arrayCicles;
    }
}
