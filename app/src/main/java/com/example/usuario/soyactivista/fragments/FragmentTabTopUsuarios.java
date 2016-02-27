package com.example.usuario.soyactivista.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import logica.ErrorCodeHelpers;
import logica.ListarTopUsuariosAdapter;
import logica.Usuario;
import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by Luis Adrian on 19/01/2016.
 */
public class FragmentTabTopUsuarios extends Fragment{
    private static final String TAG = "FragTopUsuarios";
    private ListarTopUsuariosAdapter adapter;
    private RecyclerView recyclerView;
    private List<Usuario> usuarioArrayList;
    private ParseUser currentUser = ParseUser.getCurrentUser();
    private RelativeLayout emptyLayout;
    private String entidad;
private TextView vacio;
    public FragmentTabTopUsuarios() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View v = inflater.inflate(R.layout.fragment_tab_top_usuarios, container, false);

        emptyLayout = (RelativeLayout)v.findViewById(R.id.emptyLayout);
        vacio= (TextView) v.findViewById(R.id.textViewempty);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        usuarioArrayList = new ArrayList<>();

        if(getArguments()!= null)
        {
            adapter = new ListarTopUsuariosAdapter(usuarioArrayList, "Municipio");
        }
        else
        {
            adapter = new ListarTopUsuariosAdapter(usuarioArrayList, "Estado");
        }

        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerListTopUsuarios);

        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(adapter);

        initializeList();


        /*if(adapter.getItemCount() == 0)
        {
            Toast.makeText(getActivity(),"Aqui entre",Toast.LENGTH_LONG);
            recyclerView.setVisibility(View.GONE);
            emptyLayout.setVisibility(View.VISIBLE);
        }
        else
        {
            recyclerView.setVisibility(View.VISIBLE);
            emptyLayout.setVisibility(View.GONE);
        }*/


        setHasOptionsMenu(true);

        return v;
    }

    // Initializes list and sets listView adapter to the newly created adapter.
    public void initializeList(){
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("eliminado",false);
        query.whereGreaterThan("puntosActivismo",0);
        query.setLimit(20);
        query.orderByDescending("puntosActivismo");

        // Check if State or Municipal Level
        if(getArguments() != null){
            if(getArguments().getString("estado") != null)
                query.whereEqualTo("estado",getArguments().getString("estado"));
        }

        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> object, ParseException e) {
                if (e == null) { //no hay error
                    Usuario usuario;
                    Log.d(TAG,object.size()+" users retrieved.");
                    if(object.size()<=0)
                        vacio.setVisibility(View.VISIBLE);
                    else
                        vacio.setVisibility(View.GONE);
                    for (int i = 0; i < object.size(); i++) {
                        usuario = new Usuario();
                        usuario.setNombre(object.get(i).getString("nombre"));
                        usuario.setApellido(object.get(i).getString("apellido"));
                        usuario.setCargo(object.get(i).getString("cargo"));
                        if(getArguments()!= null)
                        {
                            usuario.setMunicipio(object.get(i).getString("municipio"));
                        }
                        else
                        {
                            usuario.setMunicipio(object.get(i).getString("estado"));
                        }

                        usuario.setPuntosActivismo(object.get(i).getInt("puntosActivismo"));
                        usuario.setFoto(object.get(i).getParseFile("fotoPerfil"));
                        usuarioArrayList.add(usuario);
                    }

                    adapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(getActivity(), ErrorCodeHelpers.resolveErrorCode(e.getCode()), Toast.LENGTH_LONG).show();
                    Log.d(TAG, ErrorCodeHelpers.resolveLogErrorString(e.getCode(), e.getMessage()));
                }
            }
        });


    }


    // Inflates custom menu for fragment.
    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        // Inflate Custom Menu
        inflater.inflate(R.menu.menu_puntuaciones, menu);

        ParseUser currentUser = ParseUser.getCurrentUser();

        MenuItem reiniciarPuntuacion = menu.findItem(R.id.reiniciar_puntuacion);

        if(currentUser != null && currentUser.getInt("rol") == 1){
            reiniciarPuntuacion.setVisible(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.ayuda:
                Fragment fragment = new FragmentAyudaTopUsuarios();
                fragment.setArguments(getArguments());
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, fragment)
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.buscar_entidad:
                // Generate List Holder
                final android.support.v7.app.AlertDialog filterDialogEstadales;
                android.support.v7.app.AlertDialog.Builder builderEstadales = new android.support.v7.app.AlertDialog.Builder(getActivity());
                builderEstadales.setTitle("Seleccionar Estado");

                // Fill Holder with State List from String Array
                final ListView listViewDialogEstadales = new ListView(getActivity());
                final ArrayList<String> arrayListEstadales = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.Estados)));

                ArrayAdapter<String> stringArrayAdapterEstadales = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,arrayListEstadales);
                listViewDialogEstadales.setAdapter(stringArrayAdapterEstadales);
                builderEstadales.setView(listViewDialogEstadales);

                // Show Dialog
                filterDialogEstadales = builderEstadales.create();
                filterDialogEstadales.show();

                // Define on touch action.
                listViewDialogEstadales.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Bundle datos = new Bundle();
                        datos.putString("estado", listViewDialogEstadales.getItemAtPosition(position).toString());

                        Log.d(TAG, "Bundle Created with " + listViewDialogEstadales.getItemAtPosition(position).toString());

                        // Dismiss dialog
                        filterDialogEstadales.dismiss();

                        // Redirect View to next Fragment
                        Fragment fragment = new FragmentPuntuaciones();
                        // Store fragment in back stack if main fragment
                        if( getArguments() == null){
                            getActivity().getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.content_frame, fragment)
                                    .addToBackStack(null)
                                    .commit();
                        }
                        else{
                            getActivity().getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.content_frame, fragment)
                                    .commit();
                        }
                    }
                });
                break;

            case R.id.reiniciar_puntuacion:

                // Show Confirmation Dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Confirmar");
                builder.setMessage("¿Está seguro de que desea reiniciar la puntuación de todos los usuarios?");

                builder.setPositiveButton("Reiniciar", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int which) {

                        final ProgressDialog dialog1 = ProgressDialog.show(getActivity(), "", "Actualizando Usuarios.", true);
                        // Call cloud function
                        final HashMap<String, Object> params = new HashMap<>();
                        ParseCloud.callFunctionInBackground("resetScores", params, new FunctionCallback<Map<String, Object>>() {
                            @Override
                            public void done(Map<String, Object> response, ParseException e) {
                                if (response != null && response.get("status").toString().equals("OK")) {
                                    dialog1.dismiss();
                                    Toast.makeText(getActivity(), "Usuarios actualizados correctamente. Los cambios se reflejarán en la próxima actualización", Toast.LENGTH_SHORT).show();

                                    // Publish Notification of Activity Created.
                                    ParseObject mensaje = new ParseObject("Mensaje");
                                    mensaje.put("texto",currentUser.getString("nombre")+" ha reiniciado las puntuaciones de Activismo en la tabla de puntuaciones");
                                    mensaje.put("autor",currentUser);
                                    mensaje.put("reportado", false);
                                    mensaje.saveEventually();
                                } else {
                                    dialog1.dismiss();
                                    if (e != null) {
                                        Log.d(TAG, "Error " + e.getCode() + ":  " + e.getMessage());
                                        Toast.makeText(getActivity(), ErrorCodeHelpers.resolveErrorCode(e.getCode()), Toast.LENGTH_LONG).show();
                                    }

                                    if (response != null) {
                                        Log.d(TAG, "Error: " + response.get("code").toString() + " " + response.get("message").toString());
                                        Toast.makeText(getActivity(), ErrorCodeHelpers.resolveErrorCode(Integer.valueOf(response.get("code").toString())), Toast.LENGTH_LONG).show();
                                    }

                                    if (e == null && response == null) {
                                        Log.d(TAG, "Error: unknown error");
                                        Toast.makeText(getActivity(), "Error, por favor intente de nuevo mas tarde.", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        });
                    }
                });

                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                        dialog.dismiss();
                    }
                });

                // After Dialog is Completely defined - Show Dialog.
                AlertDialog alert = builder.create();
                alert.show();
                break;
        }


        return true;
    }
}
