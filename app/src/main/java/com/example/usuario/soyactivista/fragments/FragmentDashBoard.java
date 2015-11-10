package com.example.usuario.soyactivista.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

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
    //GoogleMap mapa;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       /* View v = inflater.inflate(R.layout.mapa_layout, container, false);


        /*GoogleMap mapa = ((SupportMapFragment) this.getFragmentManager()
                .findFragmentById(R.id.map)).getMap();
        mapa.setMapType(GoogleMap.MAP_TYPE_TERRAIN);*/
        View v = inflater.inflate(R.layout.fragment_dashboard, container, false);

        fabBoton=(FloatingActionButton)v.findViewById(R.id.fabMensajeNuevo);
        fabBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentEscribirMensaje fragMensaje  = new FragmentEscribirMensaje();
                getFragmentManager().beginTransaction().replace(R.id.content_frame, fragMensaje).commit();
            }
        });
            //iniciar lista
            List<Mensaje> items = new ArrayList<>();
            Usuario usr;
            for(int i=0; i<20; i++){
                usr=new Usuario();
                usr.setIdentificador("epica");
                usr.setNombre("emanuel");
                usr.setApellido("ortega");
                usr.setMuncipio("caroni");
                usr.setEstado("bolivar");
                items.add(new Mensaje(usr,"MENSAJE DE PRUEBA", R.drawable.ic_attach_file));
            }
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