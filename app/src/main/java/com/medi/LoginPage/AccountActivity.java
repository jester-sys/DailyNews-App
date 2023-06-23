package com.medi.LoginPage;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.medi.dailynews.BuildConfig;
import com.medi.dailynews.R;
import com.medi.dailynews.databinding.ActivityAccountBinding;


public class AccountActivity extends AppCompatActivity {

    ActivityAccountBinding binding;
    GoogleSignInClient gsc;
    GoogleSignInOptions gso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences preferences = getSharedPreferences("UserLogin",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        preferences.getString("UserEmail",binding.Email.toString());
        preferences.getString("UserName" , binding.Name.toString());
        String userEmail = preferences.getString("UserEmail","");
        String userName = preferences.getString("UserName", "");
        binding.Name.setText(userName);
        binding.Email.setText(userEmail);
        editor.apply();

        binding.ShareApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        "Hey check out my app at: https://play.google.com/store/apps/details?id=com.medi.dailynews" + BuildConfig.APPLICATION_ID);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });
        binding.feedback.setOnClickListener(v -> startActivity( new Intent(AccountActivity.this,FeedbackActivity.class)));
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        gsc = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            String name = account.getDisplayName();
            String email = account.getEmail();
            Uri img = account.getPhotoUrl();

            binding.Name.setText(name);
            binding.Email.setText(email);
            Glide
                    .with(this)
                    .load(img)
                    .centerCrop()
                    .into(binding.AccountImg);
        }

        binding.logout.setOnClickListener(v -> signOut());
    }

    private void signOut() {

        gsc.signOut().addOnCompleteListener(this, task -> {


        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage(" Are You Sure For Logout");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SharedPreferences preferences = getSharedPreferences("UserLogin",MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean("flag",false);
                        editor.apply();
                        finish();
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));

                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
        });
    }
}
