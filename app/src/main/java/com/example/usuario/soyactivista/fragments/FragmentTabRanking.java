package com.example.usuario.soyactivista.fragments;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import logica.ErrorCodeHelper;
import logica.Entidad;
import logica.ListarRankingEntidadesAdapter;
import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by Luis Adrian on 19/01/2016.
 */
public class FragmentTabRanking extends Fragment{
    private ImageView bolivar;
    private LinearLayout parentLayout;
    private ImageView deltaAmacuro;
    private RecyclerView recyclerView;
    private ArrayList<Entidad> entidadArrayList = new ArrayList<>();
    private View view, map;
    private RelativeLayout mapContainer;
    private Entidad entidad;
    private int alpha, red, green, blue, color;
    //private int color = R.attr.colorPrimary;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_tab_ranking, container, false);

        mapContainer = (RelativeLayout) view.findViewById(R.id.map_container);

        loadMap();

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_ranking);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());

        llm.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(llm);

        initializeList(entidadArrayList);

        return view;
    }

    public void initializeList(final ArrayList<Entidad> list){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("RankingEstados");
        query.orderByDescending("puntos");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> object, ParseException e) {
                if (e == null) { //no hay error

                    for (int i = 0; i < object.size(); i++) {
                        entidad = new Entidad();
                        String nombreEntidad = object.get(i).getString("nombre");
                        entidad.setNombre(object.get(i).getString("nombre"));
                        entidad.setPuntos(object.get(i).getInt("puntos"));
                        entidad.setUsuarios(object.get(i).getInt("usuarios"));
                        entidad.setId(object.get(i).getString("objectId"));
                        list.add(entidad);
                        colorMap(i);
                    }
                    recyclerView.setAdapter(new ListarRankingEntidadesAdapter(list));


                } else {
                    Toast.makeText(getActivity(), ErrorCodeHelper.resolveErrorCode(e.getCode()), Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void colorMap(int i ) {
        TypedValue typedValue = new TypedValue();
        getActivity().getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        color = typedValue.data;
        alpha = Color.alpha(color);
        red = Color.red(color);
        green = Color.green(color);
        blue = Color.blue(color);
        Entidad entidades = entidadArrayList.get(i);

        int imageViewId = 0;
        if (getArguments() != null) {
            String estado = getArguments().getString("estado");
            if (estado != null && !TextUtils.isEmpty(estado)) {

            } else {

            }
        }
        else {
            String nombreEntidad = entidades.getNombre();
            imageViewId = getResources().getIdentifier(nombreEntidad.toLowerCase().replaceAll("\\s+", "_"), "id", getActivity().getPackageName());
        }

        ImageView entidad = (ImageView) view.findViewById(imageViewId);

        if (red > blue && red > green)
        {
            red = asignarTono(entidades.getPuntos(),red-80);
        }
        else
        {
            if(blue > green && blue > red)
            {
                blue = asignarTono(entidades.getPuntos(),blue-80);
            }
            else
                green=asignarTono(entidades.getPuntos(),green-80);

        }


        entidad.setColorFilter(Color.argb(alpha, red, green, blue));
    }

    public int asignarTono(int x,int color)
    {
            int tono=color;

            if(x>=1000 && x<4000 )
            tono=color+20;

            if(x>=4000 && x<6000 )
            tono=color+40;

            if(x>=6000 && x<8000 )
            tono=color+60;

            if(x>=8000 )
            tono=color+80;

            return tono;
    }

    // Map is loaded according to arguments whether it is at national or state level.
    private void loadMap() {

        int layoutId = 0;
        int containerId = 0;

        if( getArguments() != null ){
            String estado = getArguments().getString("estado");
            if( estado != null && !TextUtils.isEmpty(estado)){
                // Load State map
                layoutId = getResources().getIdentifier("map_"+estado, "layout", getActivity().getPackageName());

                containerId = getResources().getIdentifier("map_"+estado, "id",getActivity().getPackageName());
            }
            else
            {
                // State not found
                // Load Venezuela Map.
                layoutId = getResources().getIdentifier("map_venezuela", "layout", getActivity().getPackageName());
                containerId = getResources().getIdentifier("map_venezuela", "id",getActivity().getPackageName());
            }
        }
        else{
            // Load Venezuela Map.
            layoutId = getResources().getIdentifier("map_venezuela", "layout", getActivity().getPackageName());
            containerId = getResources().getIdentifier("map_venezuela", "id",getActivity().getPackageName());
        }

        LayoutInflater infl = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        map = infl.inflate(layoutId, (ViewGroup) view.findViewById(containerId));

        mapContainer.addView(map);
    }



}
