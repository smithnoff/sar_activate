package com.example.usuario.soyactivista.fragments;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;

import logica.Selector_de_Tema;
import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by Luis Adrian on 29/12/2015.
 */
public class FragmentEstadisticaUsuario extends Fragment {

    private TextView nombreUsuario, puntosAcumulados, partidasJugadas, tipoActivista;
    private int puntos = 0;
    private int partidas = 0;
    private LinearLayout layoutColor;
    private Toolbar appbar;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {


        //Choose fragment to inflate
        View v = inflater.inflate(R.layout.fragment_estadistica_usuario, container, false);
        ParseUser currentUser = ParseUser.getCurrentUser();

        nombreUsuario = (TextView)v.findViewById(R.id.nombreUsuario);
        puntosAcumulados = (TextView)v.findViewById(R.id.puntosTotales);
        partidasJugadas = (TextView)v.findViewById(R.id.partidasTotales);
        tipoActivista = (TextView)v.findViewById(R.id.tipoActivista);
        layoutColor = (LinearLayout)v.findViewById(R.id.linearColor);

        nombreUsuario.setText(currentUser.getString("nombre") + " " + currentUser.getString("apellido"));
        puntos = currentUser.getInt("Puntos");
        String puntoschar = String.valueOf(puntos);
        puntosAcumulados.setText(puntoschar+" puntos");
        partidas = currentUser.getInt("Partidas");
        String partidaschar = String.valueOf(partidas);
        partidasJugadas.setText(partidaschar+" partidas");

        if(puntos>=400)
        {
            tipoActivista.setText("JEFE");
        }
        else
        {
            tipoActivista.setText("LITTLE");
        }

        /*Resources res = getResources();
        Selector_de_Tema colorTema = new Selector_de_Tema();
        int colorTrivia2 = Selector_de_Tema.getTema();
        int colorTrivia = res.getColor(R.color.colorPrimary,);*/

        return v;
    }
}
