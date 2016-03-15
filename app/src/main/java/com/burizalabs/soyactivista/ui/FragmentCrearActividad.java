package com.burizalabs.soyactivista.ui;

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

import com.burizalabs.soyactivista.utils.ErrorCodeHelpers;
import com.parse.FindCallback;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.burizalabs.soyactivista.utils.TextHelpers;
import com.burizalabs.soyactivista.R;


/**
 * Created by Brahyam on 25/11/2015.
 */
public class FragmentCrearActividad extends Fragment {


    private String TAG = "CREAR-ACTIVIDAD";
    private TextView labelPuntaje, labelDescripcion, labelEstado, labelMunicipio, labelParroquia, labelFotos;
    private TextView textCharCountObjetive,inicio, fin;
    private EditText puntaje, descripcion, objetivo, encargado, creador; // Edit Field holders
    private Spinner nombre, ubicacion, estado, municipio, parroquia; // Spinner holders
    private Button crear,cancelar; // Button holders
    private ImageButton adjuntarFoto; // Add Image Button.
    private ProgressDialog dialog;
    private ParseObject tipoActividad; // TipoActividad to be associated with Actividad
    private DialogDatePicker picker1;
    private DialogDatePicker picker2;
    private View view;
    private ParseUser usuarioActual;

    // Image Storing Variables/Constants
    private Bitmap bitmap;
    static int random = (int) (Math.random() *1000) + 1;
    private static byte[] imagenSeleccionada = null;
    private static byte[] imagenSeleccionada2 = null;
    private static byte[] imagenSeleccionada3 = null;
    private static byte[] imagenSeleccionada4 = null;//Array to store Image
    private String APP_DIRECTORY = "fotosSoyActivista/";
    private String MEDIA_DIRECTORY = APP_DIRECTORY + "media";
    private String TEMPORAL_PICTURE_NAME = "temporal"+ random +".jpg";

    private final int PHOTO_CODE = 100;
    private final int SELECT_PICTURE = 200;

    private ArrayList<ParseObject> activityTypes;
    private ArrayList<String> activityTypeNames;

