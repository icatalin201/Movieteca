package app.mov.movieteca.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import app.mov.movieteca.R;
import app.mov.movieteca.fragments.CastFavorites;
import app.mov.movieteca.fragments.MovieFavorites;
import app.mov.movieteca.fragments.ShowsFavorites;

/**
 * Created by Catalin on 12/23/2017.
 */

public class FavFragmentPageAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public FavFragmentPageAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0) {
            fragment = new MovieFavorites();
        } else if (position == 1) {
            fragment = new ShowsFavorites();
        }
        else if (position == 2){
            fragment = new CastFavorites();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return mContext.getString(R.string.favorites_movies);
            case 1:
                return mContext.getString(R.string.favorites_shows);
            case 2:
                return mContext.getString(R.string.favorites_casts);
            default:
                return null;
        }
    }

}