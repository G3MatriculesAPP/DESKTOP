package sample.utils;

import sample.interfaces.impl.CicleImpl;
import sample.interfaces.impl.ModulImpl;
import sample.interfaces.impl.UnitatFormativaImpl;
import sample.models.Modul;
import sample.models.UnitatFormativa;

import java.util.List;

public class TestAPI {

    // TestAPI{}
    // Clase especial para testear sin arrancar la aplicación completamente

    public static void main(String[] args) {

        UnitatFormativaImpl unitatFormativaManager = new UnitatFormativaImpl();
        List<UnitatFormativa> ufList = unitatFormativaManager.getAllUFSFromCicleByModul("604f8b8228db691addd7490f", 8);
        for (UnitatFormativa uf : ufList) {
            System.out.println(uf.getNomUF());
        }

    }

}
