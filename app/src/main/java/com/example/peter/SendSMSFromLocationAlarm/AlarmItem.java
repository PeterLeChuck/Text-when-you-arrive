package com.example.peter.SendSMSFromLocationAlarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.google.android.gms.maps.model.LatLng;

import java.util.Calendar;

/**
 * Created by Peter on 9.3.2015.
 */
public class AlarmItem {


    private String mMessage;
    private String mPhoneName;
    private String mPhoneNumber;
    private boolean[] mWeek;
    private Calendar mTime;
    private LatLng mPlace;
    private int mDistance;
    private Intent mIntent;

    private Context mContext;
    private AlarmManager mAlarmManager;
    private PendingIntent mPendingIntent;

    public AlarmItem(Context context){
        mContext = context;
        mAlarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);

    }

    protected String getmMessage() {
        return mMessage;
    }

    protected void setmMessage(String mMessage) {
        this.mMessage = mMessage;
    }

    protected Intent getmIntent() {
        return mIntent;
    }

    protected void setmIntent(Intent mIntent) {
        this.mIntent = mIntent;
    }

    protected String getmPhoneName() {
        return mPhoneName;
    }

    protected void setmPhoneName(String mPhoneName) {
        this.mPhoneName = mPhoneName;
    }

    protected String getmPhoneNumber() {
        return mPhoneNumber;
    }

    protected void setmPhoneNumber(String mPhoneNumber) {
        this.mPhoneNumber = mPhoneNumber;
    }

    protected boolean[] getmWeek() {
        return mWeek;
    }

    protected void setmWeak(boolean[] week) {
        this.mWeek = week;
    }

    protected Calendar getmTime() {
        return mTime;
    }

    protected void setmTime(Calendar mTime) {
        this.mTime = mTime;
    }

    protected String setTimeString() {
        int hourOfDay =mTime.get(Calendar.HOUR_OF_DAY);
        int minute = mTime.get(Calendar.MINUTE);
        String hour = "" + hourOfDay;
        String min = "" + minute;

        if (hourOfDay < 10)
            hour = "0" + hourOfDay;
        if (minute < 10)
            min = "0" + minute;

        return  hour + ":" + min;
    }


    protected LatLng getmPlace() {
        return mPlace;
    }

    protected void setmPlace(LatLng mPlace) {
        this.mPlace = mPlace;
    }

    protected int getmDistance() {
        return mDistance;
    }

    protected void setmDistance(int distance) {
        this.mDistance = distance;
    }

    protected void submit(){
        Intent sendSMS = new Intent(mContext, SendSMS.class);
        sendSMS.putExtra("number", mPhoneNumber);
        sendSMS.putExtra("message", mMessage);
        sendSMS.putExtra("week", mWeek);
        sendSMS.putExtra("place", mPlace);
        sendSMS.putExtra("distance",mDistance);
        mPendingIntent = PendingIntent.getBroadcast(mContext, 0, sendSMS, PendingIntent.FLAG_CANCEL_CURRENT);
        //setAlarm(pendingIntent);
        //Log.i(TAG, "Sending intent with number " + sendSMS.getStringExtra("number"));
        mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, mTime.getTimeInMillis(), AlarmManager.INTERVAL_DAY, mPendingIntent);
    }

    protected void cancelIntent(){
        mAlarmManager.cancel(mPendingIntent);
    }
}
