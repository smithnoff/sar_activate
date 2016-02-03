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
public class ListarRankingEstadosAdapter extends RecyclerView.Adapter<RankingViewHolder> {
    private int pos = 0;
    private List<Entidades> entidades;

    public ListarRankingEstadosAdapter(List<Entidades> entidad) {
        this.entidades = new ArrayList<>();
        this.entidades.addAll(entidad);
    }

    @Override
    public RankingViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
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
    }

    @Override
    public int getItemCount() {
        return entidades.size();
    }
}
