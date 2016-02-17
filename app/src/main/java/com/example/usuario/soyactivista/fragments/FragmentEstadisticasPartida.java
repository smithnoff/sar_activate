package com.example.usuario.soyactivista.fragments;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import logica.ErrorCodeHelpers;
import soy_activista.quartzapp.com.soy_activista.R;
import logica.AnimateCounter;

/**
 * Created by Luis Adrian on 29/12/2015.
 */
public class FragmentEstadisticasPartida extends Fragment {


    private static final String TAG = "FragEstadisticaPartida";
    private TextView valuePuntosConseguidos, valueRespuestasCorrectas, valueRespuestasIncorrectas;

    private RatingBar ratingBar;
    private Button buttonMenuPrincipal;

    private int puntosPartida, correctas, incorrectas;
    private String ptos,aciertos,fallados;

    private ImageView starOne, starTwo, starThree, noStarOne, noStarTwo, noStarThree;
    private int one, two, three, twoNull, threeNull;
    private View v;

    private int number = 0;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {


        //Choose fragment to inflate
        v = inflater.inflate(R.layout.fragment_estadistica_partida, container, false);

        final ParseUser currentUser = ParseUser.getCurrentUser();

        //Set Textview, Button and RatingBar
        valuePuntosConseguidos = (TextView)v.findViewById(R.id.valuePuntosConseguidos);
        valueRespuestasCorrectas = (TextView)v.findViewById(R.id.valueRespuestasCorrectas);
        valueRespuestasIncorrectas = (TextView)v.findViewById(R.id.valueRespuestasIncorrectas);

        starOne = (ImageView)v.findViewById(R.id.starOne);
        starTwo = (ImageView)v.findViewById(R.id.starTwo);
        starThree = (ImageView)v.findViewById(R.id.starThree);
        noStarOne = (ImageView)v.findViewById(R.id.starOneNull);
        noStarTwo = (ImageView)v.findViewById(R.id.starTwoNull);
        noStarThree = (ImageView)v.findViewById(R.id.starThreeNull);

        buttonMenuPrincipal = (Button)v.findViewById(R.id.buttonVolverMenuPrincipal);

        /*ratingBar = (RatingBar)v.findViewById(R.id.ratingBar);
        ratingBar.setEnabled(false);*/

        Integer puntos = getArguments().getInt("puntuacionPartida");
        Integer correctas = getArguments().getInt("respuestasCorrectas");

        // Check for below cero values.
        if(puntos < 0)
            puntos = 0;

        // Set Values
        //valuePuntosConseguidos.setText(puntos.toString());
        valueRespuestasCorrectas.setText(correctas.toString());
        Integer incorrectas = getArguments().getInt("totalPreguntas")- getArguments().getInt("respuestasCorrectas");
        valueRespuestasIncorrectas.setText(incorrectas.toString());

        // Load Rating
        if (correctas <= 2)
        {

            starOne.setVisibility(View.VISIBLE);
            noStarTwo.setVisibility(View.VISIBLE);
            noStarThree.setVisibility(View.VISIBLE);
            //Parameters for AnimationStar
            one = R.id.starOne;
            twoNull = R.id.starTwoNull;
            threeNull = R.id.starThreeNull;
            //Call function to animate
            AnimationStar(one, twoNull, threeNull);

        }
        else
        {
            if (correctas <= 5)
            {
                starOne.setVisibility(View.VISIBLE);
                starTwo.setVisibility(View.VISIBLE);
                noStarThree.setVisibility(View.VISIBLE);
                //Parameters for AnimationStar
                one = R.id.starOne;
                two = R.id.starTwo;
                threeNull = R.id.starThreeNull;
                //Call function to animate
                AnimationStar(one, two, threeNull);

            }
            else
            {
                starOne.setVisibility(View.VISIBLE);
                starTwo.setVisibility(View.VISIBLE);
                starThree.setVisibility(View.VISIBLE);
                //Parameters for AnimationStar
                one = R.id.starOne;
                two = R.id.starTwo;
                three = R.id.starThree;
                //Call function to animate
                AnimationStar(one, two, three);
            }
        }

        //Animated Points
        if(puntos == 0)
            valuePuntosConseguidos.setText(puntos.toString());
        else
            AnimarTexto(puntos,valuePuntosConseguidos);


        // Update user points
        currentUser.put("puntos",currentUser.getInt("puntos")+puntos);
        currentUser.saveEventually();

        // Update user stadistics
        ParseQuery<ParseObject> stadisticsQuery = ParseQuery.getQuery("EstadisticasUsuario");
        stadisticsQuery.whereEqualTo("usuario",currentUser);
        stadisticsQuery.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null && object != null) {
                    // Update stadistics.
                    object.put("partidas", object.getInt("partidas") + 1);

                    // Increment difficulty answers
                    switch (getArguments().getString("dificultad")) {
                        case "facil":
                            object.put("faciles", object.getInt("faciles") + getArguments().getInt("respuestasCorrectas"));
                            break;
                        case "intermedio":
                            object.put("intermedias", object.getInt("intermedias") + getArguments().getInt("respuestasCorrectas"));
                            break;
                        case "dificil":
                            object.put("dificiles", object.getInt("dificiles") + getArguments().getInt("respuestasCorrectas"));
                            break;
                        default:
                            object.put("faciles", object.getInt("faciles") + getArguments().getInt("respuestasCorrectas"));
                            break;
                    }
                    object.saveEventually();
                } else {
                    // Object not found
                    if (e.getCode() == 101) {
                        Log.d(TAG, "NO stadistics found for user");
                        // Create Stadistics and load
                        ParseObject estadisticas = new ParseObject("EstadisticasUsuario");
                        estadisticas.put("usuario", currentUser);
                        estadisticas.put("partidas", 1);
                        // Increment difficulty answers
                        switch (getArguments().getString("dificultad")) {
                            case "facil":
                                estadisticas.put("faciles", getArguments().getInt("respuestasCorrectas"));
                                estadisticas.put("intermedias", 0);
                                estadisticas.put("dificiles", 0);
                                break;
                            case "intermedio":
                                estadisticas.put("faciles", 0);
                                estadisticas.put("intermedias", getArguments().getInt("respuestasCorrectas"));
                                estadisticas.put("dificiles", 0);
                                break;
                            case "dificil":
                                estadisticas.put("faciles", 0);
                                estadisticas.put("intermedias", 0);
                                estadisticas.put("dificiles", getArguments().getInt("respuestasCorrectas"));
                                break;
                            default:
                                estadisticas.put("faciles", getArguments().getInt("respuestasCorrectas"));
                                estadisticas.put("intermedias", 0);
                                estadisticas.put("dificiles", 0);
                                break;
                        }

                        estadisticas.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    Log.d(TAG, "New Stadistics saved correctly");
                                } else {
                                    Log.d(TAG, "Error Saving New Stadistics: " + e.getMessage() + " " + e.getCode());
                                }

                            }
                        });

                    } else {
                        // Another error
                        Log.d(TAG, "Error: " + e.getMessage() + " " + e.getCode());
                        Toast.makeText(getActivity(), ErrorCodeHelpers.resolveErrorCode(e.getCode()), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });


        buttonMenuPrincipal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new FragmentTriviaPrincipal();
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, fragment)
                        .commit();
            }
        });
        return v;
    }

    private void AnimarTexto(int numero, TextView text){
        AnimateCounter animateCounterWrong = new AnimateCounter.Builder(text)
                .setCount(0, numero)
                .setDuration(2000)
                .build();
        animateCounterWrong.execute();
    }

    private void AnimationStar(int a,int b, int c)
    {
        int[] star = new int[]{a,b,c};

        for (int i=0; i<star.length; i++)
        {
            Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.scale_up_stars);
            ((ImageView)v.findViewById(star[i])).startAnimation(anim);
        }
        
    }

}
