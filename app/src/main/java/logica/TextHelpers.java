package logica;

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
}
