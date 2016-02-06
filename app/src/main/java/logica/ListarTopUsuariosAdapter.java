package logica;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import soy_activista.quartzapp.com.soy_activista.R;
/**
 * Created by Luis Adrian on 27/01/2016.
 */
public class ListarTopUsuariosAdapter extends RecyclerView.Adapter<TopUsuariosViewHolder> {

    private List<Usuario> usuarios;

    public ListarTopUsuariosAdapter(List<Usuario> userArrayList) {
        this.usuarios = userArrayList;
    }

    @Override
    public TopUsuariosViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_view_top_usuarios, viewGroup, false);

        return new TopUsuariosViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TopUsuariosViewHolder topUsuariosViewHolder, int i) {
        topUsuariosViewHolder.setUsuario(usuarios.get(i),i+1);
    }

    @Override
    public int getItemCount() {
        return usuarios.size();
    }

}
