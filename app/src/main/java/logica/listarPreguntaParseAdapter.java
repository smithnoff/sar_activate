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
 * Created by darwin on 28/12/2015.
 */
 public class listarPreguntaParseAdapter extends ParseQueryAdapter<ParseObject> {



    public listarPreguntaParseAdapter(Context context) {
        super(context, new ParseQueryAdapter.QueryFactory<ParseObject>() {
            public ParseQuery create() {
                ParseQuery query = new ParseQuery("Pregunta");
                query.orderByDescending("puntaje");
                return query;
            }
        });
    }

    public listarPreguntaParseAdapter(Context context, final String constraint) {
        super(context, new ParseQueryAdapter.QueryFactory<ParseObject>() {
            public ParseQuery create() {

                ParseQuery query = new ParseQuery("Pregunta");

                // Determine type of constraint
                String[] queryArray = constraint.toString().split("=");
                String value = queryArray[1];

                switch (queryArray[0]){
                    case "puntaje":
                        query.whereEqualTo("puntaje", Integer.parseInt(value));
                        break;
                    case "nivel":
                        query.whereEqualTo("dificultad", value);

                        break;

                    default:

                        break;
                }
                query.orderByDescending("puntaje");
                return query;
            }
        });
    }



    public View getItemView(ParseObject object, View v, ViewGroup parent){
        if(v == null){
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista_preguntas,parent,false);
        }

        super.getItemView(object,v,parent);

        // Add Fields
        TextView NombrePreguntaTextView = (TextView)v.findViewById(R.id.NombrePregunta);
        TextView puntajePreguntaTextView = (TextView)v.findViewById(R.id.puntajePregunta);
        TextView NivelDificultadTextView = (TextView)v.findViewById(R.id.NivelDificultad);

        NombrePreguntaTextView.setText(object.getString("pregunta"));
        puntajePreguntaTextView.setText("Puntos: "+object.getNumber("puntaje").toString());
        NivelDificultadTextView.setText("Nivel: "+object.getString("dificultad"));


        return v;
    }


}
