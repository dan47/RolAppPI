package authorization.login;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Patterns;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.logging.LogRecord;

public class LoginInteractor {

    private Contract.LoginListener loginListener;

    public LoginInteractor(Contract.LoginListener loginListener) {
        this.loginListener = loginListener;
    }

    public void login(final LoginCredentials loginCredentials) {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(hasError(loginCredentials))
                {
                    return;
                }
                FirebaseAuth.getInstance()
                        .signInWithEmailAndPassword(loginCredentials.getUsername(),loginCredentials.getPassword())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    loginListener.onSuccess();

                                } else {
                                    loginListener.onFailed("Niepoprawne dane");
                                }
                            }
                        });
            }
        }, 3000);

    }

    private boolean hasError(LoginCredentials loginCredentials) {
        String username = loginCredentials.getUsername();
        String password = loginCredentials.getPassword();

        if (TextUtils.isEmpty(username)) {
            loginListener.onFailed("Mail jest pusty");
            return true;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(username).matches()) {
            loginListener.onFailed("Mail jest nieprawidłowy");
            return true;
        }

        if (TextUtils.isEmpty(password)) {
            loginListener.onFailed("Hasło jest puste");
            return true;
        }

        if (password.length() < 5) {
            loginListener.onFailed("Hasło jest za krótkie (min.6)");
            return true;
        }

        return false;
    }

}
