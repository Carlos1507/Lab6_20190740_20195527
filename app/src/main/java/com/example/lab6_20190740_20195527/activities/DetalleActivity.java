package com.example.lab6_20190740_20195527.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.lab6_20190740_20195527.databinding.ActivityDetalleBinding;
import com.example.lab6_20190740_20195527.entities.Actividad;
import com.example.lab6_20190740_20195527.entities.Usuario;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;

public class DetalleActivity extends AppCompatActivity {
    ActivityDetalleBinding binding;
    RequestOptions requestOptions = new RequestOptions()
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetalleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Gson gson = new Gson();
        SharedPreferences sharedPreferences = getSharedPreferences("MainPreference", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String activStr = sharedPreferences.getString("actividad","");
        String userStr = sharedPreferences.getString("usuario", "");
        Type userType = new TypeToken<Usuario>(){}.getType();
        Usuario usuario = gson.fromJson(userStr, userType);
        editor.remove("actividad");
        editor.apply();
        Type activType = new TypeToken<Actividad>(){}.getType();
        Actividad actividad = gson.fromJson(activStr, activType);
        binding.descripcion.setText(actividad.getDescripcion());
        binding.fecha.setText(actividad.getFecha());
        binding.timeInicio.setText(actividad.getHoraInicio());
        binding.timeFin.setText(actividad.getHoraFin());
        binding.tituloDetalle.setText(actividad.getTitulo());
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference imageRef = storage.getReference().child(usuario.getGoogleKey()).child(actividad.getIdAct());
        Glide.with(this).load(imageRef).apply(requestOptions).into(binding.fotoActiv);
        binding.regresar.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
    }
}