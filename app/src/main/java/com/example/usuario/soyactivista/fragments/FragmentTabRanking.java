package com.example.usuario.soyactivista.fragments;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import logica.ColorHelpers;
import logica.ErrorCodeHelpers;
import logica.Entidad;
import logica.ListarRankingEntidadesAdapter;
import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by Luis Adrian on 19/01/2016.
 */
public class FragmentTabRanking extends Fragment{

    private static final String TAG = "FragTabRanking";
    private RecyclerView recyclerView; // List Holder
    private RelativeLayout mapContainer; // Map Container
    private View map; // Map Holder
    private View view; // Main Layout

    // List Vars
    private ListarRankingEntidadesAdapter adapter;
    private ArrayList<Entidad> entidadArrayList;

    private int alpha, red, green, blue, color;
    //private int color = R.attr.colorPrimary;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_tab_top_5, container, false);

        mapContainer = (RelativeLayout) view.findViewById(R.id.map_container);

        loadMap();

        // Inititalize Entity Rank List

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        entidadArrayList = new ArrayList<>();

        adapter = new ListarRankingEntidadesAdapter(entidadArrayList);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_top_5);

        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(adapter);

        // Download entities from DB

        initializeList();

        return view;
    }

    public void initializeList(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("RankingEstados");
        query.orderByDescending("puntos");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> object, ParseException e) {
                if (e == null) { //no hay error
                    Log.d(TAG, object.size() + " entities retrieved");
                    for (int i = 0; i < object.size(); i++) {
                        Entidad entidad = new Entidad();
                        entidad.setNombre(object.get(i).getString("nombre"));
                        entidad.setPuntos(object.get(i).getInt("puntos"));
                        entidad.setUsuarios(object.get(i).getInt("usuarios"));
                        entidad.setId(object.get(i).getObjectId());
                        entidadArrayList.add(entidad);
                        colorEntity(entidad.getNombre(), entidad.getPuntos());
                    }
                    // Reload List
                    adapter.notifyDataSetChanged();

                } else {
                    // Log & display error
                    Log.d(TAG, ErrorCodeHelpers.resolveLogErrorString(e.getCode(), e.getMessage()));
                    Toast.makeText(getActivity(), ErrorCodeHelpers.resolveErrorCode(e.getCode()), Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void colorEntity(String nombre, int puntos) {

        // Get Resource ID
        int imageViewId = getResources().getIdentifier(nombre.toLowerCase().replace(" ","_"), "id",getActivity().getPackageName());
        ImageView vector = (ImageView) map.findViewById(imageViewId);

        // Get Primary Color
        TypedValue typedValue = new TypedValue();
        getContext().getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        int color = typedValue.data;

        int finalColor = color;

        // Get color tone
        if ( puntos > 6000 && puntos <= 7999)
            finalColor = ColorHelpers.lighten(color, 0.2);

        if ( puntos > 4000 && puntos <= 5999)
            finalColor = ColorHelpers.lighten(color, 0.4);

        if ( puntos > 1000 && puntos <= 3999)
            finalColor = ColorHelpers.lighten(color, 0.6);

        if ( puntos > 0 && puntos <= 999)
            finalColor = ColorHelpers.lighten(color, 0.8);

        // Paint with Primary Color.
        vector.setColorFilter(finalColor, PorterDuff.Mode.SRC_IN);
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
