package logica;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by Luis Adrian on 26/01/2016.
 */
public class RankingEstadosViewHolder extends RecyclerView.ViewHolder {
    protected TextView nombreEstado;
    protected TextView posicion;
    protected TextView puntos;
    protected TextView cantidadUser;
    protected CardView card;

    public RankingEstadosViewHolder(View itemView) {
        super(itemView);
        nombreEstado = (TextView) itemView.findViewById(R.id.nombreEstado);
        puntos = (TextView) itemView.findViewById(R.id.puntosEstado);
        posicion= (TextView) itemView.findViewById(R.id.posicionestado);
        cantidadUser = (TextView) itemView.findViewById(R.id.cantidadUsuarios);
        card = (CardView) itemView;
    }
}
