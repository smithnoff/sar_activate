package com.burizalabs.soyactivista.ui;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.burizalabs.soyactivista.utils.ColorHelpers;
import com.burizalabs.soyactivista.entities.Entidad;
import com.burizalabs.soyactivista.utils.ErrorCodeHelpers;
import com.burizalabs.soyactivista.adapters.ListarRankingEntidadesAdapter;
import com.burizalabs.soyactivista.utils.TextHelpers;
import com.burizalabs.soyactivista.R;
/**
 * Created by Luis Adrian on 19/01/2016.
 */
public class FragmentTabTop5 extends Fragment {

    private static final String TAG = "FragmentTabTop5"; //  For Log

    private RecyclerView recyclerView; // List Holder
    private RelativeLayout mapContainer; // Map Container
    private View map; // Map Holder
    private View view; // Main Layout

    // List Vars
    private ListarRankingEntidadesAdapter adapter;
    private ArrayList<Entidad> entidadArrayList;

    // Arguments
    private String entityName;
    private String entityId;

    private ParseUser currentUser = ParseUser.getCurrentUser();


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
        Log.d(TAG,"Loading Fragment Top 5");

        view = inflater.inflate(R.layout.fragment_tab_top_5, container, false);

        mapContainer = (RelativeLayout) view.findViewById(R.id.map_container);

        // Recover data from arguments
        if( getArguments() != null){
            Log.d(TAG,"Fragment has arguments");
            entityName = getArguments().getString("estado");
            entityId = getArguments().getString("estadoId");
        }

        loadMap();

        // Inititalize Entity Rank List

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        entidadArrayList = new ArrayList<>();

        Boolean clickable = true;

        if ( getArguments() != null)
            clickable = false;

