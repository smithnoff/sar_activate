package com.example.usuario.soyactivista.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by Luis Adrian on 07/12/2015.
 */
public class FragmentMensajeCustom extends Fragment {
    private Button botonIngresar;
    private Button botonRegresar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.mensajes, container, false);

        botonIngresar = (Button) v.findViewById(R.id.buttonIngresar);
        botonRegresar = (Button) v.findViewById(R.id.buttonRegresar);

        return v;
    }
}
