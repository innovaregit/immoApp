package com.innovare.marceloagustini.immoapp.controllers;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.innovare.marceloagustini.immoapp.R;
import com.innovare.marceloagustini.immoapp.adapters.PublicacionAdapter;
import com.innovare.marceloagustini.immoapp.clases.Publicacion;
import com.innovare.marceloagustini.immoapp.redes.JsonToObject;
import com.innovare.marceloagustini.immoapp.redes.PublicacionesAsyncTask;
import com.innovare.marceloagustini.immoapp.utilidades.Global;
import com.innovare.marceloagustini.immoapp.utilidades.HardcodePubs;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


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
        fillListaNativo(vista);
        return vista;
    }

    private void fillLista(View v) {
        PublicacionAdapter adapter = new PublicacionAdapter(this.getActivity(), Global.publicaciones);
        ListView listView = (ListView) v.findViewById(R.id.lista_publicaciones);
        listView.setAdapter(adapter);
    }

    private void fillListaNativo(View v) {

        try {
            //Leemos al servidor
            String json =
                    new PublicacionesAsyncTask().execute(Global.restUrl + "/publicacion").get();
            Log.e("OK", json); // Miramos en consola

            //Convertimos a Objetos
            ArrayList<Publicacion> lista = JsonToObject.obtenerPublicaciones(json);
            //Seguimos
            PublicacionAdapter adapter = new PublicacionAdapter(this.getActivity(),lista);
            ListView listView = (ListView) v.findViewById(R.id.lista_publicaciones);
            listView.setAdapter(adapter);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }
}
