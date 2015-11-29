package com.example.usuario.soyactivista.fragments;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import logica.ListarMensajesAdapter;
import logica.Mensaje;
import logica.Usuario;
import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by root on 26/11/15.
 */
public class FragmentListarMensajes extends Fragment {

    View vistaListado;

    // Card View Elements
    RecyclerView recyclerView;


    // Data Source
    List<Mensaje> mensajes;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        vistaListado = inflater.inflate(R.layout.fragment_listar_mensajes, container, false);

        recyclerView = (RecyclerView)vistaListado.findViewById(R.id.vista_listar_mensajes);
        recyclerView.setHasFixedSize(true);

        // TODO: In previous fragment is using getActviity() instead of getContext(). Check!
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        initializeData();

        return vistaListado;


    }

    // Query for data from Parse.com to initialize message list.
    private void initializeData(){

        mensajes = new ArrayList<>();
        Log.d(getActivity().getClass().getName(), "Initializing Data");
        // Query for 10 top recent Messages including author information
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Mensaje");
        query.include("autor");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> mensajesParse, ParseException e) {
                if (e == null) { //no hay error
                    Log.d(getActivity().getClass().getName(), "Fragment Retrieved " + mensajesParse.size() + " mensajes");
                    mensajes = new ArrayList<>();
                    for (ParseObject mensajeBuscado : mensajesParse) {

                        ParseUser autorBuscado = mensajeBuscado.getParseUser("autor");

                        Usuario autor = new Usuario();
                        autor.setNombre(autorBuscado.getString("nombre"));
                        autor.setApellido(autorBuscado.getString("apellido"));
                        autor.setEstado(autorBuscado.getString("estado"));
                        autor.setMuncipio(autorBuscado.getString("municipio"));

                        Mensaje mensaje = new Mensaje();
                        mensaje.setTexto(mensajeBuscado.getString("texto"));
                        mensaje.setAutor(autor);

                        mensajes.add(mensaje);
                    }

                    initializeAdapter();
                } else {
                    // TODO: Something Went Wrong
                    Log.d(getActivity().getClass().getName(), "Error!!!");
                }
            }
        });
        Log.d(getActivity().getClass().getName(), "Finished Querying");
    }

    private void initializeAdapter(){
        Log.d(getActivity().getClass().getName(), "Initializing Adapter");
        ListarMensajesAdapter listarMensajesAdapter = new ListarMensajesAdapter(mensajes);
        recyclerView.setAdapter(listarMensajesAdapter);
    }
}
