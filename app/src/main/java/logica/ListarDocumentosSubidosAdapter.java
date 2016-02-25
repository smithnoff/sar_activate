package logica;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.ArrayList;

import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by Luis Adrian on 24/02/2016.
 */
public class ListarDocumentosSubidosAdapter extends ArrayAdapter<Documento>  {

    private String TAG = "ListarUsuarioAdapter";

    // For Fitlering
    ArrayList<Documento> documentoArrayList;
    ParseUser currentUser = ParseUser.getCurrentUser();

    public ListarDocumentosSubidosAdapter(Activity context, ArrayList<Documento> documentArrayList) {
        super(context, 0,documentArrayList);
        this.documentoArrayList = new ArrayList<>(documentArrayList);
    }

    public View getView(int position,View view,ViewGroup parent) {

        // TODO: Use View Holder method to improve performance by recycling views.
        if (view == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista_documentos_subidos, parent, false);
        }

        Documento documento = getItem(position);

        //Declare all fields
        final TextView valueTitulo, valueDescripcion;

        // Assign to holders
        valueTitulo = (TextView) view.findViewById(R.id.valueTitulo);
        valueDescripcion = (TextView) view.findViewById(R.id.valueDescripcion);

        // Load Values
        valueTitulo.setText(documento.getTitulo());
        valueDescripcion.setText(documento.getDescripcion());

        return view;

    }


}
