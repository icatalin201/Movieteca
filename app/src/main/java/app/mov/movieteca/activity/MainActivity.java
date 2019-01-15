package app.mov.movieteca.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import app.mov.movieteca.R;
import app.mov.movieteca.fragment.Favorites;
import app.mov.movieteca.fragment.Home;
import app.mov.movieteca.fragment.More;
import app.mov.movieteca.fragment.Search;
import app.mov.movieteca.utils.Util;

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
            Util.changeFragment(this, new Favorites());
            bottomNavigationView.getMenu().getItem(2).setChecked(true);
            getIntent().setAction("");
        } else if (savedInstanceState == null) {
            Util.changeFragment(this, new Home());
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
                item.setChecked(true);
                break;
            case R.id.search:
                fragment = new Search();
                item.setChecked(true);
                break;
            case R.id.favorites:
                fragment = new Favorites();
                item.setChecked(true);
                break;
//            case R.id.more:
//                fragment = new More();
//                item.setChecked(true);
//                break;
        }
        if (fragment != null) {
            Util.changeFragment(this, fragment);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        recreate();
        super.onActivityResult(requestCode, resultCode, data);
    }
}
