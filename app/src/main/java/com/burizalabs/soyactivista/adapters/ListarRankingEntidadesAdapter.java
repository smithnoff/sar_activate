package com.burizalabs.soyactivista.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import com.burizalabs.soyactivista.R;
import com.burizalabs.soyactivista.ui.ActivityPantallaMenu;
import com.burizalabs.soyactivista.entities.Entidad;
import com.burizalabs.soyactivista.ui.RankingEntityViewHolder;

/**
 * Created by Luis Adrian on 26/01/2016.
 */
public class ListarRankingEntidadesAdapter extends RecyclerView.Adapter<RankingEntityViewHolder> {

    private ArrayList<Entidad> entidades;
    private View itemView;
    private Context context;
    private Boolean clickable;
    private Boolean gradient;
    private Boolean entidad;

    public ListarRankingEntidadesAdapter(Context context,ArrayList<Entidad> entidadList, Boolean onClick, Boolean gradient, Boolean entidad) {
        this.entidades = entidadList;
        this.context = context;
        this.clickable = onClick;
        this.gradient = gradient;
        this.entidad = entidad;
    }

    @Override
    public RankingEntityViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_view_ranking, viewGroup, false);

        return new RankingEntityViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RankingEntityViewHolder rankingEntityViewHolder, int i) {
        rankingEntityViewHolder.setEntidad(entidades.get(i), (ActivityPantallaMenu)context, clickable, gradient, entidad);
    }

    @Override
    public int getItemCount() {
        return entidades.size();
    }


}
