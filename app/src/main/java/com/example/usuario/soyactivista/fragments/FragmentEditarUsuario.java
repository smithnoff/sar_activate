package com.example.usuario.soyactivista.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.HashMap;
import java.util.Map;

import logica.ActivityPantallaInicio;
import logica.ErrorCodeHelper;
import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by Brahyam on 2/12/2015.
 */
public class FragmentEditarUsuario extends Fragment {

    // Variable Initialization
    String TAG = "FragmentEditarUsuario"; // For Log.d

    private String userID;
    private EditText editUsername, editNombre, editApellido, editEmail, editCargo, editParroquia;
    private TextView valueEstado, valueMunicipio, valueComite, valueRol;
    private Spinner spinEstado, spinMunicipio, spinComite, spinRol;
    private Button buttonEditar, buttonGuardar, buttonEliminar;
    private ProgressDialog progressDialog;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        //Choose fragment to inflate
        View v = inflater.inflate(R.layout.fragment_editar_usuario, container, false);

        // Setting Holders
        editUsername = (EditText) v.findViewById(R.id.editUsername);
        editNombre = (EditText) v.findViewById(R.id.editNombre);
        editApellido = (EditText) v.findViewById(R.id.editApellido);
        editEmail = (EditText) v.findViewById(R.id.editEmail);
        editCargo = (EditText) v.findViewById(R.id.editCargo);
        editParroquia = (EditText) v.findViewById(R.id.editParroquia);

        valueEstado = (TextView) v.findViewById(R.id.valueEstado);
        valueMunicipio = (TextView) v.findViewById(R.id.valueMunicipio);
        valueComite = (TextView) v.findViewById(R.id.valueComite);
        valueRol = (TextView) v.findViewById(R.id.valueRol);

        spinEstado = (Spinner) v.findViewById(R.id.spinEstado);
        spinMunicipio = (Spinner) v.findViewById(R.id.spinMunicipio);
        spinComite = (Spinner) v.findViewById(R.id.spinComite);
        spinRol = (Spinner) v.findViewById(R.id.spinRol);

        buttonEditar = (Button) v.findViewById(R.id.buttonEditar);
        buttonGuardar = (Button) v.findViewById(R.id.buttonGuardar);
        buttonEliminar = (Button) v.findViewById(R.id.buttonEliminar);


        // Fill Current Values (From Arguments or Current User
        final ParseUser currentUser = ParseUser.getCurrentUser();


        // Show edit button depending on Role
        if( currentUser.getInt("rol") == 1){
            buttonEditar.setVisibility(View.VISIBLE);
        }

        // Load Spinners
        fillSpinnerfromResource(spinEstado, R.array.Estados);
        fillSpinnerfromResource(spinComite, R.array.Comite);
        fillSpinnerfromResource(spinRol, R.array.Roles);

