package com.example.usuario.soyactivista.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by Luis Adrian on 29/12/2015.
 */
public class FragmentTriviaDificultad extends Fragment {

    private Button dificultadFacil, dificultadIntermedio, dificultadDificil;
    public String dificultad;


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
                                                       dificultad = "facil";
                                                       // Store data in bundle to send to next fragment
                                                       Bundle data = new Bundle();
                                                       data.putString("dificultad", dificultad);

                                                       Fragment fragment = new FragmentContestarPregunta();
                                                       fragment.setArguments(data);
                                                       getFragmentManager()
                                                               .beginTransaction()
                                                               .replace(R.id.content_frame, fragment)
                                                               .addToBackStack(null)
                                                               .commit();
                                                   }
                                               });

            dificultadIntermedio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dificultad = "intermedio";
                    // Store data in bundle to send to next fragment
                    Bundle data = new Bundle();
                    data.putString("dificultad", dificultad);

                    Fragment fragment = new FragmentContestarPregunta();
                    fragment.setArguments(data);
                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.content_frame, fragment)
                            .addToBackStack(null)
                            .commit();
                }
            });

            dificultadDificil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dificultad = "dificil";
                    // Store data in bundle to send to next fragment
                    Bundle data = new Bundle();
                    data.putString("dificultad", dificultad);

                    Fragment fragment = new FragmentContestarPregunta();
                    fragment.setArguments(data);
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
