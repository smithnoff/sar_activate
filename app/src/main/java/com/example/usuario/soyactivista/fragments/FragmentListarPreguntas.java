package com.example.usuario.soyactivista.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.parse.ParseObject;

import logica.listarPreguntaParseAdapter;
import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by darwin on 28/12/2015.
 */
public class FragmentListarPreguntas extends Fragment {
    private ListView listView;
    private listarPreguntaParseAdapter mainAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate View
        View view = inflater.inflate(R.layout.fragment_listar_preguntas, container, false);

        // Initialize main ParseQueryAdapter
        mainAdapter = new listarPreguntaParseAdapter(this.getContext());

        // Initialize list view
        listView = (ListView)view.findViewById(R.id.listaPreguntas);

        mainAdapter.clear();
        listView.setAdapter(mainAdapter);
        mainAdapter.loadObjects();

        // Handle Item OnClick Events
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Store data in bundle to send to next fragment
                ParseObject actividad = (ParseObject)listView.getItemAtPosition(position);

                Bundle datos = new Bundle();
                datos.putString("id",actividad.getObjectId());
                datos.putString("pregunta",actividad.getString("pregunta"));
                datos.putString("opcion1",actividad.getString("opcion1"));
                datos.putString("opcion2",actividad.getString("opcion2"));
                datos.putString("opcion3",actividad.getString("opcion3"));
                datos.putString("opcion4",actividad.getString("opcion4"));
                datos.putString("dificultad",actividad.getString("dificultad"));
                datos.putString("puntaje", Integer.toString(actividad.getInt("puntaje")));

                // Redirect View to next Fragment
                Fragment fragment = new FragmentEditarTipoActividad();
                fragment.setArguments(datos);
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });


        FloatingActionButton botonAgregarActividad =(FloatingActionButton)view.findViewById(R.id.botonAgregarPregunta);

        // Create new Activity Type Button
        botonAgregarActividad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new FragmentCrearTipoActividad();
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;

    }
}
