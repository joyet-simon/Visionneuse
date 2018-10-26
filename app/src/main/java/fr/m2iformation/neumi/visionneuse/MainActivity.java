package fr.m2iformation.neumi.visionneuse;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    ImageView ivImage;
    Sensor sensorAcc;
    Sensor sensorLight;
    SensorManager sensorManager;
    int[] images = {
            R.mipmap.carte1, R.mipmap.carte2, R.mipmap.carte3, R.mipmap.carte4, R.mipmap.carte5,
            R.mipmap.carte6, R.mipmap.manga_girl1, R.mipmap.manga_girl2, R.mipmap.manga_girl3,
            R.mipmap.manga_girl4, R.mipmap.manga_girl5, R.mipmap.manga_girl6, R.mipmap.manga_girl7,
            R.mipmap.img_ff7};
    int[] sons = {R.raw.chevre, R.raw.fouet, R.raw.apphoto, R.raw.chat, R.raw.photo, R.raw.alarme,
            R.raw.soda, R.raw.epee, R.raw.spitz, R.raw.fermeture, R.raw.sonnette, R.raw.bouchon,
            R.raw.klaxon, R.raw.barre};
    int index = 0;
    int index2 = 0;
    long lastTime = 0l;
    private final long ACCVALUE = 20;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ivImage = findViewById(R.id.ivImage);
        ivImage.setImageResource(images[0]);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorAcc = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorLight = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensorAcc, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, sensorLight, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    public void suivant(View view) {
        ++index;
        afficherImage();
        ++index2;
        jouerSon();
    }

    public void precedent(View view) {
        --index;
        afficherImage();
        --index2;
        jouerSon();
    }

    private void afficherImage() {
        if (index == images.length) index = 0;
        if (index < 0) index = images.length - 1;
        ivImage.setImageResource(images[index]);
    }

    private void jouerSon() {
        if (index2 == sons.length) index2 = 0;
        if (index2 < 0) index2 = sons.length - 1;
        MediaPlayer mp = MediaPlayer.create(getApplicationContext(), sons[index2]);
        mp.start();
    }

    private void changerImageEtSon() {
        if (System.currentTimeMillis() - lastTime > 750) {
            lastTime = System.currentTimeMillis();
            ++index;
            jouerSon();
            afficherImage();
            ++index2;
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float lightValue;
        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            lightValue = event.values[0];
            if (lightValue < 50) {
                changerImageEtSon();
            }
        }
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            if (event.values[0] > ACCVALUE || event.values[1] > ACCVALUE || event.values[2] > ACCVALUE) {
                changerImageEtSon();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
