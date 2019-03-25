package cornejo.luis.bci.Clases;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import cornejo.luis.bci.R;

public class Servicio2Plano extends Service
{
    //Codigo que se ejecute en 2° Plano
    MediaPlayer mediaPlayer;
    public void onCreate()
    {

    }
    public int onStartCommand(Intent intent, int flag, int idProceso)
    {
        mediaPlayer = MediaPlayer.create(this, R.raw.madre);
        mediaPlayer.start();
        return START_STICKY;
    }
    public void onDestroy()
    {
        mediaPlayer.stop();
    }

    public IBinder onBind(Intent intent) {
        return null;
    }
}
