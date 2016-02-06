package com.example.usuario.soyactivista.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import logica.ErrorCodeHelpers;
import logica.ListarTopUsuariosAdapter;
import logica.Usuario;
import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by Luis Adrian on 19/01/2016.
 */
public class FragmentTabTopUsuarios extends Fragment{
    private static final String TAG = "FragTopUsuarios";
    private ListarTopUsuariosAdapter adapter;
    private RecyclerView recyclerView;
    private List<Usuario> usuarioArrayList;
    public FragmentTabTopUsuarios() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tab_top_usuarios, container, false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        usuarioArrayList = new ArrayList<>();

        adapter = new ListarTopUsuariosAdapter(usuarioArrayList);

        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerListTopUsuarios);

        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(adapter);

        initializeList();

        setHasOptionsMenu(true);

        return v;
    }

    // Initializes list and sets listView adapter to the newly created adapter.
    public void initializeList(){
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("eliminado",false);
        query.setLimit(20);
        query.orderByDescending("puntos");

        // Check if State or Municipal Level
        if(getArguments() != null){
            if(getArguments().getString("estado") != null)
                query.whereEqualTo("estado",getArguments().getString("estado"));
        }

        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> object, ParseException e) {
                if (e == null) { //no hay error
                    Usuario usuario;
                    Log.d(TAG,object.size()+" users retrieved.");
                    for (int i = 0; i < object.size(); i++) {
                        usuario = new Usuario();
                        usuario.setNombre(object.get(i).getString("nombre"));
                        usuario.setApellido(object.get(i).getString("apellido"));
                        usuario.setCargo( object.get(i).getString("cargo"));
                        usuario.setMunicipio( object.get(i).getString("municipio"));
                        usuarioArrayList.add(usuario);
                    }

                    adapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(getActivity(), ErrorCodeHelpers.resolveErrorCode(e.getCode()), Toast.LENGTH_LONG).show();
                    Log.d(TAG, ErrorCodeHelpers.resolveLogErrorString(e.getCode(), e.getMessage()));
                }
            }
        });

    }


    // Inflates custom menu for fragment.
    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        // Inflate Custom Menu
        inflater.inflate(R.menu.menu_puntuaciones, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.ayuda:
                Fragment fragment = new FragmentAyudaTopUsuarios();
                fragment.setArguments(getArguments());
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, fragment)
                        .addToBackStack(null)
                        .commit();
                break;
        }

        return true;
    }
}
