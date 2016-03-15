package com.burizalabs.soyactivista.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.burizalabs.soyactivista.R;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import com.burizalabs.soyactivista.utils.ErrorCodeHelpers;

/**
 * Created by Brahyam on 27/11/2015.
 */
public class ListarMensajeParseAdapter extends ParseQueryAdapter<ParseObject> {


    private static final String TAG = "MensajeAdapter";

    // Modify Default query to look for objects Mensaje
    public ListarMensajeParseAdapter(Context context) {
        super(context, new ParseQueryAdapter.QueryFactory<ParseObject>() {
            public ParseQuery create() {
                ParseQuery query = new ParseQuery("Mensaje");
                query.include("autor");
                query.orderByDescending("createdAt");
                return query;
            }
        });
    }

    // Modify Default query to look for objects Mensaje with Constraint
    public ListarMensajeParseAdapter(Context context, final String constraint) {
        super(context, new ParseQueryAdapter.QueryFactory<ParseObject>() {
            public ParseQuery create() {

                Log.d(TAG, "Filtering with query "+constraint);

                // Initialize Query on table Mensaje
                ParseQuery query = new ParseQuery("Mensaje");

                // Determine type of constraint
                String[] queryArray = constraint.toString().split("=");
                String value = queryArray[1];

                switch (queryArray[0]){
                    case "estado":

                        // Query is asking for whole list skip search.
                        if(value.equals("Todos"))
                            break;

                        Log.d(TAG, "value " + value);
                        // Set new Query
                        ParseQuery<ParseObject> innerQuery = ParseQuery.getQuery("_User");
                        innerQuery.whereEqualTo("estado", value);
                        query.whereMatchesQuery("autor", innerQuery);
                        break;

                    case "propios":
                        Log.d(TAG, "value "+value);
                        ParseQuery<ParseObject> innerQuery2 = ParseQuery.getQuery("_User");
                        innerQuery2.whereEqualTo("username", value);
                        query.whereMatchesQuery("autor", innerQuery2);
                        break;

                    case "reportados":
                        Log.d(TAG, "value "+value);
                        query.whereEqualTo("reportado", true);
                        break;

                    default:
                        Log.d(TAG, "Returning Default Result");
                        break;
                }

                query.include("autor");
                query.orderByDescending("createdAt");
                return query;
            }
        });
    }

    public View getItemView(final ParseObject mensaje, View v, ViewGroup parent) {
        if (v == null) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista_mensaje, parent, false);
        }

        super.getItemView(mensaje, v, parent);

        // TODO: Validate User not null
        ParseUser creador = mensaje.getParseUser("autor");

        if (creador == null){
            ErrorCodeHelpers.resolveLogErrorString(00, "User not found");
            creador = new ParseUser();
            creador.put("nombre","Usuario Desconocido");
            creador.put("apellido","");
            creador.put("estado","");
            creador.put("municipio","");
        }

        //Declare all fields
        final TextView valueNombre, valueEstado, valueMunicipio, valueTexto;
        final ImageView imageView;

        // Assign to holders
        valueNombre = (TextView) v.findViewById(R.id.valueCreador);
        valueEstado = (TextView) v.findViewById(R.id.valueEstado);
        valueMunicipio = (TextView) v.findViewById(R.id.valueMunicipio);
        valueTexto = (TextView) v.findViewById(R.id.valueTexto);
        imageView = (ImageView) v.findViewById(R.id.valueAdjunto);
        imageView.setVisibility(View.GONE);

        // Load Values
        valueNombre.setText(creador.getString("nombre") + " " + creador.getString("apellido"));
        valueEstado.setText(creador.getString("estado")+" - "+creador.getString("municipio"));
        //valueMunicipio.setText(creador.getString("municipio"));
        valueTexto.setText(mensaje.getString("texto"));

        // Load Image
        ParseFile adjunto = mensaje.getParseFile("adjunto");
        ParseGeoPoint ubicacion = mensaje.getParseGeoPoint("ubicacion");
        if (adjunto != null) {
            imageView.setVisibility(View.VISIBLE);
            String fileName = adjunto.getName();
            String extension = fileName.substring((fileName.lastIndexOf(".") + 1), fileName.length());

            //Attached File is an Image
            if(extension.equalsIgnoreCase("jpg")) {
                String url = adjunto.getUrl();
                Glide.with(getContext())
                        .load(url)
                        .placeholder(R.drawable.ic_image)
                        .centerCrop()
                        .into(imageView);
            }
            else{
                Glide.with(getContext())
                        .load(R.drawable.ic_archivo)
                        .centerCrop()
                        .into(imageView);
            }

        } else {

            if(ubicacion != null){
                imageView.setVisibility(View.VISIBLE);

                String latMsg = String.valueOf(mensaje.getParseGeoPoint("ubicacion").getLatitude());
                String lngMsg = String.valueOf(mensaje.getParseGeoPoint("ubicacion").getLongitude());
                String url = "http://maps.google.com/maps/api/staticmap?center=" + latMsg+ "," +lngMsg +
                        "&zoom=15&size=500x400&maptype=roadmap&markers=color:red%7Clabel:U%7C" + latMsg + "," + lngMsg + "%7Csize:small&";
                Glide.with(getContext())
                        .load(url).asBitmap()
                        .centerCrop()
                        .into(imageView);
            }
            else{
                Glide.clear(imageView);
                imageView.setImageDrawable(null);
            }

        }

        return v;
    }

    @Override
    public View getNextPageView(View v, ViewGroup parent) {
        if (v == null) {
            //   v = getDefaultView(parent.getContext());
            v = View.inflate(getContext(), R.layout.list_load_more_footer, null);

        }
        TextView textView = (TextView) v.findViewById(R.id.load_more); //set the button
        textView.setText("Cargar más mensajes...");
        return v;
    }


}
