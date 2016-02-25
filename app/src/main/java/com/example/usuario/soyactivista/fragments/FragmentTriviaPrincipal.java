package com.example.usuario.soyactivista.fragments;

import android.app.AlertDialog;
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


import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import logica.ErrorCodeHelpers;
import logica.Selector_de_Tema;
import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by Luis Adrian on 28/12/2015.
 */
public class FragmentTriviaPrincipal extends Fragment{

    public static final String EXTRAS_ENDLESS_MODE = "EXTRAS_ENDLESS_MODE";
    private static final String TAG = "FragTriviaPrincipal";
    private Button adminPreguntas, nuevaPartida, misEstadisticas,cargarArchivos,verDoocumnetos;
    private TextView valueNombrePartido;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        //Choose fragment to inflate
        View v = inflater.inflate(R.layout.fragment_trivia_principal, container, false);

        // Assing to Holders
        valueNombrePartido = (TextView) v.findViewById(R.id.valueNombrePartido);
        adminPreguntas = (Button)v.findViewById(R.id.administrarPreguntas);
        nuevaPartida = (Button)v.findViewById(R.id.nuevaPartida);
        misEstadisticas = (Button)v.findViewById(R.id.misEstadisticas);
        cargarArchivos = (Button)v.findViewById(R.id.cargarDocumentos);
        verDoocumnetos = (Button)v.findViewById(R.id.verDocumentos);

        valueNombrePartido.setText(Selector_de_Tema.getNombrePartido());

        // Check if user is Admin
        ParseUser currentUser = ParseUser.getCurrentUser();

        if(currentUser.getInt("rol") != 1){
            adminPreguntas.setEnabled(false);
            adminPreguntas.setVisibility(View.GONE);
        }


        nuevaPartida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkIfQuestionsAvailable()){
                    Fragment fragment = new FragmentTriviaDificultad();
                    getFragmentManager()
                            .beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.content_frame, fragment)
                            .commit();
                }
                else{
                    // Show "NO questions" Dialog.
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Trivia de Formación");
                    builder.setMessage("No se encontraron preguntas disponibles.");

                    builder.setPositiveButton("Regresar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    // After Dialog is Completely defined - Show Dialog.
                    AlertDialog alert = builder.create();
                    alert.show();
                }

            }
        });

        adminPreguntas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new FragmentListarPregunta();
                getFragmentManager()
                        .beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.content_frame, fragment)
                        .commit();
            }
        });

        misEstadisticas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new FragmentEstadisticasUsuario();
                getFragmentManager()
                        .beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.content_frame, fragment)
                        .commit();
            }
        });
        cargarArchivos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new FragmentCrearDocumento();
                getFragmentManager()
                        .beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.content_frame, fragment)
                        .commit();
            }
        });
        verDoocumnetos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new FragmentListarDocumentos();
                getFragmentManager()
                        .beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.content_frame, fragment)
                        .commit();
            }
        });
        return v;
    }

    // Returns False if there are 0 questions available in DB
    private boolean checkIfQuestionsAvailable() {
        ParseObject pregunta;
        // Call first object of questions table to detect if there are any questions available
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Pregunta");
        try {
            pregunta = query.getFirst();
            if(pregunta != null)
                return true;
            else
                return false;

        } catch (ParseException e) {
            Log.d(TAG, "Error " + e.getCode() + " : " + e.getMessage());
            if(e.getCode() == 101) // Object not found code
                return false;
            else{
                Toast.makeText(getActivity(), ErrorCodeHelpers.resolveErrorCode(e.getCode()), Toast.LENGTH_LONG).show();
                return false;
            }
        }

    }
}
