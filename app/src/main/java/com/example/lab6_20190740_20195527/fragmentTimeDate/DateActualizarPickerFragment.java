package com.example.lab6_20190740_20195527.fragmentTimeDate;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.lab6_20190740_20195527.activities.ActualizarActivity;
import com.example.lab6_20190740_20195527.activities.CrearActivity;

import java.time.LocalDate;
import java.util.Calendar;

public class DateActualizarPickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        ActualizarActivity actualizarActivity = (ActualizarActivity) getActivity();
        actualizarActivity.setFecha(LocalDate.of(year, month, day));
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Calendar c = Calendar.getInstance();
        return new DatePickerDialog(getActivity(), this,
                c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH));
    }
}
