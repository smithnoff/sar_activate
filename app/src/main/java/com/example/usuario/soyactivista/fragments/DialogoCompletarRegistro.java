package com.example.usuario.soyactivista.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
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

import soy_activista.quartzapp.com.soy_activista.R;

public class DialogoCompletarRegistro extends DialogFragment {

    Dialog customDialog = null;
    Dialog nueva = null;
    Dialog mensaje = null;
    EditText t1,t2,t3,t4;
    String identificador,token, passw, otrapassw;
    TextView tv1;
    private ProgressDialog dialog,dia;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        customDialog = new Dialog(getActivity(), R.style.Theme_Dialog_Translucent);
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customDialog.setCancelable(false);
        customDialog.setContentView(R.layout.completar_registro);
        t1 = (EditText) customDialog.findViewById(R.id.textCIdentificador);
        t2 = (EditText) customDialog.findViewById(R.id.textCToken);

        ((Button) customDialog.findViewById(R.id.ingresar)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                dialog = ProgressDialog.show(getActivity(), "", "Verificando Datos...", true);
                identificador = t1.getText().toString();
                token = t2.getText().toString();

                ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
                query.whereEqualTo("username",identificador);
                query.whereEqualTo("objectId", token);
                query.getFirstInBackground(new GetCallback<ParseObject>()
                {
                    public void done(ParseObject object, ParseException e)
                    {
                        if (object == null)
                        {
                            dialog.dismiss();
                            Toast.makeText(getActivity(), "Datos no encontrados", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            dialog.dismiss();
                            nuevaPass(identificador);
                        }
                    }
                });
            }
        });

        ((Button) customDialog.findViewById(R.id.regresar)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                customDialog.dismiss();
            }
        });

        return customDialog;
    }

    public void nuevaPass(final String identificador){
        customDialog.dismiss();

        nueva = new Dialog(getActivity(), R.style.Theme_Dialog_Translucent);
        nueva.requestWindowFeature(Window.FEATURE_NO_TITLE);
        nueva.setCancelable(false);
        nueva.setContentView(R.layout.comprobar_passw);

        tv1 = (TextView) nueva.findViewById(R.id.CIdentificador);
        t3 = (EditText) nueva.findViewById(R.id.CNpasw);
        t4 = (EditText) nueva.findViewById(R.id.CNOpasw);

        tv1.setText(identificador);

        ((Button) nueva.findViewById(R.id.ingresar)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                passw = t3.getText().toString();
                otrapassw = t4.getText().toString();

                try {
                    if(passw.equals(otrapassw)) {
                        ParseUser user = ParseUser.logIn(identificador, "1234");
                        user.setPassword(passw);
                        user.saveInBackground();

                        nueva.dismiss();

                        nueva.setContentView(R.layout.mensajes);
                        ((TextView) nueva.findViewById(R.id.mensaje)).setText("Su cuenta ha sido registrada satisfactoriamente");

                        ((Button) nueva.findViewById(R.id.ingresar)).setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                nueva.dismiss();
                            }
                        });

                        ((Button) nueva.findViewById(R.id.regresar)).setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                nueva.dismiss();
                            }
                        });
                        nueva.show();
                    }else{
                        tv1.setText("Las contraseñas no coinciden");
                        tv1.setTextColor(Color.RED);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        ((Button) nueva.findViewById(R.id.regresar)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                nueva.dismiss();
            }
        });

        nueva.show();
    }
}
