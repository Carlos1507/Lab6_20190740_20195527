package com.example.lab6_20190740_20195527.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.example.lab6_20190740_20195527.R;
import com.example.lab6_20190740_20195527.databinding.ActivityCrearBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class CrearActivity extends AppCompatActivity {
    ActivityCrearBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCrearBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        FirebaseStorage storage = FirebaseStorage.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null){
            String uuid = user.getUid();
            File directorio = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            String filename = "Clase 1.pdf";
            File archivo = new File(directorio, filename);
            Uri fileUri = Uri.fromFile(archivo);
            StorageReference storageRef = storage.getReference().child(uuid+"/"+filename);
            UploadTask uploadTask = storageRef.putFile(fileUri);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.d("infoApp", "subido exitoso");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("infoApp", "error"+e.getCause());
                }
            });
        }

    }
}