        // Fill Municipios on Estado Selected
        spinEstado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinMunicipio.setAdapter(null);
                fillSpinnerfromResource(spinMunicipio, getResources().getIdentifier(spinEstado.getSelectedItem().toString().replace(' ', '_'), "array", getActivity().getPackageName()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Fill from Arguments if not empty
        if(getArguments() != null){
            userID = getArguments().getString("id");
            int rolparse = getArguments().getInt("rol");

            if(rolparse == 0){
                valueRol.setText("Activista");
            }
            else{
                valueRol.setText("Registrante");
            }
            //valueRol.setText(getArguments().getInt("rol"));
            editUsername.setText(getArguments().getString("username"));
            editNombre.setText(getArguments().getString("nombre"));
            editApellido.setText(getArguments().getString("apellido"));
            editEmail.setText(getArguments().getString("email"));
            editCargo.setText(getArguments().getString("cargo"));
            valueEstado.setText(getArguments().getString("estado"));
            valueMunicipio.setText(getArguments().getString("municipio"));
            valueComite.setText(getArguments().getString("comite"));


        }
        // Fill From current user
        else{

            userID = currentUser.getObjectId();
            //valueRol.setText(currentUser.getInt("rol"));
            int rolparse = currentUser.getInt("rol");
            if(rolparse==0)
            {
                valueRol.setText("Activista");
            }
            else
            {
                valueRol.setText("Registrante");
            }
            editUsername.setText(currentUser.getUsername());
            editNombre.setText(currentUser.getString("nombre"));
            editApellido.setText(currentUser.getString("apellido"));
            editEmail.setText(currentUser.getString("email"));
            editCargo.setText(currentUser.getString("cargo"));
            valueEstado.setText(currentUser.getString("estado"));
            valueMunicipio.setText(currentUser.getString("municipio"));
            valueComite.setText(currentUser.getString("comite"));


        }

        // Handle Edit Button
        buttonEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonEditar.setVisibility(View.GONE);
                buttonGuardar.setVisibility(View.VISIBLE);

                // Enable all edit Text
                //editUsername.setEnabled(true);
                editNombre.setEnabled(true);
                editApellido.setEnabled(true);
                editEmail.setEnabled(true);
                editCargo.setEnabled(true);
                editParroquia.setEnabled(true);

                // Hide All Values
                valueEstado.setVisibility(View.GONE);
                valueMunicipio.setVisibility(View.GONE);
                valueComite.setVisibility(View.GONE);
                valueRol.setVisibility(View.GONE);

                // Show all Spinners
                spinEstado.setVisibility(View.VISIBLE);
                spinMunicipio.setVisibility(View.VISIBLE);
                spinComite.setVisibility(View.VISIBLE);
                spinRol.setVisibility(View.VISIBLE);
            }
        });

        // Handle Save Button

        buttonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                // Validate if fieldss are not empty
                if (    editNombre.getText().toString().trim().length() > 0
                        && editApellido.getText().toString().trim().length() > 0
                        && editUsername.getText().toString().trim().length() > 0
                        && editEmail.getText().toString().trim().length() > 0
                        && editCargo.getText().toString().trim().length() > 0
                        && editParroquia.getText().toString().trim().length() > 0) {

                    // Validate Email matches pattern
                    if (editEmail.getText().toString().matches(emailPattern)) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Confirmar");
                        builder.setMessage("¿Está seguro de que desea guardar los cambios?");

                        builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                progressDialog = ProgressDialog.show(getActivity(), "", "Guardando Usuario.", true);

                                if (getArguments() != null) {

                                    Log.d(getClass().getName(), "Modifying from Arguments");
                                    final HashMap<String, Object> params = new HashMap<>();
                                    //params.put("username", editUsername.getText().toString());
                                    params.put("nombre", editNombre.getText().toString());
                                    params.put("apellido", editApellido.getText().toString());
                                    params.put("correo", editEmail.getText().toString());
                                    params.put("cargo", editCargo.getText().toString());
                                    params.put("estado", spinEstado.getSelectedItem().toString());
                                    params.put("municipio", spinMunicipio.getSelectedItem().toString());
                                    params.put("parroquia", editParroquia.getText().toString());
                                    params.put("comite", spinComite.getSelectedItem().toString());
                                    params.put("rol", String.valueOf(spinRol.getSelectedItemPosition()));
                                    ParseCloud.callFunctionInBackground("modifyUser", params, new FunctionCallback<Map<String, Object>>() {
                                        @Override
                                        public void done(Map<String, Object> response, ParseException e) {
                                            if (response != null && response.get("status").toString() == "OK") {
                                                progressDialog.dismiss();
                                                Toast.makeText(getActivity(), "Usuario editado correctamente.", Toast.LENGTH_SHORT).show();

                                                // Redirect to User List
                                                Fragment fragment = new FragmentListarUsuario();
                                                getFragmentManager()
                                                        .beginTransaction()
                                                        .replace(R.id.content_frame, fragment)
                                                        .commit();
                                            } else {
                                                progressDialog.dismiss();
                                                if(e != null){
                                                    Log.d(TAG, "Error: " + e.getMessage());
                                                    Toast.makeText(getActivity(), ErrorCodeHelper.resolveErrorCode(e.getCode()), Toast.LENGTH_LONG).show();
                                                }

                                                if(response != null){
                                                    Log.d(TAG, "Error: " +response.get("code").toString()+" "+ response.get("message").toString());
                                                    Toast.makeText(getActivity(), ErrorCodeHelper.resolveErrorCode(Integer.valueOf(response.get("code").toString())), Toast.LENGTH_LONG).show();
                                                }

                                                if(e == null && response == null){
                                                    Log.d(TAG, "Error: unknown error");
                                                    Toast.makeText(getActivity(), "Error, por favor intenta de nuevo mas tarde.", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        }
                                    });
                                } else {

                                    if (currentUser != null) {
                                        Log.d(getClass().getName(), "Filling from Current User");
                                        //currentUser.setUsername(editUsername.getText().toString());
                                        currentUser.put("nombre", editNombre.getText().toString());
                                        currentUser.put("apellido", editApellido.getText().toString());
                                        currentUser.setEmail(editEmail.getText().toString());
                                        currentUser.put("cargo", editCargo.getText().toString());
                                        currentUser.put("estado", spinEstado.getSelectedItem().toString());
                                        currentUser.put("municipio", spinMunicipio.getSelectedItem().toString());
                                        currentUser.put("parroquia", editParroquia.getText().toString());
                                        currentUser.put("comite", spinComite.getSelectedItem().toString());
                                        currentUser.put("rol", spinRol.getSelectedItemPosition());
                                        currentUser.saveInBackground(new SaveCallback() {
                                            @Override
                                            public void done(ParseException e) {

                                                if(e == null){
                                                    Toast.makeText(getActivity(),"Usuario guardado correctamente.", Toast.LENGTH_SHORT).show();
                                                    progressDialog.dismiss();
                                                    // Redirect to Dashboard
                                                    Fragment fragment = new FragmentListarMensaje();
                                                    getFragmentManager()
                                                            .beginTransaction()
                                                            .replace(R.id.content_frame, fragment)
                                                            .commit();
                                                }
                                                else{
                                                    progressDialog.dismiss();
                                                    Toast.makeText(getActivity(), ErrorCodeHelper.resolveErrorCode(e.getCode()), Toast.LENGTH_LONG).show();
                                                }

                                            }
                                        });
                                    }
                                }
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

                    }
                    else
                    {
                        Toast.makeText(getContext(), "Correo Inválido.", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                else
                {
                    Toast.makeText(getContext(),"No puede haber campos vacíos.",Toast.LENGTH_LONG).show();
                    return;
                }
            }
        });

        buttonEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Eliminar Button Clicked");
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Confirmar");
                builder.setMessage("¿Está seguro de que desea eliminar al usuario?");

                builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        String deleteUsername;

                        if(getArguments()!=null)
                            deleteUsername = editUsername.getText().toString();
                        else
                            deleteUsername = currentUser.getUsername();

                        final HashMap<String, Object> params = new HashMap<>();
                        params.put("username", deleteUsername);
                        ParseCloud.callFunctionInBackground("deleteUser", params, new FunctionCallback<Map<String, Object>>() {
                            @Override
                            public void done(Map<String, Object> response, ParseException e) {
                                if (e == null && response != null && Integer.valueOf(response.get("code").toString()) == 0) {

                                    if(getArguments() != null){
                                        // Is another user account, redirect to user list
                                        Toast.makeText(getActivity(), "Usuario eliminado correctamente.", Toast.LENGTH_SHORT).show();
                                        Fragment fragment = new FragmentListarUsuario();
                                        getFragmentManager()
                                                .beginTransaction()
                                                .replace(R.id.content_frame, fragment)
                                                .addToBackStack(null)
                                                .commit();
                                    }
                                    else{
                                        currentUser.logOut();
                                        Toast.makeText(getActivity(), "Tu cuenta ha sido eliminada correctamente..", Toast.LENGTH_SHORT).show();
                                        // Redirect to init Screen
                                        Intent i = new Intent(getContext(), ActivityPantallaInicio.class);
                                        startActivity(i);
                                    }
                                }
                                else {
                                    // TODO: Discern error types by examining error code.
                                    if(e != null){
                                        Log.d(TAG, "Error: " + e.getMessage());
                                        Toast.makeText(getActivity(), ErrorCodeHelper.resolveErrorCode(e.getCode()), Toast.LENGTH_LONG).show();
                                    }

                                    if(response != null){
                                        Log.d(TAG, "Error: " +response.get("code").toString()+" "+ response.get("message").toString());
                                        Toast.makeText(getActivity(), ErrorCodeHelper.resolveErrorCode(Integer.valueOf(response.get("code").toString())), Toast.LENGTH_LONG).show();
                                    }

                                    if(e == null && response == null){
                                        Log.d(TAG, "Error: unknown error");
                                        Toast.makeText(getActivity(), "Error, por favor intenta de nuevo mas tarde.", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        });
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
