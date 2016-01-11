package com.example.usuario.soyactivista.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import logica.ErrorCodeHelper;
import logica.Selector_de_Tema;
import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by Luis Adrian on 29/12/2015.
 */
public class FragmentTriviaDificultad extends Fragment {

    private static final String TAG = "FragTriviaDif";
    private String dificultad;
    private Integer preguntasPartida = 6;
    private Integer preguntasBuscadas = preguntasPartida * 2;
    ProgressDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        TextView valueNombrePartido;
        Button dificultadFacil, dificultadIntermedio, dificultadDificil;

        //Choose fragment to inflate
        View v = inflater.inflate(R.layout.fragment_trivia_dificultad, container, false);

        valueNombrePartido = (TextView) v.findViewById(R.id.valueNombrePartido);
        valueNombrePartido.setText(Selector_de_Tema.getNombrePartido());

        dificultadFacil = (Button) v.findViewById(R.id.btnFacil);
        dificultadIntermedio = (Button) v.findViewById(R.id.btnIntermedio);
        dificultadDificil = (Button) v.findViewById(R.id.btnDificil);

        dificultadFacil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dificultad = "Fácil";
                iniciarPartida();
            }
        });

        dificultadIntermedio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dificultad = "Intermedio";
                iniciarPartida();
            }
        });

        dificultadDificil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dificultad = "Difícil";
                iniciarPartida();
            }
        });

        return v;
    }

    // Query DB for total amount of questions in difficulty. Then Queries DB for 6 random questions
    // from selected difficulty and starts the game.
    private void iniciarPartida() {

        dialog = ProgressDialog.show(getContext(), "Buscando Preguntas", "Cargando", true);
        // Query last question to find out index.
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Pregunta");
        query.whereEqualTo("dificultad", dificultad);
        query.orderByDescending("indice");
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(final ParseObject object, ParseException e) {
                if (e == null) {
                    // Questions Found.
                    // Get 6 random numbers between 0 and last index.
                    Random generator = new Random();
                    ArrayList<Integer> indices = new ArrayList<>();
                    Set<Integer> randomNumbers = new LinkedHashSet<>();

                    Log.d(TAG, "Last Index found: " + object.getInt("indice"));

                    if(object.getInt("indice") + 1 < preguntasBuscadas){
                        for(int i = 0; i < object.getInt("indice") + 1 ; i++) {
                            indices.add(i);
                        }
                    }
                    else{
                        while (randomNumbers.size() < preguntasBuscadas) {
                            randomNumbers.add(generator.nextInt(object.getInt("indice") + 1));
                        }
                        indices.addAll(randomNumbers);
                    }

                    Log.d(TAG, indices.toString());

                    // Query 12 random questions. (Prevent Index elimination)
                    ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Pregunta");
                    query2.whereEqualTo("dificultad", dificultad);
                    query2.whereContainedIn("indice", indices);
                    query2.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {
                            if (e == null) {
                                dialog.dismiss();
                                // Questions Found.
                                ArrayList<String> preguntas = new ArrayList<>();
                                ArrayList<String> respuesta1 = new ArrayList<>();
                                ArrayList<String> respuesta2 = new ArrayList<>();
                                ArrayList<String> respuesta3 = new ArrayList<>();
                                ArrayList<String> respuesta4 = new ArrayList<>();
                                ArrayList<String> puntajes = new ArrayList<>();
                                ArrayList<String> tiempos = new ArrayList<>();
                                ArrayList<String> correctas = new ArrayList<>();

                                for (int i = 0; i < objects.size(); i++) {
                                    // Check to add only 6 questions to a game. (As we are looking for more questions (12))
                                    if( i < preguntasPartida){
                                        Log.d(TAG,"Adding Question: "+objects.get(i).getString("pregunta"));
                                        preguntas.add(objects.get(i).getString("pregunta"));
                                        respuesta1.add(objects.get(i).getString("opcion1"));
                                        respuesta2.add(objects.get(i).getString("opcion2"));
                                        respuesta3.add(objects.get(i).getString("opcion3"));
                                        respuesta4.add(objects.get(i).getString("opcion4"));
                                        puntajes.add(String.valueOf(objects.get(i).getInt("puntaje")));
                                        tiempos.add(String.valueOf(objects.get(i).getInt("tiempo")));
                                        correctas.add(String.valueOf(objects.get(i).getInt("correcta")));

                                    }
                                }

                                // Store data in Bundle and call fragment contestar preguntas
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
                                data.putInt("puntuacionPartida", 0);
                                data.putInt("respuestasCorrectas", 0);
                                data.putInt("preguntaActual",0);
                                data.putInt("totalPreguntas",objects.size());
                                data.putString("dificultad", dificultad);

                                // Log Game Vars
                                Log.d(TAG, "Total Preguntas: " + preguntas.size());

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

                            } else {
                                dialog.dismiss();
                                Log.d(TAG, "Error: " + e.getMessage() + " " + e.getCode());
                                Toast.makeText(getActivity(), ErrorCodeHelper.resolveErrorCode(e.getCode()), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    dialog.dismiss();
                    // No questions found
                    if( e.getCode() == 101){
                        // Show "NO questions" Dialog.
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Trivia de Formación");
                        builder.setMessage("No se encontraron preguntas disponibles en esta dificultad.");

                        builder.setPositiveButton("Regresar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        // After Dialog is Completely defined - Show Dialog.
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                    else{ // Another Error)
                        dialog.dismiss();
                        Log.d(TAG, "Error: " + e.getMessage() + " " + e.getCode());
                        Toast.makeText(getActivity(), ErrorCodeHelper.resolveErrorCode(e.getCode()), Toast.LENGTH_LONG).show();
                    }

                }
            }
        });
    }
}
