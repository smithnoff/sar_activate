package logica;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.usuario.soyactivista.fragments.FragmentEditarUsuario;
import com.example.usuario.soyactivista.fragments.FragmentPuntuaciones;
import com.parse.ParseFile;

import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by Luis Adrian on 27/01/2016.
 */
public class TopUsuariosViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


    protected TextView nombreUsuario, cargo, entidad, posicion, puntosActivismo;
    protected CardView card;
    protected LinearLayout linear;
    protected ImageView photo;


    private Usuario usuario;
    private ActivityPantallaMenu activity;

    public TopUsuariosViewHolder(View itemView) {
        super(itemView);

        itemView.setOnClickListener(this);

        // Set Holders
        posicion = (TextView) itemView.findViewById(R.id.position);
        nombreUsuario = (TextView) itemView.findViewById(R.id.nombreUsuario);
        cargo = (TextView) itemView.findViewById(R.id.cargo);
        entidad = (TextView) itemView.findViewById(R.id.nombreEntidad);
        puntosActivismo = (TextView)itemView.findViewById(R.id.puntosUsuario);
        card = (CardView) itemView;
        linear = (LinearLayout)itemView.findViewById(R.id.linearUsuarios);
        photo = (ImageView)itemView.findViewById(R.id.person_photo);
    }


    public void setUsuario(Usuario usuario, int nPosicion, String entity, ActivityPantallaMenu activity, Boolean onClick){

            // Store Entidad
            this.usuario = usuario;

            // Store Context
            this.activity = activity;

            posicion.setText(String.valueOf(nPosicion));
            nombreUsuario.setText("Nombre: " + usuario.getNombre()+" "+usuario.getApellido());
            cargo.setText("Cargo: "+ usuario.getCargo());
            entidad.setText(entity + ": " + usuario.getMunicipio());
            puntosActivismo.setText("Puntos: "+String.valueOf(usuario.getPuntosActivismo()));

        ParseFile foto = usuario.getFoto();
        if (foto != null)
        {
            String fileName = foto.getName();
            String extension = fileName.substring((fileName.lastIndexOf(".") + 1), fileName.length());
            //Attached File is an Image
            if (extension.equalsIgnoreCase("jpg"))
            {
                String url = foto.getUrl();
                Glide.with(itemView.getContext()).load(url).asBitmap().centerCrop().into(new BitmapImageViewTarget(photo)
                {
                    @Override
                    protected void setResource(Bitmap resource)
                    {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(itemView.getContext().getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        photo.setImageDrawable(circularBitmapDrawable);
                    }
                });
            }
        }

    }

    @Override
    public void onClick(View v) {

        ParseFile pic;
        pic = usuario.getFoto();
        Bundle datos = new Bundle();
        datos.putString("nombre", usuario.getNombre());
        datos.putString("username", usuario.getUsername());
        datos.putString("nombre", usuario.getNombre());
        datos.putString("apellido", usuario.getApellido());
        datos.putString("email", usuario.getEmail());
        datos.putString("estado", usuario.getEstado());
        datos.putString("municipio", usuario.getMunicipio());
        datos.putString("parroquia", usuario.getParroquia());
        datos.putString("cargo", usuario.getCargo());
        datos.putString("comite", usuario.getComite());
        datos.putString("rol", usuario.getRolName());
        datos.putInt("puntos", usuario.getPuntos());
        datos.putInt("puntosActivismo", usuario.getPuntosActivismo());
        if( pic != null)
            datos.putString("foto",pic.getUrl());


        // Redirect View to next Fragment
        Fragment fragment = new FragmentEditarUsuario();

        fragment.setArguments(datos);
        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, fragment)
                .addToBackStack(null)
                .commit();
    }


}
