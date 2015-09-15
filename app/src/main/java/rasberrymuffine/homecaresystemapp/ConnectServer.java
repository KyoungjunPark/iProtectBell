package rasberrymuffine.homecaresystemapp;

import android.os.AsyncTask;


/**
 * Created by 경준 on 2015-09-13.
 */
public class ConnectServer {
    private AsyncTask<String, Void, Boolean> task;
    private String userID;
    private String userPW;


    public ConnectServer(AsyncTask<String, Void, Boolean> task) {
        this.task = task;
        this.task.execute();
    }

    public boolean isFinished() {
        if (task.getStatus() == AsyncTask.Status.FINISHED) return false;
        else return true;
    }

    private static class CommunicationTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {

            return true;
        }
    }


}
