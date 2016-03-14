package com.burizalabs.soyactivista.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.design.widget.CoordinatorLayout;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.parse.FindCallback;
import com.parse.FunctionCallback;
import com.parse.GetCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.burizalabs.soyactivista.R;

/**
 * Created by Brahyam on 1/12/2015.
 */
public class FragmentCrearMensajeDirecto extends Fragment {

    // Variable Declaration
    private String TAG = "FCrearMensajeDir"; // For Log.d

    private ProgressDialog dialog;
    private ParseUser currentUser;
    private ParseUser prueba;
    private String receptorId,receptorUsername;
    private boolean existeConversacion;
    private TextView textCharCount;
    private EditText editText;
    private ImageButton buttonAddImage, buttonAddFile, buttonAddLocation;
    private ImageView imageAttachmentPreview;
    private Button buttonCreateMessage;

    // Image Saving Variables
    private Bitmap bitmap;
    private int random = (int)(Math.random() * 1000) +1; // For file name.
    private byte[] selectedImage;
    // Constants
    private String APP_DIRECTORY = "fotosSoyActivista/";
    private String MEDIA_DIRECTORY = APP_DIRECTORY + "media";
    private String TEMPORAL_PICTURE_NAME = "temporal"+ random +".jpg";
    private final int PHOTO_CODE = 100;
    private final int SELECT_PICTURE = 200;
    private static final int PICKFILE_RESULT_CODE = 300;
    private static final int PLACE_PICKER_REQUEST = 400;
    private CoordinatorLayout coordinatorLayout;
    final ParseUser usuarioActual = ParseUser.getCurrentUser();
    //Location
    private ParseGeoPoint location;
    PlacePicker.IntentBuilder builder;

    //PDF Attachments
    private byte[] selectedFile;

