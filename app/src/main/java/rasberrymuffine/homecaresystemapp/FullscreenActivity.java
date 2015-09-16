package rasberrymuffine.homecaresystemapp;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by 예림 on 2015-09-15.
 */
public class FullscreenActivity extends Activity {

    WebView web;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);

        web = (WebView) findViewById(R.id.fullScreenView);
        web.getSettings().setJavaScriptEnabled(true);
        web.loadUrl("http://165.194.104.19:8080/stream");
        web.setWebViewClient(new WebViewClient());

    }

}
