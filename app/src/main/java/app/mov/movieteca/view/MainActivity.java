package app.mov.movieteca.view;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import app.mov.movieteca.R;
import app.mov.movieteca.view.favorite.Favorites;
import app.mov.movieteca.view.home.Home;
import app.mov.movieteca.view.main.Main;
import app.mov.movieteca.view.profile.Profile;
import app.mov.movieteca.view.search.Search;

public class MainActivity extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener {

    private boolean onBackPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nagivation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        if ("android.intent.action.LAUNCH_FAVORITES".equals(getIntent().getAction())) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.container, new Favorites()).commit();
            bottomNavigationView.getMenu().getItem(2).setChecked(true);
            getIntent().setAction("");
        } else if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.container, new Home()).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item){
        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.home:
                fragment = new Home();
                break;
            case R.id.search:
                fragment = new Search();
                break;
            case R.id.main:
                fragment = new Main();
                break;
            case R.id.favorites:
                fragment = new Favorites();
                break;
            case R.id.profile:
                fragment = new Profile();
                break;
        }
        item.setChecked(true);
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
        }
        onBackPressed = false;
        return true;
    }

    @Override
    public void onBackPressed(){
        if (onBackPressed) {
            super.onBackPressed();
        }
        else {
            Toast.makeText(this, R.string.press_again, Toast.LENGTH_SHORT).show();
            onBackPressed = true;
        }
    }

}
