package com.burizalabs.soyactivista.ui;

import android.app.ProgressDialog;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import com.burizalabs.soyactivista.R;

/**
 * Created by Brahyam on 1/12/2015.
 */
public class FragmentVerImagen extends Fragment {

    private ImageView imageView;
    private Button botonSalir;
    private ProgressDialog dialog;
    private ProgressBar ProgressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_ver_imagen, container, false);


        ProgressBar = (ProgressBar)v.findViewById(R.id.ProgressBar);

        // Assign holders
        imageView = (ImageView)v.findViewById(R.id.valueImage);

        botonSalir = (Button)v.findViewById(R.id.botonSalir);

        String url = getArguments().getString("imageUrl");

        Log.d("DETALLE", "url "+url );

        if(url != null && !url.equals("")){

            Glide.with(getContext())
                    .load(url)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            ProgressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .fitCenter()
                    .into(imageView);
        }
        else{
            Glide.with(getContext())
                    .load(getArguments().getByteArray("imageByteArray"))
                    .fitCenter()
                    .into(imageView);

        }

        // On Click listener to report Message
        botonSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager()
                        .popBackStack();
            }
        });

        return v;
    }
}
