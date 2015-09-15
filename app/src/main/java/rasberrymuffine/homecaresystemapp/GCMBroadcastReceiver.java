package rasberrymuffine.homecaresystemapp;

/**
 * Created by 원세 on 2015-09-15.
 */
import java.net.URLDecoder;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;


public class GCMBroadcastReceiver extends WakefulBroadcastReceiver {
    private static final String TAG = "GCMBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {		//상대방이 메시지 보낼때  intent의 부가적인 정보로 사용
        String action = intent.getAction();
        //Log.d(TAG, "action : " + action);

        if (action != null) {
            if (action.equals("com.google.android.c2dm.intent.RECEIVE")) { // 푸시 메시지 수신 시
                String rawData = intent.getStringExtra("data");		// 서버에서 보낸 data 라는 키의 value 값
                String data = "";
                try {
                    data = URLDecoder.decode(rawData, "UTF-8");
                } catch(Exception ex) {
                    ex.printStackTrace();
                }

                //Log.v(TAG,  "data : " + data);

                // 액티비티로 전달
                sendToActivity(context, data);

            } else {
                //Log.d(TAG, "Unknown action : " + action);
            }
        } else {
            //Log.d(TAG, "action is null.");
        }
    }

    private void sendToActivity(Context context, String data) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("data", data);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }
}
