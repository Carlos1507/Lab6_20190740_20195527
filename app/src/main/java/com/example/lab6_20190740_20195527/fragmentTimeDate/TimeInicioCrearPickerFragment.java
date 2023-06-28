package com.example.lab6_20190740_20195527.fragmentTimeDate;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import androidx.fragment.app.DialogFragment;

import com.example.lab6_20190740_20195527.activities.CrearActivity;

import java.time.LocalTime;
import java.util.Calendar;

public class TimeInicioCrearPickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        CrearActivity crearActivity = (CrearActivity) getActivity();
        if (hour>=23 && minute>30){
            hour= 23; minute=30;
        } else if (hour<6) {
            hour = 6; minute =0;
        }
        LocalTime horaInicio = LocalTime.of(hour, minute);
        crearActivity.setHoraInicio(horaInicio);
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }
}
