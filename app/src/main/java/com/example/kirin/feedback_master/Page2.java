package com.example.kirin.feedback_master;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.*;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.hardware.Sensor;
import android.view.View;
import android.content.Intent;
import java.text.SimpleDateFormat;

import android.app.Activity;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.os.*;

import java.util.*;
import java.io.*;

import android.widget.SeekBar.OnSeekBarChangeListener;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

class Voice{
    String time, location;
    int lvl;
    public Voice(){

    }
    public Voice(String time, String location, int lvl){
        this.location = location;
        this.time = time;
        this.lvl  = lvl;
    }
}
public class Page2 extends AppCompatActivity {
    private SeekBar voiceBar;
    private String fileName = "Feedback.txt";
    private Context mContext;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd   HH:mm:ss");
    Date    curDate    =   new    Date(System.currentTimeMillis());
    String    str    =    formatter.format(curDate);
    private int tempLevel = 0;
    private AlertDialog.Builder builder;
    private Button sub2;
    private Button next2;
    private TextView voiceBarView1, voiceBarView2;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference dr = database.getReference("Voice");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page2);

        mContext=getApplicationContext();
        voiceBar = (SeekBar) findViewById(R.id.voiceBar);
        sub2 = (Button) findViewById(R.id.subButton2);
        next2 = (Button) findViewById(R.id.next2);
        sub2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog(v);
                save();
                writeNewVoice(str, FrontPage.loca, tempLevel);
            }
        });
        next2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Page2.this, Page3.class);
                startActivity(intent);
            }
            });

        voiceBarView1 = (TextView)findViewById(R.id.voiceBarView1);
        voiceBarView2 = (TextView)findViewById(R.id.voiceBarView2);
        voiceBar.setOnSeekBarChangeListener(seekbarChangeListener);
    }


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

    private SeekBar.OnSeekBarChangeListener seekbarChangeListener = new SeekBar.OnSeekBarChangeListener() {

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
            voiceBarView2.setText("Stopped");

        }
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // TODO Auto-generated method stub
            voiceBarView2.setText("What is your feeling");
        }
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            voiceBarView2.setText("Please select");
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
            if (msg.getData().getInt("key") == 4)
                voiceBarView1.setText("You feel VERY NOISY");
            if (msg.getData().getInt("key") == 3)
                voiceBarView1.setText("You feel A LITTLE NOISY");
            if (msg.getData().getInt("key") == 2)
                voiceBarView1.setText("You feel It Is OK");
            if (msg.getData().getInt("key") == 1)
                voiceBarView1.setText("You feel QUIET");
            if (msg.getData().getInt("key") == 0)
                voiceBarView1.setText("You feel VERY QUIET");

        }

    };

    private void save() {

        FileHelper fHelper = new FileHelper(mContext);
        String filename = fileName;
        String filedetail = str + "\t" + FrontPage.loca + "\t" + "level of noise: "+ "\t" + tempLevel + "\n";
        try {
            fHelper.save(filename, filedetail);
            Toast.makeText(getApplicationContext(), "SAVE SUCCESSFULLY", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "SAVE FAILED", Toast.LENGTH_SHORT).show();
        }
    }
    private void writeNewVoice(String str, String location, int lvl) {
        Voice voice = new Voice(str, location, lvl);

        dr.push().setValue(voice);


    }

}

