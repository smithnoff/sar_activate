package com.example.usuario.soyactivista.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import logica.ListarUsuarioAdapter;
import logica.ListarUsuarioParseAdapter;
import logica.Usuario;
import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by Brahyam on 2/12/2015.
 */
public class FragmentListarUsuario extends Fragment{

    // Log TAG
    private String TAG = "FragmentListarUsuario";

    // Data Holders
    private ListarUsuarioAdapter listarUsuarioAdapter;
    private ListView listView;
    private ArrayList<Usuario> usuarioArrayList = new ArrayList<>();

    // Buttons
    FloatingActionButton botonCrearUsuario;

    // Progress Dialog For Filtering/Retrieving Users
    ProgressDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate View
        View view = inflater.inflate(R.layout.fragment_listar_usuario, container, false);

        // Initialize list view
        listView = (ListView) view.findViewById(R.id.usuariosListView);

        // Initialize Buttons
        botonCrearUsuario = (FloatingActionButton) view.findViewById(R.id.botonCrearUsuario);

        // Initializes the list - If the fragment was called with query data also filters data.
        if(usuarioArrayList.size()==0)
            initializeList(usuarioArrayList);

        // Handle Item OnClick Events
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Store data in bundle to send to next fragment
                ParseUser usuario = (ParseUser) listView.getItemAtPosition(position);

                Bundle datos = new Bundle();
                datos.putString("id", usuario.getObjectId());
                datos.putString("username", usuario.getUsername());
                datos.putString("nombre", usuario.getString("nombre"));
                datos.putString("apellido", usuario.getString("apellido"));
                datos.putString("email", usuario.getEmail());
                datos.putString("estado", usuario.getString("estado"));
                datos.putString("municipio", usuario.getString("municipio"));
                datos.putString("cargo", usuario.getString("cargo"));
                datos.putString("comite", usuario.getString("comite"));
                datos.putInt("rol", usuario.getInt("rol"));

                // Redirect View to next Fragment
                Fragment fragment = new FragmenteEditarUsuario();
                fragment.setArguments(datos);
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });



        // Create new User Button
        botonCrearUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new FragmentCrearUsuario();
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });


        return view;
    }

    // TODO: Find a way to provide search query from within the fragment, so list doesnt have to be initialized again.
    public void initializeList(final ArrayList<Usuario> list){
        dialog = ProgressDialog.show(getContext(),"Buscando Usuarios","Cargando",true);
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> object, ParseException e) {
                if (e == null) { //no hay error
                    Usuario usuario;
                    for (int i = 0; i < object.size(); i++) {
                        usuario = new Usuario();
                        usuario.setNombre((String) object.get(i).get("nombre"));
                        usuario.setApellido((String) object.get(i).get("apellido"));
                        usuario.setCorreo(object.get(i).getEmail());
                        usuario.setIdentificador(object.get(i).getUsername()/*.toLowerCase()*/);
                        usuario.setCargo((String) object.get(i).get("cargo"));
                        usuario.setEstado((String) object.get(i).get("estado"));
                        usuario.setMunicipio((String) object.get(i).get("municipio"));
                        usuario.setComite(object.get(i).getString("comite"));
                        usuario.setRol(object.get(i).getInt("rol"));
                        list.add(usuario);
                    }
                    Log.d(TAG, "List have " + list.size() + " items.");
                    listarUsuarioAdapter = new ListarUsuarioAdapter(getActivity(),list);
                    listView.setAdapter(listarUsuarioAdapter);

                    // If no Search/Filter Argumentinitialize list, else filter.
                    if(getArguments() != null && getArguments().getString("busqueda") != null){
                        listarUsuarioAdapter.getFilter().filter(getArguments().getString("busqueda"));
                    }

                    dialog.dismiss();

                } else {
                    dialog.dismiss();
                    /*Log.d("HORROR", "Error: " + e.getMessage());*/
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
