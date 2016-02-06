package logica;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by Luis Adrian on 27/01/2016.
 */
public class TopUsuariosViewHolder extends RecyclerView.ViewHolder {


    protected TextView nombreUsuario, cargo, municipio, posicion;
    protected CardView card;
    protected LinearLayout linear;

    public TopUsuariosViewHolder(View itemView) {
        super(itemView);

        // Set Holders
        posicion = (TextView) itemView.findViewById(R.id.position);
        nombreUsuario = (TextView) itemView.findViewById(R.id.nombreUsuario);
        cargo = (TextView) itemView.findViewById(R.id.cargo);
        municipio = (TextView) itemView.findViewById(R.id.nombreMunicipio);
        card = (CardView) itemView;
        linear = (LinearLayout)itemView.findViewById(R.id.linearUsuarios);
    }

    public void setUsuario(Usuario usuario, int nPosicion){

        posicion.setText(String.valueOf(nPosicion));
        nombreUsuario.setText(usuario.getNombre()+" "+usuario.getApellido());
        cargo.setText(usuario.getCargo());
        municipio.setText(usuario.getMunicipio());


    }


}
