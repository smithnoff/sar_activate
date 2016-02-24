package com.example.usuario.soyactivista.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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
    private TextView comite;

    private ParseUser currentUser;

    // Progress Dialog For Filtering/Retrieving Users
    ProgressDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // Inflate View
        View view = inflater.inflate(R.layout.fragment_directorio_comite, container, false);

        // Initialize list view
        listView = (ListView) view.findViewById(R.id.estadalListView);

        directorioComite= getActivity().getResources().getStringArray(R.array.Comite);

        adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, directorioComite);
        listView.setAdapter(adapter);
        comite = (TextView)view.findViewById(R.id.valueDirectorio);

        currentUser = ParseUser.getCurrentUser();

        //Log.d(TAG, "List contains " + usuarioArrayList.size() + " elements");

        // Handle Item OnClick Events
        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                String directorio = comite.getText().toString();
                switch (directorio){
                    case "Estadal":

                            break;
                }
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
        });*/

        // Let the fragment know we will be loading some options for this fragment

        return view;
    }

    /*public void initializeList(final ArrayList<String> list){

        List<ParseQuery<ParseUser>> queries = new ArrayList<ParseQuery<ParseUser>>();

        switch (currentUser.getString(("comite"))){
            case "Estadal":

                // Users from Comité Nacinal.
                ParseQuery<ParseUser> query0 = ParseUser.getQuery();
                query0.whereEqualTo("comite", "Nacional");
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

                // Users from Comité Parroquial from my State.
                ParseQuery<ParseUser> query3 = ParseUser.getQuery();
                query3.whereEqualTo("comite","Parroquial");
                query3.whereEqualTo("estado",currentUser.getString("estado"));
                queries.add(query3);

                // Users from Activista from my State.
                ParseQuery<ParseUser> query4 = ParseUser.getQuery();
                query4.whereEqualTo("comite","Activista");
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
                query22.whereEqualTo("municipio",currentUser.getString("municipio"));
                queries.add(query22);

                // Users from Activista from my Municipio.
                ParseQuery<ParseUser> query33 = ParseUser.getQuery();
                query33.whereEqualTo("comite","Activista");
                query33.whereEqualTo("estado",currentUser.getString("estado"));
                query33.whereEqualTo("municipio",currentUser.getString("municipio"));
                queries.add(query33);

                break;

            case "Parroquial":

                // Users from Comité Municipal from my State.
                ParseQuery<ParseUser> query000= ParseUser.getQuery();
                query000.whereEqualTo("comite","Municipal");
                query000.whereEqualTo("estado",currentUser.getString("estado"));
                query000.whereEqualTo("municipio",currentUser.getString("municipio"));
                queries.add(query000);

                // Users from Comité Parroquial from my State.
                ParseQuery<ParseUser> query111 = ParseUser.getQuery();
                query111.whereEqualTo("comite","Parroquial");
                query111.whereEqualTo("estado",currentUser.getString("estado"));
                query111.whereEqualTo("municipio",currentUser.getString("municipio"));
                queries.add(query111);

                //User from Activista from my Municipio
                ParseQuery<ParseUser> query222 = ParseUser.getQuery();
                query222.whereEqualTo("comite","Activista");
                query222.whereEqualTo("estado",currentUser.getString("estado"));
                query222.whereEqualTo("municipio",currentUser.getString("municipio"));
                queries.add(query222);

                //User from Activista from my Parroquia
                ParseQuery<ParseUser> query333 = ParseUser.getQuery();
                query333.whereEqualTo("comite","Activista");
                query333.whereEqualTo("estado",currentUser.getString("estado"));
                query333.whereEqualTo("municipio",currentUser.getString("municipio"));
                query333.whereEqualTo("parroquia",currentUser.getString("parroquia"));
                queries.add(query333);

                break;
            case "Activista":

                //Users from Comite Parroquial from my State
                ParseQuery<ParseUser> query0000 = ParseUser.getQuery();
                query0000.whereEqualTo("comite","Parroquial");
                query0000.whereEqualTo("estado",currentUser.getString("estado"));
                query0000.whereEqualTo("municipio",currentUser.getString("municipio"));
                queries.add(query0000);

                //User from Activista from my Municipio
                ParseQuery<ParseUser> query1111 = ParseUser.getQuery();
                query1111.whereEqualTo("comite","Activista");
                query1111.whereEqualTo("estado",currentUser.getString("estado"));
                query1111.whereEqualTo("municipio",currentUser.getString("municipio"));
                queries.add(query1111);

                //User from Activista from my Parroquia
                ParseQuery<ParseUser> query2222 = ParseUser.getQuery();
                query2222.whereEqualTo("comite","Activista");
                query2222.whereEqualTo("estado",currentUser.getString("estado"));
                query2222.whereEqualTo("municipio",currentUser.getString("municipio"));
                query2222.whereEqualTo("parroquia",currentUser.getString("parroquia"));
                queries.add(query2222);

                break;
            default:
                break;
        }

        ParseQuery<ParseUser> mainQuery;

        if(!queries.isEmpty())
            mainQuery = ParseQuery.or(queries);
        else
            mainQuery = ParseUser.getQuery();

        //Log.d(TAG, "Current user is: " + currentUser.getUsername());
        //mainQuery.whereNotContainedIn("objectId",conversacionesAbiertas);
       // mainQuery.whereEqualTo("eliminado", false);
       // mainQuery.whereNotEqualTo("objectId", currentUser.getObjectId());
        //mainQuery.findInBackground(new FindCallback<ParseUser>() {
            /*public void done(List<ParseUser> object, ParseException e) {
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
                    listarUsuarioJerarquiaAdapter = new ListarUsuarioJerarquiaAdapter(getActivity(), list);
                    listView.setAdapter(listarUsuarioJerarquiaAdapter);

                    // If no Search/Filter Argument initialize list, else filter.
                    if (getArguments() != null && getArguments().getString("busqueda") != null) {
                        listarUsuarioJerarquiaAdapter.getFilter().filter(getArguments().getString("busqueda"));
                    }

                    dialog.dismiss();

                } else {
                    dialog.dismiss();
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

    }*/

}
