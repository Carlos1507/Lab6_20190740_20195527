package com.example.lab6_20190740_20195527.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;


import com.example.lab6_20190740_20195527.fragmentTimeDate.DateCrearPickerFragment;
import com.example.lab6_20190740_20195527.fragmentTimeDate.TimeFinCrearPickerFragment;
import com.example.lab6_20190740_20195527.fragmentTimeDate.TimeInicioCrearPickerFragment;
import com.example.lab6_20190740_20195527.databinding.ActivityCrearBinding;
import com.example.lab6_20190740_20195527.entities.Actividad;
import com.example.lab6_20190740_20195527.entities.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Random;

public class CrearActivity extends AppCompatActivity {
    ActivityCrearBinding binding;
    FirebaseDatabase firebaseDatabase;

    public LocalDate fecha;
    public LocalTime horaInicio;
    public LocalTime horaFin;

    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
        binding.editTextHoraFin.setText(horaFin.getHour()+":"+horaFin.getMinute());
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
        binding.editTextHoraInicio.setText(horaInicio.getHour()+":"+horaInicio.getMinute());
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
        binding.editTextFecha.setText(fecha.getDayOfMonth()+"/"+(fecha.getMonthValue()+1)+"/"+fecha.getYear());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCrearBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();

        Gson gson = new Gson();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        SharedPreferences sharedPreferences = getSharedPreferences("MainPreference", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String userStr = sharedPreferences.getString("usuario", "");
        Type userType = new TypeToken<Usuario>(){}.getType();
        Usuario usuarioLog = gson.fromJson(userStr, userType);
        String uuid = usuarioLog.getGoogleKey();
        binding.editTextHoraInicio.setOnClickListener(view -> {
            TimeInicioCrearPickerFragment timeInicioCrearPickerFragment = new TimeInicioCrearPickerFragment();
            timeInicioCrearPickerFragment.show(getSupportFragmentManager(), "timeInicioCrearPicker");
        });
        binding.editTextFecha.setOnClickListener(view -> {
            DateCrearPickerFragment dateCrearPickerFragment = new DateCrearPickerFragment();
            dateCrearPickerFragment.show(getSupportFragmentManager(), "dateCrearPicker");
        });
        binding.editTextHoraFin.setOnClickListener(view -> {
            TimeFinCrearPickerFragment timeFinCrearPickerFragment = new TimeFinCrearPickerFragment();
            timeFinCrearPickerFragment.show(getSupportFragmentManager(), "timeFinCrearPicker");
        });
        binding.anadirActividad.setOnClickListener(view -> {
            String titulo = binding.editTextTitulo.getText().toString();
            String descripcion = binding.editTextDescripcion.getText().toString();
            String fechaStr = binding.editTextFecha.getText().toString();
            String timeInicioStr = binding.editTextHoraInicio.getText().toString();
            String timeFinStr = binding.editTextHoraFin.getText().toString();
            Actividad actividad = new Actividad();
            actividad.setDescripcion(descripcion);
            actividad.setTitulo(titulo);
            actividad.setIdAct("Actividad_"+generateID(7));
            actividad.setHoraInicio(timeInicioStr);
            actividad.setHoraFin(timeFinStr);
            actividad.setFecha(fechaStr);
            Log.d("googlekey", usuarioLog.getGoogleKey());
            databaseReference.child(uuid).child(actividad.getIdAct()).setValue(actividad);
            Intent intent = new Intent(CrearActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }

    private String generateID(int tamano){
        String letras = "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        Random r = new Random();
        String id = "";
        for (int i = 0; i<tamano; i++){
            id+= letras.charAt(r.nextInt(letras.length()));
        }
        return id;
    }
}