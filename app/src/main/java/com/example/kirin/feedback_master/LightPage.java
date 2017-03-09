package com.example.kirin.feedback_master;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
class Light{
    String time, location;
    int lvl;
    public Light(){

    }
    public Light(String time, String location, int lvl){
        this.location = location;
        this.time = time;
        this.lvl  = lvl;
    }
}
public class LightPage extends AppCompatActivity {
    private SeekBar lightBar;
    private String fileName = "Feedback.txt";
    private Context mContext;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    Date curDate = new Date(System.currentTimeMillis());
    String str = formatter.format(curDate);
    private int tempLevel = 0;
    private AlertDialog.Builder builder;
    private Button sub3;
    private Button next3;
    private TextView lightBarView1, lightBarView2;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference dr = database.getReference("Light");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light_page);

        mContext=getApplicationContext();
        lightBar = (SeekBar) findViewById(R.id.lightBar);
        sub3 = (Button) findViewById(R.id.subButton3);
        next3 = (Button) findViewById(R.id.nextButton3);
        sub3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog(v);
                save();
            }
        });
        next3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(LightPage.this, Page2.class);
                startActivity(intent);
            }
        });

        lightBarView1 = (TextView)findViewById(R.id.lightView1);
        lightBarView2 = (TextView)findViewById(R.id.lightView2);
        lightBar.setOnSeekBarChangeListener(seekbarChangeListener);
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
        writeNewLight(str, FrontPage.loca, tempLevel);
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
            lightBarView2.setText("Stopped");

        }
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // TODO Auto-generated method stub
            lightBarView2.setText("What is your feeling");
        }
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            lightBarView2.setText("Please select");
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
                lightBarView1.setText("You feel VERY DARK");
            if (msg.getData().getInt("key") == 3)
                lightBarView1.setText("You feel A LITTLE DARK");
            if (msg.getData().getInt("key") == 2)
                lightBarView1.setText("You feel It Is OK");
            if (msg.getData().getInt("key") == 1)
                lightBarView1.setText("You feel QUIET");
            if (msg.getData().getInt("key") == 0)
                lightBarView1.setText("You feel VERY QUIET");

        }

    };

    private void save() {

        FileHelper fHelper = new FileHelper(mContext);
        String filename = fileName;
        String filedetail = str + "\t" + FrontPage.loca + "\t" + "level of light: "+ "\t" + tempLevel + "\n";
        try {
            fHelper.save(filename, filedetail);
            Toast.makeText(getApplicationContext(), "SAVE SUCCESSFULLY", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "SAVE FAILED", Toast.LENGTH_SHORT).show();
        }
    }

    private void writeNewLight(String str, String location, int lvl) {
        Light light = new Light(str, location, lvl);

        dr.push().setValue(light);



    }
}
