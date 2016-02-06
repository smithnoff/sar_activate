package logica;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.usuario.soyactivista.fragments.FragmentCrearMensaje;
import com.example.usuario.soyactivista.fragments.FragmentPuntuaciones;

import java.io.Console;

import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by Luis Adrian on 26/01/2016.
 */
public class RankingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private static final String TAG = "RankingViewHolder";
    private Entidad entidad;

    private ActivityPantallaMenu activity;

    protected TextView nombreEntidad;
    protected TextView posicion;
    protected TextView puntos;
    protected TextView cantidadUsuarios;
    protected CardView card;
    protected LinearLayout linearRanking;


    public RankingViewHolder(View itemView) {
        super(itemView);

        // Set Listener
        itemView.setOnClickListener(this);

        // Set Holders
        nombreEntidad = (TextView) itemView.findViewById(R.id.nombreEntidad);
        puntos = (TextView) itemView.findViewById(R.id.puntosEstado);
        posicion= (TextView) itemView.findViewById(R.id.posicionestado);
        cantidadUsuarios = (TextView) itemView.findViewById(R.id.cantidadUsuarios);
        card = (CardView) itemView;
        linearRanking = (LinearLayout)itemView.findViewById(R.id.linearTopRanking);

    }

    public void setEntidad(Entidad entidad, ActivityPantallaMenu activity){

        // Store Entidad
        this.entidad = entidad;

        // Store Context
        this.activity = activity;

        // Set View Values
        nombreEntidad.setText(entidad.getNombre());
        puntos.setText(String.valueOf(entidad.getPuntos()));
        posicion.setText(String.valueOf(entidad.getPosicion()));
        cantidadUsuarios.setText(String.valueOf(entidad.getUsuarios()));

        // Get Primary Color
        TypedValue typedValue = new TypedValue();
        nombreEntidad.getContext().getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        int color = typedValue.data;

        int finalColor = color;
        int puntos = entidad.getPuntos();

        // Get color tone
        if ( puntos > 6000 && puntos <= 7999)
            finalColor = ColorHelpers.lighten(color, 0.2);

        if ( puntos > 4000 && puntos <= 5999)
            finalColor = ColorHelpers.lighten(color, 0.4);

        if ( puntos > 1000 && puntos <= 3999)
            finalColor = ColorHelpers.lighten(color, 0.6);

        if ( puntos > 0 && puntos <= 999)
            finalColor = ColorHelpers.lighten(color, 0.8);

        linearRanking.setBackgroundColor(finalColor);
    }


    @Override
    public void onClick(View v) {

        Bundle datos = new Bundle();
        datos.putString("estado", entidad.getNombre());

        Log.d(TAG, "Bundle Created with " + entidad.getNombre() + " " + entidad.getId());

        // Redirect View to next Fragment
        Fragment fragment = new FragmentPuntuaciones();

        fragment.setArguments(datos);
        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, fragment)
                .addToBackStack(null)
                .commit();
    }
}
