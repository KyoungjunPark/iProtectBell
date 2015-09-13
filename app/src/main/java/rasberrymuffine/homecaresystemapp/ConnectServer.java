package rasberrymuffine.homecaresystemapp;

import android.os.AsyncTask;
import android.util.Log;

import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by USER on 2015-09-13.
 */
public class ConnectServer {
    public static void Get_Log() {
        CommunicationTask task = new CommunicationTask();
        task.execute("log");
    }

    private static class CommunicationTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {

            if(params[0] == "log") {
                try {
                    URL url = new URL("http://165.194.104.19:5000/log");
                    BufferedReader rd = new BufferedReader(new InputStreamReader(url.openStream()));

                    String line;
                    while ((line = rd.readLine()) != null) {
                        Log.d("hello", line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return true;
        }
    }
}
