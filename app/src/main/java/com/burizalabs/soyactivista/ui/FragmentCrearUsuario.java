package com.burizalabs.soyactivista.ui;

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
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import java.util.HashMap;
import java.util.Map;

import com.burizalabs.soyactivista.utils.ErrorCodeHelpers;
import com.burizalabs.soyactivista.utils.TextHelpers;
import com.burizalabs.soyactivista.R;

/**
 * Created by Brahyam on 2/12/2015.
 */
public class FragmentCrearUsuario extends Fragment {

    // Variable Declaration
    private String TAG = "FragmentCrearUsuario"; // For Log.d
    private EditText editUsername, editNombre, editApellido, editEmail, editCargo;
    private Spinner spinEstado, spinMunicipio, spinParroquia, spinComite, spinRol;
    private Button buttonRegistrar;
    private ProgressDialog progressDialog;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        //Choose fragment to inflate
        View v = inflater.inflate(R.layout.fragment_crear_usuario, container, false);

        // Setting Holders
        editUsername = (EditText)v.findViewById(R.id.editUsername);
        editNombre = (EditText)v.findViewById(R.id.editNombre);
        editApellido = (EditText)v.findViewById(R.id.editApellido);
        editEmail = (EditText)v.findViewById(R.id.editEmail);
        editCargo = (EditText)v.findViewById(R.id.editCargo);


        spinEstado = (Spinner)v.findViewById(R.id.spinEstado);
        spinMunicipio = (Spinner)v.findViewById(R.id.spinMunicipio);
        spinComite = (Spinner)v.findViewById(R.id.spinComite);
        spinRol = (Spinner)v.findViewById(R.id.spinRol);
        spinParroquia = (Spinner)v.findViewById(R.id.spinParroquia);

        buttonRegistrar = (Button)v.findViewById(R.id.buttonRegistrar);

        // Load Spinners
        fillSpinnerfromResource(spinEstado,R.array.Estados);
        fillSpinnerfromResource(spinComite, R.array.comites);
        fillSpinnerfromResource(spinRol, R.array.Roles);

