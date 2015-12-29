package com.example.usuario.soyactivista.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.parse.DeleteCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by Brahyam on 25/11/2015.
 */
public class FragmentDetalleActividad extends Fragment {

    private static final String TAG = "FragDetalleActividad";
    private TextView labelEstado, labelMunicipio, labelParroquia, nombreActual,ubicacionActual, estadoActual, municipioActual, textMeGusta,
            labelImagenes, fechaInicio, fechaFin, puntaje, descripcion, objetivo, encargado,
            creador,  parroquia;

    private Spinner estatus; // Spinner holders

    private Button botonGuardar, botonEditar, botonEliminar, botonCancelar; // Button holders

    private ImageButton botonMeGusta, botonNoMeGusta, botonAgregarImagenes, botonRemoverImagenes;

    private ImageView imagen1,imagen2,imagen3,imagen4;

    private ProgressDialog dialog;

    private int contadorImagenes = 0;

    // Image Storing Variables/Constants
    private Bitmap bitmap;
    private static byte[] imagenSeleccionada = null;
    private static byte[] imagenSeleccionada2 = null;
    private static byte[] imagenSeleccionada3 = null;
    private static byte[] imagenSeleccionada4 = null;//Array to store Image

    private Boolean existeImagen1 = false;
    private Boolean existeImagen2 = false;
    private Boolean existeImagen3 = false;
    private Boolean existeImagen4 = false;


    private String APP_DIRECTORY = "fotosSoyActivista/";
    private String MEDIA_DIRECTORY = APP_DIRECTORY + "media";
    private String TEMPORAL_PICTURE_NAME = "temporal.jpg";

    private final int PHOTO_CODE = 100;
    private final int SELECT_PICTURE = 200;

