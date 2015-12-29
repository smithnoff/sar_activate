package com.example.usuario.soyactivista.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by Luis Adrian on 29/12/2015.
 */
public class FragmentTriviaDificultad extends Fragment {

    private Button dificultadFacil, dificultadIntermedio, dificultadDificil;
    public int dificultad;


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        //Choose fragment to inflate
        View v = inflater.inflate(R.layout.fragment_trivia_dificultad, container, false);

            dificultadFacil = (Button) v.findViewById(R.id.btnFacil);
            dificultadIntermedio = (Button)v.findViewById(R.id.btnIntermedio);
            dificultadDificil = (Button)v.findViewById(R.id.btnDificil);

            dificultadFacil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Pregunta");
                    query.whereEqualTo("dificultad", "facil");
                    query.findInBackground(new FindCallback<ParseObject>() {
                        public void done(List<ParseObject> PreguntaList, ParseException e) {
                            if (e == null) {
                                Log.d("score", "Retrieved " + PreguntaList.size() + " scores");
                                Toast.makeText(getActivity(), "Preguntas faciles: " + PreguntaList.size(), Toast.LENGTH_LONG);
                                dificultad = 1;
                                // Store data in bundle to send to next fragment
                                Bundle data = new Bundle();
                                data.putInt("dificultad", dificultad);

                                Fragment fragment = new FragmentEstadisticaPartida();
                                fragment.setArguments(data);
                                getFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.content_frame, fragment)
                                        .addToBackStack(null)
                                        .commit();
                            } else {
                                Toast.makeText(getActivity(),"No se trae datos",Toast.LENGTH_LONG);
                                Log.d("score", "Error: " + e.getMessage());
                            }
                        }
                    });
                }
            });

            dificultadIntermedio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Pregunta");
                    query.whereEqualTo("dificultad", "intermedio");
                    query.findInBackground(new FindCallback<ParseObject>() {
                        public void done(List<ParseObject> PreguntaList, ParseException e) {
                            if (e == null) {
                                Log.d("score", "Retrieved " + PreguntaList.size() + " scores");
                                Toast.makeText(getActivity(), "Preguntas faciles: " + PreguntaList.size(), Toast.LENGTH_LONG);
                                dificultad = 2;
                                // Store data in bundle to send to next fragment
                                Bundle data = new Bundle();
                                data.putInt("dificultad", dificultad);

                                Fragment fragment = new FragmentEstadisticaPartida();
                                fragment.setArguments(data);
                                getFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.content_frame, fragment)
                                        .addToBackStack(null)
                                        .commit();
                            } else {
                                Toast.makeText(getActivity(),"No se trae datos",Toast.LENGTH_LONG);
                                Log.d("score", "Error: " + e.getMessage());
                            }
                        }
                    });
                }
            });

            dificultadDificil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Pregunta");
                    query.whereEqualTo("dificultad", "dificil");
                    query.findInBackground(new FindCallback<ParseObject>() {
                        public void done(List<ParseObject> PreguntaList, ParseException e) {
                            if (e == null) {
                                Log.d("score", "Retrieved " + PreguntaList.size() + " scores");
                                Toast.makeText(getActivity(), "Preguntas faciles: " + PreguntaList.size(), Toast.LENGTH_LONG);
                                dificultad = 3;
                                // Store data in bundle to send to next fragment
                                Bundle data = new Bundle();
                                data.putInt("dificultad", dificultad);

                                Fragment fragment = new FragmentEstadisticaPartida();
                                fragment.setArguments(data);
                                getFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.content_frame, fragment)
                                        .addToBackStack(null)
                                        .commit();
                            } else {
                                Toast.makeText(getActivity(),"No se trae datos",Toast.LENGTH_LONG);
                                Log.d("score", "Error: " + e.getMessage());
                            }
                        }
                    });
                }
            });

        return v;
        }


}
