package logica;
import android.content.Context;
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
    private Context context;
    private Boolean clickable;

    public ListarTopUsuariosAdapter(Context context, List<Usuario> userArrayList, String entidad, Boolean onClick) {
        this.usuarios = userArrayList;
        this.entidad = entidad;
        this.context = context;
        this.clickable = onClick;
    }

    @Override
    public TopUsuariosViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_view_top_usuarios, viewGroup, false);
        return new TopUsuariosViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TopUsuariosViewHolder topUsuariosViewHolder, int i) {
        topUsuariosViewHolder.setUsuario(usuarios.get(i),i+1, entidad,(ActivityPantallaMenu)context, clickable);
    }

    @Override
    public int getItemCount() {
        return usuarios.size();
    }


}
