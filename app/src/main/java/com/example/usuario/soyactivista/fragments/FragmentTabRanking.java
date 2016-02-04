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
import logica.Entidades;
import logica.ListarRankingEstadosAdapter;
import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by Luis Adrian on 19/01/2016.
 */
public class FragmentTabRanking extends Fragment{
    private ImageView bolivar;
    private LinearLayout parentLayout;
    private ImageView deltaAmacuro;
    private RecyclerView recyclerView;
    private List<Entidades> entidadesArrayList = new ArrayList<>();
    private View view, map;
    private RelativeLayout mapContainer;
    private Entidades entidades;
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

        initializeList(entidadesArrayList);

        return view;
    }

    public void initializeList(final List<Entidades> list){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("RankingEstados");
        query.orderByDescending("puntos");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> object, ParseException e) {
                if (e == null) { //no hay error

                    for (int i = 0; i < object.size(); i++) {
                        entidades = new Entidades();
                        String nombreEntidad = object.get(i).getString("nombre");
                        entidades.setnombreEntidad(object.get(i).getString("nombre"));
                        entidades.setPuntos(object.get(i).getInt("puntos"));
                        entidades.setCantidadUsuarios(object.get(i).getInt("usuarios"));
                        entidades.setIdEntidad(object.get(i).getString("objectId"));
                        list.add(entidades);
                    }
                    recyclerView.setAdapter(new ListarRankingEstadosAdapter(list));
                    colorMap();

                } else {
                    Toast.makeText(getActivity(), ErrorCodeHelper.resolveErrorCode(e.getCode()), Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    /* Función que recorre la lista de entidades que recuperamos de la BD, si el fragmento tiene
     argumentos es la lista de Estados si no es la lista de Municipios.

     Por cada uno busca el Id del ImageView ( Usando la funcion getResource usada en loadMap)

     y dependiendo de la cantidad de puntos lo llena con un color especifico.

     */

    private void colorMap() {
        TypedValue typedValue = new TypedValue();

        getActivity().getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);

        color = typedValue.data;

        alpha = Color.alpha(color);
        red = Color.red(color)-20;
        green = Color.green(color)-20;
        blue = Color.blue(color)-20;

        Toast.makeText(getActivity(),"El color es: "+red,Toast.LENGTH_LONG);

        /* Como obtener el recurso sin colocarlo a mano?
        estado = listaEstado.get(i);
        imageViewId = getResources().getIdentifier(estado.getName(), "id", getActivity().getPackageName());
        */
        deltaAmacuro = (ImageView)view.findViewById(R.id.DeltaAmacuro);
        deltaAmacuro.setColorFilter(Color.argb(alpha,red,green,blue));
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

    public int changeAlpha(int alpha)
    {
        alpha = Color.alpha(color)-10;
        return alpha;
    }
    public int changeRed(int red)
    {
        red = Color.red(color)-10;
        return red;
    }
    public int changeGreen(int green)
    {
        green = Color.green(color)-10;
        return green;
    }
    public int changeBlue(int blue)
    {
        blue = Color.blue(color)-10;
        return blue;
    }

}
