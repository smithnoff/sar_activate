package com.example.usuario.soyactivista.fragments;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.Timer;
import java.util.TimerTask;

import logica.ErrorCodeHelper;
import soy_activista.quartzapp.com.soy_activista.R;
import logica.AnimateCounter;

/**
 * Created by Luis Adrian on 29/12/2015.
 */
public class FragmentEstadisticasPartida extends Fragment {


    private static final String TAG = "FragEstadisticaPartida";
    private TextView valuePuntosConseguidos, valueRespuestasCorrectas, valueRespuestasIncorrectas;

    private RatingBar ratingBar;
    private Button buttonMenuPrincipal;

    private int puntosPartida, correctas, incorrectas;
    private String ptos,aciertos,fallados;

    private int number = 0;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {


        //Choose fragment to inflate
        View v = inflater.inflate(R.layout.fragment_estadistica_partida, container, false);

        final ParseUser currentUser = ParseUser.getCurrentUser();

        //Set Textview, Button and RatingBar
        valuePuntosConseguidos = (TextView)v.findViewById(R.id.valuePuntosConseguidos);
        valueRespuestasCorrectas = (TextView)v.findViewById(R.id.valueRespuestasCorrectas);
        valueRespuestasIncorrectas = (TextView)v.findViewById(R.id.valueRespuestasIncorrectas);

        buttonMenuPrincipal = (Button)v.findViewById(R.id.buttonVolverMenuPrincipal);

        ratingBar = (RatingBar)v.findViewById(R.id.ratingBar);
        ratingBar.setEnabled(false);

        Integer puntos = getArguments().getInt("puntuacionPartida");
        Integer correctas = getArguments().getInt("respuestasCorrectas");

        // Check for below cero values.
        if(puntos < 0)
            puntos = 0;

        // Set Values
        //valuePuntosConseguidos.setText(puntos.toString());
        valueRespuestasCorrectas.setText(correctas.toString());
        valueRespuestasCorrectas.startAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.slide_in_left));
        Integer incorrectas = getArguments().getInt("totalPreguntas")- getArguments().getInt("respuestasCorrectas");
        valueRespuestasIncorrectas.setText(incorrectas.toString());
        valueRespuestasIncorrectas.startAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.slide_in_left));

        // Load Rating
        asignarEstrellas();

        //Animated Points
        AnimarTexto(puntos,valuePuntosConseguidos);


        // Update user points
        currentUser.put("puntos",currentUser.getInt("puntos")+puntos);
        currentUser.saveEventually();

        // Update user stadistics
        ParseQuery<ParseObject> stadisticsQuery = ParseQuery.getQuery("EstadisticasUsuario");
        stadisticsQuery.whereEqualTo("usuario",currentUser);
        stadisticsQuery.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null && object != null) {
                    // Update stadistics.
                    object.put("partidas", object.getInt("partidas") + 1);

                    // Increment difficulty answers
                    switch (getArguments().getString("dificultad")) {
                        case "facil":
                            object.put("faciles", object.getInt("faciles") + getArguments().getInt("respuestasCorrectas"));
                            break;
                        case "intermedio":
                            object.put("intermedias", object.getInt("intermedias") + getArguments().getInt("respuestasCorrectas"));
                            break;
                        case "dificil":
                            object.put("dificiles", object.getInt("dificiles") + getArguments().getInt("respuestasCorrectas"));
                            break;
                        default:
                            object.put("faciles", object.getInt("faciles") + getArguments().getInt("respuestasCorrectas"));
                            break;
                    }
                    object.saveEventually();
                } else {
                    // Object not found
                    if (e.getCode() == 101) {
                        Log.d(TAG, "NO stadistics found for user");
                        // Create Stadistics and load
                        ParseObject estadisticas = new ParseObject("EstadisticasUsuario");
                        estadisticas.put("usuario", currentUser);
                        estadisticas.put("partidas", 1);
                        // Increment difficulty answers
                        switch (getArguments().getString("dificultad")) {
                            case "facil":
                                estadisticas.put("faciles", getArguments().getInt("respuestasCorrectas"));
                                estadisticas.put("intermedias", 0);
                                estadisticas.put("dificiles", 0);
                                break;
                            case "intermedio":
                                estadisticas.put("faciles", 0);
                                estadisticas.put("intermedias", getArguments().getInt("respuestasCorrectas"));
                                estadisticas.put("dificiles", 0);
                                break;
                            case "dificil":
                                estadisticas.put("faciles", 0);
                                estadisticas.put("intermedias", 0);
                                estadisticas.put("dificiles", getArguments().getInt("respuestasCorrectas"));
                                break;
                            default:
                                estadisticas.put("faciles", getArguments().getInt("respuestasCorrectas"));
                                estadisticas.put("intermedias", 0);
                                estadisticas.put("dificiles", 0);
                                break;
                        }

                        estadisticas.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    Log.d(TAG, "New Stadistics saved correctly");
                                } else {
                                    Log.d(TAG, "Error Saving New Stadistics: " + e.getMessage() + " " + e.getCode());
                                }

                            }
                        });

                    } else {
                        // Another error
                        Log.d(TAG, "Error: " + e.getMessage() + " " + e.getCode());
                        Toast.makeText(getActivity(), ErrorCodeHelper.resolveErrorCode(e.getCode()), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });


        buttonMenuPrincipal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new FragmentTriviaPrincipal();
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, fragment)
                        .commit();
            }
        });
        return v;
    }

    // Set star rating depending on correct answers
    private void asignarEstrellas() {

        if(getArguments().getInt("respuestasCorrectas") < 1)
        animarEstrellas(0);
        else {
            if (getArguments().getInt("respuestasCorrectas") <= 2)
                animarEstrellas(1);
            else {
                if (getArguments().getInt("respuestasCorrectas") <= 5)
                    animarEstrellas(2);
                else
                    animarEstrellas(3);
            }
        }
    }

    private void animarEstrellas(int current){
        ratingBar.setRating(current);
        ObjectAnimator anim = ObjectAnimator.ofFloat(ratingBar, "rating", current, 3f);
        anim.setDuration(1000);
        anim.start();
    }

    private void AnimarTexto(int numero, TextView text){
        AnimateCounter animateCounterWrong = new AnimateCounter.Builder(text)
                .setCount(0, numero)
                .setDuration(2000)
                .build();
        animateCounterWrong.execute();
    }

}
