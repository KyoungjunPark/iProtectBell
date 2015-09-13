package rasberrymuffine.homecaresystemapp;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by 경준 on 2015-09-13.
 */
public class ConnectServer {
    private static CommunicationTask task;
    private static String userID;
    private static String userPW;
    private static String resultCode;

    ConnectServer(){
       task = new CommunicationTask();
    }

    public static void Send_Login_Info(String id, String password){
        userID = id;
        userPW = password;
        task.execute("sendLoginInfo");
    }


    public static void Get_Log() {
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
            if(params[0] == "sendLoginInfo"){
                try {
                    URL url = new URL("http://165.194.104.19:5000/login");
                    HttpClient client = new DefaultHttpClient();
                    HttpConnectionParams.setConnectionTimeout(client.getParams(), 30000);
                    HttpPost post = new HttpPost(url.toString());
                    JSONObject userInfo = new JSONObject();

                    userInfo.put("user_id", userID);
                    userInfo.put("user_password", userPW);

                    try {
                        StringEntity entity = new StringEntity(userInfo.toString(), HTTP.UTF_8);
                        entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

                        post.setEntity(entity);
                        try {
                            HttpResponse httpResponse = client.execute(post);

                            try {
                                Thread.sleep(3000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            resultCode = httpResponse.getEntity().toString();

                            Log.d("----result---", resultCode);


                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
            return true;
        }
    }
}
