package com.example.notespro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    EditText emailedit,passwordedit;
    Button loginbutton;
    ProgressBar progressBar;
    TextView signuptextview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailedit=findViewById(R.id.emailedittext);
        passwordedit=findViewById(R.id.passwordedittext);

        loginbutton=findViewById(R.id.loginbutton);
        progressBar=findViewById(R.id.progressBarlogin);
        signuptextview=findViewById(R.id.signuptextview);

        loginbutton.setOnClickListener((v)-> loginUser());
        signuptextview.setOnClickListener((v)->startActivity(new Intent(LoginActivity.this,CreateAccountActivity.class)));
    }
    void loginUser()
    {
        String email=emailedit.getText().toString();
        String password=passwordedit.getText().toString();

        boolean isvalidated=validate(email,password);
        if(!isvalidated)
        {
            return;
        }
        loginAccountInFirebase(email,password);
    }
    void loginAccountInFirebase(String email,String password){
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        changenProgress(true);
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                changenProgress(false);
                if(task.isSuccessful())
                {
                    if(firebaseAuth.getCurrentUser().isEmailVerified())
                    {
                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    }
                    else{
                        Utility.showtoast(LoginActivity.this,"email not verified");
                    }
                }
                else{
                    Utility.showtoast(LoginActivity.this,task.getException().getLocalizedMessage());
                }
            }
        });

    }
    void changenProgress(boolean inprogess){
        if(inprogess)
        {
            progressBar.setVisibility(View.VISIBLE);
            loginbutton.setVisibility(View.GONE);
        }
        else{
            progressBar.setVisibility(View.GONE);
            loginbutton.setVisibility(View.VISIBLE);
        }
    }
    boolean validate(String email,String password)
    //validate data
    {
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            emailedit.setError("email is invalid");
            return false;
        }
        if(password.length()<6)
        {
            passwordedit.setError("password should be <=6 characters");
            return false;

        }
        Toast.makeText(this, "correct data", Toast.LENGTH_SHORT).show();
        return true;
    }
}
