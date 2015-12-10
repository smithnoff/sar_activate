package logica;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.parse.ParseUser;

import java.util.ArrayList;

import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by Usuario on 19/10/2015.
 */
public class ListarUsuarioAdapter extends ArrayAdapter<Usuario> {

    private String TAG = "ListarUsuarioAdapter";

    // For Fitlering
    ArrayList<Usuario> usuarioArrayList;

    public ListarUsuarioAdapter(Activity context,ArrayList<Usuario> userArrayList) {
        super(context, 0,userArrayList);
        this.usuarioArrayList = new ArrayList<>(userArrayList);
    }

    public View getView(int position,View view,ViewGroup parent) {

        // TODO: Use View Holder method to improve performance by recycling views.
        if (view == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista_usuario, parent, false);
        }

        Usuario usuario = getItem(position);

        //Declare all fields
        final TextView valueNombre, valueEstado, valueMunicipio, valueCargo;

        // Assign to holders
        valueNombre = (TextView) view.findViewById(R.id.valueNombre);
        valueEstado = (TextView) view.findViewById(R.id.valueEstado);
        valueMunicipio = (TextView) view.findViewById(R.id.valueMunicipio);
        valueCargo = (TextView) view.findViewById(R.id.valueCargo);


        // Load Values
        valueNombre.setText(usuario.getNombre() + " " + usuario.getApellido());
        valueEstado.setText(usuario.getEstado());
        valueMunicipio.setText(usuario.getMunicipio());
        valueCargo.setText(usuario.getCargo());

        return view;

    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                // Write your logic for PUBLISHING RESULTS and notify your dataset for change
                clear();
                addAll((ArrayList<Usuario>) results.values);
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                // Write your logic here to PERFORM FILTERING and return filtered result
                FilterResults results = new FilterResults();

                ArrayList<Usuario> resultArrayList;

                // if no constraint given return current list.
                if(constraint == null || constraint.length() == 0){
                    resultArrayList = new ArrayList<>(usuarioArrayList);
                    results.count = getCount();
                    results.values = resultArrayList;
                    return  results;
                }

                // if constraint given- Filter by id
                constraint = constraint.toString().toLowerCase();
                resultArrayList  = new ArrayList<>();
                for (int i = 0; i < getCount(); i++ )
                {
                    Usuario data = getItem(i);
                    Log.d(TAG,"Comparing "+constraint.toString()+" to "+data.getIdentificador());
                    if(data.getIdentificador().toLowerCase().startsWith(constraint.toString()))
                        resultArrayList.add(data);
                }
                Log.d(TAG,"Final Result list contains "+resultArrayList.size()+" elements.");
                results.count = resultArrayList.size();
                results.values = resultArrayList;
                return results;
            }
        };
    }


}
