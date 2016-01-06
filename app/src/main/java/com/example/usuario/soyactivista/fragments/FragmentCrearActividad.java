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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import soy_activista.quartzapp.com.soy_activista.R;


/**
 * Created by Brahyam on 25/11/2015.
 */
public class FragmentCrearActividad extends Fragment {


    private String TAG = "CREAR-ACTIVIDAD";
    private TextView labelPuntaje, labelDescripcion, labelEstado, labelMunicipio, labelParroquia, labelFotos,fechaInicial;
    private TextView textCharCountObjetive;
    private EditText puntaje, descripcion, objetivo, encargado, creador,  inicio, fin, parroquia; // Edit Field holders
    private Spinner nombre, ubicacion, estado, municipio; // Spinner holders
    private Button crear,cancelar; // Button holders
    private ImageButton adjuntarFoto,calendarInicio,calendarFin; // Add Image Button.
    private ProgressDialog dialog;
    private ParseObject tipoActividad; // TipoActividad to be associated with Actividad

    // Image Storing Variables/Constants
    private Bitmap bitmap;
    static int random = (int) (Math.random() *1000) + 1;
    static int random2 = (int) (Math.random() *1000) + 1;
    static int random3 = (int) (Math.random() *1000) + 1;
    static int random4 = (int) (Math.random() *1000) + 1;
    private static byte[] imagenSeleccionada = null;
    private static byte[] imagenSeleccionada2 = null;
    private static byte[] imagenSeleccionada3 = null;
    private static byte[] imagenSeleccionada4 = null;//Array to store Image
    private String APP_DIRECTORY = "fotosSoyActivista/";
    private String MEDIA_DIRECTORY = APP_DIRECTORY + "media";
    private String TEMPORAL_PICTURE_NAME = "temporal"+ random +".jpg";
   public int presionado;
    private final int PHOTO_CODE = 100;
    private final int SELECT_PICTURE = 200;

