package com.example.usuario.soyactivista.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import logica.FActivityPantallaMenu;
import soy_activista.quartzapp.com.soy_activista.R;

import static java.lang.Integer.parseInt;

/**
 * Created by Brahyam on 25/11/2015.
 */
public class FragmentCrearActividad extends Fragment {


    private TextView labelPuntaje, labelDescripcion, labelEstado, labelMunicipio, labelParroquia;
    private EditText puntaje, descripcion, objetivo, encargado, creador,  inicio, fin, parroquia; // Edit Field holders
    private Spinner nombre, ubicacion, estado, municipio; // Spinner holders
    private Button crear,cancelar; // Button holders
    private ProgressDialog dialog;
    private ParseObject tipoActividad; // TipoActividad to be associated with Actividad

    // Class Constructor
    public FragmentCrearActividad(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        //Choose fragment to inflate
        View v = inflater.inflate(R.layout.fragment_crear_actividad, container, false);

        //Gets Current User
        final ParseUser usuarioActual = ParseUser.getCurrentUser();

        //Asign TextViews to Holders
        labelPuntaje = (TextView)v.findViewById(R.id.labelPuntaje);
        labelDescripcion = (TextView)v.findViewById(R.id.labelDescripcion);
        labelEstado = (TextView)v.findViewById(R.id.labelEstado);
        labelMunicipio = (TextView)v.findViewById(R.id.labelMunicipio);
        labelParroquia = (TextView)v.findViewById(R.id.labelParroquia);


        //Asign Text Edit to holders
        puntaje = (EditText)v.findViewById(R.id.editPuntaje);
        descripcion = (EditText)v.findViewById(R.id.editDescripcion);
        objetivo = (EditText)v.findViewById(R.id.editObjetivo);
        encargado = (EditText)v.findViewById(R.id.editEncargado);
        creador = (EditText)v.findViewById(R.id.editCreador);
        inicio = (EditText)v.findViewById(R.id.editInicio);
        fin = (EditText)v.findViewById(R.id.editFin);
        parroquia = (EditText)v.findViewById(R.id.editParroquia);

        // Asigns Spinners to holders
        nombre = (Spinner)v.findViewById(R.id.spinNombreActividad);
        ubicacion = (Spinner)v.findViewById(R.id.spinUbicacion);
        estado = (Spinner)v.findViewById(R.id.spinEstado);
        municipio = (Spinner)v.findViewById(R.id.spinMunicipio);
        //parroquia = (Spinner)v.findViewById(R.id.spinParroquia); Commented as will be used as Edit Text while data is parsed.

        // Asign Buttons to holders
        crear = (Button)v.findViewById(R.id.botonCrearActividad);
        cancelar = (Button)v.findViewById(R.id.botonCancelar);

        // Load Defaults
        creador.setEnabled(false);
        creador.setText(usuarioActual.getString("nombre"));

        //Fill Spinners with Preset Options
        this.llenarSpinnerdesdeId(ubicacion, R.array.Ubicaciones);
        this.llenarSpinnerdesdeId(estado, R.array.Estados);

        // Fill Name Spinner from parse
        ParseQueryAdapter.QueryFactory<ParseObject> factory = new ParseQueryAdapter.QueryFactory<ParseObject>() {
            public ParseQuery create() {
                ParseQuery query = new ParseQuery("TipoActividad");
                return query;
            }
        };
        // Overrriding ParseQueryAdapter getViewTypeCount method to get past issue 79011
        final ParseQueryAdapter<ParseObject> adapter = new ParseQueryAdapter<ParseObject>(this.getActivity(), factory){
            @Override
            public int getViewTypeCount(){
                return 1;
            }
        };
        adapter.setTextKey("nombre");
        nombre.setAdapter(adapter);

        // On Activity selected populate puntaje and descripcion
        nombre.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tipoActividad = adapter.getItem(position);
                puntaje.setText(Integer.toString(tipoActividad.getInt("puntaje")));
                labelPuntaje.setVisibility(View.VISIBLE);
                puntaje.setVisibility(View.VISIBLE);
                descripcion.setText(tipoActividad.getString("descripcion"));
                labelDescripcion.setVisibility(View.VISIBLE);
                descripcion.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Spinner OnItemSelected Listeners
        ubicacion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0){ // Estadal Selected
                    //Show remaining Text/Spinners/Fields
                    labelEstado.setVisibility(View.VISIBLE);
                    estado.setVisibility(View.VISIBLE);
                    labelMunicipio.setVisibility(View.VISIBLE);
                    municipio.setVisibility(View.VISIBLE);
                    labelParroquia.setVisibility(View.VISIBLE);
                    parroquia.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        estado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                municipio.setAdapter(null);
                llenarSpinnerdesdeId(municipio, getResources().getIdentifier(estado.getSelectedItem().toString().replace(' ', '_'), "array", getActivity().getPackageName()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Buttons Behavior
        crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dialog = ProgressDialog.show(getActivity(),"","Creando Actividad",true);

                // Fill ParseObject to send
                ParseObject actividad = new ParseObject("Actividad");
                actividad.put("tipoActividad",tipoActividad);
                actividad.put("objetivo",objetivo.getText().toString());
                actividad.put("ubicacion",ubicacion.getSelectedItem().toString());
                if(ubicacion.getSelectedItem().toString() == "Estadal" && estado.getSelectedItem() != null )
                {
                    actividad.put("estado",estado.getSelectedItem().toString());
                    actividad.put("municipio",municipio.getSelectedItem().toString());
                    actividad.put("parroquia",parroquia.getText().toString());
                }
                actividad.put("encargado",encargado.getText().toString());
                actividad.put("creador",usuarioActual);
                actividad.put("estatus","En Ejecución");
                // Declare Date Format
                DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                try{
                    actividad.put("inicio",df.parse(inicio.getText().toString()));
                    actividad.put("fin",df.parse(fin.getText().toString()));
                } catch (java.text.ParseException e){
                    dialog.dismiss();
                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                }

                actividad.put("meGusta",0);

                actividad.saveInBackground(new SaveCallback() {
                    public void done(ParseException e) {
                        if (e == null) {
                            dialog.dismiss();
                            Toast.makeText(getActivity(), "Actividad Creada", Toast.LENGTH_SHORT).show();
                            // Redirect View to Boletin de Actividades
                            Fragment fragment = new FragmentListarActividad();
                            getFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.content_frame, fragment)
                                    .commit();
                        } else {
                            dialog.dismiss();
                            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        cancelar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0){
                // Redirect View to list
                Fragment fragment = new FragmentListarActividad();
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
