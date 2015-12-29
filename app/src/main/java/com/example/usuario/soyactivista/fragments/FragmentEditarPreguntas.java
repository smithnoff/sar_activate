package com.example.usuario.soyactivista.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import soy_activista.quartzapp.com.soy_activista.R;

import static java.lang.Integer.parseInt;

/**
 * Created by darwin on 29/12/2015.
 */
public class FragmentEditarPreguntas extends Fragment {

    private EditText nombre, opcion_uno,opcion_dos,opcion_tres,opcion_cuatro; // Edit Field holders
    private Spinner puntaje,dificultad,tiempo; // Spinner holders
    private Button editar,eliminar,guardar, cancelar; // Button holders
    private TextView textPuntaje,textDificultad,textTiempo;
    private ProgressDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        //Choose fragment to inflate
        View v = inflater.inflate(R.layout.fragment_editar_preguntas, container, false);

        //Gets Current User
        final ParseUser usuarioActual = ParseUser.getCurrentUser();



        //Asign Text Edit and text view to holders
        nombre          =   (EditText)v.findViewById(R.id.editPregunta);
        opcion_uno      =   (EditText)v.findViewById(R.id.opcionUno);
        opcion_dos      =   (EditText)v.findViewById(R.id.opcionDos);
        opcion_tres     =   (EditText)v.findViewById(R.id.opcionTres);
        opcion_cuatro   =   (EditText)v.findViewById(R.id.opcionCuatro);
        textPuntaje     =   (TextView)v.findViewById(R.id.valuePuntaje);
        textDificultad  =   (TextView)v.findViewById(R.id.valueDificultad);
        textTiempo      =   (TextView)v.findViewById(R.id.valueTiempo);



        // Asigns Spinners to holders
        puntaje = (Spinner)v.findViewById(R.id.spinnerPuntaje);
        dificultad = (Spinner)v.findViewById(R.id.spinnerDificultad);
        tiempo = (Spinner)v.findViewById(R.id.spinnerTiempoRespuesta);


        // Asign Buttons to holders

        editar = (Button)v.findViewById(R.id.buttonEditarPregunta);
        eliminar = (Button)v.findViewById(R.id.buttonEliminarPregunta);
        guardar = (Button)v.findViewById(R.id.buttonGuardarPreguntas);
        cancelar = (Button)v.findViewById(R.id.buttonCancelar);





        //Fill Spinners with Preset Options
        this.llenarSpinnerdesdeId(puntaje, R.array.Puntuaciones);
        this.llenarSpinnerdesdeId(dificultad, R.array.Dificultad);
        this.llenarSpinnerdesdeId(tiempo, R.array.Tiempo);

        //Set Values from bundle
        nombre.setText(getArguments().getString("pregunta"));
        opcion_uno.setText(getArguments().getString("opcion1"));
        opcion_dos.setText(getArguments().getString("opcion2"));
        opcion_tres.setText(getArguments().getString("opcion3"));
        opcion_cuatro.setText(getArguments().getString("opcion4"));

       this.ObtenerSpinnerdesdePosition(puntaje,getArguments().getString("puntaje"));
       this.ObtenerSpinnerdesdePosition(dificultad,getArguments().getString("dificultad"));
       this.ObtenerSpinnerdesdePosition(tiempo,getArguments().getString("tiempo"));




        // Buttons Behavior
        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                // Turn fields editable
                nombre.setEnabled(true);
                opcion_uno.setEnabled(true);
                opcion_dos.setEnabled(true);
                opcion_tres.setEnabled(true);
                opcion_cuatro.setEnabled(true);


                textPuntaje.setVisibility(View.GONE);
                textDificultad.setVisibility(View.GONE);
                textTiempo.setVisibility(View.GONE);

                puntaje.setVisibility(View.VISIBLE);
                dificultad.setVisibility(View.VISIBLE);
                tiempo.setVisibility(View.VISIBLE);

                // Hide Delete/Edit Button.
                editar.setVisibility(View.GONE);
                eliminar.setVisibility(View.GONE);
                guardar.setVisibility(View.VISIBLE);
                cancelar.setVisibility(View.VISIBLE);
            }
        });


        // Saving method
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Confirmar");
                builder.setMessage("¿Está seguro que desea guardar la actividad?");

                builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialogo, int which) {
                        // Do nothing but close the dialog
                      ParseQuery<ParseObject> query = ParseQuery.getQuery("TipoActividad");

                        // Retrieve the object by id from parse
                        query.getInBackground(getArguments().getString("id"), new GetCallback<ParseObject>() {
                            public void done(ParseObject tipoActividad, ParseException e) {
                                if (e == null) {
                                    // Disable tipoActividad
                                    tipoActividad.put("activa", false);
                                    tipoActividad.saveInBackground();

                                    // Fill new ParseObject to send
                                    ParseObject newTipoActividad = new ParseObject("TipoActividad");
                                    tipoActividad.put("nombre", nombre.getText().toString());
                                    tipoActividad.put("puntaje", parseInt(puntaje.getSelectedItem().toString()));

                                    tipoActividad.put("creador", usuarioActual);
                                    tipoActividad.put("activa",true);

                                    tipoActividad.saveInBackground(new SaveCallback() {
                                        public void done(ParseException e) {
                                            if (e == null) {
                                                dialog.dismiss();
                                                Toast.makeText(getActivity(), "Tipo de actividad Guardada", Toast.LENGTH_SHORT).show();
                                                // Redirect View to Boletin de Actividades
                                                Fragment fragment = new FragmentListarTipoActividad();
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


        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Turn fields not editable
                nombre.setEnabled(false);
                opcion_uno.setEnabled(false);
                opcion_dos.setEnabled(false);
                opcion_tres.setEnabled(false);
                opcion_cuatro.setEnabled(false);

                textPuntaje.setVisibility(View.VISIBLE);
                textDificultad.setVisibility(View.VISIBLE);
                textTiempo.setVisibility(View.VISIBLE);

                puntaje.setVisibility(View.GONE);
                dificultad.setVisibility(View.GONE);
                tiempo.setVisibility(View.GONE);

                // Hide Delete/Edit Button.
                editar.setVisibility(View.VISIBLE);
                eliminar.setVisibility(View.VISIBLE);
                guardar.setVisibility(View.GONE);
                cancelar.setVisibility(View.GONE);

            }
        });



        eliminar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0){


                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Confirmar");
                builder.setMessage("¿Está seguro que desea eliminar el tipo de actividad?.");

                builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialogo, int which) {
                        dialogo.dismiss();

                        ParseObject delTipoActividad = ParseObject.createWithoutData("TipoActividad", getArguments().getString("id"));
                        // Disable tipoActividad
                        delTipoActividad.put("activa", false);
                        delTipoActividad.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    Toast.makeText(getActivity(), "Tipo de actividad eliminada correctamente.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getActivity(), "Ocurrió un error eliminando la actividad."+e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                        Fragment fragment = new FragmentListarTipoActividad();
                        getFragmentManager()
                                .beginTransaction()
                                .replace(R.id.content_frame, fragment)
                                .commit();

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
    public void ObtenerSpinnerdesdePosition(Spinner spin,String opcion){


        for (int i=0;i<spin.getCount();i++)
        {
           if( spin.getItemAtPosition(i).toString().trim().equals(opcion.trim()))
            {
                spin.setSelection(i);
            }
        }
    }
}
