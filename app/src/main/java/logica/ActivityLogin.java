package logica;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import soy_activista.quartzapp.com.soy_activista.R;


public class ActivityLogin extends AppCompatActivity {
    private EditText identificador,pass;
    private String usuario, contrasena;
    private ProgressDialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantallalogin);

        //asignamos los controles a las variables
        identificador=(EditText)findViewById(R.id.textMail);
        pass=(EditText)findViewById(R.id.editTextPass);
    }

    public void iniciarSesion(View view) {
        dialog = ProgressDialog.show(ActivityLogin.this, "","Verificando usuario...", true);
        usuario = identificador.getText().toString();
        contrasena = pass.getText().toString();

        ParseUser.logInInBackground(usuario, contrasena,
                new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if(user != null){
                            dialog.dismiss();
                            Intent i = new Intent(getApplication(),FActivityPantallaMenu.class);
                            startActivity(i);
                            finish();
                        }else{
                            dialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Identificador o Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
