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

        ConnAPI connAPI = new ConnAPI("/api/alumnes/readOne", "POST", false);
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

        JSONObject dirJSON = alumneData.getJSONObject("direccio");
        if (dirJSON.isNull("localitat"))
            dirJSON.put("localitat", "");

        JSONObject centreProcedenciaJSON = alumneData.getJSONObject("centreProcedencia");
        if (centreProcedenciaJSON.isNull("curs"))
            centreProcedenciaJSON.put("curs", "");

        alumneData.put("centreProcedencia", centreProcedenciaJSON);
        alumneData.put("direccio", dirJSON);
        alumneData.put("centreProcedencia", cpJSON);



        if (!alumneData.isNull("dni"))
            alumneData.put("tipusDocument", 0);
        else if(!alumneData.isNull("nie"))
            alumneData.put("tipusDocument", 1);

        return alumneData;
    }

    @Override
    public boolean deleteAlumne(String idAlumne) {

        JSONObject requestJSON = new JSONObject();
        requestJSON.put("id", idAlumne);

        ConnAPI connAPI = new ConnAPI("/api/alumnes/deleteOne", "DELETE", false);
        connAPI.setData(requestJSON);
        connAPI.establishConn();

        int status = connAPI.getStatus();
        return status == 200;
    }

    @Override
    public boolean updateAlumne(JSONObject alumneJSON) {

        JSONObject requestJSON = new JSONObject();
        requestJSON.put("data", alumneJSON);

        ConnAPI connAPI = new ConnAPI("/api/alumnes/updateOne", "PUT", false);
        connAPI.setData(requestJSON);
        connAPI.establishConn();

        int status = connAPI.getStatus();
        return status == 200;
    }
}
