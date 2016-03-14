package com.burizalabs.soyactivista.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import com.burizalabs.soyactivista.R;


public class FragmentDetalleMensaje extends Fragment {

    private TextView valueNombre,valueEstado,valueMunicipio,valueFecha,valueTexto, nameFile;
    private ImageView previewAdjunto, photoUser;
    private Button botonReportar;
    private Button botonEliminar;
    private ProgressBar ProgressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_detalle_mensaje, container, false);

        // Assign holders
        valueNombre = (TextView)v.findViewById(R.id.valueNombre);
        valueEstado = (TextView)v.findViewById(R.id.valueEstado);
        valueMunicipio = (TextView)v.findViewById(R.id.valueMunicipio);
        valueFecha = (TextView)v.findViewById(R.id.valueFecha);
        valueTexto = (TextView)v.findViewById(R.id.valueTexto);
        nameFile =  (TextView)v.findViewById(R.id.nameFile);

        previewAdjunto = (ImageView)v.findViewById(R.id.valueAdjunto);
        photoUser = (ImageView)v.findViewById(R.id.messageUser);

        botonReportar = (Button)v.findViewById(R.id.botonReportar);
        botonEliminar = (Button)v.findViewById(R.id.botonEliminar);
        ProgressBar = (ProgressBar)v.findViewById(R.id.ProgressBar);

        //Gets Current User
        final ParseUser usuarioActual = ParseUser.getCurrentUser();

        // Set Values
        valueNombre.setText(getArguments().getString("nombre"));
        valueEstado.setText(getArguments().getString("estado"));
        valueMunicipio.setText(getArguments().getString("municipio"));
        valueFecha.setText(getArguments().getString("fechaCreacion"));
        valueTexto.setText(getArguments().getString("texto"));

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("objectId",getArguments().getString("autor"));
        query.getFirstInBackground(new GetCallback<ParseUser>() {
            public void done(ParseUser object, ParseException e) {
                if (object == null) {

                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                } else
                {

                    ParseFile foto = object.getParseFile("fotoPerfil");
                    if (foto != null)
                    {
                        String fileName = foto.getName();
                        String extension = fileName.substring((fileName.lastIndexOf(".") + 1), fileName.length());
                        //Attached File is an Image
                        if (extension.equalsIgnoreCase("jpg"))
                        {
                            String url = foto.getUrl();
                            Glide.with(getContext()).load(url).asBitmap().centerCrop().into(new BitmapImageViewTarget(photoUser)
                            {
                                @Override
                                protected void setResource(Bitmap resource)
                                {
                                    RoundedBitmapDrawable circularBitmapDrawable =
                                            RoundedBitmapDrawableFactory.create(getContext().getResources(), resource);
                                    circularBitmapDrawable.setCircular(true);
                                    photoUser.setImageDrawable(circularBitmapDrawable);
                                }
                            });
                        }
                    }
                }
            }
        });

        if(getArguments().getBoolean("reportado") == true)
        {
            valueTexto.setTextColor(Color.RED);
        }


        if(getArguments().getString("adjunto") != null){

            previewAdjunto.setVisibility(View.VISIBLE);
            String fileName = getArguments().getString("nombreAdjunto");
            String extension = fileName.substring((fileName.lastIndexOf(".") + 1), fileName.length());

            //Attached File is an Image
            if(extension.equalsIgnoreCase("jpg")) {
                ProgressBar.setVisibility(View.VISIBLE);
                String url = getArguments().getString("adjunto");

                Glide.with(getContext())
                        .load(url)
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                ProgressBar.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                ProgressBar.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .centerCrop()
                        .into(previewAdjunto);

                previewAdjunto.setOnClickListener(seeImageDetail(getArguments().getString("adjunto")));
            }
            else{
                String file = getArguments().getString("nombreAdjunto");
                nameFile.setText("Descargar: "+file);
                nameFile.setVisibility(View.VISIBLE);

                // Get Primary Color
                TypedValue typedValue = new TypedValue();
                getContext().getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
                int color = typedValue.data;

                previewAdjunto.setVisibility(View.VISIBLE);

                // Attached is a PDF File
                Glide.with(getContext())
                        .load(R.drawable.ic_download)
                        .into(previewAdjunto);
                previewAdjunto.setColorFilter(color);
                previewAdjunto.setAdjustViewBounds(true);
                previewAdjunto.setOnClickListener(seePDFDetail(getArguments().getString("adjunto")));

            }

        }

        // Have Attached location
        if(getArguments().getString("ubicacion") != null)
        {
            int color = R.color.colorPrimary;
            String latMsg = getArguments().getString("latitud");
            String lngMsg = getArguments().getString("longitud");
            //String url = "http://maps.google.com/maps/api/staticmap?center=" + latMsg + "," + lngMsg + "&zoom=15&size=600x300&sensor=false";
            String url = "http://maps.google.com/maps/api/staticmap?center=" + latMsg+ "," +lngMsg +
                    "&zoom=15&size=500x400&maptype=roadmap&markers=color:"+ color +"%7Clabel:U%7C" + latMsg + "," + lngMsg + "%7Csize:small&";

            previewAdjunto.setVisibility(View.VISIBLE);
            Glide.with(getContext())
                    .load(url).asBitmap().centerCrop()
                    .into(previewAdjunto);

            previewAdjunto.setAdjustViewBounds(true);
            Log.d("INTENT LOCATION","Location is:"+getArguments().getString("ubicacion"));
            previewAdjunto.setOnClickListener(seeLocationDetail(getArguments().getString("ubicacion")));
        }

        // Hide reported button if the message was reported before / Hide when Direct Message / Hide when is owner message
        if(getArguments().getBoolean("reportado")
                || getArguments().getBoolean("directo")
                || usuarioActual.getObjectId().equals(getArguments().get("autor"))){
            botonReportar.setVisibility(View.GONE);
            botonReportar.setEnabled(false);
        }

        //Show Delete button only if the user role is "1" & Not a direct Message or message owner.
        if(usuarioActual.getInt("rol")== 1 && !getArguments().getBoolean("directo") || usuarioActual.getObjectId().equals(getArguments().get("autor"))){
            botonEliminar.setVisibility(View.VISIBLE);
            botonEliminar.setEnabled(true);
        }
        
        // On Click listener to report Message
        botonReportar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Confirmar");
                builder.setMessage("¿Está seguro que desea reportar este mensaje?");

                builder.setPositiveButton("Reportar", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialogo, int which) {
                        ParseObject mensaje = ParseObject.createWithoutData("Mensaje", getArguments().getString("id"));
                        mensaje.put("reportado", true);
                        mensaje.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                Toast.makeText(getContext(), "Mensaje reportado.", Toast.LENGTH_SHORT).show();
                                botonReportar.setVisibility(View.GONE);
                                botonReportar.setEnabled(false);
                            }
                        });
                        dialogo.dismiss();
                    }

                });

                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogo, int which) {
                        // Do nothing
                        dialogo.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();




            }
        });

        // On Click listener to Eliminated Message
        botonEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Confirmar");
                builder.setMessage("¿Está seguro que desea eliminar el mensaje?");

                builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialogo, int which) {
                        dialogo.dismiss();
                        ParseObject mensaje;
                        if (getArguments().getBoolean("directo")) {
                            mensaje = ParseObject.createWithoutData("MensajeDirecto", getArguments().getString("id"));
                        } else {
                            mensaje = ParseObject.createWithoutData("Mensaje", getArguments().getString("id"));
                        }

                        mensaje.deleteInBackground();
                        Toast.makeText(getContext(), "Mensaje eliminado.", Toast.LENGTH_SHORT).show();

                        // Redirect User
                        // If public message dashboard
                        if (!getArguments().getBoolean("directo")) {
                            Fragment fragment = new FragmentListarMensaje();
                            getFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.content_frame, fragment)
                                    .commit();

                        } else { // Redirect to private Conversation
                            Bundle datos = new Bundle();
                            datos.putString("conversacionId", getArguments().getString("conversacionId"));

                            Fragment fragment = new FragmentListarMensajeDirecto();
                            fragment.setArguments(datos);
                            getFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.content_frame, fragment)
                                    .commit();
                        }

                    }

                });

                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogo, int which) {
                        // Do nothing
                        dialogo.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        return v;
    }

    // Listener for image details
    public View.OnClickListener seeImageDetail(final String url){
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle data = new Bundle();
                data.putString("imageUrl",url);
                // Redirect View to next Fragment
                Fragment fragment = new FragmentVerImagen();
                fragment.setArguments(data);
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        };
        return listener;
    }

    // Listener for PDFs
    public View.OnClickListener seePDFDetail(final String url){
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
        };
        return listener;
    }

    // Listener for Locations
    public View.OnClickListener seeLocationDetail(final String location){
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Creates an Intent that will load a map
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q="+location));
                startActivity(intent);
            }
        };
        return listener;
    }



}
