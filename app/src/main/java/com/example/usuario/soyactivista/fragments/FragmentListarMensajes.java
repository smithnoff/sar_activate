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

import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;

import logica.ListarActividadAdapter;
import logica.ListarMensajesAdapter;
import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by root on 26/11/15.
 */
public class FragmentListarMensajes extends Fragment {


    private ListarMensajesAdapter listarMensajesAdapter;
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Inflate View
        View view = inflater.inflate(R.layout.fragment_listar_mensajes, container, false);

        // Initialize main ParseQueryAdapter
        listarMensajesAdapter = new ListarMensajesAdapter(this.getContext());

        // Initialize list view
        listView = (ListView) view.findViewById(R.id.mensajesListView);

        if (listarMensajesAdapter != null) {
            listView.setAdapter(listarMensajesAdapter);
            listarMensajesAdapter.loadObjects();
        } else {
            Log.d("ADAPTER", "Adapter returned null!");
        }

        // Handle Item OnClick Events
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Store data in bundle to send to next fragment
                ParseObject mensaje = (ParseObject) listView.getItemAtPosition(position);
                ParseUser autor = mensaje.getParseUser("autor");
                ParseFile adjunto;

                adjunto = mensaje.getParseFile("adjunto");

                SimpleDateFormat format = new SimpleDateFormat("HH:mm dd/MM/yyyy");

                Bundle datos = new Bundle();
                datos.putString("id", mensaje.getObjectId());
                datos.putString("nombre", autor.getString("nombre") + " " + autor.getString("Apellido"));
                datos.putString("estado",autor.getString("estado"));
                datos.putString("municipio", autor.getString("municipio"));
                datos.putString("texto", mensaje.getString("texto"));
                datos.putString("ubicacion", mensaje.getString("ubicacion"));
                datos.putString("fechaCreacion", format.format(mensaje.getCreatedAt()));
                datos.putBoolean("reportado",mensaje.getBoolean("reportado"));

                // Check if images are null and save URLs
                if (adjunto != null)
                    datos.putString("adjunto", adjunto.getUrl());

                // Redirect View to next Fragment
                Fragment fragment = new FragmentDetalleMensaje();
                fragment.setArguments(datos);
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        FloatingActionButton botonCrearMensaje = (FloatingActionButton) view.findViewById(R.id.botonCrearMensaje);

        // Create new Activity Type Button
        botonCrearMensaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new FragmentEscribirMensaje();
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
