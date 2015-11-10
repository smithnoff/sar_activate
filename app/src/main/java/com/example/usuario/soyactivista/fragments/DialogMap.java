package com.example.usuario.soyactivista.fragments;


import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import soy_activista.quartzapp.com.soy_activista.R;


public class DialogMap extends DialogFragment {


    /**CLASE GENERAL PARA METER UN MAPA EN UN DIALOGO, PARA
     * IMPLEMENTAR SE EXTIENDE DE ESTA CLASE**/
    private String ubicacionMarca=null;
    private Button mapBotonCancelar;
    private Button mapBotonAceptar;
     GoogleMap googleMap;
    private SupportMapFragment mMapView;

    public Button getMapBotonAceptar() {
        return mapBotonAceptar;
    }

    public String getUbicacionMarca() {
        return ubicacionMarca;
    }

    public Button getMapBotonCancelar() {
        return mapBotonCancelar;
    }


    public DialogMap() {
        this.setStyle(STYLE_NO_TITLE,R.style.Theme_Dialog_Translucent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mapa_layout, container, false);

        mapBotonCancelar = (Button)view.findViewById(R.id.mapBotonCancelar);
        mapBotonAceptar = (Button)view.findViewById(R.id.mapBotonAceptar);

        mMapView = (SupportMapFragment) this.getFragmentManager().findFragmentById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();

        googleMap = mMapView.getMap();

        /*googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(7.3596096,-66.6874229),19));*/
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(7.3596096,-66.6874229),5));




        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng point) {

                Projection proj = googleMap.getProjection();
                Point coord = proj.toScreenLocation(point);
                    googleMap.clear();

                 googleMap.addMarker(new MarkerOptions().title("Adjuntar").position(point));

                ubicacionMarca=String.valueOf(point.latitude)+"|"+String.valueOf(point.longitude);


            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getFragmentManager().beginTransaction().remove(mMapView).commit();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }



    public static DialogMap newInstance(int title) {
        DialogMap frag = new DialogMap();
        Bundle args = new Bundle();
        args.putInt("title", title);
        frag.setArguments(args);
        return frag;
    }
}

