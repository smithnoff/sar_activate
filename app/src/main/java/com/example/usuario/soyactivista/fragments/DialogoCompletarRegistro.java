package com.example.usuario.soyactivista.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import logica.ActivityPantallaInicio;
import logica.ActivityPantallaMenu;
import soy_activista.quartzapp.com.soy_activista.R;

public class DialogoCompletarRegistro extends DialogFragment {

    private static final String TAG = "CompletarRegistro";

    // View Elements
    private TextView username, valueMensaje;
    private EditText editPassword, editRepeatPasword, editUsername, editToken;
    private Button buttonIngresar,buttonRegresar;

    // Dialogs needed for flow
    Dialog completarRegistro, nuevaContraseña;

    // Progress Dialog
    ProgressDialog progressDialog;

    private ProgressDialog dialog;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        // Initialize COmpletar Registro Dialog
        completarRegistro = new Dialog(getActivity(), R.style.Theme_Dialog_Translucent);
        completarRegistro.requestWindowFeature(Window.FEATURE_NO_TITLE);
        completarRegistro.setCancelable(false);
        completarRegistro.setContentView(R.layout.fragment_completar_registro); // assign View

        // Asociate Fields
        editUsername = (EditText) completarRegistro.findViewById(R.id.editUsername);
        editToken = (EditText) completarRegistro.findViewById(R.id.editToken);

        buttonIngresar = (Button) completarRegistro.findViewById(R.id.buttonIngresar);
        buttonRegresar = (Button) completarRegistro.findViewById(R.id.buttonRegresar);

        //TODO: declare buttons and properly instantiate clicklisteners.
        // Ingresar Button

        buttonIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = ProgressDialog.show(getActivity(), "", "Verificando Datos...", true);

                ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
                query.whereEqualTo("username", editUsername.getText().toString());
                query.whereEqualTo("objectId", editToken.getText().toString());
                query.getFirstInBackground(new GetCallback<ParseObject>() {
                    public void done(ParseObject object, ParseException e) {
                        if (object == null) {
                            dialog.dismiss();
                            Toast.makeText(getActivity(), "Datos Incorrectos", Toast.LENGTH_SHORT).show();
                        } else {
                            dialog.dismiss();
                            completarRegistro.dismiss();
                            // Open new Dialog for password
                            setNewPassword(editUsername.getText().toString());
                        }
                    }
                });
            }
        });

        buttonRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                completarRegistro.dismiss();
            }
        });

        return completarRegistro;
    }

    // Dialog that enables the user to set a new password.
    public void setNewPassword(final String identificador){

        nuevaContraseña = new Dialog(getActivity(), R.style.Theme_Dialog_Translucent);
        nuevaContraseña.requestWindowFeature(Window.FEATURE_NO_TITLE);
        nuevaContraseña.setCancelable(false);
        nuevaContraseña.setContentView(R.layout.fragment_completar_password);

        // Assign Holders
        username = (TextView) nuevaContraseña.findViewById(R.id.valueUsername);
        editPassword = (EditText) nuevaContraseña.findViewById(R.id.editPassword);
        editRepeatPasword = (EditText) nuevaContraseña.findViewById(R.id.editRepeatPassword);

        buttonIngresar = (Button) nuevaContraseña.findViewById(R.id.buttonIngresar);
        buttonRegresar = (Button) nuevaContraseña.findViewById(R.id.buttonRegresar);

        // Set Defaults
        username.setText(identificador);

        // Buttons Behavior
        buttonIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "Ingresar Clicked");

                try {
                    // Validate if Passwords are equal.
                    // TODO: Validate security measures for password (longitude, characters type)
                    if (editPassword.getText().toString().equals(editRepeatPasword.getText().toString())) {
                        // Logs user with default password to be able to change it.
                        ParseUser user = ParseUser.logIn(identificador, getResources().getString(R.string.tempPassword));
                        // Set new Password & Save
                        user.setPassword(editPassword.getText().toString());
                        // TODO: Save Callback catch
                        user.saveInBackground();

                        nuevaContraseña.dismiss();

                        // Assign new Content with Result Message to Dialog
                        nuevaContraseña = new Dialog(getActivity(), R.style.Theme_Dialog_Translucent);
                        nuevaContraseña.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        nuevaContraseña.setCancelable(false);
                        nuevaContraseña.setContentView(R.layout.mensajes);

                        valueMensaje = (TextView) nuevaContraseña.findViewById(R.id.valueMensaje);

                        valueMensaje.setText("Su cuenta ha sido registrada satisfactoriamente");

                        buttonIngresar = (Button) nuevaContraseña.findViewById(R.id.buttonIngresar);
                        buttonRegresar = (Button) nuevaContraseña.findViewById(R.id.buttonRegresar);

                        // Button Ingresar Behavior
                        buttonIngresar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Redirect to Dashboard
                                nuevaContraseña.dismiss();
                                Intent i = new Intent(getActivity().getApplicationContext(), ActivityPantallaMenu.class);
                                startActivity(i);
                            }
                        });

                        // Button Regresar Behavior
                        buttonRegresar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                nuevaContraseña.dismiss();
                                Intent i = new Intent(getActivity().getApplicationContext(), ActivityPantallaInicio.class);
                                startActivity(i);
                            }
                        });

                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "Las contraseñas no coinciden.", Toast.LENGTH_LONG).show();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });

        buttonRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nuevaContraseña.dismiss();
                Intent i = new Intent(getActivity().getApplicationContext(), ActivityPantallaInicio.class);
                startActivity(i);
            }
        });

    }
}
