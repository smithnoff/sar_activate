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
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import logica.ListarUsuarioJerarquiaAdapter;
import logica.Usuario;
import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by Brahyam on 2/12/2015.
 */
public class FragmentListarUsuariosConversacion extends Fragment{

    // Log TAG
    private String TAG = "FragmentListarUsuarioConversacion";

    // Data Holders
    private ListarUsuarioJerarquiaAdapter listarUsuarioJerarquiaAdapter;
    private ListView listView;
    private ArrayList<Usuario> usuarioArrayList = new ArrayList<>();
    private ParseUser currentUser;
    private ArrayList<String> conversacionesAbiertas;

    // Filter Vars
    private String comite;
    private String estado;
    private String municipio;
    private String parroquia;

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

        currentUser = ParseUser.getCurrentUser();

        getFilterBundle(); // Get filter variables-

        // load already opened conversations
        conversacionesAbiertas = new ArrayList<>(getArguments().getStringArrayList("conversacionesAbiertas"));

        Log.d(TAG,"List contains "+usuarioArrayList.size()+" elements");

        // If adapter is null Initialize list and set adapter to view
        if(listarUsuarioJerarquiaAdapter == null){
            Log.d(TAG,"Array Adapter is null");
            initializeList(usuarioArrayList);
        }
        // List Already contains elements/ Just set adapter to view
        else{
            Log.d(TAG, "Array Adapter is OK with " + listarUsuarioJerarquiaAdapter.getCount()+" elements");
            // Add Elements to List and reset adapter
            listView.setAdapter(listarUsuarioJerarquiaAdapter);
        }

        // Handle Item OnClick Events
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                // Store data in bundle to send to next fragment
                Usuario usuarioSeleccionado = (Usuario) listView.getItemAtPosition(position);
                Bundle datos = new Bundle();
                Log.d(TAG,"Cargando en Bundle: Usuario "+usuarioSeleccionado.getId());
                datos.putString("receptorId", usuarioSeleccionado.getId());
                datos.putString("receptorUsername", usuarioSeleccionado.getUsername());
                datos.putBoolean("existeConversacion",false);


                // Redirect View to next Fragment
                Fragment fragment = new FragmentCrearMensajeDirecto();
                fragment.setArguments(datos);
                getFragmentManager()
                        .beginTransaction()
                        .replace(((ViewGroup) getView().getParent()).getId(), fragment)
                        .commit();

            }
        });

        // Let the fragment know we will be loading some options for this fragment
        setHasOptionsMenu(true);

        return view;
    }

    //Retrieve Arguments and set up Filters
    private void getFilterBundle() {
        Bundle data = getArguments();

        if(data.getString("comite") != null){
            comite = data.getString("comite");
            Log.d(TAG,"Retrived Comite:"+comite);
        }

        if(data.getString("estado") != null){
            estado = data.getString("estado");
            Log.d(TAG,"Retrived Estado:"+estado);
        }

        if(data.getString("municipio") != null){
            municipio = data.getString("municipio");
            Log.d(TAG,"Retrived Municipio:"+municipio);
        }

        if(data.getString("parroquia") != null){
            parroquia = data.getString("parroquia");
            Log.d(TAG,"Retrived Parroquia:"+parroquia);
        }
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
                    listarUsuarioJerarquiaAdapter.getFilter().filter("texto=" + query);
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
                        listarUsuarioJerarquiaAdapter.getFilter().filter("estado="+listView.getItemAtPosition(position));
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

        ParseQuery mainQuery = new ParseQuery("_User");

        if(comite != null)
            mainQuery.whereEqualTo("comite", comite);
        if(estado != null)
            mainQuery.whereEqualTo("estado",estado);
        if(municipio != null)
            mainQuery.whereEqualTo("municipio",municipio);
        if(parroquia != null)
            mainQuery.whereEqualTo("parroquia",parroquia);

        Log.d(TAG,"Current user is: "+currentUser.getUsername());
        mainQuery.whereNotContainedIn("objectId",conversacionesAbiertas);
        mainQuery.whereEqualTo("eliminado",false);
        mainQuery.whereNotEqualTo("objectId",currentUser.getObjectId());
        mainQuery.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> object, ParseException e) {
                if (e == null) { //no hay error
                    Usuario usuario;
                    for (int i = 0; i < object.size(); i++) {
                        usuario = new Usuario();
                        usuario.setId(object.get(i).getObjectId());
                        usuario.setNombre(object.get(i).getString("nombre"));
                        usuario.setApellido(object.get(i).getString("apellido"));
                        usuario.setEmail(object.get(i).getEmail());
                        usuario.setUsername(object.get(i).getUsername());
                        usuario.setCargo(object.get(i).getString("cargo"));
                        usuario.setEstado(object.get(i).getString("estado"));
                        usuario.setMunicipio(object.get(i).getString("municipio"));
                        usuario.setComite(object.get(i).getString("comite"));
                        usuario.setRol(object.get(i).getInt("rol"));
                        usuario.setFoto(object.get(i).getParseFile("fotoPerfil"));
                        list.add(usuario);
                    }
                    Log.d(TAG, "List have " + list.size() + " items.");
                    listarUsuarioJerarquiaAdapter = new ListarUsuarioJerarquiaAdapter(getActivity(),list);
                    listView.setAdapter(listarUsuarioJerarquiaAdapter);

                    // If no Search/Filter Argument initialize list, else filter.
                    if(getArguments() != null && getArguments().getString("busqueda") != null){
                        listarUsuarioJerarquiaAdapter.getFilter().filter(getArguments().getString("busqueda"));
                    }

                    dialog.dismiss();

                } else {
                    dialog.dismiss();
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

    }

}
