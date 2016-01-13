package logica;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import android.app.AlertDialog;
import android.content.DialogInterface;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import com.parse.ParseException;

import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by Brahyam on 28/11/2015.
 */
public class ListarActividadParseAdapter extends ParseQueryAdapter<ParseObject> {

    private String TAG = "ListarActividadParseAdapter";
    private ArrayList<String> likes;
    private ParseUser currentUser;
    private String cantidad_likes;



    // Modify Default query to look for objects Actividad
    public ListarActividadParseAdapter(Context context, ArrayList<String> likes) {
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
    public ListarActividadParseAdapter(Context context, ArrayList<String> likes, final String constraint) {
        super(context, new ParseQueryAdapter.QueryFactory<ParseObject>() {
            public ParseQuery create() {

                // Determine type of constraint
                String[] queryArray = constraint.toString().split("=");
                String value = queryArray[1];


                // Start Query
                ParseQuery query = new ParseQuery("Actividad");

                switch (queryArray[0]){
                    case "estatus":
                        query.whereEqualTo("estatus",value);
                        query.orderByDescending("createdAt");

                        break;
                    case "ubicacion":
                        query.whereEqualTo("ubicacion",value);
                        query.orderByDescending("createdAt");

                        break;
                    case "estado":
                        // Query is asking for whole list, skip search.
                        if(value.equals("Todos")) {
                            query.orderByDescending("createdAt");
                            break;
                        }
                        else
                        {
                            query.whereEqualTo("ubicacion","Estadal");
                            query.whereEqualTo("estado",value);
                            query.orderByDescending("createdAt");
                            break;
                        }


                    case "propios":

                        ParseQuery<ParseObject> innerQuery2 = ParseQuery.getQuery("_User");
                        innerQuery2.whereEqualTo("username", value);
                        query.whereMatchesQuery("creador", innerQuery2);
                        query.orderByDescending("createdAt");
                        break;

                    case "meGusta":


                        query.orderByDescending("meGusta");
                        break;

                    default:

                        break;
                }

                query.include("tipoActividad");
                query.include("creador");

                return query;
            }
        });

        currentUser = ParseUser.getCurrentUser();
        this.likes = likes;

    }

    public View getItemView(final ParseObject object, View vista, ViewGroup parent){
    // patron ViewHolder

        View v;
        final   ViewHolder holder;


        if (vista == null) {
            v = new View(parent.getContext());
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista_actividad,parent,false);

            holder = new ViewHolder();
            holder.textNombre = (TextView)v.findViewById(R.id.nombreActividad);
            holder.textEstatus = (TextView)v.findViewById(R.id.valueEjecucion);
            holder.textCreador = (TextView)v.findViewById(R.id.valueCreador);
            holder.textInicio = (TextView)v.findViewById(R.id.valueInicio);
            holder.textFin = (TextView)v.findViewById(R.id.valueFin);
            holder.textLikes = (TextView)v.findViewById(R.id.valueLikes);

            holder.separator = v.findViewById(R.id.separator);

            holder.botonMeGusta = (ImageButton) v.findViewById(R.id.botonMeGusta);
            holder.botonNoMeGusta = (ImageButton) v.findViewById(R.id.botonNoMeGusta);

            holder.imageView1 = (ImageView)v.findViewById(R.id.imagen1);
            holder.imageView2 = (ImageView)v.findViewById(R.id.imagen2);
            holder.imageView3 = (ImageView)v.findViewById(R.id.imagen3);
            holder.imageView4= (ImageView)v.findViewById(R.id.imagen4);

            // Find out if User already liked activity
            if(likes.contains(object.getObjectId())){
                holder.botonMeGusta.setEnabled(false);
                holder.botonMeGusta.setVisibility(View.GONE);
                holder.botonNoMeGusta.setVisibility(View.VISIBLE);
                holder.textLikes.setTextColor(getContext().getResources().getColor(R.color.verde));
            }

            v.setTag(holder);

        } else {
            v = vista;

            holder = (ViewHolder)v.getTag();

            if(likes.contains(object.getObjectId())){
                holder.botonMeGusta.setEnabled(false);
                holder.botonMeGusta.setVisibility(View.GONE);
                holder.botonNoMeGusta.setVisibility(View.VISIBLE);
                holder.botonNoMeGusta.setEnabled(true);
                holder.textLikes.setTextColor(getContext().getResources().getColor(R.color.verde));
            }
            else
            {
                holder.botonMeGusta.setEnabled(true);
                holder.botonMeGusta.setVisibility(View.VISIBLE);
                holder.botonNoMeGusta.setVisibility(View.GONE);
                holder.botonNoMeGusta.setEnabled(false);
                holder.textLikes.setTextColor(getContext().getResources().getColor(R.color.grisOscuro));
            }
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


        // TODO: Use ViewHolder to improve performance.
        // Assign to holders

        // Load Values
        holder.textNombre.setText(tipoActividad.getString("nombre"));
        holder.textEstatus.setText(object.getString("estatus"));
        holder.textCreador.setText(creador.getString("nombre")+" "+creador.getString("apellido"));

        cantidad_likes = String.valueOf(object.getInt("meGusta"));

        holder.textLikes.setText(String.valueOf(object.getInt("meGusta")));

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        holder.textInicio.setText(dateFormat.format(object.getDate("inicio")));
        holder.textFin.setText(dateFormat.format(object.getDate("fin")));

        // Paint Status Accordingly
        if(object.getString("estatus").equals("En Ejecución"))
            holder.textEstatus.setTextColor(getContext().getResources().getColor(R.color.verde));
        else
            holder.textEstatus.setTextColor(getContext().getResources().getColor(R.color.rojo));

        // Load Images

        ParseFile imagen1 = object.getParseFile("imagen1");
        ParseFile imagen2 = object.getParseFile("imagen2");
        ParseFile imagen3 = object.getParseFile("imagen3");
        ParseFile imagen4 = object.getParseFile("imagen4");

        String url;

        if(imagen1 != null){
            holder.separator.setVisibility(View.VISIBLE);
            holder.imageView1.setVisibility(View.VISIBLE);
            url = imagen1.getUrl();
            Glide.with(getContext())
                    .load(url)
                    .centerCrop()
                    .into(holder.imageView1);
        }
        else{
            Glide.clear(holder.imageView1);
            holder.imageView1.setImageDrawable(null);
            holder.imageView1.setVisibility(View.GONE);
        }

        if(imagen2 != null){
            holder.separator.setVisibility(View.VISIBLE);
            holder.imageView2.setVisibility(View.VISIBLE);
            url = imagen2.getUrl();
            Glide.with(getContext())
                    .load(url)
                    .centerCrop()
                    .into(holder.imageView2);
        }
        else{
            Glide.clear(holder.imageView2);
            holder.imageView2.setImageDrawable(null);
            holder.imageView2.setVisibility(View.GONE);
        }

        if(imagen3 != null){
            holder.separator.setVisibility(View.VISIBLE);
            holder.imageView3.setVisibility(View.VISIBLE);
            url = imagen3.getUrl();
            Glide.with(getContext())
                    .load(url)
                    .centerCrop()
                    .into(holder.imageView3);
        }
        else{
            Glide.clear(holder.imageView3);
            holder.imageView3.setImageDrawable(null);
            holder.imageView3.setVisibility(View.GONE);
        }

        if(imagen4 != null){
            holder.separator.setVisibility(View.VISIBLE);
            holder.imageView4.setVisibility(View.VISIBLE);
            url = imagen4.getUrl();
            Glide.with(getContext())
                    .load(url)
                    .centerCrop()
                    .into(holder.imageView4);
        }
        else{
            Glide.clear(holder.imageView4);
            holder.imageView4.setImageDrawable(null);
            holder.imageView4.setVisibility(View.GONE);

        }



        holder.botonMeGusta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ParseObject like = new ParseObject("MeGusta");
                like.put("usuario", currentUser);
                like.put("actividad", object);
                like.saveInBackground();

                object.increment("meGusta");
                object.saveInBackground();
                holder.textLikes.setText(String.valueOf(object.getInt("meGusta")));

                // Paint Like text green
                holder.botonMeGusta.setVisibility(View.GONE);
                holder.botonMeGusta.setEnabled(false);
                holder.textLikes.setTextColor(getContext().getResources().getColor(R.color.verde));
               // botonMeGusta.setEnabled(false);
                holder.botonNoMeGusta.setVisibility(View.VISIBLE);
                holder.botonNoMeGusta.setEnabled(true);

                likes.add(object.getObjectId());

            }
        });


        // Likes Behavior
        holder.botonNoMeGusta.setOnClickListener(new View.OnClickListener() {
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

                            // Paint Like button green
                            holder.botonNoMeGusta.setVisibility(View.GONE);
                            holder.botonNoMeGusta.setEnabled(false);

                            holder.botonMeGusta.setVisibility(View.VISIBLE);
                            holder.botonMeGusta.setEnabled(true);
                            holder.textLikes.setTextColor(getContext().getResources().getColor(R.color.grisOscuro));

                        } else {
                            Log.d(TAG, e.getMessage());
                        }
                    }
                });
                object.increment("meGusta", -1);
                object.saveInBackground();
                holder.textLikes.setText(String.valueOf(object.getInt("meGusta")));
                likes.remove(object.getObjectId());
            }
        });

        return v;
    }


}


 class ViewHolder {

      TextView textNombre,textEstatus,textCreador,textInicio,textFin,textLikes;
      ImageButton botonMeGusta, botonNoMeGusta;
      ImageView imageView1,imageView2,imageView3,imageView4;
      View separator;

 }
