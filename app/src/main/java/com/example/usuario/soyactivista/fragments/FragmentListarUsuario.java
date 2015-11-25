package com.example.usuario.soyactivista.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import logica.ListaUsuarioAdapter;
import logica.Usuario;
import soy_activista.quartzapp.com.soy_activista.R;




public class FragmentListarUsuario extends Fragment {


    public FragmentListarUsuario(){}

    public static FragmentListarUsuario fragConstruct(Bundle arguments){
        FragmentListarUsuario f = new FragmentListarUsuario();
        if(arguments != null){
            f.setArguments(arguments);
        }
        return f;
    }

    ArrayList<Usuario>US=new ArrayList<>();
    Usuario usuario;
    Dialog dialogo;
    private FloatingActionButton boton;

    ListView ListaUsuarioView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_listar_usuario, container, false);

        ListaUsuarioView=(ListView)v.findViewById(R.id.listUsuariosView);

        boton = (FloatingActionButton) v.findViewById(R.id.fabEstados);
        this. llenarLista(ListaUsuarioView,getArguments());
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                construirDialogo(); //para el dialogo donde listo los estado
            }
        });
        return v;
    }


    private void construirDialogo(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Listar por estado");
        final ListView modeList = new ListView(getActivity());


        ArrayList<String>lista=new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.Estados)));
        lista.add(0,"Todos los estados");
        ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,lista);
        modeList.setAdapter(modeAdapter);
        builder.setView(modeList);

        modeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //re-hace la lista de la pantalla para que filtre:
                filtrar((String) modeList.getItemAtPosition(position));
                dialogo.dismiss();

            }
        });
        dialogo=builder.create();
        dialogo.show();
    }
    private void llenarLista(final ListView listaUsuarioView,Bundle argumento) {
        final ProgressDialogFragment pg = new ProgressDialogFragment();
        final Bundle arg=argumento;
        pg.setTitulo("Listando");
        pg.setMensajeCargando("Consultando...");
        pg.show(getFragmentManager(),"cargando");

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.findInBackground(new FindCallback<ParseUser>() {

            public void done(List<ParseUser> object, ParseException e) {

                if (e == null) { //no hay error
                    for (int i = 0; i < object.size(); i++) {
                        usuario = new Usuario();
                        usuario.setNombre((String) object.get(i).get("Nombre"));
                        usuario.setApellido((String) object.get(i).get("Apellido"));
                        usuario.setCorreo(object.get(i).getEmail());
                        usuario.setIdentificador(object.get(i).getUsername()/*.toLowerCase()*/);
                        usuario.setCargo((String) object.get(i).get("Cargo"));
                        usuario.setEstado((String) object.get(i).get("Estado"));
                        usuario.setMunicipio((String) object.get(i).get("Municipio"));
                        usuario.setPertenencia(object.get(i).getString("Comite"));
                        usuario.setRol(object.get(i).getInt("Rol"));
                        US.add(usuario);

                    }
                    listaUsuarioView.setAdapter(null);
                    ListaUsuarioAdapter adaptador;
                    if(arg!=null){
                        adaptador = new ListaUsuarioAdapter(getActivity(), filtrarPorID(arg.getString("query")/*.toLowerCase()*/,US), R.layout.items_listado_usuarios);
                    }
                    else{
                        adaptador = new ListaUsuarioAdapter(getActivity(), US, R.layout.items_listado_usuarios);
                    }
                    listaUsuarioView.setAdapter(adaptador);
                    pg.dismiss();

                    listaUsuarioView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            usuario = (Usuario) listaUsuarioView.getItemAtPosition(position);

                            ArrayList<String> informacion = new ArrayList<String>();
                            informacion.add(usuario.getIdentificador());
                            informacion.add(usuario.getNombre());
                            informacion.add(usuario.getApellido());
                            informacion.add(usuario.getCorreo());
                            informacion.add(usuario.getCargo());
                            informacion.add(usuario.getEstado());
                            informacion.add(usuario.getMunicipio());
                            informacion.add(usuario.getPetenencia());
                            informacion.add(usuario.getRolName());


                            Bundle informacionUsuario = new Bundle();
                            informacionUsuario.putStringArrayList("Informacion",informacion);

                            Fragment fragment = new FragmentEditarMilitante();
                            fragment.setArguments(informacionUsuario);
                            getFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.content_frame, fragment)
                                    .commit();
                        }
                    });

                } else {
                    /*Log.d("HORROR", "Error: " + e.getMessage());*/
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    pg.dismiss();
                }
            }
        });
    }

    private void filtrar(String item){
        ArrayList<Usuario>resultado=new ArrayList<>();
        ListaUsuarioAdapter adaptador;
        if(!US.isEmpty()){

                if(item.equalsIgnoreCase("Todos los estados")){
                    ListaUsuarioView.setAdapter(null);
                    adaptador = new ListaUsuarioAdapter(getActivity(), US, R.layout.items_listado_usuarios);
                    ListaUsuarioView.setAdapter(adaptador);
                }
                else{
                    for(int i=0; i<US.size(); i++){
                        if(item.equals((String)US.get(i).getEstado()))
                            resultado.add(US.get(i));
                    }
                    if(!resultado.isEmpty()){
                        ListaUsuarioView.setAdapter(null);
                        adaptador = new ListaUsuarioAdapter(getActivity(), resultado, R.layout.items_listado_usuarios);
                        ListaUsuarioView.setAdapter(adaptador);
                    }
                }


        }
        else{
            Toast.makeText(getActivity(), "No existen usuarios registrados", Toast.LENGTH_SHORT).show();
        }
    }

    ArrayList<Usuario>filtrarPorID(String id,ArrayList<Usuario> listaUsuario){
        ArrayList<Usuario> retorno = new ArrayList<>();
        for(int i=0; i<listaUsuario.size(); i++){
            if( (id.equalsIgnoreCase((String)listaUsuario.get(i).getIdentificador())) || (id.equalsIgnoreCase((String)listaUsuario.get(i).getNombre()))
                    || (id.equalsIgnoreCase((String)listaUsuario.get(i).getApellido())) )
                retorno.add(listaUsuario.get(i));
        }
        if(!retorno.isEmpty()){
            return retorno;
        }
        else{
            Toast.makeText(getActivity(), "No se encuentra resultado en la busqueda", Toast.LENGTH_SHORT).show();
            return listaUsuario;
        }
    }


}
