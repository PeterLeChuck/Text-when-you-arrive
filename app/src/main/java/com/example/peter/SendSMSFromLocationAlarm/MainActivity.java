package com.example.peter.SendSMSFromLocationAlarm;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.maps.model.LatLng;

import java.util.Calendar;

/*
 IZBOLJŠAVE:
 -ikona
 -barva pisave
 -kakšna tema
 */
public class MainActivity extends Activity {
    private static final int PICK_CONTACT = 0;
    private static final int SELECT_PLACE = 1;
    private final String TAG = "mauuuu";
    private static TextView timeView;
    private static TextView numberView;
    private EditText mDistance;
    private EditText mMessage;
    private TextView mWarrning;

    private CheckBox mMO;
    private CheckBox mTU;
    private CheckBox mWE;
    private CheckBox mTH;
    private CheckBox mFR;
    private CheckBox mSA;
    private CheckBox mSU;
    private boolean[] mWeek;
    private MainActivityAdapter mAdapter;
    private ListView mLinear;

    protected AlarmItem alarmItem;

    private static Calendar mCalendar;
    ContentResolver cr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_sms_from_location);

        alarmItem = new AlarmItem(getApplicationContext());
        mAdapter = new MainActivityAdapter(getApplicationContext());

        timeView = (TextView) findViewById(R.id.Time);
        numberView = (TextView) findViewById(R.id.Number);
        mWarrning = (TextView) findViewById(R.id.warrning);
        mDistance = (EditText) findViewById(R.id.Distance);
        mMessage = (EditText) findViewById(R.id.Message);
        mMO = (CheckBox) findViewById(R.id.Monday);
        mTU = (CheckBox) findViewById(R.id.Tuesday);
        mWE = (CheckBox) findViewById(R.id.Wednesday);
        mTH = (CheckBox) findViewById(R.id.Thursday);
        mFR = (CheckBox) findViewById(R.id.Friday);
        mSA = (CheckBox) findViewById(R.id.Saturday);
        mSU = (CheckBox) findViewById(R.id.Sunday);
        mLinear = (ListView) findViewById(R.id.linear);

        cr = getContentResolver();

        mLinear.setAdapter(mAdapter);

        final Button numberButton = (Button) findViewById(R.id.NumberButton);
        numberButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
                startActivityForResult(intent, PICK_CONTACT);
                Log.i(TAG,"01");
            }
        });


        final Button placePicker = (Button) findViewById(R.id.PlaceButton);
        placePicker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);

                Log.i(TAG,"-1");
                startActivityForResult(intent, SELECT_PLACE);
            }
        });


        final Button timePickerButton = (Button) findViewById(R.id.TimeButton);
        timePickerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });

    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK){
            if (requestCode == PICK_CONTACT) {

                Cursor cursor = cr.query(data.getData(), new String[]{
                        ContactsContract.CommonDataKinds.Phone.NUMBER,
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME}, null, null, null);

                if (cursor != null && cursor.moveToFirst()) {
                    alarmItem.setmPhoneNumber(cursor.getString(0));
                    alarmItem.setmPhoneName(cursor.getString(1));
                    numberView.setText(cursor.getString(1));
                    cursor.close();
                }
            }
            if (requestCode == SELECT_PLACE) {
                alarmItem.setmPlace((LatLng) data.getExtras().get("coordinates"));
            }
        }
    }


    private static void setTimeString(int hourOfDay, int minute, int mili) {
        String hour = "" + hourOfDay;
        String min = "" + minute;

        if (hourOfDay < 10)
            hour = "0" + hourOfDay;
        if (minute < 10)
            min = "0" + minute;

        timeView.setText( hour + ":" + min);

    }

    public static class TimePickerFragment extends DialogFragment implements
            TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return
            return new TimePickerDialog(getActivity(), this, hour, minute, true);
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            setTimeString(hourOfDay, minute, 0);
            mCalendar = Calendar.getInstance();
            mCalendar.setTimeInMillis(System.currentTimeMillis());
            mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            mCalendar.set(Calendar.MINUTE, minute);

        }
    }

    private void showTimePickerDialog() {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }


    public void reset(View view){
        alarmItem = new AlarmItem(getApplicationContext());
      /*point = null;
        phoneNumber = null;*/
        mCalendar = null;
        timeView.setText(null);
        mMessage.setText(null);
        mDistance.setText(null);
        numberView.setText(null);
        mMO.setChecked(true);
        mTU.setChecked(true);
        mWE.setChecked(true);
        mTH.setChecked(true);
        mFR.setChecked(true);
        mSA.setChecked(false);
        mSU.setChecked(false);
        mWarrning.setText(null);
    }

    public void submit(View view){
        mWeek = new boolean[]{mSU.isChecked(),mMO.isChecked(), mTU.isChecked(),mWE.isChecked(),mTH.isChecked(),mFR.isChecked(),mSA.isChecked()};
        alarmItem.setmWeak(mWeek);
        alarmItem.setmTime(mCalendar);
        Log.i(TAG,"in submit");
        if(numberView.getText() == null)        mWarrning.setText("Please select a contact to whom you wish to deliver a message!");
        else if(alarmItem.getmTime() == null)   mWarrning.setText("Choose a time when you would like to send a message.");
        else if(alarmItem.getmPlace() == null)  mWarrning.setText("Please select place from a map!");
        else if(mDistance.getText() == null)    mWarrning.setText("Please enter the distance!");
        else if(mMessage.getText() == null)     mWarrning.setText("Don't send an empty message. That's just rude.");
        else {
            Log.i(TAG,"in else submit");

            alarmItem.setmDistance(Integer.parseInt(mDistance.getText().toString()));
            alarmItem.setmMessage(mMessage.getText().toString());
            Log.i(TAG,"02");
            alarmItem.submit();
            Log.i(TAG,"03");
            mAdapter.add(alarmItem);
            Log.i(TAG,"04");
            mLinear.setAdapter(mAdapter);
            Log.i(TAG,"05");
            reset(view);
            Log.i(TAG,"06");
        }
    }
}
