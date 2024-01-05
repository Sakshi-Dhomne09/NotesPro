package com.example.notespro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import java.util.regex.Pattern;

public class CreateAccountActivity extends AppCompatActivity {
    EditText emailedit,passwordedit,confirmpassedit;
    Button createAccountBtn;
    ProgressBar progressBar;
    TextView loginBtnTextview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        emailedit=findViewById(R.id.emailedittext);
        passwordedit=findViewById(R.id.passwordedittext);
        confirmpassedit=findViewById(R.id.confirmpasswordedit);
        createAccountBtn=findViewById(R.id.createaccountbutton);
        progressBar=findViewById(R.id.progressBar2);
        loginBtnTextview=findViewById(R.id.textView5);

        createAccountBtn.setOnClickListener(v->createaccount());
        loginBtnTextview.setOnClickListener(v->finish());

    }
    void createaccount()
    {
        String email=emailedit.getText().toString();
        String password=passwordedit.getText().toString();
        String confirmpassword=confirmpassedit.getText().toString();
        boolean isvalidated=validate(email,password,confirmpassword);
        if(!isvalidated)
        {
            return;
        }
        createAccountinFirebase(email,password);
    }
    void createAccountinFirebase(String email,String password)
    {
            changenProgress(true);
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(CreateAccountActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                changenProgress(false);
                if(task.isSuccessful())
                {
                    Toast.makeText(CreateAccountActivity.this,"Sucessfully created account,check email to verify",Toast.LENGTH_SHORT).show();
                    firebaseAuth.getCurrentUser().sendEmailVerification();
                    firebaseAuth.signOut();
                    finish();
                }else{
                    //failure
                    Toast.makeText(CreateAccountActivity.this,task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    void changenProgress(boolean inprogess){
        if(inprogess)
        {
            progressBar.setVisibility(View.VISIBLE);
            createAccountBtn.setVisibility(View.GONE);
        }
        else{
            progressBar.setVisibility(View.GONE);
            createAccountBtn.setVisibility(View.VISIBLE);
        }
    }
    boolean validate(String email,String password,String confirmpassword)
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
        if(!password.equals(confirmpassword))
        {
            confirmpassedit.setError("password and confirm password should match");
            return false;
        }
        Toast.makeText(this, "correct data", Toast.LENGTH_SHORT).show();
        return true;
    }
}