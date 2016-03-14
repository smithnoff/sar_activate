package com.burizalabs.soyactivista.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.burizalabs.soyactivista.R;


public class ActivityPantallaInicio extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_principal);
    }

    public void completarRegistro(View view){

        Fragment frag = new FragmentCompletarRegistro();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, frag)
                //.addToBackStack(null)
                //.add(android.R.id.content, frag)
                .commit();
    }


    public void iniciarSesion(View view){
        Intent i = new Intent(getApplication(),ActivityLogin.class);
        startActivity(i);
    }


    public void recuperarContraseña(View view){
        Fragment fragment = new FragmentRecuperarContrasena();
        getSupportFragmentManager()
                .beginTransaction()
                .add(android.R.id.content, fragment)
                .addToBackStack(null)
                .commit();
    }

    public void recuperarIdentificador(View view){
        Fragment fragment = new FragmentRecuperarIdentificador();
        getSupportFragmentManager()
                .beginTransaction()
                .add(android.R.id.content,fragment)
                .addToBackStack(null)
                .commit();
    }


}
