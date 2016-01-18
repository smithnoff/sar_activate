package com.example.usuario.soyactivista.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by Luis Adrian on 14/01/2016.
 */
public class FragmentAAA extends Fragment {
private ImageView bolivar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.aaexample, container, false);
        bolivar = (ImageView)v.findViewById(R.id.bolivar);
        //bolivar.setColorFilter(R.color.red900);
        //bolivar.setColorFilter(R.color.black);
        //loadImageFromURL("http://vignette3.wikia.nocookie.net/future/images/7/76/Flag_of_Venezuela.png",bolivar);

        return v;
    }

}
