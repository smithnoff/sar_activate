package logica;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by Brahyam on 18/12/2015.
 */
public class ListarConversacionAdapter extends ArrayAdapter<Conversacion> {

    private String TAG = "ListarConversacionAdapter";

    // For Fitlering
    ArrayList<Conversacion> conversacionArrayList;

    public ListarConversacionAdapter(Activity context, ArrayList<Conversacion> conversaciones) {
        super(context, 0,conversaciones);
        this.conversacionArrayList = new ArrayList<>(conversaciones);
    }

    public View getView(int position,View view,ViewGroup parent) {

        // TODO: Use View Holder method to improve performance by recycling views.
        if (view == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista_usuario, parent, false);
        }

        Conversacion conversacion = getItem(position);

        Usuario creador = conversacion.usuario;

        //Declare all fields
        final TextView valueNombre, valueEstado, valueMunicipio, valueCargo,valueComite,valueRol;

        // Assign to holders
        valueNombre = (TextView) view.findViewById(R.id.valueNombre);
        valueEstado = (TextView) view.findViewById(R.id.valueEstado);
        valueMunicipio = (TextView) view.findViewById(R.id.valueMunicipio);
        valueCargo = (TextView) view.findViewById(R.id.valueCargo);
        valueComite = (TextView) view.findViewById(R.id.valueComite);
        valueRol    = (TextView) view.findViewById(R.id.valueRol);

        // Load Values
        valueNombre.setText(creador.getNombre()+" "+creador.getApellido());
        valueEstado.setText(creador.getEstado());
        valueMunicipio.setText(creador.getMunicipio());
        valueCargo.setText(creador.getCargo());
        valueComite.setText(creador.getComite());
        valueRol.setText(creador.getRolName());

        return view;

    }

}