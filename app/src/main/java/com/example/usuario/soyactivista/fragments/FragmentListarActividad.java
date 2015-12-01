package com.example.usuario.soyactivista.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.parse.Parse;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;

import logica.ListarActividadAdapter;
import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by Brahyam on 28/11/2015.
 */
public class FragmentListarActividad extends Fragment {

    private ListarActividadAdapter listarActividadAdapter;
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Inflate View
        View view = inflater.inflate(R.layout.fragment_listar_actividades, container, false);

        // Initialize main ParseQueryAdapter
        listarActividadAdapter = new ListarActividadAdapter(this.getContext());

        // Initialize list view
        listView = (ListView)view.findViewById(R.id.actividadesListView);

        if(listarActividadAdapter !=null){
            Log.d("ADAPTER", "Adapter is not null!");
            listView.setAdapter(listarActividadAdapter);
            listarActividadAdapter.loadObjects();
        }
        else{
            Log.d("ADAPTER", "Adapter returned null!");
        }

        // Handle Item OnClick Events
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Store data in bundle to send to next fragment
                ParseObject actividad = (ParseObject)listView.getItemAtPosition(position);
                ParseObject tipoActividad = actividad.getParseObject("tipoActividad");
                ParseUser creador = actividad.getParseUser("creador");
                ParseFile imagen1,imagen2,imagen3,imagen4;

                imagen1 = actividad.getParseFile("imagen1");
                imagen2 = actividad.getParseFile("imagen2");
                imagen3 = actividad.getParseFile("imagen3");
                imagen4 = actividad.getParseFile("imagen4");

                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

                Bundle datos = new Bundle();
                Log.d("LISTAR", "Valor MeGusta: "+actividad.getObjectId());
                datos.putString("id",actividad.getObjectId());
                datos.putString("tipoId",tipoActividad.getObjectId());
                datos.putString("nombre", tipoActividad.getString("nombre"));
                datos.putString("descripcion",tipoActividad.getString("descripcion"));
                datos.putString("puntaje", Integer.toString(tipoActividad.getInt("puntaje")));
                datos.putString("objetivo",actividad.getString("objetivo"));
                datos.putString("encargado",actividad.getString("encargado"));
                datos.putString("creadorId",creador.getObjectId());
                datos.putString("creador",creador.getString("nombre")+" "+creador.getString("apellido"));
                datos.putString("estatus",actividad.getString("estatus"));
                datos.putString("inicio",format.format(actividad.getDate("inicio")));
                datos.putString("fin", format.format(actividad.getDate("fin")));
                Log.d("LISTAR", "Valor MeGusta: "+actividad.getInt("meGusta"));
                datos.putInt("meGusta",actividad.getInt("meGusta"));

                // Check if images are null and save URLs
                if(imagen1 != null)
                    datos.putString("imagen1",imagen1.getUrl());
                if(imagen2 != null)
                    datos.putString("imagen2",imagen1.getUrl());
                if(imagen3 != null)
                    datos.putString("imagen3",imagen1.getUrl());
                if(imagen4 != null)
                    datos.putString("imagen4",imagen1.getUrl());


                // Redirect View to next Fragment
                Fragment fragment = new FragmentDetalleActividad();
                fragment.setArguments(datos);
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        FloatingActionButton botonCrearActividad =(FloatingActionButton)view.findViewById(R.id.botonCrearActividad);

        // Create new Activity Type Button
        botonCrearActividad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new FragmentCrearActividad();
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
