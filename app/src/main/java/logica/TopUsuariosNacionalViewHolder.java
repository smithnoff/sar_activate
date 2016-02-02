package logica;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by Luis Adrian on 27/01/2016.
 */
public class TopUsuariosNacionalViewHolder extends RecyclerView.ViewHolder {

    protected TextView nombreUsuario;
    protected TextView cargo;
    protected TextView municipio;
    protected TextView posicion;
    protected CardView card;

    public TopUsuariosNacionalViewHolder(View itemView) {
        super(itemView);
        nombreUsuario = (TextView) itemView.findViewById(R.id.nombreUsuario);
        cargo = (TextView) itemView.findViewById(R.id.cargo);
        municipio = (TextView) itemView.findViewById(R.id.nombreMunicipio);
        posicion = (TextView)itemView.findViewById(R.id.posicionUser);
        card = (CardView) itemView;
    }
}
