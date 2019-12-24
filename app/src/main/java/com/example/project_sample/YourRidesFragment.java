package com.example.project_sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.project_sample.ui.main.SectionsPagerAdapter;

public class YourRidesFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // inflating the view
        View view = inflater.inflate(R.layout.yourrides_fragment, container, false);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getActivity(), getChildFragmentManager());
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);


        TabLayout tabs = (TabLayout) view.findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        //navigation from booking page(View_page)
        if (getArguments() != null && getArguments().containsKey("tab")) {
            if (getArguments().getString("tab").equals("ridespassenger")) {
                tabs.getTabAt(1).select(); //navigation to rides_driver_fragment
            }
        }
        return view;

    }
}