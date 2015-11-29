package logica;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by Brahyam on 27/11/2015.
 */
public class ListarMensajesAdapter extends RecyclerView.Adapter<ListarMensajesAdapter.MessageViewHolder> {

    List<Mensaje> mensajes;

    public ListarMensajesAdapter(List<Mensaje> mensajes){
        this.mensajes = mensajes;
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder{
        CardView cv;
        TextView textoMensaje;
        TextView nombreAutor;
        TextView ubicacionAutor;

        MessageViewHolder(View itemView){
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            textoMensaje = (TextView)itemView.findViewById(R.id.message_text);
            nombreAutor = (TextView)itemView.findViewById(R.id.message_author);
            ubicacionAutor = (TextView)itemView.findViewById(R.id.author_location);
        }
    }

    @Override
    public ListarMensajesAdapter.MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_mensaje,parent,false);
        MessageViewHolder mvh = new MessageViewHolder(v);
        return  mvh;
    }

    @Override
    public void onBindViewHolder(ListarMensajesAdapter.MessageViewHolder holder, int position) {
        holder.textoMensaje.setText(mensajes.get(position).getTexto());
        holder.nombreAutor.setText(mensajes.get(position).getAutor().getNombre()+" "+mensajes.get(position).getAutor().getApellido());
        holder.ubicacionAutor.setText(mensajes.get(position).getAutor().getEstado()+" "+mensajes.get(position).getAutor().getMuncipio());
    }

    @Override
    public int getItemCount() {
        return mensajes.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


}
