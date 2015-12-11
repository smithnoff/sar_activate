package com.example.usuario.soyactivista.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
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

import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by Joisacris on 19/10/2015.
 */
public class DialogoRecuperarIdentificador extends DialogFragment {
    Dialog customDialog = null;
    Dialog nueva = null;
    Dialog mensaje = null;
    EditText t1;
    String correo;
    TextView tv1;
    private ProgressDialog dialog;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        customDialog = new Dialog(getActivity(), R.style.Theme_Dialog_Translucent);
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customDialog.setCancelable(false);
        customDialog.setContentView(R.layout.recuperar_identificador);
        t1 = (EditText) customDialog.findViewById(R.id.CorreoRecuperar);

        ((Button) customDialog.findViewById(R.id.buttonIngresar)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                dialog = ProgressDialog.show(getActivity(), "", "Buscando datos...", true);
                correo = t1.getText().toString();

                ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
                query.whereEqualTo("email",correo);
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
                            nuevaPass(object.getString("username"));
                        }
                    }
                });
            }
        });

        ((Button) customDialog.findViewById(R.id.buttonRegresar)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                customDialog.dismiss();
            }
        });

        return customDialog;
    }

    public void nuevaPass(String username){
        customDialog.dismiss();

        nueva = new Dialog(getActivity(), R.style.Theme_Dialog_Translucent);
        nueva.requestWindowFeature(Window.FEATURE_NO_TITLE);
        nueva.setCancelable(false);
        nueva.setContentView(R.layout.mensajes);

        ((TextView) nueva.findViewById(R.id.valueMensaje)).setText("Su identificador es: " + username);

        ((Button) nueva.findViewById(R.id.buttonIngresar)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                nueva.dismiss();
            }
        });

        ((Button) nueva.findViewById(R.id.buttonRegresar)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                nueva.dismiss();
            }
        });

        nueva.show();
    }
}
