package com.example.usuario.soyactivista.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

import logica.ActivityPantallaInicio;
import logica.ActivityPantallaMenu;
import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by Brahyam on 11/12/2015.
 */
public class FragmentRecuperarContrasena extends Fragment {

    private static final String TAG = "FRecuperarContraseña"; // For Debug purposes
    private EditText editEmail;
    private Button buttonEnviar, buttonRegresar;
    private ProgressDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Choose fragment to inflate
        View v = inflater.inflate(R.layout.fragment_recuperar_contrasena, container, false);

        // Assign to holders
        editEmail = (EditText) v.findViewById(R.id.editEmail);

        buttonEnviar = (Button) v.findViewById(R.id.buttonEnviar);
        buttonRegresar = (Button) v.findViewById(R.id.buttonRegresar);

        // SEND BUTTON BEHAVIOR
        buttonEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                //Validate Field not empty
                if(editEmail.getText().toString().trim().length() > 0)
                {
                    // Validate Email Pattern
                    if (editEmail.getText().toString().matches(emailPattern)){
                        dialog = ProgressDialog.show(getContext(),"Recuperando Contraseña","Enviando Datos...",true);
                        ParseUser.requestPasswordResetInBackground(editEmail.toString().trim(), new RequestPasswordResetCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    dialog.dismiss();
                                    Toast.makeText(getActivity(), "Se envió a su email un link para reestablecer la contraseña.", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(getContext(), ActivityPantallaMenu.class);
                                    startActivity(i);
                                } else {
                                    dialog.dismiss();
                                    // Email not found
                                    Log.d(TAG,"Exception returned with code "+e.getCode());
                                    if(e.getCode() == 125)
                                        Toast.makeText(getActivity(), "Su email no se encuentra registrado.", Toast.LENGTH_LONG).show();
                                    else
                                        Toast.makeText(getActivity(), "Ocurrio un error al enviar correo."+e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                    }
                    else{
                        Toast.makeText(getContext(), "El email no es válido.", Toast.LENGTH_LONG).show();
                    }


                }
                else{
                    Toast.makeText(getContext(), "Por favor introduzca un Email.", Toast.LENGTH_LONG).show();
                }

            }
        });

        // BACK BUTTON BEHAVIOR
        buttonRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), ActivityPantallaInicio.class);
                startActivity(i);
            }
        });

        return v;
    }
}
