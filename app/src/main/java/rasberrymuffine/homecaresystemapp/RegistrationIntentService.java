package rasberrymuffine.homecaresystemapp;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

/**
 * Created by KJPARK on 2015-09-17.
 */
public class RegistrationIntentService extends IntentService{
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */

    private static final String TAG = "RegistrationIntent";

    public RegistrationIntentService() {
        super(TAG);
    }

    public RegistrationIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        LocalBroadcastManager.getInstance(this)
                .sendBroadcast(new Intent(QuickstartPreferences.REGISTRATION_GENERATING));

        InstanceID instanceID = InstanceID.getInstance(this);
        String token = null;

        synchronized (TAG){
            try {
                String default_senderId = getString(R.string.gcm_defaultSenderId);
                String scope = GoogleCloudMessaging.INSTANCE_ID_SCOPE;
                token = instanceID.getToken(default_senderId, scope, null);

                Log.i(TAG, "GCM Registration Token: " + token);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Intent registrationComplete = new Intent(QuickstartPreferences.REGISTRATION_COMPLETE);
        registrationComplete.putExtra("token", token);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);

    }
}
