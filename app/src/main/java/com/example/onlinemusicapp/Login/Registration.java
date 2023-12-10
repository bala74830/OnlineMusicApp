package com.example.onlinemusicapp.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlinemusicapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Registration extends AppCompatActivity {

    FirebaseAuth mauth;
    EditText email,password;
    android.widget.Button register_btn;
    TextView login_text;
    ImageButton eyetoggle;
    boolean show =true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        getSupportActionBar().hide();

        mauth= FirebaseAuth.getInstance();
        email=findViewById(R.id.reg_mail);
        password=findViewById(R.id.reg_password);
        register_btn=findViewById(R.id.register_btn);
        login_text=findViewById(R.id.login_text);
        eyetoggle=findViewById(R.id.password_toggle2);

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Register();
            }
        });
        login_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Registration.this,LoginPage.class));
                finishAffinity();
            }
        });

        eyetoggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(show)
                {
                    show=false;
                    eyetoggle.setImageResource(R.drawable.ic_baseline_visibility_off_24);
                    password.setTransformationMethod(new PasswordTransformationMethod());
                }
                else
                {
                    show=true;
                    eyetoggle.setImageResource(R.drawable.ic_baseline_visibility_24);
                    password.setTransformationMethod(null);
                }
            }
        });

        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                {
                    email.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.edit_text_focus_bg));
                    password.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.edit_text_bg));
                }
            }
        });

        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                {
                    password.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.edit_text_focus_bg));
                    email.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.edit_text_bg));
                }
            }
        });

    }

    private void Register() {


        String user= email.getText().toString().trim();
        String pass= password.getText().toString().trim();
        if (user.isEmpty()){
            email.setError("Email can not be empty..");

        }if (pass.isEmpty()){
            password.setError("Password can not be empty..");
        }
        else{
            mauth.createUserWithEmailAndPassword(user,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(Registration.this, "User registered successfully!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Registration.this,LoginPage.class));
                        finishAffinity();
                    }
                    else
                    {
                        Toast.makeText(Registration.this, "Registration Failed!!"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }
}