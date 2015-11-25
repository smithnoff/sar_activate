package com.example.usuario.soyactivista.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import logica.FActivityPantallaMenu;
import logica.Mensaje;
import logica.MensajeAdapter;
import logica.Usuario;
import soy_activista.quartzapp.com.soy_activista.R;


/**
 * Created by Usuario on 07/10/2015.
 */
public class FragmentDashBoard extends Fragment {
    private FloatingActionButton fabBoton;

    public FragmentDashBoard(){}

    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;

    //iniciar lista
    List<Mensaje> items = new ArrayList<>();
    Usuario usr;
    View v;
    Bitmap bitmap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final ProgressDialogFragment pg = new ProgressDialogFragment();
        pg.setTitulo("Listando");
        pg.setMensajeCargando("Consultando...");
        pg.show(getFragmentManager(),"cargando");
        v = inflater.inflate(R.layout.fragment_dashboard, container, false);

        fabBoton=(FloatingActionButton)v.findViewById(R.id.fabMensajeNuevo);
        fabBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentEscribirMensaje fragMensaje  = new FragmentEscribirMensaje();
                getFragmentManager().beginTransaction().replace(R.id.content_frame, fragMensaje).commit();
            }
        });

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Mensajes");
        query.include("Autor");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) { //no hay error
                    for (int i = 0; i < objects.size(); i++) {
                        usr = new Usuario();
                        usr.setIdentificador(objects.get(i).getParseObject("Autor").getString("username"));
                        usr.setNombre(objects.get(i).getParseObject("Autor").getString("Nombre"));
                        usr.setApellido(objects.get(i).getParseObject("Autor").getString("Apellido"));
                        usr.setMuncipio(objects.get(i).getParseObject("Autor").getString("Municipio"));
                        usr.setEstado(objects.get(i).getParseObject("Autor").getString("Estado"));

                        ParseFile imagen = objects.get(i).getParseFile("Imagen");

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

                        items.add(new Mensaje(usr, objects.get(i).getString("Mensaje"), bitmap));
                    }
                    pg.dismiss();
                } else {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    pg.dismiss();
                }
            }
        });

        // Obtener el Recycler
        recycler = (RecyclerView) v.findViewById(R.id.reciclador);
        // Usar un administrador para LinearLayout
        lManager = new LinearLayoutManager(getActivity());
        recycler.setLayoutManager(lManager);
        // Crear un nuevo adaptador
        adapter = new MensajeAdapter(items);
        recycler.setAdapter(adapter);

        return v;
    }
}
/*View image = v.findViewById(R.id.imagenAdjuntado);
           image.setVisibility(View.GONE);*/