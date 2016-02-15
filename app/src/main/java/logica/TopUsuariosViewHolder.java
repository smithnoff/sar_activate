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


    protected TextView nombreUsuario, cargo, entidad, posicion, puntosActivismo;
    protected CardView card;
    protected LinearLayout linear;
    protected int ptos = 0;

    public TopUsuariosViewHolder(View itemView) {
        super(itemView);

        // Set Holders
        posicion = (TextView) itemView.findViewById(R.id.position);
        nombreUsuario = (TextView) itemView.findViewById(R.id.nombreUsuario);
        cargo = (TextView) itemView.findViewById(R.id.cargo);
        entidad = (TextView) itemView.findViewById(R.id.nombreEntidad);
        puntosActivismo = (TextView)itemView.findViewById(R.id.puntosUsuario);
        card = (CardView) itemView;
        linear = (LinearLayout)itemView.findViewById(R.id.linearUsuarios);
    }

    public void setUsuario(Usuario usuario, int nPosicion, String entity){

            posicion.setText(String.valueOf(nPosicion));
            nombreUsuario.setText("Nombre: " + usuario.getNombre()+" "+usuario.getApellido());
            cargo.setText("Cargo: "+ usuario.getCargo());
            entidad.setText(entity + ": " + usuario.getMunicipio());
            puntosActivismo.setText("Puntos: "+String.valueOf(usuario.getPuntosActivismo()));

    }


}
