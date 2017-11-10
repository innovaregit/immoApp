package com.innovare.marceloagustini.immoapp.controllers;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.innovare.marceloagustini.immoapp.R;
import com.innovare.marceloagustini.immoapp.clases.Publicacion;
import com.innovare.marceloagustini.immoapp.utilidades.Global;
import com.innovare.marceloagustini.immoapp.utilidades.TrackGPS;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;


public class NewPubFragment extends Fragment {
    ImageView imagen;
    ImageButton btnCamera;
    Button btnGuardar;
    EditText txtTitulo, txtDescripcion, txtValor, txtDireccion;
    Spinner spinner;
    TrackGPS gps;
    Publicacion pub;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_pub, container, false);
        init(view);
        return view;
    }

    private void init(View v) {
        spinner = (Spinner) v.findViewById(R.id.spinner_tipo);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.tipos_pubs, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        txtTitulo = (EditText) v.findViewById(R.id.txtTitulo);
        txtDescripcion = (EditText) v.findViewById(R.id.txtDescripcion);
        txtValor = (EditText) v.findViewById(R.id.txtValor);
        txtDireccion = (EditText) v.findViewById(R.id.txtDireccion);
        btnGuardar = (Button) v.findViewById(R.id.btnGuardar);

        //INIT OBJECT PUBLICACION
        pub = new Publicacion();

        //BOTON GUARDAR CLICK
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarPub();
            }
        });

        //IMAGEN y BOTONES DE FOTOS
        imagen = (ImageView) v.findViewById(R.id.imagen);
        btnCamera = (ImageButton) v.findViewById(R.id.btnCamera);

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            btnCamera.setEnabled(false);
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
        //IMAGEN BUTTON CLICK
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture();
            }
        });

        //GPS
        initGPS();


    }

    private void guardarPub() {
        if (!txtTitulo.getText().toString().isEmpty() && !txtValor.getText().toString().isEmpty()) {
            pub.setTitulo(txtTitulo.getText().toString());
            pub.setDescripcion(txtDescripcion.getText().toString());
            pub.setValor(Double.parseDouble(txtValor.getText().toString()));
            pub.setDireccion(txtDireccion.getText().toString());
            Global.publicaciones.add(pub);
            Toast.makeText(this.getContext(), "PUBLICACION COMPLETA", Toast.LENGTH_LONG);
            //Redirect
            PubsFragment pubs = new PubsFragment();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_main, pubs).commit();
        } else {
            Toast.makeText(getContext(), "Faltan datos", Toast.LENGTH_LONG).show();
        }
    }

    //CAMARA
    //*****************

    Uri file;

    // 2-Tomar Foto
    public void takePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = Uri.fromFile(getOutputMediaFile());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, file);

        startActivityForResult(intent, 100);
    }

    // 3- Resultado de la foto
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                imagen.setImageURI(file);
                pub.getImagenes().add("path_upload");
            }
        }
    }

    // 1 - Almacenamiento temporal
    private static File getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "CameraDemo");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_" + timeStamp + ".jpg");
    }

    //***********


    // GPS ***************

    private void initGPS() {
        gps = new TrackGPS(this.getContext());

        if (gps.canGetLocation()) {
            Double longitude = gps.getLongitude();
            Double latitude = gps.getLatitude();
            Toast.makeText(getContext(), "Longitude:" + Double.toString(longitude) + "\nLatitude:" + Double.toString(latitude), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "NO GPS", Toast.LENGTH_SHORT).show();
        }

    }

}
