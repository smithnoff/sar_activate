package com.example.usuario.soyactivista.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import soy_activista.quartzapp.com.soy_activista.R;


public class FragmentDetalleMensaje extends Fragment {
    View v;
    LinearLayout padre;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_detalle_mensaje, container, false);
        padre=(LinearLayout)v.findViewById(R.id.LinearMensajeDetallaAdjunto);

        return v;
    }



}
