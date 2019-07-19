package id.nadji.cctvjakarta;

import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static id.nadji.cctvjakarta.MainActivity.EXTRA_LATITUDE;
import static id.nadji.cctvjakarta.MainActivity.EXTRA_LONGITUDE;
import static id.nadji.cctvjakarta.MainActivity.EXTRA_URL;

public class PlayVideo extends AppCompatActivity {
    Intent intent;
    TextView tvLokasi;
    List<Address> address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_video);
        WebView webView = (WebView) findViewById(R.id.webview);
        tvLokasi = findViewById(R.id.tv_lokasi);
        intent = getIntent();


        String url = intent.getStringExtra(EXTRA_URL);
        String lat = intent.getStringExtra(EXTRA_LATITUDE);
        String lon = intent.getStringExtra(EXTRA_LONGITUDE);

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            address = geocoder.getFromLocation(Double.valueOf(lat), Double.valueOf(lon), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String alamat = address.get(0).getAddressLine(0);

        webView.setWebChromeClient(new WebChromeClientCustomClient());
        WebSettings webSettings = webView.getSettings();
        webSettings.setPluginState(WebSettings.PluginState.ON_DEMAND);
        webSettings.setJavaScriptEnabled(true);
        webView.setFocusable(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.loadUrl(url);
        tvLokasi.setText(alamat);



    }


    private class WebChromeClientCustomClient extends WebChromeClient {
        @Override
        public Bitmap getDefaultVideoPoster() {
            return Bitmap.createBitmap(10, 10, Bitmap.Config.ARGB_8888);
        }
    }
}
