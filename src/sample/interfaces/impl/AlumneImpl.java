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
}
