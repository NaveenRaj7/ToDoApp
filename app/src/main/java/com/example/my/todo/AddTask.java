package com.example.my.todo;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.example.my.todo.MainActivity;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddTask extends AppCompatActivity {


    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView;
    private TextView timeView;
    private int year, month, day;


    private int pHour;
    private int pMinute;
    private int am_pm;


    private static final String TAG = "AddTask";
    private EditText mText;
    private DatabaseReference mFirebaseDatabaseReference2;
    public static final String MESSAGES_CHILD = "messages";


    private String mNewTime;
    private String dayOfWeek;

    private String mNewDate;
    private Spinner spinner2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        timeView = (TextView) findViewById(R.id.textView4);
        calendar = Calendar.getInstance();
        pHour = calendar.get(Calendar.HOUR_OF_DAY);

        pMinute = calendar.get(Calendar.MINUTE);
        //am_pm = calendar.get(Calendar.AM_PM);
        showTime(pHour, pMinute);

        Log.e(TAG, "am pm" + am_pm);








        dateView = (TextView) findViewById(R.id.textView3);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        SimpleDateFormat simpledateformat = new SimpleDateFormat("EEEE");
        Date date = new Date(year, month, day-1);
        dayOfWeek = simpledateformat.format(date);
        showDate(year, month+1, day);




        mFirebaseDatabaseReference2 = FirebaseDatabase.getInstance().getReference();



        spinner2 = (Spinner) findViewById(R.id.assign_spinner);

        // Create a new Adapter
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1);

        // Assign adapter to ListView
        spinner2.setAdapter(adapter);

        // Use Firebase to populate the list.
        Firebase.setAndroidContext(this);



        new Firebase("https://todoapp-c1e67.firebaseio.com/users")
                .addChildEventListener(new ChildEventListener() {
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        adapter.add((String)dataSnapshot.getKey());
                    }
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        //adapter.remove((String)dataSnapshot.child("text").getValue());
                    }
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) { }
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) { }
                    public void onCancelled(FirebaseError firebaseError) { }
                });







        // Add items via the Button and EditText at the bottom of the window.
        mText = (EditText) findViewById(R.id.newtodoText);
        final Button button = (Button) findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CommonUtilities friendlyMessage2 = new CommonUtilities(mText.getText().toString(), String.valueOf(spinner2.getSelectedItem()), mNewDate, mNewTime );

                Log.e(TAG, "Hello" + mText.getText().toString()+ mNewDate);

                mFirebaseDatabaseReference2.child(MESSAGES_CHILD).push().setValue(friendlyMessage2);
                mText.setText("");
                startActivity(new Intent(AddTask.this, MainActivity.class));
                finish();

            }
        });

















        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }




    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
        Toast.makeText(getApplicationContext(), "ca", Toast.LENGTH_SHORT)
                .show();
    }



    @SuppressWarnings("deprecation")
    public void setTime(View view) {
        showDialog(0);
        /*int hour = timePicker1.getCurrentHour();
        int min = timePicker1.getCurrentMinute();
        showTime(hour, min);*/
    }





    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        else if(id == 0) {
            return new TimePickerDialog(this, mTimeSetListener, pHour, pMinute, false);
        }
        return null;
    }


    private TimePickerDialog.OnTimeSetListener mTimeSetListener =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    pHour = hourOfDay;
                    pMinute = minute;
                    showTime(hourOfDay, minute);
                }
            };








    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            // arg1 = year
            // arg2 = month
            // arg3 = day

            SimpleDateFormat simpledateformat = new SimpleDateFormat("EEEE");
            Date date = new Date(arg1, arg2, arg3-1);
            dayOfWeek = simpledateformat.format(date);




            showDate(arg1, arg2+1, arg3);
        }
    };

    private void showDate(int year, int month, int day) {

        mNewDate = (new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year).append(" - ").append(dayOfWeek)).toString();
        dateView.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year).append(" - ").append(dayOfWeek));
    }



    private void showTime(int hourOfDay, int minute) {

        mNewTime = (new StringBuilder().append(hourOfDay).append(":")
                .append(minute)).toString();
        timeView.setText(new StringBuilder().append(hourOfDay).append(":")
                .append(minute));
    }












}
