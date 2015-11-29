package logica;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import soy_activista.quartzapp.com.soy_activista.R;

public class MensajeAdapter extends RecyclerView.Adapter<MensajeAdapter.MensajeAdapterViewHolder> implements View.OnClickListener {

    private List<Mensaje> items;
    public static int position;

    public MensajeAdapter(List<Mensaje> items) {
        this.items = items;
    }

    public class MensajeAdapterViewHolder extends RecyclerView.ViewHolder {

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
            itemView.setOnClickListener(MensajeAdapter.this);
        }

    }

    @Override
    public void onClick(View v) {
        Log.d("MENSAJE","Mensaje Seleccionado "+position);
        /*
        Intent intent = new Intent(mContext, FragmentDetalleMensaje.class);
        Fragment fragment = new FragmentDetalleMensaje();
        ((Activity)mContext).getFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, fragment)
                .addToBackStack(null)
                .commit();

        mContext.startActivity(intent);
        */
    }

    @Override
    public MensajeAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_dashboard,parent,false);
        return new MensajeAdapterViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MensajeAdapterViewHolder holder, final int position) {

        holder.adjuntado.setImageBitmap(items.get(position).getAdjunto());
        holder.identificador.setText(items.get(position).getAutor().getNombre() + " " + items.get(position).getAutor().getApellido() + " ");
        holder.lugar.setText(" "+items.get(position).getAutor().getEstado()+"-" +items.get(position).getAutor().getMuncipio());
        holder.textoMensaje.setText(items.get(position).getTexto());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
