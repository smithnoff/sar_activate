package com.example.usuario.soyactivista.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by Brahyam on 30/11/2015.
 */
public class FragmentEditarPartido extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){

        //Choose fragment to inflate
        View v = inflater.inflate(R.layout.fragment_editar_partido, container, false);

        // TODO: Implement Theme Editing.

        return v;
    }

}
