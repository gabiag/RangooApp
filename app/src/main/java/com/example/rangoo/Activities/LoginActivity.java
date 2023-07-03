package com.example.rangoo.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rangoo.Interfaces.AuthCallback;
import com.example.rangoo.Model.LoginData;
import com.example.rangoo.Network.FirebaseNetwork;
import com.example.rangoo.Network.GoogleNetwork;
import com.example.rangoo.R;
import com.example.rangoo.Utils.GoTo;
import com.example.rangoo.databinding.ActivityLoginBinding;
import com.google.android.material.snackbar.Snackbar;

public class LoginActivity extends AppCompatActivity {

    private  GoogleNetwork google;
    private FirebaseNetwork firebase;

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        google = new GoogleNetwork();
        firebase = new FirebaseNetwork();

        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        binding.resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(findViewById(android.R.id.content), R.string.solicitacao_redefinicao_enviada, Snackbar.LENGTH_SHORT)
                        .setBackgroundTint(getColor(R.color.pumpkin)).show();
            }
        });

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithEmailPass(binding.signinLogin.getText().toString(), binding.signinPassword.getText().toString());
            }
        });

        binding.btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInWithGoogle();
            }
        });
    }


    protected void signInWithGoogle(){
        google.signIn(this, startActivity,  new AuthCallback() {
            @Override
            public void onSuccess(String UID) {
                goToHomeView(UID);
            }

            @Override
            public void onError(String error) {
                Log.d("FALHA LOGIN: ", error);
                errorMessage(getString(R.string.user_google_error));
            }
        });
    }

    protected void signInWithEmailPass(String email, String senha){
        firebase.signIn(new LoginData(email, senha), new AuthCallback() {
            @Override
            public void onSuccess(String UID) {
                goToHomeView(UID);
            }

            @Override
            public void onError(String error) {
                Log.d("FALHA LOGIN: ", error);
                errorMessage(getString(R.string.user_login_error));
            }
        });
    }

    protected void goToHomeView(String UID){
        Snackbar.make(findViewById(android.R.id.content), R.string.user_login_success, Snackbar.LENGTH_SHORT)
                .setBackgroundTint(getColor(R.color.vegan)).show();
        saveUID(UID);
        GoTo.homeView(LoginActivity.this);
    }

    protected void errorMessage(String msg){
        Snackbar.make(findViewById(android.R.id.content), msg, Snackbar.LENGTH_SHORT)
                .setBackgroundTint(getColor(R.color.baron)).show();
    }

    protected void saveUID(String UID){
        SharedPreferences preferences = getSharedPreferences(getString(R.string._COM_RANGO_PREFERENCES), MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("UID", UID);
        editor.putBoolean("isLoggedIn", true);
        editor.apply();
    }

    //----------------------- Activity Result
    ActivityResultLauncher<Intent> startActivity = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode() == Activity.RESULT_OK){
                    Intent intent = result.getData();
                    google.handleResultSignIn(intent);
                }else{
                    Log.d("ERRO: ", "" + result.getResultCode());
                    Toast.makeText(getApplicationContext(), R.string.error_login_google, Toast.LENGTH_SHORT).show();
                }
            }
    );
}