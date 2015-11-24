package com.example.usuario.soyactivista.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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

import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.HashMap;

import logica.FActivityPantallaMenu;
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
    public static final String rolMilitante = "";

    EditText idEditar, nombreEditar, apellidoEditar, correoEditar, cargoEditar;
    TextView estadoEditar, municipioEditar, pertenenciaEditar, rolEditar;
    Button editar, eliminar, guardar;
    Spinner spinEstado;
    Spinner spinMunicipio;
    Spinner spinPertinencia;
    Spinner spinRol;
    private String identificador,nombre,apellido,email,cargo;

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
        rolEditar = (TextView)v.findViewById(R.id.rolEditar);

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
        this.llenarSpinnerdesdeId(spinRol, R.array.Roles);

        if (getArguments()!= null){

            Log.d(getClass().getName(),"Filling from Arguments");

            idEditar.setText(getArguments().getStringArrayList("Informacion").get(0));
            nombreEditar.setText(getArguments().getStringArrayList("Informacion").get(1));
            apellidoEditar.setText(getArguments().getStringArrayList("Informacion").get(2));
            correoEditar.setText(getArguments().getStringArrayList("Informacion").get(3));
            cargoEditar.setText(getArguments().getStringArrayList("Informacion").get(4));
            estadoEditar.setText("Estado de inscripción: "+ getArguments().getStringArrayList("Informacion").get(5));
            municipioEditar.setText("Municipio de inscripción: "+ getArguments().getStringArrayList("Informacion").get(6));
            pertenenciaEditar.setText("Comite de pertenencia: "+ getArguments().getStringArrayList("Informacion").get(7));
            rolEditar.setText("Rol: "+getArguments().getStringArrayList("Informacion").get(8));

        }else{
            ParseUser usuarioActual = ParseUser.getCurrentUser();
            if (usuarioActual != null){

                Log.d(getClass().getName(),"Filling from Current User");

                idEditar.setText(usuarioActual.getUsername());
                nombreEditar.setText(usuarioActual.getString("Nombre"));
                apellidoEditar.setText(usuarioActual.getString("Apellido"));
                correoEditar.setText(usuarioActual.getEmail());
                cargoEditar.setText(usuarioActual.getString("Cargo"));
                estadoEditar.setText("Eatado de inscripcion: "+ usuarioActual.getString("Estado"));
                municipioEditar.setText("Municipio de inscripcion: "+ usuarioActual.getString("Municipio"));
                pertenenciaEditar.setText("Comite de pertenencia: "+ usuarioActual.getString("Comite"));
                if(usuarioActual.getInt("Rol")==0)
                    rolEditar.setText("Rol: Activista");
                else
                    rolEditar.setText("Rol: Registrante");
            }
        }

        // Buttons Behavior
        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                //idEditar.setEnabled(true); Commenting as Username will be used to fetch.
                nombreEditar.setEnabled(true);
                apellidoEditar.setEnabled(true);
                correoEditar.setEnabled(true);
                cargoEditar.setEnabled(true);
                spinEstado.setVisibility(View.VISIBLE);
                spinMunicipio.setVisibility(View.VISIBLE);
                spinPertinencia.setVisibility(View.VISIBLE);
                spinRol.setVisibility(View.VISIBLE);

                editar.setEnabled(false);
                guardar.setEnabled(true);
            }
        });

        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (getArguments() != null) {
                    Log.d(getClass().getName(), "Deleting User"+idEditar.getText().toString());

                    final HashMap<String, Object> params = new HashMap<>();
                    params.put("username", idEditar.getText().toString());
                    ParseCloud.callFunctionInBackground("deleteUser", params, new FunctionCallback<Object>(){
                        @Override
                        public void done(Object response, ParseException e) {
                            if (e == null) {
                                Toast.makeText(getActivity(), "Usuario eliminado correctamente.", Toast.LENGTH_SHORT).show();
                                getFragmentManager().beginTransaction()
                                        .replace(R.id.content_frame,new FragmentListarUsuario())
                                        .addToBackStack(null)
                                        .commit();
                            } else {
                                Toast.makeText(getActivity(), "Ocurrió un error, por favor intente más tarde.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } else {
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
                Log.d(getClass().getName(),"Guardar Clicked");

                if (getArguments()!= null){

                    Log.d(getClass().getName(),"MOdifying from Arguments");

                    final HashMap<String, Object> params = new HashMap<>();
                    params.put("username", idEditar.getText().toString());
                    params.put("nombre", nombreEditar.getText().toString());
                    params.put("apellido", apellidoEditar.getText().toString());
                    params.put("correo", correoEditar.getText().toString());
                    params.put("cargo", cargoEditar.getText().toString());
                    params.put("estado", spinEstado.getSelectedItem().toString());
                    params.put("municipio", spinMunicipio.getSelectedItem().toString());
                    params.put("comite", spinPertinencia.getSelectedItem().toString());
                    params.put("rol", String.valueOf(spinRol.getSelectedItemPosition()));
                    ParseCloud.callFunctionInBackground("modifyUser", params, new FunctionCallback<Object>(){
                        @Override
                        public void done(Object response, ParseException e) {
                            if (e == null) {
                                Toast.makeText(getActivity(), "Usuario editado correctamente.", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(getActivity(), FActivityPantallaMenu.class);
                                startActivity(i);
                            } else {
                                Toast.makeText(getActivity(), "Ocurrió un error, por favor intente más tarde.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else {

                    ParseUser user = ParseUser.getCurrentUser();
                    if (user != null) {
                        Log.d(getClass().getName(),"Filling from Current User");

                        user.put("Nombre", nombreEditar.getText().toString());
                        user.put("Apellido", apellidoEditar.getText().toString());
                        user.setEmail(correoEditar.getText().toString());
                        user.put("Cargo", cargoEditar.getText().toString());
                        user.put("Estado", spinEstado.getSelectedItem().toString());
                        user.put("Municipio", spinMunicipio.getSelectedItem().toString());
                        user.put("Comite", spinPertinencia.getSelectedItem().toString());
                        user.put("Rol", spinRol.getSelectedItemPosition());
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
