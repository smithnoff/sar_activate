package com.example.usuario.soyactivista.fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import logica.ActivityPantallaMenu;
import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by Luis Adrian on 19/01/2016.
 */
public class FragmentPuntuaciones extends Fragment {

    private String TAG = "FragPuntuaciones";

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String top5Title;
    private String rankingTitle;
    private ActivityPantallaMenu activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View v = inflater.inflate(R.layout.fragment_puntuaciones, container, false);

        Log.d(TAG, "Loading Fragment Puntuaciones");

        viewPager = (ViewPager) v.findViewById(R.id.viewpager);

        // TODO: Optimize to not regenerate list on swiping.
        viewPager.setOffscreenPageLimit(2);

        // Check if fragment was initialized with bundle. / State / Mun Level
        setTabTitles(getArguments());

        setupViewPager(viewPager);

        tabLayout = (TabLayout) v.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        return v;
    }

    // Checks for arguments initialized with the fragment and modify tab titles accordingly,
    private void setTabTitles(Bundle arguments) {
        if( arguments!= null){
            // Set titles to municipality
            top5Title = "Top 5 Municipios";
            rankingTitle = "Ranking Estadal";
        }
        else{
            // Set titles to state
            top5Title = "Top 5 Estado";
            rankingTitle = "Ranking Nacional";
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());

        // Initialize Tabs
        FragmentTabTop5 fragmentTabTop5 = new FragmentTabTop5();
        FragmentTabRanking fragmentTabRanking = new FragmentTabRanking();
        FragmentTabTopUsuarios fragmentTabTopUsuarios = new FragmentTabTopUsuarios();

        // Propagate Arguments
        if(getArguments() != null){
            Log.d(TAG,"Fragment has arguments");
            Bundle datos = getArguments();
            Log.d(TAG,"Bundle Contains "+datos.getString("estado")+" "+datos.getString("estadoId"));
            fragmentTabTop5.setArguments(datos);
            fragmentTabRanking.setArguments(datos);
            fragmentTabTopUsuarios.setArguments(datos);
        }
        else{
            Log.d(TAG,"Fragment does not have arguments");
        }

        adapter.addFragment(fragmentTabTop5, top5Title);
        adapter.addFragment(fragmentTabRanking , rankingTitle);
        adapter.addFragment(fragmentTabTopUsuarios , "Top 20 Usuarios");
        viewPager.setAdapter(adapter);

        Log.d(TAG, "Adapter has "+ adapter.getCount());
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


}
