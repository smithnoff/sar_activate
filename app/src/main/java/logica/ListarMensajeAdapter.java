package logica;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by Brahyam on 27/11/2015.
 */
public class ListarMensajeAdapter extends ParseQueryAdapter<ParseObject> {


    // Modify Default query to look for objects Actividad
    public ListarMensajeAdapter(Context context) {
        super(context, new ParseQueryAdapter.QueryFactory<ParseObject>() {
            public ParseQuery create() {
                ParseQuery query = new ParseQuery("Mensaje");
                query.include("autor");
                query.orderByDescending("createdAt");
                return query;
            }
        });
    }

    public View getItemView(final ParseObject object, View v, ViewGroup parent) {
        if (v == null) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista_mensaje, parent, false);
        }

        super.getItemView(object, v, parent);

        ParseUser creador = object.getParseUser("autor");

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
        valueEstado.setText(creador.getString("estado"));
        valueMunicipio.setText(creador.getString("municipio"));
        valueTexto.setText(object.getString("texto"));

        // Load Image
        ParseFile adjunto = object.getParseFile("adjunto");
        ParseGeoPoint ubicacion = object.getParseGeoPoint("ubicacion");
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
                Glide.with(getContext())
                        .load(R.drawable.ic_place)
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


}
