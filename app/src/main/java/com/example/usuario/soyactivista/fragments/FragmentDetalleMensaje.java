package com.example.usuario.soyactivista.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.Objects;

import soy_activista.quartzapp.com.soy_activista.R;


public class FragmentDetalleMensaje extends Fragment {

    private TextView valueNombre,valueEstado,valueMunicipio,valueFecha,valueTexto;
    private ImageView valueAdjunto;
    private Button botonReportar;
    private Button botonEliminar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_detalle_mensaje, container, false);

        // Assign holders
        valueNombre = (TextView)v.findViewById(R.id.valueNombre);
        valueEstado = (TextView)v.findViewById(R.id.valueEstado);
        valueMunicipio = (TextView)v.findViewById(R.id.valueMunicipio);
        valueFecha = (TextView)v.findViewById(R.id.valueFecha);
        valueTexto = (TextView)v.findViewById(R.id.valueTexto);

        valueAdjunto = (ImageView)v.findViewById(R.id.valueAdjunto);

        botonReportar = (Button)v.findViewById(R.id.botonReportar);
        botonEliminar = (Button)v.findViewById(R.id.botonEliminar);

        //Gets Current User
        final ParseUser usuarioActual = ParseUser.getCurrentUser();

        // Set Values
        valueNombre.setText(getArguments().getString("nombre"));
        valueEstado.setText(getArguments().getString("estado"));
        valueMunicipio.setText(getArguments().getString("municipio"));
        valueFecha.setText(getArguments().getString("fechaCreacion"));
        valueTexto.setText(getArguments().getString("texto"));
        Log.d("DETALLE", "adjunto value: "+getArguments().getString("adjunto"));

        if(getArguments().getString("adjunto") != null){
            valueAdjunto.setVisibility(View.VISIBLE);
            Glide.with(getContext())
                    .load(getArguments().getString("adjunto"))
                    .placeholder(R.mipmap.ic_placeholder)
                    .centerCrop()
                    .into(valueAdjunto);

            valueAdjunto.setOnClickListener(seeImageDetail(getArguments().getString("adjunto")));
        }else {
            Glide.clear(valueAdjunto);
            valueAdjunto.setImageDrawable(null);
            Log.d("DETALLE", "Adjunto URL was Null");
        }

        // Show reported button only if the message was not reported before
        if(getArguments().getBoolean("reportado")){
            botonReportar.setVisibility(View.GONE);
            botonReportar.setEnabled(false);
        }

        //Show EliminarMensaje button only if the user role is "1" or owner message.
        if(usuarioActual.getInt("rol")== 1 || usuarioActual.equals(getArguments().get("creadorId"))){
            botonEliminar.setVisibility(View.VISIBLE);
            botonEliminar.setEnabled(true);
        }

        // On Click listener to report Message
        botonReportar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseObject mensaje = ParseObject.createWithoutData("Mensaje",getArguments().getString("id"));
                mensaje.put("reportado",true);
                mensaje.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        Toast.makeText(getContext(), "Mensaje reportado.", Toast.LENGTH_SHORT).show();
                        botonReportar.setVisibility(View.GONE);
                        botonReportar.setEnabled(false);
                    }
                });
            }
        });

        // On Click listener to Eliminated Message
        botonEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseObject mensaje = ParseObject.createWithoutData("Mensaje",getArguments().getString("id"));
                mensaje.deleteInBackground();
                mensaje.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        Toast.makeText(getContext(), "Mensaje eliminado.", Toast.LENGTH_SHORT).show();
                        botonEliminar.setVisibility(View.GONE);
                        botonEliminar.setEnabled(false);
                    }
                });
                Fragment fragment = new FragmentListarMensajes();
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, fragment)
                        .commit();;
            }
        });

        return v;
    }

    // Listener for image details
    public View.OnClickListener seeImageDetail(final String url){
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle data = new Bundle();
                data.putString("imageUrl",url);
                // Redirect View to next Fragment
                Fragment fragment = new FragmentVerImagen();
                fragment.setArguments(data);
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        };
        return listener;
    }



}
