package sample.utils;

import sample.interfaces.impl.CicleImpl;
import sample.interfaces.impl.ModulImpl;
import sample.interfaces.impl.UnitatFormativaImpl;
import sample.models.Modul;
import sample.models.UnitatFormativa;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TestAPI {

    // TestAPI{}
    // Clase especial para testear sin arrancar la aplicación completamente

    public static void main(String[] args) throws ParseException {

        SimpleDateFormat formatter = new SimpleDateFormat("d MMM yyyy kk:mm:ss zzz",Locale.US);
        Date response = formatter.parse("8 Apr 2013 00:00:00 GMT");
        System.out.println(response);

    }

}
