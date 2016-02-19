package com.example.usuario.soyactivista.fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import logica.Selector_de_Tema;
import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by Brahyam on 30/11/2015.
 */
public class FragmentEditarPartido extends Fragment {

    private static final String TAG = "FragEditarPartido";

    private int colorChecked;
    private EditText editNombrePartido;
    private  View colorAnterior;
    private Button guardarTema, editPhoto;
    private View brown, red, orange, yellow, green, blue, purple, indigo;
    private String nombrePartidonull;
    private ImageView photoPartido;
    // Image Saving Variables
    private Bitmap bitmap;
    private int random = (int)(Math.random() * 1000) +1; // For file name.
    private byte[] imagenSeleccionada = null;
    // Constants
    private final int PHOTO_CODE = 100;
    private final int SELECT_PICTURE = 200;

    private String APP_DIRECTORY = "fotosSoyActivista/";
    private String MEDIA_DIRECTORY = APP_DIRECTORY + "media";
    private String TEMPORAL_PICTURE_NAME = "temporal"+ random +".jpg";
    private ParseFile foto = Selector_de_Tema.getImage();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Choose fragment to inflate
        View v = inflater.inflate(R.layout.fragment_editar_partido, container, false);

        // Assign Holders
        editNombrePartido = (EditText) v.findViewById(R.id.editNombrePartido);
        brown = v.findViewById(R.id.themeBrown);
        red =  v.findViewById(R.id.themeRed);
        orange = v.findViewById(R.id.themeOrange);
        yellow =  v.findViewById(R.id.themeYellow);
        green = v.findViewById(R.id.themeGreen);
        blue = v.findViewById(R.id.themeBlue);
        purple = v.findViewById(R.id.themePurple);
        indigo = v.findViewById(R.id.themeDefault);
        photoPartido = (ImageView)v.findViewById(R.id.photo_partido);
        editPhoto = (Button)v.findViewById(R.id.edit_photo);

        guardarTema= (Button) v.findViewById(R.id.btnguardarTema);

        // Load current name
        editNombrePartido.setText(Selector_de_Tema.getNombrePartido());

        // Load current color
        switch (Selector_de_Tema.getTema()){
            case 0:
                indigo.setBackground(getResources().getDrawable(R.drawable.circuloindigo));
                colorAnterior = indigo;
                break;
            case 1:
                blue.setBackground(getResources().getDrawable(R.drawable.circuloazul));
                colorAnterior = blue;
                break;
            case 2:
                brown.setBackground(getResources().getDrawable(R.drawable.circulomarron));
                colorAnterior = brown;
                break;
            case 3:
                red.setBackground(getResources().getDrawable(R.drawable.circulorojo));
                colorAnterior = red;
                break;
            case 4:
                orange.setBackground(getResources().getDrawable(R.drawable.circulonaranja));
                colorAnterior = orange;
                break;
            case 5:
                yellow.setBackground(getResources().getDrawable(R.drawable.circuloamarillo));
                colorAnterior = yellow;
                break;
            case 6:
                purple.setBackground(getResources().getDrawable(R.drawable.circulopurpura));
                colorAnterior = purple;
                break;
            case 7:
                green.setBackground(getResources().getDrawable(R.drawable.circuloverde));
                colorAnterior = green;
                break;
            default:
                indigo.setBackground(getResources().getDrawable(R.drawable.circuloindigo));
                colorAnterior = indigo;
                break;
        }

        nombrePartidonull = editNombrePartido.getText().toString();

        if (foto != null)
        {
            String fileName = foto.getName();
            String extension = fileName.substring((fileName.lastIndexOf(".") + 1), fileName.length());
            //Attached File is an Image
            if (extension.equalsIgnoreCase("jpg"))
            {
                String url = foto.getUrl();
                Glide.with(getContext()).load(url).asBitmap().centerCrop().into(new BitmapImageViewTarget(photoPartido)
                {
                    @Override
                    protected void setResource(Bitmap resource)
                    {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(getContext().getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        photoPartido.setImageDrawable(circularBitmapDrawable);
                    }
                });
            }
        }

        editPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final CharSequence[] options = {"Tomar foto", "Elegir de galeria", "Cancelar"};
                final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getContext());

