package com.example.usuario.soyactivista.fragments;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.parse.ParseUser;

import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by Luis Adrian on 29/12/2015.
 */
public class FragmentEstadisticaPartida extends Fragment {

    private TextView finPartida, textPuntosConseguidos, textCorrectas, textIncorrectas;
    private TextView puntosConseguidos, respuestasCorrectas, respuestasIncorrectas;

    private RatingBar ratingBar;
    private Button menuPrincipal;

    private int puntosPartida, correctas, incorrectas;
    private String ptos,aciertos,fallados;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {


        //Choose fragment to inflate
        View v = inflater.inflate(R.layout.fragment_estadistica_partida, container, false);

        ParseUser currentUser = ParseUser.getCurrentUser();

        //Set Textview, Button and RatingBar
        finPartida = (TextView)v.findViewById(R.id.finPartida);
        textPuntosConseguidos = (TextView)v.findViewById(R.id.textPuntosConseguidos);
        textCorrectas = (TextView)v.findViewById(R.id.textCorrectas);
        textIncorrectas = (TextView)v.findViewById(R.id.textIncorrectas);
        puntosConseguidos = (TextView)v.findViewById(R.id.puntosConseguidos);
        respuestasCorrectas = (TextView)v.findViewById(R.id.respuestasCorrectas);
        respuestasIncorrectas = (TextView)v.findViewById(R.id.respuestasIncorrectas);

        menuPrincipal = (Button)v.findViewById(R.id.volverMenuPrincipal);

        ratingBar = (RatingBar)v.findViewById(R.id.ratingBar);
        ratingBar.setEnabled(false);



        //User Bundle from last fragment
        puntosPartida = getArguments().getInt("puntos");
        ptos = String.valueOf(puntosPartida);
        puntosConseguidos.setText(ptos+" puntos");

        correctas = getArguments().getInt("aciertos");
        aciertos = String.valueOf(correctas);
        respuestasCorrectas.setText(aciertos);

        incorrectas = 6-correctas;
        fallados = String.valueOf(incorrectas);
        respuestasIncorrectas.setText(fallados);

        int puntosUser = currentUser.getInt("Puntos");
        int puntosTotales = puntosPartida+puntosUser;

        //Save Sum Points Gameplay + PointsUserCurrent
        currentUser.put("Puntos",puntosTotales);
        currentUser.saveInBackground();

        if(correctas == 6)
        {
          ratingBar.setRating(3);
        }
        else
        {
            if(correctas >=3 && correctas <= 5 )
            {
                ratingBar.setRating(2);
            }
            else
            {
                ratingBar.setRating(1);
            }
        }

        float current = ratingBar.getRating();

        ObjectAnimator anim = ObjectAnimator.ofFloat(ratingBar, "rating", 0f, current);
        anim.setDuration(3000);
        anim.start();

        menuPrincipal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new FragmentTriviaPrincipal();
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        return v;
    }
}