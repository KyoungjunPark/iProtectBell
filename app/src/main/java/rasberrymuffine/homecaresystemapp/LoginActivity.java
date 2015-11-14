package rasberrymuffine.homecaresystemapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class LoginActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_MAIN = 1001;
    public static final int REQUEST_CODE_JOIN = 1002;

    public static final int LOGIN_PERMITTED = 200;
    public static final int LOGIN_DENIED = 404;

    private EditText idEdit;
    private EditText pwEdit;
    private String userInputID;
    private String userInputPW;
    private String userSerialNumber;

    private Button loginButton;
    private Button joinButton;

    private String isLoginPermitted;

    private UserSettingInfo userSettingInfo;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        idEdit = (EditText) findViewById(R.id.idEdit);
        pwEdit = (EditText) findViewById(R.id.pwEdit);

        loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userInputID = idEdit.getText().toString();
                userInputPW = pwEdit.getText().toString();



/*
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivityForResult(intent, REQUEST_CODE_MAIN);
                finish();
*/


                ConnectServer.getInstance().setAsncTask(new AsyncTask<String, Void, Boolean>() {

                    @Override
                    protected Boolean doInBackground(String... params) {

                        URL obj = null;
                        try {
                            obj = new URL("http://165.194.17.4:5000/login");
                            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                            //implement below code if token is send to server
                            con = ConnectServer.getInstance().setHeader(con);

                            con.setDoOutput(true);

                            String parameter = URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(userInputID, "UTF-8");
                            parameter += "&" + URLEncoder.encode("user_password", "UTF-8") + "=" + URLEncoder.encode(userInputPW, "UTF-8");

                            OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
                            wr.write(parameter);
                            wr.flush();

                            BufferedReader rd = null;

                            if (con.getResponseCode() == LOGIN_PERMITTED) {
                                // 로그인 성공

                                rd = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
                                String token = rd.readLine();
                                ConnectServer.getInstance().setToken(token);


                                isLoginPermitted = LOGIN_PERMITTED + "";
                                userSerialNumber = "RA-SP-BERRY-VERY-GOOD";
                                Log.d("---- success ----", token);

                                createDatabase();
                                setSettings();

                                UserSettingInfo.getInstance().setAsncTask(new AsyncTask<String, Void, Boolean>() {
                                    @Override
                                    protected Boolean doInBackground(String... params) {
                                        return null;
                                    }

                                    @Override
                                    protected void onPostExecute(Boolean aBoolean) {

                                        /** 서버통신, file I/O 대신 임시로 넣어줌 ㅎㅎㅎ 내일 짜죠 */
                                        //UserSettingInfo.getInstance().setPhoneNumber("01093866983");
                                        //UserSettingInfo.getInstance().setSerialNumber("RA-SP-BERRY-VERY-GOOD");
                                    }
                                });
                                UserSettingInfo.getInstance().execute();

                            } else {
                                // 로그인 실패
                                rd = new BufferedReader(new InputStreamReader(con.getErrorStream(), "UTF-8"));
                                isLoginPermitted = rd.readLine();
                                Log.d("---- failed ----", String.valueOf(rd.readLine()));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Boolean aBoolean) {
                        if (isLoginPermitted == LOGIN_PERMITTED + "") {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivityForResult(intent, REQUEST_CODE_MAIN);
                            finish();
                        } else {
                            AlertDialog dialog = createDialogBox(isLoginPermitted);
                            dialog.show();
                            idEdit.setText("");
                            pwEdit.setText("");
                        }

                    }
                });

                ConnectServer.getInstance().execute();


            }
        });
        joinButton = (Button) findViewById(R.id.joinButton);
        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), JoinActivity.class);

                startActivity(intent);
            }
        });


    }

    private void createDatabase() {
        try {
            db = openOrCreateDatabase("bellSetting", MODE_WORLD_READABLE, null);
            createSettingTable();
            //databaseCreated = true;
            Log.d("database", "-----created-----");
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.e("database", "-----not created-----");
        }
    }

    private void createSettingTable() {
        db.execSQL("create table setting ("
                + "ID text, "
                + "serial_number text, "
                + "phone_number text, "
                + "alarm_type text);");
    }

    private void setSettings() {
        Cursor c1 = db.rawQuery("select * from setting where ID='" + userInputID + "'", null);
        if(c1.getCount() == 0) {
            UserSettingInfo.getInstance().setID(userInputID);
            UserSettingInfo.getInstance().setSerialNumber(userSerialNumber);
            UserSettingInfo.getInstance().setPhoneNumber("01093866983");
            UserSettingInfo.getInstance().setAlarmType("0");
            db.execSQL("insert into setting (ID, serial_number, phone_number, alarm_type) values ("
                    + "'" + UserSettingInfo.getInstance().getID() + "', '"
                    + UserSettingInfo.getInstance().getSerialNumber() + "', '"
                    + UserSettingInfo.getInstance().getPhoneNumber() + "', '"
                    + UserSettingInfo.getInstance().getAlarmType() + "');");

        }
        else {
            c1.moveToNext();
            UserSettingInfo.getInstance().setID(c1.getString(0));
            UserSettingInfo.getInstance().setSerialNumber(c1.getString(1));
            UserSettingInfo.getInstance().setPhoneNumber(c1.getString(2));
            UserSettingInfo.getInstance().setAlarmType(c1.getString(3));
        }
        c1.close();
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

    private AlertDialog createDialogBox(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("로그인 실패");

        builder.setMessage("아이디와 비밀번호를 확인해주세요. \n" + msg + "\n");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });

        AlertDialog dialog = builder.create();
        return dialog;

    }

}
