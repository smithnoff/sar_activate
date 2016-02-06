package logica;

import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
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
    private Context context;

    public ListarRankingEntidadesAdapter(Context context,ArrayList<Entidad> entidadList) {
        this.entidades = entidadList;
        this.context = context;
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
        rankingViewHolder.setEntidad(entidades.get(i), (ActivityPantallaMenu)context);
    }

    @Override
    public int getItemCount() {
        return entidades.size();
    }


}
