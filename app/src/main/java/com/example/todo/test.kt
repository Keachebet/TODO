/*
package com.example.todo

import android.widget.TimePicker

if (v == btnDatePicker) {

    // Get Current Date
    final Calendar c = Calendar.getInstance();
    mYear = c.get(Calendar.YEAR);
    mMonth = c.get(Calendar.MONTH);
    mDay = c.get(Calendar.DAY_OF_MONTH);


    DatePickerDialog datePickerDialog = new DatePickerDialog(this,
        new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year,
                int monthOfYear, int dayOfMonth) {

                txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

            }
        }, mYear, mMonth, mDay);
    datePickerDialog.show();
}
if (v == btnTimePicker) {

    // Get Current Time
    final Calendar c = Calendar.getInstance();
    mHour = c.get(Calendar.HOUR_OF_DAY);
    mMinute = c.get(Calendar.MINUTE);

    // Launch Time Picker Dialog
    TimePickerDialog timePickerDialog = new TimePickerDialog(this,
        new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(
                TimePicker view, int hourOfDay,
                int minute) {

                txtTime.setText(hourOfDay + ":" + minute);
            }
        }, mHour, mMinute, false);
    timePickerDialog.show();*/
