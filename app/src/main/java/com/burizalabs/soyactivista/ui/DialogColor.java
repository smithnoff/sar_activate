package com.burizalabs.soyactivista.ui;


import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;

import com.burizalabs.soyactivista.R;
import com.github.clans.fab.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Created by Luis Adrian on 07/01/2016.
 */
public class DialogColor extends DialogFragment {
    Button btnDialog,btnDialog2;
    TextView imageDialog;
    ImageView imageDialog2,imageDialog3,imageDialog4, fabView;
    FloatingActionButton botonCrearMensaje;
    FloatingActionButton fabEdit;
    private FloatingActionButton fabcrear;
   private int colorTema=R.color.indigo;
    private int botonfloat;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.FullHeightDialog);
        dialog.setContentView(R.layout.dialog_vista_previa);
        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();

        imageDialog = (TextView)dialog.findViewById(R.id.dialogImagen);
        EditText dialogText = (EditText) dialog.findViewById(R.id.dialogUsername);
         btnDialog = (Button) dialog.findViewById(R.id.dialogButton);
        btnDialog2 = (Button) dialog.findViewById(R.id.dialogButton2);
        //botonCrearMensaje = (FloatingActionButton) dialog.findViewById(R.id.dialogFab);
         imageDialog2 = (ImageView)dialog.findViewById(R.id.dialogTriangule);
         imageDialog3 = (ImageView)dialog.findViewById(R.id.dialogCircle);
         imageDialog4 = (ImageView)dialog.findViewById(R.id.dialogSquare);

        //fabcrear = (FloatingActionButton) dialog.findViewById(R.id.fabcrear);
        fabEdit = (FloatingActionButton) dialog.findViewById(R.id.fab_edit);
        fabEdit.setShowAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.scale_up));
        //fabEdit.setHideAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.scale_down));
        int delay = 1000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fabEdit.show(true);
            }
        }, delay + 150);

        previewTheme();

        
        return dialog;
    }

    public void previewTheme()
    {     getColorTema();
        getBotonTema();
        imageDialog.setBackground(getResources().getDrawable(colorTema));

        btnDialog.setBackground(getResources().getDrawable(colorTema));
        btnDialog2.setBackground(getResources().getDrawable(colorTema));
        imageDialog2.setBackgroundColor(getResources().getColor(colorTema));
        imageDialog3.setBackgroundColor(getResources().getColor(colorTema));
        imageDialog4.setBackgroundColor(getResources().getColor(colorTema));

        fabEdit.setBackgroundDrawable(getResources().getDrawable(botonfloat));
        //botonCrearMensaje.setBackgroundDrawable(getResources().getDrawable(botonfloat));
        fabEdit.setColorNormal(getResources().getColor(colorTema));

    }
    public void setColorTema(int tema,int boton)
    {   botonfloat=boton;
        colorTema=tema;
    }
    public int getColorTema(){
        return colorTema;
    }
    public int getBotonTema(){
        return botonfloat;
    }
}
