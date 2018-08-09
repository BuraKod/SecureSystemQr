package com.burakod.securesystemqr;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnLogin ;
    EditText email,password;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogin = findViewById(R.id.btn_login);
        email = findViewById(R.id.et_email);
        password = findViewById(R.id.et_password);
        auth = FirebaseAuth.getInstance();
        btnLogin.setOnClickListener(this) ;

    }

    @Override
    public void onClick(View view) {
        if (view.getId()== R.id.btn_login){
            UserLogin();
        }
    }

    private void UserLogin() {
        String sEmail = email.getText().toString().trim();
        String sPassword = password.getText().toString().trim();

        if (sEmail.isEmpty()){
            email.setError("Email is required");
            email.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(sEmail).matches()){
            email.setError("Please enter a valid email");
            email.requestFocus();
            return;
        }
        if (sPassword.isEmpty()){
            password.setError("Email is required");
            password.requestFocus();
            return;
        }
        if (sPassword.length()<6){
            password.setError("Minumum lenght of password should be 6");
            password.requestFocus();
            return;
        }
        auth.signInWithEmailAndPassword(sEmail,sPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Intent intent = new Intent(MainActivity.this,EventsActivity.class);

                    startActivity(intent);
                }else {
                    Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
