package logica;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
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
import java.util.List;

import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by Brahyam on 27/11/2015.
 */
public class ListarMensajesAdapter extends ParseQueryAdapter<ParseObject> {


    // Modify Default query to look for objects Actividad
    public ListarMensajesAdapter(Context context) {
        super(context, new ParseQueryAdapter.QueryFactory<ParseObject>() {
            public ParseQuery create() {
                ParseQuery query = new ParseQuery("Mensaje");
                query.include("autor");
                return query;
            }
        });
    }

    public View getItemView(final ParseObject object, View v, ViewGroup parent) {
        if (v == null) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista_mensajes, parent, false);
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

        // Load Values
        valueNombre.setText(creador.getString("nombre") + " " + creador.getString("Apellido"));
        valueEstado.setText(creador.getString("estado"));
        valueMunicipio.setText(creador.getString("municipio"));
        valueTexto.setText(object.getString("texto"));

        // Load Image
        ParseFile imagen = object.getParseFile("adjunto");
        if (imagen != null) {
            imageView.setVisibility(View.VISIBLE);
            String url = imagen.getUrl();
            Glide.with(getContext())
                    .load(url)
                    .placeholder(R.drawable.ic_image)
                    .centerCrop()
                    .into(imageView);
        } else {
            Glide.clear(imageView);
            imageView.setImageDrawable(null);
        }

        return v;
    }


}
