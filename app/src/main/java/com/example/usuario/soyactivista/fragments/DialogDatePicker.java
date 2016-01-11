package com.example.usuario.soyactivista.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import soy_activista.quartzapp.com.soy_activista.R;

/**
 * Created by Brahyam on 25/11/2015.
 */
public class DialogDatePicker extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {
    private String formattedDate;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dpd = new DatePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT,this,year, month, day);
        DatePicker dp = dpd.getDatePicker();
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
        c.set(year, month, day);


        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        formattedDate = sdf.format(c.getTime());

        if(((ImageButton)getActivity().findViewById(R.id.imgCalendarInicio)).isSelected()==true)
            ((TextView)getActivity().findViewById(R.id.textViewFechaInicio)).setText(formattedDate);

        if(((ImageButton)getActivity().findViewById(R.id.imgCalendarFin)).isSelected()==true)
            ((TextView)getActivity().findViewById(R.id.textViewFechaFin)).setText(formattedDate);


        /*if (((EditText) getActivity().findViewById(R.id.editInicio)).isFocused())
            ((EditText) getActivity().findViewById(R.id.editInicio)).setText(formattedDate);

        if (((EditText) getActivity().findViewById(R.id.editFin)).isFocused())
            ((EditText) getActivity().findViewById(R.id.editFin)).setText(formattedDate);*/


    }

    }