package rasberrymuffine.homecaresystemapp;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {

    /**
     * Created by 예림 on 2015-09-09.
     */
    public static final int REQUEST_CODE_SETTING = 1003;

    VideoView videoView;
    Button callButton;
    Button speakButton;
    Button logButton;
    Button settingButton;
    Switch doorControlSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent fromLoginIntent = getIntent();
        String serialNum = fromLoginIntent.getStringExtra("serialNum");

        Toast.makeText(this,serialNum+"" , Toast.LENGTH_LONG).show();

        videoView = (VideoView)findViewById(R.id.videoView);
        callButton = (Button)findViewById(R.id.callButton);
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call();
            }
        });
        speakButton = (Button)findViewById(R.id.speakButton);
        logButton = (Button)findViewById(R.id.logButton);
        // settingButton = (Button)findViewById(R.id.settingButton);
        doorControlSwitch = (Switch)findViewById(R.id.openSwitch);


    }
    private void call(){
        String num = "01093866983";                     // 사용자가 등록한 긴급전화번호를 사용해도 좋을듯
        try {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);     // ACTION CALL 쓰면 곧장 걸린다고 함 _ 나는 통신이 안돼서 확인 불가 ㅜㅜ
            callIntent.setData(Uri.parse("tel:" + num));
            startActivity(callIntent);
        }catch(ActivityNotFoundException e){
            Log.e("전화를 겁니다.", "전화를 걸 수 없습니다.", e);
        }
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
            Intent intent = new Intent(getApplicationContext(),SettingActivity.class);
            startActivityForResult(intent, REQUEST_CODE_SETTING);

            return true;
        }


        return super.onOptionsItemSelected(item);
    }
}

