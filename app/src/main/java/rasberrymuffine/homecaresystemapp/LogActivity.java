package rasberrymuffine.homecaresystemapp;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by 예림 on 2015-09-13.
 */
public class LogActivity extends Activity {
    ListView listView;
    LogListAdapter adapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        listView = (ListView) findViewById(R.id.logListView);
        adapter = new LogListAdapter(this);

        Resources res = getResources();

        /*
         * server에서 log를 불러와야해
         *
         * 불러올 예정이다!
         *
         */
        ConnectServer c = new ConnectServer();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ArrayList<ArrayList<String>> logList = c.Get_Log();

        for(int i=0; i<logList.size(); i++){
            for(int j=0; j<3; j++){

                Log.d("check", logList.get(i).get(j).toString());
            }

        }
        for (int i = 0; i < logList.size(); i++) {
            // index 1번이 infomation
            switch (logList.get(i).get(1)) {
                case "신고":
                    adapter.addItem(new LogItem(res.getDrawable(R.drawable.siren), logList.get(i).get(0), logList.get(i).get(1), logList.get(i).get(2)));
                    break;
                case "종료":
                    adapter.addItem(new LogItem(res.getDrawable(R.drawable.logoff), logList.get(i).get(0), logList.get(i).get(1), logList.get(i).get(2)));
                    break;
                case "음성":
                    adapter.addItem(new LogItem(res.getDrawable(R.drawable.white_speaker), logList.get(i).get(0), logList.get(i).get(1), logList.get(i).get(2)));
                    break;
                case "열림":
                    adapter.addItem(new LogItem(res.getDrawable(R.drawable.opened), logList.get(i).get(0), logList.get(i).get(1), logList.get(i).get(2)));
                    break;
                case "닫힘":
                    adapter.addItem(new LogItem(res.getDrawable(R.drawable.closed), logList.get(i).get(0), logList.get(i).get(1), logList.get(i).get(2)));
                    break;
                case "로그인":
                    adapter.addItem(new LogItem(res.getDrawable(R.drawable.login), logList.get(i).get(0), logList.get(i).get(1), logList.get(i).get(2)));
                    break;
                case "로그오프":
                    adapter.addItem(new LogItem(res.getDrawable(R.drawable.logoff), logList.get(i).get(0), logList.get(i).get(1), logList.get(i).get(2)));
                    break;
                case "로그":
                    adapter.addItem(new LogItem(res.getDrawable(R.drawable.white_log), logList.get(i).get(0), logList.get(i).get(1), logList.get(i).get(2)));
                    break;
                default:
                    break;
            }

            listView.setAdapter(adapter);

        }

    }
}
