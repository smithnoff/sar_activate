package com.example.usuario.soyactivista.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by Luis Adrian on 29/12/2015.
 */
public class FragmentEstadisticaUsuario extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {


        //Choose fragment to inflate
        View v = inflater.inflate(R.layout.fragment_estadistica_usuario, container, false);


        return v;
    }
}
