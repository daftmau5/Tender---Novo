package com.didasko.eduardo.tender;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;

/**
 * Created by Tecnico_Tarde on 17/03/2016.
 */
public class Prefs {

    public static void gravarNome(Context contexto, String nome) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(contexto);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("nome", nome);
        editor.commit();
    }

    public static String getNome(Context contexto) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(contexto);
        String retorno;
        retorno = preferences.getString("nome", "");
        return retorno;
    }
}