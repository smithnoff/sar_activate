package com.example.usuario.soyactivista.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by Brahyam on 25/11/2015.
 */
public class FragmentDetalleActividad extends Fragment {

    private static final String TAG = "FragDetalleActividad";
    private TextView labelPuntaje, labelDescripcion, labelEstado, labelMunicipio, labelParroquia, nombreActual, ubicacionActual, estadoActual, municipioActual, textMeGusta;
    private EditText puntaje, descripcion, objetivo, encargado, creador,  inicio, fin, parroquia; // Edit Field holders
    private Spinner nombre, ubicacion, estado, municipio, estatus; // Spinner holders
    private Button guardar,editar,eliminar,cancelar; // Button holders
    private ImageButton botonMeGusta, botonNoMeGusta;
    private ImageView imagen1,imagen2,imagen3,imagen4;
    private ProgressDialog dialog;
    private ParseObject tipoActividad; // TipoActividad to be associated with Actividad

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
        labelPuntaje = (TextView)v.findViewById(R.id.labelPuntaje);
        labelDescripcion = (TextView)v.findViewById(R.id.labelDescripcion);
        ubicacionActual = (TextView)v.findViewById(R.id.valueUbicacion);
        labelEstado = (TextView)v.findViewById(R.id.labelEstado);
        estadoActual = (TextView)v.findViewById(R.id.estadoActual);
        labelMunicipio = (TextView)v.findViewById(R.id.labelMunicipio);
        municipioActual = (TextView)v.findViewById(R.id.municipioActual);
        labelParroquia = (TextView)v.findViewById(R.id.labelParroquia);
        textMeGusta = (TextView)v.findViewById(R.id.valueMeGusta);



