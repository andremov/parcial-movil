package com.example.myapplication.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Calendar;

public class DatePickerDialogShells extends BottomSheetDialogFragment {

    Calendar calendar;
    DatePickerDialog datePickerDialog;
    EditText inputDateStart, inputDateEnd;
    ImageButton btnDateStart, btnDateEnd;
    Button btnSearchInDates;
    String startSearchDate, endSearchDate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.date_range_picker, container, false);

        inputDateStart = (EditText) view.findViewById(R.id.input_date_start);
        inputDateEnd = (EditText) view.findViewById(R.id.input_date_end);
        btnDateStart = (ImageButton) view.findViewById(R.id.date_start);
        btnDateEnd = (ImageButton) view.findViewById(R.id.date_end);
        btnSearchInDates = (Button) view.findViewById(R.id.btn_search_in_dates);

        btnDateStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int mDay, int mMonth, int mYear) {
                        String date = mYear + "-" + (mMonth + 1) + "-" + mDay;
                        inputDateStart.setText(date);
                        startSearchDate = date + " 00:00:00.000";
                    }
                }, day, month, year);
                datePickerDialog.updateDate(2019, 9, 1);
                datePickerDialog.show();
            }
        });

        btnDateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int mDay, int mMonth, int mYear) {
                        String date = mYear + "-" + (mMonth + 1) + "-" + mDay;
                        inputDateEnd.setText(date);
                        endSearchDate = date + " 00:00:00.000";
                    }
                }, day, month, year);
                datePickerDialog.updateDate(2019, 9, 1);
                datePickerDialog.show();
            }
        });

        btnSearchInDates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Buscar Locations", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
