package authorization.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rolapppi.HomeActivity;
import com.example.rolapppi.R;

import authorization.register.RegisterActivity;

public class LoginActivity extends AppCompatActivity implements Contract.LoginView {


    private ProgressBar progressBar;

    private LoginPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        presenter = new LoginPresenter( this);
        progressBar = new ProgressBar(this);

        progressBar = (ProgressBar) findViewById(R.id.progressBarLogin);
        final EditText emailView = findViewById(R.id.inputEmail);
        final EditText passwordView = findViewById(R.id.inputPassword);

        TextView createAccuont = findViewById(R.id.createNewAccount);
        createAccuont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        Button loginButton = findViewById(R.id.btnLogin);
        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String email = emailView.getText().toString();
                String password = passwordView.getText().toString();

                LoginCredentials credentials = new LoginCredentials(email, password);
                presenter.start(credentials);
            }

        });
    }

    @Override
    public void showProgressbar() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressbar() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onSuccess() {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    @Override
    public void onFailed(String messsage) {
        Toast.makeText(this, messsage, Toast.LENGTH_SHORT).show();
    }
}