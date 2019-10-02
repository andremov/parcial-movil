package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class DatePickerDialog extends BottomSheetDialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.date_range_picker, container, false);



        return view;
    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.date_start:
//                pickDate();
                break;
        }
    }

//    private void pickDate(){
//        DatePickerDialog recogerFecha = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                //Esta variable lo que realiza es aumentar en uno el mes ya que comienza desde 0 = enero
//                final int mesActual = month + 1;
//                //Formateo el día obtenido: antepone el 0 si son menores de 10
//                String diaFormateado = (dayOfMonth < 10)? CERO + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);
//                //Formateo el mes obtenido: antepone el 0 si son menores de 10
//                String mesFormateado = (mesActual < 10)? CERO + String.valueOf(mesActual):String.valueOf(mesActual);
//                //Muestro la fecha con el formato deseado
//                etFecha.setText(diaFormateado + BARRA + mesFormateado + BARRA + year);
//
//
//            }
//            //Estos valores deben ir en ese orden, de lo contrario no mostrara la fecha actual
//            /**
//             *También puede cargar los valores que usted desee
//             */
//        },anio, mes, dia);
//        //Muestro el widget
//        recogerFecha.show();
//    }
}
