package com.example.usuario.soyactivista.fragments;

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

import com.bumptech.glide.Glide;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by Brahyam on 1/12/2015.
 */
public class FragmentCrearMensaje extends Fragment {

    // Variable Declaration
    private String TAG = "FragmentCrearMensaje"; // For Log.d

    private ProgressDialog dialog;

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

    //Location
    private ParseGeoPoint location;
    PlacePicker.IntentBuilder builder;

    //PDF Attachments
    private byte[] selectedFile;

    // Class Constructor
    public FragmentCrearMensaje(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        //Choose fragment to inflate
        View v = inflater.inflate(R.layout.fragment_crear_mensaje, container, false);

        //Gets Current User
        final ParseUser usuarioActual = ParseUser.getCurrentUser();

        //Asign Visuals to Holders

        textCharCount = (TextView)v.findViewById(R.id.textCharCount);

        editText = (EditText)v.findViewById(R.id.editText);

        imageAttachmentPreview = (ImageView) v.findViewById(R.id.imageAttachmentPreview);

        buttonCreateMessage= (Button)v.findViewById(R.id.buttonCreateMessage);



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
                    Toast.makeText(getContext(), "No se encontró un manejador de archivos. Por favor descargue como \"Astro File Manager\" de la Tienda de Google.", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(getContext(), "Erro de Google Play Services. Por favor asegurese de que Google Play Services está instalado y actualizado.", Toast.LENGTH_SHORT).show();
                } catch (GooglePlayServicesNotAvailableException e) {
                    Toast.makeText(getContext(), "Error de Google Play Services. El servicio no está disponible", Toast.LENGTH_SHORT).show();
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

                                dialog = ProgressDialog.show(getActivity(), "", "Creando Mensaje", true);

                                // Fill ParseObject to send
                                final ParseObject mensaje = new ParseObject("Mensaje");

                                mensaje.put("texto", editText.getText().toString());
                                mensaje.put("autor", usuarioActual);
                                mensaje.put("reportado", false);

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
                                                Toast.makeText(getActivity(), "Foto Cargada.", Toast.LENGTH_SHORT).show();
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
                                                Toast.makeText(getActivity(), "PDF Cargado.", Toast.LENGTH_SHORT).show();
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
                                            Toast.makeText(getActivity(), "Mensaje Publicado", Toast.LENGTH_SHORT).show();
                                            // Redirect View to ListarMensajes
                                            Fragment fragment = new FragmentListarMensaje();
                                            getFragmentManager()
                                                    .beginTransaction()
                                                    .replace(R.id.content_frame, fragment)
                                                    .commit();
                                        } else {
                                            dialog.dismiss();
                                            Log.d(TAG, e.toString());
                                            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                                        }
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

                } else {
                    Toast.makeText(getContext(), "No puede publicar un mensaje vacío.", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(getActivity(), "Error adjuntando la imagen."+resultCode, Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getActivity(), "Error adjuntando la imagen."+resultCode, Toast.LENGTH_SHORT).show();
                }
                break;

            case PICKFILE_RESULT_CODE:
                if(resultCode == Activity.RESULT_OK) {
                    Uri path = data.getData();
                    try {
                        InputStream pdfStream = getContext().getContentResolver().openInputStream(path);
                        selectedFile = getBytes(pdfStream);

                        imageAttachmentPreview.setVisibility(View.VISIBLE);
                        Glide.with(getContext())
                                .load(R.drawable.ic_attach_file)
                                .centerCrop()
                                .into(imageAttachmentPreview);

                        location = null;
                        selectedImage = null;

                    } catch (FileNotFoundException e) {
                        Toast.makeText(getActivity(), "Error adjuntando la posición."+resultCode, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Toast.makeText(getActivity(), "Error adjuntando el archivo."+resultCode, Toast.LENGTH_SHORT).show();
                    }


                }
                else{
                    Toast.makeText(getActivity(), "Error adjuntando el archivo."+resultCode, Toast.LENGTH_SHORT).show();
                }
                break;

            case PLACE_PICKER_REQUEST:
                if (resultCode == Activity.RESULT_OK) {
                    Place place = PlacePicker.getPlace(data, getContext());
                    location= new ParseGeoPoint(place.getLatLng().latitude,place.getLatLng().longitude);
                    String toastMsg = String.format("Place: %s", place.getName());
                    Toast.makeText(getContext(), toastMsg, Toast.LENGTH_LONG).show();
                    selectedImage = null;
                    selectedFile = null;
                    imageAttachmentPreview.setVisibility(View.VISIBLE);
                    Glide.with(getContext())
                            .load(R.drawable.ic_place)
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


}
