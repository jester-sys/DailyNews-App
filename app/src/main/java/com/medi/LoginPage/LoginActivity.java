package com.medi.LoginPage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.medi.dailynews.MainActivity;
import com.medi.dailynews.R;
import com.medi.dailynews.databinding.ActivityLoginBinding;

import java.util.concurrent.Executor;
import java.util.regex.Pattern;


public class LoginActivity extends AppCompatActivity {

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    private FirebaseAuth auth;
    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        FirebaseApp.initializeApp(this);
        auth = FirebaseAuth.getInstance();


        binding.btnLogin.setOnClickListener(v -> {
            String email = binding.inputEmail.getText().toString();
            String pass = binding.inputPassword.getText().toString();


            if(!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                if(!pass.isEmpty()){
                    auth.signInWithEmailAndPassword(email,pass)
                            .addOnSuccessListener(authResult -> {
                                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                SharedPreferences preferences = getSharedPreferences("UserLogin",MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putBoolean("flag",true);
                                editor.apply();

                                    Intent NextScreen = new Intent(LoginActivity.this, MainActivity.class);
//                    NextScreen.putExtra("UserName",binding.UserName.getText().toString());
                                    startActivity(NextScreen);
                            }).addOnFailureListener(e -> Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show());
                }
                else{
                    binding.inputPassword.setError("Password cannot be empty");

                }
            } else if (email.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Email cannot be empty", Toast.LENGTH_SHORT).show();

            }else {
                binding.inputEmail.setError("Please enter valid email");
            }
        });
        binding.gotoRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,SignActivity.class));
            }
        });
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        gsc = GoogleSignIn.getClient(this, gso);

        binding.googleLogin.setOnClickListener(v -> sign());

        binding.ForgetPassword.setOnClickListener(v -> {
            AlertDialog.Builder  builder = new AlertDialog.Builder(LoginActivity.this);
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_forgot,null);
            EditText emailBox = dialogView.findViewById(R.id.emailBox);
            builder.setView(dialogView);
            AlertDialog dialog = builder.create();
            dialog.show();
            dialogView.findViewById(R.id.btnReset).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String userEmail = emailBox.getText().toString();
                    if(TextUtils.isEmpty(userEmail) && !Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){
                        Toast.makeText(LoginActivity.this, " Enter Your registered email id", Toast.LENGTH_SHORT).show();
                    }
                    auth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(LoginActivity.this, "Check your Email", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(LoginActivity.this, "Unable to send failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    dialogView.findViewById(R.id.btnCancel).setOnClickListener(v1 -> dialog.dismiss());
                    if(dialog.getWindow()!=null){
                        dialog.getWindow().setBackgroundDrawable( new ColorDrawable(0));
                    }


                }
            });
        });
    }
    private void sign() {
        SharedPreferences preferences = getSharedPreferences("UserLogin",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("flag",true);
        editor.apply();
        Intent intent = gsc.getSignInIntent();
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    homeActivity();
                }
            } catch (ApiException e) {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void homeActivity() {
        finish();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}
