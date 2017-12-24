package app.mov.movieteca.activities;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import app.mov.movieteca.R;
import app.mov.movieteca.fragments.About;
import app.mov.movieteca.fragments.Favorites;
import app.mov.movieteca.fragments.Home;
import app.mov.movieteca.fragments.TVShows;
import app.mov.movieteca.utils.Helper;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private ActionBarDrawerToggle actionbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawer = (DrawerLayout)findViewById(R.id.drawer);
        actionbar = new ActionBarDrawerToggle(this, drawer, R.string.open, R.string.close);
        drawer.addDrawerListener(actionbar);
        actionbar.syncState();

        // first launch with home fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.FragmentContainer, new Home()).commit();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        if (mNavigationView != null) {
            mNavigationView.setNavigationItemSelectedListener(this);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(actionbar.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item){
        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.nav1:
                fragment = new Home();
                break;
            case R.id.nav2:
                fragment = new TVShows();
                break;
            case R.id.nav3:
                fragment = new Favorites();
                break;
            case R.id.nav4:
                //fragment = new Seen();
                break;
            case R.id.nav5:
                fragment = new About();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        if (fragment != null) {
            Helper.changeFragment(this, fragment);
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            Log.i("MainActivity", "popping backstack");
            fragmentManager.popBackStack();
        } else {
            Log.i("MainActivity", "nothing on backstack, calling super");
            super.onBackPressed();
        }
    }
}
