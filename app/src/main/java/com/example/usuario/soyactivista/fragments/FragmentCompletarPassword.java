package com.example.usuario.soyactivista.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;

import logica.ActivityPantallaInicio;
import logica.ActivityPantallaMenu;
import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by Luis Adrian on 07/12/2015.
 */
public class FragmentCompletarPassword extends Fragment{
    private static final String TAG = "CompletarPassword";

    // View Elements
    private TextView username, valueMensaje;
    private EditText editPassword, editRepeatPasword, editUsername, editToken;
    private Button buttonIngresar,buttonRegresar;

    // Progress Dialog
    ProgressDialog progressDialog;

    private ProgressDialog dialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_completar_password, container, false);

        // Assign Holders
        username = (TextView) v.findViewById(R.id.valueUsername);
        editPassword = (EditText) v.findViewById(R.id.editPassword);
        editRepeatPasword = (EditText) v.findViewById(R.id.editRepeatPassword);

        buttonIngresar = (Button) v.findViewById(R.id.buttonIngresar);
        buttonRegresar = (Button) v.findViewById(R.id.buttonRegresar);

        // Set Defaults
        username.setText(getArguments().getString("username"));

        // Buttons Behavior
        buttonIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "Ingresar Clicked");

                // Validate if Passwords are equal.
                // TODO: Validate security measures for password (longitude, characters type)
                if (editPassword.getText().toString().equals(editRepeatPasword.getText().toString())) {

                    ParseUser currentUser = ParseUser.getCurrentUser();

                    if (currentUser != null) {
                        Log.d(TAG,"Checking if User is logged."+currentUser);
                        currentUser.logOut();
                    }
                    // Logs user with default password to be able to change it.
                    ParseUser.logInInBackground(getArguments().getString("username"), getResources().getString(R.string.tempPassword), new LogInCallback() {
                        public void done(ParseUser user, ParseException e) {
                            if (user != null) {
                                // Hooray! The user is logged in.
                                user.setPassword(editPassword.getText().toString());
                                user.saveInBackground();
                                // Let user know everything went ok
                                Toast.makeText(getActivity().getApplicationContext(), "Su cuenta ha sido registrada correctamente.", Toast.LENGTH_LONG).show();

                                // Redirect to main dashboard
                                Intent i = new Intent(getActivity().getApplicationContext(), ActivityPantallaInicio.class);
                                startActivity(i);

                            } else {
                                // Signup failed. Look at the ParseException to see what happened.
                                Toast.makeText(getActivity().getApplicationContext(), "Ya completó su registro previamente." + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Las contraseñas no coinciden.", Toast.LENGTH_LONG).show();
                }

            }
        });

        buttonRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity().getApplicationContext(), ActivityPantallaInicio.class);
                startActivity(i);
            }
        });


        return v;
    }
}
