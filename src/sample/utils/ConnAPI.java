package sample.utils;

import org.json.JSONObject;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Scanner;

public class ConnAPI {

    private static HttpURLConnection conn;

    public ConnAPI(String endpoint, String method, boolean isTest){

        // ConnAPI()
        // Crea una nueva conexion con la API, recibe la ruta al endpoint y el metodo con el
        // que se hara la llamada (GET, POST, ETC...). Dependiendo del valor de "isTest" conectará con
        // HEROKU o con LOCALHOST

        try{
            URL url = null;
            if (!isTest) {url = new URL(Data.API_URL + endpoint);}
            else        {url = new URL(Data.LOCALHOST + endpoint);}

            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void establishConn(){

        // establishConn()
        // Para conectar con la API, debe ponerse siempre al final a la hora de ejecutar la conexion
        // en alguna parte del programa.

        try {
            conn.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setData(JSONObject jsonObject){

        // setData()
        // Método para pasar la información a la API, este recibe un JSONObject ya hecho y se encarga de enviarselo
        // a la API para que pueda leerlo.

        conn.setRequestProperty("Content-Type","application/json");
        conn.setDoOutput(true);

        try {
            String dataJSON = jsonObject.toString();
            byte[] outputInBytes = dataJSON.getBytes("UTF-8");
            OutputStream os = conn.getOutputStream();
            os.write( outputInBytes );
            os.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public JSONObject getDataJSON(){

        // getDataJSON()
        // Obtiene la informacion obtenida por la API en formato JSON

        String rawData = "";

        try {
            Scanner sc = new Scanner(conn.getInputStream());
            while (sc.hasNext()){
                rawData += sc.nextLine();
            }

            JSONObject jsonObject = new JSONObject(rawData);
            return jsonObject;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    public int getStatus(){

        // getStatus()
        // Devuevle el STATUS de la conexion con la API

        try {
            return conn.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void closeConn(){
        conn.disconnect();
    }

}
