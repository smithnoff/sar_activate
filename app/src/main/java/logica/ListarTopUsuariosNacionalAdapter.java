package logica;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import logica.Estados;
import logica.RankingEstadosViewHolder;
import logica.TopUsuariosNacionalViewHolder;
import soy_activista.quartzapp.com.soy_activista.R;
/**
 * Created by Luis Adrian on 27/01/2016.
 */
public class ListarTopUsuariosNacionalAdapter extends RecyclerView.Adapter<TopUsuariosNacionalViewHolder> {

    private List<Usuario> usuarioArrayList;

    public ListarTopUsuariosNacionalAdapter(List<Usuario> userArrayList) {
        //this.estados = new ArrayList<>();
        //this.estados.addAll(estados);
        this.usuarioArrayList = new ArrayList<>(userArrayList);
        this.usuarioArrayList.addAll(usuarioArrayList);
    }

    @Override
    public TopUsuariosNacionalViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_view_top_usuarios_nacional, viewGroup, false);

        return new TopUsuariosNacionalViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TopUsuariosNacionalViewHolder topUsuarios, int i) {
        Usuario usuario = usuarioArrayList.get(i);
        topUsuarios.nombreUsuario.setText(usuario.getNombre()+" "+usuario.getApellido());
        topUsuarios.cargo.setText(usuario.getCargo());
        topUsuarios.municipio.setText(usuario.getMunicipio());
    }

    @Override
    public int getItemCount() {
        return usuarioArrayList.size();
    }

}
