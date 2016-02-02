package com.example.usuario.soyactivista.fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by Luis Adrian on 19/01/2016.
 */
public class FragmentPuntuaciones extends Fragment {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String top5Title;
    private String rankingTitle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_puntuaciones, container, false);

        viewPager = (ViewPager) v.findViewById(R.id.viewpager);

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
            rankingTitle = "Ranking Municipal";
        }
        else{
            // Set titles to state
            top5Title = "Top 5 Estados";
            rankingTitle = "Ranking Estadal";
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());

        FragmentTabTop5 top5 = new FragmentTabTop5();
        top5.setArguments(getArguments());

        FragmentTabRanking ranking = new FragmentTabRanking();
        ranking.setArguments(getArguments());

        FragmentTabTopUsuarios topUsuarios = new FragmentTabTopUsuarios();
        topUsuarios.setArguments(getArguments());

        adapter.addFragment(top5 ,top5Title);
        adapter.addFragment(ranking , rankingTitle);
        adapter.addFragment(topUsuarios , "Top 20 Usuarios");
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
