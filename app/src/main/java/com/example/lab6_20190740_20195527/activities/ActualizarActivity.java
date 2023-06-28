package com.example.lab6_20190740_20195527.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.lab6_20190740_20195527.Configurations.Config;
import com.example.lab6_20190740_20195527.databinding.ActivityActualizarBinding;
import com.example.lab6_20190740_20195527.entities.Actividad;
import com.example.lab6_20190740_20195527.entities.Usuario;
import com.example.lab6_20190740_20195527.fragmentTimeDate.DateActualizarPickerFragment;
import com.example.lab6_20190740_20195527.fragmentTimeDate.TimeFinActualizarPickerFragment;
import com.example.lab6_20190740_20195527.fragmentTimeDate.TimeInicioActualizarPickerFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalTime;

public class ActualizarActivity extends AppCompatActivity {
    ActivityActualizarBinding binding;
    FirebaseDatabase firebaseDatabase;
    Config config = new Config();
    public LocalDate fecha;
    public LocalTime horaInicio;
    public LocalTime horaFin;
    FirebaseStorage storage = FirebaseStorage.getInstance();

    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
        binding.editTextHoraFin.setText(config.horaStrFormateada(horaFin));
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
        binding.editTextHoraInicio.setText(config.horaStrFormateada(horaInicio));
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
        binding.editTextFecha.setText(config.fechaStrFormateada(fecha));
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
        String activStr = sharedPreferences.getString("actividad","");
        String userStr = sharedPreferences.getString("usuario", "");
        Type activType = new TypeToken<Actividad>(){}.getType();
        Type userType = new TypeToken<Usuario>(){}.getType();
        Usuario usuarioLog = gson.fromJson(userStr, userType);
        Actividad actividad = gson.fromJson(activStr, activType);
        binding.editTextFecha.setText(actividad.getFecha());
        binding.editTextTitulo.setText(actividad.getTitulo());
        binding.editTextDescripcion.setText(actividad.getDescripcion());
        binding.editTextHoraFin.setText(actividad.getHoraFin());
        binding.editTextHoraInicio.setText(actividad.getHoraInicio());

        StorageReference imageRef = storage.getReference().child(usuarioLog.getGoogleKey()).child(actividad.getIdAct());
        Log.d("msg-imagen", imageRef.getPath());
        Glide.with(ActualizarActivity.this).load(imageRef).into(binding.subir);
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
        binding.subir.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 1);
        });
        binding.anadirActividad.setOnClickListener(view -> {
            String titulo = binding.editTextTitulo.getText().toString();
            String descripcion = binding.editTextDescripcion.getText().toString();
            String fechaStr = binding.editTextFecha.getText().toString();
            String timeInicioStr = binding.editTextHoraInicio.getText().toString();
            String timeFinStr = binding.editTextHoraFin.getText().toString();
            actividad.setDescripcion(descripcion);
            actividad.setTitulo(titulo);
            actividad.setHoraInicio(timeInicioStr);
            actividad.setHoraFin(timeFinStr);
            actividad.setFecha(fechaStr);
            Log.d("googlekey", usuarioLog.getGoogleKey());
            databaseReference.child(usuarioLog.getGoogleKey()).child(actividad.getIdAct()).setValue(actividad).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    editor.remove("actividad");
                    editor.apply();
                    Intent intent = new Intent(ActualizarActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            });

        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        binding.progreso.setText("");
        if (requestCode == 1 && resultCode == RESULT_OK && data !=null && data.getData()!=null){

            Gson gson = new Gson();
            SharedPreferences sharedPreferences = getSharedPreferences("MainPreference", MODE_PRIVATE);
            String userStr = sharedPreferences.getString("usuario", "");
            String activStr = sharedPreferences.getString("actividad","");
            Type activType = new TypeToken<Actividad>(){}.getType();
            Type userType = new TypeToken<Usuario>(){}.getType();
            Actividad actividad = gson.fromJson(activStr, activType);
            Usuario usuarioLog = gson.fromJson(userStr, userType);
            String uuid = usuarioLog.getGoogleKey();

            Uri uri = data.getData();
            String fileName = uri.getLastPathSegment();
            String[] strings = fileName.split("/");
            String fileNameFinal = actividad.getIdAct();
            StorageMetadata metadata = new StorageMetadata.Builder()
                    .setCacheControl("no-store")
                    .setCustomMetadata("nombre", fileNameFinal)
                    .build();
            try {
                InputStream inputStream = this.getContentResolver().openInputStream(uri);
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference imageRef = storage.getReference().child(uuid).child(fileNameFinal);
                Glide.with(ActualizarActivity.this).clear(binding.subir);
                imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                         imageRef.putStream(inputStream, metadata).addOnSuccessListener(taskSnapshot -> {
                            Log.d("msg-test", "archivo subido exitosamente");
                            binding.progreso.setText("100 %");
                            StorageReference imageRefNew = storage.getReference().child(uuid).child(fileNameFinal);
                            Glide.with(ActualizarActivity.this).load(imageRefNew).into(binding.subir);
                         }).addOnFailureListener(e -> {
                            Log.e("msg-test", "error");
                        });
                    }
                });

            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }

        }else{
            Toast.makeText(ActualizarActivity.this, "No se seleccionó un archivo", Toast.LENGTH_SHORT).show();
        }
    }

    public String obtenerExtensionArchivo(String nombreArchivo) {
        int indicePunto = nombreArchivo.lastIndexOf(".");
        if (indicePunto > 0 && indicePunto < nombreArchivo.length() - 1) {
            return nombreArchivo.substring(indicePunto + 1);
        } else {
            return "";
        }
    }
}