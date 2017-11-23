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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.innovare.marceloagustini.immoapp.R;
import com.innovare.marceloagustini.immoapp.adapters.PublicacionAdapter;
import com.innovare.marceloagustini.immoapp.clases.Publicacion;
import com.innovare.marceloagustini.immoapp.utilidades.Global;
import com.innovare.marceloagustini.immoapp.utilidades.TrackGPS;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.entity.mime.Header;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;


public class NewPubFragment extends Fragment {
    ImageView imagen;
    ImageButton btnCamera;
    Button btnGuardar;
    EditText txtTitulo, txtDescripcion, txtValor, txtDireccion;
    Spinner spinner;
    TrackGPS gps;
    Publicacion pub; //VARIABLE A NIVEL CLASE QUE VA ARMANDO LA PUBLICACION HASTA SU ENVIO AL SERVIDOR DE IMMO.

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_pub, container, false);
        init(view);
        return view;
    }

    private void init(View v) {
        //Spinner
        spinner = (Spinner) v.findViewById(R.id.spinner_tipo);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.tipos_pubs, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        //
        txtTitulo = (EditText) v.findViewById(R.id.txtTitulo);
        txtDescripcion = (EditText) v.findViewById(R.id.txtDescripcion);
        txtValor = (EditText) v.findViewById(R.id.txtValor);
        txtDireccion = (EditText) v.findViewById(R.id.txtDireccion);
        btnGuardar = (Button) v.findViewById(R.id.btnGuardar);

        //INIT OBJECT PUBLICACION
        pub = new Publicacion();
        pub.setUsuario(Global.usuario.get_id()); //Agregamos la referencia

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
            pub.setTipo(spinner.getSelectedItem().toString());

            enviarPublicacion();
        } else {
            Toast.makeText(getContext(), "Faltan datos", Toast.LENGTH_LONG).show();
        }
    }

    //CAMARA
    //*****************

    Uri file;

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

                //Obtenemos nombre archivo
                String nombre = file.getPath().substring(file.getPath().lastIndexOf('/') + 1);

                Log.e(nombre, "FILE");
                //Agregamos al Array. Siempre una sola imagen en este caso.
                pub.setImagenes(new ArrayList<String>());
                pub.getImagenes().add(nombre);
            }
        }
    }


    //***********


    // GPS ***************

    private void initGPS() {
        gps = new TrackGPS(this.getContext());

        if (gps.canGetLocation()) {
            Double longitude = gps.getLongitude();
            Double latitude = gps.getLatitude();
            //
            this.pub.setLat(latitude);
            this.pub.setLng(longitude);
            //
            Toast.makeText(getContext(), "Longitude:" + Double.toString(longitude) + "\nLatitude:" + Double.toString(latitude), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "NO GPS", Toast.LENGTH_SHORT).show();
        }

    }

    //
    private void enviarPublicacion() {
        final Fragment fragment = this;
        Gson gson = new Gson();
        StringEntity entity = new StringEntity(gson.toJson(this.pub), "UTF-8");
        //---
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(this.getContext(), Global.restUrl + "/publicacion", entity, "application/json", new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                Log.e("OK", "OK");
                //SUBIMOS IMAGEN?
                if (pub.getImagenes() != null && pub.getImagenes().size() > 0 && file != null) {
                    try {
                        Log.e("_ID_PUB", response.getString("id"));
                        subirImagen(response.getString("id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                Toast.makeText(fragment.getContext(), "PUBLICACION COMPLETA", Toast.LENGTH_LONG);
                //Redirect
                PubsFragment pubs = new PubsFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_main, pubs).commit();

            }

            @Override
            public void onFailure(int x, cz.msebera.android.httpclient.Header[] header, Throwable t, JSONObject object) {
                //ERRORES AQUI
            }
        });
    }

    private void subirImagen(String id_pub) {
        RequestParams params = new RequestParams();
        try {
            File upload = new File(file.getPath());
            params.put("path", id_pub + "/" + upload.getName(), upload);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(Global.restUrl + "upload/", params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                // ok
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                // error
            }

        });
    }

}
