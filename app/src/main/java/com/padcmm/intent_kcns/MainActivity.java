package com.padcmm.intent_kcns;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.CalendarContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import java.util.GregorianCalendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {




    static final int REQUEST_PHONE = 100;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int PICK_IMAGE_REQUEST = 1;
   /* static final int PROJECTION_ID_INDEX = 0;
    static final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
    static final int PROJECTION_DISPLAY_NAME_INDEX = 2;
    static final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this, this);
    }

    @OnClick(R.id.btn_call_ph)
    public void onTapCallPhone() {
        makeCall("09972250670");
    }

    @OnClick(R.id.btn_share)
    public void onTapShare() {
        sendViaShareIntent("Greetings from KCNS!");
    }

    @OnClick(R.id.btn_map)
    public void onTapNavigateToMap() {
        openLocationInMap("Yangon");
    }

    @OnClick(R.id.btn_send_email)
    public void onTapSendEmail() {
        sendEmail();
    }

    @OnClick(R.id.btn_take_camera_pic)
    public void onTapTakeCameraPic() {
        takePicture();
    }

    @OnClick(R.id.btn_select_picture)
    public void onTapSelectPictureFromDevice() {
        selectPictureFromDevice();
    }

    @OnClick(R.id.btn_save_event_calendar)
    public void onTapSaveEventInCalendar() {
        saveEventInCalendar();
    }

    private void makeCall(String numberToCall) {
        numberToCall = numberToCall.replaceAll(" ", "");
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + numberToCall));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    REQUEST_PHONE);
            return;
        }
        startActivity(intent);
    }

    private void sendViaShareIntent(String msg) {
        startActivity(Intent.createChooser(ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setText(msg)
                .getIntent(), "Share"));
    }

    private void openLocationInMap(String location) {
        String uriToOpen = "http://maps.google.com/maps?daddr=" + location;
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uriToOpen));
        startActivity(intent);
    }

    private void sendEmail() {
        Intent send = new Intent(Intent.ACTION_SENDTO);
        String uriText = "mailto:" + Uri.encode("k.channyeinsu@gmail.com") +
                "?subject=" + Uri.encode("my subject") +
                "&body=" + Uri.encode("This is kcns.......");
        Uri uri = Uri.parse(uriText);
        send.setData(uri);
        startActivity(Intent.createChooser(send, "Send mail..."));
    }

    private void takePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void selectPictureFromDevice(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }


  /*  public static final String[] EVENT_PROJECTION = new String[] {
            CalendarContract.Calendars._ID,                           // 0
            CalendarContract.Calendars.ACCOUNT_NAME,                  // 1
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,         // 2
            CalendarContract.Calendars.OWNER_ACCOUNT                  // 3
    };*/

    private void saveEventInCalendar(){
        Intent calIntent = new Intent(Intent.ACTION_INSERT);
        calIntent.setType("vnd.android.cursor.item/event");
        calIntent.putExtra(CalendarContract.Events.TITLE, "My Event");
        calIntent.putExtra(CalendarContract.Events.EVENT_LOCATION, "My House");
        calIntent.putExtra(CalendarContract.Events.DESCRIPTION, "Gathering");

        GregorianCalendar calDate = new GregorianCalendar(2017, 12, 01);
        calIntent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);
        calIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                calDate.getTimeInMillis());
        calIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
                calDate.getTimeInMillis());

        startActivity(calIntent);
    }


}
