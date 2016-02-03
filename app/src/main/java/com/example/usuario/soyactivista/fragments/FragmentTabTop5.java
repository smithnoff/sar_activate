package com.example.usuario.soyactivista.fragments;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import logica.ErrorCodeHelper;
import logica.Estado;
import logica.ListarRankingEstadosAdapter;
import soy_activista.quartzapp.com.soy_activista.R;
/**
 * Created by Luis Adrian on 19/01/2016.
 */
public class FragmentTabTop5 extends Fragment {
    private static final String TAG = "FragmentTabTop5";
    private RecyclerView recyclerView;
    private List<Estado> estadoArrayList = new ArrayList<>();
    private Estado estado;
    private RelativeLayout mapContainer;
    private View map, view;

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

        view = inflater.inflate(R.layout.fragment_tab_top_5, container, false);

        mapContainer = (RelativeLayout) view.findViewById(R.id.map_container);

        loadMap();

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_top_5);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());

        llm.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(llm);

        initializeList(estadoArrayList);

        return view;
    }


    private void fillTop5() {


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

    public void initializeList(final List<Estado> list){
        ParseQuery<ParseObject> query;

        if(getArguments() != null){
            // Generate state object without data
            ParseObject estado = ParseObject.createWithoutData("RankingEstado",getArguments().getString("estadoId"));
            query = ParseQuery.getQuery("RankingMunicipios");
            query.whereEqualTo("estado",estado);
        }
        else{
            query = ParseQuery.getQuery("RankingEstados");
        }

        query.setLimit(5);
        query.orderByDescending("puntos");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> object, ParseException e) {
                if (e == null) { //no hay error

                    for (int i = 0; i < object.size(); i++) {
                        estado = new Estado();
                        estado.setNombreEstado(object.get(i).getString("nombre"));
                        estado.setPuntos(object.get(i).getInt("puntos"));
                        estado.setPosicion(i+1);
                        list.add(estado);
                    }

                    recyclerView.setAdapter(new ListarRankingEstadosAdapter(list));

                    // Paint map using list of queried objects.
                    fillTop5();

                } else {

                    Log.d(TAG,ErrorCodeHelper.resolveLogErrorString(e.getCode(),e.getMessage()));
                    Toast.makeText(getActivity(), ErrorCodeHelper.resolveErrorCode(e.getCode()), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
