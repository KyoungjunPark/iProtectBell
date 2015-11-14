package rasberrymuffine.homecaresystemapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class SettingActivity  extends AppCompatActivity {

    public static final int RESULT_CODE1 = 1;
    public static final int RESULT_CODE2 = 2;

    EditText inputPhoneNumber;
    TextView serialNumber;

    Button settingSaveButton;
    Button developerInfoButton;

    RadioButton popup_button;
    RadioButton execution_button;

    SeekBar volumeBar;

    int volume;                     // user가 설정한 볼륨의 크기
    String phoneNumber;             // user가 설정한 긴급전화번호
    String noticeMean;              // user가 설정한 알림 방식

    private SQLiteDatabase db;
    File settingRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Intent fromMainIntent = getIntent();

        inputPhoneNumber = (EditText)findViewById(R.id.inputPhoneNumber);
        phoneNumber=UserSettingInfo.getInstance().getPhoneNumber();
        inputPhoneNumber.setText(phoneNumber);


        serialNumber = (TextView)findViewById(R.id.userSerialNumber);
        serialNumber.setText(UserSettingInfo.getInstance().getSerialNumber());

        popup_button = (RadioButton)findViewById(R.id.popupRadioButton);
        popup_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //UserSettingInfo INFO = new UserSettingInfo();
                //INFO.setAlarmType("pushalarm");
                UserSettingInfo.getInstance().setAlarmType("0");
            }
        });

        execution_button = (RadioButton)findViewById(R.id.executionRadioButton);
        execution_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //UserSettingInfo INFO = new UserSettingInfo();
                //INFO.setAlarmType("execution");
                UserSettingInfo.getInstance().setAlarmType("1");
            }
        });

        developerInfoButton = (Button)findViewById(R.id.developerInfo);
        developerInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent infoIntent = new Intent(getApplicationContext(), developInfo.class);
                startActivity(infoIntent);

            }
        });
/*
        volumeBar = (SeekBar)findViewById(R.id.VolumeControlBar);
        volumeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                volume = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
*/
        settingSaveButton = (Button)findViewById(R.id.saveButton);
        settingSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumber = inputPhoneNumber.getText().toString();
                UserSettingInfo.getInstance().setPhoneNumber(phoneNumber);
                db = openOrCreateDatabase("bellSetting", MODE_WORLD_READABLE, null);
                Cursor c1 = db.rawQuery("select * from setting where ID='" + UserSettingInfo.getInstance().getID() + "'", null);
                if(c1.getCount() == 1) {
                    c1.moveToNext();
                    db.execSQL("update setting set serial_number=" + "'" + UserSettingInfo.getInstance().getSerialNumber() + "', "
                            + "phone_number='" + UserSettingInfo.getInstance().getPhoneNumber() + "', "
                            + "alarm_type='" + UserSettingInfo.getInstance().getAlarmType() + "'");
                    c1.close();
                }
                AlertDialog dialog = createDialogBox(phoneNumber +" // "+ noticeMean +" // "+ volume);
                dialog.show();
            }
        });

    }

    private AlertDialog createDialogBox(String phoneNumber) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("확인");
        builder.setMessage("신고 시 "+phoneNumber+" 으로 연결됩니다.");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });
        AlertDialog dialog = builder.create();
        return dialog;

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
