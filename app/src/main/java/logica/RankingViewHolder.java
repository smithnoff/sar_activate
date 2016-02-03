package logica;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by Luis Adrian on 26/01/2016.
 */
public class RankingViewHolder extends RecyclerView.ViewHolder {
    protected TextView nombreEntidad;
    protected TextView posicion;
    protected TextView puntos;
    protected TextView cantidadUser;
    protected CardView card;

    public RankingViewHolder(View itemView) {
        super(itemView);
        nombreEntidad = (TextView) itemView.findViewById(R.id.nombreEntidad);
        puntos = (TextView) itemView.findViewById(R.id.puntosEstado);
        posicion= (TextView) itemView.findViewById(R.id.posicionestado);
        cantidadUser = (TextView) itemView.findViewById(R.id.cantidadUsuarios);
        card = (CardView) itemView;
    }
}
