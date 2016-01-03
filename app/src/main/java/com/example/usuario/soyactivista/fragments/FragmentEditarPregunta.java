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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import logica.ErrorCodeHelper;
import soy_activista.quartzapp.com.soy_activista.R;

import static java.lang.Integer.parseInt;

/**
 * Created by darwin on 29/12/2015.
 */
public class FragmentEditarPregunta extends Fragment {

    private static final String TAG = "FragEditarPregunta";
    private EditText editPregunta, editRespuesta1, editRespuesta2, editRespuesta3, editRespuesta4; // Edit Field holders
    private Spinner spinPuntaje, spinDificultad, spinTiempo, spinCorrecta; // Spinner holders
    private Button buttonEditar, buttonEliminar, buttonGuardar, buttonCancelar; // Button holders
    private TextView valuePuntaje, valueDificultad, valueTiempo, valueCorrecta;
    private ProgressDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        //Choose fragment to inflate
        View v = inflater.inflate(R.layout.fragment_editar_pregunta, container, false);

        //Asign Text Edit and text view to holders
        editPregunta =   (EditText)v.findViewById(R.id.editPregunta);
        editRespuesta1 =   (EditText)v.findViewById(R.id.opcionUno);
        editRespuesta2 =   (EditText)v.findViewById(R.id.opcionDos);
        editRespuesta3 =   (EditText)v.findViewById(R.id.opcionTres);
        editRespuesta4 =   (EditText)v.findViewById(R.id.opcionCuatro);

        valuePuntaje =   (TextView)v.findViewById(R.id.valuePuntaje);
        valueDificultad =   (TextView)v.findViewById(R.id.valueDificultad);
        valueTiempo =   (TextView)v.findViewById(R.id.valueTiempo);
        valueCorrecta =   (TextView)v.findViewById(R.id.valueCorrecta);

        // Asigns Spinners to holders
        spinPuntaje = (Spinner)v.findViewById(R.id.spinnerPuntaje);
        spinDificultad = (Spinner)v.findViewById(R.id.spinnerDificultad);
        spinTiempo = (Spinner)v.findViewById(R.id.spinnerTiempoRespuesta);
        spinCorrecta = (Spinner)v.findViewById(R.id.spinnerOpcionCorrecta);

        // Asign Buttons to holders
        buttonEditar = (Button)v.findViewById(R.id.buttonEditarPregunta);
        buttonEliminar = (Button)v.findViewById(R.id.buttonEliminarPregunta);
        buttonGuardar = (Button)v.findViewById(R.id.buttonGuardarPreguntas);
        buttonCancelar = (Button)v.findViewById(R.id.buttonCancelar);

        //Fill Spinners with Preset Options
        llenarSpinnerdesdeId(spinPuntaje, R.array.Puntuaciones);
        llenarSpinnerdesdeId(spinDificultad, R.array.Dificultad);
        llenarSpinnerdesdeId(spinTiempo, R.array.Tiempo);
        llenarSpinnerdesdeId(spinCorrecta, R.array.Opciones);

        //Set Values from bundle
        editPregunta.setText(getArguments().getString("pregunta"));
        editRespuesta1.setText(getArguments().getString("opcion1"));
        editRespuesta2.setText(getArguments().getString("opcion2"));
        editRespuesta3.setText(getArguments().getString("opcion3"));
        editRespuesta4.setText(getArguments().getString("opcion4"));

        valueDificultad.setText(getArguments().getString("dificultad"));
        valueTiempo.setText(getArguments().getString("tiempo"));
        valuePuntaje.setText(getArguments().getString("puntaje"));
        valueCorrecta.setText(getArguments().getString("correcta"));

        ObtenerSpinnerdesdePosition(spinPuntaje, getArguments().getString("puntaje"));
        ObtenerSpinnerdesdePosition(spinDificultad, getArguments().getString("dificultad"));
        ObtenerSpinnerdesdePosition(spinTiempo, getArguments().getString("tiempo"));
        ObtenerSpinnerdesdePosition(spinCorrecta, getArguments().getString("correcta"));

        // Buttons Behavior
        buttonEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                // Turn fields editable
                editPregunta.setEnabled(true);
                editRespuesta1.setEnabled(true);
                editRespuesta2.setEnabled(true);
                editRespuesta3.setEnabled(true);
                editRespuesta4.setEnabled(true);

                valuePuntaje.setVisibility(View.GONE);
                valueDificultad.setVisibility(View.GONE);
                valueTiempo.setVisibility(View.GONE);
                valueCorrecta.setVisibility(View.GONE);

                spinPuntaje.setVisibility(View.VISIBLE);
                spinDificultad.setVisibility(View.VISIBLE);
                spinTiempo.setVisibility(View.VISIBLE);
                spinCorrecta.setVisibility(View.VISIBLE);

