package com.example.usuario.soyactivista.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import logica.Conversacion;
import logica.ListarConversacionAdapter;
import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by Brahyam on 1/12/2015.
 */
public class FragmentListarConversacion extends Fragment {



    private static final String TAG = "FragListarConversation";
    private ListarConversacionAdapter listarConversacionAdapter;
    private ListView listView;
    private ParseUser currentUser;
    private TextView listaVacia;
    private ArrayList<Conversacion> conversacionArrayList = new ArrayList<>();
    private ProgressDialog dialog;
    private ArrayList<String> conversacionesAbiertas;
    // Buttons
    FloatingActionButton botonCrearConversacion;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        // Inflate View
        View view = inflater.inflate(R.layout.fragment_listar_conversacion, container, false);

        // Initialize list view
        listView = (ListView) view.findViewById(R.id.conversacionesListView);

        // Set empty list message
        listaVacia = (TextView) view.findViewById(R.id.listaVacia);
        listView.setEmptyView(listaVacia);

        // Initialize Buttons
        botonCrearConversacion = (FloatingActionButton) view.findViewById(R.id.botonCrearConversacion);


        Log.d(TAG,"List contains "+conversacionArrayList.size()+" elements");

        initializeList(conversacionArrayList);


        botonCrearConversacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle datos = new Bundle();
                datos.putStringArrayList("conversacionesAbiertas",conversacionesAbiertas);
                Log.d(TAG,"Ids Usuarios: "+conversacionesAbiertas.toString());

                Fragment fragment = new FragmentListarUsuariosConversacion();
                fragment.setArguments(datos);
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
                Conversacion conversacion = (Conversacion) listView.getItemAtPosition(position);

                Bundle datos = new Bundle();
                datos.putString("conversacionId", conversacion.getId());
                datos.putString("receptorId", conversacion.usuario.getId());
                Log.d(TAG, "Item Selected id "+conversacion.getId());

                // Redirect View to next Fragment
                Fragment fragment = new FragmentListarMensajeDirecto();
                fragment.setArguments(datos);
                getFragmentManager()
                        .beginTransaction()
                        .replace(((ViewGroup)getView().getParent()).getId(), fragment)
                        .addToBackStack(null)
                        .commit();
            }

        });

        // Let the fragment know we will be loading some options for this fragment
        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_listar_conversacion, menu);

        // Search View Initialization - Call to getActionView to be able to cast SearchView on Item
        SearchView searchView = (SearchView) menu.findItem(R.id.buscador).getActionView();
        searchView.setQueryHint("Identificador");

        // Listener
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // If query has something filter adapter
                if (query.length() > 0) {
                    listarConversacionAdapter.getFilter().filter("texto=" + query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.filtroEstados:
                // Generate List Holder
                final AlertDialog filterDialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Filtrar por estado");

                // Fill Holder with State List from String Array
                final ListView listView = new ListView(getActivity());
                final ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.Estados)));

                // Add element All.
                arrayList.add(0, "Todos");

                ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,arrayList);
                listView.setAdapter(stringArrayAdapter);
                builder.setView(listView);

                // Show Dialog
                filterDialog = builder.create();
                filterDialog.show();

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        // Request List to filter
                        listarConversacionAdapter.getFilter().filter("estado="+listView.getItemAtPosition(position));
                        filterDialog.dismiss();

                    }
                });

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    // TODO: Structure query / table to not need both queries to get the final list.
    // Initializes list and sets listView adapter to the newly createde adapter.
    public void initializeList(final ArrayList<Conversacion> list) {
        // Show loading Dialog
        dialog = ProgressDialog.show(getContext(), "Buscando Conversaciones", "Cargando", true);

        // List of Conversations
        final ArrayList<String> idsConversaciones = new ArrayList<>();

        conversacionesAbiertas = new ArrayList<>();

        // Current User
        currentUser = ParseUser.getCurrentUser();
        Log.d(TAG,"Current User; "+currentUser.toString());

        // Get Query
        final ParseQuery<ParseObject> query = ParseQuery.getQuery("ParticipanteConversacion");
        query.whereEqualTo("usuario", currentUser);
        query.include("conversacion");
        query.include("receptor");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) { //no hay error
                    Log.d(TAG,"Conversaciones buscadas. Encontradas: "+objects.size());
                    // If elements found
                    if(objects.size() != 0){
                        // Get all conversations ids in a list.
                        Conversacion conversacion;
                        for (int i = 0; i < objects.size(); i++) {

                            conversacion = new Conversacion();
                            conversacion.setId(objects.get(i).getParseObject("conversacion").getObjectId());
                            conversacion.setUltimaActividad(objects.get(i).getParseObject("conversacion").getDate("ultimaActividad"));
                            conversacion.usuario.setId(objects.get(i).getParseUser("receptor").getObjectId());
                            conversacion.usuario.setUsername(objects.get(i).getParseUser("receptor").getUsername());
                            conversacion.usuario.setNombre(objects.get(i).getParseUser("receptor").getString("nombre"));
                            conversacion.usuario.setApellido(objects.get(i).getParseUser("receptor").getString("apellido"));
                            conversacion.usuario.setCargo(objects.get(i).getParseUser("receptor").getString("cargo"));
                            conversacion.usuario.setEstado(objects.get(i).getParseUser("receptor").getString("estado"));
                            conversacion.usuario.setMunicipio(objects.get(i).getParseUser("receptor").getString("municipio"));
                            conversacion.usuario.setRol(objects.get(i).getParseUser("receptor").getInt("rol"));
                            conversacionesAbiertas.add(conversacion.usuario.getId());
                            Log.d(TAG, "Conversacion "+conversacion.getId()+" date "+conversacion.getUltimaActividad());
                            list.add(conversacion);
                        }

                        Log.d(TAG, "Conversaciones Abiertas " + conversacionesAbiertas.size());

                        if(listarConversacionAdapter == null ){
                            listarConversacionAdapter = new ListarConversacionAdapter(getActivity(), list);
                            listView.setAdapter(listarConversacionAdapter);
                        }
                        else{
                            listarConversacionAdapter.clear();
                            listarConversacionAdapter.addAll(list);
                            listarConversacionAdapter.notifyDataSetChanged();
                        }

                        dialog.dismiss();
                    }
                    else // No Elements
                    {
                        dialog.dismiss();
                        Toast.makeText(getActivity(), "Todavía no tienes ninguna conversación abierta.", Toast.LENGTH_LONG).show();
                    }


                } else {
                    dialog.dismiss();
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}