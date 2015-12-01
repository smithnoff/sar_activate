package com.example.usuario.soyactivista.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.EventListener;




import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by Usuario on 29/10/2015.
 */
public class FragmentEscribirMensaje extends Fragment {
    private EditText textArea;
    private TextView contador;
    private TextView mensajeAviso;
    public static ParseGeoPoint ubicacion = null; //aqui se guarda la ubicacion
    private Button publicar;
    private ImageButton botonUbicacion;
    private String mensaje;
    View vi;


    private ImageButton imagen;
    static int numero = (int) (Math.random() *1000) + 1;
    private static byte[] imagenSeleccionada = null; //aqui se guarda la imagen
    private Bitmap bitmap;

    private static final int ACTIVITY_SELECT_IMAGE = 1020;

    private String APP_DIRECTORY = "myPictureApp/";
    private String MEDIA_DIRECTORY = APP_DIRECTORY + "media";
    private String TEMPORAL_PICTURE_NAME = "temporal"+ numero +".jpg";

    private final int PHOTO_CODE = 100;
    private final int SELECT_PICTURE = 200;

    // Fragment COnstructor
    public FragmentEscribirMensaje(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
         vi= inflater.inflate(R.layout.fragment_escribir_mensaje, container, false);
        textArea = (EditText)vi.findViewById(R.id.editTextMensaje);
        contador = (TextView)vi.findViewById(R.id.txtContadorCaracteres);
        publicar = (Button)vi.findViewById(R.id.button2);
        botonUbicacion = (ImageButton)vi.findViewById(R.id.imageButton3);
        mensajeAviso = (TextView)vi.findViewById(R.id.avisoInsertado);
        imagen = (ImageButton)vi.findViewById(R.id.imageButton2);

        textArea.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                contador.setText(String.valueOf(textArea.getText().length())+"/200");
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Attach image button behavior
        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CharSequence[] options = {"Tomar foto", "Elegir de galeria", "Cancelar"};
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                builder.setTitle("Elija una opcion:");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int seleccion) {
                        if (options[seleccion] == "Tomar foto") {
                            openCamera();
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


        // Create Message Button
        publicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                publicar();
            }
        });

        // Add location button
        botonUbicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DMap mapadialogo = new DMap();
                mapadialogo.setmsn(mensajeAviso);
                mapadialogo.show(getFragmentManager(), "dialog");

            }
        });

        return vi;
    }

    private void openCamera() {
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
                    mensajeAviso.setVisibility(View.VISIBLE);
                    mensajeAviso.setText("");
                }
                break;
            }

            case SELECT_PICTURE: {
                if (resultCode == Activity.RESULT_OK) {
                    Uri path = data.getData();

                    try {
                        InputStream imageStream = getContext().getContentResolver().openInputStream(path);
                        bitmap = BitmapFactory.decodeStream(imageStream);

                        preparePhoto(bitmap);
                        if(ubicacion!=null){
                            ubicacion=null;
                        }
                        mensajeAviso.setVisibility(View.VISIBLE);
                        mensajeAviso.setText("Se a adjuntado Una imagen");
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    mensajeAviso.setVisibility(View.INVISIBLE);
                    mensajeAviso.setText("");
                }
                break;
            }
        }
    }

    private void decodeBitmap(String dir) {
        Bitmap bitmap;
        bitmap = BitmapFactory.decodeFile(dir);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

        this.imagenSeleccionada = stream.toByteArray();

        if(ubicacion!=null){
            ubicacion=null;
        }
        mensajeAviso.setVisibility(View.VISIBLE);
        mensajeAviso.setText("Se a adjuntado Una imagen");
    }


    private void publicar(){
        final ProgressDialogFragment pg = new ProgressDialogFragment();
        pg.setTitulo("Publicando");
        pg.setMensajeCargando("Se esta publicando tu mensaje...");
        pg.show(getFragmentManager(), "cargando");

        mensaje = textArea.getText().toString();

        ParseUser usuarioActual = ParseUser.getCurrentUser();

        //Nuevo mensaje simple
        ParseObject post = new ParseObject("Mensaje");
        post.put("texto", mensaje);
        post.put("autor", usuarioActual);
        post.put("reportado",false);

        if (imagenSeleccionada != null){
            String nombreArchivo = usuarioActual.getUsername()+ numero +".jpg";
            ParseFile fileImagen = new ParseFile(nombreArchivo, imagenSeleccionada);
            fileImagen.saveInBackground();
            post.put("adjunto",fileImagen);
        }
        if(ubicacion!=null) {
         post.put("ubicacion",ubicacion);
        }


        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    pg.dismiss();
                    Toast.makeText(getContext(), "Mensaje Publicado", Toast.LENGTH_SHORT).show();
                    Fragment fragment = new FragmentListarMensajes();
                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.content_frame, fragment)
                            .commit();
                } else {
                    pg.dismiss();
                    Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });


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
        imagenSeleccionada = bos.toByteArray();

    }

    public static class DMap extends DialogMap{
        /**clase con los eventos de los botones aceptar y cancelar implementados**/
        public DMap(){}
        TextView mensaje;

        public void setmsn(TextView mensaje) {
            this.mensaje=null;
            this.mensaje = mensaje;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            ((Button)getMapBotonCancelar()).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });

            ((Button)getMapBotonAceptar()).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ubicacion = getUbicacionMarca();
                    if(ubicacion!=null) {
                        if(imagenSeleccionada!=null)
                        imagenSeleccionada=null;
                        mensaje.setVisibility(View.VISIBLE);
                        mensaje.setText("Se a adjuntado ubicación gps");

                    }
                    else{
                        mensaje.setVisibility(View.INVISIBLE);
                        mensaje.setText("");
                    }

                    dismiss();
                }
            });
        }

    }

}