        //Asign Text Edit to holders
        puntaje = (EditText)v.findViewById(R.id.editPuntaje);
        descripcion = (EditText)v.findViewById(R.id.editDescripcion);
        objetivo = (EditText)v.findViewById(R.id.editObjetivo);
        encargado = (EditText)v.findViewById(R.id.editEncargado);
        creador = (EditText)v.findViewById(R.id.editCreador);
        inicio = (EditText)v.findViewById(R.id.editInicio);
        fin = (EditText)v.findViewById(R.id.editFin);
        parroquia = (EditText)v.findViewById(R.id.editParroquia);
        inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fin.setText("");
                DialogDatePicker picker2 = new DialogDatePicker();
                picker2.show(getFragmentManager(), "Fecha de Final");



            }
        });
        fin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fin.setText("");
                DialogDatePicker picker2 = new DialogDatePicker();
                picker2.show(getFragmentManager(), "Fecha de Final");



            }
        });

        // Asigns Spinners to holders
        nombre = (Spinner)v.findViewById(R.id.spinNombreActividad);
        ubicacion = (Spinner)v.findViewById(R.id.spinUbicacion);
        estado = (Spinner)v.findViewById(R.id.spinEstado);
        municipio = (Spinner)v.findViewById(R.id.spinMunicipio);
        //parroquia = (Spinner)v.findViewById(R.id.spinParroquia); Commented as will be used as Edit Text while data is parsed.
        estatus = (Spinner)v.findViewById(R.id.spinEstatus);
        estatus.setEnabled(false);

        // Asign Buttons to holders
        editar = (Button)v.findViewById(R.id.botonEditar);
        guardar = (Button)v.findViewById(R.id.botonGuardar);
        eliminar = (Button)v.findViewById(R.id.botonEliminar);
        cancelar = (Button)v.findViewById(R.id.botonCancelar);

        botonMeGusta = (ImageButton)v.findViewById(R.id.botonMeGusta);
        botonNoMeGusta = (ImageButton)v.findViewById(R.id.botonNoMeGusta);

        // Assign Images to PlaceHolders
        imagen1 = (ImageView)v.findViewById(R.id.imagen1);
        imagen2 = (ImageView)v.findViewById(R.id.imagen2);
        imagen3 = (ImageView)v.findViewById(R.id.imagen3);
        imagen4 = (ImageView)v.findViewById(R.id.imagen4);


        // Show buttons depending on Role or if user is owner
        if(usuarioActual.getInt("rol") == 1 || usuarioActual.getObjectId().equals(getArguments().getString("creadorId"))){
            editar.setVisibility(View.VISIBLE);
            eliminar.setVisibility(View.VISIBLE);
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
        // Fill Name Spinner from parse
        ParseQueryAdapter.QueryFactory<ParseObject> factory = new ParseQueryAdapter.QueryFactory<ParseObject>() {
            public ParseQuery create() {
                ParseQuery query = new ParseQuery("TipoActividad");
                return query;
            }
        };
        // Overrriding ParseQueryAdapter getViewTypeCount method to get past issue 79011
        final ParseQueryAdapter<ParseObject> adapter = new ParseQueryAdapter<ParseObject>(this.getActivity(), factory){
            @Override
            public int getViewTypeCount(){
                return 1;
            }
        };
        adapter.setTextKey("nombre");
        nombre.setAdapter(adapter);

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

        this.llenarSpinnerdesdeId(estatus, R.array.Estatuses);
        if(getArguments().getString("estatus") == "En Ejecución")
            estatus.setSelection(0);
        else
            estatus.setSelection(1);

        inicio.setText(getArguments().getString("inicio"));
        fin.setText(getArguments().getString("fin"));

        //Fill Spinners with Preset Options
        this.llenarSpinnerdesdeId(ubicacion, R.array.Ubicaciones);
        this.llenarSpinnerdesdeId(estado, R.array.Estados);

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

        // Load Images
        if(getArguments().getString("imagen1") != null){
            imagen1.setVisibility(View.VISIBLE);
            Glide.with(getContext())
                    .load(getArguments().getString("imagen1"))
                    .placeholder(R.mipmap.ic_placeholder)
                    .centerCrop()
                    .into(imagen1);

            imagen1.setOnClickListener(seeImageDetail(getArguments().getString("imagen1")));
        }

        if(getArguments().getString("imagen2") != null){
            imagen2.setVisibility(View.VISIBLE);
            Glide.with(getContext())
                    .load(getArguments().getString("imagen2"))
                    .placeholder(R.mipmap.ic_placeholder)
                    .centerCrop()
                    .into(imagen2);

            imagen2.setOnClickListener(seeImageDetail(getArguments().getString("imagen2")));
        }

        if(getArguments().getString("imagen3") != null){
            imagen3.setVisibility(View.VISIBLE);
            Glide.with(getContext())
                    .load(getArguments().getString("imagen3"))
                    .placeholder(R.mipmap.ic_placeholder)
                    .centerCrop()
                    .into(imagen3);

            imagen3.setOnClickListener(seeImageDetail(getArguments().getString("imagen3")));
        }

        if(getArguments().getString("imagen4") != null){
            imagen4.setVisibility(View.VISIBLE);
            Glide.with(getContext())
                    .load(getArguments().getString("imagen4"))
                    .placeholder(R.mipmap.ic_placeholder)
                    .centerCrop()
                    .into(imagen4);

            imagen4.setOnClickListener(seeImageDetail(getArguments().getString("imagen4")));
        }

        //Load Likes
        Log.d("DETALLE", "Value Likes; "+getArguments().getInt("meGusta"));
        final String procureLikes = String.valueOf(getArguments().getInt("meGusta"));
        Log.d("DETALLE", "String Likes; "+procureLikes);
        textMeGusta.setText(procureLikes);


        // Buttons Behavior
        editar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Hide Edit button/show save

                //Show/enable all editors
                nombreActual.setVisibility(View.GONE);
                nombre.setVisibility(View.VISIBLE);

                objetivo.setEnabled(true);
                ubicacionActual.setVisibility(View.GONE);
                ubicacion.setVisibility(View.VISIBLE);
                ubicacion.setEnabled(true);

                encargado.setEnabled(true);

                estatus.setEnabled(true);

                inicio.setEnabled(true);
                fin.setEnabled(true);

                eliminar.setVisibility(View.GONE);
                editar.setVisibility(View.GONE);
                guardar.setVisibility(View.VISIBLE);
                cancelar.setVisibility(View.VISIBLE);

            }
        });

        cancelar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                //Show/enable all editors
                nombreActual.setVisibility(View.VISIBLE);
                nombre.setVisibility(View.GONE);

                objetivo.setEnabled(false);
                ubicacionActual.setVisibility(View.VISIBLE);
                ubicacion.setVisibility(View.GONE);
                ubicacion.setEnabled(false);

                encargado.setEnabled(false);

                estatus.setEnabled(false);

                inicio.setEnabled(false);
                fin.setEnabled(false);

                eliminar.setVisibility(View.VISIBLE);
                editar.setVisibility(View.VISIBLE);
                guardar.setVisibility(View.GONE);
                cancelar.setVisibility(View.GONE);

            }
        });

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Confirmar");
                builder.setMessage("¿Está seguro de que desea editar la actividad?");

                builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialogo, int which) {

                        dialog = ProgressDialog.show(getActivity(), "", "Guardando Actividad", true);

                        final ParseObject tipoActividad = ParseObject.createWithoutData("TipoActividad", getArguments().getString("tipoId"));

                        // Retrieve the object by id from parse
                        final ParseQuery<ParseObject> query = ParseQuery.getQuery("Actividad");
                        query.getInBackground(getArguments().getString("id"), new GetCallback<ParseObject>() {
                            public void done(ParseObject actividad, ParseException e) {
                                if (e == null) {
                                    actividad.put("tipoActividad", tipoActividad);
                                    actividad.put("objetivo", objetivo.getText().toString());
                                    actividad.put("ubicacion", ubicacion.getSelectedItem().toString());
                                    if (ubicacion.getSelectedItem().toString() == "Estadal" && estado.getSelectedItem() != null) {
                                        actividad.put("estado", estado.getSelectedItem().toString());
                                        actividad.put("municipio", municipio.getSelectedItem().toString());
                                        actividad.put("parroquia", parroquia.getText().toString());
                                    }
                                    actividad.put("encargado", encargado.getText().toString());

                                    actividad.put("estatus", estatus.getSelectedItem().toString());
                                    // Declare Date Format
                                    DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                                    try {
                                        actividad.put("inicio", df.parse(inicio.getText().toString()));
                                        actividad.put("fin", df.parse(fin.getText().toString()));
                                    } catch (java.text.ParseException ex) {
                                        dialog.dismiss();
                                        Toast.makeText(getActivity(), ex.toString(), Toast.LENGTH_SHORT).show();
                                    }

                                    actividad.saveInBackground(new SaveCallback() {
                                        public void done(ParseException e) {
                                            if (e == null) {
                                                dialog.dismiss();
                                                Toast.makeText(getActivity(), "Actividad Guardada", Toast.LENGTH_SHORT).show();
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

                                } else {
                                    // Object not found in Parse
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

            }
        });

        eliminar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0){


                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Confirmar");
                builder.setMessage("¿Está seguro que desea eliminar la actividad?");

                builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialogo, int which) {
                        // Redirect View to list
                        Fragment fragment = new FragmentListarActividad();
                        getFragmentManager()
                                .beginTransaction()
                                .replace(R.id.content_frame, fragment)
                                .commit();
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



        return v;
    }

    //Auxiliar method for filling spinners with String Arrays
    public void llenarSpinnerdesdeId(Spinner spin,int id){
        ArrayAdapter spinner_adapter = ArrayAdapter.createFromResource(getActivity(), id, android.R.layout.simple_spinner_item);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(spinner_adapter);
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

}
