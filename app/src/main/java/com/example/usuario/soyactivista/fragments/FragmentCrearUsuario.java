package com.example.usuario.soyactivista.fragments;

import android.app.ProgressDialog;
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

import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import java.util.HashMap;

import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by Brahyam on 2/12/2015.
 */
public class FragmentCrearUsuario extends Fragment {

    // Variable Declaration
    private String TAG = "FragmentCrearUsuario"; // For Log.d
    private EditText editUsername, editNombre, editApellido, editEmail, editCargo;
    private Spinner spinEstado, spinMunicipio, spinComite, spinRol;
    private Button buttonRegistrar;
    private ProgressDialog dialog;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        //Choose fragment to inflate
        View v = inflater.inflate(R.layout.fragment_crear_usuario, container, false);

        // Setting Holders
        editUsername = (EditText)v.findViewById(R.id.editUsername);
        editNombre = (EditText)v.findViewById(R.id.editNombre);
        editApellido = (EditText)v.findViewById(R.id.editApellido);
        editEmail = (EditText)v.findViewById(R.id.editEmail);
        editCargo = (EditText)v.findViewById(R.id.editCargo);

        spinEstado = (Spinner)v.findViewById(R.id.spinEstado);
        spinMunicipio = (Spinner)v.findViewById(R.id.spinMunicipio);
        spinComite = (Spinner)v.findViewById(R.id.spinComite);
        spinRol = (Spinner)v.findViewById(R.id.spinRol);

        buttonRegistrar = (Button)v.findViewById(R.id.buttonRegistrar);

        // Load Spinners
        fillSpinnerfromResource(spinEstado,R.array.Estados);
        fillSpinnerfromResource(spinComite,R.array.Comite);
        fillSpinnerfromResource(spinRol,R.array.Roles);

        // Fill Municipios on Estado Selected
        spinEstado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinMunicipio.setAdapter(null);
                fillSpinnerfromResource(spinMunicipio,getResources().getIdentifier(spinEstado.getSelectedItem().toString().replace(' ','_'),"array",getActivity().getPackageName()));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        buttonRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                // Validate fields are not empty
                if (editUsername.getText().toString().trim().length()> 0
                        && editNombre.getText().toString().trim().length()>0
                        && editApellido.getText().toString().trim().length()>0
                        && editCargo.getText().toString().trim().length()>0
                        && editEmail.getText().toString().trim().length()>0) {

                    // Validate email address matches pattern
                    if (editEmail.getText().toString().matches(emailPattern)) {

                        // Start Loading Dialog
                        dialog = ProgressDialog.show(getActivity(), "Crear Usuario", "Enviando Datos...", true);

                        // Load values in hash map to send to parse
                        final HashMap<String, Object> params = new HashMap<>();
                        params.put("username", editUsername.getText().toString());
                        params.put("nombre", editNombre.getText().toString());
                        params.put("apellido", editApellido.getText().toString());
                        params.put("correo", editEmail.getText().toString());
                        params.put("cargo", editCargo.getText().toString());
                        params.put("estado", spinEstado.getSelectedItem().toString());
                        params.put("municipio", spinMunicipio.getSelectedItem().toString());
                        params.put("comite", spinComite.getSelectedItem().toString());
                        params.put("rol", String.valueOf(spinRol.getSelectedItemPosition()));
                        ParseCloud.callFunctionInBackground("createUser", params, new FunctionCallback<Object>() {
                            @Override
                            public void done(Object response, ParseException e) {
                                if (e == null) {
                                    dialog.dismiss();
                                    Toast.makeText(getActivity(), "Usuario creado correctamente.", Toast.LENGTH_SHORT).show();
                                    Fragment fragment = new FragmentListarUsuario();
                                    getFragmentManager()
                                            .beginTransaction()
                                            .replace(R.id.content_frame, fragment)
                                            .commit();

                                } else {
                                    dialog.dismiss();
                                    Toast.makeText(getActivity(), "Ocurrió un error, por favor intente más tarde." + e.toString(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                    else
                    {
                        Toast.makeText(getContext(),"Correo inválido.",Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                else
                {
                    Toast.makeText(getContext(),"No puede haber campos vacíos.",Toast.LENGTH_LONG).show();
                    return;
                }

            }
        });


        return v;
    }

    // Fills spinners setting adapter from a String array.
    public void fillSpinnerfromResource(Spinner spin,int id){
        ArrayAdapter spinner_adapter = ArrayAdapter.createFromResource(getActivity(), id, android.R.layout.simple_spinner_item);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(spinner_adapter);
    }
}
