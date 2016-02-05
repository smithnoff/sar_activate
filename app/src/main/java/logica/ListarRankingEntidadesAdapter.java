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

        // Get Model
        Entidad entidad = this.entidades.get(i);

        // Set View Values
        rankingViewHolder.nombreEntidad.setText(entidad.getNombre());
        rankingViewHolder.puntos.setText(String.valueOf(entidad.getPuntos()));
        rankingViewHolder.posicion.setText(""+(1+i));
        rankingViewHolder.cantidadUsuarios.setText(String.valueOf(entidad.getUsuarios()));

        // Get Primary Color
        TypedValue typedValue = new TypedValue();
        rankingViewHolder.nombreEntidad.getContext().getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        int color = typedValue.data;

        int finalColor = color;
        int puntos = entidad.getPuntos();

        // Get color tone
        if ( puntos > 6000 && puntos <= 7999)
            finalColor = ColorHelpers.lighten(color, 0.2);

        if ( puntos > 4000 && puntos <= 5999)
            finalColor = ColorHelpers.lighten(color, 0.4);

        if ( puntos > 1000 && puntos <= 3999)
            finalColor = ColorHelpers.lighten(color, 0.6);

        if ( puntos > 0 && puntos <= 999)
            finalColor = ColorHelpers.lighten(color, 0.8);

        rankingViewHolder.linearRanking.setBackgroundColor(finalColor);

    }

    @Override
    public int getItemCount() {
        return entidades.size();
    }


}