                // Hide Delete/Edit Button.
                buttonEditar.setVisibility(View.GONE);
                buttonEliminar.setVisibility(View.GONE);
                buttonGuardar.setVisibility(View.VISIBLE);
                buttonCancelar.setVisibility(View.VISIBLE);
            }
        });

        // Saving method
        buttonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                if ( editPregunta.getText().toString().trim().length() == 0 ||
                        editRespuesta1.getText().toString().trim().length() == 0 ||
                        editRespuesta2.getText().toString().trim().length() == 0 ||
                        editRespuesta3.getText().toString().trim().length() == 0 ||
                        editRespuesta4.getText().toString().trim().length() == 0
                        ){
                    Toast.makeText(getContext(), "No pueden haber campos vacíos.", Toast.LENGTH_LONG).show();
                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Confirmar");
                    builder.setMessage("¿Está seguro que desea guardar la pregunta?");

                    builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialogo, int which) {

                            dialog = ProgressDialog.show(getActivity(), "Editando Pregunta", "Enviando datos...", true);

                            ParseQuery<ParseObject> query = ParseQuery.getQuery("Pregunta");
                            // Retrieve the object by id from parse
                            query.getInBackground(getArguments().getString("id"), new GetCallback<ParseObject>() {
                                public void done(ParseObject pregunta, ParseException e) {
                                    if (e == null) {
                                        pregunta.put("dificultad", spinDificultad.getSelectedItem().toString());
                                        pregunta.put("pregunta", editPregunta.getText().toString());
                                        pregunta.put("opcion1", editRespuesta1.getText().toString());
                                        pregunta.put("opcion2", editRespuesta2.getText().toString());
                                        pregunta.put("opcion3", editRespuesta3.getText().toString());
                                        pregunta.put("opcion4", editRespuesta4.getText().toString());
                                        pregunta.put("tiempo", Integer.parseInt(spinTiempo.getSelectedItem().toString()));
                                        pregunta.put("puntaje", Integer.parseInt(spinPuntaje.getSelectedItem().toString()));
                                        pregunta.put("correcta", Integer.parseInt(spinCorrecta.getSelectedItem().toString()));
                                        pregunta.saveInBackground(new SaveCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                if (e == null) {
                                                    dialog.dismiss();
                                                    Toast.makeText(getActivity(), "Pregunta guardada correctamente.", Toast.LENGTH_SHORT).show();
                                                    Fragment fragment = new FragmentListarPregunta();
                                                    getFragmentManager()
                                                            .beginTransaction()
                                                            .replace(R.id.content_frame, fragment)
                                                            .commit();
                                                } else {
                                                    dialog.dismiss();
                                                    Log.d(TAG,"Error: "+e.getMessage()+" "+e.getCode());
                                                    Toast.makeText(getActivity(), ErrorCodeHelper.resolveErrorCode(e.getCode()), Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });

                                    } else {
                                        // Object not found in Parse or else
                                        Log.d(TAG, "Error: " + e.getMessage() + " " + e.getCode());
                                        Toast.makeText(getActivity(), ErrorCodeHelper.resolveErrorCode(e.getCode()), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }

                    });

                    builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialogo, int which) {
                            dialogo.dismiss();
                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();

                }

            }
        });


        buttonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Turn fields not editable
                editPregunta.setEnabled(false);
                editRespuesta1.setEnabled(false);
                editRespuesta2.setEnabled(false);
                editRespuesta3.setEnabled(false);
                editRespuesta4.setEnabled(false);

                valuePuntaje.setVisibility(View.VISIBLE);
                valueDificultad.setVisibility(View.VISIBLE);
                valueTiempo.setVisibility(View.VISIBLE);
                valueCorrecta.setVisibility(View.VISIBLE);

                spinPuntaje.setVisibility(View.GONE);
                spinDificultad.setVisibility(View.GONE);
                spinTiempo.setVisibility(View.GONE);
                spinCorrecta.setVisibility(View.GONE);

                // Hide Delete/Edit Button.
                buttonEditar.setVisibility(View.VISIBLE);
                buttonEliminar.setVisibility(View.VISIBLE);
                buttonGuardar.setVisibility(View.GONE);
                buttonCancelar.setVisibility(View.GONE);

                // Load Original Values Again in case they've changed
                editPregunta.setText(getArguments().getString("pregunta"));
                editRespuesta1.setText(getArguments().getString("opcion1"));
                editRespuesta2.setText(getArguments().getString("opcion2"));
                editRespuesta3.setText(getArguments().getString("opcion3"));
                editRespuesta4.setText(getArguments().getString("opcion4"));

                valueDificultad.setText(getArguments().getString("dificultad"));
                valueTiempo.setText(getArguments().getString("tiempo"));
                valuePuntaje.setText(getArguments().getString("puntaje"));
                valueCorrecta.setText(getArguments().getString("correcta"));
            }
        });



        buttonEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {


                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Confirmar");
                builder.setMessage("¿Está seguro que desea eliminar la pregunta?.");

                builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialogo, int which) {
                        dialogo.dismiss();
                        dialog = ProgressDialog.show(getActivity(), "Eliminando Pregunta", "Enviando datos...", true);
                        ParseObject pregunta = ParseObject.createWithoutData("Pregunta", getArguments().getString("id"));

                        // TODO: regenerate questions indexes

                        pregunta.deleteInBackground(new DeleteCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    dialog.dismiss();
                                    Toast.makeText(getActivity(), "Pregunta eliminada correctamente.", Toast.LENGTH_SHORT).show();
                                    // Redirect to list
                                    Fragment fragment = new FragmentListarPregunta();
                                    getFragmentManager()
                                            .beginTransaction()
                                            .replace(R.id.content_frame, fragment)
                                            .commit();

                                } else {
                                    dialog.dismiss();
                                    // Object not found in Parse or else
                                    Log.d(TAG, "Error: " + e.getMessage() + " " + e.getCode());
                                    Toast.makeText(getActivity(), ErrorCodeHelper.resolveErrorCode(e.getCode()), Toast.LENGTH_LONG).show();
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
