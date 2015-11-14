package rasberrymuffine.homecaresystemapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Administrator on 2015-11-04.
 */
public class AutoExecution extends BroadcastReceiver{

    public AutoExecution(){}

    @Override
    public void onReceive(Context context, Intent intent){
        if(intent.getAction().equals("EXECUTION")){
        Intent myIntent = new Intent(context,LoginActivity.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(myIntent);
        }
    }
}