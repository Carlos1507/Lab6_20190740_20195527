package com.example.lab6_20190740_20195527.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.example.lab6_20190740_20195527.R;
import com.example.lab6_20190740_20195527.databinding.ActivityActualizarBinding;
import com.example.lab6_20190740_20195527.entities.Actividad;
import com.example.lab6_20190740_20195527.entities.Usuario;
import com.example.lab6_20190740_20195527.fragmentTimeDate.DateActualizarPickerFragment;
import com.example.lab6_20190740_20195527.fragmentTimeDate.TimeFinActualizarPickerFragment;
import com.example.lab6_20190740_20195527.fragmentTimeDate.TimeInicioActualizarPickerFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalTime;

public class ActualizarActivity extends AppCompatActivity {
    ActivityActualizarBinding binding;

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
        binding = ActivityActualizarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = getSharedPreferences("MainPreference", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String activStr = sharedPreferences.getString("activEditar","");
        Type activType = new TypeToken<Actividad>(){}.getType();
        String userStr = sharedPreferences.getString("usuario", "");
        Type userType = new TypeToken<Usuario>(){}.getType();
        Usuario usuarioLog = gson.fromJson(userStr, userType);

        Actividad actividad = gson.fromJson(activStr, activType);
        binding.editTextFecha.setText(actividad.getFecha());
        binding.editTextTitulo.setText(actividad.getTitulo());
        binding.editTextDescripcion.setText(actividad.getDescripcion());
        binding.editTextHoraFin.setText(actividad.getHoraFin());
        binding.editTextHoraInicio.setText(actividad.getHoraInicio());

        binding.editTextFecha.setOnClickListener(view -> {
            DateActualizarPickerFragment dateActualizarPickerFragment = new DateActualizarPickerFragment();
            dateActualizarPickerFragment.show(getSupportFragmentManager(), "dateActualizarPicker");
        });
        binding.editTextHoraInicio.setOnClickListener(view ->{
            TimeInicioActualizarPickerFragment timeInicioActualizarPickerFragment = new TimeInicioActualizarPickerFragment();
            timeInicioActualizarPickerFragment.show(getSupportFragmentManager(), "timeInicioPicker");
        });
        binding.editTextHoraFin.setOnClickListener(view -> {
            TimeFinActualizarPickerFragment timeFinActualizarPickerFragment = new TimeFinActualizarPickerFragment();
            timeFinActualizarPickerFragment.show(getSupportFragmentManager(), "timeFinPicker");
        });

        binding.anadirActividad.setOnClickListener(view -> {
            String titulo = binding.editTextTitulo.getText().toString();
            String descripcion = binding.editTextDescripcion.getText().toString();
            String fechaStr = binding.editTextFecha.getText().toString();
            String timeInicioStr = binding.editTextHoraInicio.getText().toString();
            String timeFinStr = binding.editTextHoraFin.getText().toString();
            Actividad actividadUpdate = new Actividad();
            actividad.setDescripcion(descripcion);
            actividad.setTitulo(titulo);
            actividad.setIdAct(actividad.getIdAct());
            actividad.setHoraInicio(timeInicioStr);
            actividad.setHoraFin(timeFinStr);
            actividad.setFecha(fechaStr);
            Log.d("googlekey", usuarioLog.getGoogleKey());
            databaseReference.child(usuarioLog.getGoogleKey()).child(actividad.getIdAct()).setValue(actividad);
            Intent intent = new Intent(ActualizarActivity.this, MainActivity.class);
            startActivity(intent);
        });


    }
}