    // Class Constructor
    public FragmentDetalleActividad(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        //Choose fragment to inflate
        View v = inflater.inflate(R.layout.fragment_detalle_actividad, container, false);

        //Gets Current User
        final ParseUser usuarioActual = ParseUser.getCurrentUser();

        //Asign TextViews to Holders
        nombreActual = (TextView)v.findViewById(R.id.valueNombre);
        ubicacionActual = (TextView)v.findViewById(R.id.valueUbicacion);
        estadoActual = (TextView)v.findViewById(R.id.estadoActual);
        municipioActual = (TextView)v.findViewById(R.id.municipioActual);
        textMeGusta = (TextView)v.findViewById(R.id.valueMeGusta);
        labelImagenes = (TextView)v.findViewById(R.id.labelImagenes);
        fechaInicio = (TextView)v.findViewById(R.id.valueFechaInicio);
        fechaFin = (TextView)v.findViewById(R.id.valueFechaFin);

        //Asign Text Edit to holders
        puntaje = (TextView)v.findViewById(R.id.valuePuntaje);
        descripcion = (TextView)v.findViewById(R.id.valueDescripcion);
        objetivo = (TextView)v.findViewById(R.id.valueObjetivo);
        encargado = (TextView)v.findViewById(R.id.valueEncargado);
        creador = (TextView)v.findViewById(R.id.valueCreador);
        parroquia = (TextView)v.findViewById(R.id.valueParroquia);

        // Asigns Spinners to holders
        estatus = (Spinner)v.findViewById(R.id.spinEstatus);
        estatus.setEnabled(false);

        // Asign Buttons to holders
        botonEditar = (Button)v.findViewById(R.id.botonEditar);
        botonGuardar = (Button)v.findViewById(R.id.botonGuardar);
        botonEliminar = (Button)v.findViewById(R.id.botonEliminar);
        botonCancelar = (Button)v.findViewById(R.id.botonCancelar);

        botonMeGusta = (ImageButton)v.findViewById(R.id.botonMeGusta);
        botonNoMeGusta = (ImageButton)v.findViewById(R.id.botonNoMeGusta);

        // Assign Images to PlaceHolders
        imagen1 = (ImageView)v.findViewById(R.id.imagen1);
        imagen2 = (ImageView)v.findViewById(R.id.imagen2);
        imagen3 = (ImageView)v.findViewById(R.id.imagen3);
        imagen4 = (ImageView)v.findViewById(R.id.imagen4);


        labelImagenes.setText("0 / 4");
        botonAgregarImagenes = (ImageButton) v.findViewById(R.id.botonAgregarImagenes);
        botonRemoverImagenes = (ImageButton) v.findViewById(R.id.botonRemoverImagenes);

        // Show buttons depending on Role or if user is owner
        if(usuarioActual.getObjectId().equals(getArguments().getString("creadorId"))){
            botonEditar.setVisibility(View.VISIBLE);
            botonEliminar.setVisibility(View.VISIBLE);
        }

        // Disable like button if activity already liked
        if(getArguments().getBoolean("liked")){
            botonMeGusta.setEnabled(false);
            textMeGusta.setTextColor(getContext().getResources().getColor(R.color.verde));
            botonMeGusta.setVisibility(View.GONE);
            botonNoMeGusta.setVisibility(View.VISIBLE);
        }

        // Load Defaults from Arguments bundle
        nombreActual.setText(getArguments().getString("nombre"));
        puntaje.setText(getArguments().getString("puntaje"));
        descripcion.setText(getArguments().getString("descripcion"));
        objetivo.setText(getArguments().getString("objetivo"));
        ubicacionActual.setText(getArguments().getString("ubicacion"));

        // Show other location fields if its on state level
        if(getArguments().getString("ubicacion") == "Estadal"){
            labelEstado.setVisibility(View.VISIBLE);
            estadoActual.setVisibility(View.VISIBLE);
            estadoActual.setText(getArguments().getString("estado"));
            labelMunicipio.setVisibility(View.VISIBLE);
            municipioActual.setVisibility(View.VISIBLE);
            municipioActual.setText(getArguments().getString("municipio"));
            labelParroquia.setVisibility(View.VISIBLE);
            parroquia.setVisibility(View.VISIBLE);
            parroquia.setText(getArguments().getString("parroquia"));
        }

        encargado.setText(getArguments().getString("encargado"));
        creador.setText(getArguments().getString("creador"));

        Log.d(TAG,"Estado de Bundle: "+getArguments().getString("estatus"));
        this.llenarSpinnerdesdeId(estatus, R.array.Estatuses);
        if(getArguments().getString("estatus").equals("En Ejecución"))
            estatus.setSelection(0);
        else
            estatus.setSelection(1);

        fechaInicio.setText(getArguments().getString("inicio"));
        fechaFin.setText(getArguments().getString("fin"));


        // Load Images
        // Reasure Visibility gone for all images
        imagen1.setVisibility(View.GONE);
        imagen2.setVisibility(View.GONE);
        imagen3.setVisibility(View.GONE);
        imagen4.setVisibility(View.GONE);


        if(getArguments().getString("imagen1") != null){
            imagen1.setVisibility(View.VISIBLE);
            Glide.with(getContext())
                    .load(getArguments().getString("imagen1"))
                    .centerCrop()
                    .into(imagen1);

            imagen1.setOnClickListener(seeImageDetail(getArguments().getString("imagen1"), imagenSeleccionada));
            existeImagen1 = true;
            contadorImagenes++;
        }

        if(getArguments().getString("imagen2") != null){
            imagen2.setVisibility(View.VISIBLE);
            Glide.with(getContext())
                    .load(getArguments().getString("imagen2"))
                    .centerCrop()
                    .into(imagen2);

            imagen2.setOnClickListener(seeImageDetail(getArguments().getString("imagen2"),imagenSeleccionada));
            existeImagen2 = true;
            contadorImagenes++;
        }

        if(getArguments().getString("imagen3") != null){
            imagen3.setVisibility(View.VISIBLE);
            Glide.with(getContext())
                    .load(getArguments().getString("imagen3"))
                    .centerCrop()
                    .into(imagen3);

            imagen3.setOnClickListener(seeImageDetail(getArguments().getString("imagen3"),imagenSeleccionada));
            existeImagen3 = true;
            contadorImagenes++;
        }

        if(getArguments().getString("imagen4") != null){
            imagen4.setVisibility(View.VISIBLE);
            Glide.with(getContext())
                    .load(getArguments().getString("imagen4"))
                    .centerCrop()
                    .into(imagen4);

            imagen4.setOnClickListener(seeImageDetail(getArguments().getString("imagen4"),imagenSeleccionada));
            existeImagen4 = true;
            contadorImagenes++;

        }

        labelImagenes.setText(contadorImagenes+" / 4");

        //Load Likes
        Log.d("DETALLE", "Value Likes; "+getArguments().getInt("meGusta"));
        final String procureLikes = String.valueOf(getArguments().getInt("meGusta"));
        Log.d("DETALLE", "String Likes; "+procureLikes);
        textMeGusta.setText(procureLikes);


        // Buttons Behavior
        botonEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Hide Edit button/show save

                estatus.setEnabled(true);


                botonEliminar.setVisibility(View.GONE);
                botonEditar.setVisibility(View.GONE);
                botonGuardar.setVisibility(View.VISIBLE);
                botonCancelar.setVisibility(View.VISIBLE);

                labelImagenes.setVisibility(View.VISIBLE);
                botonAgregarImagenes.setVisibility(View.VISIBLE);
                botonRemoverImagenes.setVisibility(View.VISIBLE);


            }
        });

        botonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                estatus.setEnabled(false);

                botonEliminar.setVisibility(View.VISIBLE);
                botonEditar.setVisibility(View.VISIBLE);
                botonGuardar.setVisibility(View.GONE);
                botonCancelar.setVisibility(View.GONE);

                labelImagenes.setVisibility(View.GONE);
                botonAgregarImagenes.setVisibility(View.GONE);
                botonRemoverImagenes.setVisibility(View.GONE);

            }
        });

        botonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Confirmar");
                builder.setMessage("¿Está seguro de que desea editar la actividad?");

                builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {

                    public void onClick(final DialogInterface dialogo, int which) {

                        dialog = ProgressDialog.show(getActivity(), "", "Guardando Actividad", true);

                        ParseObject actividad = ParseObject.createWithoutData("Actividad", getArguments().getString("id"));

                        actividad.put("estatus", estatus.getSelectedItem().toString());

                        // Handle Image uploading
                        if (imagenSeleccionada != null) {
                            Log.d(TAG,"Imagen 1 sustituyendose.");

                            // Save the scaled image to Parse

                            int value = (int) (Math.random() * 1000 + 2);
                            ParseFile fotoFinal = new ParseFile(usuarioActual.getUsername() + value + ".jpg", imagenSeleccionada);

                            actividad.put("imagen1", fotoFinal);

                            fotoFinal.saveInBackground(new SaveCallback() {
                                public void done(ParseException e) {
                                    if (e != null) {
                                        Log.d(TAG, "Error guardando imagen 1: " + e.getMessage());
                                    } else {
                                        Toast.makeText(getActivity(), "Imagen 1 Cargada.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }

                        if (imagenSeleccionada == null && !existeImagen1)
                            actividad.remove("imagen1");


                        if (imagenSeleccionada2 != null) {
                            // Save the scaled image to Parse

                            int value2 = (int) (Math.random() * 1000 + 3);
                            ParseFile fotoFinal2 = new ParseFile(usuarioActual.getUsername() + value2 + ".jpg", imagenSeleccionada2);

                            actividad.put("imagen2", fotoFinal2);

                            fotoFinal2.saveInBackground(new SaveCallback() {
                                public void done(ParseException e) {
                                    if (e != null) {
                                        Log.d(TAG, "Error guardando imagen 2: " + e.getMessage());
                                    } else {
                                        Toast.makeText(getActivity(), "Imagen 2 Cargada.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }

                        if (imagenSeleccionada2 == null && !existeImagen2)
                            actividad.remove("imagen2");

                        if (imagenSeleccionada3 != null) {
                            // Save the scaled image to Parse
                            int value3 = (int) (Math.random() * 1000 + 5);
                            ParseFile fotoFinal3 = new ParseFile(usuarioActual.getUsername() + value3 + ".jpg", imagenSeleccionada3);

                            actividad.put("imagen3", fotoFinal3);

                            fotoFinal3.saveInBackground(new SaveCallback() {
                                public void done(ParseException e) {
                                    if (e != null) {
                                        Log.d(TAG, "Error guardando imagen 3: " + e.getMessage());
                                    } else {
                                        Toast.makeText(getActivity(), "Imagen 3 Cargada.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }

                        if (imagenSeleccionada3 == null && !existeImagen3)
                            actividad.remove("imagen3");

                        if (imagenSeleccionada4 != null) {
                            // Save the scaled image to Parse

                            int value4 = (int) (Math.random() * 1000 + 7);
                            ParseFile fotoFinal4 = new ParseFile(usuarioActual.getUsername() + value4 + ".jpg", imagenSeleccionada4);

                            actividad.put("imagen4", fotoFinal4);

                            fotoFinal4.saveInBackground(new SaveCallback() {
                                public void done(ParseException e) {
                                    if (e != null) {
                                        Log.d(TAG, "Error guardando imagen 4: " + e.getMessage());
                                    } else {
                                        Toast.makeText(getActivity(), "Imagen 4 Cargada.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }

                        if (imagenSeleccionada4 == null && !existeImagen4)
                            actividad.remove("imagen4");


                        actividad.saveInBackground(new SaveCallback() {
                            public void done(ParseException e) {
                                if (e == null) {
                                    dialog.dismiss();
                                    Toast.makeText(getActivity(), "Actividad guardada correctamente.", Toast.LENGTH_SHORT).show();
                                    // Redirect View to Boletin de Actividades
                                    Fragment fragment = new FragmentListarActividad();
                                    getFragmentManager()
                                            .beginTransaction()
                                            .replace(R.id.content_frame, fragment)
                                            .commit();
                                } else {
                                    dialog.dismiss();
                                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
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

        // Delete Activity
        botonEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Confirmar");
                builder.setMessage("¿Está seguro que desea eliminar la actividad?");

                builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialogo, int which) {
                        // Redirect View to list
                        dialogo.dismiss();
                        ParseObject actividad = ParseObject.createWithoutData("Actividad", getArguments().getString("id"));
                        actividad.deleteInBackground(new DeleteCallback() {
                            @Override
                            public void done(ParseException e) {
                                Toast.makeText(getActivity(), "Actividad eliminada correctamente.", Toast.LENGTH_SHORT).show();
                                // Redirect User to List
                                Fragment fragment = new FragmentListarActividad();
                                getFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.content_frame, fragment)
                                        .commit();

                            }
                        });


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

        // Likes Behavior
        botonMeGusta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseObject like = new ParseObject("MeGusta");
                ParseObject actividadLiked = ParseObject.createWithoutData("Actividad",getArguments().getString("id"));
                like.put("usuario", usuarioActual);
                like.put("actividad", actividadLiked);
                like.saveInBackground();

                actividadLiked.increment("meGusta");
                actividadLiked.saveInBackground();


                Log.d("DETALLE", "Value Likes; " + getArguments().getInt("meGusta"));
                String procureLikes = String.valueOf(getArguments().getInt("meGusta")+1);
                textMeGusta.setText(procureLikes);
                // Paint Like button green
                botonMeGusta.setVisibility(View.GONE);
                botonMeGusta.setColorFilter(R.color.verde);
                botonMeGusta.setEnabled(false);

                // Activate tinted button
                botonNoMeGusta.setVisibility(View.VISIBLE);
                botonNoMeGusta.setEnabled(true);
            }
        });

        // Likes Behavior
        botonNoMeGusta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ParseObject actividadLiked = ParseObject.createWithoutData("Actividad",getArguments().getString("id"));

                ParseQuery<ParseObject> query = ParseQuery.getQuery("MeGusta");
                query.whereEqualTo("actividad", actividadLiked);
                query.whereEqualTo("usuario", usuarioActual);

                query.getFirstInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject object, ParseException e) {
                        if(e == null){
                            object.deleteInBackground();
                        }
                        else{
                            Log.d(TAG,e.getMessage());
                        }
                    }
                });

                actividadLiked.increment("meGusta",-1);
                actividadLiked.saveInBackground();

                Log.d("DETALLE", "Value Likes; " + getArguments().getInt("meGusta"));
                String procureLikes = String.valueOf(getArguments().getInt("meGusta")-1);
                textMeGusta.setText(procureLikes);

                // Paint Like button green
                botonNoMeGusta.setVisibility(View.GONE);
                botonNoMeGusta.setEnabled(false);
                textMeGusta.setTextColor(getContext().getResources().getColor(R.color.grisOscuro));


                botonMeGusta.setVisibility(View.VISIBLE);
                botonMeGusta.setEnabled(true);
            }
        });

        botonAgregarImagenes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] options = {"Tomar foto", "Elegir de galeria", "Cancelar"};
                final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getContext());

                builder.setTitle("Elija una opcion:");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int seleccion) {
                        if (options[seleccion] == "Tomar foto") {
                            tomarFoto();
                        } else if (options[seleccion] == "Elegir de galeria") {
                            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            intent.setType("image/*");
                            startActivityForResult(intent.createChooser(intent, "Selecciona app de imagen"), SELECT_PICTURE);
                        } else if (options[seleccion] == "Cancelar") {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();

            }
        });

        botonRemoverImagenes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Confirmar");
                builder.setMessage("¿Está seguro que desea eliminar todas las fotos de la actividad?");

                builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialogo, int which) {
                        // Redirect View to list
                        imagen1.setVisibility(View.GONE);
                        imagen2.setVisibility(View.GONE);
                        imagen3.setVisibility(View.GONE);
                        imagen4.setVisibility(View.GONE);

                        imagenSeleccionada = null;
                        imagenSeleccionada2 = null;
                        imagenSeleccionada3 = null;
                        imagenSeleccionada4 = null;

                        existeImagen1 = false;
                        existeImagen2 = false;
                        existeImagen3 = false;
                        existeImagen4 = false;

                        labelImagenes.setText("0 / 4");
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

    //Auxiliar method for filling spinners with String Arrays
    public void llenarSpinnerdesdeId(Spinner spin,int id){
        ArrayAdapter spinner_adapter = ArrayAdapter.createFromResource(getActivity(), id, android.R.layout.simple_spinner_item);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(spinner_adapter);
    }

    // Listener for image details
    public View.OnClickListener seeImageDetail(final String url, final byte[] array){
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle data = new Bundle();
                data.putString("imageUrl",url);
                data.putByteArray("imageArray", array);
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

    private void tomarFoto() {
        File file = new File(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
        file.mkdirs();

        String path = Environment.getExternalStorageDirectory() + File.separator
                + MEDIA_DIRECTORY + File.separator + TEMPORAL_PICTURE_NAME;

        File newFile = new File(path);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(newFile));
        startActivityForResult(intent, PHOTO_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case PHOTO_CODE:{
                if(resultCode == Activity.RESULT_OK){
                    String dir =  Environment.getExternalStorageDirectory() + File.separator
                            + MEDIA_DIRECTORY + File.separator + TEMPORAL_PICTURE_NAME;
                    bitmap = BitmapFactory.decodeFile(dir);

                    preparePhoto(bitmap);
                }
                else{
                    Toast.makeText(getActivity(),"Error Inesperado."+resultCode, Toast.LENGTH_LONG).show();
                }
                break;
            }

            case SELECT_PICTURE: {
                if (resultCode == Activity.RESULT_OK) {
                    Uri path = data.getData();

                    try {
                        InputStream imageStream = getContext().getContentResolver().openInputStream(path);
                        bitmap = BitmapFactory.decodeStream(imageStream);

                        imageStream.close();

                        preparePhoto(bitmap);


                        Toast.makeText(getActivity(),"Se ha adjuntado una imagen correctamente.", Toast.LENGTH_SHORT).show();
                        //labelFotos.setText("1");
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getActivity(),"Adjuntar cancelado."+resultCode, Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    //Process Photo
    private void preparePhoto(Bitmap bitmap){
        // RESIZE
        this.bitmap = Bitmap.createScaledBitmap(bitmap, 400, 400
                * bitmap.getHeight() / bitmap.getWidth(), false);
        // COMPRESS
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        this.bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);

        //Store in local to be saved after

        if(imagenSeleccionada == null && !existeImagen1)
        {
            imagenSeleccionada = bos.toByteArray();
            imagen1.setVisibility(View.VISIBLE);
            Glide.with(getContext())
                    .load(imagenSeleccionada)
                    .centerCrop()
                    .into(imagen1);

            imagen1.setOnClickListener(seeImageDetail("", imagenSeleccionada));
            contadorImagenes++;
        }
        else
        {
            if(imagenSeleccionada2 == null && !existeImagen2)
            {
                imagenSeleccionada2 = bos.toByteArray();
                imagen2.setVisibility(View.VISIBLE);
                Glide.with(getContext())
                        .load(imagenSeleccionada2)
                        .centerCrop()
                        .into(imagen2);

                imagen2.setOnClickListener(seeImageDetail("", imagenSeleccionada2));
                contadorImagenes++;
            }
            else
            {
                if(imagenSeleccionada3 == null && !existeImagen3)
                {
                    imagenSeleccionada3 = bos.toByteArray();
                    imagen3.setVisibility(View.VISIBLE);
                    Glide.with(getContext())
                            .load(imagenSeleccionada3)
                            .centerCrop()
                            .into(imagen3);

                    imagen3.setOnClickListener(seeImageDetail("",imagenSeleccionada3));
                    contadorImagenes++;
                }
                else
                {
                    if(imagenSeleccionada4 == null && !existeImagen4)
                    {
                        imagen4.setVisibility(View.VISIBLE);
                        Glide.with(getContext())
                                .load(imagenSeleccionada4)
                                .centerCrop()
                                .into(imagen4);

                        imagen4.setOnClickListener(seeImageDetail("",imagenSeleccionada4));
                        contadorImagenes++;
                    }
                    else
                    {
                        Toast.makeText(getActivity(),"No se puede adjuntar más de 4 imagenes", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }

        labelImagenes.setText(contadorImagenes+" / 4");

    }

}
