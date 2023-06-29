package com.example.lab6_20190740_20195527.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.lab6_20190740_20195527.Configurations.Config;
import com.example.lab6_20190740_20195527.R;
import com.example.lab6_20190740_20195527.fragmentTimeDate.DateCrearPickerFragment;
import com.example.lab6_20190740_20195527.fragmentTimeDate.TimeFinCrearPickerFragment;
import com.example.lab6_20190740_20195527.fragmentTimeDate.TimeInicioCrearPickerFragment;
import com.example.lab6_20190740_20195527.databinding.ActivityCrearBinding;
import com.example.lab6_20190740_20195527.entities.Actividad;
import com.example.lab6_20190740_20195527.entities.Usuario;
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

public class CrearActivity extends AppCompatActivity {
    ActivityCrearBinding binding;
    FirebaseDatabase firebaseDatabase;
    Config config = new Config();
    String activityName = "Actividad_"+config.generateID(7);

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference reference = storage.getReference();
    boolean imageUploaded = false;
    int colorDesact = Color.parseColor("#808080");
    ColorStateList colorStateList = ColorStateList.valueOf(colorDesact);
    int colorAct = Color.parseColor("#FF018786");
    ColorStateList colorActList = ColorStateList.valueOf(colorAct);
    public LocalDate fecha;
    public LocalTime horaInicio;
    public LocalTime horaFin;

    RequestOptions requestOptions = new RequestOptions()
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE);

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
        binding = ActivityCrearBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.anadirActividad.setEnabled(false);
        binding.anadirActividad.setBackgroundTintList(colorStateList);

        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();

        Gson gson = new Gson();
        SharedPreferences sharedPreferences = getSharedPreferences("MainPreference", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String userStr = sharedPreferences.getString("usuario", "");
        Type userType = new TypeToken<Usuario>(){}.getType();
        Usuario usuarioLog = gson.fromJson(userStr, userType);
        String uuid = usuarioLog.getGoogleKey();
        binding.editTextDescripcion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                validar(imageUploaded);
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });

        binding.editTextFecha.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                validar(imageUploaded);
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });

        binding.editTextHoraFin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                validar(imageUploaded);
                if (!binding.editTextHoraFin.getText().toString().equals("") && !binding.editTextHoraInicio.getText().toString().equals("")){
                    if (config.timeStrToLocalTime(binding.editTextHoraInicio.getText().toString()).isAfter(config.timeStrToLocalTime(binding.editTextHoraFin.getText().toString()))){
                        Toast.makeText(CrearActivity.this, "La hora de fin debe ser mayor a la de inicio", Toast.LENGTH_SHORT).show();
                        binding.editTextHoraFin.setText(binding.editTextHoraInicio.getText().toString());
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });
        binding.editTextHoraInicio.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                validar(imageUploaded);
                if (!binding.editTextHoraFin.getText().toString().equals("") && !binding.editTextHoraInicio.getText().toString().equals("")){
                    if (config.timeStrToLocalTime(binding.editTextHoraInicio.getText().toString()).isAfter(config.timeStrToLocalTime(binding.editTextHoraFin.getText().toString()))){
                        Toast.makeText(CrearActivity.this, "La hora de inicio debe ser menor a la de fin", Toast.LENGTH_SHORT).show();
                        binding.editTextHoraInicio.setText(binding.editTextHoraFin.getText().toString());
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });
        binding.editTextTitulo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                validar(imageUploaded);
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });

        binding.subir.setOnClickListener(view -> {
           Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
           startActivityForResult(intent, 1);
        });
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
            actividad.setIdAct(activityName);
            actividad.setHoraInicio(timeInicioStr);
            actividad.setHoraFin(timeFinStr);
            actividad.setFecha(fechaStr);
            Log.d("googlekey", usuarioLog.getGoogleKey());
            databaseReference.child(uuid).child(actividad.getIdAct()).setValue(actividad);
            Intent intent = new Intent(CrearActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        binding.progreso.setText("");
        if (requestCode == 1 && resultCode == RESULT_OK && data !=null && data.getData()!=null){
            binding.progreso.setText("Subiendo ...");
            Gson gson = new Gson();
            SharedPreferences sharedPreferences = getSharedPreferences("MainPreference", MODE_PRIVATE);
            String userStr = sharedPreferences.getString("usuario", "");
            Type userType = new TypeToken<Usuario>(){}.getType();
            Usuario usuarioLog = gson.fromJson(userStr, userType);
            String uuid = usuarioLog.getGoogleKey();

            Uri uri = data.getData();
            StorageMetadata metadata = new StorageMetadata.Builder()
                    .setCustomMetadata("nombre", activityName)
                    .build();
            try {
                InputStream inputStream = this.getContentResolver().openInputStream(uri);
                StorageReference imageRef = storage.getReference().child(uuid).child(activityName);
                imageRef.putStream(inputStream, metadata).addOnSuccessListener(taskSnapshot -> {
                    Log.d("msg-test", "archivo subido exitosamente");
                    binding.progreso.setText("100 %");
                    imageUploaded = true;
                    validar(true);
                    Glide.with(this).load(imageRef).apply(requestOptions).into(binding.subir);
                }).addOnFailureListener(e -> {
                    Log.e("msg-test", "error");
                });
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }

        }else{
            Toast.makeText(CrearActivity.this, "No se seleccionó un archivo", Toast.LENGTH_SHORT).show();
        }
    }
    public boolean validar(Boolean imageUploaded){
        if (!binding.editTextTitulo.getText().toString().equals("") &&
                !binding.editTextFecha.getText().toString().equals("") &&
                !binding.editTextHoraInicio.getText().toString().equals("") &&
                !binding.editTextHoraFin.getText().toString().equals("") &&
                !binding.editTextDescripcion.getText().toString().equals("") &&
                imageUploaded){
            binding.anadirActividad.setEnabled(true);
            binding.anadirActividad.setBackgroundTintList(colorActList);
            return true;
        }else{
            binding.anadirActividad.setEnabled(false);
            binding.anadirActividad.setBackgroundTintList(colorStateList);
            return false;
        }
    }
}