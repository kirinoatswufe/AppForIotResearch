package com.example.kirin.feedback_master;

import android.*;
import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static java.lang.Math.abs;

class Network{
    String SSID, info;
    public Network(String SSID, String info){
        this.SSID = SSID;
        this.info = info;
    }
}
public class WifiScan extends AppCompatActivity {
    private static final int REQUEST_FINE_LOCATION=0;
    private TextView showLa, showLong;

    private ListView networkList;
    private Button backBut;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference dr = database.getReference("Network");
    List<ScanResult> listRes;
    Intent i = new Intent("com.example.kirin.WifiScan");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_info);


        networkList = (ListView)findViewById(R.id.netList);

        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        //String longToView = new Double(location.getLongitude()).toString();
        //String laToView = new Double(location.getLatitude()).toString();
        //showLa=(TextView)findViewById(R.id.showLa);
        //showLong=(TextView)findViewById(R.id.showLong);
        //showLong.setText("\t\t\t"+longToView);
        //showLa.setText("\t\t\t"+laToView);


        WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        WifiInfo info = wifiManager.getConnectionInfo();
        listRes = wifiManager.getScanResults();
        String[] listS = new String[listRes.size()];
        int[] listSS = new int[listRes.size()];
        String[] listBS = new String[listRes.size()];
        double[] distance = new double[listRes.size()];
        String[] subitem = new String[listRes.size()];
        if(listRes!=null){
            for( int i=0;i<listRes.size();i++){
                ScanResult scanResult = listRes.get(i);
                listBS[i]=scanResult.BSSID;//BSSID
                listS[i]=scanResult.SSID;//SSID
                listSS[i]=scanResult.level;//RSSI

                double d = (abs(listSS[i])-40.2)/(10*2);
                distance[i]= Math.pow(10, d);
            }
            for( int i=0;i<listRes.size();i++){
                subitem[i]="BSSID: " + listBS[i] + " RSSI: "+ String.valueOf(listSS[i]) + " Distance: " + String.valueOf(distance[i]);
            }
        }
        final ArrayList<HashMap<String, String>> netMap = new ArrayList<>();
        for(int i = 0; i<listRes.size();i++){
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("SSID" , listS[i]);
            hashMap.put("Info" , subitem[i]);
            netMap.add(hashMap);
        }
        ListAdapter listAdapter = new SimpleAdapter(
                this,
                netMap,
                R.layout.list_item,
                new String[]{"SSID" , "Info"} ,
                new int[]{R.id.title, R.id.subItem});
        networkList.setAdapter(listAdapter);

        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_FINE_LOCATION);
            }
        }

        for( int i=0;i<listRes.size();i++){
            writeDB(listS[i], subitem[i]);
        }


        backBut=(Button)findViewById(R.id.backBut);
        backBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dr.removeValue();
                Intent intent = new Intent();
                intent.setClass(WifiScan.this, sensorAndFeedback.class);
                startActivity(intent);
            }
        });

    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_FINE_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // permission was granted, yay! Do the
                // contacts-related task you need to do.

            } else {
                Toast.makeText(this, "Permission was not granted", Toast.LENGTH_LONG).show();
            }
        }else{
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void writeDB(String SSID, String info){
        Network network = new Network(SSID, info);
        dr.child(SSID).setValue(info);

    }
}
