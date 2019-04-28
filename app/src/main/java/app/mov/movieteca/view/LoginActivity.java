package app.mov.movieteca.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import app.mov.movieteca.R;
import app.mov.movieteca.view.viewmodel.UserViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.Unbinder;

public class LoginActivity extends AppCompatActivity {

    private UserViewModel userViewModel;
    private Unbinder unbinder;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        unbinder = ButterKnife.bind(this);
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        boolean isValid = userViewModel.hasValidSession();
        if (isValid) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        userViewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading) {
                progressBar.setVisibility(View.VISIBLE);
            } else {
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
        userViewModel.getToken().observe(this, tokenResponse -> {
            if (tokenResponse.isSuccess()) {
                if (tokenResponse.getExpiresAt() == null) {
                    userViewModel.validateToken(tokenResponse.getRequestToken());
                } else {
                    userViewModel.createSession(tokenResponse.getRequestToken());
                }
            }
        });
        userViewModel.getSession().observe(this, sessionResponse -> {
            if (sessionResponse.isSuccess()) {
                userViewModel.accessAccount(sessionResponse.getSessionId());
            }
        });
        userViewModel.getMessage().observe(this, message -> {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        });
        userViewModel.getAccount().observe(this, account -> {
            if (account.getId() != 0) {
                new Thread(() -> {
                    userViewModel.loadFavoriteMovies();
                    userViewModel.loadFavoriteShows();
                }).start();
            }
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @OnTextChanged(R.id.username)
    public void onUsernameChanged(CharSequence username) {
        userViewModel.onUsernameChanged(username);
    }

    @OnTextChanged(R.id.password)
    public void onPasswordChanged(CharSequence password) {
        userViewModel.onPasswordChanged(password);
    }

    @OnClick(R.id.sign_in)
    public void onSignIn() {
        userViewModel.generateToken();
    }

    @OnClick(R.id.sign_up)
    public void onSignUp() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://www.themoviedb.org/account/signup"));
        startActivity(intent);
    }

    @OnClick(R.id.guest)
    public void onGuest() {
        userViewModel.authenticateGuest();
    }
}
