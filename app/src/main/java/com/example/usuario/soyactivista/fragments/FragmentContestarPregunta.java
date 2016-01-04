package com.example.usuario.soyactivista.fragments;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by RSMAPP on 30/12/2015.
 */
public class FragmentContestarPregunta extends Fragment {


    private static final String FORMAT = "%02d:%02d:%02d";
   private String dificultadElegida,respuestaCorrecta;
public static int i=0;
    public  ParseQuery<ParseObject> query ;
  public CountDownTimer contadorPreg;
    public int seconds,aciertos=0,puntosPregunta=0,puntosAcumulados=0,correcta;

      public TextView pregunta,tiempo;
    public Button respuesta1,respuesta2,respuesta3,respuesta4;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_contestar_pregunta,container,false);
        pregunta=(TextView)v.findViewById(R.id.textViewPregunta);
        tiempo=(TextView)v.findViewById(R.id.textViewTiempo);
        respuesta1=(Button)v.findViewById(R.id.buttonRespuesta1);
        respuesta2=(Button)v.findViewById(R.id.buttonRespuesta2);
        respuesta3=(Button)v.findViewById(R.id.buttonRespuesta3);
        respuesta4=(Button)v.findViewById(R.id.buttonRespuesta4);

        respuesta1.setOnClickListener(verificarCorrecta);
        respuesta2.setOnClickListener(verificarCorrecta);
        respuesta3.setOnClickListener(verificarCorrecta);
        respuesta4.setOnClickListener(verificarCorrecta);
        Bundle bundle=this.getArguments();
        dificultadElegida=bundle.getString("dificultad");
        query = ParseQuery.getQuery("Pregunta");
        query.whereEqualTo("dificultad", dificultadElegida);

             setPregunta();







        return v;
    }
    public void contador(int segundos)
    {
       contadorPreg= new CountDownTimer( segundos,1000) { // adjust the milli seconds here

            public void onTick(long millisUntilFinished) {


                tiempo.setText("" + String.format(FORMAT,
                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            public void onFinish() {
                tiempo.setText("Hecho!");
                setPregunta();
            }
        }.start();
    }

    public void setPregunta()
    {
        final int ordenRespuesta[]={1,2,3,4};
        int temp,num;
        Random r=new Random();
        for (int h = 0; h <4 ; h++) {
            num=r.nextInt(4-1)+1;
            temp=ordenRespuesta[h];
            ordenRespuesta[h]=ordenRespuesta[num];
            ordenRespuesta[num]=temp;
        }


        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> PreguntaList, ParseException e) {
                if (e == null) {
                    //TODO: change the if condition to 6 questions
                 if(i<PreguntaList.size()) {
                     pregunta.setText(PreguntaList.get(i).getString("pregunta"));
                     respuesta1.setText(PreguntaList.get(i).getString("opcion"+ordenRespuesta[0]));
                     respuesta2.setText(PreguntaList.get(i).getString("opcion"+ordenRespuesta[1]));
                     respuesta3.setText(PreguntaList.get(i).getString("opcion"+ordenRespuesta[2]));
                     respuesta4.setText(PreguntaList.get(i).getString("opcion"+ordenRespuesta[3]));
                     correcta=PreguntaList.get(i).getInt("correcta");
                     respuestaCorrecta=PreguntaList.get(i).getString("opcion"+correcta);

                     puntosPregunta=PreguntaList.get(i).getInt("puntaje");
                     int timer = PreguntaList.get(i).getInt("tiempo");
                     String tiempochar = String.valueOf(timer);

                     seconds = Integer.parseInt(tiempochar) * 1000;
                     contador(seconds);

                     // Store data in bundle to send to next fragment
                 }else {
                     Toast.makeText(getActivity(), "FINALIZADO", Toast.LENGTH_SHORT).show();
                     Bundle datos = new Bundle();
                     datos.putInt("respuestasCorrectas", aciertos);
                     datos.putInt("totalPreguntas", i);
                     datos.putInt("puntuacionPartida", puntosAcumulados);
                     datos.putString("dificultad",dificultadElegida);
                     i=0;

                     Fragment fragment = new FragmentEstadisticasPartida();
                    fragment.setArguments(datos);
                     getFragmentManager()
                             .beginTransaction()
                             .replace(R.id.content_frame, fragment)
                             .addToBackStack(null)
                             .commit();
                     aciertos=0;
                 }
                } else {
                    Toast.makeText(getActivity(), "No se trae datos", Toast.LENGTH_LONG);
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });

    }

 View.OnClickListener verificarCorrecta=new View.OnClickListener() {
     @Override
     public void onClick(View v) {
         Button seleccion=(Button)v;

         if(seleccion.getText().toString().equals(respuestaCorrecta))
         {
             Toast.makeText(getActivity(), "CORRECTO", Toast.LENGTH_SHORT).show();
             puntosAcumulados+=puntosPregunta;
             aciertos++;
         }else{
             Toast.makeText(getActivity(), "INCORRECTO", Toast.LENGTH_SHORT).show();
         }
         i++;
         contadorPreg.cancel();
         setPregunta();

     }
 };



}
