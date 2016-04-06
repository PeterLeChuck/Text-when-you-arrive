package com.example.peter.SendSMSFromLocationAlarm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;


public class MapsActivity extends Activity implements OnMapReadyCallback {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.map_activity);
        MapFragment map = (com.google.android.gms.maps.MapFragment) getFragmentManager().findFragmentById(R.id.mapview);
        map.getMapAsync(this);

    }
    public void onMapReady(final GoogleMap googleMap){

        Toast.makeText(getApplicationContext(), "Select and hold your position", Toast.LENGTH_LONG).show();

        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng point) {

            Intent i = new Intent();
            i.putExtra("coordinates", point);
            MapsActivity.this.setResult(RESULT_OK, i);
            finish();
            }
        });
    }
}