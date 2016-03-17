package com.burizalabs.soyactivista.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.burizalabs.soyactivista.R;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import java.util.ArrayList;


/**
 * Created by Brahyam on 2/12/2015.
 */
public class ListarUsuarioParseAdapter extends ParseQueryAdapter<ParseObject> implements Filterable {

    private ArrayList<ParseUser> userArrayList;

    // Modify Default query to look for objects Actividad
    public ListarUsuarioParseAdapter(Context context) {
        super(context, new ParseQueryAdapter.QueryFactory<ParseObject>() {
            public ParseQuery create() {
                ParseQuery query = new ParseQuery("_User");
                return query;
            }
        });
    }

    public View getItemView(final ParseObject user, View v, ViewGroup parent) {
        if (v == null) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista_usuario, parent, false);
        }

        super.getItemView(user, v, parent);

        //Declare all fields
        final TextView valueNombre, valueEstado, valueMunicipio, valueCargo;

        // Assign to holders
        valueNombre = (TextView) v.findViewById(R.id.valueNombre);
        valueEstado = (TextView) v.findViewById(R.id.valueEstado);
        valueMunicipio = (TextView) v.findViewById(R.id.valueMunicipio);
        valueCargo = (TextView) v.findViewById(R.id.valueCargo);


        // Load Values

        valueNombre.setText(user.getString("nombre") + " " + user.getString("apellido"));
        valueEstado.setText(user.getString("estado"));
        valueMunicipio.setText(user.getString("municipio"));
        valueCargo.setText(user.getString("cargo"));

        return v;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                // Write your logic for PUBLISHING RESULTS and notify your dataset for change


                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                // Write your logic here to PERFORM FILTERING and return filtered result
                userArrayList = new ArrayList<>();
                FilterResults results = new FilterResults();

                // if no constraint given return current list.
                if(constraint == null || constraint.length() == 0){
                    results.count = getCount();
                    for (int i = 0; i < getCount();i++)
                    {
                        userArrayList.add((ParseUser)getItem(i));
                    }
                    results.values = userArrayList;
                    return  results;
                }

                // if constraint given- Filter by (Name/LastName/Id)
                constraint = constraint.toString().toLowerCase();
                for (int i = 0; i < getCount(); i++ )
                {
                    ParseUser data = (ParseUser) getItem(i);
                    if(data.getUsername().toLowerCase().startsWith(constraint.toString()))
                        userArrayList.add(data);
                }
                results.count = userArrayList.size();
                results.values = userArrayList;
                return results;
            }
        };
    }

    @Override
    public View getNextPageView(View v, ViewGroup parent) {
        if (v == null) {
            //   v = getDefaultView(parent.getContext());
            v = View.inflate(getContext(), R.layout.list_load_more_footer, null);

        }
        TextView textView = (TextView) v.findViewById(R.id.load_more); //set the button
        textView.setText("Cargar más usuarios...");
        return v;
    }

}
