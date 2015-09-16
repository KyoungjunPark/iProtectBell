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
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
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

        ConnectServer.getInstance().setAsncTask(new AsyncTask<String, Void, Boolean>() {

            private ArrayList<ArrayList<String>> logList;

            @Override
            protected Boolean doInBackground(String... params) {
                try {
                    URL url = new URL("http://165.194.104.19:5000/log");

                    HttpURLConnection con = (HttpURLConnection) url.openConnection();

                    con = ConnectServer.getInstance().setHeader(con);
                    con.setDoOutput(true);

                    BufferedReader rd = new BufferedReader(new InputStreamReader(url.openStream()));

                    String line;
                    String log = "";
                    while ((line = rd.readLine()) != null) {
                        log += line;
                    }
                    logList = jsonParse(log);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
            if(logList!=null) {
                for (int i = 0; i < logList.size(); i++) {
                    // index 1번이 type
                    switch (logList.get(i).get(1)) {
                        case "call": // 신고
                            adapter.addItem(new LogItem(res.getDrawable(R.drawable.siren), logList.get(i).get(0), logList.get(i).get(2), logList.get(i).get(3)));
                            break;
                        case "off": // 종료
                            adapter.addItem(new LogItem(res.getDrawable(R.drawable.logoff), logList.get(i).get(0), logList.get(i).get(2), logList.get(i).get(3)));
                            break;
                        case "speak":
                            adapter.addItem(new LogItem(res.getDrawable(R.drawable.white_speaker), logList.get(i).get(0), logList.get(i).get(2), logList.get(i).get(3)));
                            break;
                        case "open":
                            adapter.addItem(new LogItem(res.getDrawable(R.drawable.opened), logList.get(i).get(0), logList.get(i).get(2), logList.get(i).get(3)));
                            break;
                        case "close":
                            adapter.addItem(new LogItem(res.getDrawable(R.drawable.closed), logList.get(i).get(0), logList.get(i).get(2), logList.get(i).get(3)));
                            break;
                        case "login":
                            adapter.addItem(new LogItem(res.getDrawable(R.drawable.login), logList.get(i).get(0), logList.get(i).get(2), logList.get(i).get(3)));
                            break;
                        case "logoff":
                            adapter.addItem(new LogItem(res.getDrawable(R.drawable.logoff), logList.get(i).get(0), logList.get(i).get(2), logList.get(i).get(3)));
                            break;
                        case "log":
                            adapter.addItem(new LogItem(res.getDrawable(R.drawable.white_log), logList.get(i).get(0), logList.get(i).get(2), logList.get(i).get(3)));
                            break;
                        default:
                            break;
                    }
                }
            }
                listView.setAdapter(adapter);
            }
        });
        ConnectServer.getInstance().execute();
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
