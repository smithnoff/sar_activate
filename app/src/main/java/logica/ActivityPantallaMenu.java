package logica;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.usuario.soyactivista.fragments.FragmentCrearUsuario;
import com.example.usuario.soyactivista.fragments.FragmentEditarPartido;
import com.example.usuario.soyactivista.fragments.FragmentListarActividad;
import com.example.usuario.soyactivista.fragments.FragmentListarMensaje;
import com.example.usuario.soyactivista.fragments.FragmentListarTipoActividad;
import com.example.usuario.soyactivista.fragments.FragmentListarUsuario;
import com.example.usuario.soyactivista.fragments.FragmenteEditarUsuario;
import com.parse.ParseException;
import com.parse.ParseUser;

import soy_activista.quartzapp.com.soy_activista.R;


/**
 * Created by Usuario on 07/10/2015.
 */

public class ActivityPantallaMenu extends AppCompatActivity {

    private static final String TAG = "Act-Menu";
    private Toolbar appbar;
    private DrawerLayout drawerLayout;
    private NavigationView navView;
    private TextView nombrePartido, nombreUsuario, cargoUsuario, ubicacionUsuario;
    private Menu menu;
    private LinearLayout barDraw;
    private int colorChecked;
    private  View vistaAntrior;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Selector_de_Tema.onActivityCreateSetTheme(this);
        } catch (ParseException e) {
            Log.d(TAG,"Theme could not be retrieved.");
        }

        setContentView(R.layout.pantalla_con_menu);

        // Menu Header Text
        nombrePartido = (TextView)findViewById(R.id.usuarioPartido);
        nombreUsuario = (TextView)findViewById(R.id.usuarioID);
        cargoUsuario = (TextView)findViewById(R.id.usuarioCargo);
        ubicacionUsuario = (TextView)findViewById(R.id.usuarioEstado);

        // Gets Current User
        final ParseUser usuarioActual = ParseUser.getCurrentUser();
            nombrePartido.setText(Selector_de_Tema.getNombrePartido());
        if(usuarioActual != null) {
            nombreUsuario.setText(usuarioActual.getString("nombre") + " " + usuarioActual.getString("apellido"));
            cargoUsuario.setText(usuarioActual.getString("cargo"));
            ubicacionUsuario.setText(usuarioActual.getString("estado") + ", " + usuarioActual.getString("municipio"));
        }
        else{
            // TODO:No user logged in -> Redirect to Login.
            Intent i = new Intent(getApplication(),ActivityPantallaInicio.class);
            startActivity(i);
            finish();
        }

        appbar = (Toolbar)findViewById(R.id.appbar);
        setSupportActionBar(appbar);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_nav_menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        appbar.setTitle("Soy Activista");

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
          barDraw= (LinearLayout) findViewById(R.id.barraDrawer);
        int color = Color.TRANSPARENT;
        Drawable background = appbar.getBackground();
        if (background instanceof ColorDrawable)
            color = ((ColorDrawable) background).getColor();
   barDraw.setBackgroundColor(color);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(color);
        }
        // Load Message Dashboard As Main Screen
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, new FragmentListarMensaje() /*new FragmentMenuPrincipal()*/)
                .commit();

        navView = (NavigationView)findViewById(R.id.navview);

        Menu navMenu = navView.getMenu();

        MenuItem actividadesPartido = navMenu.findItem(R.id.menuManejarActividades);
        MenuItem listarUsuario = navMenu.findItem(R.id.menuListarUsuario);
        MenuItem agregarUsuario = navMenu.findItem(R.id.menuAgregarUsuario);
        MenuItem editarPartido = navMenu.findItem(R.id.menuEditarPartido);

        // Disable Menu Items if not admin user
        if(usuarioActual.getInt("rol") != 1){
            actividadesPartido.setVisible(false);
            listarUsuario.setVisible(false);
            agregarUsuario.setVisible(false);
            editarPartido.setVisible(false);
        }

        navView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        boolean fragmentTransaction = false;
                        Fragment fragment = null;

                        switch (menuItem.getItemId()) {
                            case R.id.menuDashBoard:
                                fragment = new FragmentListarMensaje();
                                fragmentTransaction = true;
                                break;

                            // PLACEHOLDER MENSAJES DIRECTOS

                            case R.id.menuBoletinActividades:
                                fragment = new FragmentListarActividad();
                                fragmentTransaction = true;
                                break;

                            case R.id.menuManejarActividades:
                                fragment = new FragmentListarTipoActividad();
                                fragmentTransaction = true;
                                break;

                            // PLACEHOLDER TRIVIA

                            // PLACEHOLDER PUNTUACIONES

                            case R.id.menuListarUsuario:
                                fragment = new FragmentListarUsuario();
                                fragmentTransaction = true;
                                break;

                            case R.id.menuAgregarUsuario:
                                fragment = new FragmentCrearUsuario();
                                fragmentTransaction = true;
                                break;

                            case R.id.menuEditarPartido:
                                fragment = new FragmentEditarPartido();
                                fragmentTransaction = true;
                                break;

                            case R.id.menuMiPerfil:
                                fragment = new FragmenteEditarUsuario();
                                fragmentTransaction = true;
                                break;

                            case R.id.menuCerrarSesion:
                                usuarioActual.logOutInBackground();
                                Intent i = new Intent(getApplication(),ActivityPantallaInicio.class);
                                startActivity(i);
                                finish();
                                break;

                            default:
                                fragment = new FragmentListarMensaje();
                                fragmentTransaction = true;
                                break;
                        }

                        // Conclude Fragment loading/view Change
                        if(fragmentTransaction) {
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.content_frame, fragment)
                                    .addToBackStack(null)
                                    .commit();

                            menuItem.setChecked(true);
                            getSupportActionBar().setTitle(menuItem.getTitle());
                        }

                        // Close Menu
                        drawerLayout.closeDrawers();

                        return true;
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pantalla_principal, menu);
        this.menu=menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }



    public void checkedView(View v) {

        //set the selected color
        colorChecked = v.getId();
        if(vistaAntrior==null)
            vistaAntrior=v;

        if(v.getId()==R.id.themeBrown) {
            v.setBackground(getResources().getDrawable(R.drawable.circulomarron));
        }else {
            if(vistaAntrior.getId()==R.id.themeBrown)
                vistaAntrior.setBackground(getResources().getDrawable(R.drawable.circulobr));
        }
        if(v.getId()==R.id.themeOrange) {
            v.setBackground(getResources().getDrawable(R.drawable.circulonaranja));
        }else {
            if(vistaAntrior.getId()==R.id.themeOrange)
                vistaAntrior.setBackground(getResources().getDrawable(R.drawable.circulon));
        }
        if(v.getId()==R.id.themeBlue) {
            v.setBackground(getResources().getDrawable(R.drawable.circuloazul));
        }else {
            if(vistaAntrior.getId()==R.id.themeBlue)
                vistaAntrior.setBackground(getResources().getDrawable(R.drawable.circuloaz));
        }
        if(v.getId()==R.id.themeRed) {
            v.setBackground(getResources().getDrawable(R.drawable.circulorojo));
        }else {
            if(vistaAntrior.getId()==R.id.themeRed)
                vistaAntrior.setBackground(getResources().getDrawable(R.drawable.circulor));
        }
        if(v.getId()==R.id.themeGreen) {
            v.setBackground(getResources().getDrawable(R.drawable.circuloverde));
        }else {
            if(vistaAntrior.getId()==R.id.themeGreen)
                vistaAntrior.setBackground(getResources().getDrawable(R.drawable.circulov));
        }
        if(v.getId()==R.id.themeYellow) {
            v.setBackground(getResources().getDrawable(R.drawable.circuloamarillo));
        }else {
            if(vistaAntrior.getId()==R.id.themeYellow)
                vistaAntrior.setBackground(getResources().getDrawable(R.drawable.circuloam));
        }
        if(v.getId()==R.id.themePurple) {
            v.setBackground(getResources().getDrawable(R.drawable.circulopurpura));
        }else {
            if(vistaAntrior.getId()==R.id.themePurple)
                vistaAntrior.setBackground(getResources().getDrawable(R.drawable.circulop));
        }
        if(v.getId()==R.id.themeDefault) {
            v.setBackground(getResources().getDrawable(R.drawable.circuloindigo));
        }else {
            if(vistaAntrior.getId()==R.id.themeDefault)
                vistaAntrior.setBackground(getResources().getDrawable(R.drawable.circulo));
        }
        vistaAntrior=v;

    }

    // Receive Actions from Edit Party Theme. Saves Values and redraws as needed
    public void cambiarTema(View v)
    {
        EditText nPartido= (EditText) findViewById(R.id.editNombrePartido);

        //select de theme color
        switch(colorChecked)
        {
            case R.id.themeBrown:
                Selector_de_Tema.changeToTheme(ActivityPantallaMenu.this, Selector_de_Tema.BROWN,nPartido.getText().toString());
                break;
            case R.id.themeBlue:
                Selector_de_Tema.changeToTheme(ActivityPantallaMenu.this, Selector_de_Tema.BLUE,nPartido.getText().toString());
                break;
            case R.id.themeRed:
                Selector_de_Tema.changeToTheme(ActivityPantallaMenu.this, Selector_de_Tema.RED,nPartido.getText().toString());
                break;
            case R.id.themeDefault:
                Selector_de_Tema.changeToTheme(ActivityPantallaMenu.this, Selector_de_Tema.DEFAULT,nPartido.getText().toString());
                break;
            case R.id.themeOrange:
                Selector_de_Tema.changeToTheme(ActivityPantallaMenu.this, Selector_de_Tema.ORANGE,nPartido.getText().toString());
                break;
            case R.id.themeGreen:
                Selector_de_Tema.changeToTheme(ActivityPantallaMenu.this, Selector_de_Tema.GREEN,nPartido.getText().toString());
                break;
            case R.id.themePurple:
                Selector_de_Tema.changeToTheme(ActivityPantallaMenu.this, Selector_de_Tema.PURPLE,nPartido.getText().toString());
                break;
            case R.id.themeYellow:
                Selector_de_Tema.changeToTheme(ActivityPantallaMenu.this, Selector_de_Tema.YELLOW,nPartido.getText().toString());
                break;
        }
    }
}
