package com.innovare.marceloagustini.immoapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.innovare.marceloagustini.immoapp.R;
import com.innovare.marceloagustini.immoapp.clases.Publicacion;

import java.util.ArrayList;

/**
 * Created by marceloagustini on 2/11/17.
 */

public class PublicacionAdapter extends ArrayAdapter<Publicacion> {

    public PublicacionAdapter(Context context, ArrayList<Publicacion> publicaciones) {
        super(context, 0, publicaciones);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Publicacion publicacion = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_publicacion, parent, false);
        }

        Log.e("","OKOKOK");
        TextView titulo = (TextView) convertView.findViewById(R.id.titulo);
        TextView descripcion = (TextView) convertView.findViewById(R.id.descripcion);

        titulo.setText(publicacion.getTitulo());
        descripcion.setText(publicacion.getDescripcion());
        return convertView;
    }

}
