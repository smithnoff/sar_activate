package com.example.usuario.soyactivista.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by RSMAPP on 30/12/2015.
 */
public class FragmentContestarPregunta extends Fragment {

    private String TAG = "FragContestarPregunta";
    private static final String FORMAT = "%02d:%02d:%02d";
    public CountDownTimer contadorPregunta;
    private ArrayList<String> preguntas,respuesta1,respuesta2,respuesta3,respuesta4,puntajes,tiempos,correctas;
    private Integer preguntaActual,puntuacionPartida,respuestasCorrectas, totalPreguntas;
    private Boolean tiempoExtra,saltarPregunta;
    private String dificultad;


    // Text Holders
    public TextView valuePregunta, valueTiempo;
    // Buttons
    public Button buttonRespuesta1, buttonRespuesta2, buttonRespuesta3, buttonRespuesta4, buttonTiempoExtra, buttonSaltarPregunta;

    public ImageButton buttonAbandonarPartida;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_contestar_pregunta,container,false);

        valuePregunta = (TextView)view.findViewById(R.id.textViewPregunta);

        valueTiempo = (TextView)view.findViewById(R.id.textViewTiempo);

        buttonRespuesta1 = (Button)view.findViewById(R.id.buttonRespuesta1);
        buttonRespuesta2 = (Button)view.findViewById(R.id.buttonRespuesta2);
        buttonRespuesta3 = (Button)view.findViewById(R.id.buttonRespuesta3);
        buttonRespuesta4 = (Button)view.findViewById(R.id.buttonRespuesta4);

        buttonTiempoExtra = (Button) view.findViewById(R.id.buttonExtraTiempo);
        buttonSaltarPregunta = (Button) view.findViewById(R.id.buttonSaltarPregunta);

        buttonAbandonarPartida = (ImageButton) view.findViewById(R.id.buttonAbandonarPartida);

        obtenerDatosPartida();
        cargarPregunta();

        // If Wildcar available: Enable and set listener.
        if(tiempoExtra){
            buttonTiempoExtra.setEnabled(true);
            buttonTiempoExtra.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG,"Comodin Activado: Tiempo Extra");
                    // Disable
                    buttonTiempoExtra.setEnabled(false);
                    // Save Wildcar State
                    tiempoExtra = false;
                    // Modify Game Points
                    Double puntosDeducidos = Double.valueOf(puntajes.get(preguntaActual));
                    puntosDeducidos = puntosDeducidos / 4;
                    Log.d(TAG,"Puntaje deducido: "+puntosDeducidos.toString());
                    puntuacionPartida = puntuacionPartida - puntosDeducidos.intValue();
                    // Reset Timer
                    contadorPregunta.cancel();
                    contador(Integer.valueOf(tiempos.get(preguntaActual)) * 1000);
                }
            });
        }

        if(saltarPregunta){
            buttonSaltarPregunta.setEnabled(true);
            buttonSaltarPregunta.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG,"Comodin Activado: Saltar Pregunta");
                    // Disable button
                    buttonSaltarPregunta.setEnabled(false);
                    // Save wildcard state
                    saltarPregunta = false;
                    // remove points
                    Double puntosDeducidos = Double.valueOf(puntajes.get(preguntaActual));
                    puntosDeducidos = puntosDeducidos / 2;
                    Log.d(TAG,"Puntaje deducido: "+puntosDeducidos.toString());
                    puntuacionPartida = puntuacionPartida - puntosDeducidos.intValue();
                    // Call next question/end
                    if(preguntaActual + 1  < totalPreguntas)
                        siguientePregunta();
                    else
                        finalizarPartida();
                }
            });

        }


        buttonRespuesta1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Integer.valueOf(correctas.get(preguntaActual)) == 1)
                    respuestaCorrecta();
                else
                    respuestaIncorrecta();
            }
        });

        buttonRespuesta2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Integer.valueOf(correctas.get(preguntaActual)) == 2)
                    respuestaCorrecta();
                else
                    respuestaIncorrecta();
            }
        });

        buttonRespuesta3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.valueOf(correctas.get(preguntaActual)) == 3)
                    respuestaCorrecta();
                else
                    respuestaIncorrecta();
            }
        });

        buttonRespuesta4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Integer.valueOf(correctas.get(preguntaActual)) == 4)
                    respuestaCorrecta();
                else
                    respuestaIncorrecta();
            }
        });

        buttonAbandonarPartida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Confirmar");
                builder.setMessage("¿Está seguro que desea abandonar la partida?");

                builder.setPositiveButton("Abandonar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo, int which) {
                        contadorPregunta.cancel();
                        // Redirect
                        Fragment fragment = new FragmentTriviaPrincipal();
                        getFragmentManager()
                                .beginTransaction()
                                .replace(R.id.content_frame, fragment)
                                .commit();
                    }
                });

                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogo, int which) {
                        // Do nothing
                        dialogo.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        return view;
    }

    // Show incorrect Message and remove points
    private void respuestaIncorrecta() {
        // Show incorrect messages
        Toast.makeText(getActivity(), "INCORRECTO", Toast.LENGTH_SHORT).show();
        valueTiempo.setTextColor(getResources().getColor(R.color.red_error));
        valueTiempo.setText("- " + puntajes.get(preguntaActual) + " puntos");

        // remove points
        puntuacionPartida = puntuacionPartida - Integer.valueOf(puntajes.get(preguntaActual));

        // Call next question/end
        if(preguntaActual + 1 < totalPreguntas)
            siguientePregunta();
        else
            finalizarPartida();
    }

    // Show correct Message and add points
    private void respuestaCorrecta() {
        // Show incorrect messages
        Toast.makeText(getActivity(), "CORRECTO", Toast.LENGTH_SHORT).show();
        valueTiempo.setTextColor(getResources().getColor(R.color.green_complete));
        valueTiempo.setText("+ " + puntajes.get(preguntaActual) + " puntos");
        valueTiempo.setText("Correcto");

        // add points
        puntuacionPartida = puntuacionPartida + Integer.valueOf(puntajes.get(preguntaActual));

        respuestasCorrectas++;

        // Call next question/end
        if(preguntaActual + 1 < totalPreguntas)
            siguientePregunta();
        else
            finalizarPartida();

    }


    // Get all relevant data in a bundle and call fragment contestar pregunta.
    private void siguientePregunta() {

        // Cancel timer
        contadorPregunta.cancel();

        Bundle data = new Bundle();
        data.putStringArrayList("preguntas", preguntas);
        data.putStringArrayList("respuesta1", respuesta1);
        data.putStringArrayList("respuesta2", respuesta2);
        data.putStringArrayList("respuesta3", respuesta3);
        data.putStringArrayList("respuesta4", respuesta4);
        data.putStringArrayList("puntajes", puntajes);
        data.putStringArrayList("tiempos", tiempos);
        data.putStringArrayList("correctas", correctas);
        // Game variables
        data.putInt("puntuacionPartida", puntuacionPartida);
        data.putInt("respuestasCorrectas", respuestasCorrectas);
        data.putInt("preguntaActual",preguntaActual+1);
        data.putInt("totalPreguntas", totalPreguntas);
        data.putString("dificultad", dificultad);
        // Wildcars
        data.putBoolean("tiempoExtra", tiempoExtra);
        data.putBoolean("saltarPregunta", saltarPregunta);

        // Store data in bundle to send to next fragment
        Fragment fragment = new FragmentContestarPregunta();
        fragment.setArguments(data);
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();
    }


    // Get Game data into a bundle and call Game stadistic fragment.
    private void finalizarPartida() {

        // Cancel timer
        contadorPregunta.cancel();

        Bundle data = new Bundle();
        data.putInt("puntuacionPartida", puntuacionPartida);
        data.putInt("respuestasCorrectas", respuestasCorrectas);
        data.putInt("totalPreguntas",totalPreguntas);
        data.putString("dificultad", dificultad);

        // Store data in bundle to send to next fragment
        Fragment fragment = new FragmentEstadisticasPartida();
        fragment.setArguments(data);
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();
    }

    // Stores Game Data from Arguments into local variables.
    private void obtenerDatosPartida() {

        preguntas = getArguments().getStringArrayList("preguntas");
        respuesta1 = getArguments().getStringArrayList("respuesta1");
        respuesta2 = getArguments().getStringArrayList("respuesta2");
        respuesta3 = getArguments().getStringArrayList("respuesta3");
        respuesta4 = getArguments().getStringArrayList("respuesta4");
        puntajes = getArguments().getStringArrayList("puntajes");
        tiempos = getArguments().getStringArrayList("tiempos");
        correctas = getArguments().getStringArrayList("correctas");

        dificultad = getArguments().getString("dificultad");

        preguntaActual = getArguments().getInt("preguntaActual");
        puntuacionPartida = getArguments().getInt("puntuacionPartida");
        respuestasCorrectas = getArguments().getInt("respuestasCorrectas");
        totalPreguntas = getArguments().getInt("totalPreguntas");

        tiempoExtra = getArguments().getBoolean("tiempoExtra");
        saltarPregunta = getArguments().getBoolean("saltarPregunta");

        // LOG for debugging
        Log.d(TAG, "Current Question: " + preguntaActual.toString());
        Log.d(TAG, "Current Points: " + puntuacionPartida.toString());
        Log.d(TAG, "Question Points: " + puntajes.get(preguntaActual));

    }

    public void contador(int milisegundos){

        contadorPregunta = new CountDownTimer( milisegundos,1000) { // adjust the milli seconds here

            public void onTick(long millisUntilFinished) {

                valueTiempo.setText(String.format(FORMAT,
                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            // TODO: Set failed to answer procedure.
            public void onFinish() {
                respuestaIncorrecta();
            }

        }.start();
    }

    // Load Text to Holders and Starts the clock.
    public void cargarPregunta(){
        // Assign text to holders
        valuePregunta.setText(preguntas.get(preguntaActual));
        buttonRespuesta1.setText(respuesta1.get(preguntaActual));
        buttonRespuesta2.setText(respuesta2.get(preguntaActual));
        buttonRespuesta3.setText(respuesta3.get(preguntaActual));
        buttonRespuesta4.setText(respuesta4.get(preguntaActual));

        // Set & Start Clock
        contador(Integer.valueOf(tiempos.get(preguntaActual)) * 1000);
    }

}
