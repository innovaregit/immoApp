package com.innovare.marceloagustini.immoapp.controllers;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.innovare.marceloagustini.immoapp.R;
import com.innovare.marceloagustini.immoapp.adapters.PublicacionAdapter;
import com.innovare.marceloagustini.immoapp.clases.Publicacion;
import com.innovare.marceloagustini.immoapp.clases.Usuario;
import com.innovare.marceloagustini.immoapp.utilidades.Global;
import com.innovare.marceloagustini.immoapp.utilidades.Persistencia;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    EditText username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        username.setText("");
        password.setText("");

        if (!Persistencia.getUsername(this).isEmpty() && !Persistencia.getPassword(this).isEmpty()) {
            loginRemoto(Persistencia.getUsername(this), Persistencia.getPassword(this));
        }
    }

    //
    public void login(View view) {
        loginRemoto(username.getText().toString(), password.getText().toString());
    }


    private void createCustomToast(String texto) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast,
                (ViewGroup) findViewById(R.id.custom_toast_container));

        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(texto);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }


    /*
    http://i-mmo.net/publicacion/598dc685fbca93613846a598
    http://i-mmo.net/usuario/login/MARCELO%20AGUSTINI/xxxxx
    */

    private void loginRemoto(final String username, final String password) {
        final Activity act = this;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Global.restUrl + "/usuario/login/" + username + "/" + password, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                try {
                    if (!response.isNull("data")) {
                        Gson gson = new Gson();
                        Usuario usuario = gson.fromJson(response.getJSONObject("data").toString(),
                                new TypeToken<Usuario>() {
                                }.getType());
                        if (usuario != null) {
                            Global.usuario = usuario;
                            //
                            Persistencia.setUsername(act,username);
                            Persistencia.setPassword(act,password);
                            //
                            Intent intent = new Intent(act, MainActivity.class);
                            startActivity(intent);
                            Log.e("_ID", usuario.get_id());
                            act.finish();
                            createCustomToast("BIENVENIDO A IMMO");
                        }

                    } else {
                        createCustomToast("ERROR");
                    }
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