        if (getArguments() != null) {
            adapter = new ListarRankingEntidadesAdapter(getActivity(), entidadArrayList, clickable, true, true);
        } else {
            adapter = new ListarRankingEntidadesAdapter(getActivity(), entidadArrayList, clickable, true, false);
        }

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_top_5);

        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(adapter);

        // Download entities from DB

        initializeList();

        // Enable Options
        setHasOptionsMenu(true);

        return view;
    }


    // Map is loaded according to arguments whether it is at national or state level.
    private void loadMap() {

        int layoutId = 0;
        int containerId = 0;
        String resource = "map_venezuela";

        if( entityName != null ){
            resource = TextHelpers.NormalizeResource(getArguments().getString("estado"));

            // Look for resource id
            Log.d(TAG,"Looking for map file: map_"+resource);
            layoutId = getResources().getIdentifier("map_"+resource, "layout", getActivity().getPackageName());
            containerId = getResources().getIdentifier("map_"+resource, "id",getActivity().getPackageName());

        }
        else{
            // Look for resource id
            Log.d(TAG,"Looking for map file: map_venezuela");
            layoutId = getResources().getIdentifier(resource, "layout", getActivity().getPackageName());
            containerId = getResources().getIdentifier("map_venezuela", "id",getActivity().getPackageName());
        }

        LayoutInflater infl = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        try {
            map = infl.inflate(layoutId, (ViewGroup) view.findViewById(containerId));
        }
        catch (Resources.NotFoundException e){
            // Map not found
            Toast.makeText(getContext(),"Map not found : "+resource,Toast.LENGTH_LONG).show();
            Log.e(TAG,"Map resource not found : map_"+resource);
        }


        // Check if resource was found.
        if( map != null) {
            // Map Found
            mapContainer.addView(map);
        }
    }

    public void initializeList(){
        // TODO: Save queried list in activity and ask for it on creation.

        ParseQuery<ParseObject> query;

        if(getArguments() != null){
            // Generate state object without data
            query = ParseQuery.getQuery("RankingMunicipios");
            query.whereEqualTo("estado",getArguments().getString("estado"));
            Log.d(TAG, "Querying for estado " + getArguments().getString("estado"));
        }
        else{
            query = ParseQuery.getQuery("RankingEstados");
        }

        //query.whereGreaterThan("puntos", 0);
        query.setLimit(5);
        query.orderByDescending("puntos");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> object, ParseException e) {
                if (e == null) { //no hay error
                    Log.d(TAG, object.size() + " entities retrieved");
                    Entidad entidad;
                    for (int i = 0; i < object.size(); i++) {
                        entidad = new Entidad();
                        entidad.setId(object.get(i).getObjectId());
                        entidad.setNombre(object.get(i).getString("nombre"));
                        entidad.setPuntos(object.get(i).getInt("puntos"));
                        entidad.setUsuarios(object.get(i).getInt("usuarios"));
                        entidad.setPosicion(i + 1);
                        colorEntity(entidad.getNombre(),entidad.getPuntos());
                        entidadArrayList.add(entidad);
                    }
                    // Reload List
                    adapter.notifyDataSetChanged();

                } else {
                    // Log & display error
                    Log.d(TAG, ErrorCodeHelpers.resolveLogErrorString(e.getCode(), e.getMessage()));
                    Toast.makeText(getActivity(), ErrorCodeHelpers.resolveErrorCode(e.getCode()), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void colorEntity(String nombre, int puntos) {

        // Check if map not null from previous validations, log & abort
        if( map == null ){
            return;
        }

        // Get Resource ID
        String finalName = TextHelpers.NormalizeResource(nombre);
        int imageViewId = getResources().getIdentifier("map_"+ finalName, "id",getActivity().getPackageName());
        ImageView vector = (ImageView) map.findViewById(imageViewId);

        // Check if image not found log & abort.
        if ( vector == null){
            Log.e(TAG,"Resource not found: map_"+finalName);
            return;
        }

        // Get Primary Color
        TypedValue typedValue = new TypedValue();
        getContext().getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        int color = typedValue.data;

        // Paint with Primary Color.
        vector.setColorFilter(ColorHelpers.getGradient(color, puntos), PorterDuff.Mode.SRC_IN);
    }

    // Inflates custom menu for fragment.
    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        // Inflate Custom Menu
        inflater.inflate(R.menu.menu_puntuaciones, menu);

        ParseUser currentUser = ParseUser.getCurrentUser();

        MenuItem reiniciarPuntuacion = menu.findItem(R.id.reiniciar_puntuacion);

        if(currentUser != null && currentUser.getInt("rol") == 1){
            reiniciarPuntuacion.setVisible(true);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.ayuda:
                Fragment fragment = new FragmentAyudaTop5();
                fragment.setArguments(getArguments());
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, fragment)
                        .addToBackStack(null)
                        .commit();
                break;

            case R.id.buscar_entidad:
                // Generate List Holder
                final android.support.v7.app.AlertDialog filterDialogEstadales;
                android.support.v7.app.AlertDialog.Builder builderEstadales = new android.support.v7.app.AlertDialog.Builder(getActivity());
                builderEstadales.setTitle("Seleccionar Estado");

                // Fill Holder with State List from String Array
                final ListView listViewDialogEstadales = new ListView(getActivity());
                final ArrayList<String> arrayListEstadales = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.Estados)));

                ArrayAdapter<String> stringArrayAdapterEstadales = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,arrayListEstadales);
                listViewDialogEstadales.setAdapter(stringArrayAdapterEstadales);
                builderEstadales.setView(listViewDialogEstadales);

                // Show Dialog
                filterDialogEstadales = builderEstadales.create();
                filterDialogEstadales.show();

                // Define on touch action.
                listViewDialogEstadales.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Bundle datos = new Bundle();
                        datos.putString("estado", listViewDialogEstadales.getItemAtPosition(position).toString());

                        Log.d(TAG, "Bundle Created with " + listViewDialogEstadales.getItemAtPosition(position).toString());

                        // Dismiss dialog
                        filterDialogEstadales.dismiss();

                        // Redirect View to next Fragment
                        Fragment fragment = new FragmentPuntuaciones();

                        fragment.setArguments(datos);

                        // Store fragment in back stack if main fragment
                        if( getArguments() == null){
                            getActivity().getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.content_frame, fragment)
                                    .addToBackStack(null)
                                    .commit();
                        }
                        else{
                            getActivity().getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.content_frame, fragment)
                                    .commit();
                        }

                    }
                });
                break;

            case R.id.reiniciar_puntuacion:

                // Show Confirmation Dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Confirmar");
                builder.setMessage("¿Está seguro de que desea reiniciar la puntuación de todos los usuarios?");

                builder.setPositiveButton("Reiniciar", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int which) {

                        final ProgressDialog dialog1 = ProgressDialog.show(getActivity(), "", "Actualizando Usuarios.", true);
                        // Call cloud function
                        final HashMap<String, Object> params = new HashMap<>();
                        ParseCloud.callFunctionInBackground("resetScores", params, new FunctionCallback<Map<String, Object>>() {
                            @Override
                            public void done(Map<String, Object> response, ParseException e) {
                                if (response != null && response.get("status").toString().equals("OK")) {
                                    dialog1.dismiss();
                                    Toast.makeText(getActivity(), "Usuarios actualizados correctamente. Los cambios se reflejarán en la próxima actualización", Toast.LENGTH_SHORT).show();

                                    // Publish Notification of Activity Created.
                                    ParseObject mensaje = new ParseObject("Mensaje");
                                    mensaje.put("texto",currentUser.getString("nombre")+" ha reiniciado las puntuaciones de Activismo en la tabla de puntuaciones");
                                    mensaje.put("autor",currentUser);
                                    mensaje.put("reportado", false);
                                    mensaje.saveEventually();
                                } else {
                                    dialog1.dismiss();
                                    if (e != null) {
                                        Log.d(TAG, "Error " + e.getCode() + ":  " + e.getMessage());
                                        Toast.makeText(getActivity(), ErrorCodeHelpers.resolveErrorCode(e.getCode()), Toast.LENGTH_LONG).show();
                                    }

                                    if (response != null) {
                                        Log.d(TAG, "Error: " + response.get("code").toString() + " " + response.get("message").toString());
                                        Toast.makeText(getActivity(), ErrorCodeHelpers.resolveErrorCode(Integer.valueOf(response.get("code").toString())), Toast.LENGTH_LONG).show();
                                    }

                                    if (e == null && response == null) {
                                        Log.d(TAG, "Error: unknown error");
                                        Toast.makeText(getActivity(), "Error, por favor intente de nuevo mas tarde.", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        });
                    }
                });

                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                        dialog.dismiss();
                    }
                });

                // After Dialog is Completely defined - Show Dialog.
                AlertDialog alert = builder.create();
                alert.show();
                break;
        }

        return true;
    }

}
