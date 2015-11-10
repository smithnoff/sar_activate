package logica;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by Usuario on 29/10/2015.
 */
public class MensajeAdapter extends RecyclerView.Adapter<MensajeAdapter.MensajeAdapterViewHolder> {
    private List<Mensaje> items;

    public static class MensajeAdapterViewHolder extends RecyclerView.ViewHolder {
        private ImageView adjuntado;
        private TextView identificador;
        private TextView lugar; //estado y municipio
        private TextView textoMensaje;

        public MensajeAdapterViewHolder(View itemView) {
            super(itemView);
            adjuntado = (ImageView)itemView.findViewById(R.id.imagenAdjuntado); //imagen iconico del mapa o archivo
            identificador = (TextView)itemView.findViewById(R.id.cardTextNombre); //identificador Persona
            lugar = (TextView)itemView.findViewById(R.id.cardTextLugar);         //estado - municipio
            textoMensaje = (TextView)itemView.findViewById(R.id.cardTextMensaje);// el propio mensaje
        }
    }



    @Override
    public MensajeAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_dashboard,parent,false);
        return new MensajeAdapterViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MensajeAdapterViewHolder holder, int position) {

        holder.adjuntado.setImageResource(items.get(position).getImagen());
        holder.identificador.setText(items.get(position).getUsuario().getNombre()+" " +items.get(position).getUsuario().getApellido()+" ");
        holder.lugar.setText(" "+items.get(position).getUsuario().getEstado()+"-" +items.get(position).getUsuario().getMuncipio());
        holder.textoMensaje.setText(items.get(position).getMensaje());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public MensajeAdapter(List<Mensaje> items) {
        this.items = items;
    }

}
