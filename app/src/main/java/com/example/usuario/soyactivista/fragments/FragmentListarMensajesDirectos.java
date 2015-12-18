package com.example.usuario.soyactivista.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import logica.ListarMensajesDirectosParse;

import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by darwin on 16/12/2015.
 */
public class FragmentListarMensajesDirectos extends Fragment {

    private static final String TAG = "";

    //private ChatArrayAdapter chatArrayAdapter;
    private ListView listView;
    private EditText chatText;
    private Button buttonSend;
    private boolean side = false;
    private String conversacionID;

    private ListarMensajesDirectosParse mainAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Choose fragment to inflate
        View v = inflater.inflate(R.layout.fragment_detalles_conversacion_directa, container, false);

        // Initialize main ParseQueryAdapter
        conversacionID = getArguments().getString("id");
        mainAdapter = new ListarMensajesDirectosParse(this.getContext(),conversacionID);



        listView = (ListView) v.findViewById(R.id.mensajesListView);
     /*   listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listView.setStackFromBottom(true);*/
        listView.setAdapter(mainAdapter);
        mainAdapter.loadObjects();


        return v;
    }


}
