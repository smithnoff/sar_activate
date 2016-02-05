package logica;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by Luis Adrian on 26/01/2016.
 */
public class ListarRankingEntidadesAdapter extends RecyclerView.Adapter<RankingViewHolder> {
    private ArrayList<Entidad> entidades;
    private View itemView;

    public ListarRankingEntidadesAdapter(ArrayList<Entidad> entidadList) {
        this.entidades = entidadList;
    }

    @Override
    public RankingViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_view_ranking, viewGroup, false);

        return new RankingViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RankingViewHolder rankingViewHolder, int i) {

            Entidad entidad = this.entidades.get(i);
            rankingViewHolder.nombreEntidad.setText(entidad.getNombre());
            rankingViewHolder.puntos.setText(String.valueOf(entidad.getPuntos()));
            rankingViewHolder.posicion.setText(""+(1+i));
            rankingViewHolder.cantidadUsuarios.setText(String.valueOf(entidad.getUsuarios()));
            if(entidad.getPuntos()>3000)
            {
                rankingViewHolder.linearRanking.setBackgroundColor(Color.argb(255, 193, 66, 66));
            }
            if(entidad.getPuntos()<2999)
            {
                rankingViewHolder.linearRanking.setBackgroundColor(Color.argb(255, 114, 38, 38));
            }

    }

    @Override
    public int getItemCount() {
        return entidades.size();
    }


}
