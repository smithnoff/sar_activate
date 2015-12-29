package com.example.usuario.soyactivista.fragments;

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

import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by RSMAPP on 28/12/2015.
 */
public class FragmentCrearPreguntas extends Fragment {


    private EditText pregunta, respuestaCorrecta, respuestaFalsa1,respuestaFalsa2,respuestaFalsa3;
   private Spinner puntaje,nivel,tiempo;
    private Button crearPregunta, regresar;


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
        this.llenarSpinnerdesdeId(puntaje,R.array.Puntajes);
        this.llenarSpinnerdesdeId(nivel,R.array.Niveles);
        this.llenarSpinnerdesdeId(tiempo,R.array.Tiempos);

        crearPregunta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //metodo parse para guardar las opciones
                Fragment fragment = new FragmentDetallePregunta();
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, fragment)
                        .commit();

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
