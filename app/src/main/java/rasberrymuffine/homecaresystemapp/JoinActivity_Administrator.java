package rasberrymuffine.homecaresystemapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class JoinActivity_Administrator extends AppCompatActivity {

    Button join_button;
    EditText userID;
    EditText userPW;
    EditText userSerialNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_administrator);

        join_button = (Button)findViewById(R.id.join_button);
        join_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userID = (EditText)findViewById(R.id.editID);
                userPW = (EditText)findViewById(R.id.editPW);
                userSerialNum = (EditText)findViewById(R.id.editSerialNumber);

                ConnectServer.Send_Join_Info(userID.getText().toString(), userPW.getText().toString(), userSerialNum.getText().toString());

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                String resultCode = ConnectServer.getPermission();

                   AlertDialog dialog = createDialogBox(resultCode);
                   dialog.show();

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

        if(msg=="200") {
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
        }else{  builder.setTitle("회원가입 실패");

            // 에러 메시지 전송
            builder.setMessage("비밀번호는 알파벳, 숫자, 특수문자를 모두 포함한 20자리 이상이어야 합니다. \n\n");
            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                }
            });}

        AlertDialog dialog = builder.create();
        return dialog;

    }
}

