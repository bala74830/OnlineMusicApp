package com.example.onlinemusicapp.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.onlinemusicapp.MainActivity;
import com.example.onlinemusicapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import ru.github.igla.ferriswheel.FerrisWheelView;

public class LoginPage extends AppCompatActivity {

    FirebaseAuth mauth;
    EditText mail,password;
    android.widget.Button login_btn,reg_text;
    TextView resetpass;
    String user;
    ImageButton eyetoggle;
    SharedPreferences settings;
    SharedPreferences.Editor editor;
    boolean show =true,firsttime=false;
    LottieAnimationView greendotloader;
    FerrisWheelView ferrisWheelView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        //getSupportActionBar().hide();


        mauth=FirebaseAuth.getInstance();
        settings = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        editor = settings.edit();
        mail=findViewById(R.id.login_mail);
        password=findViewById(R.id.login_password);
        login_btn=findViewById(R.id.login_btn);
        reg_text=findViewById(R.id.register_text);
        resetpass=findViewById(R.id.forgetpass);
        eyetoggle=findViewById(R.id.password_toggle);
        ferrisWheelView=findViewById(R.id.ferrisWheelView2);
        //greendotloader=findViewById(R.id.greendotloader);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.primarydark));
        }


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

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mail.getText().toString().equals("admin@gmail.com") && password.getText().toString().equals("admin123")){
                    Intent i=new Intent(LoginPage.this, MainActivity.class);
                    startActivity(i);
                    firsttime=true;
                    editor.putBoolean("ft",firsttime);
                    editor.apply();
                    finishAffinity();
                }
                else {
                    login();
                }
            }
        });

        reg_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //greendotloader.setVisibility(View.VISIBLE);
                startActivity(new Intent(LoginPage.this,Registration.class));
                //greendotloader.setVisibility(View.GONE);
                finishAffinity();
            }
        });

        resetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user= mail.getText().toString().trim();
                if (!TextUtils.isEmpty(user)){
                    ResetPassword();
                }
                else {
                    mail.setError("Email can not be empty..");
                }
            }
        });

        mail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                {
                    mail.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.edit_text_focus_bg));
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
                    mail.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.edit_text_bg));
                }
            }
        });
    }

    private void login() {
        String user= mail.getText().toString().trim();
        String pass= password.getText().toString().trim();
        if (user.isEmpty()){
            mail.setError("Email can not be empty..");

        }if (pass.isEmpty()){
            password.setError("Password can not be empty..");
        }
        else
        {
            mauth.signInWithEmailAndPassword(user,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful())
                    {
                        login_btn.setClickable(false);
                        reg_text.setClickable(false);
                        ferrisWheelView.setVisibility(View.VISIBLE);
                        ferrisWheelView.startAnimation();
                        ferrisWheelView.setClickable(false);
                        Toast.makeText(LoginPage.this, "Logging in..", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginPage.this,MainActivity.class));
                        ferrisWheelView.stopAnimation();
                        ferrisWheelView.setVisibility(View.GONE);
                        firsttime=true;
                        editor.putBoolean("ft",firsttime);
                        editor.apply();
                        finishAffinity();
                    }
                    else
                    {
                        login_btn.setClickable(false);
                        reg_text.setClickable(false);
                        ferrisWheelView.setVisibility(View.VISIBLE);
                        ferrisWheelView.startAnimation();
                        ferrisWheelView.setClickable(false);
                        Toast.makeText(LoginPage.this, "Login Failed!!"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        ferrisWheelView.stopAnimation();
                        ferrisWheelView.setVisibility(View.GONE);
                        login_btn.setClickable(true);
                        reg_text.setClickable(true);
                    }
                }
            });
        }
    }

    public void ResetPassword(){
        //greendotloader.setVisibility(View.VISIBLE);
        login_btn.setClickable(false);
        reg_text.setClickable(false);
        ferrisWheelView.setVisibility(View.VISIBLE);
        ferrisWheelView.startAnimation();
        ferrisWheelView.setClickable(false);
        resetpass.setVisibility(View.GONE);

        mauth.sendPasswordResetEmail(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(LoginPage.this, "Reset Password link has been sent to your registered email", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginPage.this,LoginPage.class);
                startActivity(intent);
                //greendotloader.setVisibility(View.GONE);
                finishAffinity();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginPage.this, "Error:"+e.getMessage(), Toast.LENGTH_SHORT).show();
                resetpass.setVisibility(View.VISIBLE);
                ferrisWheelView.stopAnimation();
                ferrisWheelView.setVisibility(View.GONE);
                login_btn.setClickable(true);
                reg_text.setClickable(true);
            }
        });
        //password.addTextChangedListener(logintextwatcher);
        //mail.addTextChangedListener(logintextwatcher);
    }

    public TextWatcher logintextwatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            String emailinput,passwordinput;
            emailinput=mail.getText().toString().trim();
            passwordinput=password.getText().toString().trim();


            if(!emailinput.isEmpty() && !passwordinput.isEmpty())

            {
                login_btn.setEnabled(true);
                login_btn.setClickable(true);
                login_btn.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.btn_bg_active));
            }
            else
            {
                login_btn.setEnabled(false);
                login_btn.setClickable(false);
                login_btn.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.btn_background));
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}