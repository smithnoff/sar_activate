package com.burizalabs.soyactivista.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.burizalabs.soyactivista.R;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;

import com.burizalabs.soyactivista.utils.ErrorCodeHelpers;


public class ActivityLogin extends AppCompatActivity {
    private static final String TAG = "LoginAct";
    private EditText editUsername, editPassword;
    private Button buttonIngresar, buttonRegresar;
    private String usuario, contrasena;
    private ProgressDialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pantalla_login);

        //asignamos los controles a las variables
        editUsername = (EditText) findViewById (R.id.editUsername);
        editPassword = (EditText) findViewById (R.id.editEmail);

        buttonIngresar = (Button) findViewById(R.id.buttonIngresar);
        buttonRegresar = (Button) findViewById(R.id.buttonRegresar);

        buttonIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                {
                    // Validate Fields before posting
                    if (editUsername.getText().toString().trim().length() > 0 &&
                            editPassword.getText().toString().trim().length() > 0){


                        dialog = ProgressDialog.show(ActivityLogin.this, "Verificando usuario","Enviando datos...", true);
                        usuario = editUsername.getText().toString();
                        contrasena = editPassword.getText().toString();

                        ParseUser.logInInBackground(usuario, contrasena,
                                new LogInCallback() {
                                    @Override
                                    public void done(ParseUser user, ParseException e) {
                                        if(user != null){
                                            dialog.dismiss();
                                            ParseUser currentUser = ParseUser.getCurrentUser();

                                            // Check if user was deleted and redirect to Start.
                                            if( currentUser.getBoolean("eliminado")){
                                                Toast.makeText(getApplicationContext(), "Su cuenta ha sido eliminada. Pongase en contacto con un miembro registro." , Toast.LENGTH_LONG).show();
                                                Intent i = new Intent(getApplication(), ActivityPantallaInicio.class);
                                                startActivity(i);
                                            }

                                            Log.d(TAG,"User Logged In");
                                            // Associate user with Installation to receive push notifications
                                            ParseInstallation installation = ParseInstallation.getCurrentInstallation();
                                            installation.put("usuario",currentUser);
                                            installation.saveEventually();

                                            // Redirect to dash
                                            Intent i = new Intent(getApplication(), ActivityPantallaMenu.class);
                                            startActivity(i);
                                            finish();

                                        }else{
                                            dialog.dismiss();
                                            // TODO: discern between different exceptions and show appropiate message.
                                            Toast.makeText(getApplicationContext(), ErrorCodeHelpers.resolveErrorCode(e.getCode()) , Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });

                    }
                    else{
                        Toast.makeText(getBaseContext(),"No puede haber campos vacíos.",Toast.LENGTH_LONG).show();
                        return;
                    }

                }
            }
        });

        buttonRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplication(), ActivityPantallaInicio.class);
                startActivity(i);
            }
        });
    }

}
