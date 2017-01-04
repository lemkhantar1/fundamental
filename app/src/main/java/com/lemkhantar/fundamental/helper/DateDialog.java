package com.lemkhantar.fundamental.helper;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;

import com.lemkhantar.fundamental.R;

import java.util.Calendar;

/**
 * Created by lemkhantar1 on 6/19/16.
 */
public class DateDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener {


    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), this, year, month, day);

    }

    public void onDateSet(DatePicker view, int year, int month, int  day)
    {
        String date = day+"-"+(month+1)+"-"+year;
        ((EditText)getActivity().findViewById(R.id.f_dateRechange)).setText(date);
    }
}