    // Class Constructor
    public FragmentCrearActividad(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        //Choose fragment to inflate
        View v = inflater.inflate(R.layout.fragment_crear_actividad, container, false);

        //Gets Current User
        final ParseUser usuarioActual = ParseUser.getCurrentUser();

        //Asign TextViews to Holders
        labelPuntaje = (TextView)v.findViewById(R.id.labelPuntaje);
        labelDescripcion = (TextView)v.findViewById(R.id.labelDescripcion);
        labelEstado = (TextView)v.findViewById(R.id.labelEstado);
        labelMunicipio = (TextView)v.findViewById(R.id.labelMunicipio);
        labelParroquia = (TextView)v.findViewById(R.id.labelParroquia);
        fechaInicial = (TextView)v.findViewById(R.id.textViewFechaInicio);
        labelFotos = (TextView)v.findViewById(R.id.valueFoto);
        textCharCountObjetive = (TextView)v.findViewById(R.id.textCharCountObjetive);


        //Asign Text Edit to holders
        puntaje = (EditText)v.findViewById(R.id.editPuntaje);
        descripcion = (EditText)v.findViewById(R.id.editDescripcion);
        objetivo = (EditText)v.findViewById(R.id.editObjetivo);
        encargado = (EditText)v.findViewById(R.id.editEncargado);
        creador = (EditText)v.findViewById(R.id.editCreador);
        inicio = (EditText)v.findViewById(R.id.textViewFechaInicio);
       fin = (EditText)v.findViewById(R.id.textViewFechaFin);
        parroquia = (EditText)v.findViewById(R.id.editParroquia);
        calendarInicio= (ImageButton) v.findViewById(R.id.imgCalendarInicio);
        calendarFin= (ImageButton) v.findViewById(R.id.imgCalendarFin);

        // Update CharCount on writting
        objetivo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textCharCountObjetive.setText(String.valueOf(objetivo.getText().length()) + "/500");
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        calendarInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                calendarInicio.setSelected(true);
                DialogDatePicker picker2 = new DialogDatePicker();
                calendarFin.setSelected(false);
                picker2.show(getFragmentManager(), "Fecha de inicio");



            }
        });

        calendarFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarFin.setSelected(true);
                calendarInicio.setSelected(false);
                DialogDatePicker picker2 = new DialogDatePicker();
                picker2.show(getFragmentManager(), "Fecha de Fin");


            }
        });
        // Asigns Spinners to holders
        nombre = (Spinner)v.findViewById(R.id.spinNombreActividad);
        ubicacion = (Spinner)v.findViewById(R.id.spinUbicacion);
        estado = (Spinner)v.findViewById(R.id.spinEstado);
        municipio = (Spinner)v.findViewById(R.id.spinMunicipio);
        //parroquia = (Spinner)v.findViewById(R.id.spinParroquia); Commented as will be used as Edit Text while data is parsed.

        // Asign Buttons to holders
        crear = (Button)v.findViewById(R.id.botonCrear);
        cancelar = (Button)v.findViewById(R.id.botonCancelar);
        adjuntarFoto = (ImageButton)v.findViewById(R.id.botonAdjuntarFoto);

        // Load Defaults
        creador.setEnabled(false);
        creador.setText(usuarioActual.getString("nombre"));

        //Fill Spinners with Preset Options
        this.llenarSpinnerdesdeId(ubicacion, R.array.Ubicaciones);
        this.llenarSpinnerdesdeId(estado, R.array.Estados);

        // Fill Name Spinner from parse
        ParseQueryAdapter.QueryFactory<ParseObject> factory = new ParseQueryAdapter.QueryFactory<ParseObject>() {
            public ParseQuery create() {
                ParseQuery query = new ParseQuery("TipoActividad");
                query.whereEqualTo("activa",true);
                return query;
            }
        };
        // Overriding ParseQueryAdapter getViewTypeCount method to get past issue 79011
        final ParseQueryAdapter<ParseObject> adapter = new ParseQueryAdapter<ParseObject>(this.getActivity(), factory){
            @Override
            public int getViewTypeCount(){
                return 1;
            }
        };

        adapter.setTextKey("nombre");
        nombre.setAdapter(adapter);

        // On Activity selected populate puntaje and descripcion
        nombre.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tipoActividad = adapter.getItem(position);
                puntaje.setText(Integer.toString(tipoActividad.getInt("puntaje")));
                labelPuntaje.setVisibility(View.VISIBLE);
                puntaje.setVisibility(View.VISIBLE);
                descripcion.setText(tipoActividad.getString("descripcion"));
                labelDescripcion.setVisibility(View.VISIBLE);
                descripcion.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Spinner OnItemSelected Listeners
        ubicacion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0){ // Estadal Selected
                    //Show remaining Text/Spinners/Fields
                    labelEstado.setVisibility(View.VISIBLE);
                    estado.setVisibility(View.VISIBLE);
                    labelMunicipio.setVisibility(View.VISIBLE);
                    municipio.setVisibility(View.VISIBLE);
                    labelParroquia.setVisibility(View.VISIBLE);
                    parroquia.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        estado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                municipio.setAdapter(null);
                llenarSpinnerdesdeId(municipio, getResources().getIdentifier(estado.getSelectedItem().toString().replace(' ', '_'), "array", getActivity().getPackageName()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Buttons Behavior
        adjuntarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final CharSequence[] options = {"Tomar foto", "Elegir de galeria", "Cancelar"};
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

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


        crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Confirmar");
                builder.setMessage("¿Estas seguro que desea crear la actividad?");


                builder.setPositiveButton("Crear", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialogo, int which) {

                        if
                                (
                                objetivo.getText().toString().trim().length() == 0 ||
                                        encargado.getText().toString().trim().length() == 0 ||
                                     inicio.getText().toString().trim().length() == 0 ||
                                      fin.getText().toString().trim().length() == 0
                                ) {
                            Toast.makeText(getContext(), "Completa los campos vacíos", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            dialog = ProgressDialog.show(getActivity(), "", "Creando Actividad", true);

                            // Fill ParseObject to send
                            final ParseObject actividad = new ParseObject("Actividad");
                            actividad.put("tipoActividad", tipoActividad);
                            actividad.put("objetivo", objetivo.getText().toString());
                            actividad.put("ubicacion", ubicacion.getSelectedItem().toString());
                            if (ubicacion.getSelectedItem().toString() == "Estadal" && estado.getSelectedItem() != null) {
                                actividad.put("estado", estado.getSelectedItem().toString());
                                actividad.put("municipio", municipio.getSelectedItem().toString());
                                actividad.put("parroquia", parroquia.getText().toString());
                            }
                            actividad.put("encargado", encargado.getText().toString());
                            actividad.put("creador", usuarioActual);
                            actividad.put("estatus", "En Ejecución");
                            // Declare Date Format
                            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                            try {
                                actividad.put("inicio", df.parse(inicio.getText().toString()));
                                actividad.put("fin", df.parse(fin.getText().toString()));
                            } catch (java.text.ParseException e) {
                                dialog.dismiss();
                                Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                                Log.d(TAG, e.toString());
                            }

                            actividad.put("meGusta", 0);

                            // Handle Image uploading
                            if (imagenSeleccionada != null) {
                                // Save the scaled image to Parse

                                int value = (int)(Math.random() * 1000 + 2);
                                ParseFile fotoFinal = new ParseFile(usuarioActual.getUsername() + value + ".jpg", imagenSeleccionada);

                                //ParseFile fotoFinal = new ParseFile(usuarioActual.getUsername() + random + "1.jpg", imagenSeleccionada);

                                actividad.put("imagen1", fotoFinal);

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

                            if (imagenSeleccionada2 != null) {
                                // Save the scaled image to Parse

                                int value2 = (int)(Math.random() * 1000 + 3);
                                ParseFile fotoFinal2 = new ParseFile(usuarioActual.getUsername() + value2 + ".jpg", imagenSeleccionada2);

                                //ParseFile fotoFinal2 = new ParseFile(usuarioActual.getUsername() + random + "2.jpg", imagenSeleccionada2);

                                actividad.put("imagen2", fotoFinal2);

                                fotoFinal2.saveInBackground(new SaveCallback() {
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

                            if (imagenSeleccionada3 != null) {
                                // Save the scaled image to Parse
                                int value3 = (int)(Math.random() * 1000 + 5);
                                ParseFile fotoFinal3 = new ParseFile(usuarioActual.getUsername() + value3 + ".jpg", imagenSeleccionada3);

                                //ParseFile fotoFinal3 = new ParseFile(usuarioActual.getUsername() + random + "3.jpg", imagenSeleccionada3);

                                actividad.put("imagen3", fotoFinal3);

                                fotoFinal3.saveInBackground(new SaveCallback() {
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

                            if (imagenSeleccionada4 != null) {
                                // Save the scaled image to Parse

                                int value4 = (int)(Math.random() * 1000 + 7);
                                ParseFile fotoFinal4 = new ParseFile(usuarioActual.getUsername() + value4 + ".jpg", imagenSeleccionada4);

                                //ParseFile fotoFinal4 = new ParseFile(usuarioActual.getUsername() + random + "4.jpg", imagenSeleccionada4);

                                actividad.put("imagen4", fotoFinal4);

                                fotoFinal4.saveInBackground(new SaveCallback() {
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
                            // Save Activity
                            actividad.saveInBackground(new SaveCallback() {
                                public void done(ParseException e) {
                                    if (e == null) {
                                        dialog.dismiss();
                                        Toast.makeText(getActivity(), "Actividad Creada", Toast.LENGTH_SHORT).show();

                                        // Publish Notification of Activity Created.
                                        ParseObject mensaje = new ParseObject("Mensaje");
                                        mensaje.put("texto",usuarioActual.getString("nombre")+" ha iniciado una nueva actividad: "+tipoActividad.getString("nombre"));
                                        mensaje.put("autor",usuarioActual);
                                        mensaje.put("reportado",false);
                                        mensaje.saveEventually();

                                        // Redirect View to Boletin de Actividades
                                        Fragment fragment = new FragmentListarActividad();
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
            }
        });

        cancelar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0){
                // Redirect View to list
                Fragment fragment = new FragmentListarActividad();
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, fragment)
                        .commit();
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
                    Toast.makeText(getActivity(),"Error Inesperado."+resultCode, Toast.LENGTH_SHORT).show();
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


                        Toast.makeText(getActivity(),"Se ha adjuntado una imagen correctamente.", Toast.LENGTH_SHORT).show();
                        //labelFotos.setText("1");
                    } catch (FileNotFoundException e) {
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

        if(imagenSeleccionada == null)
        {
            imagenSeleccionada = bos.toByteArray();
            labelFotos.setText("1");
        }
        else
        {
            if(imagenSeleccionada2 == null)
            {
                imagenSeleccionada2 = bos.toByteArray();
                labelFotos.setText("2");
            }
            else
            {
                if(imagenSeleccionada3 == null)
                {
                    imagenSeleccionada3 = bos.toByteArray();
                    labelFotos.setText("3");
                }
                else
                {
                    if(imagenSeleccionada4 == null)
                    {
                        imagenSeleccionada4 = bos.toByteArray();
                        labelFotos.setText("4");
                    }
                    else
                    {
                        Toast.makeText(getActivity(),"No se puede adjuntar más de 4 imagenes", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }

    }



}
