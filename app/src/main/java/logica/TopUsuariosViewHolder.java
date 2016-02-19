package logica;

import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.parse.ParseFile;

import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by Luis Adrian on 27/01/2016.
 */
public class TopUsuariosViewHolder extends RecyclerView.ViewHolder {


    protected TextView nombreUsuario, cargo, entidad, posicion, puntosActivismo;
    protected CardView card;
    protected LinearLayout linear;
    protected ImageView photo;
    protected int ptos = 0;

    public TopUsuariosViewHolder(View itemView) {
        super(itemView);

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

    public void setUsuario(Usuario usuario, int nPosicion, String entity){

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


}
