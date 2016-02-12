package logica;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.usuario.soyactivista.fragments.FragmentPuntuaciones;

import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by Luis Adrian on 26/01/2016.
 */
public class RankingEntityViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private static final String TAG = "RankingEntityViewHolder";
    private Entidad entidad;
    private Boolean entity;
    private ActivityPantallaMenu activity;

    protected TextView nombreEntidad;
    protected TextView posicion;
    protected TextView puntos;
    protected TextView cantidadUsuarios;
    protected CardView card;
    protected LinearLayout linearRanking;


    public RankingEntityViewHolder(View itemView) {
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

    public void setEntidad(Entidad entidad, ActivityPantallaMenu activity, Boolean onClick, Boolean gradient, Boolean entity){

        // Store Entidad
        this.entidad = entidad;

        // Store Context
        this.activity = activity;

        this.entity = entity;

        // Deactivate OnClickListener
        if( !onClick )
            card.setOnClickListener(null);

        // Set View Values
        if(entity == true)
        {
            cantidadUsuarios.setText("Usuarios en el municipio: "+String.valueOf(entidad.getUsuarios()));
        }
        else
        {
            cantidadUsuarios.setText("Usuarios en el estado: "+String.valueOf(entidad.getUsuarios()));
        }
        nombreEntidad.setText(entidad.getNombre());
        puntos.setText("Puntos acumulados: "+String.valueOf(entidad.getPuntos()));
        posicion.setText(String.valueOf(entidad.getPosicion()));


        // Get Primary Color
        TypedValue typedValue = new TypedValue();
        nombreEntidad.getContext().getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        int color = typedValue.data;

        int puntos = entidad.getPuntos();

        if( gradient )
            linearRanking.setBackgroundColor(ColorHelpers.getGradient(color, puntos));
        else
            linearRanking.setBackgroundColor(color);
    }


    @Override
    public void onClick(View v) {

        Bundle datos = new Bundle();
        datos.putString("estado", entidad.getNombre());

        Log.d(TAG, "Bundle Created with " + entidad.getNombre());

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
