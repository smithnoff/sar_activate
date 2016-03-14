package com.burizalabs.soyactivista.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.CountCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import com.burizalabs.soyactivista.utils.AnimateCounter;
import com.burizalabs.soyactivista.utils.ErrorCodeHelpers;
import com.burizalabs.soyactivista.R;

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
        }
        else{
            if( puntos < 100000 )
            {
                valueRango.setText("VETERANO");
            }
            else
            {
                valueRango.setText("PROFESIONAL");
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
                    } else {
                        Toast.makeText(getActivity(), ErrorCodeHelpers.resolveErrorCode(e.getCode()), Toast.LENGTH_LONG).show();
                    }
                }
            });

            if (puntos == 0)
                valuePuntos.setText(String.valueOf(puntos)+ " puntos");
            else
                AnimarTexto(puntos,valuePuntos);

            // Query stadistics
            ParseQuery<ParseObject> query = ParseQuery.getQuery("EstadisticasUsuario");
            query.whereEqualTo("usuario", currentUser);
            query.getFirstInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject estadisticas, ParseException e) {
                    if (e == null) {

                        valuePartidas.setText(String.valueOf(estadisticas.getInt("partidas")));

                        // Get best answer type.
                        if( estadisticas.getInt("faciles") == 0 &&
                                estadisticas.getInt("intermedias") == 0 &&
                                estadisticas.getInt("dificiles") == 0)
                        {
                            // If all cero tell none
                            valuePreguntas.setText("Ninguna");
                        }
                        else{
                            // Find biggest.
                            if(estadisticas.getInt("faciles") >= estadisticas.getInt("intermedias") &&
                                    estadisticas.getInt("faciles") >= estadisticas.getInt("dificiles") ){

                                valuePreguntas.setText("Fácil");
                            }
                            else{

                                if(estadisticas.getInt("intermedias") >= estadisticas.getInt("faciles") &&
                                        estadisticas.getInt("intermedias") >= estadisticas.getInt("dificiles") ){

                                    valuePreguntas.setText("Intermedio");
                                }
                                else{
                                    valuePreguntas.setText("Difícil");
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
                        Toast.makeText(getActivity(), ErrorCodeHelpers.resolveErrorCode(e.getCode()), Toast.LENGTH_LONG).show();
                    }
                }
            });

        }
        else{
            dialog.dismiss();
            Toast.makeText(getActivity(), "Todavía no has jugado tu primera partida.", Toast.LENGTH_LONG).show();
            valuePosicion.setText("Ninguno");
            valuePosicion.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_left));
            valuePuntos.setText("0 puntos");
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
