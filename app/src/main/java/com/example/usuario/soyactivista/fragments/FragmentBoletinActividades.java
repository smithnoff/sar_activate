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
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import logica.Actividad;
import logica.ActividadAdapter;
import logica.Mensaje;
import logica.MensajeAdapter;
import logica.Usuario;
import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by Joisacris on 25/11/2015.
 */
public class FragmentBoletinActividades extends Fragment {
    private FloatingActionButton fabBoton;

    public FragmentBoletinActividades(){}

    View v;
    //iniciar lista
    ArrayList<Actividad> items = new ArrayList<>();
    Bitmap bitmap1,bitmap2,bitmap3,bitmap4;

    ListView ListaActividadesView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final ProgressDialogFragment pg = new ProgressDialogFragment();
        pg.setTitulo("Listando");
        pg.setMensajeCargando("Consultando...");
        pg.show(getFragmentManager(), "cargando");
        v = inflater.inflate(R.layout.fragment_listar_boletin_actividades, container, false);

        ListaActividadesView=(ListView)v.findViewById(R.id.listActividadesView);

        fabBoton=(FloatingActionButton)v.findViewById(R.id.fabMensajeNuevo);
        fabBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Actividad");
        query.include("creador");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) { //no hay error
                    Toast.makeText(getActivity(), String.valueOf(objects.size()), Toast.LENGTH_SHORT).show();
                    for (int i = 0; i < objects.size(); i++) {

                        items.add(new Actividad(objects.get(i).getString("nombre"), objects.get(i).getString("estatus"), objects.get(i).getParseObject("creador").getString("username"), objects.get(i).getDate("inicio"), objects.get(i).getDate("fin")));
                    }

                    ListaActividadesView.setAdapter(null);
                    ActividadAdapter adaptador;
                    adaptador = new ActividadAdapter(getActivity(), items, R.layout.item_card_boletin_actividades);
                    ListaActividadesView.setAdapter(adaptador);

                    pg.dismiss();
                } else {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    pg.dismiss();
                }
            }
        });

        /*for (int i = 0; i < 10; i++) {
            //usr.setIdentificador(objects.get(i).getParseObject("Autor").getString("username"));

            Calendar calendario = GregorianCalendar.getInstance();
            Date inicio = calendario.getTime();

            items.add(new Actividad("Nombre Actividad", "Terminado", "Joisamar Garcia", inicio, inicio));
        }
        pg.dismiss();*/



        return v;
    }
}
