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

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

import logica.ErrorCodeHelpers;
import logica.Entidad;
import logica.ListarRankingEntidadesAdapter;
import logica.TextHelpers;
import soy_activista.quartzapp.com.soy_activista.R;
/**
 * Created by Luis Adrian on 19/01/2016.
 */
public class FragmentTabTop5 extends Fragment {

    private static final String TAG = "FragmentTabTop5"; //  For Log

    private RecyclerView recyclerView; // List Holder
    private RelativeLayout mapContainer; // Map Container
    private View map; // Map Holder
    private View view; // Main Layout

    // List Vars
    private ListarRankingEntidadesAdapter adapter;
    private ArrayList<Entidad> entidadArrayList;

    // Arguments
    private String entityName;
    private String entityId;


    public FragmentTabTop5() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG,"Loading Fragment Top 5");

        view = inflater.inflate(R.layout.fragment_tab_top_5, container, false);

        mapContainer = (RelativeLayout) view.findViewById(R.id.map_container);

        // Recover data from arguments
        if( getArguments() != null){
            Log.d(TAG,"Fragment has arguments");
            entityName = getArguments().getString("estado");
            entityId = getArguments().getString("estadoId");
        }

        loadMap();

        // Inititalize Entity Rank List

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        entidadArrayList = new ArrayList<>();

        adapter = new ListarRankingEntidadesAdapter(getActivity(),entidadArrayList);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_top_5);

        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(adapter);

        // Download entities from DB

        initializeList();

        return view;
    }


    // Map is loaded according to arguments whether it is at national or state level.
    private void loadMap() {

        int layoutId = 0;
        int containerId = 0;
        String resource = "map_venezuela";

        if( entityName != null ){
            resource = TextHelpers.NormalizeResource(getArguments().getString("estado"));

            // Look for resource id
            Log.d(TAG,"Looking for map file: map_"+resource);
            layoutId = getResources().getIdentifier("map_"+resource, "layout", getActivity().getPackageName());
            containerId = getResources().getIdentifier("map_"+resource, "id",getActivity().getPackageName());

        }
        else{
            // Look for resource id
            Log.d(TAG,"Looking for map file: map_venezuela");
            layoutId = getResources().getIdentifier(resource, "layout", getActivity().getPackageName());
            containerId = getResources().getIdentifier("map_venezuela", "id",getActivity().getPackageName());
        }

        LayoutInflater infl = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        map = infl.inflate(layoutId, (ViewGroup) view.findViewById(containerId));


        // Check if resource was found.
        if( map == null){
            // Map not found
            Toast.makeText(getContext(),"Map not found : "+resource,Toast.LENGTH_LONG).show();
            Log.e(TAG,"Map resource not found : map_"+resource);
        }
        else{
            // Map Found
            mapContainer.addView(map);
        }
    }

    public void initializeList(){
        // TODO: Save queried list in activity and ask for it on creation.

        ParseQuery<ParseObject> query;

        if(getArguments() != null){
            // Generate state object without data
            query = ParseQuery.getQuery("RankingMunicipios");
            query.whereEqualTo("estado",getArguments().getString("estado"));
            Log.d(TAG, "Querying for estado " + getArguments().getString("estado"));
        }
        else{
            query = ParseQuery.getQuery("RankingEstados");
        }

        query.setLimit(5);
        query.orderByDescending("puntos");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> object, ParseException e) {
                if (e == null) { //no hay error
                    Log.d(TAG, object.size() + " entities retrieved");
                    Entidad entidad;
                    for (int i = 0; i < object.size(); i++) {
                        entidad = new Entidad();
                        entidad.setId(object.get(i).getObjectId());
                        entidad.setNombre(object.get(i).getString("nombre"));
                        entidad.setPuntos(object.get(i).getInt("puntos"));
                        entidad.setUsuarios(object.get(i).getInt("usuarios"));
                        entidad.setPosicion(i + 1);
                        colorEntity(entidad.getNombre());
                        entidadArrayList.add(entidad);
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

    private void colorEntity(String nombre) {

        // Get Resource ID
        String finalName = TextHelpers.NormalizeResource(nombre);
        int imageViewId = getResources().getIdentifier("map_"+ finalName, "id",getActivity().getPackageName());
        ImageView vector = (ImageView) map.findViewById(imageViewId);

        // Check if image not found log & abort.
        if ( vector == null){
            Log.e(TAG,"Resource not found: map_"+finalName);
            return;
        }

        // Get Primary Color
        TypedValue typedValue = new TypedValue();
        getContext().getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        int color = typedValue.data;

        // Paint with Primary Color.
        vector.setColorFilter(color, PorterDuff.Mode.SRC_IN);
    }
}