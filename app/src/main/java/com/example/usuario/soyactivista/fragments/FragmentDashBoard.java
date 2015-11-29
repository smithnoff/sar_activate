package com.example.usuario.soyactivista.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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

import java.util.ArrayList;
import java.util.List;

import logica.Mensaje;
import logica.MensajeAdapter;
import logica.Usuario;
import soy_activista.quartzapp.com.soy_activista.R;


/**
 * Created by Usuario on 07/10/2015.
 */
public class FragmentDashBoard extends Fragment {
    private FloatingActionButton botonCrearMensaje;

    public FragmentDashBoard(){}

    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;

    //iniciar lista
    List<Mensaje> items = new ArrayList<>();
    Usuario usuario;
    View v;
    Bitmap bitmap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        final ProgressDialogFragment pg = new ProgressDialogFragment();
        pg.setTitulo("Listando");
        pg.setMensajeCargando("Consultando...");
        pg.show(getFragmentManager(), "cargando");

        v = inflater.inflate(R.layout.fragment_dashboard, container, false);

        botonCrearMensaje =(FloatingActionButton)v.findViewById(R.id.fabMensajeNuevo);

        // CREATE MESSAGE BUTTON
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

        // Obtener el Recycler
        recycler = (RecyclerView) v.findViewById(R.id.reciclador);

        // Usar un administrador para LinearLayout
        lManager = new LinearLayoutManager(getActivity());
        recycler.setLayoutManager(lManager);

        // Query for Messages
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Mensaje");
        query.include("autor");
        //Execute
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> mensajes, ParseException e) {
                if (e == null) { //no hay error
                    Log.d(getActivity().getClass().getName(), "Retrieved " + mensajes.size() + " mensajes");
                    for (int i = 0; i < mensajes.size(); i++) {
                        usuario = new Usuario();
                        usuario.setIdentificador(mensajes.get(i).getParseObject("autor").getString("username"));
                        usuario.setNombre(mensajes.get(i).getParseObject("autor").getString("nombre"));
                        usuario.setApellido(mensajes.get(i).getParseObject("autor").getString("apellido"));
                        usuario.setMuncipio(mensajes.get(i).getParseObject("autor").getString("municipio"));
                        usuario.setEstado(mensajes.get(i).getParseObject("autor").getString("estado"));

                        ParseFile imagen = mensajes.get(i).getParseFile("adjunto");

                        if (imagen != null) {
                            try {
                                byte[] imagenSubida = imagen.getData();

                                bitmap = BitmapFactory.decodeByteArray(imagenSubida, 0, imagenSubida.length);
                            } catch (ParseException e1) {
                                e1.printStackTrace();
                            }
                        } else {
                            bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.ic_attach_file);
                        }

                        items.add(new Mensaje(usuario, mensajes.get(i).getString("mensaje"), bitmap));
                    }
                    pg.dismiss();

                    // Crear un nuevo adaptador
                    adapter = new MensajeAdapter(items);
                    recycler.setAdapter(adapter);

                } else {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    pg.dismiss();
                }
            }
        });
        return v;
    }
}