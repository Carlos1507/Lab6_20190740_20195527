package com.example.lab6_20190740_20195527.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;


import com.example.lab6_20190740_20195527.DateCrearPickerFragment;
import com.example.lab6_20190740_20195527.TimeFinCrearPickerFragment;
import com.example.lab6_20190740_20195527.TimeInicioCrearPickerFragment;
import com.example.lab6_20190740_20195527.databinding.ActivityCrearBinding;
import com.example.lab6_20190740_20195527.entities.Actividad;
import com.example.lab6_20190740_20195527.entities.Usuario;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalTime;

public class CrearActivity extends AppCompatActivity {
    ActivityCrearBinding binding;
    FirebaseDatabase firebaseDatabase;

    public LocalDate fecha;
    public LocalTime horaInicio;
    public LocalTime horaFin;

    public LocalDate getFecha() {
        return fecha;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

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
        binding.editTextFecha.setText(fecha.getDayOfMonth()+"/"+fecha.getMonthValue()+"/"+fecha.getYear());
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
            Log.d("msg-act", ""+getFecha().getYear());
            databaseReference.child(uuid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    long count = snapshot.getChildrenCount();
                    Actividad actividad = new Actividad();
                    actividad.setDescripcion(descripcion);
                    actividad.setTitulo(titulo);
                    actividad.setIdAct("Actividad_"+(count+1));
                    actividad.setHoraInicio(timeInicioStr);
                    actividad.setHoraFin(timeFinStr);
                    actividad.setFecha(fechaStr);
                    Log.d("googlekey", usuarioLog.getGoogleKey());
                    databaseReference.child(uuid).child(actividad.getIdAct()).setValue(actividad);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        });
    }
}