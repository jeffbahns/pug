package com.squad.pug;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import static com.squad.pug.R.id.submit;


/*
**********************************************************
* ~~SO I DON'T FORGET~~
* NOTE FROM TREVOR ----> JEFF:
* This will fail if it is called (using the + button in the app) with no searchresultmodel object.
* We'll later implement a check to see if they've populated their result model yet.
*
* *********************************************************
 */



public class CreateGameActivity extends AppCompatActivity {
    TextView setTime;
    TextView setDate;
    EditText setNumPlayers;
    EditText setLocation;
    Button submitButton;
    String time;
    String date;
    String location;
    int numPlayers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);
        submitButton = (Button) findViewById(submit);
        setTime = (TextView) findViewById(R.id.setTime);
        setDate = (TextView) findViewById(R.id.setDate);
        setNumPlayers = (EditText) findViewById(R.id.setNumPlayers);
        ArrayList<String> courtNames = new ArrayList<>();
        try {
            courtNames = getIntent().getStringArrayListExtra("courtNames");
        } catch(RuntimeException e ) {
            e.printStackTrace();
        }
        // time picker
        setTime.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar mCurrentTime = Calendar.getInstance();
                        int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
                        int minute = mCurrentTime.get(Calendar.MINUTE);
                        TimePickerDialog mTimePicker = new TimePickerDialog(CreateGameActivity.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                setTime.setText(selectedHour + ":" + selectedMinute);
                            }
                        }, hour, minute, false);
                        mTimePicker.setTitle("Select a court time");
                        mTimePicker.show();
                    }
                }
        );

        // date picker
        setDate.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar mCurrentDate = Calendar.getInstance();
                        int year = mCurrentDate.get(Calendar.YEAR);
                        int month = mCurrentDate.get(Calendar.MONTH);
                        int day = mCurrentDate.get(Calendar.DAY_OF_MONTH);

                        DatePickerDialog mDatePicker = new DatePickerDialog(CreateGameActivity.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void  onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay ) {
                                setDate.setText( selectedMonth + " / " + selectedDay + " / " + selectedYear );
                            }
                        }, year, month, day);
                        mDatePicker.setTitle("Select a court date");
                        mDatePicker.show();
                    }
                }
        );

        // submit button
        submitButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        time = setTime.getText().toString();
                        date = setDate.getText().toString();
                        numPlayers = Integer.parseInt(setNumPlayers.getText().toString());
                        //location = setLocation.getText().toString();
                        location = "Dorotea Park";

                        Game newGame = new Game(1,  "Jeff Bahns", time, date, numPlayers, location);
                        createGame(newGame);

                        testPrint();
                        /*
                        AlertDialog alertDialog = new AlertDialog.Builder(CreateGameActivity.this).create();
                        alertDialog.setTitle("Hello");
                        alertDialog.setMessage("Your game was successfully created");
                        alertDialog.show();
                        */
                    }
                }
        );

            // autocomplete location picker
            String[] COURTS = new String[]{
                    "Lady Bug Park", "Dorotea Park", "Callinan Sports & Fitness Center",
                    "Rancho Cotate High School", "Sonoma State University"
            };
            AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.autoLocation);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, courtNames);
            textView.setAdapter(adapter);

    }

    public void makeToast() {
        Toast courtSnippet = Toast.makeText(CreateGameActivity.this, "Game successfully submitted", Toast.LENGTH_SHORT);
        courtSnippet.setGravity(Gravity.TOP| Gravity.CENTER_HORIZONTAL, 0, 0);
        courtSnippet.show();
    }

    protected void testPrint() {
        Log.v("Time : ", time);
        Log.v("Date : ", date);
        Log.v("Number Players : ", String.valueOf(numPlayers));
    }
    private void createGame(Game game){
        ServerRequests serverRequests = new ServerRequests(this);
        serverRequests.storedGameDataInBackground(game, new GetGameCallback() {
            @Override
            public void done(Game returnedGame) {
                startActivity(new Intent(CreateGameActivity.this, MapsActivity.class));

            }
        });

    }
}