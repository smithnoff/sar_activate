package com.example.usuario.soyactivista.fragments;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import soy_activista.quartzapp.com.soy_activista.R;
/**
 * Created by Luis Adrian on 19/01/2016.
 */
public class FragmentTabTopEstados extends Fragment {

    public FragmentTabTopEstados() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tab_top_estados, container, false);
    }
}
