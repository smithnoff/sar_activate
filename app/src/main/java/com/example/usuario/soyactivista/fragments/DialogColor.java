package com.example.usuario.soyactivista.fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by Luis Adrian on 07/01/2016.
 */
public class DialogColor extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.FullHeightDialog);
        dialog.setContentView(R.layout.dialog_vista_previa);
        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();

        TextView imageDialog = (TextView)dialog.findViewById(R.id.dialogImagen);
        EditText dialogText = (EditText) dialog.findViewById(R.id.dialogUsername);
        Button btnDialog = (Button) dialog.findViewById(R.id.dialogButton);
        Button btnDialog2 = (Button) dialog.findViewById(R.id.dialogButton2);
        FloatingActionButton botonCrearMensaje = (FloatingActionButton) dialog.findViewById(R.id.dialogFab);
        ImageView imageDialog2 = (ImageView)dialog.findViewById(R.id.dialogTriangule);
        ImageView imageDialog3 = (ImageView)dialog.findViewById(R.id.dialogCircle);
        ImageView imageDialog4 = (ImageView)dialog.findViewById(R.id.dialogSquare);

        imageDialog.setBackground(getResources().getDrawable(R.color.indigo));
        btnDialog.setBackground(getResources().getDrawable(R.color.indigo));
        btnDialog2.setBackground(getResources().getDrawable(R.color.indigo));
        
        return dialog;
    }
}
