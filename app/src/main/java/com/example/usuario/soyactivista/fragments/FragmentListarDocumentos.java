package com.example.usuario.soyactivista.fragments;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import logica.ActivityPantallaMenu;
import logica.ListarDocumentosParseAdapter;
import soy_activista.quartzapp.com.soy_activista.R;

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


}
