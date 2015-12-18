package logica;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.usuario.soyactivista.fragments.FragmentVerImagen;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by darwin on 16/12/2015.
 */
public class ListarMensajesDirectosParse extends ParseQueryAdapter<ParseObject> {


    final String usuarioActual = ParseUser.getCurrentUser().getObjectId();
    private String usuario_sent_msg="";
    private ImageView previewAdjunto;


    public ListarMensajesDirectosParse(Context context, final String conversacionid) {
        super(context, new ParseQueryAdapter.QueryFactory<ParseObject>() {
            public ParseQuery create() {

                ParseQuery innerQuery = new ParseQuery("Conversacion");
                innerQuery.whereEqualTo("objectId", conversacionid);

                ParseQuery query = new ParseQuery("MensajeDirecto");
                query.whereMatchesQuery("conversacion", innerQuery);
                query.include("autor");
                return query;
            }
        });

    }



    public View getItemView(ParseObject object, View v, ViewGroup parent){

        ParseUser user = object.getParseUser("autor");
        usuario_sent_msg=user.getObjectId();

        if(v == null){

            if(usuarioActual.equals(usuario_sent_msg))
            {
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_right,parent,false);
            }
            else
            {
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_left,parent,false);
            }
            previewAdjunto = (ImageView)v.findViewById(R.id.valueAdjunto);
        }

        super.getItemView(object, v, parent);

        // Add Fields
        TextView escritor_msg = (TextView)v.findViewById(R.id.lbl1);
        TextView body_msg = (TextView)v.findViewById(R.id.lbl2);

        escritor_msg.setText(user.getString("nombre") + " " + user.getString("apellido") + " | " + user.getString("estado") + "-" + user.getString("municipio"));
        body_msg.setText(object.getString("texto"));




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
                        .centerCrop()
                        .into(previewAdjunto);
                previewAdjunto.setAdjustViewBounds(true);
            }

        }
        else
        {
            previewAdjunto.setVisibility(View.GONE);


        }

        
        return v;
    }



}
