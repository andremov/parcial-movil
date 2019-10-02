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
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;
import com.example.myapplication.objects.ServerResponse;
import com.example.myapplication.objects.UserHistoryLocations;
import com.example.myapplication.utils.Settings;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONObject;
import java.util.Calendar;

public class DatePickerDialogShells extends BottomSheetDialogFragment {

    Calendar calendar;
    DatePickerDialog datePickerDialog;
    EditText inputDateStart, inputDateEnd;
    ImageButton btnDateStart, btnDateEnd;
    Button btnSearchInDates;
    String startSearchDate, endSearchDate;
    MapActivity mapActivity;

    public DatePickerDialogShells(){
        mapActivity = ((MapActivity)getActivity());
    }

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
                    public void onDateSet(DatePicker view, int mYear, int mMonth, int mDay) {
                        String monthString = mMonth + "";
                        String dayString = mDay + "";
                        if(mMonth < 10){
                            monthString = "0" + mMonth;
                        }
                        if(mDay < 10){
                            dayString  = "0" + mDay;
                        }
                        if(dayString.length() == 1){
                            dayString = "0" +dayString;
                        }
                        if(monthString .length() == 1){
                            monthString  = "0" +monthString;
                        }
                        String date = mYear + "-" + monthString + "-" + dayString;
                        inputDateStart.setText(date);
                        startSearchDate = date + " 00:00:00.001";
                    }
                }, year, month, day);
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
                    public void onDateSet(DatePicker view, int mYear, int mMonth, int mDay) {
                        String monthString = mMonth + "";
                        String dayString = mDay + "";
                        if(mMonth < 10){
                            monthString = "0" + mMonth;
                        }
                        if(mDay < 10){
                            dayString  = "0" + mDay;
                        }
                        String date = mYear + "-" + monthString + "-" + dayString;
                        inputDateEnd.setText(date);
                        endSearchDate = date + " 23:59:59.999";
                    }
                }, year, month, day);
                datePickerDialog.updateDate(2019, 9, 1);
                datePickerDialog.show();
            }
        });

        btnSearchInDates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    RequestQueue queue = Volley.newRequestQueue((MapActivity)getActivity());

                    JSONObject jsonBody = new JSONObject();
                    jsonBody.put("first_value", startSearchDate);
                    jsonBody.put("last_value", endSearchDate);

                    JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, Settings.getUrlAPI() + "locations/" + ((MapActivity) getActivity()).getLastMarkerClicked(), jsonBody,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    String res = response.toString();
                                    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss.SSS").create();
                                    ServerResponse responseJSON = gson.fromJson(res, ServerResponse.class);
                                    if(responseJSON.isSuccess()) {
                                        String data = responseJSON.getData();
                                        UserHistoryLocations[] userHistoryLocations = gson.fromJson(data, UserHistoryLocations[].class);
                                        ((MapActivity) getActivity()).setUserHistoryLocations(userHistoryLocations);
                                        ((MapActivity) getActivity()).drawUsersHistoryLocations();
                                    } else {
                                        Toast.makeText(getActivity(), "Ups! No hay conexión", Toast.LENGTH_SHORT). show();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getActivity(), "Ups! No hay conexión", Toast.LENGTH_SHORT). show();
                        }
                    });
                    queue.add(stringRequest);
                } catch(Exception e) { }
            }
        });

        return view;
    }
}
