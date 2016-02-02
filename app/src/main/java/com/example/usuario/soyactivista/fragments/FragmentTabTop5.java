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

import java.util.ArrayList;
import java.util.List;

import logica.ErrorCodeHelper;
import logica.Estados;
import logica.ListarRankingEstadosAdapter;
import soy_activista.quartzapp.com.soy_activista.R;
/**
 * Created by Luis Adrian on 19/01/2016.
 */
public class FragmentTabTop5 extends Fragment {
    private ImageView bolivar;
    private LinearLayout parentLayout;
    private ImageView venezuela;
    private RecyclerView recyclerView;
    private ListarRankingEstadosAdapter listarUsuarioAdapter;
    private List<Estados> estadosArrayList = new ArrayList<>();
    private Estados estado;
    private int sumUser;

    public FragmentTabTop5() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v=inflater.inflate(R.layout.fragment_tab_top_5, container, false);

        /*parentLayout = (LinearLayout)v.findViewById(R.id.parentLayout);

        LayoutInflater infl = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View childLayout = infl.inflate(R.layout.map_venezuela, (ViewGroup) v.findViewById(R.id.venezuelaMap));
        parentLayout.addView(childLayout);

        venezuela = (ImageView)childLayout.findViewById(R.id.deltaAmacuro);
        venezuela.setColorFilter(Color.argb(255, 51, 51, 255));*/



        recyclerView = (RecyclerView) v.findViewById(R.id.recyclertop5);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        initializeList(estadosArrayList);

        return v;
    }

    public void initializeList(final List<Estados> list){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("RankingEstados");
        query.orderByDescending("puntos");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> object, ParseException e) {
                if (e == null) { //no hay error

                    for (int i = 0; i < 5; i++) {

                        estado = new Estados();
                        estado.setNombreEstado(object.get(i).getString("nombre"));
                        estado.setPuntos(object.get(i).getInt("puntos"));
                        estado.setPosicion(i+1);
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
