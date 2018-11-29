package id.nadji.cctvjakarta;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import static id.nadji.cctvjakarta.MainActivity.EXTRA_URL;

public class PlayVideo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_video);
        WebView webView = (WebView)findViewById(R.id.webview);

        Intent intent = getIntent();
        String url = intent.getStringExtra(EXTRA_URL);

        webView.setWebChromeClient(new WebChromeClientCustomClient());

        WebSettings webSettings = webView.getSettings();
        webSettings.setPluginState(WebSettings.PluginState.ON_DEMAND);

        webSettings.setJavaScriptEnabled(true);
        webView.setFocusable(true);
        webView.loadUrl(url);

    }

    private class WebChromeClientCustomClient extends WebChromeClient {
        @Override
        public Bitmap getDefaultVideoPoster() {
            return Bitmap.createBitmap(10, 10, Bitmap.Config.ARGB_8888);
        }
    }
}
