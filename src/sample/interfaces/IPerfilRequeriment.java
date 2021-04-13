package sample.interfaces;

import org.json.JSONArray;
import org.json.JSONObject;
import sample.interfaces.impl.PerfilRequerimentImpl;
import sample.models.PerfilRequeriment;

import java.util.ArrayList;

public interface IPerfilRequeriment {

    public ArrayList<PerfilRequeriment> getAllPerfils();

    public PerfilRequeriment getPerfilRequeriments(PerfilRequeriment perfilRequeriment);

    public boolean createPerfil(JSONObject dataJSON);

}
