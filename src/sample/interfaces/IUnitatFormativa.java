package sample.interfaces;

import org.json.JSONObject;
import sample.models.UnitatFormativa;

import java.util.ArrayList;

public interface IUnitatFormativa {

    public ArrayList<UnitatFormativa> getAllUFSFromCicleByModul(String idCicle, int posModul);

    public String getTipusUF(int tipusUF);

    public int setTipusUF(JSONObject rawData);

}
