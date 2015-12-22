package logica;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by RSMAPP on 09/12/2015.
 */
public class Selector_de_Tema {
    private static final String TAG = "SelectorDeTema";
    private static int tema;
    private static String nombrePartido ="Nombre del partido";
    public final static int DEFAULT = 0;
    public final static int BLUE = 1;
    public final static int BROWN = 2;
    public final static int RED = 3;
    public final static int ORANGE = 4;
    public final static int YELLOW = 5;
    public final static int PURPLE = 6;
    public final static int GREEN = 7;

    public static String getNombrePartido(){
        return nombrePartido;
    }
    public static void setNombrePartido(String nombre){
        nombrePartido = nombre;
    }

    public static int getTema(){
        return tema;
    }

    public static void setTema(int newTema){
        tema = newTema;
    }

    public static void changeToTheme(Activity activity, int theme, String partido)
    {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Config");
        query.fromLocalDatastore();
        try {
            ParseObject config = query.getFirst();

            if(config != null){
                config.put("nombrePartido",partido);
                config.put("tema",theme);
                config.saveInBackground();
            }
        } catch (ParseException e) {
            Log.d(TAG,"Theme could not be found");
        }
        //reset main activity
        tema = theme;

        nombrePartido =partido;
        activity.finish();
        activity.startActivity(new Intent(activity, activity.getClass()));


    }


    public static void onActivityCreateSetTheme(Activity activity) throws ParseException {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Config");
        ParseObject config = query.getFirst();
        Log.d(TAG, "Theme Queried " + config.getString("nombrePartido") + " " + config.getInt("tema"));
        Selector_de_Tema.setNombrePartido(config.getString("nombrePartido"));
        Selector_de_Tema.setTema(config.getInt("tema"));
        config.pinInBackground();

        switch (tema)

        {
            default:

            case DEFAULT:

                activity.setTheme(R.style.AppTheme);

                break;
            case BROWN:

                activity.setTheme(R.style.BrownTheme);

                break;

            case BLUE:

                activity.setTheme(R.style.BlueTheme);

                break;
            case RED:

                activity.setTheme(R.style.RedTheme);

                break;
            case ORANGE:

                activity.setTheme(R.style.OrangeTheme);

                break;
            case YELLOW:

                activity.setTheme(R.style.YellowTheme);

                break;
            case GREEN:

                activity.setTheme(R.style.GreenTheme);

                break;
            case PURPLE:

                activity.setTheme(R.style.PurpleTheme);

                break;
        }
    }
}
