package logica;

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
public class ListarRankingEstadosAdapter extends RecyclerView.Adapter<RankingEstadosViewHolder> {
    private int pos = 0;
    private List<Estados> estados;

    public ListarRankingEstadosAdapter(List<Estados> estados) {
        this.estados = new ArrayList<>();
        this.estados.addAll(estados);
    }

    @Override
    public RankingEstadosViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_view_ranking_estados, viewGroup, false);

        return new RankingEstadosViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RankingEstadosViewHolder rankingEstadosViewHolder, int i) {

            Estados estado = estados.get(i);
            rankingEstadosViewHolder.nombreEstado.setText(estado.getNombreEstado());
            rankingEstadosViewHolder.puntos.setText(String.valueOf(estado.getPuntos()));
            rankingEstadosViewHolder.posicion.setText(""+(1+i));
            rankingEstadosViewHolder.cantidadUser.setText(String.valueOf(estado.getCantidadUsuarios()));
    }

    @Override
    public int getItemCount() {
        return estados.size();
    }
}
