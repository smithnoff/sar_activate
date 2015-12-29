package com.example.usuario.soyactivista.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by RSMAPP on 28/12/2015.
 */
public class FragmentCrearPreguntas extends Fragment {


    private EditText pregunta, respuestaCorrecta, respuestaFalsa1,respuestaFalsa2,respuestaFalsa3;
   private Spinner puntaje,nivel,tiempo;
    private Button crearPregunta, regresar;
    private ProgressDialog dialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crear_preguntas, container, false);
        //edit holders
           pregunta=(EditText)v.findViewById(R.id.editPregunta);
           respuestaCorrecta=(EditText)v.findViewById(R.id.editRespuestaCorrecta);
           respuestaFalsa1=(EditText)v.findViewById(R.id.editRespuestaFalsa1);
           respuestaFalsa2=(EditText)v.findViewById(R.id.editRespuestaFalsa2);
           respuestaFalsa3=(EditText)v.findViewById(R.id.editRespuestaFalsa3);
// spinner holders
        puntaje=(Spinner)v.findViewById(R.id.spinnerPuntaje);
        nivel=(Spinner)v.findViewById(R.id.spinnerNivel);
       tiempo=(Spinner)v.findViewById(R.id.spinnerTiempoRespuesta);
        //button holders
       crearPregunta=(Button)v.findViewById(R.id.buttonCrearpregunta);

//fill spinners
        this.llenarSpinnerdesdeId(puntaje,R.array.Puntuaciones);
        this.llenarSpinnerdesdeId(nivel,R.array.Dificultad);
        this.llenarSpinnerdesdeId(tiempo,R.array.Tiempo);

        crearPregunta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (pregunta.getText().toString().trim().equals("") || respuestaCorrecta.getText().toString().trim().equals("") ||
                        respuestaFalsa1.getText().toString().toString().equals("") || respuestaFalsa2.getText().toString().toString().equals("")
                        || respuestaFalsa3.getText().toString().toString().equals("")) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Advertencia");
                    builder.setMessage("Uno o mas campos estan vacios verifique que todos los campos esten llenos y vuelva a intentarlo");
                      builder.setNeutralButton("Aceptar",null).create().show();
                } else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Confirmar");
                    builder.setMessage("¿Está seguro que desea guardar la Pregunta?");

                    builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialogo, int which) {
                            // Do nothing but close the dialog

                            ParseObject preguntaParse = new ParseObject("Pregunta");
                            preguntaParse.put("dificultad", nivel.getSelectedItem().toString());
                            preguntaParse.put("pregunta", pregunta.getText().toString());
                            preguntaParse.put("opcion1", respuestaCorrecta.getText().toString());
                            preguntaParse.put("opcion2", respuestaFalsa1.getText().toString());
                            preguntaParse.put("opcion3", respuestaFalsa2.getText().toString());
                            preguntaParse.put("opcion4", respuestaFalsa3.getText().toString());
                            preguntaParse.put("tiempo", Integer.parseInt(tiempo.getSelectedItem().toString()));
                            preguntaParse.put("puntaje", Integer.parseInt(puntaje.getSelectedItem().toString()));
                            preguntaParse.put("correct", 1);
                            preguntaParse.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        Toast.makeText(getActivity(), "Pregunta Creada correctamente.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getActivity(), "Ocurrió un error creando la Pregunta." + e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });


                        }

                    });

                    builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialogo, int which) {
                            // Do nothing

                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        });




        return v;
    }

    //Auxiliar method for filling spinners with String Arrays
    public void llenarSpinnerdesdeId(Spinner spin,int id){
        ArrayAdapter spinner_adapter = ArrayAdapter.createFromResource(getActivity(), id, android.R.layout.simple_spinner_item);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(spinner_adapter);
    }




}
