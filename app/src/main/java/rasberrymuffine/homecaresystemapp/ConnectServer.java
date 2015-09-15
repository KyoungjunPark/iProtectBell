package rasberrymuffine.homecaresystemapp;

import android.os.AsyncTask;


/**
 * Created by 경준 on 2015-09-13.
 */
public class ConnectServer {
    private AsyncTask<String, Void, Boolean> task;
    private String token;


    private static final ConnectServer instance = new ConnectServer();

    private ConnectServer(){}
    public static ConnectServer getInstance(){
        return instance;
    }
    public void setAsncTask(AsyncTask<String, Void, Boolean> task) {
        this.task = task;
    }
    public void setToken(String token){this.token = token;}
    public void execute(){
        this.task.execute();
    }
}
