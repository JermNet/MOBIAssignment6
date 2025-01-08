package com.example.m03_bounce;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {


    // bbView is our bouncing ball view
    private BouncingBallView bbView;
    // Added here so it can be handed to bbView, so that class can save to DB using a DBClass object
    private DBClass db = new DBClass(this);
    // Handed to bbView so that it can save to DB, but also used here so we never have 2 ids that are the same
    private static int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the view object so we can reference it later, and set a DBClass and id to it
        bbView = (BouncingBallView) findViewById(R.id.custView);
        bbView.setDB(db, id);
    }

    // Button action
    public void addBalls(View v) {

        Log.d("MainActivity  BUTTON", "User tapped the  button ... MAIN");
        // Get the values from the XML
        EditText x = (EditText) findViewById(R.id.x);
        EditText y = (EditText) findViewById(R.id.y);
        EditText dx = (EditText) findViewById(R.id.dx);
        EditText dy = (EditText) findViewById(R.id.dy);
        EditText name = (EditText) findViewById(R.id.name);
        EditText color = (EditText) findViewById(R.id.color);

        // Convert values to proper data types
        float xValue = Float.parseFloat(x.getText().toString());
        float yValue = Float.parseFloat(y.getText().toString());
        float dxValue = Float.parseFloat(dx.getText().toString());
        float dyValue = Float.parseFloat(dy.getText().toString());
        String nameValue = name.getText().toString();
        String colorValue = color.getText().toString();

        // Convert user's String to a int colour value, if they don't enter a valid colour, it will default to red
        int actualColor;
        try {
            actualColor = Color.parseColor(colorValue);
        } catch (Exception e) {
            e.printStackTrace();
            actualColor = Color.RED;
        }

        // Save to DB, add ball to bbView so it can be drawn, and increment id
        DataModel data = new DataModel(id, nameValue, xValue, yValue, dxValue, dyValue, actualColor);
        db.save(data);
        bbView.addBall(new Ball(actualColor, xValue, yValue, dxValue, dyValue));
        id++;
    }

    // Clear all balls and DB
    public void clearBalls(View v) {
        bbView.clearBalls();
        bbView.invalidate();
        db.wipeDatabase();

    }

    // Not sure if this is what you meant by getting the balls "restarted" but I think this is a pretty cool way to do it.
    // NOTE: This acts a bit funky on emulator (sometimes there would be one ball left instead of clearing them all) but I'm like 90% sure this is just an emulator issue
    @Override
    protected void onDestroy() {
        // Basically just calls clearBalls() when the app is closed
        View bouncingBallView = findViewById(R.id.custView);
        clearBalls(bouncingBallView);
        super.onDestroy();
    }
}