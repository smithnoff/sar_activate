package logica;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;

import java.util.ArrayList;

import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by Usuario on 19/10/2015.
 */
public class ListarUsuarioJerarquiaAdapter extends ArrayAdapter<Usuario> implements Filterable {

    private String TAG = "ListarUsuarioJerarquiaAdapter";

    // For Fitlering
    ArrayList<Usuario> usuarioArrayList;
    ParseUser currentUser = ParseUser.getCurrentUser();
    public ListarUsuarioJerarquiaAdapter(Activity context,ArrayList<Usuario> userArrayList) {
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
        if (currentUser.getUsername() != usuario.getUsername()) {

            valueNombre.setText(usuario.getNombre() + " " + usuario.getApellido());
            valueEstado.setText(usuario.getEstado());
            valueMunicipio.setText(usuario.getMunicipio());
            valueCargo.setText(usuario.getCargo());
        }
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

                ProgressDialog dialog = ProgressDialog.show(getContext(),"Filtrando","Cargando...",true);

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

                // Determine type of constraint
                String[] stringQuery = constraint.toString().split("=");
                String value = stringQuery[1].toLowerCase();
                resultArrayList  = new ArrayList<>();

                switch (stringQuery[0]){
                    case "estado":

                        // Query is asking for whole list skip search.
                        if(value.equals("todos")){
                            resultArrayList = new ArrayList<>(usuarioArrayList);
                            break;
                        }


                        for (int i = 0; i < usuarioArrayList.size(); i++ )
                        {
                            Usuario data = usuarioArrayList.get(i);
                            Log.d(TAG,"Comparing "+value+" to "+data.getEstado());
                            if(data.getEstado().toLowerCase().startsWith(value))
                                resultArrayList.add(data);
                        }

                        break;

                    case "username":
                        for (int i = 0; i < usuarioArrayList.size(); i++ )
                        {
                            Usuario data = usuarioArrayList.get(i);
                            // Search by Username
                            if(data.getUsername().toLowerCase().startsWith(value))
                                resultArrayList.add(data);
                        }
                        break;

                    case "nombre":
                        for (int i = 0; i < usuarioArrayList.size(); i++ )
                        {
                            Usuario data = usuarioArrayList.get(i);
                            if(data.getNombre().toLowerCase().startsWith(value))
                                resultArrayList.add(data);
                            else // Search by LastName
                                if(data.getApellido().toLowerCase().startsWith(value))
                                    resultArrayList.add(data);
                        }
                        break;

                    default:
                        Log.d(TAG,"Returning Default Result");
                        resultArrayList = new ArrayList<>(usuarioArrayList);
                        break;
                }


                Log.d(TAG,"Final Result list contains "+resultArrayList.size()+" elements.");

                results.count = resultArrayList.size();
                results.values = resultArrayList;
                dialog.dismiss();
                return results;
            }
        };
    }


}
