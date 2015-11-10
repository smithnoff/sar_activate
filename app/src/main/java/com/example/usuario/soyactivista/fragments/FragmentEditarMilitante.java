package com.example.usuario.soyactivista.fragments;

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
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;

import logica.Usuario;
import logica.pantalla_principal;
import soy_activista.quartzapp.com.soy_activista.R;


/**
 * Created by Usuario on 07/10/2015.
 */
public class FragmentEditarMilitante extends Fragment {

    public FragmentEditarMilitante(){}
    Usuario usuario;
    public static final String identiificadorMilitante = "";
    public static final String nombreMilitante = "";
    public static final String apellidoMilitante = "";
    public static final String correoMilitante = "";
    public static final String cargoMilitante = "";
    public static final String estadoMilitante = "";
    public static final String municipioMilitante = "";
    public static final String pertenenciaMilitante = "";
    EditText idEditar, nombreEditar, apellidoEditar, correoEditar, cargoEditar;
    TextView estadoEditar, municipioEditar, pertenenciaEditar;
    Button editar, eliminar, guardar;
    Spinner spinEstado;
    Spinner spinMunicipio;
    Spinner spinPertinencia;
    private String ide,nom,ape,co,car;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_editar_militante, container, false);

        idEditar = (EditText)v.findViewById(R.id.EditarID);
        nombreEditar = (EditText)v.findViewById(R.id.EditarNombre);
        apellidoEditar = (EditText)v.findViewById(R.id.EditarApellido);
        correoEditar = (EditText)v.findViewById(R.id.EditarCorreo);
        cargoEditar = (EditText)v.findViewById(R.id.EditarCargo);

        editar = (Button)v.findViewById(R.id.botonEditarCuenta);
        eliminar = (Button)v.findViewById(R.id.botonEliminarCuenta);
        guardar = (Button)v.findViewById(R.id.botonGuardarDatos);

        estadoEditar = (TextView)v.findViewById(R.id.estadoEditar);
        municipioEditar = (TextView)v.findViewById(R.id.municipioEditar);
        pertenenciaEditar = (TextView)v.findViewById(R.id.comiteEditar);

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

        if (getArguments()!= null){
            idEditar.setText(getArguments().getStringArrayList("Informacion").get(0));
            nombreEditar.setText(getArguments().getStringArrayList("Informacion").get(1));
            apellidoEditar.setText(getArguments().getStringArrayList("Informacion").get(2));
            correoEditar.setText(getArguments().getStringArrayList("Informacion").get(3));
            cargoEditar.setText(getArguments().getStringArrayList("Informacion").get(4));
            estadoEditar.setText("Eatado de inscripcion: "+ getArguments().getStringArrayList("Informacion").get(5));
            municipioEditar.setText("Municipio de inscripcion: "+ getArguments().getStringArrayList("Informacion").get(6));
            pertenenciaEditar.setText("Comite de pertenencia: "+ getArguments().getStringArrayList("Informacion").get(7));
        }else{
            ParseUser usuarioActual = ParseUser.getCurrentUser();
            if (usuarioActual != null){
                idEditar.setText(usuarioActual.getUsername());
                nombreEditar.setText(usuarioActual.getString("Nombre"));
                apellidoEditar.setText(usuarioActual.getString("Apellido"));
                correoEditar.setText(usuarioActual.getEmail());
                cargoEditar.setText(usuarioActual.getString("Cargo"));
                estadoEditar.setText("Eatado de inscripcion: "+ usuarioActual.getString("Estado"));
                municipioEditar.setText("Municipio de inscripcion: "+ usuarioActual.getString("Municipio"));
                pertenenciaEditar.setText("Comite de pertenencia: "+ usuarioActual.getString("Comite"));
            }
        }

        editar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                idEditar.setEnabled(true);
                nombreEditar.setEnabled(true);
                apellidoEditar.setEnabled(true);
                correoEditar.setEnabled(true);
                cargoEditar.setEnabled(true);
                spinEstado.setVisibility(View.VISIBLE);
                spinMunicipio.setVisibility(View.VISIBLE);
                spinPertinencia.setVisibility(View.VISIBLE);

                editar.setEnabled(false);
                guardar.setEnabled(true);
            }
        });

        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0){
                if (getArguments()!= null){
                    eliminar.setEnabled(false);
                }else {
                    ParseUser user = ParseUser.getCurrentUser();
                    user.deleteInBackground();
                    Intent i = new Intent(getContext(), pantalla_principal.class);
                    startActivity(i);
                }
            }
        });

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (getArguments()!= null){

                }else {
                    ide = idEditar.getText().toString();
                    nom = nombreEditar.getText().toString();
                    ape = apellidoEditar.getText().toString();
                    co = correoEditar.getText().toString();
                    car = cargoEditar.getText().toString();

                    ParseUser user = ParseUser.getCurrentUser();
                    if (user != null) {
                        user.setUsername(ide);
                        //user.setPassword("1234");
                        user.put("Nombre", nom);
                        user.put("Apellido", ape);
                        user.setEmail(co);
                        user.put("Cargo", car);
                        user.put("Estado", spinEstado.getSelectedItem().toString());
                        user.put("Municipio", spinMunicipio.getSelectedItem().toString());
                        user.put("Comite", spinPertinencia.getSelectedItem().toString());
                        //user.put("Rol", 0);
                        user.saveInBackground();
                    }
                    Toast.makeText(getActivity(), "Perfil Editado", Toast.LENGTH_SHORT).show();
                }
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
