package com.burizalabs.soyactivista.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.burizalabs.soyactivista.R;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import java.text.SimpleDateFormat;

/**
 * Created by Brahyam on 25/2/2016.
 */
public class ListarDocumentosParseAdapter extends ParseQueryAdapter<ParseObject> {

    public ListarDocumentosParseAdapter(Context context) {
        super(context, new ParseQueryAdapter.QueryFactory<ParseObject>() {
            public ParseQuery create() {
                ParseQuery query = new ParseQuery("Documento");
                return query;
            }
        });
    }

    public View getItemView(ParseObject documento, View view, ViewGroup parent){

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista_documento,parent,false);

        TextView titulo = (TextView) view.findViewById(R.id.document_name);
        TextView fecha = (TextView) view.findViewById(R.id.date_value);

        titulo.setText(documento.getString("titulo"));

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        fecha.setText(simpleDateFormat.format(documento.getCreatedAt()));

        return view;

    }
}
