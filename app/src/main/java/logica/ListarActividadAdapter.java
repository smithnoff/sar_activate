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

    public View getItemView(final ParseObject object, View v, ViewGroup parent){
        if(v == null){
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
        final ImageView imageView;
        final View separator;

        // Assign to holders
        textNombre = (TextView)v.findViewById(R.id.nombreActividad);
        textEstatus = (TextView)v.findViewById(R.id.valueEjecucion);
        textCreador = (TextView)v.findViewById(R.id.valueCreador);
        textInicio = (TextView)v.findViewById(R.id.valueInicio);
        textFin = (TextView)v.findViewById(R.id.valueFin);
        textLikes = (TextView)v.findViewById(R.id.valueLikes);

        separator = (View)v.findViewById(R.id.separator);

        //botonMeGusta = (ImageButton) v.findViewById(R.id.botonMeGusta);

        imageView = (ImageView)v.findViewById(R.id.imagen1);

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

        ParseFile imagen = object.getParseFile("imagen1");
        if(imagen != null){
            separator.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.VISIBLE);
            String url = imagen.getUrl();
            Glide.with(getContext())
                    .load(url)
                    .placeholder(R.mipmap.ic_placeholder)
                    .centerCrop()
                    .into(imageView);
        }
        else{
            Glide.clear(imageView);
            imageView.setImageDrawable(null);
        }


        /*
        // Method for storing
        final ParseUser usuarioActual = ParseUser.getCurrentUser();


        botonMeGusta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseObject like = new ParseObject("MeGusta");
                like.put("usuario",usuarioActual);
                like.put("actividad",object);
                like.saveInBackground();

                object.increment("meGusta");
                object.saveInBackground();

                textLikes.setText(String.valueOf(object.getInt("meGusta")+1));
                // Paint Like button green

                botonMeGusta.setEnabled(false);

            }
        });
        */


        return v;
    }


}
