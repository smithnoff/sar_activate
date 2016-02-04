package logica;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.usuario.soyactivista.fragments.FragmentEditarUsuario;
import com.example.usuario.soyactivista.fragments.FragmentPuntuaciones;

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
    protected LinearLayout linearRanking;


    public RankingViewHolder(View itemView) {
        super(itemView);
        nombreEntidad = (TextView) itemView.findViewById(R.id.nombreEntidad);
        puntos = (TextView) itemView.findViewById(R.id.puntosEstado);
        posicion= (TextView) itemView.findViewById(R.id.posicionestado);
        cantidadUser = (TextView) itemView.findViewById(R.id.cantidadUsuarios);
        card = (CardView) itemView;
        linearRanking = (LinearLayout)itemView.findViewById(R.id.linearTopRanking);

        itemView.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Entidades entidades = new Entidades();
                Bundle datos = new Bundle();
                datos.putString("entidad",entidades.getnombreEntidad());
                datos.putString("id",entidades.getIdEntidad());

                // Redirect View to next Fragment
                Fragment fragment = new FragmentPuntuaciones();
                fragment.setArguments(datos);
                fragment.getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

    }


}
