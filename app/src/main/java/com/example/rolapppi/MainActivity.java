package com.example.rolapppi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import authorization.login.LoginActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


//    TextView createnewAccount,forgotPassword;
//
//    EditText inputEmail,inputPassword;
//    Button btnLogin;
//    String emailPattern = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";
//    ProgressDialog progressDialog;
//
//    FirebaseAuth mAuth;
//    FirebaseUser mUser;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
//
//        inputEmail=findViewById(R.id.inputEmail);
//        inputPassword=findViewById(R.id.inputPassword);
//        btnLogin=findViewById(R.id.btnLogin);
//        progressDialog=new ProgressDialog(this);
//        mAuth=FirebaseAuth.getInstance();
//        mUser=mAuth.getCurrentUser();
//
//
//        createnewAccount=findViewById(R.id.createNewAccount);
//        createnewAccount.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//               // startActivity(new Intent(MainActivity.this, RegisterActivity.class));
//                startActivity(new Intent(MainActivity.this, LoginActivity.class));
//            }
//        });
//
//
//        btnLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                performLogin();
//            }
//
//            private void performLogin() {
//                String email=inputEmail.getText().toString();
//                String password=inputPassword.getText().toString();
//
//
//                if(!email.matches(emailPattern)) {
//                    inputEmail.setError("Wpisz poprawny Email");
//                }else if(password.isEmpty() || password.length()<6) {
//                    inputPassword.setError("Wpisz poprawne hasło");
//                }else {
//                    progressDialog.setMessage("Logowanie...");
//                    progressDialog.setTitle("Logowanie");
//                    progressDialog.show();
//
//                    mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//                            if(task.isSuccessful()) {
//                                progressDialog.dismiss();
//                                sendUserToNextActivity();
//                                Toast.makeText(MainActivity.this, "Logowanie udane", Toast.LENGTH_SHORT).show();
//                            }else {
//                                progressDialog.dismiss();
//                                Toast.makeText(MainActivity.this, "Niepoprawne dane", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//
//                        private void sendUserToNextActivity() {
//                            Intent intent=new Intent(MainActivity.this,HomeActivity.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(intent);
//                        }
//                    });
//                }
//            }
//        });
//    }
}