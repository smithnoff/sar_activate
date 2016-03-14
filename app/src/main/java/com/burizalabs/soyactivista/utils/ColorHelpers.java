package com.burizalabs.soyactivista.utils;

import android.graphics.Color;

/**
 * Created by Brahyam on 5/2/2016.
 */
public class ColorHelpers {


    public static int lighten(int color, double fraction) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        red = lightenColor(red, fraction);
        green = lightenColor(green, fraction);
        blue = lightenColor(blue, fraction);
        int alpha = Color.alpha(color);
        return Color.argb(alpha, red, green, blue);
    }

    public static int darken(int color, double fraction) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        red = darkenColor(red, fraction);
        green = darkenColor(green, fraction);
        blue = darkenColor(blue, fraction);
        int alpha = Color.alpha(color);

        return Color.argb(alpha, red, green, blue);
    }
    public static int grayten() {


        return Color.argb(105, 97, 97, 97);
    }
    private static int darkenColor(int color, double fraction) {
        return (int)Math.max(color - (color * fraction), 0);
    }

    private static int lightenColor(int color, double fraction) {
        return (int) Math.min(color + (color * fraction), 255);
    }

    public static int getGradient(int color, int points){
        int finalColor = color;

        // Get color tone
        if ( points > 8000 )
            finalColor = ColorHelpers.darken(color, 0.4);

        if ( points > 6000 && points <= 7999 )
            finalColor = ColorHelpers.darken(color, 0.2);

        if ( points > 4000 && points <= 5999 )
            finalColor = color;

        if ( points > 1000 && points <= 3999 )
            finalColor = ColorHelpers.lighten(color, 0.2);

        if ( points > 0 && points <= 999 )
            finalColor = ColorHelpers.lighten(color, 0.4);
        if ( points == 0  )
            finalColor = ColorHelpers.grayten();

        return finalColor;
    }


}
