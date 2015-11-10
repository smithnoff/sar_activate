package com.example.usuario.soyactivista.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * Created by Usuario on 20/10/2015.
 */
public class ProgressDialogFragment extends DialogFragment
{
    String titulo;
    String mensajeCargando;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getMensajeCargando() {
        return mensajeCargando;
    }

    public void setMensajeCargando(String mensajeCargando) {
        this.mensajeCargando = mensajeCargando;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setCancelable(false);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        ProgressDialog dialog = new ProgressDialog(getActivity(), getTheme());
        dialog.setTitle(titulo);
        dialog.setMessage(mensajeCargando);
        dialog.setIndeterminate(true);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        return dialog;
    }
}