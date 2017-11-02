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
import com.innovare.marceloagustini.immoapp.utilidades.HardcodePubs;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class PubsFragment extends Fragment {
    ArrayList<Publicacion> publicaciones;


    public PubsFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_pubs, container, false);
        fillLista(vista);
        return vista;
    }

    private void fillLista(View v) {
        // Construct the data source
        this.publicaciones = HardcodePubs.fillPubs();
        PublicacionAdapter adapter = new PublicacionAdapter(this.getActivity(), this.publicaciones);
        ListView listView = (ListView) v.findViewById(R.id.lista_publicaciones);
        listView.setAdapter(adapter);
    }
}
