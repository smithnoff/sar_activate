package com.burizalabs.soyactivista.utils;

import java.text.Normalizer;

/**
 * Created by Brahyam on 5/2/2016.
 */
public class TextHelpers {

    public static String NormalizeResource(String string){
        // Normalize Input Name
        String finalName = Normalizer.normalize(string, Normalizer.Form.NFD);
        finalName = finalName.replaceAll("\\p{M}", "").toLowerCase().replaceAll(" ","_");
        return  finalName;
    }


    // translates a comite string into int
    public static int comiteToInt(String comite ){

        int result;
        switch (comite){
            case "Nacional":
                result = 0;
                break;
            case "Estadal":
                result = 1;
                break;
            case "Municipal":
                result = 2;
                break;
            case "Parroquial":
                result = 3;
                break;
            case "Registro":
                result = 4;
                break;
            case "Activista": // Same as Default
            default:
                result = 5;
                break;
        }

        return result;
    }


    public static String comiteToString(int comite){

        String result;
        switch (comite){
            case 0:
                result = "Nacional";
                break;
            case 1:
                result = "Estadal";
                break;
            case 2:
                result = "Municipal";
                break;
            case 3:
                result = "Parroquial";
                break;
            case 4:
                result = "Registro";
                break;
            case 5: // Same as default
            default:
                result = "Activista";
                break;

        }
        return result;
    }


}
