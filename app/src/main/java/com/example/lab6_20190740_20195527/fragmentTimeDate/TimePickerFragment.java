package com.example.lab6_20190740_20195527.fragmentTimeDate;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TimePicker;
import androidx.fragment.app.DialogFragment;
import com.example.lab6_20190740_20195527.activities.MainActivity;
import java.util.Calendar;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
        return timePickerDialog;
    }

    public void onTimeSet(TimePicker view, int hour, int minute) {
        MainActivity mainActivity = (MainActivity) getActivity();
        if (hour>=23 && minute>30){
            hour= 23; minute=30;
        } else if (hour<6) {
            hour = 6; minute =0;
        }
        mainActivity.respuestaTimeDialog(hour, minute);
    }
}