package logica;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.List;

import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by Brahyam on 29/11/2015.
 */
public class ListarActividadAdapterV2 extends ArrayAdapter<Actividad> {

    private Context context;
    private List<Actividad> actividades;

    public ListarActividadAdapterV2(Context context, List<Actividad> objets){
        super(context, R.layout.item_lista_actividades, objets);
        this.context = context;
        this. actividades = objets;
    }
    /*

    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView == null){
            LayoutInflater mLayoutInflater = LayoutInflater.from(context);
            convertView = mLayoutInflater.inflate(R.layout.item_lista_actividades, null);
        }

        Log.d("ACTIVIDAD", "Loading object " + object.getObjectId());

        Actividad actividad = actividades.get(position);

        // Objects
        ParseObject tipoActividad = new ParseObject("TipoActividad");
        tipoActividad = object.getParseObject("tipoActividad");


        ParseUser creador = new ParseUser();
        creador = object.getParseUser("creador");

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
        textCreador.setText(creador.getString("nombre")+" "+creador.getString("Apellido"));
        textLikes.setText(object.getInt("meGusta"));
        textInicio.setText(object.getDate("inicio").toString());
        textFin.setText(object.getDate("fin").toString());

        // Paint Status Accordingly
        if(object.getString("estatus") == "En Ejecución")
            textEstatus.setTextColor(getContext().getResources().getColor(R.color.verde));

        else
            textEstatus.setTextColor(getContext().getResources().getColor(R.color.rojo));

        return v;


        return convertView;
    }

*/

}
