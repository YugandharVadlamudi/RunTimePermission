package com.example.kiran.runtimepermission;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private final int reqCode = 1;
    private Button btnMainClick;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpViews();
    }

    private void setUpViews() {

        context = getApplicationContext();
        btnMainClick = (Button) findViewById(R.id.btn_main_click);
        btnMainClick.setOnClickListener(this);
        intentCheck();
    }

    private void intentCheck() {
        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                String value = getIntent().getExtras().getString(key);
                Log.d(TAG, "Key: " + key + " Value: " + value);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_main_click:
                Toast.makeText(MainActivity.this, "button clicked", Toast.LENGTH_SHORT).show();
                checkPermissions();
                boolean check=isnetworkAvailable();
                if (check) {

                    Toast.makeText(MainActivity.this, "Networ Available & Service Started", Toast.LENGTH_SHORT).show();
                    startService(new Intent(getApplicationContext(), MyFirebaseInstanceIdService.class));
                    FirebaseMessaging.getInstance().subscribeToTopic("news");
                    Log.e(TAG, "onClick: firebaseToken->"+ FirebaseInstanceId.getInstance().getId());
                } else {
                    Toast.makeText(MainActivity.this, "NetWork Not Available", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private boolean isnetworkAvailable() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo.State networkState =
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        if (networkState == NetworkInfo.State.CONNECTED) {
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case reqCode:
                Toast.makeText(MainActivity.this, "Request may Granted or not granted"
                        , Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onRequestPermissionsResult: ->"+permissions
                        +"\n grantResults->"+grantResults 
                        +"\n permissions length->"+permissions.length
                        +"\n grantResults length->"+grantResults.length);
                if (permissions.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Permission Denayed", Toast.LENGTH_SHORT).show();
                }
                
                break;
        }
    }

    private void checkPermissions() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(MainActivity.this, "Permission granted do what we want"
                    , Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "Permission not granted", Toast.LENGTH_SHORT).show();
            reqPermissions();
        }
    }
    private void reqPermissions() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                MainActivity.this, Manifest.permission.CAMERA)) {
            Toast.makeText(MainActivity.this, "With  explanation", Toast.LENGTH_SHORT).show();
            reqDialogShow();
        } else {
            reqDialogShow();
        }
    }
    private void reqDialogShow() {
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}
                , reqCode);
    }
}
