package com.example.lab6_20190740_20195527.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.example.lab6_20190740_20195527.Config;
import com.example.lab6_20190740_20195527.adapters.ListaActividadesAdapter;
import com.example.lab6_20190740_20195527.entities.Actividad;
import com.example.lab6_20190740_20195527.fragmentTimeDate.DateFiltroPickerFragment;
import com.example.lab6_20190740_20195527.fragmentTimeDate.TimePickerFragment;
import com.example.lab6_20190740_20195527.databinding.ActivityMainBinding;
import com.example.lab6_20190740_20195527.entities.Usuario;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    FirebaseDatabase firebaseDatabase;
    Config config = new Config();
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
        SharedPreferences sharedPreferences = getSharedPreferences("MainPreference", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String userStr = sharedPreferences.getString("usuario", "");
        Type userType = new TypeToken<Usuario>(){}.getType();
        Usuario usuarioLog = gson.fromJson(userStr, userType);


        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        databaseReference.child(usuarioLog.getGoogleKey()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Actividad> listaActividades = new ArrayList<>();
                for (DataSnapshot children : snapshot.getChildren()){
                    Actividad actividad = children.getValue(Actividad.class);
                    Log.d("msg-fecha", actividad.getDescripcion());
                    listaActividades.add(actividad);
                }
                ListaActividadesAdapter adapter = new ListaActividadesAdapter();
                adapter.setContext(MainActivity.this);
                adapter.setListaActividades(listaActividades);
                Log.d("msg-fecha", listaActividades.size()+"");
                binding.recyclerActivities.setAdapter(adapter);
                binding.recyclerActivities.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.fechaInicio.setOnClickListener(view -> {
            DateFiltroPickerFragment dateFiltroPickerFragment = new DateFiltroPickerFragment();
            dateFiltroPickerFragment.show(getSupportFragmentManager(), "datePicker");
            setearDateInicio = true;
        });
        binding.fechaFin.setOnClickListener(view -> {
            DateFiltroPickerFragment dateFiltroPickerFragment = new DateFiltroPickerFragment();
            dateFiltroPickerFragment.show(getSupportFragmentManager(), "datePicker");
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

        binding.floatingActionButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, CrearActivity.class);
            startActivity(intent);
        });

        if (userStr.equals("")){
            binding.nombreUser.setText("No encontrado");
        }else{
            binding.nombreUser.setText("Bienvenido: "+usuarioLog.getNombre());

            Log.d("google key", usuarioLog.getGoogleKey());

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
    public void respuestaDateDialog(int year,int month,int day){
        Log.d("msg-test","day selected: " + day);
        Log.d("msg-test","month selected: " + month);
        Log.d("msg-test","year selected: " + year);
        LocalDate fecha = LocalDate.of(year, month, day);
        if (setearDateInicio){
            fechaInicio = fecha;
            binding.fechaInicio.setText(config.fechaStrFormateada(fecha));
            setearDateInicio = false;
        }else{
            fechaFin = fecha;
            binding.fechaFin.setText(config.fechaStrFormateada(fecha));
        }
    }
    public void respuestaTimeDialog(int hour, int minute){
        Log.d("msg-test","hour selected: " + hour);
        Log.d("msg-test","minute selected: " + minute);
        LocalTime time = LocalTime.of(hour, minute);
        if (setearTimeInicio){
            horaInicio = time;
            binding.horaInicio.setText(config.horaStrFormateada(time));
            setearTimeInicio = false;
        }else{
            horaFin = time;
            binding.horaFin.setText(config.horaStrFormateada(time));
        }
    }
}