        // Fill Municipios on Estado Selected
        spinEstado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                spinMunicipio.setAdapter(null);
                String nombreEstado = TextHelpers.NormalizeResource(spinEstado.getSelectedItem().toString());
                int arrayId = getResources().getIdentifier(nombreEstado, "array", getActivity().getPackageName());
                fillSpinnerfromResource(spinMunicipio,arrayId);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        spinMunicipio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                spinParroquia.setAdapter(null);
                String nombreEstado = TextHelpers.NormalizeResource(spinEstado.getSelectedItem().toString());
                String nombreMunicipio = TextHelpers.NormalizeResource(spinMunicipio.getSelectedItem().toString());
                Log.d(TAG,"Looking for resource:"+nombreEstado+"_"+nombreMunicipio);
                int arrayId = getResources().getIdentifier(nombreEstado+"_"+nombreMunicipio, "array", getActivity().getPackageName());
                fillSpinnerfromResource(spinParroquia,arrayId);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        buttonRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                // Validate fields are not empty
                if (editUsername.getText().toString().trim().length() > 0
                        && editNombre.getText().toString().trim().length() > 0
                        && editApellido.getText().toString().trim().length() > 0
                        && editCargo.getText().toString().trim().length() > 0
                        && editEmail.getText().toString().trim().length() > 0
                        ) {

                    // Validate email address matches pattern
                    if (editEmail.getText().toString().matches(emailPattern)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Confirmar");
                        builder.setMessage("¿Está seguro de que desea registrar a "+editNombre.getText()+" "+editApellido.getText()+"?");

                        builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Start Loading Dialog

                                progressDialog = ProgressDialog.show(getActivity(), "Crear Usuario", "Enviando Datos...", true);

                                // Load values in hash map to send to parse
                                final HashMap<String, Object> params = new HashMap<>();
                                params.put("username", editUsername.getText().toString());
                                params.put("nombre", editNombre.getText().toString());
                                params.put("apellido", editApellido.getText().toString());
                                params.put("correo", editEmail.getText().toString());
                                params.put("cargo", editCargo.getText().toString());
                                params.put("estado", spinEstado.getSelectedItem().toString());
                                params.put("municipio", spinMunicipio.getSelectedItem().toString());
                                params.put("parroquia", spinParroquia.getSelectedItem().toString());
                                params.put("comite", spinComite.getSelectedItem().toString());
                                params.put("rol", String.valueOf(spinRol.getSelectedItemPosition()));
                                ParseCloud.callFunctionInBackground("createUser", params, new FunctionCallback<Map<String, Object>>() {
                                    @Override
                                    public void done(Map<String, Object> response, ParseException e) {
                                        if (response != null && response.get("status").toString().equals("OK")) {
                                            progressDialog.dismiss();
                                            Log.d(TAG, "Usuario Registrado");
                                            if (response.get("userId") != null) {

                                                Log.d(TAG, "Response ID is " + response.get("userId").toString());
                                                // Send Registration Mail in Background
                                                HashMap<String, Object> params2 = new HashMap<>();
                                                params2.put("email", editEmail.getText().toString());
                                                params2.put("username", editUsername.getText().toString());
                                                params2.put("nombre", editNombre.getText().toString());
                                                params2.put("apellido", editApellido.getText().toString());
                                                params2.put("cargo", editCargo.getText().toString());
                                                params2.put("token", response.get("userId").toString());
                                                ParseCloud.callFunctionInBackground("SendRegistrationMail", params2, new FunctionCallback<Map<String, Object>>() {
                                                    @Override
                                                    public void done(Map<String, Object> response, ParseException e) {
                                                        if (e == null && Integer.valueOf(response.get("code").toString()) == 0) {
                                                            Toast.makeText(getActivity(), "Email enviado correctamente", Toast.LENGTH_SHORT).show();
                                                            Log.d(TAG, "Email Enviado");
                                                        } else {
                                                            Toast.makeText(getActivity(), ErrorCodeHelpers.resolveErrorCode(Integer.valueOf(response.get("code").toString())), Toast.LENGTH_SHORT).show();
                                                            Log.d(TAG, "Error enviando mail. " + e.getMessage() + " " + response.get("message").toString());
                                                        }

                                                    }
                                                });
                                            }

                                            Toast.makeText(getActivity(), "Usuario creado correctamente.", Toast.LENGTH_SHORT).show();
                                            Fragment fragment = new FragmentListarUsuario();
                                            getFragmentManager()
                                                    .beginTransaction()
                                                    .replace(R.id.content_frame, fragment)
                                                    .commit();

                                        } else {
                                            // TODO: Discern error types by examining error code.
                                            progressDialog.dismiss();
                                            if (e != null) {
                                                Log.d(TAG, "Error: " + e.getMessage());
                                                Toast.makeText(getActivity(), ErrorCodeHelpers.resolveErrorCode(e.getCode()), Toast.LENGTH_LONG).show();
                                            }

                                            if (response != null) {
                                                Log.d(TAG, "Error: " + response.get("code").toString() + " " + response.get("message").toString());
                                                Toast.makeText(getActivity(), ErrorCodeHelpers.resolveErrorCode(Integer.valueOf(response.get("code").toString())), Toast.LENGTH_LONG).show();
                                            }

                                            if (e == null && response == null) {
                                                Log.d(TAG, "Error: unknown error");
                                                Toast.makeText(getActivity(), "Error, por favor intenta de nuevo mas tarde.", Toast.LENGTH_LONG).show();
                                            }


                                        }
                                    }
                                });
                                dialog.dismiss();
                            }
                        });

                        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Do nothing
                                dialog.dismiss();
                            }
                        });

                        // After Dialog is Completely defined - Show Dialog.
                        AlertDialog alert = builder.create();
                        alert.show();

                    } else {
                        Toast.makeText(getContext(), "Correo inválido.", Toast.LENGTH_LONG).show();
                        return;
                    }
                } else {
                    Toast.makeText(getContext(), "No puede haber campos vacíos.", Toast.LENGTH_LONG).show();
                    return;
                }

            }
        });


        return v;
    }

    // Fills spinners setting adapter from a String array.
    public void fillSpinnerfromResource(Spinner spin,int id){
        ArrayAdapter spinner_adapter = ArrayAdapter.createFromResource(getActivity(), id, android.R.layout.simple_spinner_item);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(spinner_adapter);
    }
}
