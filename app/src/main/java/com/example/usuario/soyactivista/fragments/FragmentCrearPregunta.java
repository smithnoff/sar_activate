package com.example.usuario.soyactivista.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import logica.ErrorCodeHelper;
import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by RSMAPP on 28/12/2015.
 */
public class FragmentCrearPregunta extends Fragment {


    private static final String TAG = "FragCrearPregunta";
    private EditText editPregunta, editRespuesta1, editRespuesta2, editRespuesta3, editRespuesta4;
    private Spinner spinPuntaje, spinNivel, spinTiempo, spinCorrecta;
    private Button buttonCrearPregunta, buttonRegresar;
    private ProgressDialog dialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_crear_pregunta, container, false);

        //edit holders
        editPregunta =(EditText)v.findViewById(R.id.editPregunta);
        editRespuesta1 =(EditText)v.findViewById(R.id.opcionUno);
        editRespuesta2 =(EditText)v.findViewById(R.id.opcionDos);
        editRespuesta3 =(EditText)v.findViewById(R.id.opcionTres);
        editRespuesta4 =(EditText)v.findViewById(R.id.opcionCuatro);

        // spinner holders
        spinPuntaje =(Spinner)v.findViewById(R.id.spinnerPuntaje);
        spinNivel =(Spinner)v.findViewById(R.id.spinnerDificultad);
        spinTiempo =(Spinner)v.findViewById(R.id.spinnerTiempoRespuesta);
        spinCorrecta =(Spinner)v.findViewById(R.id.spinnerOpcionCorrecta);

        //button holders
        buttonCrearPregunta = (Button)v.findViewById(R.id.buttonCrearpregunta);
        buttonRegresar = (Button)v.findViewById(R.id.buttonRegresar);


        //fill spinners
        this.llenarSpinnerdesdeId(spinPuntaje,R.array.Puntuaciones);
        this.llenarSpinnerdesdeId(spinNivel,R.array.Dificultad);
        this.llenarSpinnerdesdeId(spinTiempo,R.array.Tiempo);
        this.llenarSpinnerdesdeId(spinCorrecta,R.array.Opciones);

        buttonCrearPregunta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Check for empty fields.
                if (editPregunta.getText().toString().trim().length() == 0 ||
                        editRespuesta1.getText().toString().trim().length() == 0 ||
                        editRespuesta2.getText().toString().trim().length() == 0 ||
                        editRespuesta3.getText().toString().trim().length() == 0 ||
                        editRespuesta4.getText().toString().trim().length() == 0) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Advertencia");
                    builder.setMessage("Uno o más campos están vacíos, verifique que todos los campos estén llenos y vuelva a intentarlo");
                    builder.setNeutralButton("Aceptar", null).create().show();
                } else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Confirmar");
                    builder.setMessage("¿Está seguro que desea crear la Pregunta?");

                    builder.setPositiveButton("Crear", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialogo, int which) {

                            dialog = ProgressDialog.show(getActivity(), "Creando Pregunta", "Enviando Datos...", true);

                            ParseObject pregunta = new ParseObject("Pregunta");
                            pregunta.put("pregunta", editPregunta.getText().toString());
                            pregunta.put("opcion1", editRespuesta1.getText().toString());
                            pregunta.put("opcion2", editRespuesta2.getText().toString());
                            pregunta.put("opcion3", editRespuesta3.getText().toString());
                            pregunta.put("opcion4", editRespuesta4.getText().toString());
                            // Parse response time option
                            Integer tiempo;
                            switch (spinTiempo.getSelectedItemPosition()){
                                case 0:
                                    tiempo = 30;
                                    break;
                                case 1:
                                    tiempo = 60;
                                    break;
                                case 2:
                                    tiempo = 90;
                                    break;
                                default:
                                    tiempo = 30;
                                    break;
                            }
                            pregunta.put("tiempo", tiempo);
                            pregunta.put("puntaje", Integer.parseInt(spinPuntaje.getSelectedItem().toString()));
                            pregunta.put("dificultad", spinNivel.getSelectedItem().toString());
                            pregunta.put("correcta", Integer.parseInt(spinCorrecta.getSelectedItem().toString()));
                            pregunta.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        dialog.dismiss();
                                        Toast.makeText(getActivity(), "Pregunta creada correctamente.", Toast.LENGTH_SHORT).show();
                                        //Redirect to list
                                        Fragment fragment = new FragmentListarPregunta();
                                        getFragmentManager()
                                                .beginTransaction()
                                                .replace(R.id.content_frame, fragment)
                                                .commit();
                                    } else {
                                        dialog.dismiss();
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
                            // Do nothing
                            dialogo.dismiss();

                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        });

        buttonRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // redirect to list
                Fragment fragment = new FragmentListarPregunta();
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

}
