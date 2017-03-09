package com.example.kirin.feedback_master;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class FrontPage extends AppCompatActivity {
    private TextView view ;
    private Spinner spinner;
    private ArrayAdapter adapter;
    private Button back;
    public static String loca;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_page);

        view = (TextView) findViewById(R.id.spinnerText);
        spinner = (Spinner) findViewById(R.id.locSpinner);
        adapter = ArrayAdapter.createFromResource(this,R.array.Location_array,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new SpinnerSelectedListener());

        spinner.setVisibility(View.VISIBLE);

        back = (Button)findViewById(R.id.backButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(FrontPage.this, sensorAndFeedback.class);
                startActivity(intent);
            }
        });
    }


    class SpinnerSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {

            view.setText("YOUR LOCATION NOW IS："+adapter.getItem(arg2));
            loca = adapter.getItem(arg2).toString();
        }
        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }

}

