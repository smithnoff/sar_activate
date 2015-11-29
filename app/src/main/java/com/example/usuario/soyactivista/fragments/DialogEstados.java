package com.example.usuario.soyactivista.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by Usuario on 24/10/2015.
 */
public class DialogEstados extends DialogFragment {
    Dialog dialogo;

    public String getEstadoElegido() {
        return EstadoElegido;
    }

    String EstadoElegido;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Listar por estado");
        final ListView modeList = new ListView(getActivity());
        String[] stringArray = getResources().getStringArray(R.array.Estados);
        ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, stringArray);
        modeList.setAdapter(modeAdapter);

        builder.setView(modeList);
        modeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*Toast.makeText(getActivity(), (String) modeList.getItemAtPosition(position), Toast.LENGTH_SHORT).show();*/
                EstadoElegido=(String) modeList.getItemAtPosition(position);
                dialogo.dismiss();

            }
        });
        dialogo=builder.create();
        return dialogo;
    }
}
