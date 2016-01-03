package com.example.usuario.soyactivista.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.widget.Toast;

import logica.Selector_de_Tema;
import soy_activista.quartzapp.com.soy_activista.R;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.List;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import soy_activista.quartzapp.com.soy_activista.R;

import static java.lang.Integer.parseInt;

import android.app.AlertDialog;
import android.content.DialogInterface;

import java.util.List;

/**
 * Created by Brahyam on 28/11/2015.
 */
public class FragmentEditarTipoActividad extends Fragment {

    private EditText nombre, descripcion; // Edit Field holders
    private Spinner puntaje; // Spinner holders
    private Button editar,eliminar,guardar, cancelar; // Button holders
    private TextView TextPuntaje;
    private ProgressDialog dialog;

    //Floating Button
    private FloatingActionButton fabcrear;
    private FloatingActionButton fabcancelar;
    private FloatingActionButton fabeditar;
    private FloatingActionButton fabeliminar;

    private List<FloatingActionMenu> menus = new ArrayList<>();
    private Handler mUiHandler = new Handler();

    // Class Constructor
    public FragmentEditarTipoActividad(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        //Choose fragment to inflate
        View v = inflater.inflate(R.layout.fragment_editar_tipo_actividad, container, false);

        //Gets Current User
        final ParseUser usuarioActual = ParseUser.getCurrentUser();

        /* Commenting for realise version.
        final FloatingActionMenu menu1 = (FloatingActionMenu)v.findViewById(R.id.menu1);
        menu1.setBackgroundColor(Selector_de_Tema.getTema());
        fabcrear = (FloatingActionButton) v.findViewById(R.id.fabcrear);
        fabcancelar = (FloatingActionButton) v.findViewById(R.id.fabcancelar);
        fabeditar = (FloatingActionButton) v.findViewById(R.id.fabeditar);
        fabeliminar = (FloatingActionButton) v.findViewById(R.id.fabeliminar);

        menu1.setOnMenuButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                menu1.toggle(true);
            }
        });

        menus.add(menu1);

        menu1.hideMenuButton(false);

        int delay = 400;
        for (final FloatingActionMenu menu : menus) {
            mUiHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    menu.showMenuButton(true);
                }
            }, delay);
            delay += 150;
        }

        menu1.setClosedOnTouchOutside(true);

        /*fabeditar.setOnClickListener(clickListener);
        fabeliminar.setOnClickListener(clickListener);
        fabcancelar.setOnClickListener(clickListener);
        fabcrear.setOnClickListener(cliclListener);
        */

        //Asign Text Edit to holders
        nombre = (EditText)v.findViewById(R.id.editActividad);
        descripcion = (EditText)v.findViewById(R.id.editDescripcion);

        // Asigns Spinners to holders
        puntaje = (Spinner)v.findViewById(R.id.spinPuntaje);

        // Asign Buttons to holders

        editar = (Button)v.findViewById(R.id.btnEditarTipoActividad);
        eliminar = (Button)v.findViewById(R.id.btnEliminarTipoActividad);
        guardar = (Button)v.findViewById(R.id.btnGuardarTipoActividad);
        cancelar = (Button)v.findViewById(R.id.buttonCancelar);

        TextPuntaje = (TextView)v.findViewById(R.id.TextPuntaje);


        //Fill Spinners with Preset Options
        this.llenarSpinnerdesdeId(puntaje, R.array.Puntuaciones);


        //Set Values from bundle
        nombre.setText(getArguments().getString("nombre"));
        descripcion.setText(getArguments().getString("descripcion"));
        ArrayAdapter<String> array_spinner=(ArrayAdapter<String>)puntaje.getAdapter();
        puntaje.setSelection(array_spinner.getPosition(getArguments().getString("puntaje")));
        TextPuntaje.setText("Puntaje: " + getArguments().getString("puntaje"));


        // Buttons Behavior
        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                // Turn fields editable
                nombre.setEnabled(true);
                descripcion.setEnabled(true);
                puntaje.setVisibility(View.VISIBLE);

                // Hide Delete/Edit Button.
                editar.setVisibility(View.GONE);
                eliminar.setVisibility(View.GONE);
                guardar.setVisibility(View.VISIBLE);
                cancelar.setVisibility(View.VISIBLE);
            }
        });


        // Saving method
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Confirmar");
                builder.setMessage("¿Está seguro que desea guardar la actividad?");

                builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialogo, int which) {
                        // Do nothing but close the dialog
                        dialogo.dismiss();
                        dialog = ProgressDialog.show(getActivity(), "", "Editando Tipo de Actividad", true);

                        ParseQuery<ParseObject> query = ParseQuery.getQuery("TipoActividad");

                        // Retrieve the object by id from parse
                        query.getInBackground(getArguments().getString("id"), new GetCallback<ParseObject>() {
                            public void done(ParseObject tipoActividad, ParseException e) {
                                if (e == null) {
                                    // Disable tipoActividad
                                    tipoActividad.put("activa", false);
                                    tipoActividad.saveInBackground();

                                    // Fill new ParseObject to send
                                    ParseObject newTipoActividad = new ParseObject("TipoActividad");
                                    tipoActividad.put("nombre", nombre.getText().toString());
                                    tipoActividad.put("puntaje", parseInt(puntaje.getSelectedItem().toString()));
                                    tipoActividad.put("descripcion", descripcion.getText().toString());
                                    tipoActividad.put("creador", usuarioActual);
                                    tipoActividad.put("activa",true);

                                    tipoActividad.saveInBackground(new SaveCallback() {
                                        public void done(ParseException e) {
                                            if (e == null) {
                                                dialog.dismiss();
                                                Toast.makeText(getActivity(), "Tipo de actividad Guardada", Toast.LENGTH_SHORT).show();
                                                // Redirect View to Boletin de Actividades
                                                Fragment fragment = new FragmentListarTipoActividad();
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

                                } else {
                                    // Object not found in Parse
                                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }

                        });


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

            }
        });


        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Turn fields not editable
                nombre.setEnabled(false);
                descripcion.setEnabled(false);
                puntaje.setEnabled(false);

                // Hide Delete/Edit Button.
                editar.setVisibility(View.VISIBLE);
                eliminar.setVisibility(View.VISIBLE);
                guardar.setVisibility(View.GONE);
                cancelar.setVisibility(View.GONE);

            }
        });



        eliminar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0){


                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Confirmar");
                builder.setMessage("¿Está seguro que desea eliminar el tipo de actividad?.");

                builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialogo, int which) {
                        dialogo.dismiss();

                        ParseObject delTipoActividad = ParseObject.createWithoutData("TipoActividad", getArguments().getString("id"));
                        // Disable tipoActividad
                        delTipoActividad.put("activa", false);
                        delTipoActividad.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    Toast.makeText(getActivity(), "Tipo de actividad eliminada correctamente.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getActivity(), "Ocurrió un error eliminando la actividad."+e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                        Fragment fragment = new FragmentListarTipoActividad();
                        getFragmentManager()
                                .beginTransaction()
                                .replace(R.id.content_frame, fragment)
                                .commit();

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

            }
        });

        return v;
    }

    //ONLICK FLOATINGBUTTONACTION
    /*private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String text = "";

            switch (v.getId()) {
                case R.id.fab1:
                    text = fab1.getLabelText();
                    break;
                case R.id.fab2:
                    text = fab2.getLabelText();
                    fab2.setVisibility(View.GONE);
                    break;
                case R.id.fab3:
                    text = fab3.getLabelText();
                    fab2.setVisibility(View.VISIBLE);
                    break;
                case R.id.fab12:
                    text = fab12.getLabelText();
                    break;
                case R.id.fab22:
                    text = fab22.getLabelText();
                    break;
                case R.id.fab32:
                    text = fab32.getLabelText();
                    break;
            }

            Toast.makeText(FloatingMenusActivity.this, text, Toast.LENGTH_SHORT).show();
        }
    };*/

    //Auxiliar method for filling spinners with String Arrays
    public void llenarSpinnerdesdeId(Spinner spin,int id){
        ArrayAdapter spinner_adapter = ArrayAdapter.createFromResource(getActivity(), id, android.R.layout.simple_spinner_item);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(spinner_adapter);
    }
}
