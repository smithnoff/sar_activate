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
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import logica.Conversacion;
import logica.ListarConversacionAdapter;
import logica.ListarConversacionParseAdapter;
import logica.ListarUsuarioAdapter;
import logica.Usuario;
import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by Brahyam on 1/12/2015.
 */
public class FragmentListarConversacion extends Fragment {



    private static final String TAG = "FragListarConversation";
    private ListarConversacionAdapter listarConversacionAdapter;
    private ListView listView;
    private ParseUser currentUser;
    private ArrayList<Conversacion> conversacionArrayList = new ArrayList<>();
    private ProgressDialog dialog;
    // Buttons
    FloatingActionButton botonCrearConversacion;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate View
        View view = inflater.inflate(R.layout.fragment_listar_conversacion, container, false);

        // Initialize list view
        listView = (ListView) view.findViewById(R.id.conversacionesListView);

        // Initialize Buttons
        botonCrearConversacion = (FloatingActionButton) view.findViewById(R.id.botonCrearConversacion);


        Log.d(TAG,"List contains "+conversacionArrayList.size()+" elements");

        // If adapter is null Initialize list and set adapter to view
        if(listarConversacionAdapter == null){
            Log.d(TAG, "Array Adapter is null");
            initializeList(conversacionArrayList);
        }
        // List Already contains elements/ Just set adapter to view
        else{
            Log.d(TAG, "Array Adapter is OK with " + listarConversacionAdapter.getCount()+" elements");
            // Add Elements to List and reset adapter
            listView.setAdapter(listarConversacionAdapter);
        }


        botonCrearConversacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new FragmentListarUsuariosConversacion();
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Store data in bundle to send to next fragment
                ParseObject conversacion = (ParseObject) listView.getItemAtPosition(position);


                Bundle datos = new Bundle();
                datos.putString("id", conversacion.getObjectId());


                // Redirect View to next Fragment
                Fragment fragment = new FragmentListarMensajesDirectos();
                fragment.setArguments(datos);
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, fragment)
                        .addToBackStack(null)
                        .commit();
            }

        });

        return view;
    }


    // TODO: Structure query / table to not need both queries to get the final list.
    // Initializes list and sets listView adapter to the newly createde adapter.
    public void initializeList(final ArrayList<Conversacion> list) {
        // Show loading Dialog
        dialog = ProgressDialog.show(getContext(), "Buscando Conversaciones", "Cargando", true);

        // List of Conversations
        final ArrayList<String> idsConversaciones = new ArrayList<>();

        // Get Query
        final ParseQuery<ParseObject> query = ParseQuery.getQuery("ParticipanteConversacion");
        query.whereEqualTo("usuario", currentUser);
        query.include("conversacion");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> object, ParseException e) {
                if (e == null) { //no hay error

                    // Get all conversations ids in a list.
                    for (int i = 0; i < object.size(); i++) {
                        idsConversaciones.add(object.get(i).getParseObject("conversacion").getObjectId());
                    }

                    ParseQuery<ParseObject> innerQuery = ParseQuery.getQuery("Conversacion");
                    innerQuery.whereContainedIn("objectId", idsConversaciones);

                    ParseQuery<ParseObject> query2 = ParseQuery.getQuery("ParticipanteConversacion");
                    query2.whereNotEqualTo("usuario", currentUser);
                    query2.whereMatchesQuery("conversacion", innerQuery);
                    query2.include("conversacion");
                    query2.include("usuario");
                    query2.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {
                            if (e == null) { // No Error)
                                // Get all conversations in a list
                                Conversacion conversacion;
                                for (int i = 0; i < objects.size(); i++) {
                                    conversacion = new Conversacion();
                                    conversacion.usuario.setId(objects.get(i).getObjectId());
                                    conversacion.usuario.setUsername(objects.get(i).getParseUser("usuario").getUsername());
                                    conversacion.usuario.setNombre(objects.get(i).getParseUser("usuario").getString("nombre"));
                                    conversacion.usuario.setApellido(objects.get(i).getParseUser("usuario").getString("apellido"));
                                    conversacion.usuario.setCargo(objects.get(i).getParseUser("usuario").getString("cargo"));
                                    conversacion.usuario.setEstado(objects.get(i).getParseUser("usuario").getString("estado"));
                                    conversacion.usuario.setMunicipio(objects.get(i).getParseUser("usuario").getString("municipio"));
                                    conversacion.usuario.setRol(objects.get(i).getParseUser("usuario").getInt("rol"));
                                    list.add(conversacion);
                                }
                                Log.d(TAG,"List have " + list.size() + " items.");
                                listarConversacionAdapter = new ListarConversacionAdapter(getActivity(), list);
                                listView.setAdapter(listarConversacionAdapter);

                                dialog.dismiss();

                            } else {
                                dialog.dismiss();
                                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } else {
                    dialog.dismiss();
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}