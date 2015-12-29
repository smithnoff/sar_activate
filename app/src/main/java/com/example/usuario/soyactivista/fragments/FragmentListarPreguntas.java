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
import android.widget.Spinner;

import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.Arrays;

import logica.ListarActividadAdapter;
import logica.listarPreguntaParseAdapter;
import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by darwin on 28/12/2015.
 */
public class FragmentListarPreguntas extends Fragment {
    private ListView listView;
    private listarPreguntaParseAdapter mainAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate View
        View view = inflater.inflate(R.layout.fragment_listar_preguntas, container, false);

        // Initialize main ParseQueryAdapter
        mainAdapter = new listarPreguntaParseAdapter(this.getContext());

        // Initialize list view
        listView = (ListView)view.findViewById(R.id.listaPreguntas);

        mainAdapter.clear();
        listView.setAdapter(mainAdapter);
        mainAdapter.loadObjects();

        // Handle Item OnClick Events
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Store data in bundle to send to next fragment
                ParseObject actividad = (ParseObject)listView.getItemAtPosition(position);

                Bundle datos = new Bundle();
                datos.putString("id",actividad.getObjectId());
                datos.putString("pregunta",actividad.getString("pregunta"));
                datos.putString("opcion1",actividad.getString("opcion1"));
                datos.putString("opcion2",actividad.getString("opcion2"));
                datos.putString("opcion3",actividad.getString("opcion3"));
                datos.putString("opcion4",actividad.getString("opcion4"));
                datos.putString("dificultad",actividad.getString("dificultad"));
                datos.putString("puntaje", Integer.toString(actividad.getInt("puntaje")));
                datos.putString("tiempo", Integer.toString(actividad.getInt("tiempo")));

                // Redirect View to next Fragment
                Fragment fragment = new FragmentEditarPreguntas();
                fragment.setArguments(datos);
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });


        FloatingActionButton botonAgregarActividad =(FloatingActionButton)view.findViewById(R.id.botonAgregarPregunta);

        // Create new Activity Type Button
        botonAgregarActividad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new FragmentCrearPreguntas();
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
        inflater.inflate(R.menu.menu_listar_pregunta, menu);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()) {

            case R.id.filtroPuntaje:
                // Generate List Holder
                final AlertDialog alertDialogPuntaje;
                AlertDialog.Builder builderPuntaje = new AlertDialog.Builder(getActivity());
                builderPuntaje.setTitle("Filtrar por Puntaje");

                // Fill Holder with State List from String Array
                final ListView listViewDialogPuntaje = new ListView(getActivity());
                final ArrayList<String> arrayListEstatus = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.Puntuaciones)));

                ArrayAdapter<String> stringArrayAdapterPuntaje = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,arrayListEstatus);
                listViewDialogPuntaje.setAdapter(stringArrayAdapterPuntaje);
                builderPuntaje.setView(listViewDialogPuntaje);

                // Show Dialog
                alertDialogPuntaje = builderPuntaje.create();
                alertDialogPuntaje.show();

                 listViewDialogPuntaje.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                       alertDialogPuntaje.dismiss();
                        // Request List to filter
                        // TODO: Create Progress Dialog


                        mainAdapter.clear();

                        mainAdapter = new listarPreguntaParseAdapter(getContext(),"puntaje=" + listViewDialogPuntaje.getItemAtPosition(position).toString());

                        listView.setAdapter(mainAdapter);

                        mainAdapter.loadObjects();

                        mainAdapter.notifyDataSetChanged();



                    }
                });

                return true;


            case R.id.filtroNivel:

                // Generate List Holder
                final AlertDialog alertDialogNivel;
                AlertDialog.Builder builderNivel = new AlertDialog.Builder(getActivity());
                builderNivel.setTitle("Filtrar por Nivel");

                // Fill Holder with State List from String Array
                final ListView listViewDialogNivel = new ListView(getActivity());
                final ArrayList<String> arrayListNivel = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.Dificultad)));

                ArrayAdapter<String> stringArrayAdapterNivel = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,arrayListNivel);
                listViewDialogNivel.setAdapter(stringArrayAdapterNivel);
                builderNivel.setView(listViewDialogNivel);

                // Show Dialog
                alertDialogPuntaje = builderNivel.create();
                alertDialogPuntaje.show();

                listViewDialogNivel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        alertDialogPuntaje.dismiss();
                        // Request List to filter
                        // TODO: Create Progress Dialog

                        //          Log.d(TAG, "Filtering by Status");

                        mainAdapter.clear();

                        mainAdapter = new listarPreguntaParseAdapter(getContext(), "nivel=" + listViewDialogNivel.getItemAtPosition(position).toString());

                        listView.setAdapter(mainAdapter);

                        mainAdapter.loadObjects();

                        mainAdapter.notifyDataSetChanged();

                        // Log.d(TAG, "Adapter has " + mainAdapter.getCount() + " items");


                    }
                });

                return true;







            case R.id.filtroTodas:

                mainAdapter.clear();

                mainAdapter = new listarPreguntaParseAdapter(getContext());

                listView.setAdapter(mainAdapter);

                mainAdapter.loadObjects();

                mainAdapter.notifyDataSetChanged();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
   //  return true;
    }


}
