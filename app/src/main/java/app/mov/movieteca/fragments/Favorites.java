package app.mov.movieteca.fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.mov.movieteca.R;
import app.mov.movieteca.adapters.FavFragmentPageAdapter;

/**
 * Created by Catalin on 12/23/2017.
 */

public class Favorites extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.favorite_fragment, container, false);
        ViewPager viewPager = (ViewPager)view.findViewById(R.id.viewpager);
        FragmentPagerAdapter adapter = new FavFragmentPageAdapter(getContext(), getChildFragmentManager());
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = (TabLayout)view.findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }

}
