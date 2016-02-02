package com.example.usuario.soyactivista.fragments;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import logica.ErrorCodeHelper;
import logica.Estados;
import logica.ListarRankingEstadosAdapter;
import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by Luis Adrian on 19/01/2016.
 */
public class FragmentTabRanking extends Fragment{
    private ImageView bolivar;
    private LinearLayout parentLayout;
    private ImageView deltaAmacuro;
    private RecyclerView recyclerView;
    private ListarRankingEstadosAdapter listarUsuarioAdapter;
    private List<Estados> estadosArrayList = new ArrayList<>();
    private Estados estado;
    private int sumUser;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tab_ranking, container, false);

        parentLayout = (LinearLayout)v.findViewById(R.id.parentLayout);

        /*LayoutInflater infl = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View childLayout = infl.inflate(R.layout.map_venezuela, (ViewGroup) v.findViewById(R.id.venezuelaMap));
        parentLayout.addView(childLayout);

        deltaAmacuro = (ImageView)childLayout.findViewById(R.id.deltaAmacuro);
        deltaAmacuro.setColorFilter(Color.argb(255, 51, 51, 255));*/

        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerListTopEstados);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        initializeList(estadosArrayList);

        return v;
    }

    public int contarUsuarios(String nombreEstado){
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("estado",nombreEstado);
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> object, ParseException e)
            {
                if (e == null)
                { //no hay error
                    object.size();
                    if (object.size()>0)
                        sumUser = object.size();
                    else
                        sumUser = 0;
                } else {
                    Toast.makeText(getActivity(), ErrorCodeHelper.resolveErrorCode(e.getCode()), Toast.LENGTH_LONG).show();
                }
            }
        });
        return sumUser;
    }

    public void initializeList(final List<Estados> list){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("RankingEstados");
        query.orderByDescending("puntos");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> object, ParseException e) {
                if (e == null) { //no hay error

                    for (int i = 0; i < object.size(); i++) {
                        estado = new Estados();
                        String nombreEstado = object.get(i).getString("nombre");
                        estado.setNombreEstado(object.get(i).getString("nombre"));
                        estado.setPuntos(object.get(i).getInt("puntos"));
                        estado.setCantidadUsuarios(contarUsuarios(nombreEstado));
                        list.add(estado);
                    }
                    recyclerView.setAdapter(new ListarRankingEstadosAdapter(list));

                } else {
                    Toast.makeText(getActivity(), ErrorCodeHelper.resolveErrorCode(e.getCode()), Toast.LENGTH_LONG).show();
                }
            }
        });

    }





}
