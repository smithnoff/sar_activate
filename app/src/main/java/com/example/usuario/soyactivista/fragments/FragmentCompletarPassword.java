package com.example.usuario.soyactivista.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseUser;

import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by Luis Adrian on 07/12/2015.
 */
public class FragmentCompletarPassword extends Fragment{
    EditText t1,t2,t3,t4;
    String identificador,token, passw, otrapassw;
    TextView tv1;
    private ProgressDialog dialog,dia;
    private Button botonIngresar;
    private Button botonRegresar;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_completar_password, container, false);
        context = v.getContext();
                // Declare Edit Text Fields and Buttons
        tv1 = (TextView)v.findViewById(R.id.CIdentificador);
        t3 =  (EditText)v.findViewById(R.id.CNpasw);
        t4 =  (EditText)v.findViewById(R.id.CNOpasw);
        botonIngresar = (Button) v.findViewById(R.id.ingresar);
        botonRegresar = (Button) v.findViewById(R.id.regresar);
        FragmentCompletarRegistro ident = new FragmentCompletarRegistro();
        tv1.setText(ident.identificador);

        botonIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passw = t3.getText().toString();
                otrapassw = t4.getText().toString();

                try {
                    if (passw.equals(otrapassw)) {
                        ParseUser user = ParseUser.logIn(identificador, "1234");
                        user.setPassword(passw);
                        user.saveInBackground();

                        Fragment fragment = new FragmentMensajeCustom();
                        getFragmentManager()
                                .beginTransaction()
                                .replace(R.id.content_frame, fragment)
                                .commit();
                        ((TextView)v.findViewById(R.id.mensaje)).setText("Su cuenta ha sido registrada satisfactoriamente");

                    } else {
                        tv1.setText("Las contraseñas no coinciden");
                        tv1.setTextColor(Color.RED);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });


        return v;
    }
}
