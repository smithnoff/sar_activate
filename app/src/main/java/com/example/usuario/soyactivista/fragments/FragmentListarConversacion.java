package com.example.usuario.soyactivista.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.parse.ParseUser;

import logica.ListarConversacionParseAdapter;
import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by Brahyam on 1/12/2015.
 */
public class FragmentListarConversacion extends Fragment {



    private static final String TAG = "FragmentListarConversation";
    private ListarConversacionParseAdapter listarConversacionAdapter;
    private ListView listViewco;
    private ParseUser currentUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Choose fragment to inflate
        View v = inflater.inflate(R.layout.fragment_listar_conversacion, container, false);

        currentUser = ParseUser.getCurrentUser();

        // Initialize list view
        listViewco = (ListView)v.findViewById(R.id.conversacionesListView);
        listarConversacionAdapter = new ListarConversacionParseAdapter(getContext());

        if(listarConversacionAdapter !=null){
            Log.d("ADAPTER", "Adapter is not null!");
            listViewco.setAdapter(listarConversacionAdapter);
            listarConversacionAdapter.loadObjects();
        }
        else{
            Log.d("ADAPTER", "Adapter returned null!");
        }



        return v;
    }

}
