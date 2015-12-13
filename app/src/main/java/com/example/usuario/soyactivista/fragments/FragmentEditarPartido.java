package com.example.usuario.soyactivista.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import logica.ActivityPantallaMenu;
import logica.Selector_de_Tema;
import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by Brahyam on 30/11/2015.
 */
public class FragmentEditarPartido extends Fragment {

    private static final String TAG = "FragEditarPartido";
    private View vistaAnterior;
    private int colorChecked;
    private EditText editNombrePartido;

    private View brown, red, orange, yellow, green, blue, purple, indigo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Choose fragment to inflate
        View v = inflater.inflate(R.layout.fragment_editar_partido, container, false);

        // Assign Holders
        editNombrePartido = (EditText) v.findViewById(R.id.editNombrePartido);
        brown = (View) v.findViewById(R.id.themeBrown);
        red = (View) v.findViewById(R.id.themeRed);
        orange = (View) v.findViewById(R.id.themeOrange);
        yellow = (View) v.findViewById(R.id.themeYellow);
        green = (View) v.findViewById(R.id.themeGreen);
        blue = (View) v.findViewById(R.id.themeBlue);
        purple = (View) v.findViewById(R.id.themePurple);
        indigo = (View) v.findViewById(R.id.themeDefault);


        // TODO: Implement Theme Editing. Right now all login on Main Activity
        return v;
    }


}