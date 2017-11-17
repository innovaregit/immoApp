package com.innovare.marceloagustini.immoapp.redes;

import android.util.Log;

import com.innovare.marceloagustini.immoapp.clases.Publicacion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by marceloagustini on 17/11/17.
 */

public class JsonToObject {
    public static ArrayList<Publicacion> obtenerPublicaciones(String jsonString) {
        ArrayList<Publicacion> lista = new ArrayList<>();
        try {
            JSONObject jsnobject = new JSONObject(jsonString);
            JSONArray jsonArray = jsnobject.getJSONArray("data");

            for (int i = 0; i < jsonArray.length(); i++) {
                Publicacion pub = new Publicacion();
                JSONObject jsonPub = jsonArray.getJSONObject(i);
                pub.setTitulo(jsonPub.getString("titulo"));
                pub.setDescripcion(jsonPub.getString("descripcion"));
                pub.setValor(jsonPub.getDouble("valor"));
                //... POR CADA ATRIBUTO U OBJETO HACER LO MISMO.
                lista.add(pub);
            }
            return lista;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return lista;
    }
}
