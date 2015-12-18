package soy_activista;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;

import logica.Mensaje;
import logica.Usuario;

public class SoyActivistaApp extends Application{

    @Override
    public void onCreate(){
        super.onCreate();

        // Activar los datos locales
        Parse.enableLocalDatastore(this);

        // Codigo de inicializacion con la app Parse
        Parse.initialize(this, "NDe2nkVIGWqg7wvGb7oUP2nMpjCQKn2QHwQ5rrMu", "hxb19tyAVqkD4MJ0AqU0bnXx6r9ihywvOaPOokPu");
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }
}
