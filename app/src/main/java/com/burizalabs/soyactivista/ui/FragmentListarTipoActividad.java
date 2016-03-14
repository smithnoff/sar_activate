package com.burizalabs.soyactivista.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.parse.ParseObject;

import com.burizalabs.soyactivista.adapters.ListarTipoActividadAdapter;
import com.burizalabs.soyactivista.R;

/**
 * Created by Brahyam on 28/11/2015.
 */
public class FragmentListarTipoActividad extends Fragment {

    private ListarTipoActividadAdapter mainAdapter;
    private ListView listView;
    private boolean alreadyLoaded;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate View
        View view = inflater.inflate(R.layout.fragment_listar_tipo_actividad, container, false);

        // Initialize main ParseQueryAdapter
        mainAdapter = new ListarTipoActividadAdapter(this.getContext());

        // Initialize list view
        listView = (ListView)view.findViewById(R.id.listaTipoActividad);

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
                datos.putString("nombre",actividad.getString("nombre"));
                datos.putString("descripcion",actividad.getString("descripcion"));
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


        FloatingActionButton botonAgregarActividad =(FloatingActionButton)view.findViewById(R.id.botonAgregarActividad);

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