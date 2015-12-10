package com.example.usuario.soyactivista.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;

import logica.ListarMensajeAdapter;
import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by root on 26/11/15.
 */
public class FragmentListarMensaje extends Fragment  {


    private ListarMensajeAdapter listarMensajeAdapter;
    private ListView listView;
    private Spinner spEstado,spParroquia;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Inflate View
        View view = inflater.inflate(R.layout.fragment_listar_mensaje, container, false);

        // Initialize main ParseQueryAdapter
        listarMensajeAdapter = new ListarMensajeAdapter(this.getContext());

        // Initialize list view
        listView = (ListView) view.findViewById(R.id.mensajesListView);
        spEstado= (Spinner) view.findViewById(R.id.spinnerEstados);
          llenarSpinnerdesdeId(spEstado,R.array.Estados);



        if (listarMensajeAdapter != null) {
            listView.setAdapter(listarMensajeAdapter);
            listarMensajeAdapter.loadObjects();
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
                ParseGeoPoint ubicacion;

                adjunto = mensaje.getParseFile("adjunto");
                ubicacion = mensaje.getParseGeoPoint("ubicacion");


                SimpleDateFormat format = new SimpleDateFormat("HH:mm dd/MM/yyyy");

                Bundle datos = new Bundle();
                datos.putString("id", mensaje.getObjectId());
                datos.putString("nombre", autor.getString("nombre") + " " + autor.getString("apellido"));
                datos.putString("estado",autor.getString("estado"));
                datos.putString("municipio", autor.getString("municipio"));
                datos.putString("texto", mensaje.getString("texto"));
                datos.putString("fechaCreacion", format.format(mensaje.getCreatedAt()));
                datos.putBoolean("reportado",mensaje.getBoolean("reportado"));
                datos.putString("autor",autor.getObjectId());

                // Check if images are null and save URLs
                if (adjunto != null){
                    datos.putString("nombreAdjunto",adjunto.getName());
                    datos.putString("adjunto", adjunto.getUrl());
                }

                // Check if location available
                if (ubicacion != null){
                    Log.d("ENVIO", ubicacion.toString());
                    datos.putString("ubicacion", String.valueOf(ubicacion.getLatitude())+","+String.valueOf(ubicacion.getLongitude()));
                }


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

                Fragment fragment = new FragmentCrearMensaje();
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }
    public void llenarSpinnerdesdeId(Spinner spin,int id){
        ArrayAdapter spinner_adapter = ArrayAdapter.createFromResource(getActivity(), id, android.R.layout.simple_spinner_item);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(spinner_adapter);
    }



}
