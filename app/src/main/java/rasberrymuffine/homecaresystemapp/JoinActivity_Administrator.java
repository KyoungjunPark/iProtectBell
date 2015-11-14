package rasberrymuffine.homecaresystemapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class JoinActivity_Administrator extends AppCompatActivity {

    public static final int JOIN_PERMITTED = 200;

    Button join_button;

    EditText userID;
    EditText userPW;
    EditText userSerialNum;

    String userInputID;
    String userInputPW;
    String serialNum;

    String isJoinPermitted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_administrator);

        join_button = (Button) findViewById(R.id.join_button);
        join_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userID = (EditText) findViewById(R.id.editID);
                userPW = (EditText) findViewById(R.id.editPW);
                userSerialNum = (EditText) findViewById(R.id.editSerialNumber);

                userInputID = userID.getText().toString();
                userInputPW = userPW.getText().toString();
                serialNum = userSerialNum.getText().toString();

                ConnectServer.getInstance().setAsncTask(new AsyncTask<String, Void, Boolean>() {
                    @Override
                    protected Boolean doInBackground(String... params) {

                        URL obj = null;
                        try {
                            obj = new URL("http://165.194.17.4:5000/join");

                            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                            con.setRequestProperty("Accept-Language", "ko-kr,ko;q=0.8,en-us;q=0.5,en;q=0.3");
                            con.setDoOutput(true);

                            String parameter = URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(userInputID, "UTF-8");
                            parameter += "&" + URLEncoder.encode("user_password", "UTF-8") + "=" + URLEncoder.encode(userInputPW, "UTF-8");
                            parameter += "&" + URLEncoder.encode("serialNum", "UTF-8") + "=" + URLEncoder.encode(serialNum, "UTF-8");

                            OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
                            wr.write(parameter);
                            wr.flush();
                            BufferedReader rd = null;

                            if (con.getResponseCode() == JOIN_PERMITTED) {
                                // 회원가입 성공
                                isJoinPermitted = JOIN_PERMITTED + "";
                            } else {
                                // 회원가입 실패
                                rd = new BufferedReader(new InputStreamReader(con.getErrorStream(), "UTF-8"));

                                isJoinPermitted = rd.readLine();
                                Log.d("----- server -----", String.valueOf(rd.readLine()));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                    @Override
                    protected void onPostExecute(Boolean aBoolean) {
                        AlertDialog dialog = createDialogBox(isJoinPermitted);
                        dialog.show();
                    }
                });
                ConnectServer.getInstance().execute();

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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private AlertDialog createDialogBox(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if (msg == JOIN_PERMITTED+"") {
            builder.setTitle("회원가입 성공");

            builder.setMessage("환영합니다! \n서비스를 이용하려면 로그인해주세요. \n\n");
            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);

                    // 플래그 변경했음.
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

                    startActivity(intent);
                }
            });
        } else {
            builder.setTitle("회원가입 실패");

            // 에러 메시지 전송
            builder.setMessage(msg+"\n");
            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                }
            });
        }

        AlertDialog dialog = builder.create();
        return dialog;

    }
}

