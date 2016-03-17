package com.burizalabs.soyactivista.ui;

import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dd.CircularProgressButton;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import com.burizalabs.soyactivista.R;

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
    private int valor;
    private Handler mHandler = new Handler();
    CircularProgressButton circularButtonRespuesta1,animar;
    CircularProgressButton circularButtonRespuesta2;
    CircularProgressButton circularButtonRespuesta3;
    CircularProgressButton circularButtonRespuesta4;

    // Text Holders
    public TextView valuePregunta, valueTiempo;
    // Buttons
    public Button buttonTiempoExtra, buttonSaltarPregunta;

    public ImageButton buttonAbandonarPartida;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_contestar_pregunta,container,false);

        valuePregunta = (TextView)view.findViewById(R.id.textViewPregunta);

        valueTiempo = (TextView)view.findViewById(R.id.textViewTiempo);

        circularButtonRespuesta1 = (CircularProgressButton) view.findViewById(R.id.circularButton1);
        circularButtonRespuesta2 = (CircularProgressButton) view.findViewById(R.id.circularButton2);
        circularButtonRespuesta3 = (CircularProgressButton) view.findViewById(R.id.circularButton3);
        circularButtonRespuesta4 = (CircularProgressButton) view.findViewById(R.id.circularButton4);


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
                    // Stop Timer
                    contadorPregunta.cancel();

                    // Call next question/end
                    if(preguntaActual + 1  < totalPreguntas)
                        siguientePregunta();
                    else
                        finalizarPartida();
                }
            });

        }


        circularButtonRespuesta1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableButtons();
                valor = 1;
                animar = (CircularProgressButton)v;
                if(Integer.valueOf(correctas.get(preguntaActual)) == 1){
                    respuestaCorrecta();

                    simulateErrorProgress(circularButtonRespuesta2);
                    circularButtonRespuesta2.setErrorText(respuesta2.get(preguntaActual));

                    simulateErrorProgress(circularButtonRespuesta3);
                    circularButtonRespuesta3.setErrorText(respuesta3.get(preguntaActual));

                    simulateErrorProgress(circularButtonRespuesta4);
                    circularButtonRespuesta4.setErrorText(respuesta4.get(preguntaActual));

                }
                else
                {
                    respuestaIncorrecta();
                    if(Integer.valueOf(correctas.get(preguntaActual)) == 2)
                    {
                        simulateSuccessProgress(circularButtonRespuesta2);
                        circularButtonRespuesta2.setCompleteText(respuesta2.get(preguntaActual));

                        simulateErrorProgress(circularButtonRespuesta3);
                        circularButtonRespuesta3.setErrorText(respuesta3.get(preguntaActual));

                        simulateErrorProgress(circularButtonRespuesta4);
                        circularButtonRespuesta4.setErrorText(respuesta4.get(preguntaActual));
                    }
                    else
                    {
                        if(Integer.valueOf(correctas.get(preguntaActual)) == 3)
                        {

                            simulateErrorProgress(circularButtonRespuesta2);
                            circularButtonRespuesta2.setErrorText(respuesta2.get(preguntaActual));

                            simulateSuccessProgress(circularButtonRespuesta3);
                            circularButtonRespuesta3.setCompleteText(respuesta3.get(preguntaActual));

                            simulateErrorProgress(circularButtonRespuesta4);
                            circularButtonRespuesta4.setErrorText(respuesta4.get(preguntaActual));

                        }
                        else
                        {
                            simulateSuccessProgress(circularButtonRespuesta4);
                            circularButtonRespuesta4.setCompleteText(respuesta4.get(preguntaActual));

                            simulateErrorProgress(circularButtonRespuesta2);
                            circularButtonRespuesta2.setErrorText(respuesta2.get(preguntaActual));

                            simulateErrorProgress(circularButtonRespuesta3);
                            circularButtonRespuesta3.setErrorText(respuesta3.get(preguntaActual));
                        }
                    }
                }

            }
        });

        circularButtonRespuesta2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableButtons();
                valor = 2;
                animar = (CircularProgressButton)v;
                if(Integer.valueOf(correctas.get(preguntaActual)) == 2){

                    respuestaCorrecta();

                    simulateErrorProgress(circularButtonRespuesta1);
                    circularButtonRespuesta1.setErrorText(respuesta1.get(preguntaActual));

                    simulateErrorProgress(circularButtonRespuesta3);
                    circularButtonRespuesta3.setErrorText(respuesta3.get(preguntaActual));

                    simulateErrorProgress(circularButtonRespuesta4);
                    circularButtonRespuesta4.setErrorText(respuesta4.get(preguntaActual));

                }
                else {

                    respuestaIncorrecta();
                    if(Integer.valueOf(correctas.get(preguntaActual)) == 1)
                    {
                        simulateSuccessProgress(circularButtonRespuesta1);
                        simulateErrorProgress(circularButtonRespuesta3);
                        simulateErrorProgress(circularButtonRespuesta4);
                        circularButtonRespuesta1.setCompleteText(respuesta1.get(preguntaActual));
                        circularButtonRespuesta3.setErrorText(respuesta3.get(preguntaActual));
                        circularButtonRespuesta4.setErrorText(respuesta4.get(preguntaActual));
                    }
                    else
                    {
                        if(Integer.valueOf(correctas.get(preguntaActual)) == 3)
                        {
                            simulateSuccessProgress(circularButtonRespuesta3);
                            simulateErrorProgress(circularButtonRespuesta1);
                            simulateErrorProgress(circularButtonRespuesta4);
                            circularButtonRespuesta3.setCompleteText(respuesta3.get(preguntaActual));
                            circularButtonRespuesta1.setErrorText(respuesta1.get(preguntaActual));
                            circularButtonRespuesta4.setErrorText(respuesta4.get(preguntaActual));
                        }
                        else
                        {
                            simulateSuccessProgress(circularButtonRespuesta4);
                            simulateErrorProgress(circularButtonRespuesta1);
                            simulateErrorProgress(circularButtonRespuesta3);
                            circularButtonRespuesta4.setCompleteText(respuesta4.get(preguntaActual));
                            circularButtonRespuesta1.setErrorText(respuesta1.get(preguntaActual));
                            circularButtonRespuesta3.setErrorText(respuesta3.get(preguntaActual));
                        }
                    }
                }
            }
        });

        circularButtonRespuesta3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableButtons();
                valor = 3;
                animar=(CircularProgressButton)v;
                if (Integer.valueOf(correctas.get(preguntaActual)) == 3){

                    respuestaCorrecta();

                    simulateErrorProgress(circularButtonRespuesta1);
                    circularButtonRespuesta1.setErrorText(respuesta1.get(preguntaActual));

                    simulateErrorProgress(circularButtonRespuesta2);
                    circularButtonRespuesta2.setErrorText(respuesta2.get(preguntaActual));

                    simulateErrorProgress(circularButtonRespuesta4);
                    circularButtonRespuesta4.setErrorText(respuesta4.get(preguntaActual));
                }
                else {
                    respuestaIncorrecta();
                    if(Integer.valueOf(correctas.get(preguntaActual)) == 1)
                    {
                        simulateSuccessProgress(circularButtonRespuesta1);
                        simulateErrorProgress(circularButtonRespuesta2);
                        simulateErrorProgress(circularButtonRespuesta4);
                        circularButtonRespuesta1.setCompleteText(respuesta1.get(preguntaActual));
                        circularButtonRespuesta2.setErrorText(respuesta2.get(preguntaActual));
                        circularButtonRespuesta4.setErrorText(respuesta4.get(preguntaActual));
                    }
                    else
                    {
                        if(Integer.valueOf(correctas.get(preguntaActual)) == 2)
                        {
                            simulateSuccessProgress(circularButtonRespuesta2);
                            simulateErrorProgress(circularButtonRespuesta1);
                            simulateErrorProgress(circularButtonRespuesta4);
                            circularButtonRespuesta2.setCompleteText(respuesta2.get(preguntaActual));
                            circularButtonRespuesta1.setErrorText(respuesta1.get(preguntaActual));
                            circularButtonRespuesta4.setErrorText(respuesta4.get(preguntaActual));
                        }
                        else
                        {
                            simulateSuccessProgress(circularButtonRespuesta4);
                            simulateErrorProgress(circularButtonRespuesta1);
                            simulateErrorProgress(circularButtonRespuesta2);
                            circularButtonRespuesta4.setCompleteText(respuesta4.get(preguntaActual));
                            circularButtonRespuesta1.setErrorText(respuesta1.get(preguntaActual));
                            circularButtonRespuesta2.setErrorText(respuesta2.get(preguntaActual));
                        }
                    }
                }
            }
        });

        circularButtonRespuesta4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableButtons();
                valor = 4;
                animar=(CircularProgressButton)v;
                if(Integer.valueOf(correctas.get(preguntaActual)) == 4){

                    respuestaCorrecta();

                    simulateErrorProgress(circularButtonRespuesta1);
                    circularButtonRespuesta1.setErrorText(respuesta1.get(preguntaActual));

                    simulateErrorProgress(circularButtonRespuesta2);
                    circularButtonRespuesta2.setErrorText(respuesta2.get(preguntaActual));

                    simulateErrorProgress(circularButtonRespuesta3);
                    circularButtonRespuesta3.setErrorText(respuesta3.get(preguntaActual));

                }

                else {
                    respuestaIncorrecta();
                    if(Integer.valueOf(correctas.get(preguntaActual)) == 1)
                    {
                        simulateSuccessProgress(circularButtonRespuesta1);
                        simulateErrorProgress(circularButtonRespuesta3);
                        simulateErrorProgress(circularButtonRespuesta2);
                        circularButtonRespuesta1.setCompleteText(respuesta1.get(preguntaActual));
                        circularButtonRespuesta3.setErrorText(respuesta3.get(preguntaActual));
                        circularButtonRespuesta2.setErrorText(respuesta2.get(preguntaActual));
                    }
                    else
                    {
                        if(Integer.valueOf(correctas.get(preguntaActual)) == 2)
                        {
                            simulateSuccessProgress(circularButtonRespuesta2);
                            simulateErrorProgress(circularButtonRespuesta1);
                            simulateErrorProgress(circularButtonRespuesta3);
                            circularButtonRespuesta2.setCompleteText(respuesta2.get(preguntaActual));
                            circularButtonRespuesta1.setErrorText(respuesta1.get(preguntaActual));
                            circularButtonRespuesta3.setErrorText(respuesta3.get(preguntaActual));
                        }
                        else
                        {
                            simulateSuccessProgress(circularButtonRespuesta3);
                            simulateErrorProgress(circularButtonRespuesta1);
                            simulateErrorProgress(circularButtonRespuesta2);
                            circularButtonRespuesta4.setCompleteText(respuesta3.get(preguntaActual));
                            circularButtonRespuesta1.setErrorText(respuesta1.get(preguntaActual));
                            circularButtonRespuesta2.setErrorText(respuesta2.get(preguntaActual));
                        }
                    }
                }
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

    // Disable all answer buttons.
    private void disableButtons() {

        circularButtonRespuesta1.setClickable(false);
        circularButtonRespuesta2.setClickable(false);
        circularButtonRespuesta3.setClickable(false);
        circularButtonRespuesta4.setClickable(false);
    }

    // Show incorrect Message and remove points
    private void respuestaIncorrecta() {
        // Show incorrect messages
      // valueTiempo.setTextColor(getResources().getColor(R.color.red_error));
     //  valueTiempo.setText("Incorrecto");

        // Stop timer
        contadorPregunta.cancel();


        // remove points
        puntuacionPartida = puntuacionPartida - Integer.valueOf(puntajes.get(preguntaActual));

        if(animar!=null)
        simulateErrorProgress(animar);
        if(valor==1)
        {
            circularButtonRespuesta1.setErrorText(respuesta1.get(preguntaActual));
        }
        else
        {
            if(valor ==2)
            {
                circularButtonRespuesta2.setErrorText(respuesta2.get(preguntaActual));
            }
            else
            {
                if (valor==3)
                {
                    circularButtonRespuesta3.setErrorText(respuesta3.get(preguntaActual));
                }
                else
                    circularButtonRespuesta4.setErrorText(respuesta4.get(preguntaActual));
            }

        }
        // Call next question/end

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // acciones que se ejecutan tras los milisegundos
                if (preguntaActual + 1 < totalPreguntas) {
                    valueTiempo.setTextColor(getResources().getColor(R.color.red_error));
                    valueTiempo.setText("Incorrecto - " + puntajes.get(preguntaActual) + " puntos");
                    mHandler.postDelayed(new Runnable() {
                        public void run() {
                            doStuff(1);
                        }
                    }, 1800);

                } else {

                    valueTiempo.setTextColor(getResources().getColor(R.color.red_error));
                    valueTiempo.setText("Incorrecto - " + puntajes.get(preguntaActual) + " puntos");
                    mHandler.postDelayed(new Runnable() {
                        public void run() {
                            doStuff(2);
                        }
                    }, 1800);
                }
            }
        }, 2500);


    }

    // Show correct Message and add points
    private void respuestaCorrecta() {
        // Show correct messages
      // valueTiempo.setTextColor(getResources().getColor(R.color.green_complete));
     //  valueTiempo.setText("+ " + puntajes.get(preguntaActual) + " puntos");
     // valueTiempo.setText("Correcto");

        // Stop timer
        contadorPregunta.cancel();

        // add points
        puntuacionPartida = puntuacionPartida + Integer.valueOf(puntajes.get(preguntaActual));

        respuestasCorrectas++;
        if(animar!=null)
        simulateSuccessProgress(animar);
        if(valor==1)
        {
            circularButtonRespuesta1.setCompleteText(respuesta1.get(preguntaActual));
        }
        else
        {
            if(valor ==2)
            {
                circularButtonRespuesta2.setCompleteText(respuesta2.get(preguntaActual));
            }
            else
            {
                if (valor==3)
                {
                    circularButtonRespuesta3.setCompleteText(respuesta3.get(preguntaActual));
                }
                else
                    circularButtonRespuesta4.setCompleteText(respuesta4.get(preguntaActual));
            }

        }



        // Call next question/end
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // acciones que se ejecutan tras los milisegundos
                if(preguntaActual +1  < totalPreguntas) {


                    mHandler.postDelayed(new Runnable() {
                        public void run() {


                            doStuff(1);
                        }
                    }, 1800);
                    valueTiempo.setTextColor(getResources().getColor(R.color.green_complete));
                    valueTiempo.setText("Correcto + " + puntajes.get(preguntaActual) + " puntos");
                } else {
                    mHandler.postDelayed(new Runnable() {
                        public void run() {

                            doStuff(2);
                        }
                    }, 1800);
                    valueTiempo.setTextColor(getResources().getColor(R.color.green_complete));
                    valueTiempo.setText("Correcto  + " + puntajes.get(preguntaActual) + " puntos");

                }
            }
        }, 2500);
    }
    private void doStuff(int x) {


        if(x==1)
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
        data.putBoolean("tiempoExtra", true);
        data.putBoolean("saltarPregunta", true);

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
        data.putInt("totalPreguntas", totalPreguntas);
        data.putString("dificultad", dificultad);

        // Store data in bundle to send to next fragment
        Fragment fragment = new FragmentEstadisticasPartida();
        fragment.setArguments(data);
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commitAllowingStateLoss();
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
        circularButtonRespuesta1.setText(respuesta1.get(preguntaActual));
        circularButtonRespuesta2.setText(respuesta2.get(preguntaActual));
        circularButtonRespuesta3.setText(respuesta3.get(preguntaActual));
        circularButtonRespuesta4.setText(respuesta4.get(preguntaActual));
        circularButtonRespuesta1.setProgress(0);
        circularButtonRespuesta2.setProgress(0);
        circularButtonRespuesta3.setProgress(0);
        circularButtonRespuesta4.setProgress(0);

        // Set & Start Clock
        contador(Integer.valueOf(tiempos.get(preguntaActual)) * 1000);
    }

    private void simulateSuccessProgress(final CircularProgressButton button) {
        final ValueAnimator widthAnimation = ValueAnimator.ofInt(1, 100);
        widthAnimation.setDuration(1500);
        widthAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        widthAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                button.setProgress(value);

            }
        });
        widthAnimation.start();

    }

    private void simulateErrorProgress(final CircularProgressButton button) {
        ValueAnimator widthAnimation = ValueAnimator.ofInt(1, 99);
        widthAnimation.setDuration(1500);
        widthAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        widthAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                button.setProgress(value);
                if (value == 99) {
                    button.setProgress(-1);

                }
            }
        });
        widthAnimation.start();

    }
}




