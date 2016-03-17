package com.burizalabs.soyactivista.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.burizalabs.soyactivista.R;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;


/**
 * Created by darwin on 16/12/2015.
 */
public class ListarMensajeDirectoParseAdapter extends ParseQueryAdapter<ParseObject> {


    private String TAG = "MensajeDirectoAdapter";
    final String usuarioActual = ParseUser.getCurrentUser().getObjectId();
    private String autorMensaje;

    public ListarMensajeDirectoParseAdapter(Context context, final String conversacionId) {
        super(context, new ParseQueryAdapter.QueryFactory<ParseObject>() {
            public ParseQuery create() {

                ParseQuery innerQuery = new ParseQuery("Conversacion");
                innerQuery.whereEqualTo("objectId", conversacionId);

                ParseQuery query = new ParseQuery("MensajeDirecto");
                query.whereMatchesQuery("conversacion", innerQuery);
                query.include("autor");
                query.include("conversacion");
                return query;
            }
        });

    }

    public View getItemView(ParseObject object, View v, ViewGroup parent){

        // Gets autor id align message right or left
        ParseUser autor = object.getParseUser("autor");
        autorMensaje = autor.getObjectId();

        if(v == null){

            if(usuarioActual.equals(autorMensaje))
            {
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista_mensaje_directo_derecha,parent,false);
            }
            else
            {
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista_mensaje_directo_izquierda,parent,false);
            }
        }

        super.getItemView(object, v, parent);

        // Add Fields
        final TextView valueNombre, valueApellido, valueEstado, valueMunicipio, valueTexto;
        ImageView previewAdjunto;

        valueNombre = (TextView)v.findViewById(R.id.valueNombre);
        valueApellido = (TextView)v.findViewById(R.id.valueApellido);
        valueEstado = (TextView)v.findViewById(R.id.valueEstado);
        valueMunicipio = (TextView)v.findViewById(R.id.valueMunicipio);
        valueTexto = (TextView)v.findViewById(R.id.valueMensaje);

        previewAdjunto = (ImageView)v.findViewById(R.id.valueAdjunto);

        valueNombre.setText(autor.getString("nombre"));
        valueApellido.setText(autor.getString("apellido"));
        valueEstado.setText(autor.getString("estado")+" -");
        valueMunicipio.setText(autor.getString("municipio"));
        valueTexto.setText(object.getString("texto"));

        if(object.getParseFile("adjunto")!= null){

            previewAdjunto.setVisibility(View.VISIBLE);
            String fileName = object.getParseFile("adjunto").getName();
            String extension = fileName.substring((fileName.lastIndexOf(".") + 1), fileName.length());

            //Attached File is an Image
            if(extension.equalsIgnoreCase("jpg")) {

                String url = object.getParseFile("adjunto").getUrl();

               Glide.with(getContext())
                        .load(url)
                        .placeholder(R.drawable.ic_image)
                        .centerCrop()
                        .into(previewAdjunto);
            }
            else{
                // Attached is a PDF File
                Glide.with(getContext())
                        .load(R.drawable.ic_archivo)
                        .into(previewAdjunto);
                previewAdjunto.setAdjustViewBounds(true);
            }
        }
        else
        {
            if(object.getParseGeoPoint("ubicacion") != null){
                String latMsg = String.valueOf(object.getParseGeoPoint("ubicacion").getLatitude());
                String lngMsg = String.valueOf(object.getParseGeoPoint("ubicacion").getLongitude());
                String url = "http://maps.google.com/maps/api/staticmap?center=" + latMsg+ "," +lngMsg +
                        "&zoom=15&size=500x400&maptype=roadmap&markers=color:red%7Clabel:U%7C" + latMsg + "," + lngMsg + "%7Csize:small&";
                object.getParseGeoPoint("ubicacion").getLongitude();
                previewAdjunto.setVisibility(View.VISIBLE);

                Glide.with(getContext())
                        .load(url).asBitmap().centerCrop()
                        .into(previewAdjunto);
                previewAdjunto.setAdjustViewBounds(true);
            }
            else{
                previewAdjunto.setVisibility(View.GONE);
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
