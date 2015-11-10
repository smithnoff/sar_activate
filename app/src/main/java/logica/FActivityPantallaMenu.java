package logica;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.parse.ParseUser;

import com.example.usuario.soyactivista.fragments.FragmentDashBoard;
import com.example.usuario.soyactivista.fragments.FragmentEditarMilitante;
import com.example.usuario.soyactivista.fragments.FragmentListarUsuario;
import com.example.usuario.soyactivista.fragments.FragmentRegistrarMilitante;
import soy_activista.quartzapp.com.soy_activista.R;


/**
 * Created by Usuario on 07/10/2015.
 */

public class FActivityPantallaMenu extends AppCompatActivity {
    private Toolbar appbar;
    private DrawerLayout drawerLayout;
    private NavigationView navView;
    private TextView tv1, tv2, tv3, tv4;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_con_menu);

        tv1 = (TextView)findViewById(R.id.usuarioPartido);
        tv2 = (TextView)findViewById(R.id.usuarioID);
        tv3 = (TextView)findViewById(R.id.usuarioCargo);
        tv4 = (TextView)findViewById(R.id.usuarioEstado);



        final ParseUser usuarioActual = ParseUser.getCurrentUser();
        if(usuarioActual != null){
            tv2.setText(usuarioActual.getString("Nombre") +" "+ usuarioActual.getString("Apellido"));
            tv3.setText(usuarioActual.getString("Cargo"));
            tv4.setText(usuarioActual.getString("Estado") +", "+ usuarioActual.getString("Municipio"));
        }

        appbar = (Toolbar)findViewById(R.id.appbar);
        setSupportActionBar(appbar);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_nav_menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        appbar.setTitle("PRUEBA");
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, new FragmentDashBoard()/*new FragmentMenuPrincipal()*/)
                .commit();

        navView = (NavigationView)findViewById(R.id.navview);
        navView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        boolean fragmentTransaction = false;
                        Fragment fragment = null;

                        switch (menuItem.getItemId()) {
                            case R.id.MenuDashBoard:
                                fragment = new FragmentDashBoard();
                                ocultar(false,R.id.buscador);
                                fragmentTransaction = true;
                                break;

                            case R.id.MenuAgregarUsu:
                                fragment = new FragmentRegistrarMilitante();
                                ocultar(false,R.id.buscador);
                                fragmentTransaction = true;
                                break;

                            case R.id.MenuListarUsu: {
                                fragment = FragmentListarUsuario.fragConstruct(null);/*new FragmentListarUsuario();*/
                                ocultar(true,R.id.buscador);
                                fragmentTransaction = true;
                                break;
                            }
                            case R.id.EliminarCuenta:
                                ParseUser user = ParseUser.getCurrentUser();
                                user.deleteInBackground();
                                Intent i = new Intent(getApplication(),pantalla_principal.class);
                                startActivity(i);
                                finish();
                                break;

                            case R.id.MenuMiPerfil:
                                fragment = new FragmentEditarMilitante();
                                fragmentTransaction = true;
                                break;

                        }

                        if(fragmentTransaction) {
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.content_frame, fragment)
                                    .commit();

                            menuItem.setChecked(true);
                            getSupportActionBar().setTitle(menuItem.getTitle());
                        }

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
        this.ocultar(false, R.id.buscador);
        SearchView sv = (SearchView) menu.findItem(R.id.buscador).getActionView();
        sv.setQueryHint(getString(R.string.hintBuscador));

        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(query.length() > 0){ //si escribió algo!
                    Bundle parametro = new Bundle();
                    parametro.putString("query",query);
                    Fragment fragment=FragmentListarUsuario.fragConstruct(parametro);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content_frame, fragment)
                            .commit();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_settings:
                ParseUser.logOut();
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void ocultar(boolean visible,int QueOculto){
        this.menu.findItem(QueOculto).setVisible(visible);
    }
}
