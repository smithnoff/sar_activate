package com.example.usuario.soyactivista.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.ParseUser;

import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by Luis Adrian on 24/02/2016.
 */
public class FragmentDirectorioComite extends Fragment {

    // Log TAG
    private String TAG = "FragmentListarUsuarioConversacion";

    // Data Holders
    private String [] directorioComite = null;
    private ArrayAdapter<String> adapter;
    private ListView listView;

    private ParseUser currentUser = ParseUser.getCurrentUser();

    // Progress Dialog For Filtering/Retrieving Users
    ProgressDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        // Inflate View
        View view = inflater.inflate(R.layout.fragment_directorio_comite, container, false);

        // Initialize list view
        listView = (ListView) view.findViewById(R.id.estadalListView);


            final String comite = currentUser.getString("comite");
            int nivel = getNivel(comite);
            String districtResourceName = "Comite"+nivel;//+nivel;
            int districtId = getResources().getIdentifier(districtResourceName, "array", getActivity().getPackageName());
            directorioComite= getActivity().getResources().getStringArray(districtId);
            adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, directorioComite);
            listView.setAdapter(adapter);



        currentUser = ParseUser.getCurrentUser();



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {


                 if(position<2) {
                     adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.Estados));
                     listView.setAdapter(adapter);
                     listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                         @Override
                         public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                          //   int arrayId = getResources().getIdentifier(listView.getSelectedItem().toString(), "array", getActivity().getPackageName());
                           //  Toast.makeText(getActivity(), "id: " + arrayId, Toast.LENGTH_SHORT).show();

                             // adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, getResources().getStringArray(arrayId));
                             //  listView.setAdapter(adapter);

                         }
                     });
                 }
            }
        });

        // Let the fragment know we will be loading some options for this fragment

        return view;
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
