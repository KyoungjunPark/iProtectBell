package rasberrymuffine.homecaresystemapp;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by KJPARK on 2015-09-17.
 */
public class MyInstanceIDListenerService extends InstanceIDListenerService{
    private static final String TAG = "MyInstanceIDLS";

    @Override
    public void onTokenRefresh() {
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
    }
}
