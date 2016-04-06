package com.example.peter.SendSMSFromLocationAlarm;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.Calendar;

public class SendSMS extends BroadcastReceiver {
    final private String TAG = "mauuuu";
    String text;
    @Override
    public void onReceive(Context arg0, Intent intent) {
        // For our recurring task, we'll just display a message
        Log.i(TAG,"I'm in SendSMS");
        boolean[] week = intent.getBooleanArrayExtra("week");
        Log.i(TAG, "day in a week array: " + (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1));
        Log.i(TAG, "week length " + week.length);
        Log.i(TAG, "first if" + week[Calendar.getInstance().get(Calendar.DAY_OF_WEEK)-1]);
        if(week[Calendar.getInstance().get(Calendar.DAY_OF_WEEK)-1]) {

            LatLng place = (LatLng) intent.getExtras().get("place");
            int distance = intent.getIntExtra("distance",5);
            Log.i(TAG, "everything OK?");

            if (nearLocation(arg0, place, distance)) {
                Log.i(TAG, "I'm in if");
                Toast.makeText(arg0, "I'm SendingSMS", Toast.LENGTH_SHORT).show();
                String number = intent.getStringExtra("number");
                text = text + intent.getStringExtra("message");
                Log.i("mauuuu", "Sending " + text + " to " + number);
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(number, null, text, null, null);
            }
        }
    }
    private boolean nearLocation(Context arg0, LatLng place, int distance){

        LocationManager lm = (LocationManager) arg0.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = lm.getBestProvider(criteria, true);
        Location myLocation = lm.getLastKnownLocation(provider);
        int R = 6371; // km
        double x = (place.longitude - myLocation.getLongitude()) * Math.cos((myLocation.getLatitude() + place.latitude) / 2);
        double y = (place.latitude - myLocation.getLatitude());
        double distSquerd = x * x + y * y * R * R;
        Log.i(TAG,"selected place:"+place.latitude+" "+place.longitude+"    distance: "+distance);
        Log.i(TAG,"my place:"+myLocation.getLatitude()+" "+myLocation.getLongitude()+"    distance: "+distance);
        Log.i(TAG,"distance between "+Math.sqrt(distSquerd));
        text = "latitude: "+myLocation.getLatitude()+" longitude: "+myLocation.getLongitude()+" accuracy: "+myLocation.getAccuracy();
        return distance* distance > distSquerd;

    }

}