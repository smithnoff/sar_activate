package com.example.usuario.soyactivista.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import logica.ErrorCodeHelper;
import logica.ListarRankingEstadosAdapter;
import logica.ListarTopUsuariosNacionalAdapter;
import logica.ListarUsuarioAdapter;
import logica.Usuario;
import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by Luis Adrian on 19/01/2016.
 */
public class FragmentTabTopUsuariosNacional extends Fragment{
    private ListarTopUsuariosNacionalAdapter listarUsuarioAdapter;
    private RecyclerView recyclerView;
    private List<Usuario> usuarioArrayList = new ArrayList<>();
    private Usuario usuario;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tab_top_usuarios_nacional, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerListTopUsuariosNacional);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        initializeList(usuarioArrayList);

        return v;
    }

    // Initializes list and sets listView adapter to the newly created adapter.
    public void initializeList(final List<Usuario> list){
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("eliminado",false);
        query.orderByDescending("puntos");
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> object, ParseException e) {
                if (e == null) { //no hay error

                    for (int i = 0; i < object.size(); i++) {
                        usuario = new Usuario();
                        usuario.setNombre(object.get(i).getString("nombre"));
                        usuario.setApellido(object.get(i).getString("apellido"));
                        usuario.setCargo( object.get(i).getString("cargo"));
                        usuario.setMunicipio( object.get(i).getString("municipio"));
                        usuario.setPosicion(1+i);
                        list.add(usuario);
                    }
                    recyclerView.setAdapter(new ListarTopUsuariosNacionalAdapter(list));

                } else {
                    Toast.makeText(getActivity(), ErrorCodeHelper.resolveErrorCode(e.getCode()), Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
