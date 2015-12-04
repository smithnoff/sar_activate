package logica;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by Brahyam on 2/12/2015.
 */
public class ListarUsuarioParseAdapter extends ParseQueryAdapter<ParseObject> {

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

}
