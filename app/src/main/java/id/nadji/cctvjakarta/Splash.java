package id.nadji.cctvjakarta;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class Splash extends AppCompatActivity {
    TextView judul, subjudul, versi;
    ImageView logo, background;
    Animation zoomin, zoominlogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        zoomin = AnimationUtils.loadAnimation(this, R.anim.zoomin);
        zoominlogo = AnimationUtils.loadAnimation(this, R.anim.zoominlogo);
//        geserkiri = AnimationUtils.loadAnimation(this, R.anim.geserkiri);

        background = (ImageView) findViewById(R.id.bg);
        logo = (ImageView) findViewById(R.id.logo);
        judul = (TextView) findViewById(R.id.judul);
        subjudul = (TextView) findViewById(R.id.subJudul);
        versi = (TextView) findViewById(R.id.text_versi);

        background.startAnimation(zoomin);
        logo.startAnimation(zoominlogo);
//        judul.startAnimation(geserkiri);
//        subjudul.startAnimation(geserkiri);
//        versi.startAnimation(geserkiri);

        judul.setTranslationX(400);
        subjudul.setTranslationX(400);
        versi.setTranslationX(400);

        judul.animate().translationX(0).setDuration(800).setStartDelay(500).start();
        subjudul.animate().translationX(0).setDuration(800).setStartDelay(700).start();
        versi.animate().translationX(0).setDuration(800).setStartDelay(900).start();


        Thread timer = new Thread() {
            public void run() {
                try {
                    //Display for 3 seconds
                    sleep(2000);
                } catch (InterruptedException e) {
                    // TODO: handle exception
                    e.printStackTrace();
                } finally {
                    Intent intent = new Intent(Splash.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        };
        timer.start();
    }
}
