package logica;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import android.app.AlertDialog;
import android.content.DialogInterface;


import android.app.AlertDialog;
import android.content.DialogInterface;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import com.parse.ParseException;

import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by Brahyam on 28/11/2015.
 */
public class ListarActividadAdapter extends ParseQueryAdapter<ParseObject> {

    private String TAG = "ListarActividadAdapter";
    private ArrayList<String> likes;
    private ParseUser currentUser;

    // Modify Default query to look for objects Actividad
    public ListarActividadAdapter(Context context, ArrayList<String> likes) {
        super(context, new ParseQueryAdapter.QueryFactory<ParseObject>() {
            public ParseQuery create() {
                ParseQuery query = new ParseQuery("Actividad");
                query.include("tipoActividad");
                query.include("creador");
                return query;
            }
        });

        currentUser = ParseUser.getCurrentUser();
        // Load like list
        this.likes = likes;
    }

    // Modify Default query to look for objects Actividad
    public ListarActividadAdapter(Context context, ArrayList<String> likes, final String constraint) {
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

        currentUser = ParseUser.getCurrentUser();
        this.likes = likes;

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
        final ImageButton botonMeGusta, botonNoMeGusta;
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

        separator = v.findViewById(R.id.separator);

        botonMeGusta = (ImageButton) v.findViewById(R.id.botonMeGusta);
        botonNoMeGusta = (ImageButton) v.findViewById(R.id.botonNoMeGusta);


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


        // Find out if User already liked activity
        if(likes.contains(object.getObjectId())){
            botonMeGusta.setEnabled(false);
            botonMeGusta.setVisibility(View.GONE);
            botonNoMeGusta.setVisibility(View.VISIBLE);
            textLikes.setTextColor(getContext().getResources().getColor(R.color.verde));
        }

        botonMeGusta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ParseObject like = new ParseObject("MeGusta");
                like.put("usuario",currentUser);
                like.put("actividad",object);
                like.saveInBackground();

                object.increment("meGusta");
                object.saveInBackground();

                textLikes.setText(String.valueOf(object.getInt("meGusta")));
                // Paint Like text green
                textLikes.setTextColor(getContext().getResources().getColor(R.color.verde));

                botonMeGusta.setEnabled(false);

            }
        });

        // Likes Behavior
        botonNoMeGusta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ParseQuery<ParseObject> query = ParseQuery.getQuery("MeGusta");
                query.whereEqualTo("actividad", object);
                query.whereEqualTo("usuario", currentUser);

                query.getFirstInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject object, ParseException e) {
                        if (e == null) {
                            object.deleteInBackground();
                        } else {
                            Log.d(TAG, e.getMessage());
                        }
                    }
                });

                object.increment("meGusta", -1);
                object.saveInBackground();


                textLikes.setText(String.valueOf(object.getInt("meGusta")));

                // Paint Like button green
                botonNoMeGusta.setVisibility(View.GONE);
                botonNoMeGusta.setEnabled(false);
                textLikes.setTextColor(getContext().getResources().getColor(R.color.grisOscuro));


                botonMeGusta.setVisibility(View.VISIBLE);
                botonMeGusta.setEnabled(true);
            }
        });

        return v;
    }


}
