package com.example.usuario.soyactivista.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.ParseUser;


import java.util.ArrayList;
import java.util.Arrays;

import logica.TextHelpers;
import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by Luis Adrian on 24/02/2016.
 */
public class FragmentDirectorio extends Fragment {

    // Log TAG
    private String TAG = "FragmentDirectorio";

    // Data Holders
    private String [] directorioComite = null;
    private ArrayAdapter<String> adapter;
    private ListView listView;
    private ParseUser currentUser = ParseUser.getCurrentUser();

    // Vars to populate list
    private int userLevel = 0;
    private int currentLevel = 0;
    private int desiredLevel = 0;
    private String estado;
    private String municipio;
    private String parroquia;
    private String lastSelectedType;
    private String lastSelectedValue;

    private Boolean seleccionComite = true;


    // Progress Dialog For Filtering/Retrieving Users
    ProgressDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        // Inflate View
        View view = inflater.inflate(R.layout.fragment_directorio_comite, container, false);

        // Initialize list view
        listView = (ListView) view.findViewById(R.id.estadalListView);

        // Calculate which options to show
        String [] comites = getActivity().getResources().getStringArray(R.array.comites);

        userLevel = TextHelpers.comiteToInt(currentUser.getString("comite"));
        Log.d(TAG,"Setting user level: Comite "+currentUser.getString("comite")+" Level:"+userLevel);

        int showLevel = userLevel;

        if (userLevel > 0) // Minus 1 to be able to show 1 more level of comms.
            showLevel--;

        String [] comitesFiltrados = Arrays.copyOfRange(comites,showLevel,comites.length);

        ArrayList<String> lista = new ArrayList<String>(Arrays.asList(comitesFiltrados)); // Trying to circunvent crit error on adapter init wth array

        Log.d(TAG,"Comites Filtrados contains: "+Arrays.toString(comitesFiltrados) );

        adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,lista);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                Log.d(TAG,"Element Selected "+adapter.getItem(position));

                if (seleccionComite){ // Check if it is first selection
                    primerPasoDirectorio(position);
                }
                else{
                    // Save Selection
                    guardarSeleccion(position);

                    // Load Next Step
                    siguientePasoDirectorio();
                }
            }
        });

        // Let the fragment know we will be loading some options for this fragment

        return view;
    }

    private void guardarSeleccion(int posicion) {
        int passLevel = currentLevel-1;
        Log.d(TAG,"SAving Selection for level "+passLevel);
        lastSelectedType = TextHelpers.comiteToString(passLevel);
        lastSelectedValue = adapter.getItem(posicion);

        switch (lastSelectedType){
            case "Estadal":
                estado = adapter.getItem(posicion);
                break;
            case "Municipal":
                municipio = adapter.getItem(posicion);
                break;
            case "Parroquial":
                parroquia = adapter.getItem(posicion);
                break;
            default:
                Log.d(TAG,"Defaulting when saving selection.");
                break;
        }

    }

    private void primerPasoDirectorio(int position) {
        seleccionComite = false;
        desiredLevel = position;

        Log.d(TAG,"Initializing Levels. UserLevel:"+userLevel+" Desired Level:"+desiredLevel);
        // Check if needs to show users now
        if ( desiredLevel < userLevel ){
            // Show Users
            // Set Arguments
            Bundle data = setFilterBundle();
            // Redirect View to next Fragment
            Fragment fragment = new FragmentListarUsuariosConversacion();
            fragment.setArguments(data);
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .commit();
        }
        else{
            // Initialize Current Level
            if( desiredLevel == userLevel){
                currentLevel = desiredLevel;
            }
            else{
                currentLevel = userLevel+1;
            }

            lastSelectedType = "comite";
            lastSelectedValue = TextHelpers.comiteToString(desiredLevel);
            Log.d(TAG,"Initial Current Level:"+currentLevel);
            siguientePasoDirectorio();
        }

    }

    // Prepares Complete Bundle to Filter Users
    private Bundle setFilterBundle() {
        Bundle data = new Bundle();
        data.putString("comite",TextHelpers.comiteToString(desiredLevel));
        data.putString("estado", estado);
        data.putString("municipio", municipio);
        data.putString("parroquia",parroquia);
        data.putStringArrayList("conversacionesAbiertas",getArguments().getStringArrayList("conversacionesAbiertas"));
        return data;

    }

    private void siguientePasoDirectorio() {

        Log.d(TAG,"Routing Next Step. Current Level "+currentLevel+" Desired Level "+desiredLevel);

        if(currentLevel > desiredLevel || desiredLevel == 0 || desiredLevel == 4 || desiredLevel == 5 ){ // Show User List.
            // Set Arguments
            Bundle data = setFilterBundle();
            // Redirect View to next Fragment
            Fragment fragment = new FragmentListarUsuariosConversacion();
            fragment.setArguments(data);
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .commit();
        }
        else{ // List options

            Log.d(TAG,"Refreshing List");
            switch(currentLevel){
                case 1: // Comite Estadal
                    String [] estados = getActivity().getResources().getStringArray(R.array.Estados);
                    adapter.clear();
                    adapter.addAll(estados);
                    adapter.notifyDataSetChanged();
                    break;
                case 2: // Comite Municipal
                    if(estado == null)
                        estado = currentUser.getString("estado");
                    Log.d(TAG,"Looking for Estado:"+estado);
                    int idEstado = getResources().getIdentifier(TextHelpers.NormalizeResource(estado), "array", getActivity().getPackageName());
                    String [] municipios = getActivity().getResources().getStringArray(idEstado);
                    adapter.clear();
                    adapter.addAll(municipios);
                    adapter.notifyDataSetChanged();
                    break;
                case 3: // Comite Parroquial
                    if(estado == null)
                        estado = currentUser.getString("estado");
                    if(municipio == null)
                        municipio = currentUser.getString("municipio");
                    Log.d(TAG,"Looking for Municipio:"+municipio+" of Estado:"+estado);
                    String recurso = TextHelpers.NormalizeResource(estado)+"_"+TextHelpers.NormalizeResource(municipio);
                    Log.d(TAG,"Looking for resource:"+recurso);
                    int idMunicipio = getResources().getIdentifier(recurso, "array", getActivity().getPackageName());
                    String [] parroquias = getActivity().getResources().getStringArray(idMunicipio);
                    adapter.clear();
                    adapter.addAll(parroquias);
                    adapter.notifyDataSetChanged();
                    break;
                default:
                    Log.e(TAG,"Error List Refresh Defaulted");
                    break;
            }
            currentLevel = currentLevel+1;

        }
    }
}
