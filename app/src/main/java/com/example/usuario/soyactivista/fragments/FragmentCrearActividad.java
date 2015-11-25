package com.example.usuario.soyactivista.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.parse.Parse;
import com.parse.ParseUser;

import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by Brahyam on 25/11/2015.
 */
public class FragmentCrearActividad extends Fragment {


    private EditText nombre, descripcion, objetivo, encargado, inicio, fin; // All Edit Fields Holder
    private Spinner puntaje, ubicacion, estado, municipio, parroquia, estatus; // All Spinners Holder

    // Class Constructor
    public FragmentCrearActividad(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        //Choose fragment to inflate
        View v = inflater.inflate(R.layout.fragment_crear_actividad, container, false);

        //Gets Current User
        ParseUser usuarioActual = ParseUser.getCurrentUser();

        //Asign Text Edit to holders
        nombre = (EditText)v.findViewById(R.id.editActividad);
        descripcion = (EditText)v.findViewById(R.id.editDescripcion);
        objetivo = (EditText)v.findViewById(R.id.editObjetivo);
        encargado = (EditText)v.findViewById(R.id.editEncargado);
        inicio = (EditText)v.findViewById(R.id.editInicio);
        fin = (EditText)v.findViewById(R.id.editFin);

        // Asigns Spinners to holders
        puntaje = (Spinner)v.findViewById(R.id.spinPuntaje);
        ubicacion = (Spinner)v.findViewById(R.id.spinUbicacion);
        estado = (Spinner)v.findViewById(R.id.spinEstado);
        municipio = (Spinner)v.findViewById(R.id.spinMunicipio);
        parroquia = (Spinner)v.findViewById(R.id.spinParroquia);
        estatus = (Spinner)v.findViewById(R.id.spinEstatus);

        //Fill Spinners with Preset Options
        this.llenarSpinnerdesdeId(puntaje,R.array.Puntuaciones);
        this.llenarSpinnerdesdeId(ubicacion,R.array.Ubicaciones);
        this.llenarSpinnerdesdeId(municipio,getResources().getIdentifier(usuarioActual.getString("Estado"), "array", getActivity().getPackageName()));

        return v;
    }

    //Auxiliar method for filling spinners with String Arrays
    public void llenarSpinnerdesdeId(Spinner spin,int id){
        ArrayAdapter spinner_adapter = ArrayAdapter.createFromResource(getActivity(), id, android.R.layout.simple_spinner_item);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(spinner_adapter);
    }

}
