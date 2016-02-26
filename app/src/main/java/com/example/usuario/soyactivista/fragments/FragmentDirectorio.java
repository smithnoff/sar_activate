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


import java.util.Arrays;

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
    private int userLevel;
    private int currentLevel;
    private int desiredLevel;

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
        final String [] comites = getActivity().getResources().getStringArray(R.array.comites);

        userLevel = comiteToInt(currentUser.getString("comite"));

        int showLevel = userLevel;

        if (userLevel > 0) // Minus 1 to be able to show 1 more level of comms.
            showLevel--;

        String [] comitesFiltrados = Arrays.copyOfRange(comites,showLevel,comites.length);

        Log.d(TAG,"Comites Filtrados contains: "+Arrays.toString(comitesFiltrados) );

        adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, comitesFiltrados);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                if (seleccionComite){ // Check if it is first selection ( Comite )
                    seleccionComite = false;
                    String comiteSeleccionado = adapter.getItem(position);
                    desiredLevel = position;
                    siguientePasoDirectorio();
                }
                else{
                    siguientePasoDirectorio();
                }

            }
        });

        // Let the fragment know we will be loading some options for this fragment

        return view;
    }

    private void siguientePasoDirectorio() {

        // Check if needs to show users now
        if ( desiredLevel < userLevel ){
            adapter= new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.Estados));
           listView.setAdapter(adapter);
        }

    }

    // translates a comite string into int
    public int comiteToInt(String comite ){

        int result;
        switch (comite){
            case "Nacional":
                result = 0;
                break;
            case "Estadal":
                result = 1;
                break;
            case "Municipal":
                result = 2;
                break;
            case "Parroquial":
                result = 3;
                break;
            case "Registro":
                result = 4;
                break;
            case "Activista": // Same as Default
            default:
                result = 5;
                break;
        }

        return result;
    }


    public String comiteToString(int comite){

        String result;
        switch (comite){
            case 0:
                result = "Nacional";
                break;
            case 1:
                result = "Estadal";
                break;
            case 2:
                result = "Municipal";
                break;
            case 3:
                result = "Parroquial";
                break;
            case 4:
                result = "Registro";
                break;
            case 5: // Same as default
            default:
                result = "Activista";
                break;

        }
        return result;
    }



    public int getNivel(String comite)
    {
        int nivel=0;
        if(comite=="Nacional" || currentUser.getInt("rol")==1)
        {
            nivel = 5;
            return nivel;
        }
        else
        {
            switch (comite)
            {
                case "Estadal":
                    nivel = 4;
                    return nivel;
                case "Municipal":
                    nivel = 3;
                    return nivel;
                case "Parroquial":
                    nivel = 2;
                    return nivel;
                case "Activista":
                    nivel = 1;
                    return nivel;
            }

        }
        return nivel;
    }



}
