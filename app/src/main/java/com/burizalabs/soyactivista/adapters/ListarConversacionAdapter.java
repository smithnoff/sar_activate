package com.burizalabs.soyactivista.adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.burizalabs.soyactivista.R;
import com.burizalabs.soyactivista.entities.Conversacion;
import com.burizalabs.soyactivista.entities.Usuario;

/**
 * Created by Brahyam on 18/12/2015.
 */
public class ListarConversacionAdapter extends ArrayAdapter<Conversacion> {

    private String TAG = "ListarConversacionAdapter";

    // For Fitlering
    ArrayList<Conversacion> conversacionArrayList;
    ArrayList<Conversacion> originalConversacionArrayList;


    public ListarConversacionAdapter(Activity context, ArrayList<Conversacion> conversaciones) {
        super(context, 0,conversaciones);
        this.conversacionArrayList = new ArrayList<>(conversaciones);
        this.originalConversacionArrayList = new ArrayList<>(conversaciones);
    }

    public View getView(int position,View view,ViewGroup parent) {

        // TODO: Use View Holder method to improve performance by recycling views.
        if (view == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista_conversacion, parent, false);
        }

        Conversacion conversacion = getItem(position);

        Usuario creador = conversacion.usuario;

        //Declare all fields
        final TextView valueNombre,valueApellido, valueEstado, valueMunicipio, valueCargo,valueComite,valueRol, valueUltimaActividad;

        // Assign to holders
        valueNombre = (TextView) view.findViewById(R.id.valueNombre);
        valueApellido = (TextView)view.findViewById(R.id.valueApellido);
        valueEstado = (TextView) view.findViewById(R.id.valueEstado);
        valueMunicipio = (TextView) view.findViewById(R.id.valueMunicipio);
        valueCargo = (TextView) view.findViewById(R.id.valueCargo);
        valueComite = (TextView) view.findViewById(R.id.valueComite);
        valueRol    = (TextView) view.findViewById(R.id.valueRol);
        valueUltimaActividad = (TextView) view.findViewById(R.id.valueUltimaActividad);


        // Load Values
        valueNombre.setText(abreviatteName(creador.getNombre()+" "+creador.getApellido()));
        //valueApellido.setText(creador.getApellido());
        valueEstado.setText(creador.getEstado()+" - "+creador.getMunicipio());
        //valueMunicipio.setText(creador.getMunicipio());
        valueCargo.setText("Cargo: "+creador.getCargo());
        valueComite.setText("Comité: "+creador.getComite());
        valueRol.setText("Tipo de usuario: "+creador.getRolName());

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
        valueUltimaActividad.setText("Última vez activa: "+dateFormat.format(conversacion.getUltimaActividad()));

        return view;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                // Write your logic for PUBLISHING RESULTS and notify your dataset for change
                clear();
                addAll((ArrayList<Conversacion>) results.values);
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                // Write your logic here to PERFORM FILTERING and return filtered result
                FilterResults results = new FilterResults();

                ProgressDialog dialog = ProgressDialog.show(getContext(),"Filtrando","Cargando...",true);

                ArrayList<Conversacion> resultArrayList;

                // if no constraint given return current list.
                if(constraint == null || constraint.length() == 0){
                    resultArrayList = new ArrayList<>(conversacionArrayList);
                    results.count = getCount();
                    results.values = resultArrayList;
                    return  results;
                }

                // if constraint given- Filter by id
                constraint = constraint.toString().toLowerCase();

                // Determine type of constraint
                String[] stringQuery = constraint.toString().split("=");
                String value = stringQuery[1].toLowerCase();
                resultArrayList  = new ArrayList<>();

                switch (stringQuery[0]){

                    case "texto":
                        for (int i = 0; i < getCount(); i++ )
                        {
                            Conversacion data = getItem(i);
                            Log.d(TAG, "Comparing " + value + " to " + data.usuario.getUsername()+" or "+ data.usuario.getNombre()+" or "+data.usuario.getApellido());

                            if(data.usuario.getUsername().toLowerCase().startsWith(value))
                                resultArrayList.add(data);
                            else
                                if(data.usuario.getNombre().toLowerCase().startsWith(value))
                                    resultArrayList.add(data);
                                else
                                    if(data.usuario.getApellido().toLowerCase().startsWith(value))
                                        resultArrayList.add(data);

                        }
                        break;

                    default:
                        Log.d(TAG,"Returning Default Result");
                        resultArrayList = new ArrayList<>(conversacionArrayList);
                        break;
                }


                Log.d(TAG,"Final Result list contains "+resultArrayList.size()+" elements.");

                // If result list is empty, return whole list.
                if( resultArrayList.size() == 0)
                {
                    if (!value.equals("todos"))
                        Toast.makeText(getContext(), "Ningun elemento encontrado.", Toast.LENGTH_SHORT).show();

                    resultArrayList = new ArrayList<>(conversacionArrayList);
                }


                results.count = resultArrayList.size();
                results.values = resultArrayList;
                dialog.dismiss();
                return results;
            }
        };
    }
    private String abreviatteName(String subStr )
    {
        if(subStr.length()>20)
        {
            subStr=subStr.substring(0,20);
        }


        return subStr;
    }
}