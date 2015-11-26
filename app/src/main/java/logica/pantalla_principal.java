package logica;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.usuario.soyactivista.fragments.DialogoCompletarRegistro;
import com.example.usuario.soyactivista.fragments.DialogoRecuperarIdentificador;
import com.example.usuario.soyactivista.fragments.DialogoRecuperarPassw;
import soy_activista.quartzapp.com.soy_activista.R;


public class pantalla_principal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_principal);
    }

    public void completarRegistro(View view){
        FragmentManager fragmentManager = getFragmentManager();
        DialogoCompletarRegistro dialogo = new DialogoCompletarRegistro();
        dialogo.show(fragmentManager, "tagAlerta");
    }

    public void iniciarSesion(View view){
        Intent i = new Intent(getApplication(),ActivityLogin.class);
        startActivity(i);
    }


    public void recuperarContraseña(View view){
        FragmentManager fragmentManager = getFragmentManager();
        DialogoRecuperarPassw dialogo = new DialogoRecuperarPassw();
        dialogo.show(fragmentManager, "tagAlerta");
    }

    public void recuperarIdentificador(View view){
        FragmentManager fragmentManager = getFragmentManager();
        DialogoRecuperarIdentificador dialogo = new DialogoRecuperarIdentificador();
        dialogo.show(fragmentManager, "tagAlerta");
    }


}
