package com.burizalabs.soyactivista.ui;


import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseFile;
import com.parse.ParseObject;

import java.text.SimpleDateFormat;

import com.burizalabs.soyactivista.adapters.ListarDocumentosParseAdapter;
import com.burizalabs.soyactivista.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentListarDocumentos extends Fragment {


    private String TAG = "FragDocumentos";

    private View view;
    private ListView listView;
    private TextView emptyView;
    private ListarDocumentosParseAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        view = inflater.inflate(R.layout.fragment_listar_documentos, container, false);

        initList();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Store data in bundle to send to next fragment
                ParseObject documento = (ParseObject) listView.getItemAtPosition(position);
                ParseFile adjunto;

                adjunto = documento.getParseFile("adjunto");

                SimpleDateFormat format = new SimpleDateFormat("HH:mm dd/MM/yyyy");

                Bundle datos = new Bundle();
                datos.putString("id", documento.getObjectId());
                datos.putString("titulo", documento.getString("titulo"));
                datos.putString("descripcion", documento.getString("descripcion"));
                datos.putString("adjunto", adjunto.getUrl());
                datos.putString("nombreAdjunto", adjunto.getName());

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

        // Let the fragment know we will be loading some options for this fragment
        setHasOptionsMenu(true);

        return view;
    }

    private void initList() {
        listView = (ListView)view.findViewById(R.id.document_list);
        emptyView = (TextView)view.findViewById(R.id.empty_list);
        listView.setEmptyView(emptyView);
        adapter = new ListarDocumentosParseAdapter(getContext());
        listView.setAdapter(adapter);
        adapter.loadObjects();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_listar_documento, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.ver_descargas:
                startActivity(new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
