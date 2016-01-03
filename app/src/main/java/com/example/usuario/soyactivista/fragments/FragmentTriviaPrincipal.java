package com.example.usuario.soyactivista.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import logica.Selector_de_Tema;
import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by Luis Adrian on 28/12/2015.
 */
public class FragmentTriviaPrincipal extends Fragment{

    public static final String EXTRAS_ENDLESS_MODE = "EXTRAS_ENDLESS_MODE";
    private Button adminPreguntas, nuevaPartida, misEstadisticas;
    private TextView valueNombrePartido;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        //Choose fragment to inflate
        View v = inflater.inflate(R.layout.fragment_trivia_principal, container, false);


        // Assing to Holders
        adminPreguntas = (Button)v.findViewById(R.id.administrarPreguntas);
        nuevaPartida = (Button)v.findViewById(R.id.nuevaPartida);
        misEstadisticas = (Button)v.findViewById(R.id.misEstadisticas);

        valueNombrePartido = (TextView) v.findViewById(R.id.valueNombrePartido);

        valueNombrePartido.setText(Selector_de_Tema.getNombrePartido());

        nuevaPartida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new FragmentTriviaDificultad();
                getFragmentManager()
                        .beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.content_frame, fragment)
                        .commit();
            }
        });

        adminPreguntas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new FragmentListarPregunta();
                getFragmentManager()
                        .beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.content_frame, fragment)
                        .commit();
            }
        });

        misEstadisticas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new FragmentEstadisticasUsuario();
                getFragmentManager()
                        .beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.content_frame, fragment)
                        .commit();
            }
        });
        return v;
    }
}
