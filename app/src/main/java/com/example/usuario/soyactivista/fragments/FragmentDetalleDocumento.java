package com.example.usuario.soyactivista.fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import soy_activista.quartzapp.com.soy_activista.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentDetalleDocumento extends Fragment {

    private TextView titulo, nombrePdf, descripcion;
    private Button doc;

    public FragmentDetalleDocumento() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_detalle_documento, container, false);

        titulo = (TextView)v.findViewById(R.id.textTitulo);
        nombrePdf = (TextView)v.findViewById(R.id.textViewnombredoc);
        descripcion = (TextView)v.findViewById(R.id.textDescripcion);

        doc = (Button)v.findViewById(R.id.descargarDoc);

        //Load Values
        titulo.setText("Título del documento: "+getArguments().getString("titulo"));
        nombrePdf.setText("Nombre archivo: "+getArguments().getString("nombreAdjunto"));
        descripcion.setText(getArguments().getString("descripcion"));

        doc.setOnClickListener(seePDFDetail(getArguments().getString("adjunto")));

        return v;
    }

    public View.OnClickListener seePDFDetail(final String url){
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
        };
        return listener;
    }

}
