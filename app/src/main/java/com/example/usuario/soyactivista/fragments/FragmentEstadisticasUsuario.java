package com.example.usuario.soyactivista.fragments;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.CountCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import logica.AnimateCounter;
import logica.ErrorCodeHelper;
import logica.Selector_de_Tema;
import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by Luis Adrian on 29/12/2015.
 */
public class FragmentEstadisticasUsuario extends Fragment {

    private static final String TAG = "FragEstadisticasUsuario";

    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        // Declare Holders
        final TextView valueNombre, valueRango, valuePosicion, valuePuntos, valuePartidas, valuePreguntas;
        Button buttonMenuPrincipal;
        final ProgressDialog dialog;


        // Choose fragment to inflate
        View v = inflater.inflate(R.layout.fragment_estadistica_usuario, container, false);
        final ParseUser currentUser = ParseUser.getCurrentUser();

        // Match Views
        valueNombre = (TextView) v.findViewById(R.id.valueNombre);
        valueRango = (TextView) v.findViewById(R.id.valueRango);
        valuePosicion = (TextView) v.findViewById(R.id.valuePosicion);
        valuePuntos = (TextView) v.findViewById(R.id.valuePuntos);
        valuePartidas = (TextView) v.findViewById(R.id.valuePartidas);
        valuePreguntas = (TextView) v.findViewById(R.id.valuePreguntas);
        buttonMenuPrincipal = (Button) v.findViewById(R.id.buttonMenuPrincipal);

        dialog = ProgressDialog.show(getContext(), "Consultando Estadísticas", "Cargando", true);

        // Assign Values
        valueNombre.setText(currentUser.getString("nombre"));

        // Set Rank
        int puntos = currentUser.getInt("puntos");

        if( puntos < 10000 )
        {
            valueRango.setText("PRINCIPIANTE");
            valueRango.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_left));
        }
        else{
            if( puntos < 100000 )
            {
                valueRango.setText("VETERANO");
                valueRango.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_left));
            }
            else
            {
                valueRango.setText("PROFESIONAL");
                valueRango.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_left));
            }

        }

        // User has no stadistic data yet. Skip queries
        if( puntos != 0){

            // Get Position.
            ParseQuery<ParseUser> userParseQuery = ParseUser.getQuery();
            userParseQuery.whereEqualTo("estado", currentUser.getString("estado"));
            userParseQuery.whereNotEqualTo("objectId", currentUser.getObjectId());
            userParseQuery.whereGreaterThan("puntos", puntos);
            userParseQuery.countInBackground(new CountCallback() {
                @Override
                public void done(int count, ParseException e) {
                    if (e == null) {
                        Integer finalCount = count + 1;
                        valuePosicion.setText(finalCount.toString());
                        valuePosicion.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_left));
                    } else {
                        Toast.makeText(getActivity(), ErrorCodeHelper.resolveErrorCode(e.getCode()), Toast.LENGTH_LONG).show();
                    }
                }
            });

            if (puntos == 0)
                valuePuntos.setText(String.valueOf(puntos) + " puntos.");
            else
                AnimarTexto(puntos,valuePuntos);




            // Query stadistics
            ParseQuery<ParseObject> query = ParseQuery.getQuery("EstadisticasUsuario");
            query.whereEqualTo("usuario", currentUser);
            query.getFirstInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject estadisticas, ParseException e) {
                    if (e == null) {

                        valuePartidas.setText(String.valueOf(estadisticas.getInt("partidas"))+" partidas.");
                        valuePartidas.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_left));

                        // Get best answer type.
                        if( estadisticas.getInt("faciles") == 0 &&
                                estadisticas.getInt("intermedias") == 0 &&
                                estadisticas.getInt("dificiles") == 0)
                        {
                            // If all cero tell none
                            valuePreguntas.setText("Ninguna.");
                            valuePreguntas.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_left));
                        }
                        else{
                            // Find biggest.
                            if(estadisticas.getInt("faciles") >= estadisticas.getInt("intermedias") &&
                                    estadisticas.getInt("faciles") >= estadisticas.getInt("dificiles") ){

                                valuePreguntas.setText("Fácil");
                                valuePreguntas.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_left));
                            }
                            else{

                                if(estadisticas.getInt("intermedias") >= estadisticas.getInt("faciles") &&
                                        estadisticas.getInt("intermedias") >= estadisticas.getInt("dificiles") ){

                                    valuePreguntas.setText("Intermedio");
                                    valuePreguntas.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_left));
                                }
                                else{
                                    valuePreguntas.setText("Difícil");
                                    valuePreguntas.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_left));
                                }

                            }
                        }

                        dialog.dismiss();
                    } else {
                        dialog.dismiss();
                        Log.d(TAG, "Error: " + e.getMessage() + " " + e.getCode());
                        valuePartidas.setText("0 partidas");
                        valuePartidas.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_left));
                        valuePreguntas.setText("Ninguna");
                        valuePreguntas.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_left));
                        Toast.makeText(getActivity(), ErrorCodeHelper.resolveErrorCode(e.getCode()), Toast.LENGTH_LONG).show();
                    }
                }
            });

        }
        else{
            Toast.makeText(getActivity(), "Todavía no has jugado tu primera partida.", Toast.LENGTH_LONG).show();
            valuePosicion.setText("Ninguno");
            valuePosicion.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_left));
            valuePuntos.setText("0 puntos.");
            valuePartidas.setText("0 partidas");
            valuePartidas.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_left));
            valuePreguntas.setText("Ninguna");
            valuePreguntas.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_left));
        }

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

    private void AnimarTexto(int numero, TextView text){
        AnimateCounter animateCounterWrong = new AnimateCounter.Builder(text)
                .setCount(0, numero)
                .setDuration(2000)
                .build();
        animateCounterWrong.execute();
    }
}
