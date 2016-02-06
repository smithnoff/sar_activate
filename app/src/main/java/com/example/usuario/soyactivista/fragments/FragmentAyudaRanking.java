package com.example.usuario.soyactivista.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import logica.ColorHelpers;
import soy_activista.quartzapp.com.soy_activista.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentAyudaRanking extends Fragment {

    private View view;
    private Button backButton;
    private TextView helpText;
    private ImageView circulo8000;
    private ImageView circulo6000;
    private ImageView circulo4000;
    private ImageView circulo1000;
    private ImageView circulo0;


    public FragmentAyudaRanking() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_ayuda_ranking, container, false);

        // Set Holders
        backButton = (Button) view.findViewById(R.id.back_button);
        helpText = (TextView) view.findViewById(R.id.help_text);
        circulo8000 = (ImageView) view.findViewById(R.id.circulo_8000);
        circulo6000 = (ImageView) view.findViewById(R.id.circulo_6000);
        circulo4000 = (ImageView) view.findViewById(R.id.circulo_4000);
        circulo1000 = (ImageView) view.findViewById(R.id.circulo_1000);
        circulo0 = (ImageView) view.findViewById(R.id.circulo_0);

        if( getArguments() != null){
            // Is State Help. Change Text
            helpText.setText(R.string.ranking_state_help);
        }

        // Fill Circles

        // Get Primary Color
        TypedValue typedValue = new TypedValue();
        getContext().getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        int color = typedValue.data;

        circulo8000.setColorFilter(ColorHelpers.getGradient(color,8000));
        circulo6000.setColorFilter(ColorHelpers.getGradient(color,6000));
        circulo4000.setColorFilter(ColorHelpers.getGradient(color,4000));
        circulo1000.setColorFilter(ColorHelpers.getGradient(color,1000));
        circulo0.setColorFilter(ColorHelpers.getGradient(color,0));

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
