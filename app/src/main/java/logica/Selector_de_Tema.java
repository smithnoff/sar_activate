package logica;

import android.app.Activity;
import android.content.Intent;

import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by RSMAPP on 09/12/2015.
 */
public class Selector_de_Tema {
    private static int cTheme;
    private static String nPartido="Nombre del partido";
    public final static int DEFAULT = 0;
    public final static int BLUE = 1;
    public final static int BROWN = 2;
    public final static int RED = 3;
    public final static int ORANGE = 4;
    public final static int YELLOW = 5;
    public final static int PURPLE = 6;
    public final static int GREEN = 7;



    public static void changeToTheme(Activity activity, int theme, String partido)

    {
        //reset main activity
        cTheme = theme;
        nPartido=partido;
        activity.finish();



        activity.startActivity(new Intent(activity, activity.getClass()));


    }
    public static String getnPartido(){
        return nPartido;
    }

    public static void onActivityCreateSetTheme(Activity activity)

    {
             //set choosed theme
        switch (cTheme)

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
