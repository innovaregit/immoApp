package com.innovare.marceloagustini.immoapp.utilidades;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class Persistencia {

    public static void setUsername(final Context context, String username) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("username", username);
        editor.commit();
    }

    public static String getUsername(final Context context) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        return settings.getString("username", "");
    }

    public static void setPassword(final Context context, String password) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("password", password);
        editor.commit();
    }

    public static String getPassword(final Context context) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        return settings.getString("password", "");
    }
}
