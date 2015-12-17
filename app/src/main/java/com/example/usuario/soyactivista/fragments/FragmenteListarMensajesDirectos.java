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
public class FragmenteListarMensajesDirectos extends Fragment {
    private static final String TAG = "ChatActivity";

    //private ChatArrayAdapter chatArrayAdapter;
    private ListView listView;
    private EditText chatText;
    private Button buttonSend;
    private boolean side = false;

    private ListarMensajesDirectosParse mainAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Choose fragment to inflate
        View v = inflater.inflate(R.layout.fragment_detalles_conversacion_directa, container, false);

        // Initialize main ParseQueryAdapter
        mainAdapter = new ListarMensajesDirectosParse(this.getContext());



        listView = (ListView) v.findViewById(R.id.chatlist);
        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listView.setStackFromBottom(true);
        listView.setAdapter(mainAdapter);
        mainAdapter.loadObjects();

/*
        // Defined Array values to show in ListView
        String[] values = new String[] { "Android List View",
                "Adapter implementation",
                "Simple List View In Android",
                "Create List View Android",
                "Android Example",
                "List View Source Code",
                "List View Array Adapter",
                "Android Example List View"
        };

     //   ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.msg_left,values);

        //ArrayAdapter adapter = new ArrayAdapter(this, R.layout.msg_left, values);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),R.layout.msg_right, R.id.lbl1, values);
        listView.setAdapter(adapter);*/



        // TODO: Implement Theme Editing. Right now all login on Main Activity
        return v;
    }



}
