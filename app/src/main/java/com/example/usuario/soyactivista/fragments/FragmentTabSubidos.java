package com.example.usuario.soyactivista.fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import logica.Documento;
import logica.ErrorCodeHelpers;
import logica.ListarDocumentosSubidosAdapter;
import logica.ListarUsuarioAdapter;
import logica.Usuario;
import soy_activista.quartzapp.com.soy_activista.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentTabSubidos extends Fragment {

    private String TAG = "FragmentTabSubidos";

    private TextView noItems;
    private ListarDocumentosSubidosAdapter listarDocumentosSubidosAdapter;
    private ListView listView;
    private ArrayList<Documento> documentoArrayList = new ArrayList<>();

    ProgressDialog dialog;

    public FragmentTabSubidos() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab_subidos, container, false);

        // Initialize list view
        listView = (ListView) view.findViewById(R.id.documentoListView);

        noItems = (TextView) view.findViewById(R.id.listaVacia);

        // If adapter is null Initialize list and set adapter to view
        if(listarDocumentosSubidosAdapter == null){
            Log.d(TAG, "Array Adapter is null");
            // Define message to show when empty
            listView.setEmptyView(noItems);
            initializeList(documentoArrayList);
        }
        // List Already contains elements/ Just set adapter to view
        else{
            Log.d(TAG, "Array Adapter is OK with " + listarDocumentosSubidosAdapter.getCount()+" elements");
            // Add Elements to List and reset adapter
            listView.setAdapter(listarDocumentosSubidosAdapter);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Documento doc = (Documento) listView.getItemAtPosition(position);
                ParseFile file = doc.getDocumento();
                String url = file.getUrl();
                Toast.makeText(getContext(),"url: " + url,Toast.LENGTH_LONG);
                //seePDFDetail(url);
                // Store data in bundle to send to next fragment


                Bundle datos = new Bundle();
                datos.putString("titulo", doc.getTitulo());
                datos.putString("descripcion", doc.getDescripcion());
                datos.putString("url", url);


                // Redirect View to next Fragment
                Fragment fragment = new FragmentDetalleDocumento();
                fragment.setArguments(datos);
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }

    public void initializeList(final ArrayList<Documento> list){
        dialog = ProgressDialog.show(getContext(), "Buscando Documentos", "Cargando", true);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Documento");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> object, ParseException e) {
                if (e == null) { //no hay error
                    Documento doc;
                    for (int i = 0; i < object.size(); i++) {
                        doc = new Documento();
                        doc.setTitulo(object.get(i).getString("titulo"));
                        doc.setDescripcion(object.get(i).getString("descripcion"));
                        doc.setDocumento(object.get(i).getParseFile("adjunto"));
                        list.add(doc);
                    }
                    Log.d(TAG, "List have " + list.size() + " items.");
                    listarDocumentosSubidosAdapter = new ListarDocumentosSubidosAdapter(getActivity(), list);
                    listView.setAdapter(listarDocumentosSubidosAdapter);
                    dialog.dismiss();

                } else {
                    dialog.dismiss();
                    Toast.makeText(getActivity(), ErrorCodeHelpers.resolveErrorCode(e.getCode()), Toast.LENGTH_LONG).show();
                    Log.d(TAG, "Error al buscar documentos. " + e.getMessage());
                }
            }
        });

    }

    public View.OnClickListener seePDFDetail(final String url){
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
        };
        return listener;
    }

}
