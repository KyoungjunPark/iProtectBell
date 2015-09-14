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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;


/**
 * Created by 경준 on 2015-09-13.
 */
public class ConnectServer {
    private AsyncTask<String, Void, Boolean> task;
    private static String userID;
    private static String userPW;
    private static String resultCode;
    private static ArrayList<ArrayList<String>> logList;

    //not use!
    public ConnectServer(){
       task = new CommunicationTask();
    }
    public ConnectServer(AsyncTask<String, Void, Boolean> task){
        this.task = task;
    }

    public void Send_Login_Info(String id, String password){
        this.task.execute("sendLoginInfo", id, password);
    }

    public static String getPermission(){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

      //  return "200";
       return resultCode;
    }

    public static void Send_Join_Info(String id, String password, String serialNum){

        new CommunicationTask().execute("join", id, password, serialNum);

    }
    public boolean isFinished(){
        if(task.getStatus() == AsyncTask.Status.FINISHED) return false;
        else return true;
    }

    public void Get_Log() {
        this.task.execute("log");
    }

    private static class CommunicationTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {

            if(params[0].equals("log")) {

            }else if(params[0].equals("join")){
                URL obj = null;
                try {
                    obj = new URL("http://165.194.104.19:5000/join");

                    HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                    con.setRequestProperty("Accept-Language", "ko-kr,ko;q=0.8,en-us;q=0.5,en;q=0.3");
                    con.setDoOutput(true);
                    String parameter = URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(params[1], "UTF-8");
                    parameter += "&" + URLEncoder.encode("user_password", "UTF-8") + "=" + URLEncoder.encode(params[2], "UTF-8");
                    parameter += "&" + URLEncoder.encode("serial_number", "UTF-8") + "=" + URLEncoder.encode(params[3], "UTF-8");

                    OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
                    wr.write(parameter);
                    wr.flush();
                    BufferedReader rd = null;
                    if (con.getResponseCode() == 200) {
                        // 회원가입 성공
                        resultCode = 200+"";
                    } else {
                        // 회원가입 실패
                        rd = new BufferedReader(new InputStreamReader(con.getErrorStream(), "UTF-8"));
                        resultCode= rd.readLine();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else if (params[0] == "sendLoginInfo") {

                    URL obj = null;
                    try {
                        obj = new URL("http://165.194.104.19:5000/login");
                        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                        con.setRequestProperty("Accept-Language", "ko-kr,ko;q=0.8,en-us;q=0.5,en;q=0.3");
                        con.setDoOutput(true);
                        String parameter = URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(params[1], "UTF-8");

                        parameter += "&" + URLEncoder.encode("user_password", "UTF-8") + "=" + URLEncoder.encode(params[2], "UTF-8");

                        OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
                        wr.write(parameter);
                        wr.flush();
                        BufferedReader rd = null;
                        if (con.getResponseCode() == 200) {
                            // 로그인 성공
                            resultCode = 200+"";
                        } else {
                            // 로그인 실패
                            // rd.readLine() means fail reason
                            // so you must toast this message to user

                            rd = new BufferedReader(new InputStreamReader(con.getErrorStream(), "UTF-8"));
                            resultCode= rd.readLine();
                            Log.d("server", String.valueOf(rd.readLine()));
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            return true;
        }
    }


}
