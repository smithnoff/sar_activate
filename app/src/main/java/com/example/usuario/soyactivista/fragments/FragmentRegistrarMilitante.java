package com.example.usuario.soyactivista.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import logica.FActivityPantallaMenu;
import logica.pantalla_principal;
import soy_activista.quartzapp.com.soy_activista.R;


/**
 * Created by Usuario on 07/10/2015.
 */
public class FragmentRegistrarMilitante extends Fragment {

    private EditText identificador,nombre,apellido,correo,cargo;
    private Button registrar;
    private String ide,nom,ape,co,car;
    private ProgressDialog dialog;
    Spinner spinEstado;
    Spinner spinMunicipio;
    Spinner spinPertinencia;
    Spinner spinRol;
    String[] listaEstado;
    public FragmentRegistrarMilitante(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_registro_militante, container, false);

        identificador = (EditText)v.findViewById(R.id.RcampoID);
        nombre = (EditText)v.findViewById(R.id.RcampoNombre);
        apellido = (EditText)v.findViewById(R.id.RcampoApellido);
        correo = (EditText)v.findViewById(R.id.RcampoCorreo);
        cargo = (EditText)v.findViewById(R.id.RcampoCargo);
        registrar = (Button)v.findViewById(R.id.buttonRegistro);

        spinEstado = (Spinner)v.findViewById(R.id.spinEstado);
        spinMunicipio = (Spinner)v.findViewById(R.id.spinMunicipio);
        spinPertinencia = (Spinner)v.findViewById(R.id.spinPertenencia);
        this.llenarSpinnerdesdeId(spinEstado, R.array.Estados);
        this.llenarSpinnerdesdeId(spinPertinencia, R.array.Comite);
        spinEstado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinMunicipio.setAdapter(null);
                llenarSpinnerdesdeId(spinMunicipio, getResources().getIdentifier(spinEstado.getSelectedItem().toString().replace(' ', '_'), "array", getActivity().getPackageName()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinRol = (Spinner)v.findViewById(R.id.spinRol);
        this.llenarSpinnerdesdeId(spinRol,R.array.Roles);



        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dialog = ProgressDialog.show(getActivity(), "", "Registrando usuario...", true);

                ide = identificador.getText().toString();
                nom = nombre.getText().toString();
                ape = apellido.getText().toString();
                co = correo.getText().toString();
                car = cargo.getText().toString();

                final ParseUser user = new ParseUser();
                user.setUsername(ide);
                user.setPassword("1234"); //TODO: Improve password generation to be random
                user.put("Nombre", nom);
                user.put("Apellido", ape);
                user.setEmail(co);
                user.put("Cargo", car);
                user.put("Estado", spinEstado.getSelectedItem().toString());
                user.put("Municipio", spinMunicipio.getSelectedItem().toString());
                user.put("Comite", spinPertinencia.getSelectedItem().toString());
                user.put("Rol", spinRol.getSelectedItemPosition());

                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            dialog.dismiss();
                            Toast.makeText(getActivity(), "Usuario registrado correctamente", Toast.LENGTH_SHORT).show();
                            // Logout User & Redirect View to Dashboard
                            user.logOut();
                            Intent i = new Intent(getActivity(), pantalla_principal.class);
                            startActivity(i);


                        } else {
                            dialog.dismiss();
                            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        return v;
    }

    public void llenarSpinnerdesdeId(Spinner spin,int id){

        ArrayAdapter spinner_adapter = ArrayAdapter.createFromResource(getActivity(), id, android.R.layout.simple_spinner_item);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(spinner_adapter);
    }
}
