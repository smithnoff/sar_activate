package logica;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by Joisacris on 25/11/2015.
 */
public class ActividadAdapter extends ArrayAdapter<Actividad> {
    private ArrayList<Actividad> items;
    private int layoutAInflar;
    private Activity context;

    public ActividadAdapter(Activity context,ArrayList<Actividad> lista,int layoutAInflar) {
        super(context, R.layout.item_card_boletin_actividades, lista);
        this.items=lista;
        this.context=context;
        this.layoutAInflar=layoutAInflar;

    }

    public View getView(int position,View view,ViewGroup parent) {
        View rowView=null;
        LayoutInflater inflater = context.getLayoutInflater();
        rowView= inflater.inflate(this.layoutAInflar, null, true);
        TextView nombre, estatus, creador, inicio, fin;
        nombre = (TextView)rowView.findViewById(R.id.cardTextNombre);
        estatus = (TextView)rowView.findViewById(R.id.cardTextEstatus2);
        creador = (TextView)rowView.findViewById(R.id.cardTextCreado2);
        inicio = (TextView)rowView.findViewById(R.id.cardTextInicio2);
        fin = (TextView)rowView.findViewById(R.id.cardTextFin2);
        this.llenarFila(nombre, estatus, creador, inicio, fin, position);

        return rowView;

    }

    private void llenarFila(TextView nombre, TextView estatus,TextView creador, TextView inicio, TextView fin, int position) {
        nombre.setText(items.get(position).getNombre());
        estatus.setText(items.get(position).getEstatus());
        creador.setText(items.get(position).getCreador());

        SimpleDateFormat formatoDeFecha = new SimpleDateFormat("dd/MM/yyyy");

        inicio.setText(formatoDeFecha.format(items.get(position).getInicio()));
        fin.setText(formatoDeFecha.format(items.get(position).getFin()));
    }
}
