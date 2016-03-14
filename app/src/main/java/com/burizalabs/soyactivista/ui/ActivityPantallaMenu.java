package com.burizalabs.soyactivista.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.burizalabs.soyactivista.R;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import com.burizalabs.soyactivista.utils.Selector_de_Tema;

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
    private ImageView photoPartido;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        ParseUser currentUser = ParseUser.getCurrentUser();

        // Check if user is logged
        if(currentUser == null){
            Log.d(TAG, "User is null");
            Intent i = new Intent(getApplication(),ActivityPantallaInicio.class);
            startActivity(i);
            finish();
        }
        else{
            // User not null
            try {
                Selector_de_Tema.onActivityCreateSetTheme(this);
            } catch (ParseException e) {
                Log.d(TAG,"Theme could not be retrieved. "+e.getMessage());
            }

            setContentView(R.layout.activity_pantalla_con_menu);

            /* TODO: as of 23.1.1 of the support library implement
                View header = navigationView.getHeaderView(0);
                TextView text = (TextView) header.findViewById(R.id.textView);
             */

            NavigationView navigationView = (NavigationView) findViewById(R.id.navview);

            View header = LayoutInflater.from(this).inflate(R.layout.header_navview, null);

            navigationView.addHeaderView(header);

            // Menu Header Text
            nombrePartido = (TextView)header.findViewById(R.id.usuarioPartido);
            nombreUsuario = (TextView)header.findViewById(R.id.usuarioID);
            cargoUsuario = (TextView)header.findViewById(R.id.usuarioCargo);
            ubicacionUsuario = (TextView)header.findViewById(R.id.usuarioEstado);
            photoPartido = (ImageView)header.findViewById(R.id.photo_partido);

            nombrePartido.setText(Selector_de_Tema.getNombrePartido());
            nombreUsuario.setText(currentUser.getString("nombre") + " " + currentUser.getString("apellido"));
            cargoUsuario.setText(currentUser.getString("cargo"));
            ubicacionUsuario.setText(currentUser.getString("estado") + ", " + currentUser.getString("municipio"));

            ParseFile foto = Selector_de_Tema.getImage();
            if (foto != null)
            {
                String fileName = foto.getName();
                String extension = fileName.substring((fileName.lastIndexOf(".") + 1), fileName.length());
                //Attached File is an Image
                if (extension.equalsIgnoreCase("jpg"))
                {
                    String url = foto.getUrl();
                    Glide.with(getApplicationContext()).load(url).asBitmap().centerCrop().into(new BitmapImageViewTarget(photoPartido) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(getApplicationContext().getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            photoPartido.setImageDrawable(circularBitmapDrawable);
                        }
                    });
                }
            }

            appbar = (Toolbar)findViewById(R.id.appbar);
            setSupportActionBar(appbar);

            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_nav_menu);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            appbar.setTitle("Soy Activista");

            drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
            barDraw= (LinearLayout) header.findViewById(R.id.barraDrawer);

            int color = Color.TRANSPARENT;

            Drawable background = appbar.getBackground();

            if (background instanceof ColorDrawable)
                color = ((ColorDrawable) background).getColor();

            barDraw.setBackgroundColor(color);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
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
            if(currentUser.getInt("rol") != 1){
                actividadesPartido.setVisible(false);
                listarUsuario.setVisible(false);
                agregarUsuario.setVisible(false);
                editarPartido.setVisible(false);
            }

            final ParseUser userForMenu = ParseUser.getCurrentUser();

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

                                case R.id.menuMensajeDirecto:
                                    fragment = new FragmentListarConversacion();
                                    fragmentTransaction = true;
                                    break;

                                case R.id.menuBoletinActividades:
                                    fragment = new FragmentListarActividad();
                                    fragmentTransaction = true;
                                    break;

                                case R.id.menuManejarActividades:
                                    fragment = new FragmentListarTipoActividad();
                                    fragmentTransaction = true;
                                    break;

                                case R.id.menuTrivia:
                                    fragment = new FragmentTriviaPrincipal();
                                    fragmentTransaction = true;
                                    break;

                                // PLACEHOLDER PUNTUACIONES
                                case R.id.menuTablaPuntos:
                                    fragment = new FragmentPuntuaciones();
                                    fragmentTransaction = true;
                                    break;

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
                                    fragment = new FragmentEditarUsuario();
                                    fragmentTransaction = true;
                                    break;

                                case R.id.menuCerrarSesion:
                                    userForMenu.logOutInBackground();
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
        } // END IF USER NULL
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
}
