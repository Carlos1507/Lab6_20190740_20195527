package com.example.lab6_20190740_20195527.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.lab6_20190740_20195527.databinding.ActivityLoginBinding;
import com.example.lab6_20190740_20195527.entities.Usuario;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    GoogleApiClient googleApiClient;
    String TAG = "msg";
    private static final int RC_SIGN_INT = 9001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FirebaseAuth auth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, null)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        binding.loginGoogle.setOnClickListener(view -> {
            Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
            startActivityForResult(intent, RC_SIGN_INT);
            Log.d(TAG+"log", "LOGIN BOTÓN");
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_INT){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
            Log.d(TAG+"act", "ACTIVITY RESULT");
        }
    }

    private void handleSignInResult(GoogleSignInResult result){
        if (result.isSuccess()){
            Log.d(TAG+"hand", "HANDLE SIGN IN RESULT");
            GoogleSignInAccount account =result.getSignInAccount();
            Log.d("acct", "Usuario logueado con google");
            Usuario usuario = new Usuario();
            usuario.setNombre(account.getGivenName());
            usuario.setApellido(account.getFamilyName());
            usuario.setCorreo(account.getEmail());
            usuario.setGoogleKey(account.getId());
            Log.d("googlekey", usuario.getGoogleKey());
            Gson gson = new Gson();
            SharedPreferences sharedPreferences = getSharedPreferences("MainPreference",MODE_PRIVATE);
            SharedPreferences.Editor editor =sharedPreferences.edit();
            editor.putString("usuario", gson.toJson(usuario));
            editor.apply();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }else{
            Log.d(TAG+"not succ", "NOT SUCCESS HANDLE SIGN IN RESULT");
        }
    }


    public String getUUIDFromIdToken(String idToken) {
        try {
            DecodedJWT jwt = JWT.decode(idToken);
            String subject = jwt.getSubject();
            return subject;
        } catch (JWTDecodeException e) {
            e.printStackTrace();
            return null;
        }
    }

}