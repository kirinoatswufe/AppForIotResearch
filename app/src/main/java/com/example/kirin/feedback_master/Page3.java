package com.example.kirin.feedback_master;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.content.Context;
import java.io.*;
import android.widget.*;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Page3 extends AppCompatActivity {
    private Button showBut;
    private Button clearBut, uploadBut;
    private Context mContext;
    private TextView HisView;
    private String fileName = "Feedback.txt";
    private ProgressDialog mProgressDialog;
    private FirebaseAuth mAuth;
    private StorageReference mStorage;
    private static final int File_Intent = 2;
    private static final String TAG = Page3.class.getSimpleName();
    DatabaseReference dr = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page3);
        mContext = getApplicationContext();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = dr.child("Date");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        HisView = (TextView) findViewById(R.id.HisView);
        HisView.setMovementMethod(ScrollingMovementMethod.getInstance());
        showBut=(Button)findViewById(R.id.showHisButton);
        showBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String detail = "";
                FileHelper fHelper2 = new FileHelper(getApplicationContext());
                try {
                    String fname = "Feedback.txt";
                    detail = fHelper2.read(fname);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                HisView.setText(detail);
            }
        });
        clearBut=(Button)findViewById(R.id.clearbutton);
        clearBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileHelper fHelper = new FileHelper(mContext);
                String filename = fileName;
                String filedetail = "Date"+ "\t" + "Location" + "\t" + "Feedback" + "\n";
                try {
                    fHelper.delete(filename, filedetail);
                    Toast.makeText(getApplicationContext(), "DELETE SUCCESSFULLY", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "DELETE FAILED", Toast.LENGTH_SHORT).show();
                }
            }
        });
        /*uploadBut = (Button)findViewById(R.id.uploadBut);
        uploadBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Uri file = Uri.fromFile(getFilePathByName())
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("file/*");
                startActivityForResult(intent,File_Intent);
            }
        });*/

        String detail = "";
        FileHelper fHelper2 = new FileHelper(getApplicationContext());
        try {
            String fname = "Feedback.txt";
            detail = fHelper2.read(fname);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HisView.setText(detail);
    }


}