                builder.setTitle("Elija una opcion:");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int seleccion) {
                        if (options[seleccion] == "Tomar foto") {

                            File file = new File(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
                            file.mkdirs();
                            String path = Environment.getExternalStorageDirectory() + File.separator
                                    + MEDIA_DIRECTORY + File.separator + TEMPORAL_PICTURE_NAME;
                            File newFile = new File(path);
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(newFile));
                            startActivityForResult(intent, PHOTO_CODE);

                        } else if (options[seleccion] == "Elegir de galeria") {

                            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            intent.setType("image/*");
                            startActivityForResult(intent.createChooser(intent, "Seleccione una Aplicación para ver sus imágenes"), SELECT_PICTURE);

                        } else if (options[seleccion] == "Cancelar") {

                            dialog.dismiss();
                        }
                    }
                });
                builder.show();

            }
        });


        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(final View clickedColor) {
                colorChecked = clickedColor.getId();
                DialogColor fragment1 = new DialogColor();
                if(colorAnterior == null)
                    colorAnterior = clickedColor;

                if(clickedColor.getId()==R.id.themeBrown) {
                    fragment1.setColorTema(R.color.brown,R.drawable.circulobr);
                    fragment1.show(getFragmentManager(),"");
                    clickedColor.setBackground(getResources().getDrawable(R.drawable.circulomarron));

                }else {
                    if(colorAnterior.getId()==R.id.themeBrown)

                        colorAnterior.setBackground(getResources().getDrawable(R.drawable.circulobr));
                }
                if(clickedColor.getId()==R.id.themeOrange) {

                    fragment1.setColorTema(R.color.orange800,R.drawable.circulon);
                    fragment1.show(getFragmentManager(),"");
                    clickedColor.setBackground(getResources().getDrawable(R.drawable.circulonaranja));
                }else {
                    if(colorAnterior.getId()==R.id.themeOrange)
                        colorAnterior.setBackground(getResources().getDrawable(R.drawable.circulon));
                }
                if(clickedColor.getId()==R.id.themeBlue) {
                    fragment1.setColorTema(R.color.blue500,R.drawable.circuloaz);
                    fragment1.show(getFragmentManager(),"");
                    clickedColor.setBackground(getResources().getDrawable(R.drawable.circuloazul));
                }else {
                    if(colorAnterior.getId()==R.id.themeBlue)
                        colorAnterior.setBackground(getResources().getDrawable(R.drawable.circuloaz));
                }
                if(clickedColor.getId()==R.id.themeRed) {
                    fragment1.setColorTema(R.color.red900,R.drawable.circulor);
                    fragment1.show(getFragmentManager(),"");
                    clickedColor.setBackground(getResources().getDrawable(R.drawable.circulorojo));
                }else {
                    if(colorAnterior.getId()==R.id.themeRed)
                        colorAnterior.setBackground(getResources().getDrawable(R.drawable.circulor));
                }
                if(clickedColor.getId()==R.id.themeGreen) {
                    fragment1.setColorTema(R.color.green600,R.drawable.circulov);
                    fragment1.show(getFragmentManager(),"");
                    clickedColor.setBackground(getResources().getDrawable(R.drawable.circuloverde));
                }else {
                    if(colorAnterior.getId()==R.id.themeGreen)
                        colorAnterior.setBackground(getResources().getDrawable(R.drawable.circulov));
                }
                if(clickedColor.getId()==R.id.themeYellow) {
                    fragment1.setColorTema(R.color.yellow600,R.drawable.circuloam);
                    fragment1.show(getFragmentManager(),"");
                    clickedColor.setBackground(getResources().getDrawable(R.drawable.circuloamarillo));
                }else {
                    if(colorAnterior.getId()==R.id.themeYellow)

                        colorAnterior.setBackground(getResources().getDrawable(R.drawable.circuloam));
                }
                if(clickedColor.getId()==R.id.themePurple) {
                    fragment1.setColorTema(R.color.deeppurple600,R.drawable.circulop);
                    fragment1.show(getFragmentManager(), "");

                    clickedColor.setBackground(getResources().getDrawable(R.drawable.circulopurpura));
                }else {
                    if(colorAnterior.getId()==R.id.themePurple)
                        colorAnterior.setBackground(getResources().getDrawable(R.drawable.circulop));
                }
                if(clickedColor.getId() == R.id.themeDefault) {
                    fragment1.setColorTema(R.color.indigo,R.drawable.circulo);
                    fragment1.show(getFragmentManager(), "");


                    clickedColor.setBackground(getResources().getDrawable(R.drawable.circuloindigo));
                }else {
                    if(colorAnterior.getId()==R.id.themeDefault)
                        colorAnterior.setBackground(getResources().getDrawable(R.drawable.circulo));
                }
                colorAnterior = clickedColor;
            }
        };

        red.setOnClickListener(clickListener);
        indigo.setOnClickListener(clickListener);
        orange.setOnClickListener(clickListener);
        blue.setOnClickListener(clickListener);
        green.setOnClickListener(clickListener);
        brown.setOnClickListener(clickListener);
        purple.setOnClickListener(clickListener);
        yellow.setOnClickListener(clickListener);


        guardarTema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //select de theme color

                if(editNombrePartido.getText().toString().trim().equals("") || editNombrePartido.getText().toString().trim()==null )
                {
                    Toast.makeText(getActivity(), "El nombre de partido no puede estar vacio.", Toast.LENGTH_LONG).show();
                    editNombrePartido.requestFocus();

                }else

                switch(colorChecked)
                {
                    case R.id.themeBrown:
                        Selector_de_Tema.changeToTheme(getActivity(), Selector_de_Tema.BROWN, editNombrePartido.getText().toString(),foto, imagenSeleccionada);
                        break;
                    case R.id.themeBlue:
                        Selector_de_Tema.changeToTheme(getActivity(), Selector_de_Tema.BLUE,editNombrePartido.getText().toString(),foto, imagenSeleccionada);
                        break;
                    case R.id.themeRed:
                        Selector_de_Tema.changeToTheme(getActivity(), Selector_de_Tema.RED,editNombrePartido.getText().toString(),foto, imagenSeleccionada);
                        break;
                    case R.id.themeDefault:
                        Selector_de_Tema.changeToTheme(getActivity(), Selector_de_Tema.DEFAULT,editNombrePartido.getText().toString(),foto, imagenSeleccionada);
                        break;
                    case R.id.themeOrange:
                        Selector_de_Tema.changeToTheme(getActivity(), Selector_de_Tema.ORANGE,editNombrePartido.getText().toString(),foto, imagenSeleccionada);
                        break;
                    case R.id.themeGreen:
                        Selector_de_Tema.changeToTheme(getActivity(), Selector_de_Tema.GREEN,editNombrePartido.getText().toString(),foto, imagenSeleccionada);
                        break;
                    case R.id.themePurple:

                        Selector_de_Tema.changeToTheme(getActivity(), Selector_de_Tema.PURPLE, editNombrePartido.getText().toString(),foto, imagenSeleccionada);
                        break;
                    case R.id.themeYellow:
                        Selector_de_Tema.changeToTheme(getActivity(), Selector_de_Tema.YELLOW,editNombrePartido.getText().toString(),foto, imagenSeleccionada);
                        break;
                    default:
                        Selector_de_Tema.changeToTheme(getActivity(), Selector_de_Tema.getTema(),editNombrePartido.getText().toString(),foto, imagenSeleccionada);
                        break;
                }
            }
        });


        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case PHOTO_CODE:
                if(resultCode == Activity.RESULT_OK){
                    String dir =  Environment.getExternalStorageDirectory() + File.separator
                            + MEDIA_DIRECTORY + File.separator + TEMPORAL_PICTURE_NAME;
                    bitmap = BitmapFactory.decodeFile(dir);
                    preparePhoto(bitmap);

                    Glide.with(getContext()).load(imagenSeleccionada).asBitmap().centerCrop().into(new BitmapImageViewTarget(photoPartido) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(getContext().getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            photoPartido.setImageDrawable(circularBitmapDrawable);
                        }
                    });
                    /*roundedImage = new RoundImage(bitmap);
                    photoUser.setImageDrawable(roundedImage);*/

                }
                else{
                    Toast.makeText(getActivity(), "Error adjuntando la imagen."+resultCode, Toast.LENGTH_SHORT).show();
                    /*final Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, "Error adjuntando la imagen." + resultCode,
                                    Snackbar.LENGTH_LONG);

                    snackbar.show();*/
                }
                break;

            case SELECT_PICTURE:
                if (resultCode == Activity.RESULT_OK) {
                    Uri path = data.getData();

                    try {
                        InputStream imageStream = getContext().getContentResolver().openInputStream(path);
                        bitmap = BitmapFactory.decodeStream(imageStream);
                        preparePhoto(bitmap);

                        Glide.with(getContext()).load(imagenSeleccionada).asBitmap().centerCrop().into(new BitmapImageViewTarget(photoPartido) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable circularBitmapDrawable =
                                        RoundedBitmapDrawableFactory.create(getContext().getResources(), resource);
                                circularBitmapDrawable.setCircular(true);
                                photoPartido.setImageDrawable(circularBitmapDrawable);
                            }
                        });

                        /*roundedImage = new RoundImage(bitmap);
                        photoUser.setImageDrawable(roundedImage);*/

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getActivity(), "Error adjuntando la imagen."+resultCode, Toast.LENGTH_SHORT).show();
                    /*final Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, "Error adjuntando la imagen."+resultCode,
                                    Snackbar.LENGTH_LONG);
                    snackbar.show();*/
                }
                break;

        }
    }

    //Process Photo
    private void preparePhoto(Bitmap bitmap)
    {
        // RESIZE
        this.bitmap = Bitmap.createScaledBitmap(bitmap, 400, 400
                * bitmap.getHeight() / bitmap.getWidth(), false);
        // COMPRESS
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        this.bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);

        //Store in local to be saved after
        imagenSeleccionada = bos.toByteArray();
    }


}