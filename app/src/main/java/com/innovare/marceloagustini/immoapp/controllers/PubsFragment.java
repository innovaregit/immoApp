package com.innovare.marceloagustini.immoapp.controllers;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.innovare.marceloagustini.immoapp.R;
import com.innovare.marceloagustini.immoapp.adapters.PublicacionAdapter;
import com.innovare.marceloagustini.immoapp.clases.Publicacion;
import com.innovare.marceloagustini.immoapp.redes.JsonToObject;
import com.innovare.marceloagustini.immoapp.redes.PublicacionesAsyncTask;
import com.innovare.marceloagustini.immoapp.utilidades.Global;
import com.innovare.marceloagustini.immoapp.utilidades.HardcodePubs;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import cz.msebera.android.httpclient.entity.mime.Header;


/**
 * A simple {@link Fragment} subclass.
 */
public class PubsFragment extends Fragment {

    public PubsFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_pubs, container, false);
        fillListaRemotaConLibrerias(vista);
        return vista;
    }

    private void fillLista(View v) {
        PublicacionAdapter adapter = new PublicacionAdapter(this.getActivity(), Global.publicaciones);
        ListView listView = (ListView) v.findViewById(R.id.lista_publicaciones);
        listView.setAdapter(adapter);
    }

    //CON ASYNC TASK y JSON OBJECTS
    private void fillListaRemotaBasica(View v) {
        try {
            //Leemos al servidor
            String json =
                    new PublicacionesAsyncTask().execute(Global.restUrl + "/publicacion" + Global.usuario.get_id()).get();
            Log.e("OK", json); // Miramos en consola

            //Convertimos a Objetos
            ArrayList<Publicacion> lista = JsonToObject.obtenerPublicaciones(json);
            //Seguimos
            PublicacionAdapter adapter = new PublicacionAdapter(this.getActivity(), lista);
            ListView listView = (ListView) v.findViewById(R.id.lista_publicaciones);
            listView.setAdapter(adapter);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    //CON LIBRERIAS GSON Y ASYNC-HTTP
    private void fillListaRemotaConLibrerias(final View v) {
        final Fragment fragment = this;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Global.restUrl + "/publicacion/598dc685fbca93613846a598", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                Gson gson = new Gson();
                Log.e("_ID",Global.usuario.get_id());
                try {
                    ArrayList<Publicacion> lista = gson.fromJson(response.getJSONArray("data").toString(),
                            new TypeToken<ArrayList<Publicacion>>() {
                            }.getType());
                    //Adaptador
                    PublicacionAdapter adapter = new PublicacionAdapter(fragment.getActivity(), lista);
                    ListView listView = (ListView) v.findViewById(R.id.lista_publicaciones);
                    listView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int x, cz.msebera.android.httpclient.Header[] header, Throwable t, JSONObject object) {
                //ERRORES AQUI
            }
        });
    }
}
