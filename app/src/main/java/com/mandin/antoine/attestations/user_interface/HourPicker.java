package com.mandin.antoine.attestations.user_interface;

import android.view.View;
import android.widget.NumberPicker;

import com.mandin.antoine.attestations.R;

import java.util.Calendar;
import java.util.Date;

public class HourPicker {
    private NumberPicker hourPicker, minutePicker;

    public HourPicker(View contentView) {
        hourPicker = contentView.findViewById(R.id.hour_picker);
        minutePicker = contentView.findViewById(R.id.minute_picker);

        hourPicker.setMinValue(0);
        hourPicker.setMaxValue(23);
        minutePicker.setMinValue(0);
        minutePicker.setMaxValue(60);

        setDate(new Date(System.currentTimeMillis()));
    }

    public Date getDate(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        calendar.set(Calendar.HOUR_OF_DAY, hourPicker.getValue());
        calendar.set(Calendar.MINUTE, minutePicker.getValue());

        return calendar.getTime();
    }

    public void setDate(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        hourPicker.setValue(calendar.get(Calendar.HOUR_OF_DAY));
        minutePicker.setValue(calendar.get(Calendar.MINUTE));
    }
}
