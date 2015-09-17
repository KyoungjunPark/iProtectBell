package rasberrymuffine.homecaresystemapp;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.webkit.WebSettings;
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
        web.setInitialScale(1);
        web.setPadding(0, 0, 0, 0);
        web.setBackgroundColor(Color.TRANSPARENT);
        web.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
        web.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
        web.getSettings().setLoadWithOverviewMode(true);
        web.getSettings().setUseWideViewPort(true);
        web.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        web.setScrollbarFadingEnabled(false);

    }

}
