package com.example.lab6_20190740_20195527.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.example.lab6_20190740_20195527.DatePickerFragment;
import com.example.lab6_20190740_20195527.R;
import com.example.lab6_20190740_20195527.TimePickerFragment;
import com.example.lab6_20190740_20195527.databinding.ActivityMainBinding;
import com.example.lab6_20190740_20195527.entities.Usuario;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    LocalDate fechaInicio;
    Boolean setearDateInicio = false;
    LocalDate fechaFin;
    LocalTime horaInicio;
    Boolean setearTimeInicio = false;
    LocalTime horaFin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = getSharedPreferences("MainPreference",MODE_PRIVATE);
        SharedPreferences.Editor editor =sharedPreferences.edit();
        String userStr = sharedPreferences.getString("usuario","");

        binding.fechaInicio.setOnClickListener(view -> {
            DatePickerFragment datePickerFragment = new DatePickerFragment();
            datePickerFragment.show(getSupportFragmentManager(), "datePicker");
            setearDateInicio = true;
        });
        binding.fechaFin.setOnClickListener(view -> {
            DatePickerFragment datePickerFragment = new DatePickerFragment();
            datePickerFragment.show(getSupportFragmentManager(), "datePicker");
        });
        binding.horaInicio.setOnClickListener(view -> {
            DialogFragment newFragment = new TimePickerFragment();
            newFragment.show(getSupportFragmentManager(), "timePicker");
            setearTimeInicio = true;
        });
        binding.horaFin.setOnClickListener(view -> {
            DialogFragment newFragment = new TimePickerFragment();
            newFragment.show(getSupportFragmentManager(), "timePicker");
        });

        if (userStr.equals("")){
            binding.nombreUser.setText("No encontrado");
        }else{
            Type userType = new TypeToken<Usuario>(){}.getType();
            Usuario usuarioLog =gson.fromJson(userStr, userType);
            binding.nombreUser.setText("Bienvenido: "+usuarioLog.getNombre());
            binding.cerrar.setOnClickListener(view -> {
                AuthUI.getInstance()
                        .signOut(MainActivity.this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("infoApp", "logout exitoso");
                            }
                        });
                editor.remove("usuario");
                editor.apply();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            });
        }
    }
    public void respuestaDateDialog(int year, int month, int day){
        Month monthSelect = Month.of(month);
        LocalDate fecha = LocalDate.of(year, monthSelect, day);
        if (setearDateInicio){
            fechaInicio = fecha;
            binding.fechaInicio.setText(day+"/"+month+"/"+year);
            setearDateInicio = false;
        }else{
            fechaFin = fecha;
            binding.fechaFin.setText(day+"/"+month+"/"+year);
        }
    }
    public void respuestaTimeDialog(int hour, int minute){
        Log.d("msg-test","hour selected: " + hour);
        Log.d("msg-test","minute selected: " + minute);
        LocalTime time = LocalTime.of(hour, minute);
        if (setearTimeInicio){
            horaInicio = time;
            binding.horaInicio.setText(hour+":"+minute);
            setearTimeInicio = false;
        }else{
            horaFin = time;
            binding.horaFin.setText(hour+":"+minute);
        }
    }
}