package com.example.usuario.soyactivista.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by RSMAPP on 28/12/2015.
 */
public class FragmentCrearPreguntas extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crear_preguntas, container, false);




        return v;
    }
}
