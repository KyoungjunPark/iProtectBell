package rasberrymuffine.homecaresystemapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.MediaController;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import static android.widget.Toast.LENGTH_LONG;

public class MainActivity extends AppCompatActivity {

    /**
     * Created by 예림 on 2015-09-09.
     */
    public static final int REQUEST_CODE_LOG = 1001;
    public static final int REQUEST_CODE_SPEAK = 1002;
    public static final int REQUEST_CODE_SETTING = 1003;
    public static final int REQUEST_CODE_FULLSCREEN = 1004;
    public static final int RESULT_CODE1 = 1;
    public static final int RESULT_CODE2 = 2;

    WebView videoView;
    Button fullScreenButton;
    Button callButton;
    Button speakButton;
    Button logButton;
    Switch doorControlSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent fromLoginIntent = getIntent();

        videoView = (WebView)findViewById(R.id.videoView);
        videoView.getSettings().setJavaScriptEnabled(true);
        videoView.loadUrl("http://165.194.104.19:8080/stream");
        fullScreenButton = (Button)findViewById(R.id.fullScreenButton);
        fullScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFullScreen();
            }
        });
        callButton = (Button)findViewById(R.id.callButton);
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call();
            }
        });
        speakButton = (Button)findViewById(R.id.speakButton);
        speakButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speak();
            }
        });
        logButton = (Button)findViewById(R.id.logButton);
        logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogView();
            }
        });
        doorControlSwitch = (Switch)findViewById(R.id.openSwitch);
        doorControlSwitch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (doorControlSwitch.isChecked() == false) {
                        AlertDialog dialog = createDialogBox();
                        dialog.show();
                    }
                }
                return false;
            }
        });

    }

    private void showFullScreen() {
        Intent intent = new Intent(getApplicationContext(), FullscreenActivity.class);
        startActivityForResult(intent, REQUEST_CODE_FULLSCREEN);
    }

    private void call(){
        String num = "01093866983";                     // 사용자가 등록한 긴급전화번호를 사용해도 좋을듯
        try {
            Intent callIntent = new Intent(Intent.ACTION_CALL);     // ACTION_DIAL 쓰면 바로 안걸리고 다이얼창만 나타남
            callIntent.setData(Uri.parse("tel:" + num));
            startActivity(callIntent);
        }catch(ActivityNotFoundException e){
            Log.e("전화를 겁니다.", "전화를 걸 수 없습니다.", e);
        }
    }

    private AlertDialog createDialogBox(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Warning");
        builder.setMessage("도어락을 해제하시겠습니까?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int whichButton){
                //doorControlSwitch.toggle();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int whichButton){
                doorControlSwitch.toggle();
            }
        });

        AlertDialog dialog = builder.create();
        return dialog;

    }

    private void showLogView() {
        Intent intent = new Intent(getApplicationContext(), LogActivity.class);
        startActivityForResult(intent, REQUEST_CODE_LOG);
    }

    private void speak() {
        Intent intent = new Intent(getApplicationContext(), SpeakActivity.class);
        startActivityForResult(intent, REQUEST_CODE_SPEAK);
    }

    private void sentToServer(final String type) {
        ConnectServer.getInstance().setAsncTask(new AsyncTask<String, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(String... params) {

                URL obj = null;
                try {
                    obj = new URL("http://165.194.104.19:5000/login");  //추후 변경
                    HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                    con.setRequestProperty("Accept-Language", "ko-kr,ko;q=0.8,en-us;q=0.5,en;q=0.3");
                    con.setDoOutput(true);
                    String parameter = URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(type, "UTF-8");

                    OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
                    wr.write(parameter);
                    wr.flush();

                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {


            }
        });
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
            //intent.putExtra("noticeMethod",0);
            startActivityForResult(intent, REQUEST_CODE_SETTING);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestcode, int resultcode, Intent data) {
        super.onActivityResult(requestcode, resultcode , data);

        if(requestcode==REQUEST_CODE_SETTING){
            if(resultcode==RESULT_CODE1){

                Toast.makeText(getApplicationContext(),"popup이 선택됨",Toast.LENGTH_LONG).show();
            }
            else if(resultcode==RESULT_CODE2){
                Toast.makeText(getApplicationContext(),"execution이 선택됨",Toast.LENGTH_LONG).show();

            }

            else{

            }
        }
    }

}
