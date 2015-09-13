package rasberrymuffine.homecaresystemapp;

import android.content.ClipData;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class LoginActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_MAIN = 1001;
    public static final int REQUEST_CODE_JOIN = 1002;

    public static final int LOGIN_PERMITTED = 200;
    public static final int LOGIN_DENIED = 201;

    private EditText idEdit;
    private EditText pwEdit;
    private String userInputID;
    private String userInputPW;

    private Button loginButton;
    private Button joinButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        idEdit = (EditText)findViewById(R.id.idEdit);
        pwEdit = (EditText)findViewById(R.id.pwEdit);

        loginButton = (Button)findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userInputID = idEdit.getText().toString();
                userInputPW = pwEdit.getText().toString();

                Log.d("result", userInputID + " / " + userInputPW);

                ConnectServer a = new ConnectServer();
                a.Send_Login_Info(idEdit.getText().toString(), pwEdit.getText().toString());

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("serialNum", "서버한테 정보 받아오기");
                    startActivityForResult(intent, REQUEST_CODE_MAIN);
                    finish();
            }
        });
        joinButton = (Button)findViewById(R.id.joinButton);
        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), JoinActivity.class);

                startActivity(intent);
            }
        });
   //     ConnectServer.Get_Log();

    }
  

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
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
