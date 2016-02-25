package com.example.usuario.soyactivista.fragments;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import soy_activista.quartzapp.com.soy_activista.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentCrearDocumento extends Fragment {


    CoordinatorLayout coordinatorLayout;
    private static final int PICKFILE_RESULT_CODE = 300;
    private Button subirDocumento,salir;
    private EditText tituloDoc,descripcionDoc,documento;
    private byte[] selectedFile;
    private int random = (int)(Math.random() * 1000) +1;
    private ProgressDialog dialog;
    final ParseUser usuarioActual = ParseUser.getCurrentUser();

    public FragmentCrearDocumento() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_crear_documento, container, false);
        subirDocumento = (Button) v.findViewById(R.id.subirDoc);
        salir = (Button) v.findViewById(R.id.salir);
        coordinatorLayout = (CoordinatorLayout) v.findViewById(R.id.coordinatorLayout);
        tituloDoc = (EditText) v.findViewById(R.id.tituloDoc);
        descripcionDoc = (EditText) v.findViewById(R.id.descripcionDoc);
        documento = (EditText) v.findViewById(R.id.documento);

salir.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Fragment fragment = new FragmentTriviaPrincipal();
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();
    }
});
        documento.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                {
                 documento.setText("");
                    String minmeType = "application/pdf";

                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType(minmeType);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);

                    // special intent for Samsung file manager
                    Intent sIntent = new Intent("com.sec.android.app.myfiles.PICK_DATA");
                    // if you want any file type, you can skip next line
                    sIntent.putExtra("CONTENT_TYPE", minmeType);
                    sIntent.addCategory(Intent.CATEGORY_DEFAULT);

                    Intent chooserIntent;
                    if (getActivity().getPackageManager().resolveActivity(sIntent, 0) != null){
                        // it is device with samsung file manager
                        chooserIntent = Intent.createChooser(sIntent, "Open file");
                        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] { intent});
                    }
                    else {
                        chooserIntent = Intent.createChooser(intent, "Open file");
                    }

                    try {
                        startActivityForResult(chooserIntent, PICKFILE_RESULT_CODE);
                    } catch (android.content.ActivityNotFoundException ex) {
                        //Toast.makeText(getContext(), "No se encontró un manejador de archivos. Por favor descargue como \"Astro File Manager\" de la Tienda de Google.", Toast.LENGTH_LONG).show();
                        final Snackbar snackbar = Snackbar
                                .make(coordinatorLayout, "No se encontró un manejador de archivos. Por favor descargue como \"Astro File Manager\" de la Tienda de Google.",
                                        Snackbar.LENGTH_LONG);

                        snackbar.show();
                    }
                    documento.clearFocus();
                }else{

                }
            }
        });

subirDocumento.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {


        if (tituloDoc.getText().toString().trim().length() > 0) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Confirmar");
            builder.setMessage("¿Está seguro de que quiere subir este archivo?");


            builder.setPositiveButton("Publicar", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialogo, int which) {

                    dialog = ProgressDialog.show(getActivity(), "", "Cargando Archivo", true);

                    // Fill ParseObject to send
                    final ParseObject archivo = new ParseObject("DocumentoTrivia");

                    archivo.put("titulo", tituloDoc.getText().toString());
                    archivo.put("descripcion", descripcionDoc.getText().toString());



                    // Handle Image uploading

                    if (selectedFile != null) {
                        ParseFile finalFile = new ParseFile(usuarioActual.getUsername() + random + ".pdf", selectedFile);

                        archivo.put("adjunto", finalFile);
                        finalFile.saveInBackground(new SaveCallback() {
                            public void done(ParseException e) {
                                if (e != null) {
                                    Toast.makeText(getActivity(),
                                            "Error saving: " + e.getMessage(),
                                            Toast.LENGTH_LONG).show();

                                } else {
                                    //Toast.makeText(getActivity(), "PDF Cargado.", Toast.LENGTH_SHORT).show();
                                    final Snackbar snackbar2 = Snackbar
                                            .make(coordinatorLayout, "PDF Cargado.",
                                                    Snackbar.LENGTH_LONG);

                                    snackbar2.show();
                                }
                            }
                        });
                    }
                    // Handle Location

                    // Save Message
                    archivo.saveInBackground(new SaveCallback() {
                        public void done(ParseException e) {
                            if (e == null) {
                                dialog.dismiss();
                                //Toast.makeText(getActivity(), "Mensaje Publicado", Toast.LENGTH_SHORT).show();
                                final Snackbar snackbar = Snackbar
                                        .make(coordinatorLayout, "Mensaje Publicado.",
                                                Snackbar.LENGTH_LONG);

                                snackbar.show();
                                // Redirect View to ListarMensajes
                                Fragment fragment = new FragmentListarMensaje();
                                getFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.content_frame, fragment)
                                        .commit();
                            } else {
                                dialog.dismiss();

                                Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                    dialogo.dismiss();
                }

            });

            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialogo, int which) {
                    // Do nothing
                    dialogo.dismiss();
                }
            });

            AlertDialog alert = builder.create();
            alert.show();

        } else {
            //Toast.makeText(getContext(), "No puede publicar un mensaje vacío.", Toast.LENGTH_LONG).show();
            final Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "No puede publicar un mensaje vacío.",
                            Snackbar.LENGTH_LONG);

            snackbar.show();

            return;
        }

    }
});


        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        switch (requestCode){




            case PICKFILE_RESULT_CODE:
                if(resultCode == Activity.RESULT_OK) {
                    Uri path = data.getData();
                    try {
                        InputStream pdfStream = getContext().getContentResolver().openInputStream(path);
                        selectedFile = getBytes(pdfStream);

                      documento.setText(usuarioActual.getUsername() + random +".pdf");


                    } catch (FileNotFoundException e) {
                       final Snackbar snackbar = Snackbar
                                .make(coordinatorLayout, "Error adjuntando la posición."+resultCode,
                                        Snackbar.LENGTH_LONG);

                        snackbar.show();

                    } catch (IOException e) {
                        //Toast.makeText(getActivity(), "Error adjuntando el archivo."+resultCode, Toast.LENGTH_SHORT).show();
                        final Snackbar snackbar = Snackbar
                                .make(coordinatorLayout, "Error adjuntando el archivo."+resultCode,
                                        Snackbar.LENGTH_LONG);
                        snackbar.show();

                    }


                }
                else{
                    //Toast.makeText(getActivity(), "Error adjuntando el archivo."+resultCode, Toast.LENGTH_SHORT).show();
                    final Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, "Error adjuntando el archivo."+resultCode,
                                    Snackbar.LENGTH_LONG);

                    snackbar.show();
                }
                break;



        }
    }


    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

}


