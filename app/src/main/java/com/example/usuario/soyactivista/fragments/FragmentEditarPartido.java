package com.example.usuario.soyactivista.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import logica.Selector_de_Tema;
import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by Brahyam on 30/11/2015.
 */
public class FragmentEditarPartido extends Fragment {

    private static final String TAG = "FragEditarPartido";

    private int colorChecked;
    private EditText editNombrePartido;
    private  View colorAnterior;
    private Button guardarTema;
    private View brown, red, orange, yellow, green, blue, purple, indigo;
    private String nombrePartidonull;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Choose fragment to inflate
        View v = inflater.inflate(R.layout.fragment_editar_partido, container, false);

        // Assign Holders
        editNombrePartido = (EditText) v.findViewById(R.id.editNombrePartido);
        brown = v.findViewById(R.id.themeBrown);
        red =  v.findViewById(R.id.themeRed);
        orange = v.findViewById(R.id.themeOrange);
        yellow =  v.findViewById(R.id.themeYellow);
        green = v.findViewById(R.id.themeGreen);
        blue = v.findViewById(R.id.themeBlue);
        purple = v.findViewById(R.id.themePurple);
        indigo = v.findViewById(R.id.themeDefault);

        guardarTema= (Button) v.findViewById(R.id.btnguardarTema);

        // Load current name
        editNombrePartido.setText(Selector_de_Tema.getNombrePartido());

        // Load current color
        switch (Selector_de_Tema.getTema()){
            case 0:
                indigo.setBackground(getResources().getDrawable(R.drawable.circuloindigo));
                colorAnterior = indigo;
                break;
            case 1:
                blue.setBackground(getResources().getDrawable(R.drawable.circuloazul));
                colorAnterior = blue;
                break;
            case 2:
                brown.setBackground(getResources().getDrawable(R.drawable.circulomarron));
                colorAnterior = brown;
                break;
            case 3:
                red.setBackground(getResources().getDrawable(R.drawable.circulorojo));
                colorAnterior = red;
                break;
            case 4:
                orange.setBackground(getResources().getDrawable(R.drawable.circulonaranja));
                colorAnterior = orange;
                break;
            case 5:
                yellow.setBackground(getResources().getDrawable(R.drawable.circuloamarillo));
                colorAnterior = yellow;
                break;
            case 6:
                purple.setBackground(getResources().getDrawable(R.drawable.circulopurpura));
                colorAnterior = purple;
                break;
            case 7:
                green.setBackground(getResources().getDrawable(R.drawable.circuloverde));
                colorAnterior = green;
                break;
            default:
                indigo.setBackground(getResources().getDrawable(R.drawable.circuloindigo));
                colorAnterior = indigo;
                break;
        }

