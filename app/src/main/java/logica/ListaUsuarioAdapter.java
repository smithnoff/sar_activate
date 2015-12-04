package logica;

import android.app.Activity;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by Usuario on 19/10/2015.
 */
public class ListaUsuarioAdapter extends ArrayAdapter<Usuario> {

   /* private Usuario[] arrayUsuario;*/
    private ArrayList<Usuario> listaUsuario=new ArrayList<>();
    private int layoutAInflar;
    private Activity context;


    public ListaUsuarioAdapter(Activity context,ArrayList<Usuario>lista,int layoutAInflar) {
        super(context, R.layout.fragment_listar_usuarioold,lista);
       /* this.arrayUsuario=arrayUsuario;*/
        this.listaUsuario=lista;
        this.context=context;
        this.layoutAInflar=layoutAInflar;

    }

    public View getView(int position,View view,ViewGroup parent) {
        View rowView=null;
        LayoutInflater inflater = context.getLayoutInflater();
        rowView= inflater.inflate(this.layoutAInflar, null, true);
        TextView txtNombre = (TextView) rowView.findViewById(R.id.listaNombre);
        TextView txtEstado = (TextView) rowView.findViewById(R.id.listaestado);
        TextView txtCargo = (TextView) rowView.findViewById(R.id.listaCargo);
        this.llenarFila(txtNombre, txtEstado, txtCargo, position);

        return rowView;

       }

    private void llenarFila(TextView txtNombre, TextView txtEstado,TextView txtCargo,int position) {
        txtNombre.setTextSize(TypedValue.COMPLEX_UNIT_PX,38);
        txtEstado.setTextSize(TypedValue.COMPLEX_UNIT_PX,27);
        txtCargo.setTextSize(TypedValue.COMPLEX_UNIT_PX,34);


        txtNombre.setText(listaUsuario.get(position).getNombre() + " " + listaUsuario.get(position).getApellido());
        txtEstado.setText(listaUsuario.get(position).getEstado()+" - "+listaUsuario.get(position).getMuncipio());
        txtCargo.setText(listaUsuario.get(position).getCargo());





    }

}
