package com.example.usuario.soyactivista.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import soy_activista.quartzapp.com.soy_activista.R;

import static java.lang.Integer.parseInt;

/**
 * Created by Brahyam on 28/11/2015.
 */
public class FragmentCrearTipoActividad extends Fragment {

    private EditText nombre, descripcion; // Edit Field holders
    private Spinner puntaje; // Spinner holders
    private Button crear,cancelar; // Button holders
    private ProgressDialog dialog;

    // Class Constructor
    public FragmentCrearTipoActividad(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        //Choose fragment to inflate
        View v = inflater.inflate(R.layout.fragment_crear_tipo_actividad, container, false);

        //Gets Current User
        final ParseUser usuarioActual = ParseUser.getCurrentUser();

        //Asign Text Edit to holders
        nombre = (EditText)v.findViewById(R.id.editActividad);
        descripcion = (EditText)v.findViewById(R.id.editDescripcion);

        // Asigns Spinners to holders
        puntaje = (Spinner)v.findViewById(R.id.spinPuntaje);

        // Asign Buttons to holders
        crear = (Button)v.findViewById(R.id.botonCrearTipoActividad);
        cancelar = (Button)v.findViewById(R.id.botonCancelar);

        //Fill Spinners with Preset Options
        this.llenarSpinnerdesdeId(puntaje, R.array.Puntuaciones);

        // Buttons Behavior
        crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if(nombre.getText().toString().trim().length()>0 && descripcion.getText().toString().trim().length()>0 ) {
                    dialog = ProgressDialog.show(getActivity(), "", "Creando Tipo de Actividad", true);

                    // Fill ParseObject to send
                    ParseObject tipoActividad = new ParseObject("TipoActividad");
                    tipoActividad.put("nombre", nombre.getText().toString());
                    tipoActividad.put("puntaje", parseInt(puntaje.getSelectedItem().toString()));
                    tipoActividad.put("descripcion", descripcion.getText().toString());
                    tipoActividad.put("creador", usuarioActual);

                    tipoActividad.saveInBackground(new SaveCallback() {
                        public void done(ParseException e) {
                            if (e == null) {
                                dialog.dismiss();
                                Toast.makeText(getActivity(), "Tipo de actividad creada", Toast.LENGTH_SHORT).show();
                                // Redirect View to Boletin de Actividades
                                Fragment fragment = new FragmentListarTipoActividad();
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
                else
                {
                    Toast.makeText(getContext(),"Completa los campos vacíos",Toast.LENGTH_SHORT).show();
                    return;
                }
            }

        });

        cancelar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0){
                // Redirect View to Boletin de Actividades
                Fragment fragment = new FragmentListarTipoActividad();
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
