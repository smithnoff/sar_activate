package com.example.usuario.soyactivista.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import logica.ListarActividadAdapter;
import logica.ListarMensajeParseAdapter;
import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by Brahyam on 28/11/2015.
 */
public class FragmentListarActividad extends Fragment {

    private static final String TAG = "FragmentListarActividad";
    private ListarActividadAdapter listarActividadAdapter;
    private ListView listView;
    private ParseUser currentUser;
    private ArrayList<String> likes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Inflate View
        final View view = inflater.inflate(R.layout.fragment_listar_actividad, container, false);

        // Ask for current User
        currentUser = ParseUser.getCurrentUser();

        // Initialize list view
        listView = (ListView)view.findViewById(R.id.actividadesListView);

        likes = new ArrayList<>();

        // Get current user likes
        // TODO: Limit returning likes number
        ParseQuery<ParseObject> query = ParseQuery.getQuery("MeGusta");
        query.whereEqualTo("usuario", currentUser);

        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> parseLikes, ParseException e) {
                if (e == null) {
                    Log.d("Likes", "Retrieved " + parseLikes.size() + " likes");
                    // Add user likes to list
                    for (int i = 0; i < parseLikes.size(); i++) {
                        likes.add(parseLikes.get(i).getParseObject("actividad").getObjectId());
                    }
                    Log.d("Likes", "Likes list contains " + likes.size() + " likes");

                    // Initialize main ParseQueryAdapter
                    listarActividadAdapter = new ListarActividadAdapter(getContext(),likes);

                    if(listarActividadAdapter !=null){
                        Log.d("ADAPTER", "Adapter is not null!");
                        listarActividadAdapter.clear();
                        listView.setAdapter(listarActividadAdapter);
                        listarActividadAdapter.loadObjects();
                    }
                    else{
                        Log.d("ADAPTER", "Adapter returned null!");
                    }

                } else {
                    Log.d("Likes", "Error: " + e.getMessage());
                }
            }
        });


        // Handle Item OnClick Events
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Store data in bundle to send to next fragment
                ParseObject actividad = (ParseObject)listView.getItemAtPosition(position);
                ParseObject tipoActividad = actividad.getParseObject("tipoActividad");
                ParseUser creador = actividad.getParseUser("creador");
                ParseFile imagen1,imagen2,imagen3,imagen4;

                imagen1 = actividad.getParseFile("imagen1");
                imagen2 = actividad.getParseFile("imagen2");
                imagen3 = actividad.getParseFile("imagen3");
                imagen4 = actividad.getParseFile("imagen4");

                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

                Boolean liked = likes.contains(actividad.getObjectId());

                Bundle datos = new Bundle();
                Log.d("LISTAR", "Valor MeGusta: "+actividad.getObjectId());
                datos.putString("id", actividad.getObjectId());
                datos.putString("tipoId",tipoActividad.getObjectId());
                datos.putString("nombre", tipoActividad.getString("nombre"));
                datos.putString("descripcion",tipoActividad.getString("descripcion"));
                datos.putString("puntaje", Integer.toString(tipoActividad.getInt("puntaje")));
                datos.putString("objetivo",actividad.getString("objetivo"));
                datos.putString("encargado",actividad.getString("encargado"));
                datos.putString("creadorId",creador.getObjectId());
                datos.putString("creador",creador.getString("nombre")+" "+creador.getString("apellido"));
                datos.putString("estatus",actividad.getString("estatus"));
                datos.putString("inicio",format.format(actividad.getDate("inicio")));
                datos.putString("fin", format.format(actividad.getDate("fin")));
                datos.putBoolean("liked",liked);

                Log.d("LISTAR", "Valor MeGusta: "+actividad.getInt("meGusta"));
                datos.putInt("meGusta",actividad.getInt("meGusta"));

                // Check if images are null and save URLs
                if(imagen1 != null)
                    datos.putString("imagen1",imagen1.getUrl());
                if(imagen2 != null)
                    datos.putString("imagen2",imagen2.getUrl());
                if(imagen3 != null)
                    datos.putString("imagen3",imagen3.getUrl());
                if(imagen4 != null)
                    datos.putString("imagen4",imagen4.getUrl());


                // Redirect View to next Fragment
                Fragment fragment = new FragmentDetalleActividad();
                fragment.setArguments(datos);
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        FloatingActionButton botonCrearActividad = (FloatingActionButton)view.findViewById(R.id.botonCrearActividad);

        // Create new Activity Type Button
        botonCrearActividad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new FragmentCrearActividad();
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


    // Inflates custom menu for fragment.
    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        // Inflate Custom Menu
        inflater.inflate(R.menu.menu_listar_actividad, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()) {

            case R.id.filtroEstatus:
                // Generate List Holder
                final AlertDialog alertDialogEstatus;
                AlertDialog.Builder builderEstatus = new AlertDialog.Builder(getActivity());
                builderEstatus.setTitle("Filtrar por Estatus");

                // Fill Holder with State List from String Array
                final ListView listViewDialogEstatus = new ListView(getActivity());
                final ArrayList<String> arrayListEstatus = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.Estatuses)));

                ArrayAdapter<String> stringArrayAdapterEstatus = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,arrayListEstatus);
                listViewDialogEstatus.setAdapter(stringArrayAdapterEstatus);
                builderEstatus.setView(listViewDialogEstatus);

                // Show Dialog
                alertDialogEstatus = builderEstatus.create();
                alertDialogEstatus.show();

                listViewDialogEstatus.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        alertDialogEstatus.dismiss();
                        // Request List to filter
                        // TODO: Create Progress Dialog

                        Log.d(TAG, "Filtering by Status");

                        listarActividadAdapter.clear();

                        listarActividadAdapter = new ListarActividadAdapter(getContext(),likes, "estatus=" + listViewDialogEstatus.getItemAtPosition(position).toString());

                        listView.setAdapter(listarActividadAdapter);

                        listarActividadAdapter.loadObjects();

                        listarActividadAdapter.notifyDataSetChanged();

                        Log.d(TAG, "Adapter has " + listarActividadAdapter.getCount() + " items");


                    }
                });

                return true;

            case R.id.filtroNacionales:

                // Request List to filter
                // TODO: Create Progress Dialog

                listarActividadAdapter.clear();

                listarActividadAdapter = new ListarActividadAdapter(getContext(),likes,"ubicacion=Nacional");

                listView.setAdapter(listarActividadAdapter);

                listarActividadAdapter.loadObjects();

                listarActividadAdapter.notifyDataSetChanged();

                Log.d(TAG, "Adapter has " + listarActividadAdapter.getCount() + " items");

                break;

            case R.id.filtroEstadales:
                // Generate List Holder
                final AlertDialog filterDialogEstadales;
                AlertDialog.Builder builderEstadales = new AlertDialog.Builder(getActivity());
                builderEstadales.setTitle("Filtrar por estado");

                // Fill Holder with State List from String Array
                final ListView listViewDialogEstadales = new ListView(getActivity());
                final ArrayList<String> arrayListEstadales = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.Estados)));

                // Add element All.
                arrayListEstadales.add(0, "Todos");

                ArrayAdapter<String> stringArrayAdapterEstadales = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,arrayListEstadales);
                listViewDialogEstadales.setAdapter(stringArrayAdapterEstadales);
                builderEstadales.setView(listViewDialogEstadales);

                // Show Dialog
                filterDialogEstadales = builderEstadales.create();
                filterDialogEstadales.show();

                listViewDialogEstadales.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        filterDialogEstadales.dismiss();
                        // Request List to filter
                        // TODO: Create Progress Dialog

                        Log.d(TAG, "Filtering by State");

                        listarActividadAdapter.clear();

                        listarActividadAdapter = new ListarActividadAdapter(getContext(),likes, "estado=" + listViewDialogEstadales.getItemAtPosition(position).toString());

                        listView.setAdapter(listarActividadAdapter);

                        listarActividadAdapter.loadObjects();

                        listarActividadAdapter.notifyDataSetChanged();

                        Log.d(TAG, "Adapter has " + listarActividadAdapter.getCount() + " items");


                    }
                });

                return true;

            case R.id.filtroMeGusta:

                // Request List to filter
                // TODO: Create Progress Dialog

                listarActividadAdapter.clear();

                listarActividadAdapter = new ListarActividadAdapter(getContext(),likes,"meGusta=true");

                listView.setAdapter(listarActividadAdapter);

                listarActividadAdapter.loadObjects();

                listarActividadAdapter.notifyDataSetChanged();

                Log.d(TAG, "Adapter has " + listarActividadAdapter.getCount() + " items");

                break;

            case R.id.filtroPropios:

                // Request List to filter
                // TODO: Create Progress Dialog
                if(currentUser != null){

                    listarActividadAdapter.clear();

                    listarActividadAdapter = new ListarActividadAdapter(getContext(),likes,"propios="+currentUser.getUsername());

                    listView.setAdapter(listarActividadAdapter);

                    listarActividadAdapter.loadObjects();

                    listarActividadAdapter.notifyDataSetChanged();

                    Log.d(TAG, "Adapter has " + listarActividadAdapter.getCount() + " items");

                }
                break;

            case R.id.filtroTodas:

                    listarActividadAdapter.clear();

                    listarActividadAdapter = new ListarActividadAdapter(getContext(),likes);

                    listView.setAdapter(listarActividadAdapter);

                    listarActividadAdapter.loadObjects();

                    listarActividadAdapter.notifyDataSetChanged();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

}
