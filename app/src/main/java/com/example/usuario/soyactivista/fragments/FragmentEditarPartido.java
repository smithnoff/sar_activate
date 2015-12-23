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
    private  View vistaAntrior;
    private Button guardarTema;
    private View brown, red, orange, yellow, green, blue, purple, indigo;
          private String nombrePartidonull;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Choose fragment to inflate
        View v = inflater.inflate(R.layout.fragment_editar_partido, container, false);

        // Assign Holders
        editNombrePartido = (EditText) v.findViewById(R.id.editNombrePartido);
        brown = (View) v.findViewById(R.id.themeBrown);
        red = (View) v.findViewById(R.id.themeRed);
        orange = (View) v.findViewById(R.id.themeOrange);
        yellow = (View) v.findViewById(R.id.themeYellow);
        green = (View) v.findViewById(R.id.themeGreen);
        blue = (View) v.findViewById(R.id.themeBlue);
        purple = (View) v.findViewById(R.id.themePurple);
        indigo = (View) v.findViewById(R.id.themeDefault);

        editNombrePartido.setText(Selector_de_Tema.getNombrePartido());
         nombrePartidonull=editNombrePartido.getText().toString();
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(final View v) {



                    colorChecked = v.getId();
                    if(vistaAntrior==null)
                        vistaAntrior=v;

                    if(v.getId()==R.id.themeBrown) {
                        v.setBackground(getResources().getDrawable(R.drawable.circulomarron));
                    }else {
                        if(vistaAntrior.getId()==R.id.themeBrown)
                            vistaAntrior.setBackground(getResources().getDrawable(R.drawable.circulobr));
                    }
                    if(v.getId()==R.id.themeOrange) {
                        v.setBackground(getResources().getDrawable(R.drawable.circulonaranja));
                    }else {
                        if(vistaAntrior.getId()==R.id.themeOrange)
                            vistaAntrior.setBackground(getResources().getDrawable(R.drawable.circulon));
                    }
                    if(v.getId()==R.id.themeBlue) {
                        v.setBackground(getResources().getDrawable(R.drawable.circuloazul));
                    }else {
                        if(vistaAntrior.getId()==R.id.themeBlue)
                            vistaAntrior.setBackground(getResources().getDrawable(R.drawable.circuloaz));
                    }
                    if(v.getId()==R.id.themeRed) {
                        v.setBackground(getResources().getDrawable(R.drawable.circulorojo));
                    }else {
                        if(vistaAntrior.getId()==R.id.themeRed)
                            vistaAntrior.setBackground(getResources().getDrawable(R.drawable.circulor));
                    }
                    if(v.getId()==R.id.themeGreen) {
                        v.setBackground(getResources().getDrawable(R.drawable.circuloverde));
                    }else {
                        if(vistaAntrior.getId()==R.id.themeGreen)
                            vistaAntrior.setBackground(getResources().getDrawable(R.drawable.circulov));
                    }
                    if(v.getId()==R.id.themeYellow) {
                        v.setBackground(getResources().getDrawable(R.drawable.circuloamarillo));
                    }else {
                        if(vistaAntrior.getId()==R.id.themeYellow)
                            vistaAntrior.setBackground(getResources().getDrawable(R.drawable.circuloam));
                    }
                    if(v.getId()==R.id.themePurple) {
                        v.setBackground(getResources().getDrawable(R.drawable.circulopurpura));
                    }else {
                        if(vistaAntrior.getId()==R.id.themePurple)
                            vistaAntrior.setBackground(getResources().getDrawable(R.drawable.circulop));
                    }
                    if(v.getId()==R.id.themeDefault) {
                        v.setBackground(getResources().getDrawable(R.drawable.circuloindigo));
                    }else {
                        if(vistaAntrior.getId()==R.id.themeDefault)
                            vistaAntrior.setBackground(getResources().getDrawable(R.drawable.circulo));
                    }
                    vistaAntrior=v;


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
          guardarTema= (Button) v.findViewById(R.id.btnguardarTema);
        guardarTema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //select de theme color

                if(editNombrePartido.getText().toString().trim().equals("") || editNombrePartido.getText().toString().trim()==null )
                {
                    Toast.makeText(getActivity(), "El Nombre de Partido no puede ir vacio", Toast.LENGTH_LONG).show();
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
                        Selector_de_Tema.changeToTheme(getActivity(), Selector_de_Tema.PURPLE,editNombrePartido.getText().toString());
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