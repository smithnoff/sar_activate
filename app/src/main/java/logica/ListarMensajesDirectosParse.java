package logica;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by darwin on 16/12/2015.
 */
public class ListarMensajesDirectosParse extends ParseQueryAdapter<ParseObject> {


    final String usuarioActual = ParseUser.getCurrentUser().getObjectId();
    private String usuario_sent_msg="";

    public ListarMensajesDirectosParse(Context context) {
        super(context, new ParseQueryAdapter.QueryFactory<ParseObject>() {
            public ParseQuery create() {

                ParseQuery innerQuery = new ParseQuery("Conversacion");
                innerQuery.whereEqualTo("objectId", "HkiiYh9kXy");

                ParseQuery query = new ParseQuery("MensajeDirecto");
                query.whereMatchesQuery("conversacion", innerQuery);
                query.include("autor");
                return query;
            }
        });

    }



    public View getItemView(ParseObject object, View v, ViewGroup parent){

        ParseUser user = object.getParseUser("autor");
        usuario_sent_msg=user.getObjectId();
        if(v == null){

            if(usuarioActual.equals(usuario_sent_msg))
            {
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_right,parent,false);
            }
            else
            {
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_left,parent,false);
            }

        }

        super.getItemView(object, v, parent);

        // Add Fields
        TextView escritor_msg = (TextView)v.findViewById(R.id.lbl1);
        TextView body_msg = (TextView)v.findViewById(R.id.lbl2);

        escritor_msg.setText(user.getString("nombre")+" "+user.getString("nombre")+" | "+user.getString("estado")+"-"+user.getString("municipio"));
        body_msg.setText(object.getString("texto"));

        return v;
    }



}
