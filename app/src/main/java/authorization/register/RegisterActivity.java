package authorization.register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.rolapppi.HomeActivity;
import com.example.rolapppi.R;

public class RegisterActivity extends AppCompatActivity implements Contract.RegisterView {


    private ProgressBar progressBar;

    private RegisterPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        presenter = new RegisterPresenter( this);
        progressBar = new ProgressBar(this);

        progressBar = (ProgressBar) findViewById(R.id.progressBarRegister);
        final EditText emailView = findViewById(R.id.inputEmail);
        final EditText passwordView = findViewById(R.id.inputPassword);
        final EditText confirmPasswordView = findViewById(R.id.inputConfirmPassword);
        Button registerButton = findViewById(R.id.btnRegister);
        registerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String email = emailView.getText().toString();
                String password = passwordView.getText().toString();
                String confirmPassword = confirmPasswordView.getText().toString();

                RegisterCredentials credentials = new RegisterCredentials(email, password, confirmPassword);
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