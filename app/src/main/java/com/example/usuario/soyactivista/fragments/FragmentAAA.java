package com.example.usuario.soyactivista.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by Luis Adrian on 14/01/2016.
 */
public class FragmentAAA extends Fragment {
private ImageView bolivar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.aaexample, container, false);
        bolivar = (ImageView)v.findViewById(R.id.imageViewVen);
        //bolivar.setColorFilter(R.color.red900);
        //bolivar.setColorFilter(R.color.black);
        //loadImageFromURL("http://vignette3.wikia.nocookie.net/future/images/7/76/Flag_of_Venezuela.png",bolivar);

        return v;
    }

}
