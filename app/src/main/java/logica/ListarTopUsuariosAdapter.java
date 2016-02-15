package logica;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
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

    private static final int VIEW_TYPE_EMPTY_LIST_PLACEHOLDER = 0;
    private static final int VIEW_TYPE_OBJECT_VIEW = 1;
    private List<Usuario> usuarios;
    private String entidad;
    private View itemView;

    public ListarTopUsuariosAdapter(List<Usuario> userArrayList, String entidad) {
        this.usuarios = userArrayList;
        this.entidad = entidad;
    }

    @Override
    public TopUsuariosViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        /*switch(i) {
            case VIEW_TYPE_EMPTY_LIST_PLACEHOLDER:
                // return view holder for your placeholder
                itemView = LayoutInflater.
                        from(viewGroup.getContext()).
                        inflate(R.layout.card_view_top_usuarios_empty, viewGroup,false);
                break;
            case VIEW_TYPE_OBJECT_VIEW:
                itemView = LayoutInflater.
                        from(viewGroup.getContext()).
                        inflate(R.layout.card_view_top_usuarios, viewGroup, false);
                // return view holder for your normal list item
                break;
        }*/
        itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_view_top_usuarios, viewGroup, false);
        return new TopUsuariosViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TopUsuariosViewHolder topUsuariosViewHolder, int i) {
        topUsuariosViewHolder.setUsuario(usuarios.get(i),i+1, entidad);
    }

    @Override
    public int getItemCount() {
        return usuarios.size();
    }

    /*@Override
    public int getItemViewType(int position) {
        if (usuarios.isEmpty()) {
            return VIEW_TYPE_EMPTY_LIST_PLACEHOLDER;
        } else {
            return VIEW_TYPE_OBJECT_VIEW;
        }
    }*/

}
