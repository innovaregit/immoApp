package com.innovare.marceloagustini.immoapp.adapters;

import android.content.Context;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.innovare.marceloagustini.immoapp.R;
import com.innovare.marceloagustini.immoapp.clases.Publicacion;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marceloagustini on 2/11/17.
 */

public class PublicacionAdapter extends ArrayAdapter<Publicacion> {

    Context context;
    List<Publicacion> publicaciones;

    public PublicacionAdapter(Context context, ArrayList<Publicacion> publicaciones) {
        super(context, 0, publicaciones);
        this.context = context;
        this.publicaciones = publicaciones;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Publicacion publicacion = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_publicacion, parent, false);
        }

        TextView titulo = (TextView) convertView.findViewById(R.id.titulo);
        TextView descripcion = (TextView) convertView.findViewById(R.id.descripcion);
        ImageView imagen = (ImageView) convertView.findViewById(R.id.imagen);

        titulo.setText(publicacion.getTitulo());
        descripcion.setText(publicacion.getDescripcion());


        if (publicacion.getImagenes() != null && publicacion.getImagenes().size() > 0) {
            Picasso.with(context).load("http://i-mmo.net/uploads/" +
                    publicacion.get_id() + "/" + publicacion.getImagenes().get(0)).into(imagen);
        }
        if (publicacion.getDescripcion().isEmpty()) {
            descripcion.setText("No tiene ninguna descripcion en esta propiedad.");

        }
        return convertView;
    }

}
