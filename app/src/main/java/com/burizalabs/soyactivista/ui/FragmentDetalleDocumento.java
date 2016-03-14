package com.burizalabs.soyactivista.ui;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.ParseException;
import com.parse.ParseObject;

import com.burizalabs.soyactivista.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentDetalleDocumento extends Fragment {

    private TextView titulo, nombrePdf, descripcion;
    private Button doc, buttonEliminar, buttonRegresar;

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
        buttonEliminar = (Button)v.findViewById(R.id.buttonEliminar);
        buttonRegresar = (Button)v.findViewById(R.id.buttonRegresar);

        //Load Values
        titulo.setText("Título del documento: "+getArguments().getString("titulo"));
        nombrePdf.setText("Nombre archivo: "+getArguments().getString("nombreAdjunto"));
        descripcion.setText(getArguments().getString("descripcion"));

        doc.setOnClickListener(seePDFDetail(getArguments().getString("adjunto")));

        buttonRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new FragmentListarDocumentos();
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        buttonEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Confirmar");
                builder.setMessage("¿Está seguro que desea eliminar el documento?");

                builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialogo, int which) {
                        // Redirect View to list
                        dialogo.dismiss();
                        ParseObject documento = ParseObject.createWithoutData("Documento", getArguments().getString("id"));
                        documento.deleteInBackground(new DeleteCallback() {
                            @Override
                            public void done(ParseException e) {
                                Toast.makeText(getActivity(), "Documento eliminado correctamente.", Toast.LENGTH_SHORT).show();
                                // Redirect User to List
                                Fragment fragment = new FragmentListarDocumentos();
                                getFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.content_frame, fragment)
                                        .commit();

                            }
                        });


                    }

                });

                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogo, int which) {
                        // Do nothing
                        dialogo.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();

            }
        });


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
