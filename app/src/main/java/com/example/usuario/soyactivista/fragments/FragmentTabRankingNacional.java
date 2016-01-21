package com.example.usuario.soyactivista.fragments;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import soy_activista.quartzapp.com.soy_activista.R;
import logica.ColorFilterGenerator;
/**
 * Created by Luis Adrian on 19/01/2016.
 */
public class FragmentTabRankingNacional extends Fragment{
private ImageView bolivar;
    public FragmentTabRankingNacional() {
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
        View v = inflater.inflate(R.layout.fragment_tab_ranking_nacional, container, false);
        bolivar = (ImageView)v.findViewById(R.id.bolivar);
       
        bolivar.setColorFilter(R.color.red900,PorterDuff.Mode.SRC_ATOP);



        return v;
    }

}
