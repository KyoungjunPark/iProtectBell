package rasberrymuffine.homecaresystemapp;

/**
 * Created by 원세 on 2015-09-15.
 */
import java.net.URLDecoder;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
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
                String title = "iProtectBell";
                try {
                    data = URLDecoder.decode(rawData, "UTF-8");
                } catch(Exception ex) {
                    ex.printStackTrace();
                }

                //Log.v(TAG,  "data : " + data);
                //////////////////////////////////////////////////////////////////////////
                Vibrator vibrator =
                        (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(1000);
                setNotification(context, title, data);
                //////////////////////////////////////////////////////////////////////////
                // 액티비티로 전달
                sendToActivity(context, data);

            } else {
                //Log.d(TAG, "Unknown action : " + action);
            }
        } else {
            //Log.d(TAG, "action is null.");
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////
    private void setNotification(Context context, String title, String message) {
        NotificationManager notificationManager = null;
        Notification notification = null;
        try {
            notificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            notification = new Notification(R.drawable.siren,
                    message, System.currentTimeMillis());
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pi = PendingIntent.getActivity(context, 0, intent, 0);
            notification.setLatestEventInfo(context, title, message, pi);
            notificationManager.notify(0, notification);
        } catch (Exception e) {

        }
    }
    /////////////////////////////////////////////////////////////////////////////////////////
    private void sendToActivity(Context context, String data) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("data", data);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }
}
/*
    @Override
    protected void onMessage(Context context, Intent intent) {
        // 메세지를 수신했을 때 동작 설명
        if (BuildConfig.DEBUG)
            Log.d(LOG_TAG, "GCMReceiver Message");
        try {
            String title = intent.getStringExtra(PUSH_DATA_TITLE);
            String message = intent.getStringExtra(PUSH_DATA_MESSAGE);
            Vibrator vibrator =
                    (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(1000);
            setNotification(context, title, message);
            if (BuildConfig.DEBUG)
                Log.d(LOG_TAG, title + ":" + message);
        } catch (Exception e) {
            Log.e(LOG_TAG, "[onMessage] Exception : " + e.getMessage());
        }
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        if (BuildConfig.DEBUG)
            Log.d(LOG_TAG, "onRecoverableError: " + errorId);
        return super.onRecoverableError(context, errorId);
    }

    private void setNotification(Context context, String title, String message) {
        NotificationManager notificationManager = null;
        Notification notification = null;
        try {
            notificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            notification = new Notification(R.drawable.ic_launcher,
                    message, System.currentTimeMillis());
            Intent intent = new Intent(context, NotificationActivity.class);
            PendingIntent pi = PendingIntent.getActivity(context, 0, intent, 0);
            notification.setLatestEventInfo(context, title, message, pi);
            notificationManager.notify(0, notification);
        } catch (Exception e) {
            Log.e(LOG_TAG, "[setNotification] Exception : " + e.getMessage());
        }
    }*/