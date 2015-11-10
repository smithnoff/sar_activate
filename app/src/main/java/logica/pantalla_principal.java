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

    Button botonIniciar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_principal);

        botonIniciar = (Button)findViewById(R.id.boton_iniciar_sesion);

        botonIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplication(),ActivityLogin.class);
                startActivity(i);
            }
        });

        /*ParseAnalytics.trackAppOpenedInBackground(getIntent());

        ParseObject testObject = new ParseObject("TestObject");
        testObject.put("foo", "hola");
        testObject.saveInBackground();*/
    }
    public void recuperarpasw(View view){
        FragmentManager fragmentManager = getFragmentManager();
        DialogoRecuperarPassw dialogo = new DialogoRecuperarPassw();
        dialogo.show(fragmentManager, "tagAlerta");
    }

    public void recuperaridentificador(View view){
        FragmentManager fragmentManager = getFragmentManager();
        DialogoRecuperarIdentificador dialogo = new DialogoRecuperarIdentificador();
        dialogo.show(fragmentManager, "tagAlerta");
    }

    public void completarRegistro(View view){
        FragmentManager fragmentManager = getFragmentManager();
        DialogoCompletarRegistro dialogo = new DialogoCompletarRegistro();
        dialogo.show(fragmentManager, "tagAlerta");
    }
}
