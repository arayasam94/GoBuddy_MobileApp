package com.example.project_sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class HomeFragment extends Fragment {
    Toolbar toolbar;
    Button search;
    Button post;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.home_fragment, container, false);
        toolbar = (Toolbar) v.findViewById(R.id.toolbarHome);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        search = (Button) v.findViewById(R.id.btnhomesrch);
        post = (Button) v.findViewById(R.id.btnhomePost);


        search.setOnClickListener(new View.OnClickListener() {
            //navigating to search page from home screens
            @Override
            public void onClick(View v) {

                Fragment searchFragment = new SearchFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragement_container, searchFragment);
                ((BottomNavigationView) getActivity().findViewById(R.id.bottom_nav_view)).setSelectedItemId(R.id.nav_search);//selecting search fragment on bottom navigation menu
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        //navigating to post page from home screens
        post.setOnClickListener(new View.OnClickListener() {
        //navigating to post page from home screens
            @Override
            public void onClick(View v) {

                Fragment searchFragment = new PostAdFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragement_container, searchFragment);
                ((BottomNavigationView) getActivity().findViewById(R.id.bottom_nav_view)).setSelectedItemId(R.id.nav_post_ad);//selecting post fragment on bottom navigation menu
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        return v;
    }

}
