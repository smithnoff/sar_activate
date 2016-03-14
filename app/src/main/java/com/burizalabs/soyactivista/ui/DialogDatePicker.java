package com.burizalabs.soyactivista.ui;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.TextView;

import com.burizalabs.soyactivista.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by Brahyam on 25/11/2015.
 */
public class DialogDatePicker extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {
    private String formattedDate;
   public DatePickerDialog dpd;
    public Date ini, ending;

    DatePicker dp;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        dpd = new DatePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT,this,year, month, day);
         dp = dpd.getDatePicker();
        //Set the DatePicker minimum date selection to current date
        dp.setMinDate(c.getTimeInMillis());//get the current day

        // Create a new instance of DatePickerDialog and return it
        //return new DatePickerDialog(getActivity(), this, year, month, day);
        return dpd;
    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        //set date
        Calendar c = Calendar.getInstance();
        int dia = c.get(Calendar.DAY_OF_YEAR);
        c.set(year, month, day);


        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        formattedDate = sdf.format(c.getTime());

        if(((TextView)getActivity().findViewById(R.id.textViewFechaInicio)).isSelected()==true){
            ((TextView) getActivity().findViewById(R.id.textViewFechaInicio)).setText(formattedDate);
        }
        if(((TextView)getActivity().findViewById(R.id.textViewFechaFin)).isSelected()==true) {
            ((TextView) getActivity().findViewById(R.id.textViewFechaFin)).setText(formattedDate);
        }


    }

}