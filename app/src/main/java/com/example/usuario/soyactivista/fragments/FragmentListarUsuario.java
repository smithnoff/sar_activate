package com.example.usuario.soyactivista.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.parse.ParseUser;

import logica.ListarUsuarioParseAdapter;
import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by Brahyam on 2/12/2015.
 */
public class FragmentListarUsuario extends Fragment{

    private ListarUsuarioParseAdapter listarUsuarioParseAdapter;
    private ListView listView;


    public FragmentListarUsuario(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Inflate View
        View view = inflater.inflate(R.layout.fragment_listar_usuario, container, false);

        // Initialize main ParseQueryAdapter
        listarUsuarioParseAdapter = new ListarUsuarioParseAdapter(this.getContext());

        // Initialize list view
        listView = (ListView) view.findViewById(R.id.usuariosListView);

        if (listarUsuarioParseAdapter != null) {
            listView.setAdapter(listarUsuarioParseAdapter);
            listarUsuarioParseAdapter.loadObjects();
        } else {
            Log.d("ADAPTER", "Adapter returned null!");
        }

        // Handle Item OnClick Events
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Store data in bundle to send to next fragment
                ParseUser usuario = (ParseUser) listView.getItemAtPosition(position);

                Bundle datos = new Bundle();
                datos.putString("id", usuario.getObjectId());
                datos.putString("username", usuario.getString("username"));
                datos.putString("nombre", usuario.getString("nombre"));
                datos.putString("apellido", usuario.getString("apellido"));
                datos.putString("email", usuario.getString("email"));
                datos.putString("estado", usuario.getString("estado"));
                datos.putString("municipio", usuario.getString("municipio"));
                datos.putString("cargo", usuario.getString("cargo"));
                datos.putString("comite", usuario.getString("comite"));
                datos.putString("rol", usuario.getString("rol"));

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

        FloatingActionButton botonCrearUsuario = (FloatingActionButton) view.findViewById(R.id.botonCrearUsuario);

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




}
