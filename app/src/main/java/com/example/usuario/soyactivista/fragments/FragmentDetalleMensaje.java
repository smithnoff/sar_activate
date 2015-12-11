package com.example.usuario.soyactivista.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.bumptech.glide.Glide;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import soy_activista.quartzapp.com.soy_activista.R;


public class FragmentDetalleMensaje extends Fragment {

    private TextView valueNombre,valueEstado,valueMunicipio,valueFecha,valueTexto;
    private ImageView previewAdjunto;
    private Button botonReportar;
    private Button botonEliminar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_detalle_mensaje, container, false);

        // Assign holders
        valueNombre = (TextView)v.findViewById(R.id.valueNombre);
        valueEstado = (TextView)v.findViewById(R.id.valueEstado);
        valueMunicipio = (TextView)v.findViewById(R.id.valueMunicipio);
        valueFecha = (TextView)v.findViewById(R.id.valueFecha);
        valueTexto = (TextView)v.findViewById(R.id.valueTexto);

        previewAdjunto = (ImageView)v.findViewById(R.id.valueAdjunto);

        botonReportar = (Button)v.findViewById(R.id.botonReportar);
        botonEliminar = (Button)v.findViewById(R.id.botonEliminar);

        //Gets Current User
        final ParseUser usuarioActual = ParseUser.getCurrentUser();

        // Set Values
        valueNombre.setText(getArguments().getString("nombre"));
        valueEstado.setText(getArguments().getString("estado"));
        valueMunicipio.setText(getArguments().getString("municipio"));
        valueFecha.setText(getArguments().getString("fechaCreacion"));
        valueTexto.setText(getArguments().getString("texto"));

        if(getArguments().getString("adjunto") != null){

            previewAdjunto.setVisibility(View.VISIBLE);
            String fileName = getArguments().getString("nombreAdjunto");
            String extension = fileName.substring((fileName.lastIndexOf(".") + 1), fileName.length());

            //Attached File is an Image
            if(extension.equalsIgnoreCase("jpg")) {
                String url = getArguments().getString("adjunto");
                Glide.with(getContext())
                        .load(url)
                        .placeholder(R.drawable.ic_image)
                        .centerCrop()
                        .into(previewAdjunto);

                previewAdjunto.setOnClickListener(seeImageDetail(getArguments().getString("adjunto")));
            }
            else{
                // Attached is a PDF File
                Glide.with(getContext())
                        .load(R.drawable.ic_archivo)
                        .centerCrop()
                        .into(previewAdjunto);

                previewAdjunto.setOnClickListener(seePDFDetail(getArguments().getString("adjunto")));

            }

        }

        // Have Attached location
        if(getArguments().getString("ubicacion") != null){
            previewAdjunto.setVisibility(View.VISIBLE);
            Glide.with(getContext())
                    .load(R.drawable.ic_place)
                    .centerCrop()
                    .into(previewAdjunto);

            Log.d("INTENT LOCATION","Location is:"+getArguments().getString("ubicacion"));
            previewAdjunto.setOnClickListener(seeLocationDetail(getArguments().getString("ubicacion")));
        }

        // Show reported button only if the message was not reported before
        if(getArguments().getBoolean("reportado")){
            botonReportar.setVisibility(View.GONE);
            botonReportar.setEnabled(false);
        }

        //Show EliminarMensaje button only if the user role is "1" or owner message.
        if(usuarioActual.getInt("rol")== 1 || usuarioActual.equals(getArguments().get("autor"))){
            botonEliminar.setVisibility(View.VISIBLE);
            botonEliminar.setEnabled(true);
        }

        // On Click listener to report Message
        botonReportar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Confirmar");
                builder.setMessage("¿Estas Seguro?");

                builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {

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

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

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
                builder.setMessage("¿Estas Seguro?");

                builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialogo, int which) {
                        dialogo.dismiss();
                        ParseObject mensaje = ParseObject.createWithoutData("Mensaje", getArguments().getString("id"));
                        mensaje.deleteInBackground();
                        mensaje.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                Toast.makeText(getContext(), "Mensaje eliminado.", Toast.LENGTH_SHORT).show();
                                botonEliminar.setVisibility(View.GONE);
                                botonEliminar.setEnabled(false);
                            }
                        });
                        Fragment fragment = new FragmentListarMensaje();
                        getFragmentManager()
                                .beginTransaction()
                                .replace(R.id.content_frame, fragment)
                                .commit();

                    }

                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

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

    // Listener for PDFs
    public View.OnClickListener seeLocationDetail(final String location){
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Creates an Intent that will load a map
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:"+location));
                startActivity(intent);
            }
        };
        return listener;
    }



}