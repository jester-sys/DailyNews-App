package com.medi.LoginPage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.medi.dailynews.R;
import com.medi.dailynews.databinding.ActivitySignBinding;

public class SignActivity extends AppCompatActivity {
ActivitySignBinding binding;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        auth = FirebaseAuth.getInstance();

        binding.gotoLoginbtn.setOnClickListener(v1 -> startActivity( new Intent(SignActivity.this,LoginActivity.class)));
         binding.btnLogin.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if(!binding.Name.getText().toString().equals("") ) {

                     SharedPreferences preferences = getSharedPreferences("UserLogin", MODE_PRIVATE);
                     SharedPreferences.Editor editor = preferences.edit();
                     editor.putBoolean("flag", true);
                     editor.putString("UserEmail",binding.inputEmail.getText().toString());
                     editor.putString("UserName", binding.Name.getText().toString());
                     editor.apply();
                 }


                 String name = binding.Name.getText().toString().trim();
                 String user = binding.inputEmail.getText().toString().trim();
                 String  pass = binding.inputPassword.getText().toString().trim();

                 if(name.isEmpty()){
                     binding.Name.setError("Name cannot be empty");
                 }
                 if(user.isEmpty()){
                     binding.Name.setError("Email cannot be empty");
                 }
                 if(pass.isEmpty()){
                     binding.Name.setError("Password cannot be empty");
                 }
                 else{
                     auth.createUserWithEmailAndPassword(user,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                         @Override
                         public void onComplete(@NonNull Task<AuthResult> task) {
                             if(task.isSuccessful()) {
                                 Toast.makeText(SignActivity.this, "SignUp Successful", Toast.LENGTH_SHORT).show();
                                 startActivity( new Intent(SignActivity.this,LoginActivity.class));
                             }
                             else {
                                 Toast.makeText(SignActivity.this, "SignUp Failed", Toast.LENGTH_SHORT).show();
                             }
                         }
                     });
                 }
             }
         });

    }
}