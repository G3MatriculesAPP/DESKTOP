package sample.interfaces;

import org.json.JSONArray;
import org.json.JSONObject;

public interface IAlumne {

    public JSONArray getAlumnesByCourse(String cicleName);

    public JSONObject getAlumneData(String idAlumne);

}
