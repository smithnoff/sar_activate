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

import logica.ListarUsuarioAdapter;
import logica.Usuario;
import soy_activista.quartzapp.com.soy_activista.R;


public class FragmentListarUsuarioOLD extends Fragment {


    public FragmentListarUsuarioOLD(){}

    public static FragmentListarUsuarioOLD fragConstruct(Bundle arguments){
        FragmentListarUsuarioOLD f = new FragmentListarUsuarioOLD();
        if(arguments != null){
            f.setArguments(arguments);
        }
        return f;
    }

    ArrayList<Usuario> usuarioArrayList = new ArrayList<>();
    Usuario usuario;
    Dialog dialogo;
    private FloatingActionButton boton;

    ListView ListaUsuarioView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_listar_usuarioold, container, false);

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
                        usuario.setNombre((String) object.get(i).get("nombre"));
                        usuario.setApellido((String) object.get(i).get("apellido"));
                        usuario.setCorreo(object.get(i).getEmail());
                        usuario.setIdentificador(object.get(i).getUsername()/*.toLowerCase()*/);
                        usuario.setCargo((String) object.get(i).get("cargo"));
                        usuario.setEstado((String) object.get(i).get("estado"));
                        usuario.setMunicipio((String) object.get(i).get("municipio"));
                        usuario.setComite(object.get(i).getString("comite"));
                        usuario.setRol(object.get(i).getInt("rol"));
                        usuarioArrayList.add(usuario);

                    }
                    listaUsuarioView.setAdapter(null);
                    ListarUsuarioAdapter adaptador;
                    if(arg!=null){
                        adaptador = new ListarUsuarioAdapter(getActivity(), filtrarPorID(arg.getString("query")/*.toLowerCase()*/, usuarioArrayList));
                    }
                    else{
                        adaptador = new ListarUsuarioAdapter(getActivity(), usuarioArrayList);
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
                            informacion.add(usuario.getComite());
                            informacion.add(usuario.getRolName());


                            Bundle informacionUsuario = new Bundle();
                            informacionUsuario.putStringArrayList("Informacion",informacion);

                            Fragment fragment = new FragmentEditarMilitante();
                            fragment.setArguments(informacionUsuario);
                            getFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.content_frame, fragment)
                                    .addToBackStack(null)
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
        ListarUsuarioAdapter adaptador;
        if(!usuarioArrayList.isEmpty()){

                if(item.equalsIgnoreCase("Todos los estados")){
                    ListaUsuarioView.setAdapter(null);
                    adaptador = new ListarUsuarioAdapter(getActivity(), usuarioArrayList);
                    ListaUsuarioView.setAdapter(adaptador);
                }
                else{
                    for(int i=0; i< usuarioArrayList.size(); i++){
                        if(item.equals((String) usuarioArrayList.get(i).getEstado()))
                            resultado.add(usuarioArrayList.get(i));
                    }
                    if(!resultado.isEmpty()){
                        Toast.makeText(getActivity(), "No existen usuarios en esta seleccion.", Toast.LENGTH_SHORT).show();
                        ListaUsuarioView.setAdapter(null);
                        adaptador = new ListarUsuarioAdapter(getActivity(), resultado);
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