    // Class Constructor
    public FragmentCrearActividad(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        //Choose fragment to inflate
        view = inflater.inflate(R.layout.fragment_crear_actividad, container, false);

        //Gets Current User
        usuarioActual = ParseUser.getCurrentUser();

        initializeComponents();

        fetchActivityTypes();

        //Fill Spinners with Preset Options
        this.llenarSpinnerdesdeId(ubicacion, R.array.Ubicaciones);
        this.llenarSpinnerdesdeId(estado, R.array.Estados);

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
                String nombreEstado = TextHelpers.NormalizeResource(estado.getSelectedItem().toString());
                int arrayId = getResources().getIdentifier(nombreEstado, "array", getActivity().getPackageName());
                llenarSpinnerdesdeId(municipio, arrayId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        municipio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                parroquia.setAdapter(null);
                String nombreEstado = TextHelpers.NormalizeResource(estado.getSelectedItem().toString());
                String nombreMunicipio = TextHelpers.NormalizeResource(municipio.getSelectedItem().toString());
                Log.d(TAG,"Looking for resource:"+nombreEstado+"_"+nombreMunicipio);
                int arrayId = getResources().getIdentifier(nombreEstado+"_"+nombreMunicipio, "array", getActivity().getPackageName());
                llenarSpinnerdesdeId(parroquia, arrayId);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
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
                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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

                Log.v(TAG,"valor es: "+MinDate());

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
                            if (MinDate() > MaxDate()) {
                                Toast.makeText(getContext(), "La fecha de Finalizacion debe ser mayor a la Inicial", Toast.LENGTH_SHORT).show();
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
                                    actividad.put("parroquia", parroquia.getSelectedItem().toString());
                                }

                                actividad.put("encargado", encargado.getText().toString());
                                actividad.put("creador", usuarioActual);
                                actividad.put("estatus", "En Ejecución");
                                // Declare Date Format
                                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

                                try {
                                    actividad.put("inicio", df.parse(inicio.getText().toString()));
                                    actividad.put("inicio", df.parse(inicio.getText().toString()));
                                    actividad.put("fin", df.parse(fin.getText().toString()));
                                } catch (java.text.ParseException e) {
                                    dialog.dismiss();
                                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, e.toString());
                                }

                                actividad.put("meGusta", 0);


                                // Save Activity
                                actividad.saveInBackground(new SaveCallback() {
                                    public void done(ParseException e) {
                                        if (e == null) {
                                            dialog.dismiss();
                                            Toast.makeText(getActivity(), "Actividad Creada", Toast.LENGTH_SHORT).show();


                                            // Handle Image uploading
                                            if (imagenSeleccionada != null) {
                                                // Save the scaled image to Parse

                                                int value = (int) (Math.random() * 1000 + 2);
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

                                                int value2 = (int) (Math.random() * 1000 + 3);
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
                                                int value3 = (int) (Math.random() * 1000 + 5);
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

                                                int value4 = (int) (Math.random() * 1000 + 7);
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

                                            // Publish Notification of Activity Created.
                                            ParseObject mensaje = new ParseObject("Mensaje");
                                            mensaje.put("texto", usuarioActual.getString("nombre") + " ha iniciado una nueva actividad: " + tipoActividad.getString("nombre"));
                                            mensaje.put("autor", usuarioActual);
                                            mensaje.put("reportado", false);
                                            mensaje.saveEventually();

                                            // Add activism points to activity creator
                                            usuarioActual.increment("puntosActivismo", tipoActividad.getInt("puntaje"));

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




        return view;
    }

    /**
     * Queries Parse.com db for Activity Types and initializes activity name spinner.
     */
    private void fetchActivityTypes() {

        dialog = ProgressDialog.show(getContext(),"Buscando Tipos de Actividad","Cargando",true);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("TipoActividad");
        query.whereEqualTo("activa", true);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null){
                    dialog.dismiss();
                    if(objects.size() > 0){
                        activityTypes = new ArrayList<ParseObject>();
                        activityTypeNames = new ArrayList<String>();

                        for (int i = 0; i<objects.size();i++){
                            activityTypes.add(objects.get(i));
                            activityTypeNames.add(objects.get(i).getString("nombre"));
                        }

                        ArrayAdapter<String> tipoActividadAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,activityTypeNames);
                        tipoActividadAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        nombre.setAdapter(tipoActividadAdapter);

                        // On Activity selected populate puntaje and descripcion
                        nombre.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                tipoActividad = activityTypes.get(position);
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
                    }
                    else{
                        Toast.makeText(getContext(),"No existen tipos de actividad creados.",Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    dialog.dismiss();
                    Log.e(TAG, ErrorCodeHelpers.resolveLogErrorString(e.getCode(),e.getMessage()));
                    Toast.makeText(getContext(),ErrorCodeHelpers.resolveErrorCode(e.getCode()),Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * Method that initializes all view components and sets default values
     */
    private void initializeComponents() {

        //Asign TextViews to Holders
        labelPuntaje = (TextView)view.findViewById(R.id.labelPuntaje);
        labelDescripcion = (TextView)view.findViewById(R.id.labelDescripcion);
        labelEstado = (TextView)view.findViewById(R.id.labelEstado);
        labelMunicipio = (TextView)view.findViewById(R.id.labelMunicipio);
        labelParroquia = (TextView)view.findViewById(R.id.labelParroquia);
        labelFotos = (TextView)view.findViewById(R.id.valueFoto);
        textCharCountObjetive = (TextView)view.findViewById(R.id.textCharCountObjetive);


        //Asign Text Edit to holders
        puntaje = (EditText)view.findViewById(R.id.editPuntaje);
        descripcion = (EditText)view.findViewById(R.id.editDescripcion);
        objetivo = (EditText)view.findViewById(R.id.editObjetivo);
        encargado = (EditText)view.findViewById(R.id.editEncargado);
        creador = (EditText)view.findViewById(R.id.editCreador);
        inicio = (TextView)view.findViewById(R.id.textViewFechaInicio);
        fin = (TextView)view.findViewById(R.id.textViewFechaFin);

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

        inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fin.setSelected(false);
                fin.setEnabled(true);
                inicio.setSelected(true);
                picker1 = new DialogDatePicker();
                picker1.show(getFragmentManager(), "Fecha de inicio");
            }
        });

        fin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fin.setSelected(true);
                inicio.setSelected(false);
                picker2 = new DialogDatePicker();
                picker2.show(getFragmentManager(), "Fecha de Fin");
            }
        });

        // Asigns Spinners to holders
        nombre = (Spinner)view.findViewById(R.id.spinNombreActividad);
        ubicacion = (Spinner)view.findViewById(R.id.spinUbicacion);
        estado = (Spinner)view.findViewById(R.id.spinEstado);
        municipio = (Spinner)view.findViewById(R.id.spinMunicipio);
        parroquia = (Spinner)view.findViewById(R.id.spinParroquia);

        // Asign Buttons to holders
        crear = (Button)view.findViewById(R.id.botonCrear);
        cancelar = (Button)view.findViewById(R.id.botonCancelar);
        adjuntarFoto = (ImageButton)view.findViewById(R.id.botonAdjuntarFoto);

        // Load Defaults
        creador.setEnabled(false);
        creador.setText(usuarioActual.getString("nombre"));

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

    public long MinDate(){
        long num = 0;
        Calendar c = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date ini = formatter.parse(inicio.getText().toString());
            c.setTime(ini);
            num = c.getTimeInMillis();
        } catch (java.text.ParseException i) {
            Toast.makeText(getActivity(), i.toString(), Toast.LENGTH_SHORT).show();
        }

        return num;
    }

    public long MaxDate(){
        long num = 0;
        Calendar c = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date ini = formatter.parse(fin.getText().toString());
            c.setTime(ini);
            num = c.getTimeInMillis();
        } catch (java.text.ParseException i) {
            Toast.makeText(getActivity(), i.toString(), Toast.LENGTH_SHORT).show();
        }

        return num;
    }

}
