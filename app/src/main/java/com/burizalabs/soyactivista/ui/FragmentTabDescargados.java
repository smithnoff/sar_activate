package com.burizalabs.soyactivista.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.burizalabs.soyactivista.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentTabDescargados extends Fragment {


    public FragmentTabDescargados() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_tab_descargados, container, false);

    return v;
    }

}
