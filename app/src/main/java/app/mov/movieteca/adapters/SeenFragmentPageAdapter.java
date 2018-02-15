package app.mov.movieteca.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import app.mov.movieteca.R;
import app.mov.movieteca.fragments.SeenMoviesFragment;
import app.mov.movieteca.fragments.SeenShowsFragment;

/**
 * Created by Catalin on 12/24/2017.
 */

public class SeenFragmentPageAdapter extends FragmentPagerAdapter {

    private Context context;

    public SeenFragmentPageAdapter(Context context, FragmentManager fragmentManager){
        super(fragmentManager);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0){
            fragment = new SeenMoviesFragment();
        }
        else if (position == 1){
            fragment = new SeenShowsFragment();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.seen_movies);
            case 1:
                return context.getString(R.string.seen_shows);
            default:
                return null;
        }
    }
}
