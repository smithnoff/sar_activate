package logica;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;

import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by Brahyam on 28/11/2015.
 */
public class ListarActividadAdapter extends ParseQueryAdapter<ParseObject> {

    // Modify Default query to look for objects Actividad
    public ListarActividadAdapter(Context context) {
        super(context, new ParseQueryAdapter.QueryFactory<ParseObject>() {
            public ParseQuery create() {
                ParseQuery query = new ParseQuery("Actividad");
                query.include("tipoActividad");
                query.include("creador");
                return query;
            }
        });
    }

    public View getItemView(ParseObject object, View v, ViewGroup parent){
        if(v == null){
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista_actividades,parent,false);
        }

        super.getItemView(object, v, parent);

        Log.d("ACTIVIDAD", "Loading object " + object.getObjectId());


        ParseObject tipoActividad = new ParseObject("TipoActividad");
        // Objects
        try{
            tipoActividad = object.getParseObject("tipoActividad").fetchIfNeeded();
            Log.d("BUSCAR", "Actividad buscada ."+tipoActividad.getObjectId()+" "+tipoActividad.getString("nombre"));
        }
        catch (com.parse.ParseException e){
            Log.d("BUSCAR", "Fetch object failed.");
        }

        ParseUser creador = object.getParseUser("creador");
        Log.d("BUSCAR", "Usuario buscado ."+creador.getObjectId()+" "+creador.getString("nombre"));



        //Declare all fields
        TextView textNombre,textEstatus,textCreador,textInicio,textFin,textLikes;

        // Assign to holders
        textNombre = (TextView)v.findViewById(R.id.nombreActividad);
        textEstatus = (TextView)v.findViewById(R.id.valueEjecucion);
        textCreador = (TextView)v.findViewById(R.id.valueCreador);
        textInicio = (TextView)v.findViewById(R.id.valueInicio);
        textFin = (TextView)v.findViewById(R.id.valueFin);
        textLikes = (TextView)v.findViewById(R.id.valueLikes);

        // Load Values
        textNombre.setText(tipoActividad.getString("nombre"));
        textEstatus.setText(object.getString("estatus"));
        textCreador.setText(creador.getString("nombre")+" "+creador.getString("apellido"));
        textLikes.setText(String.valueOf(object.getInt("meGusta")));

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        textInicio.setText(dateFormat.format(object.getDate("inicio")));
        textFin.setText(dateFormat.format(object.getDate("fin")));

        // Paint Status Accordingly
        if(object.getString("estatus") == "En Ejecución")
            textEstatus.setTextColor(getContext().getResources().getColor(R.color.verde));

        else
            textEstatus.setTextColor(getContext().getResources().getColor(R.color.rojo));

        return v;
    }


}
