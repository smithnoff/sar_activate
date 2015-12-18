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

import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import logica.ActivityPantallaInicio;
import logica.ActivityPantallaMenu;
import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by Brahyam on 11/12/2015.
 */
public class FragmentRecuperarIdentificador extends Fragment {

    private static final String TAG = "FRecuperarIdentificador"; // For Debug purposes
    private EditText editEmail;
    private Button buttonEnviar, buttonRegresar;
    private ProgressDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Choose fragment to inflate
        View v = inflater.inflate(R.layout.fragment_recuperar, container, false);

        // Assign to holders
        editEmail = (EditText) v.findViewById(R.id.editEmail);

        buttonEnviar = (Button) v.findViewById(R.id.buttonEnviar);
        buttonRegresar = (Button) v.findViewById(R.id.buttonRegresar);

        // SEND BUTTON BEHAVIOR
        buttonEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                final String email = editEmail.getText().toString().trim();

                //Validate Field not empty
                if(email.length() > 0)
                {
                    // Validate Email Pattern
                    if (email.matches(emailPattern)){
                        dialog = ProgressDialog.show(getContext(),"Recuperando Identificador","Enviando Datos...",true);
                        final HashMap<String, Object> params = new HashMap<>();
                        params.put("email",email);
                        ParseCloud.callFunctionInBackground("recoverUsername", params, new FunctionCallback< Map<String, Object> >() {
                            @Override
                            public void done(Map<String, Object> response, ParseException e) {
                                dialog.dismiss();
                                Log.d(TAG,"response value is "+response);
                                if (response != null && Integer.valueOf(response.get("code").toString()) == 0) {
                                    Toast.makeText(getActivity(), "Se ha enviado un email a su dirección con los datos solicitados..", Toast.LENGTH_SHORT).show();

                                    // Redirect to Login
                                    Intent i = new Intent(getActivity(), ActivityPantallaInicio.class);
                                    startActivity(i);

                                } else {
                                    Toast.makeText(getActivity(), "Ocurrió un error, por favor intente más tarde.", Toast.LENGTH_LONG).show();
                                    if(response != null)
                                        Toast.makeText(getActivity(), response.get("message").toString(), Toast.LENGTH_LONG).show();
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
