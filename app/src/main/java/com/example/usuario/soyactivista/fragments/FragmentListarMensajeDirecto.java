package com.example.usuario.soyactivista.fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.List;

import logica.ListarMensajeDirectoParseAdapter;
import logica.SqliteManager;
import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by darwin on 16/12/2015.
 */
public class FragmentListarMensajeDirecto extends Fragment {

    private static final String TAG = "FListarMensajeDirecto";

    View view;
    private ListarMensajeDirectoParseAdapter listarMensajeDirectoParseAdapter;
    private ProgressDialog progressDialog;
    private ListView listView;
    ProgressDialog dialog;
    public SqliteManager manager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG,"Creating View Detalle Conversacion");
        // Inflate View
        view = inflater.inflate(R.layout.fragment_listar_mensaje_directo, container, false);

        // Initialize main ParseQueryAdapter
        listarMensajeDirectoParseAdapter = new ListarMensajeDirectoParseAdapter(this.getContext(),getArguments().getString("conversacionId"));
        Log.d(TAG,"Mensaje Adapter contains: "+listarMensajeDirectoParseAdapter.getCount()+" items");
          manager=new SqliteManager(getContext());
        // Initialize list view
        listView = (ListView) view.findViewById(R.id.mensajesDirectosListView);

        // Show loading messages dialog.
        progressDialog = ProgressDialog.show(getContext(),"Buscando Mensajes","Cargando",true);
        listarMensajeDirectoParseAdapter.addOnQueryLoadListener(new ParseQueryAdapter.OnQueryLoadListener<ParseObject>() {
            @Override
            public void onLoading() {
                if(progressDialog == null)
                    progressDialog = ProgressDialog.show(getContext(),"Buscando Mensajes","Cargando",true);
            }

            @Override
            public void onLoaded(List<ParseObject> objects, Exception e) {
                progressDialog.dismiss();
            }
        });

        listarMensajeDirectoParseAdapter.clear();
        listView.setAdapter(listarMensajeDirectoParseAdapter);
        listarMensajeDirectoParseAdapter.loadObjects();
        Log.d(TAG, "Mensaje Adapter loaded objects. Now contains: " + listarMensajeDirectoParseAdapter.getCount() + " items");

        // Handle Item OnClick Events
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Store data in bundle to send to next fragment
                ParseObject mensaje = (ParseObject) listView.getItemAtPosition(position);
                ParseUser autor = mensaje.getParseUser("autor");
                ParseFile adjunto;
                ParseGeoPoint ubicacion;

                adjunto = mensaje.getParseFile("adjunto");
                ubicacion = mensaje.getParseGeoPoint("ubicacion");


                SimpleDateFormat format = new SimpleDateFormat("HH:mm dd/MM/yyyy");

                Bundle datos = new Bundle();
                datos.putString("id", mensaje.getObjectId());
                datos.putString("nombre", autor.getString("nombre") + " " + autor.getString("apellido"));
                datos.putString("estado",autor.getString("estado"));
                datos.putString("municipio", autor.getString("municipio"));
                datos.putString("texto", mensaje.getString("texto"));
                datos.putString("fechaCreacion", format.format(mensaje.getCreatedAt()));
                datos.putString("autor", autor.getObjectId());
                datos.putBoolean("directo", true);
                datos.putString("conversacionId",getArguments().getString("conversacionId"));

                // Check if images are null and save URLs
                if (adjunto != null){
                    datos.putString("nombreAdjunto",adjunto.getName());
                    datos.putString("adjunto", adjunto.getUrl());
                }

                // Check if location available
                if (ubicacion != null){
                    Log.d("ENVIO", ubicacion.toString());
                    datos.putString("ubicacion", String.valueOf(ubicacion.getLatitude())+","+String.valueOf(ubicacion.getLongitude()));
                }

                // Redirect View to next Fragment
                Fragment fragment = new FragmentDetalleMensaje();
                fragment.setArguments(datos);
                getFragmentManager()
                        .beginTransaction()
                        .replace(((ViewGroup)getView().getParent()).getId(), fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        FloatingActionButton botonCrearMensaje = (FloatingActionButton) view.findViewById(R.id.botonCrearMensaje);

        botonCrearMensaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle datos = new Bundle();
                datos.putString("conversacionId",getArguments().getString("conversacionId"));
                datos.putString("receptorId", getArguments().getString("receptorId"));
                datos.putBoolean("existeConversacion",true);

                // Redirect View to next Fragment
                Fragment fragment = new FragmentCrearMensajeDirecto();
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
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_listar_mensaje_directo, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
        builder.setTitle("Confirmar");
        switch (item.getItemId()) {


              case R.id.guardarConversacion:
                  builder.setMessage("¿Está seguro que desea Guardar la conversación?");

                  builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {

                      public void onClick(DialogInterface dialogo, int which) {
                          dialogo.dismiss();
                          android.app.AlertDialog.Builder builder2 = new android.app.AlertDialog.Builder(getActivity());
                         //TODO: verificar que parametro es null al enviarlo a la base de datos
                         for (int i=0;i<listView.getCount();i++  ){

                             ParseObject mensaje = (ParseObject) listView.getItemAtPosition(i);
                             String valores="autor: "+mensaje.getParseUser("autor").getString("nombre").toString()+
                                     "\nadjunto: "+//mensaje.getParseFile("adjunto").toString()+
                                     "\nconversa"+getArguments().getString("conversacionId")+
                                     "\nconversaid: "+  mensaje.getObjectId()+
                                     "\nUbicacion: "+//mensaje.getString("ubicacion").toString()
                                     "\nMnsaj. "+mensaje.getString("texto")+
                                     "\ncreado. " + mensaje.getCreatedAt().toString();
                             builder2.setMessage(valores).create().show();
                          //   hacer metodo para insertar en bd;


                         }
                          Toast.makeText(getContext(), "Conversación Guardada.", Toast.LENGTH_SHORT).show();

                          // Redirect user to conversation list
                          Fragment fragment = new FragmentListarConversacion();
                          getFragmentManager()
                                  .beginTransaction()
                                  .replace(((ViewGroup)getView().getParent()).getId(), fragment)
                                  .commit();
                      }

                  });

                  builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {

                      @Override
                      public void onClick(DialogInterface dialogo, int which) {
                          // Do nothing
                          dialogo.dismiss();
                      }
                  });

                  android.app.AlertDialog alert2 = builder.create();
                  alert2.show();





                  return true;

            case R.id.eliminarConversacion:
                // Ask for confirmation
                // On Click listener to Eliminated Message

                    builder.setMessage("¿Está seguro que desea eliminar la conversación?");

                    builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialogo, int which) {
                            dialogo.dismiss();

                            // Get current User
                            ParseUser currentUser = ParseUser.getCurrentUser();

                            // Make COnversation without Data for query/eliminate
                            ParseObject conversacion = ParseObject.createWithoutData("Conversacion",getArguments().getString("conversacionId"));

                            // Query my conversations
                            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("ParticipanteConversacion");
                            query.whereEqualTo("conversacion",conversacion);
                            query.whereEqualTo("usuario",currentUser);
                            query.findInBackground(new FindCallback<ParseObject>() {
                                @Override
                                public void done(List<ParseObject> objects, ParseException e) {
                                    if (e == null) {
                                        if (objects.size() > 0) {
                                            ParseObject participante;
                                            for (int i = 0; i < objects.size(); i++) {
                                                participante = objects.get(i);
                                                participante.deleteInBackground();
                                            }
                                        }

                                        Toast.makeText(getContext(), "Conversación eliminada.", Toast.LENGTH_SHORT).show();

                                        // Redirect user to conversation list
                                        Fragment fragment = new FragmentListarConversacion();
                                        getFragmentManager()
                                                .beginTransaction()
                                                .replace(((ViewGroup)getView().getParent()).getId(), fragment)
                                                .commit();

                                    } else {
                                        Toast.makeText(getContext(), "Error. " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


                        }

                    });

                    builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialogo, int which) {
                            // Do nothing
                            dialogo.dismiss();
                        }
                    });

                    android.app.AlertDialog alert = builder.create();
                    alert.show();


                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
