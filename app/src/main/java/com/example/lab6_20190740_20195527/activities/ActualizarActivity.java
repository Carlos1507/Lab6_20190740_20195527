package com.example.lab6_20190740_20195527.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.lab6_20190740_20195527.R;
import com.example.lab6_20190740_20195527.databinding.ActivityActualizarBinding;

public class ActualizarActivity extends AppCompatActivity {
    ActivityActualizarBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityActualizarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}