package logica;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by Brahyam on 28/11/2015.
 */
public class ListarTipoActividadAdapter extends ParseQueryAdapter<ParseObject> {



    public ListarTipoActividadAdapter(Context context) {
        super(context, new ParseQueryAdapter.QueryFactory<ParseObject>() {
            public ParseQuery create() {
                ParseQuery query = new ParseQuery("TipoActividad");
                query.whereEqualTo("activa",true);
                return query;
            }
        });
    }

    public View getItemView(ParseObject object, View v, ViewGroup parent){
        if(v == null){
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista_tipo_actividad,parent,false);
        }

        super.getItemView(object,v,parent);

        // Add Fields
        TextView nombreActividadTextView = (TextView)v.findViewById(R.id.nombreActividad);
        nombreActividadTextView.setText(object.getString("nombre"));

        TextView puntajeActividadTextView = (TextView)v.findViewById(R.id.puntajeActividad);
        puntajeActividadTextView.setText("Valor: "+String.valueOf(object.getInt("puntaje")));

        return v;
    }



}
