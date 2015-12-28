package com.example.usuario.soyactivista.fragments;


import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;



import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by Luis Adrian on 28/12/2015.
 */
public class FragmentTriviaPrincipal extends Fragment{

    private Toolbar appbar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        //Choose fragment to inflate
        View v = inflater.inflate(R.layout.fragment_trivia_principal, container, false);

        appbar = (Toolbar)v.findViewById(R.id.appbar);

        return v;
    }



}
