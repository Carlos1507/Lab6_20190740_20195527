package com.example.lab6_20190740_20195527.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.lab6_20190740_20195527.R;
import com.example.lab6_20190740_20195527.databinding.ActivityDetalleBinding;
import com.example.lab6_20190740_20195527.entities.Actividad;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class DetalleActivity extends AppCompatActivity {
    ActivityDetalleBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetalleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = getSharedPreferences("MainPreference", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String activStr = sharedPreferences.getString("actividad","");
        editor.remove("actividad");
        editor.apply();
        Type activType = new TypeToken<Actividad>(){}.getType();
        Actividad actividad = gson.fromJson(activStr, activType);
        binding.descripcion.setText(actividad.getDescripcion());
        binding.fecha.setText(actividad.getFecha());
        binding.timeInicio.setText(actividad.getHoraInicio());
        binding.timeFin.setText(actividad.getHoraFin());
        binding.tituloDetalle.setText(actividad.getTitulo());
        binding.regresar.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
    }
}