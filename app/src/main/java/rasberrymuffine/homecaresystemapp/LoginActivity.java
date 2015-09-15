package rasberrymuffine.homecaresystemapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
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

    private Button loginButton;
    private Button joinButton;

    private String isLoginPermitted;

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

                ConnectServer.getInstance().setAsncTask(new AsyncTask<String, Void, Boolean>() {

                    @Override
                    protected Boolean doInBackground(String... params) {

                        URL obj = null;
                        try {
                            obj = new URL("http://165.194.104.19:5000/login");
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

                                isLoginPermitted = LOGIN_PERMITTED+"";
                                Log.d("---- success ----", String.valueOf(rd.readLine()));
                            } else {
                                // 로그인 실패
                                rd = new BufferedReader(new InputStreamReader(con.getErrorStream(), "UTF-8"));
                                isLoginPermitted= rd.readLine();
                                Log.d("---- failed ----", String.valueOf(rd.readLine()));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Boolean aBoolean) {
                        if (isLoginPermitted==LOGIN_PERMITTED+"") {

                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivityForResult(intent, REQUEST_CODE_MAIN);
                            finish();
                        }
                        else{
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
        joinButton = (Button)findViewById(R.id.joinButton);
        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), JoinActivity.class);

                startActivity(intent);
            }
        });

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
