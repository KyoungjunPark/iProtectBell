package rasberrymuffine.homecaresystemapp;

import android.app.Activity;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by 예림 on 2015-09-13.
 */
public class LogActivity extends Activity {
    ListView listView;
    LogListAdapter adapter;
    Resources res;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        listView = (ListView) findViewById(R.id.logListView);
        adapter = new LogListAdapter(this);

        res = getResources();

        /*
         * server에서 log를 불러와야해
         *
         * 불러올 예정이다!
         *
         */

        // 임시로 등록해둔 정보 불러옴
        ConnectServer c = new ConnectServer(new AsyncTask<String, Void, Boolean>() {

            private ArrayList<ArrayList<String>> logList;

            @Override
            protected Boolean doInBackground(String... params) {
                try {
                    URL url = new URL("http://165.194.104.19:5000/log");
                    BufferedReader rd = new BufferedReader(new InputStreamReader(url.openStream()));

                    String line;
                    String log = "";
                    while ((line = rd.readLine()) != null) {
                        log+=line;
                    }
                    logList = ConnectServer.jsonParse(log);
                    Log.d("server", String.valueOf(logList.size()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            }
            @Override
            protected void onPostExecute(Boolean aBoolean) {
                Log.d("server",String.valueOf(logList.size()));
                for (int i = 0; i < logList.size(); i++) {
                    // index 1번이 infomation
                    switch (logList.get(i).get(1)) {
                        case "신고":
                            adapter.addItem(new LogItem(res.getDrawable(R.drawable.siren), logList.get(i).get(0), logList.get(i).get(1), logList.get(i).get(2)));
                            break;
                        case "종료":
                            adapter.addItem(new LogItem(res.getDrawable(R.drawable.logoff), logList.get(i).get(0), logList.get(i).get(1), logList.get(i).get(2)));
                            break;
                    }
                }
                listView.setAdapter(adapter);
            }
        });

        c.Get_Log();
    }
}
