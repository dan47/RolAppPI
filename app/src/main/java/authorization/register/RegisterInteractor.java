package authorization.register;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Patterns;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterInteractor {

    private Contract.RegisterListener registerListener;

    public RegisterInteractor(Contract.RegisterListener registerListener) {
        this.registerListener = registerListener;
    }

    public void register(final RegisterCredentials registerCredentials) {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(hasError(registerCredentials))
                {
                    return;
                }
                FirebaseAuth.getInstance()
                        .createUserWithEmailAndPassword(registerCredentials.getUsername(), registerCredentials.getPassword())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    registerListener.onSuccess();

                                } else {
                                    registerListener.onFailed("Niepoprawne dane");
                                }
                            }
                        });
            }
        }, 3000);

    }

    private boolean hasError(RegisterCredentials registerCredentials) {
        String username = registerCredentials.getUsername();
        String password = registerCredentials.getPassword();
        String confirmPassword = registerCredentials.getConfirmPassword();

        if (TextUtils.isEmpty(username)) {
            registerListener.onFailed("Mail jest pusty");
            return true;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(username).matches()) {
            registerListener.onFailed("Mail jest nieprawidłowy");
            return true;
        }

        if (TextUtils.isEmpty(password)) {
            registerListener.onFailed("Hasło jest puste");
            return true;
        }

        if (!TextUtils.equals(password,confirmPassword)){
            registerListener.onFailed("Hasła nie są takie same");
            return true;
        }

        if (password.length() < 5) {
            registerListener.onFailed("Hasło jest za krótkie (min.6)");
            return true;
        }

        return false;
    }

}
