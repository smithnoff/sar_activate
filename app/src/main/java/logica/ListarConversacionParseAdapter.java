package logica;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by RSMAPP on 16/12/2015.
 */
public class ListarConversacionParseAdapter  extends ParseQueryAdapter<ParseObject> {

    private static final String TAG = "ConversacionAdapter"; // For Log Use
    private ParseUser otroUsuario;


    public ListarConversacionParseAdapter(Context context) {
        super(context, new ParseQueryAdapter.QueryFactory<ParseObject>() {
            public ParseQuery create() {

                // Get Conversation Owner - current user
                ParseUser currentUser = ParseUser.getCurrentUser();

                ParseQuery query = new ParseQuery("ParticipantesConversacion");
                query.whereEqualTo("usuario",currentUser);
                query.include("conversacion");
                return query;
            }
        });
    }

    public View getItemView(final ParseObject object, View v, ViewGroup parent) {

        if (v == null) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista_conversacion, parent, false);
        }

        super.getItemView(object, v, parent);

        ParseUser creador = object.getParseUser("usuario2");

        //Declare all fields
        final TextView valueNombre, valueEstado, valueMunicipio, valueCargo,valueComite,valueRol;

        // Assign to holders
        valueNombre = (TextView) v.findViewById(R.id.valueNombre);
        valueEstado = (TextView) v.findViewById(R.id.valueEstado);
        valueMunicipio = (TextView) v.findViewById(R.id.valueMunicipio);
        valueCargo = (TextView) v.findViewById(R.id.valueCargo);
        valueComite = (TextView) v.findViewById(R.id.valueComite);
        valueRol    = (TextView) v.findViewById(R.id.valueRol);

        // Load Values
        valueNombre.setText(creador.getString("nombre") + " " + creador.getString("apellido"));
        valueEstado.setText(creador.getString("estado"));
        valueMunicipio.setText(creador.getString("municipio"));
        valueCargo.setText(object.getString("cargo"));
        valueComite.setText(object.getString("comite"));

        if(object.getInt("rol") == 0)
            valueRol.setText(object.getInt("Activista"));
        else
            valueRol.setText(object.getInt("Registrante"));

        return v;
    }
}
