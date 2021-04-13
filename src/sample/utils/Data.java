package sample.utils;

import sample.interfaces.impl.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class Data {

    public static String API_URL = "https://g3matriculesapp.herokuapp.com";
    public static String LOCALHOST = "http://localhost:5000";
    public static String TOKEN = "";

    public static SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy kk:mm:ss zzz", Locale.US);       // Formato DATES de MongoDB

    public static CicleImpl cicleManager = new CicleImpl();
    public static ModulImpl modulManager = new ModulImpl();
    public static UnitatFormativaImpl ufManager = new UnitatFormativaImpl();
    public static AlumneImpl alumneManager = new AlumneImpl();
    public static PerfilRequerimentImpl reqPerfilsManager = new PerfilRequerimentImpl();

    public static List ciclesList;
    public static List perfilsList;
    public static String[] optionsReqs = new String[]{"IMAGE", "DOCUMENT", "TEXT"};


}
