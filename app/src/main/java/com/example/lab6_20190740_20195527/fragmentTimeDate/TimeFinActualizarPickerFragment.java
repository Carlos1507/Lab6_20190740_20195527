package com.example.lab6_20190740_20195527.fragmentTimeDate;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import androidx.fragment.app.DialogFragment;

import com.example.lab6_20190740_20195527.activities.ActualizarActivity;

import java.time.LocalTime;
import java.util.Calendar;

public class TimeFinActualizarPickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        ActualizarActivity actualizarActivity = (ActualizarActivity) getActivity();
        LocalTime horaFin = LocalTime.of(hour, minute);
        actualizarActivity.setHoraFin(horaFin);
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }
}
