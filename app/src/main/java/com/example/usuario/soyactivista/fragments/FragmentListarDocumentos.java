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
 * A simple {@link Fragment} subclass.
 */
public class FragmentListarDocumentos extends Fragment {


    private String TAG = "FragDocumentos";

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String descargaTitle;
    private String subidaTitle;
    private ActivityPantallaMenu activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View v = inflater.inflate(R.layout.fragment_listar_documentos, container, false);

        viewPager = (ViewPager) v.findViewById(R.id.viewpager);

        // TODO: Optimize to not regenerate list on swiping.
        //viewPager.setOffscreenPageLimit(2);

        // Check if fragment was initialized with bundle. / State / Mun Level
        setTabTitles(getArguments());

        setupViewPager(viewPager);

        tabLayout = (TabLayout) v.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        return v;
    }

    // Checks for arguments initialized with the fragment and modify tab titles accordingly,
    private void setTabTitles(Bundle arguments) {


            descargaTitle = "Descargados";
            subidaTitle = "Subidos";


    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());

        // Initialize Tabs
        FragmentTabDescargados fragmentTabDescargados = new FragmentTabDescargados();
        FragmentTabSubidos fragmentTabSubidos = new FragmentTabSubidos() ;

        // Propagate Arguments
        if(getArguments() != null){
            Bundle datos = getArguments();
            Log.d(TAG, "Bundle Contains " + datos.getString("estado"));
            fragmentTabDescargados.setArguments(datos);
            fragmentTabSubidos.setArguments(datos);

        }

        adapter.addFragment(fragmentTabDescargados, descargaTitle);
        adapter.addFragment(fragmentTabSubidos , subidaTitle);

        viewPager.setAdapter(adapter);

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
