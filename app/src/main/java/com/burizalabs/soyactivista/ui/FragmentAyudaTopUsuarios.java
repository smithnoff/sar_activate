package com.burizalabs.soyactivista.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.burizalabs.soyactivista.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentAyudaTopUsuarios extends Fragment {

    private View view;
    private Button backButton;
    private TextView helpText;


    public FragmentAyudaTopUsuarios() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_ayuda_top_usuarios, container, false);

        // Set Holders
        backButton = (Button) view.findViewById(R.id.back_button);
        helpText = (TextView) view.findViewById(R.id.help_text);

        if( getArguments() != null){
            // Is State Help. Change Text
            helpText.setText(R.string.top_users_state_help);
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Return to previous view
                getFragmentManager().popBackStack();
            }
        });

        return view;
    }

}