    // Class Constructor
    public FragmentCrearMensajeDirecto(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        //Choose fragment to inflate
        View v = inflater.inflate(R.layout.fragment_crear_mensaje, container, false);

        // Set action bar title
        ActionBar actionBar = getActivity().getActionBar();
        if(actionBar != null)
            actionBar.setTitle("Enviar Mensaje Directo");

        //Asign Visuals to Holders

        textCharCount = (TextView)v.findViewById(R.id.textCharCount);

        editText = (EditText)v.findViewById(R.id.editText);

        imageAttachmentPreview = (ImageView) v.findViewById(R.id.imageAttachmentPreview);

        buttonCreateMessage= (Button)v.findViewById(R.id.buttonCreateMessage);


        coordinatorLayout = (CoordinatorLayout)v.findViewById(R.id
                .coordinatorLayout);

        buttonAddFile = (ImageButton)v.findViewById(R.id.buttonAddFile);
        buttonAddImage = (ImageButton)v.findViewById(R.id.buttonAddImage);
        buttonAddLocation = (ImageButton)v.findViewById(R.id.buttonAddLocation);



        // Update CharCount on writting
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textCharCount.setText(String.valueOf(editText.getText().length()) + "/200");
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Buttons Behavior
        // ADD IMAGE
        buttonAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final CharSequence[] options = {"Tomar foto", "Elegir de galeria", "Cancelar"};
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                builder.setTitle("Elija una opcion:");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int seleccion) {
                        if (options[seleccion] == "Tomar foto") {

                            File file = new File(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
                            file.mkdirs();
                            String path = Environment.getExternalStorageDirectory() + File.separator
                                    + MEDIA_DIRECTORY + File.separator + TEMPORAL_PICTURE_NAME;
                            File newFile = new File(path);
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(newFile));
                            startActivityForResult(intent, PHOTO_CODE);

                        } else if (options[seleccion] == "Elegir de galeria") {

                            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            intent.setType("image/*");
                            startActivityForResult(intent.createChooser(intent, "Seleccione una Aplicación para ver sus imágenes"), SELECT_PICTURE);

                        } else if (options[seleccion] == "Cancelar") {

                            dialog.dismiss();
                        }
                    }
                });
                builder.show();

            }
        });

        // ADD FILE
        buttonAddFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String minmeType = "application/pdf";

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType(minmeType);
                intent.addCategory(Intent.CATEGORY_OPENABLE);

                // special intent for Samsung file manager
                Intent sIntent = new Intent("com.sec.android.app.myfiles.PICK_DATA");
                // if you want any file type, you can skip next line
                sIntent.putExtra("CONTENT_TYPE", minmeType);
                sIntent.addCategory(Intent.CATEGORY_DEFAULT);

                Intent chooserIntent;
                if (getActivity().getPackageManager().resolveActivity(sIntent, 0) != null){
                    // it is device with samsung file manager
                    chooserIntent = Intent.createChooser(sIntent, "Open file");
                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] { intent});
                }
                else {
                    chooserIntent = Intent.createChooser(intent, "Open file");
                }

                try {
                    startActivityForResult(chooserIntent, PICKFILE_RESULT_CODE);
                } catch (android.content.ActivityNotFoundException ex) {
                    //Toast.makeText(getContext(), "No se encontró un manejador de archivos. Por favor descargue como \"Astro File Manager\" de la Tienda de Google.", Toast.LENGTH_LONG).show();
                    final Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, "No se encontró un manejador de archivos. Por favor descargue como \"Astro File Manager\" de la Tienda de Google.",
                                    Snackbar.LENGTH_LONG);

                    snackbar.show();
                }

            }
        });

        // ADD LOCATION
        buttonAddLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    //Toast.makeText(getContext(), "Erro de Google Play Services. Por favor asegurese de que Google Play Services está instalado y actualizado.", Toast.LENGTH_SHORT).show();
                    final Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, "Erro de Google Play Services. Por favor asegurese de que Google Play Services está instalado y actualizado.",
                                    Snackbar.LENGTH_LONG);

                    snackbar.show();
                } catch (GooglePlayServicesNotAvailableException e) {
                    //Toast.makeText(getContext(), "Error de Google Play Services. El servicio no está disponible", Toast.LENGTH_SHORT).show();
                    final Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, "Error de Google Play Services. El servicio no está disponible",
                                    Snackbar.LENGTH_LONG);

                    snackbar.show();
                }

            }
        });


        // CREATE MESSAGE
        buttonCreateMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {


                if (editText.getText().toString().trim().length() > 0) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Confirmar");
                    builder.setMessage("¿Está seguro de que quiere publicar el mensaje?");


                    builder.setPositiveButton("Publicar", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialogo, int which) {


                            receptorId = getArguments().getString("receptorId");
                            receptorUsername =getArguments().getString("receptorUsername");

                            currentUser = ParseUser.getCurrentUser();

                            existeConversacion = getArguments().getBoolean("existeConversacion");

                            Log.d(TAG,"Existe Conversacion "+existeConversacion);

                            if(!existeConversacion)
                            {

                                dialog = ProgressDialog.show(getActivity(), "", "Creando Conversación", true);

                                ParseQuery<ParseObject> innerQuery2 = ParseQuery.getQuery("_User");
                                innerQuery2.whereEqualTo("username", receptorUsername);
                                ParseQuery<ParseObject> query = ParseQuery.getQuery("ParticipanteConversacion");
                                query.whereEqualTo("receptor", currentUser);
                                query.whereMatchesQuery("usuario", innerQuery2);
                                query.findInBackground(new FindCallback<ParseObject>() {
                                    public void done(List<ParseObject> parseLikes, ParseException e) {
                                        if (e == null) {

                                            //se restaura la conversacion si un participante elimino su copia y vuelve a escribir a el mismo
                                            //participante a quien le borro de sus conversacion
                                            if (parseLikes.size()>=1)
                                            {
                                                Log.d("score", "Retrieved " + parseLikes.size() + " scores");
                                                for (ParseObject question : parseLikes) {

                                                    ParseObject conversacion    =   question.getParseObject("conversacion");
                                                    String conversacionId       =   conversacion.getObjectId().toString();
                                                    ParseObject receptor        =  question.getParseObject("usuario");

                                                    final ParseObject nuevoParticipante = new ParseObject("ParticipanteConversacion");

                                                    nuevoParticipante.put("conversacion", conversacion);
                                                    nuevoParticipante.put("usuario", currentUser);
                                                    nuevoParticipante.put("receptor", receptor);
                                                    nuevoParticipante.saveInBackground(new SaveCallback() {
                                                        @Override
                                                        public void done(ParseException e) {
                                                            if (e == null)
                                                            {
                                                                Log.d(TAG, "Participante  agregado nuevamente, correctamente");
                                                            }

                                                            else {
                                                                Log.d(TAG, " Ocurrió un error agregando al Participante nuevamente " + e.getMessage());
                                                            }

                                                        }
                                                    });
                                                    saveMensajeDirecto(conversacionId, receptorId);
                                                }
                                            }
                                            else
                                            {

                                                // Creates New Conversation and Adds Users to conversation
                                                final ParseObject nuevaConversacion = new ParseObject("Conversacion");
                                                nuevaConversacion.put("ultimaActividad", new Date());

                                                nuevaConversacion.saveInBackground(new SaveCallback() {
                                                    @Override
                                                    public void done(ParseException e) {

                                                        if (e == null) {

                                                            final ParseObject nuevoParticipante = new ParseObject("ParticipanteConversacion");
                                                            Log.d(TAG, "Participante: " + nuevaConversacion.getObjectId() + " " + currentUser.getUsername());
                                                            nuevoParticipante.put("conversacion", nuevaConversacion);
                                                            nuevoParticipante.put("usuario", currentUser);


                                                            final ParseObject nuevoParticipante2 = new ParseObject("ParticipanteConversacion");
                                                            nuevoParticipante2.put("conversacion", nuevaConversacion);

                                                            Log.d(TAG, "Participante: " + nuevaConversacion.getObjectId() + " " + receptorId);

                                                            // Query for 2nd user because Parse does not allow creation of Users without data.

                                                            ParseQuery query = ParseUser.getQuery();
                                                            query.whereEqualTo("objectId", receptorId);
                                                            query.getFirstInBackground(new GetCallback() {
                                                                @Override
                                                                public void done(final ParseObject object, ParseException e) {
                                                                    if (e == null && object != null) {
                                                                        // User Retrieved
                                                                        nuevoParticipante.put("receptor",object);
                                                                        nuevoParticipante.saveInBackground(new SaveCallback() {
                                                                            @Override
                                                                            public void done(ParseException e) {
                                                                                if (e == null)
                                                                                    Log.d(TAG, "Participante 1 agregado correctamente");
                                                                                else
                                                                                    Log.d(TAG, " Ocurrió un error agregando al Participante 1 " + e.getMessage());
                                                                            }
                                                                        });

                                                                        nuevoParticipante2.put("usuario", object);
                                                                        nuevoParticipante2.put("receptor",currentUser);
                                                                        nuevoParticipante2.saveInBackground(new SaveCallback() {
                                                                            @Override
                                                                            public void done(ParseException e) {
                                                                                if (e == null)
                                                                                {
                                                                                    Log.d(TAG, "Participante 2 agregado correctamente");

                                                                                    dialog.setMessage("Guardando Mensaje");
                                                                                    saveMensajeDirecto(nuevaConversacion.getObjectId(), object.getObjectId());

                                                                                }

                                                                                else {
                                                                                    Log.d(TAG, " Ocurrió un error agregando al Participante 2 " + e.getMessage());

                                                                                }


                                                                            }
                                                                        });

                                                                    } else {
                                                                        Log.d(TAG, " Ocurrió un error buscando al usuario " + e.getMessage());
                                                                    }

                                                                }

                                                                @Override
                                                                public void done(final Object o, Throwable throwable) {
                                                                    if (o != null) {

                                                                        final ParseUser user = (ParseUser) o;
                                                                        Log.d(TAG, "Caso Desconocido " + o.toString());
                                                                        // User Retrieved
                                                                        nuevoParticipante.put("receptor",user);
                                                                        nuevoParticipante.saveInBackground(new SaveCallback() {
                                                                            @Override
                                                                            public void done(ParseException e) {
                                                                                if (e == null)
                                                                                    Log.d(TAG, "Participante 1 agregado correctamente");
                                                                                else
                                                                                    Log.d(TAG, " Ocurrió un error agregando al Participante 1 " + e.getMessage());
                                                                            }
                                                                        });


                                                                        nuevoParticipante2.put("usuario", o);
                                                                        nuevoParticipante2.put("receptor",currentUser);
                                                                        nuevoParticipante2.saveInBackground(new SaveCallback() {
                                                                            @Override
                                                                            public void done(ParseException e) {
                                                                                if (e == null) {
                                                                                    Log.d(TAG, "Participante 2 agregado correctamente");

                                                                                    saveMensajeDirecto(nuevaConversacion.getObjectId(), user.getObjectId());

                                                                                }
                                                                                else {
                                                                                    Log.d(TAG, " Ocurrió un error agregando al Participante 2 " + e.getMessage());
                                                                                }
                                                                            }
                                                                        });
                                                                    } else{

                                                                        Log.d(TAG, "Caso Desconocido " + throwable.getMessage());
                                                                        //Toast.makeText(getActivity(), "No se encontró al usuario con el que desea iniciar una conversación", Toast.LENGTH_LONG).show();
                                                                        final Snackbar snackbar = Snackbar
                                                                                .make(coordinatorLayout, "No se encontró al usuario con el que desea iniciar una conversación",
                                                                                        Snackbar.LENGTH_LONG);

                                                                        snackbar.show();

                                                                        dialog.dismiss();

                                                                    }



                                                                }
                                                            });
                                                        }
                                                        else{
                                                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                });
                                                ///darw





                                            }




                                        } else {
                                            Log.d("score", "Error: " + e.getMessage());
                                        }
                                    }
                                });



















                                ///////////////
/*
                                ParseQuery query2 = ParseUser.getQuery();
                                query2.whereEqualTo("objectId", receptorId);

                                query2.getFirstInBackground(new GetCallback() {
                                @Override
                                public void done(final ParseObject object, ParseException e) {
                                    if (e == null) {


                                        ParseQuery<ParseObject> innerQuery2 = ParseQuery.getQuery("_User");
                                        innerQuery2.whereEqualTo("username", receptorUsername);


                                        ParseQuery<ParseObject> query = ParseQuery.getQuery("ParticipanteConversacion");
                                        query.whereEqualTo("receptor", currentUser);
                                        query.whereMatchesQuery("usuario", innerQuery2);


                                        query.findInBackground(new FindCallback<ParseObject>() {
                                            public void done(List<ParseObject> parseLikes, ParseException e) {
                                                if (e == null) {
                                                    Log.d("score", "Retrieved " + parseLikes.size() + " scores");

                                                } else {
                                                    Log.d("score", "Error: " + e.getMessage());
                                                }
                                            }
                                        });










*/




















                                        //List contain object with specific user id.
                                    /*    ParseQuery<ParseObject> query = ParseQuery.getQuery("ParticipanteConversacion");
                                        query.whereEqualTo("receptor", currentUser);
                                        query.whereEqualTo("usuario", object);


                                        query.findInBackground(new FindCallback<ParseObject>() {
                                            @Override
                                            public void done(List<ParseObject> parseLikes, ParseException e) {
                                                if (e == null) {
                                                    Log.d("dos_user", "Retrieved " + parseLikes.size() + " scores");


                                                } else {
                                                    Log.d("dos_user", "Error: " + e.getMessage());
                                                }
                                            }
                                        });


                                    } else {
                                        Log.d("score", "Error: " + e.getMessage());
                                    }
                                }
                            });
*/













                             /*   ParseQuery query2 = ParseUser.getQuery();
                                query2.whereEqualTo("objectId",receptorId);
                                query2.getFirstInBackground(new GetCallback() {
                                    public void done(final ParseObject object, ParseException e) {
                                          if (e == null) {
                                              //List contain object with specific user id.
                                              ParseQuery<ParseObject> query = ParseQuery.getQuery("ParticipanteConversacion");
                                              query.whereEqualTo("receptor", currentUser);
                                              query.whereEqualTo("usuario", object);


                                              query.findInBackground(new FindCallback<ParseObject>() {
                                                  public void done(List<ParseObject> parseLikes, ParseException e) {
                                                      if (e == null) {
                                                          Log.d("dos_user", "Retrieved " + parseLikes.size() + " scores");


                                                      } else {
                                                          Log.d("dos_user", "Error: " + e.getMessage());
                                                      }
                                                  }
                                              });


                                          } else {
                                              // error
                                          }
                                      }
                                  });*/


                                //darwin





                                //fin darwin







                            }
                            //ya existe la conversacion
                            else
                            {
                                dialog = ProgressDialog.show(getActivity(), "", "Enviando Mensaje", true);
                                saveMensajeDirecto(getArguments().getString("conversacionId"), getArguments().getString("receptorId"));
                            }

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

                } else {
                    //Toast.makeText(getContext(), "No puede publicar un mensaje vacío.", Toast.LENGTH_LONG).show();
                    final Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, "No puede publicar un mensaje vacío.",
                                    Snackbar.LENGTH_LONG);

                    snackbar.show();
                    return;
                }

            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case PHOTO_CODE:
                if(resultCode == Activity.RESULT_OK){
                    String dir =  Environment.getExternalStorageDirectory() + File.separator
                            + MEDIA_DIRECTORY + File.separator + TEMPORAL_PICTURE_NAME;
                    bitmap = BitmapFactory.decodeFile(dir);
                    preparePhoto(bitmap);
                    imageAttachmentPreview.setVisibility(View.VISIBLE);
                    Glide.with(getContext())
                            .load(selectedImage)
                            .placeholder(R.mipmap.ic_placeholder)
                            .centerCrop()
                            .into(imageAttachmentPreview);

                    location = null;
                }
                else{
                    //Toast.makeText(getActivity(), "Error adjuntando la imagen."+resultCode, Toast.LENGTH_SHORT).show();
                    final Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, "Error adjuntando la imagen."+resultCode,
                                    Snackbar.LENGTH_LONG);

                    snackbar.show();
                }
                break;

            case SELECT_PICTURE:
                if (resultCode == Activity.RESULT_OK) {
                    Uri path = data.getData();

                    try {
                        InputStream imageStream = getContext().getContentResolver().openInputStream(path);
                        bitmap = BitmapFactory.decodeStream(imageStream);
                        preparePhoto(bitmap);
                        imageAttachmentPreview.setVisibility(View.VISIBLE);
                        Glide.with(getContext())
                                .load(selectedImage)
                                .centerCrop()
                                .into(imageAttachmentPreview);

                        location = null;
                        selectedFile = null;

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    //Toast.makeText(getActivity(), "Error adjuntando la imagen."+resultCode, Toast.LENGTH_SHORT).show();
                    final Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, "Error adjuntando la imagen."+resultCode,
                                    Snackbar.LENGTH_LONG);

                    snackbar.show();
                }
                break;

            case PICKFILE_RESULT_CODE:
                if(resultCode == Activity.RESULT_OK) {
                    Uri path = data.getData();
                    try {
                        InputStream pdfStream = getContext().getContentResolver().openInputStream(path);
                        selectedFile = getBytes(pdfStream);

                        // 8388608 bytes son 8mb
                        if(selectedFile.length>=8388608){
                            selectedFile=null;
                            final Snackbar snackbar = Snackbar
                                    .make(coordinatorLayout, "El Archivo no puede ser mayor a 8MB",
                                            Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                        else
                        {
                            imageAttachmentPreview.setVisibility(View.VISIBLE);
                            Glide.with(getContext())
                                    .load(R.drawable.ic_attach_file)
                                    .centerCrop()
                                    .into(imageAttachmentPreview);
                        }

                        location = null;
                        selectedImage = null;

                    } catch (FileNotFoundException e) {
                        //Toast.makeText(getActivity(), "Error adjuntando la posición."+resultCode, Toast.LENGTH_SHORT).show();
                        final Snackbar snackbar = Snackbar
                                .make(coordinatorLayout, "Error adjuntando la posición."+resultCode,
                                        Snackbar.LENGTH_LONG);

                        snackbar.show();
                    } catch (IOException e) {
                        //Toast.makeText(getActivity(), "Error adjuntando el archivo."+resultCode, Toast.LENGTH_SHORT).show();
                        final Snackbar snackbar = Snackbar
                                .make(coordinatorLayout, "Error adjuntando el archivo."+resultCode,
                                        Snackbar.LENGTH_LONG);

                        snackbar.show();
                    }


                }
                else{
                    //Toast.makeText(getActivity(), "Error adjuntando el archivo."+resultCode, Toast.LENGTH_SHORT).show();
                    final Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, "Error adjuntando el archivo."+resultCode,
                                    Snackbar.LENGTH_LONG);

                    snackbar.show();
                }
                break;

            case PLACE_PICKER_REQUEST:
                if (resultCode == Activity.RESULT_OK) {
                    Place place = PlacePicker.getPlace(data, getContext());
                    location= new ParseGeoPoint(place.getLatLng().latitude,place.getLatLng().longitude);
                    String toastMsg = String.format("Place: %s", place.getName());
                    Toast.makeText(getContext(), toastMsg, Toast.LENGTH_LONG).show();
                    String latMsg = String.valueOf(place.getLatLng().latitude);
                    String lngMsg = String.valueOf(place.getLatLng().longitude);
                    String url = "http://maps.google.com/maps/api/staticmap?center=" + latMsg+ "," +lngMsg +
                            "&zoom=15&size=500x400&maptype=roadmap&markers=color:red%7Clabel:U%7C" + latMsg + "," + lngMsg + "%7Csize:small&";
                    selectedImage = null;
                    selectedFile = null;
                    imageAttachmentPreview.setVisibility(View.VISIBLE);
                    Glide.with(getContext())
                            .load(url).asBitmap()
                            .centerCrop()
                            .into(imageAttachmentPreview);

                }
                break;

        }
    }

    //Process Photo
    private void preparePhoto(Bitmap bitmap){
        // RESIZE
        this.bitmap = Bitmap.createScaledBitmap(bitmap, 600, 600
                * bitmap.getHeight() / bitmap.getWidth(), false);
        // COMPRESS
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        this.bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);

        //Store in local to be saved after
        selectedImage = bos.toByteArray();

    }

    // Transform InputStream to Byte Array (For File Storage)
    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }


    private void saveMensajeDirecto(final String conversationId, final String receptorId)
    {

        // Fill ParseObject to send
        final ParseObject mensaje = new ParseObject("MensajeDirecto");

        mensaje.put("texto", editText.getText().toString());
        mensaje.put("autor", usuarioActual);

        ParseObject conversacion = ParseObject.createWithoutData("Conversacion", conversationId);
        conversacion.put("ultimaActividad",new Date());

        // Save conversation last activity date
        conversacion.saveEventually();

        mensaje.put("conversacion", conversacion);

        // Handle Image uploading
        if (selectedImage != null) {
            // Save the scaled image to Parse
            location = null; // Disabling other attachments
            selectedFile = null;
            location = null;

            ParseFile fotoFinal = new ParseFile(usuarioActual.getUsername() + random + ".jpg", selectedImage);
            mensaje.put("adjunto", fotoFinal);
            fotoFinal.saveInBackground(new SaveCallback() {
                public void done(ParseException e) {
                    if (e != null) {
                        Toast.makeText(getActivity(),
                                "Error saving: " + e.getMessage(),
                                Toast.LENGTH_LONG).show();
                        Log.d(TAG, e.toString());
                    } else {
                        //Toast.makeText(getActivity(), "Foto Cargada.", Toast.LENGTH_SHORT).show();
                        final Snackbar snackbar = Snackbar
                                .make(coordinatorLayout, "Foto Cargada.",
                                        Snackbar.LENGTH_LONG);

                        snackbar.show();
                    }
                }
            });
        }

        if (selectedFile != null) {

            ParseFile finalFile = new ParseFile(usuarioActual.getUsername() + random + ".pdf", selectedFile);
            selectedImage = null;
            location = null;
            mensaje.put("adjunto", finalFile);
            finalFile.saveInBackground(new SaveCallback() {
                public void done(ParseException e) {
                    if (e != null) {
                        Toast.makeText(getActivity(),
                                "Error saving: " + e.getMessage(),
                                Toast.LENGTH_LONG).show();
                        Log.d(TAG, e.toString());
                    } else {
                        //Toast.makeText(getActivity(), "PDF Cargado.", Toast.LENGTH_SHORT).show();
                        final Snackbar snackbar = Snackbar
                                .make(coordinatorLayout, "PDF Cargado.",
                                        Snackbar.LENGTH_LONG);

                        snackbar.show();
                    }
                }
            });


        }

        // Handle Location
        if (location != null) {
            mensaje.put("ubicacion", location);
        }

        // Save Message
        mensaje.saveInBackground(new SaveCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    dialog.dismiss();
                    //Toast.makeText(getActivity(), "Mensaje Publicado", Toast.LENGTH_SHORT).show();
                    final Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, "Mensaje Publicado.",
                                    Snackbar.LENGTH_INDEFINITE);

                    snackbar.show();

                    Bundle datos = new Bundle();


                    datos.putString("conversacionId", conversationId);
                    datos.putString("receptorId",receptorId);

                    //   datos.putString("conversacionId", getArguments().getString("conversacionId"));
                    HashMap<String, Object> params = new HashMap<String, Object>();
                    params.put("recipientId", receptorId);
                    params.put("message",  editText.getText().toString());
                    ParseCloud.callFunctionInBackground("sendPushToUser", params, new FunctionCallback<String>() {
                        public void done(String success, ParseException e) {
                            if (e == null) {
                                // Push sent successfully
                                Log.d(TAG,"Push Sent");
                            }
                        }
                    });

                    // Redirect View to ListarMensajes
                    Fragment fragment = new FragmentListarMensajeDirecto();
                    fragment.setArguments(datos);
                    getFragmentManager()
                            .beginTransaction()
                            .replace(((ViewGroup)getView().getParent()).getId(), fragment)
                            .commit();
                } else {
                    dialog.dismiss();
                    Log.d(TAG, e.toString());
                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
