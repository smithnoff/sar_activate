package logica;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import java.util.ArrayList;

/**
 * Created by Brahyam on 3/12/2015.
 */
public class ListarUsuarioAdapter extends ArrayAdapter<Usuario> {

    public ListarUsuarioAdapter(Context context, ArrayList<Usuario> usuarios){
        super(context,0,usuarios);
    }

}
