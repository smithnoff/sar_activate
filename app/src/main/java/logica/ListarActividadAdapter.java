package logica;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;

import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by Brahyam on 28/11/2015.
 */
public class ListarActividadAdapter extends ParseQueryAdapter<ParseObject> {

    private String TAG = "ListarActividadAdapter";

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

    // Modify Default query to look for objects Actividad
    public ListarActividadAdapter(Context context, final String constraint) {
        super(context, new ParseQueryAdapter.QueryFactory<ParseObject>() {
            public ParseQuery create() {
                // Start Query
                ParseQuery query = new ParseQuery("Actividad");

                // Determine type of constraint
                String[] queryArray = constraint.toString().split("=");
                String value = queryArray[1];

                switch (queryArray[0]){
                    case "estatus":
                        query.whereEqualTo("estatus",value);

                        break;
                    case "ubicacion":
                        query.whereEqualTo("ubicacion",value);

                        break;
                    case "estado":
                        // Query is asking for whole list, skip search.
                        if(value.equals("Todos"))
                            break;

                        query.whereEqualTo("ubicacion","Estadal");
                        query.whereEqualTo("estado",value);
                        break;

                    case "propios":

                        ParseQuery<ParseObject> innerQuery2 = ParseQuery.getQuery("_User");
                        innerQuery2.whereEqualTo("username", value);
                        query.whereMatchesQuery("creador", innerQuery2);
                        break;

                    case "meGusta":

                        query.orderByDescending("meGusta");
                        break;

                    default:

                        break;
                }


                query.include("tipoActividad");
                query.include("creador");
                query.orderByDescending("createdAt");
                return query;
            }
        });
    }

    public View getItemView(final ParseObject object, View v, ViewGroup parent){
        if(v == null){
            // Choose which template to inflate depending of photo amount.
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista_actividad,parent,false);
        }

        super.getItemView(object, v, parent);

        Log.d("ACTIVIDAD", "Loading object " + object.getObjectId());


        ParseObject tipoActividad = new ParseObject("TipoActividad");
        // Objects
        try{
            tipoActividad = object.getParseObject("tipoActividad").fetchIfNeeded();
        }
        catch (com.parse.ParseException e){
            // TODO: Catch case when object does not have TipoACtividad and the app crashes
            Log.d("BUSCAR", "Fetch object failed.");
        }

        ParseUser creador = object.getParseUser("creador");

        //Declare all fields
        final TextView textNombre,textEstatus,textCreador,textInicio,textFin,textLikes;
        final ImageButton botonMeGusta;
        final ImageView imageView1,imageView2,imageView3,imageView4;
        final View separator;

        // TODO: Use ViewHolder to improve performance.
        // Assign to holders
        textNombre = (TextView)v.findViewById(R.id.nombreActividad);
        textEstatus = (TextView)v.findViewById(R.id.valueEjecucion);
        textCreador = (TextView)v.findViewById(R.id.valueCreador);
        textInicio = (TextView)v.findViewById(R.id.valueInicio);
        textFin = (TextView)v.findViewById(R.id.valueFin);
        textLikes = (TextView)v.findViewById(R.id.valueLikes);

        separator = (View)v.findViewById(R.id.separator);

        //botonMeGusta = (ImageButton) v.findViewById(R.id.botonMeGusta);

        imageView1 = (ImageView)v.findViewById(R.id.imagen1);
        imageView2 = (ImageView)v.findViewById(R.id.imagen2);
        imageView3 = (ImageView)v.findViewById(R.id.imagen3);
        imageView4= (ImageView)v.findViewById(R.id.imagen4);

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

        // Load Images

        ParseFile imagen1 = object.getParseFile("imagen1");
        ParseFile imagen2 = object.getParseFile("imagen2");
        ParseFile imagen3 = object.getParseFile("imagen3");
        ParseFile imagen4 = object.getParseFile("imagen4");

        String url;

        if(imagen1 != null){
            separator.setVisibility(View.VISIBLE);
            imageView1.setVisibility(View.VISIBLE);
            url = imagen1.getUrl();
            Glide.with(getContext())
                    .load(url)
                    .centerCrop()
                    .into(imageView1);
        }
        else{
            Glide.clear(imageView1);
            imageView1.setImageDrawable(null);
        }

        if(imagen2 != null){
            separator.setVisibility(View.VISIBLE);
            imageView2.setVisibility(View.VISIBLE);
            url = imagen2.getUrl();
            Glide.with(getContext())
                    .load(url)
                    .centerCrop()
                    .into(imageView2);
        }
        else{
            Glide.clear(imageView2);
            imageView2.setImageDrawable(null);
        }

        if(imagen3 != null){
            separator.setVisibility(View.VISIBLE);
            imageView3.setVisibility(View.VISIBLE);
            url = imagen3.getUrl();
            Glide.with(getContext())
                    .load(url)
                    .centerCrop()
                    .into(imageView3);
        }
        else{
            Glide.clear(imageView3);
            imageView3.setImageDrawable(null);
        }

        if(imagen4 != null){
            separator.setVisibility(View.VISIBLE);
            imageView4.setVisibility(View.VISIBLE);
            url = imagen4.getUrl();
            Glide.with(getContext())
                    .load(url)
                    .centerCrop()
                    .into(imageView4);
        }
        else{
            Glide.clear(imageView4);
            imageView4.setImageDrawable(null);
        }

        return v;
    }


}
