package com.example.kirin.feedback_master;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.*;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.hardware.Sensor;
import android.view.View;
import android.content.Intent;
import java.text.SimpleDateFormat;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.os.*;
import java.util.*;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
//android wifi location;
//

class Temperature{
    String time, location;
    int lvl;
    public Temperature(){

    }
    public Temperature(String time, String location, int lvl){
        this.location = location;
        this.time = time;
        this.lvl  = lvl;
    }
}
public class sensorAndFeedback extends AppCompatActivity implements View.OnClickListener {

    int tempLevel = 0;
    private String fileName = "Feedback.txt";
    private Context mContext;
    private String temp = Sensor.STRING_TYPE_AMBIENT_TEMPERATURE;
    private Button wifiBut, fillBut;
    private SeekBar seekBar;
    private Button subBut, nextBut;
    private TextView barView1, barView2;
    private AlertDialog.Builder builder;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd   HH:mm:ss");
    Date curDate = new Date(System.currentTimeMillis());
    String datetime = formatter.format(curDate);
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference dr = database.getReference("Temperature");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_and_feedback);

        
        mContext = getApplicationContext();
        subBut = (Button) findViewById(R.id.subButton);
        nextBut = (Button) findViewById(R.id.nextButton);
        subBut.setOnClickListener(this);
        nextBut.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                intent.setClass(sensorAndFeedback.this, LightPage.class);
                startActivity(intent);
            }
        });
        wifiBut= (Button) findViewById(R.id.wifiButton);
        fillBut  = (Button) findViewById(R.id.frontButton);
        wifiBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(sensorAndFeedback.this,WifiScan.class);
                startActivity(intent);
            }
        });
        fillBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(sensorAndFeedback.this, FrontPage.class);
                startActivity(intent);
            }
        });
        seekBar = (SeekBar) findViewById(R.id.seekBar3);
        barView1 = (TextView) findViewById(R.id.barView1);
        barView2 = (TextView) findViewById(R.id.barView2);
        seekBar.setOnSeekBarChangeListener(seekbarChangeListener);
    }

    // dialog method
    private void Dialog(View view) {
        builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("Thank You!");
        builder.setMessage("Do you want to submit?");
        builder.setPositiveButton(R.string.postive_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(), R.string.toast_postive, Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton(R.string.negative_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(), R.string.toast_negative, Toast.LENGTH_SHORT).show();
            }
        });

        builder.setCancelable(true);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.subButton:
                writeNewTemperature(datetime, FrontPage.loca, tempLevel);
                Dialog(v);
                FileHelper fHelper = new FileHelper(mContext);
                String filename = fileName;
                String filedetail = datetime + "\t" + FrontPage.loca + "\t" + "Level of temperature: "+tempLevel + "\n";
                try {
                    fHelper.save(filename, filedetail);
                    Toast.makeText(getApplicationContext(), "SAVE SUCCESSFULLY", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "SAVE FAILED", Toast.LENGTH_SHORT).show();
                }
        }
    }


    private OnSeekBarChangeListener seekbarChangeListener = new OnSeekBarChangeListener() {

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // TODO Auto-generated method stub
            int seekProgress = seekBar.getProgress();
            if (seekProgress < 25) {
                seekBar.setProgress(0);
            } else if (seekProgress >= 25 && seekProgress < 50) {
                seekBar.setProgress(25);
            } else if (seekProgress >= 50 && seekProgress < 75) {
                seekBar.setProgress(50);
            } else if (seekProgress >= 75 && seekProgress < 100) {
                seekBar.setProgress(75);
            }
            barView2.setText("Stopped");

        }
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // TODO Auto-generated method stub
            barView2.setText("What is your feeling");
        }
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            barView2.setText("Please select");
            Message message = new Message();

            Bundle bundle = new Bundle();

            float pro = seekBar.getProgress();

            float num = seekBar.getMax();

            float result = (pro / num) * 4;

            tempLevel = (int) result;
            bundle.putInt("key", tempLevel);

            message.setData(bundle);

            message.what = 0;

            handler.sendMessage(message);

        }
    };

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.getData().getInt("key") == 0)
                barView1.setText("You feel VERY COLD");
            if (msg.getData().getInt("key") == 1)
                barView1.setText("You feel A LITTLE COLD");
            if (msg.getData().getInt("key") == 2)
                barView1.setText("You feel it is OK");
            if (msg.getData().getInt("key") == 3)
                barView1.setText("You feel A LITTLE HOT");
            if (msg.getData().getInt("key") == 4)
                barView1.setText("You feel VERY HOT");

        }

    };

    private void writeNewTemperature(String datetime, String location, int lvl) {
        Temperature temperature = new Temperature(datetime, location, lvl);

        dr.push().setValue(temperature);

    }


}