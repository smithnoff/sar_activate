package logica;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import soy_activista.quartzapp.com.soy_activista.R;

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
        valueEstado.setText(autor.getString("estado"));
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
                previewAdjunto.setVisibility(View.VISIBLE);

                Glide.with(getContext())
                        .load(R.drawable.ic_place)
                        .into(previewAdjunto);
                previewAdjunto.setAdjustViewBounds(true);
            }
            else{
                previewAdjunto.setVisibility(View.GONE);
            }

        }

        return v;
    }



}