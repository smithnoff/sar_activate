package com.example.usuario.soyactivista.fragments;
import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.util.ArrayList;

import logica.Estados;
import logica.ListarRankingEstadosAdapter;
import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by Luis Adrian on 19/01/2016.
 */
public class FragmentTabRankingNacional extends Fragment{
    private ImageView bolivar;
    private LinearLayout parentLayout;
    private ImageView deltaAmacuro;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tab_ranking_nacional, container, false);

        parentLayout = (LinearLayout)v.findViewById(R.id.parentLayout);

        LayoutInflater infl = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View childLayout = infl.inflate(R.layout.map_venezuela, (ViewGroup) v.findViewById(R.id.venezuelaMap));
        parentLayout.addView(childLayout);

        deltaAmacuro = (ImageView)childLayout.findViewById(R.id.deltaAmacuro);
        deltaAmacuro.setColorFilter(Color.argb(255, 51, 51, 255));



        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.recyclerListTopEstados);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        recyclerView.setAdapter(new ListarRankingEstadosAdapter(generateEstados()));



        return v;
    }

    private ArrayList<Estados> generateEstados() {
        ArrayList<Estados> estados = new ArrayList<>();
        int posicion = 0;
        estados.add(new Estados("Amazonas",posicion));
        estados.add(new Estados("Anzoategui",posicion));
        estados.add(new Estados("Apure",posicion));
        estados.add(new Estados("Aragua",posicion));
        estados.add(new Estados("Area Metropolitana de Caracas",posicion));
        estados.add(new Estados("Barinas",posicion));
        estados.add(new Estados("Bolivar",posicion));
        estados.add(new Estados("Carabobo",posicion));
        estados.add(new Estados("Cojedes",posicion));
        estados.add(new Estados("Falcon",posicion));
        estados.add(new Estados("Guarico",posicion));
        estados.add(new Estados("Lara",posicion));
        estados.add(new Estados("Merida",posicion));
        estados.add(new Estados("Miranda",posicion));
        estados.add(new Estados("Monagas",posicion));
        estados.add(new Estados("Nueva Esparta",posicion));
        estados.add(new Estados("Portuguesa",posicion));
        estados.add(new Estados("Sucre",posicion));
        estados.add(new Estados("Tachira",posicion));
        estados.add(new Estados("Trujillo",posicion));
        estados.add(new Estados("Vargas",posicion));
        estados.add(new Estados("Yaracuy",posicion));
        estados.add(new Estados("Zulia",posicion));
        return estados;
    }



}
