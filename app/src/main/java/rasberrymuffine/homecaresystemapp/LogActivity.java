package rasberrymuffine.homecaresystemapp;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

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
         */

        LogItem a = new LogItem(res.getDrawable(R.drawable.siren), "2015-09-13 16:26", "신고", "MAJOR");
        adapter.addItem(a);
        Log.d("LogItem", a.getData()[0] + a.getData()[1] + a.getData()[2]);
        adapter.addItem(new LogItem(res.getDrawable(R.drawable.logoff), "2015-09-13 16:52", "종료", "MINOR"));

        listView.setAdapter(adapter);

    }

}
