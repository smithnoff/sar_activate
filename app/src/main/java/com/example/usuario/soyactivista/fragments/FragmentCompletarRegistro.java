package com.example.usuario.soyactivista.fragments;

import android.app.Dialog;
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
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import logica.ActivityPantallaInicio;
import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by Luis Adrian on 07/12/2015.
 */
public class FragmentCompletarRegistro extends Fragment {

    private static final String TAG = "CompletarRegistro";

    // View Elements
    private TextView username, valueMensaje;
    private EditText editPassword, editRepeatPasword, editUsername, editToken;
    private Button buttonIngresar,buttonRegresar;

    // Dialogs needed for flow
    Dialog v, nuevaContraseña;

    // Progress Dialog
    ProgressDialog progressDialog;

    private ProgressDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        View v = inflater.inflate(R.layout.fragment_completar_registro, container, false);

        // Asociate Fields
        editUsername = (EditText) v.findViewById(R.id.editUsername);
        editToken = (EditText) v.findViewById(R.id.editToken);

        buttonIngresar = (Button) v.findViewById(R.id.buttonIngresar);
        buttonRegresar = (Button) v.findViewById(R.id.buttonRegresar);

        //TODO: declare buttons and properly instantiate clicklisteners.
        // Ingresar Button

        buttonIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = ProgressDialog.show(getActivity(), "", "Verificando Datos...", true);
                Log.d(TAG, "Searching for Username:"+editUsername.getText().toString().trim());
                Log.d(TAG, "Searching for Token:" + editToken.getText().toString().trim());

                ParseUser currentUser = ParseUser.getCurrentUser();
                if(currentUser != null)
                    currentUser.logOut();

                ParseQuery<ParseUser> query = ParseUser.getQuery();
                query.whereEqualTo("username", editUsername.getText().toString().trim());
                query.whereEqualTo("objectId", editToken.getText().toString().trim());
                query.getFirstInBackground(new GetCallback<ParseUser>() {
                    public void done(ParseUser object, ParseException e) {
                        if (object == null) {
                            dialog.dismiss();
                            if(e.getCode() == 101)
                                Toast.makeText(getActivity(), "Datos Incorrectos", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            dialog.dismiss();
                            // Open new fragment for password

                            // Pass Username
                            Bundle data = new Bundle();
                            data.putString("username",object.getString("username"));
                            Fragment fragment = new FragmentCompletarPassword();
                            fragment.setArguments(data);
                            getFragmentManager()
                                    .beginTransaction()
                                    .add(android.R.id.content, fragment)
                                    .addToBackStack(null)
                                    .commit();
                        }
                    }
                });
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