        nombrePartidonull = editNombrePartido.getText().toString();


        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(final View clickedColor) {
                colorChecked = clickedColor.getId();

                if(colorAnterior == null)
                    colorAnterior = clickedColor;

                if(clickedColor.getId()==R.id.themeBrown) {
                    clickedColor.setBackground(getResources().getDrawable(R.drawable.circulomarron));
                }else {
                    if(colorAnterior.getId()==R.id.themeBrown)
                        colorAnterior.setBackground(getResources().getDrawable(R.drawable.circulobr));
                }
                if(clickedColor.getId()==R.id.themeOrange) {
                    clickedColor.setBackground(getResources().getDrawable(R.drawable.circulonaranja));
                }else {
                    if(colorAnterior.getId()==R.id.themeOrange)
                        colorAnterior.setBackground(getResources().getDrawable(R.drawable.circulon));
                }
                if(clickedColor.getId()==R.id.themeBlue) {
                    clickedColor.setBackground(getResources().getDrawable(R.drawable.circuloazul));
                }else {
                    if(colorAnterior.getId()==R.id.themeBlue)
                        colorAnterior.setBackground(getResources().getDrawable(R.drawable.circuloaz));
                }
                if(clickedColor.getId()==R.id.themeRed) {
                    clickedColor.setBackground(getResources().getDrawable(R.drawable.circulorojo));
                }else {
                    if(colorAnterior.getId()==R.id.themeRed)
                        colorAnterior.setBackground(getResources().getDrawable(R.drawable.circulor));
                }
                if(clickedColor.getId()==R.id.themeGreen) {
                    clickedColor.setBackground(getResources().getDrawable(R.drawable.circuloverde));
                }else {
                    if(colorAnterior.getId()==R.id.themeGreen)
                        colorAnterior.setBackground(getResources().getDrawable(R.drawable.circulov));
                }
                if(clickedColor.getId()==R.id.themeYellow) {
                    clickedColor.setBackground(getResources().getDrawable(R.drawable.circuloamarillo));
                }else {
                    if(colorAnterior.getId()==R.id.themeYellow)
                        colorAnterior.setBackground(getResources().getDrawable(R.drawable.circuloam));
                }
                if(clickedColor.getId()==R.id.themePurple) {
                    clickedColor.setBackground(getResources().getDrawable(R.drawable.circulopurpura));
                }else {
                    if(colorAnterior.getId()==R.id.themePurple)
                        colorAnterior.setBackground(getResources().getDrawable(R.drawable.circulop));
                }
                if(clickedColor.getId()==R.id.themeDefault) {
                    DialogColor fragment1 = new DialogColor();
                    fragment1.show(getFragmentManager(),"");
                    clickedColor.setBackground(getResources().getDrawable(R.drawable.circuloindigo));
                }else {
                    if(colorAnterior.getId()==R.id.themeDefault)
                        colorAnterior.setBackground(getResources().getDrawable(R.drawable.circulo));
                }
                colorAnterior = clickedColor;
            }
        };

        red.setOnClickListener(clickListener);
        indigo.setOnClickListener(clickListener);
        orange.setOnClickListener(clickListener);
        blue.setOnClickListener(clickListener);
        green.setOnClickListener(clickListener);
        brown.setOnClickListener(clickListener);
        purple.setOnClickListener(clickListener);
        yellow.setOnClickListener(clickListener);


        guardarTema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //select de theme color

                if(editNombrePartido.getText().toString().trim().equals("") || editNombrePartido.getText().toString().trim()==null )
                {
                    Toast.makeText(getActivity(), "El nombre de partido no puede estar vacio.", Toast.LENGTH_LONG).show();
                    editNombrePartido.requestFocus();

                }else

                switch(colorChecked)
                {
                    case R.id.themeBrown:
                        Selector_de_Tema.changeToTheme(getActivity(), Selector_de_Tema.BROWN, editNombrePartido.getText().toString());
                        break;
                    case R.id.themeBlue:
                        Selector_de_Tema.changeToTheme(getActivity(), Selector_de_Tema.BLUE,editNombrePartido.getText().toString());
                        break;
                    case R.id.themeRed:
                        Selector_de_Tema.changeToTheme(getActivity(), Selector_de_Tema.RED,editNombrePartido.getText().toString());
                        break;
                    case R.id.themeDefault:
                        Selector_de_Tema.changeToTheme(getActivity(), Selector_de_Tema.DEFAULT,editNombrePartido.getText().toString());
                        break;
                    case R.id.themeOrange:
                        Selector_de_Tema.changeToTheme(getActivity(), Selector_de_Tema.ORANGE,editNombrePartido.getText().toString());
                        break;
                    case R.id.themeGreen:
                        Selector_de_Tema.changeToTheme(getActivity(), Selector_de_Tema.GREEN,editNombrePartido.getText().toString());
                        break;
                    case R.id.themePurple:

                        Selector_de_Tema.changeToTheme(getActivity(), Selector_de_Tema.PURPLE, editNombrePartido.getText().toString());
                        break;
                    case R.id.themeYellow:
                        Selector_de_Tema.changeToTheme(getActivity(), Selector_de_Tema.YELLOW,editNombrePartido.getText().toString());
                        break;
                    default:
                        Selector_de_Tema.changeToTheme(getActivity(), Selector_de_Tema.getTema(),editNombrePartido.getText().toString());
                        break;
                }
            }
        });


        return v;
    }


}