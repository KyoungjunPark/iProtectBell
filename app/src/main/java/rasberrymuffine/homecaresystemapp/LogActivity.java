package rasberrymuffine.homecaresystemapp;

import android.app.Activity;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

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
                    logList = jsonParse(log);
                    Log.d("server", String.valueOf(logList.size()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            }
            @Override
            protected void onPostExecute(Boolean aBoolean) {
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
                        case "login":
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
                }
                listView.setAdapter(adapter);
            }
        });
        c.Get_Log();
    }
    public static ArrayList<ArrayList<String>> jsonParse(String log){
        ArrayList<ArrayList<String>> logList = new ArrayList<ArrayList<String>>();

        ArrayList<String> jsonKey = new ArrayList<String>();
        jsonKey.add("date");
        jsonKey.add("type");
        jsonKey.add("information");
        jsonKey.add("importance");


        ArrayList<String> oneLog;

        try {
            JSONArray jsonArray = new JSONArray(log);
            org.json.JSONObject json = null;

            for (int i = 0; i < jsonArray.length(); i++) {
                json = jsonArray.getJSONObject(i);
                Log.d("-------", json.toString());
                oneLog = new ArrayList<String>();
                for (int j = 0; j < jsonKey.size(); j++) {
                    oneLog.add(json.getString(jsonKey.get(j)));
                    Log.d("result", json.getString(jsonKey.get(j)));
                }
                logList.add(oneLog);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return logList;
    }
}
