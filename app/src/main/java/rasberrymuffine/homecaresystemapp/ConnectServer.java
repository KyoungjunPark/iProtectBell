package rasberrymuffine.homecaresystemapp;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;

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
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


/**
 * Created by 경준 on 2015-09-13.
 */
public class ConnectServer {
    public static void Send_Login_Info(String id, String password){
        new CommunicationTask().execute("sendLoginInfo", id, password);
    }

    public static void Get_Log() {
        new CommunicationTask().execute("log");
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
            } else if(params[0] == "sendLoginInfo"){
                try {
                    URL obj = new URL("http://165.194.104.19:5000/login");
                    HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                    //add reuqest header
                    con.setRequestMethod("POST");
                    con.setRequestProperty("user_id", params[1]);
                    con.setRequestProperty("user_password", params[2]);
                    con.setDoOutput(true);
                    DataOutputStream wr = new DataOutputStream(con.getOutputStream());

                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(con.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                    Log.d("server",response.toString());

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return true;
        }
    }
}
