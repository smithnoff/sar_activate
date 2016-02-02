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
public class ListarTopUsuariosAdapter extends RecyclerView.Adapter<TopUsuariosNacionalViewHolder> {

    private List<Usuario> usuarioArrayList;

    public ListarTopUsuariosAdapter(List<Usuario> userArrayList) {
        this.usuarioArrayList = new ArrayList<>();
        this.usuarioArrayList.addAll(userArrayList);
    }

    @Override
    public TopUsuariosNacionalViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_view_top_usuarios, viewGroup, false);

        return new TopUsuariosNacionalViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TopUsuariosNacionalViewHolder topUsuarios, int i) {
        Usuario usuario = usuarioArrayList.get(i);

        topUsuarios.posicion.setText(String.valueOf(i+1));
        topUsuarios.nombreUsuario.setText(usuario.getNombre()+" "+usuario.getApellido());
        topUsuarios.cargo.setText(usuario.getCargo());
        topUsuarios.municipio.setText(usuario.getMunicipio());
    }

    @Override
    public int getItemCount() {
        return usuarioArrayList.size();
    }

}
