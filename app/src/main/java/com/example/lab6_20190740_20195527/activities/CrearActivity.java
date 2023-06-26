package com.example.lab6_20190740_20195527.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.lab6_20190740_20195527.R;
import com.example.lab6_20190740_20195527.databinding.ActivityCrearBinding;

public class CrearActivity extends AppCompatActivity {
    ActivityCrearBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCrearBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}