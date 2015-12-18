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
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import logica.ListarUsuarioAdapter;
import logica.Usuario;
import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by Brahyam on 2/12/2015.
 */
public class FragmentListarUsuariosConversacion extends Fragment{

    // Log TAG
    private String TAG = "FragmentListarUsuarioConversacion";

    // Data Holders
    private ListarUsuarioAdapter listarUsuarioAdapter;
    private ListView listView;
    private ArrayList<Usuario> usuarioArrayList = new ArrayList<>();
    private ParseUser currentUser = ParseUser.getCurrentUser();

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
        listView = (ListView) view.findViewById(R.id.mensajesListView);

        // Initialize Buttons / Hide
        botonCrearUsuario = (FloatingActionButton) view.findViewById(R.id.botonCrearUsuario);
        botonCrearUsuario.setVisibility(View.GONE);


        Log.d(TAG,"List contains "+usuarioArrayList.size()+" elements");

        // If adapter is null Initialize list and set adapter to view
        if(listarUsuarioAdapter == null){
            Log.d(TAG,"Array Adapter is null");
            initializeList(usuarioArrayList);
        }
        // List Already contains elements/ Just set adapter to view
        else{
            Log.d(TAG, "Array Adapter is OK with " + listarUsuarioAdapter.getCount()+" elements");
            // Add Elements to List and reset adapter
            listView.setAdapter(listarUsuarioAdapter);
        }

        // Handle Item OnClick Events
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Creates New Conversation and Adds Users to conversation
                final ParseObject nuevaConversacion = new ParseObject("Conversacion");
                nuevaConversacion.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            ParseObject nuevoParticipante = new ParseObject("ParticipanteConversacion");
                            nuevoParticipante.put("conversacion", nuevaConversacion);
                            nuevoParticipante.put("usuario", currentUser);
                            nuevoParticipante.saveInBackground();

                            ParseObject nuevoParticipante2 = new ParseObject("ParticipanteConversacion");
                            nuevoParticipante.put("conversacion", nuevaConversacion);
                            nuevoParticipante.put("usuario", currentUser);
                            nuevoParticipante2.saveInBackground();



                        }

                    }
                });


                // Store data in bundle to send to next fragment
                Usuario usuario = (Usuario) listView.getItemAtPosition(position);

                Bundle datos = new Bundle();
                datos.putString("userId", usuario.getId());
                datos.putString("username", usuario.getUsername());
                datos.putString("conversacionId",nuevaConversacion.getObjectId());

                // Redirect View to next Fragment
                Fragment fragment = new FragmentCrearMensajeDirectoNew();
                fragment.setArguments(datos);
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, fragment)
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
        inflater.inflate(R.menu.menu_listar_usuario, menu);

        // Search View Initialization - Call to getActionView to be able to cast SearchView on Item
        SearchView searchView = (SearchView) menu.findItem(R.id.buscador).getActionView();
        searchView.setQueryHint("Identificador");

        // Listener
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // If query has something filter adapter
                if (query.length() > 0) {
                    listarUsuarioAdapter.getFilter().filter("texto=" + query);
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
                        listarUsuarioAdapter.getFilter().filter("estado="+listView.getItemAtPosition(position));
                        filterDialog.dismiss();

                    }
                });

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // TODO: Find a way to provide search query from within the fragment, so list doesnt have to be initialized again.
    // Initializes list and sets listView adapter to the newly createde adapter.
    public void initializeList(final ArrayList<Usuario> list){

        dialog = ProgressDialog.show(getContext(),"Buscando Usuarios","Cargando",true);

        // Query List
        List<ParseQuery<ParseUser>> queries = new ArrayList<ParseQuery<ParseUser>>();

        switch (currentUser.getString(("comite"))){
            case "Estadal":

                // Users from Comité Nacinal.
                ParseQuery<ParseUser> query0 = ParseUser.getQuery();
                query0.whereEqualTo("comite","Nacional");
                queries.add(query0);

                // Users from Comité Estadal.
                ParseQuery<ParseUser> query1 = ParseUser.getQuery();
                query1.whereEqualTo("comite","Estadal");
                queries.add(query1);

                // Users from Comité Municipal from my State.
                ParseQuery<ParseUser> query2 = ParseUser.getQuery();
                query2.whereEqualTo("comite","Municipal");
                query2.whereEqualTo("estado",currentUser.getString("estado"));
                queries.add(query2);

                // Users from Comité Municipal from my State.
                ParseQuery<ParseUser> query3 = ParseUser.getQuery();
                query3.whereEqualTo("comite","Municipal");
                query3.whereEqualTo("estado",currentUser.getString("estado"));
                queries.add(query3);

                // Users from Comité Parroquial from my State.
                ParseQuery<ParseUser> query4 = ParseUser.getQuery();
                query4.whereEqualTo("comite","Parroquial");
                query4.whereEqualTo("estado",currentUser.getString("estado"));
                queries.add(query4);

                break;

            case "Municipal":
                // Users from Comité Estadal from my state.
                ParseQuery<ParseUser> query00 = ParseUser.getQuery();
                query00.whereEqualTo("comite", "Estadal");
                query00.whereEqualTo("estado",currentUser.getString("estado"));
                queries.add(query00);

                // Users from Comité Municipal from my State.
                ParseQuery<ParseUser> query11 = ParseUser.getQuery();
                query11.whereEqualTo("comite","Municipal");
                query11.whereEqualTo("estado",currentUser.getString("estado"));
                queries.add(query11);

                // Users from Comité Parroquial from my State.
                ParseQuery<ParseUser> query22 = ParseUser.getQuery();
                query22.whereEqualTo("comite","Parroquial");
                query22.whereEqualTo("estado",currentUser.getString("estado"));
                queries.add(query22);

                break;

            case "Parroquial":

                // Users from Comité Municipal from my State.
                ParseQuery<ParseUser> query000= ParseUser.getQuery();
                query000.whereEqualTo("comite","Municipal");
                query000.whereEqualTo("estado",currentUser.getString("estado"));
                queries.add(query000);

                // Users from Comité Parroquial from my State.
                ParseQuery<ParseUser> query111 = ParseUser.getQuery();
                query111.whereEqualTo("comite","Parroquial");
                query111.whereEqualTo("estado",currentUser.getString("estado"));
                queries.add(query111);

                break;
            default:
                break;
        }

        ParseQuery<ParseUser> mainQuery;

        if(!queries.isEmpty())
            mainQuery = ParseQuery.or(queries);
        else
            mainQuery = ParseUser.getQuery();

        mainQuery.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> object, ParseException e) {
                if (e == null) { //no hay error
                    Usuario usuario;
                    for (int i = 0; i < object.size(); i++) {
                        usuario = new Usuario();
                        usuario.setNombre((String) object.get(i).get("nombre"));
                        usuario.setApellido((String) object.get(i).get("apellido"));
                        usuario.setEmail(object.get(i).getEmail());
                        usuario.setUsername(object.get(i).getUsername()/*.toLowerCase()*/);
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

                    // If no Search/Filter Argument initialize list, else filter.
                    if(getArguments() != null && getArguments().getString("busqueda") != null){
                        listarUsuarioAdapter.getFilter().filter(getArguments().getString("busqueda"));
                    }

                    dialog.dismiss();

                } else {
                    dialog.dismiss();
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
