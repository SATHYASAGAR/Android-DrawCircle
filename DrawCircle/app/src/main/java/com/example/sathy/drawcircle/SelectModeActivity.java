package com.example.sathy.drawcircle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class SelectModeActivity extends AppCompatActivity {

    static Integer drawModeSwitch = 1, deleteModeSwitch = 0, moveModeSwitch = 0;
    static String defaultColor="BLACK";
    static String circleColorCurrent = defaultColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("CircleApp - Draw Mode");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_mode_activity);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menus, menu);
        return true;
    }

    public void setColor(MenuItem selectedItem) {
        circleColorCurrent = String.valueOf(selectedItem);
    }

    public void switchToDeleteMode(MenuItem selectedItem) {
        setTitle("CircleApp - Delete Mode");
        drawModeSwitch = 0;
        moveModeSwitch = 0;
        deleteModeSwitch = 1;
    }

    public void switchToMoveMode(MenuItem selectedItem) {
        setTitle("CircleApp - Move Mode");
        drawModeSwitch = 0;
        moveModeSwitch = 1;
        deleteModeSwitch = 0;
    }

    public void switchToDrawMode(MenuItem selectedItem) {
        setTitle("CircleApp - Draw Mode");
        drawModeSwitch = 1;
        moveModeSwitch = 0;
        deleteModeSwitch = 0;
    }
}