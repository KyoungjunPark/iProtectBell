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

        // 임시로 등록해둔 정보 불러옴
        ConnectServer c = new ConnectServer();
        ArrayList<ArrayList<String>> logList = c.Get_Log();

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

            listView.setAdapter(adapter);

        }

    }
}
