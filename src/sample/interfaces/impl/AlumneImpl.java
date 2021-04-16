package sample.interfaces.impl;

import org.json.JSONArray;
import org.json.JSONObject;
import sample.interfaces.IAlumne;
import sample.utils.ConnAPI;

public class AlumneImpl implements IAlumne {

    @Override
    public JSONArray getAlumnesByCourse(String cicleName) {

        JSONArray arrayAlumnes = null;

        JSONObject requestJSON = new JSONObject();
        requestJSON.put("cicle", cicleName);

        ConnAPI connAPI = new ConnAPI("/api/alumnes/readAllByCicle", "POST", false);
        connAPI.setData(requestJSON);
        connAPI.establishConn();

        JSONObject responseJSON = connAPI.getDataJSON();
        arrayAlumnes = responseJSON.getJSONArray("data");
        return arrayAlumnes;
    }

    @Override
    public JSONObject getAlumneData(String idAlumne) {

        JSONObject requestJSON = new JSONObject();
        requestJSON.put("id", idAlumne);

        ConnAPI connAPI = new ConnAPI("/api/alumnes/readOne", "POST", true);
        connAPI.setData(requestJSON);
        connAPI.establishConn();

        JSONObject responseJSON = connAPI.getDataJSON();
        JSONArray arrayJSON = responseJSON.getJSONArray("data");
        JSONObject alumneData = arrayJSON.getJSONObject(0);

        if (alumneData.isNull("telefon"))
            alumneData.put("telefon", "");

        if (alumneData.isNull("email"))
            alumneData.put("email", "");

        if (alumneData.isNull("codiCentreAssignat"))
            alumneData.put("codiCentreAssignat", "");

        JSONObject cpJSON = alumneData.getJSONObject("centreProcedencia");
        if (cpJSON.isNull("curs"))
            cpJSON.put("curs", "");
        if (cpJSON.isNull("llengua"))
            cpJSON.put("curs", "");

        alumneData.put("centreProcedencia", cpJSON);

        if (!alumneData.isNull("dni"))
            alumneData.put("tipusDocument", 0);
        else if(!alumneData.isNull("nie"))
            alumneData.put("tipusDocument", 1);

        return alumneData;
    }
}
