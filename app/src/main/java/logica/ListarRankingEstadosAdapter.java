package logica;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by Luis Adrian on 26/01/2016.
 */
public class ListarRankingEstadosAdapter extends RecyclerView.Adapter<RankingViewHolder> {
    private int pos = 0;
    private List<Entidades> entidades;
    private View itemView;

    public ListarRankingEstadosAdapter(List<Entidades> entidad) {
        this.entidades = new ArrayList<>();
        this.entidades.addAll(entidad);
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

            Entidades entidades = this.entidades.get(i);
            rankingViewHolder.nombreEntidad.setText(entidades.getnombreEntidad());
            rankingViewHolder.puntos.setText(String.valueOf(entidades.getPuntos()));
            rankingViewHolder.posicion.setText(""+(1+i));
            rankingViewHolder.cantidadUser.setText(String.valueOf(entidades.getCantidadUsuarios()));
            if(entidades.getPuntos()>3000)
            {
                rankingViewHolder.linearRanking.setBackgroundColor(Color.argb(255, 193, 66, 66));
            }
            if(entidades.getPuntos()<2999)
            {
                rankingViewHolder.linearRanking.setBackgroundColor(Color.argb(255, 114, 38, 38));
            }

    }

    @Override
    public int getItemCount() {
        return entidades.size();
    }


}
