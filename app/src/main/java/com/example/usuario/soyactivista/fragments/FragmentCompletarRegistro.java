package com.example.usuario.soyactivista.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import logica.pantalla_principal;
import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by Luis Adrian on 07/12/2015.
 */
public class FragmentCompletarRegistro extends Fragment {

    EditText t1,t2,t3,t4;
    String identificador,token, passw, otrapassw;
    TextView tv1;
    private ProgressDialog dialog,dia;
    private Button botonIngresar;
    private Button botonRegresar;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_completar_registro, container, false);
        context = v.getContext();



        // Declare Edit Text Fields and Buttons

        t1 = (EditText)v.findViewById(R.id.textCIdentificador);
        t2 = (EditText)v.findViewById(R.id.textCToken);
        botonIngresar = (Button)v.findViewById(R.id.ingresar);
        botonRegresar = (Button)v.findViewById(R.id.regresar);

        botonIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                            Toast.makeText(getActivity(), "Datos Incorrectos", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            dialog.dismiss();
                            // Open new Fragment for password
                            Fragment fragment = new FragmentCompletarPassword();
                            Bundle parametro = new Bundle();

                            parametro.putString("key",identificador);
                            fragment.setArguments(parametro);
                            getFragmentManager()
                                    .beginTransaction()
                                    .replace(android.R.id.content, fragment, "tag").addToBackStack("tag")
                            .commit();
                        }
                    }
                });
            }
        });

        botonRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, pantalla_principal.class);
                startActivity(intent);
            }
        });

        return v;
    }
}
