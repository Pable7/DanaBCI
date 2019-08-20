package cornejo.luis.bci.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;

import java.util.ArrayList;

import cornejo.luis.bci.R;

public class ControlValores extends AppCompatActivity {

    private SeekBar volumen, brillo;
    private LinearLayout linearLayoutValores;
    private AudioManager audioManager;
    private ArrayList<String> tipoValores;
    private String[] valores = {"Control Subir Brillo", "Control Bajar Brillo", "Control Subir Volumen", "Control Bajar Volumen"};
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_valores);

        tipoValores.add("Control Subir Brillo");
        tipoValores.add("Control Subir Volumen");
        tipoValores.add("Control Bajar Brillo");
        tipoValores.add("Control Bajar Volumen");

        spinner = findViewById(R.id.Spinner_tipoValor);
        ArrayAdapter<String> adapter = ArrayAdapter.createFromResource(ControlValores.this, ,
        valores);
        spinner.setAdapter(adapter);

        volumen = findViewById(R.id.SkBr_Volumen);
        brillo =  findViewById(R.id.SkBr_Brillo);
        linearLayoutValores = findViewById(R.id.Ll_Control);
        audioManager = (AudioManager) ControlValores.this.getSystemService(Context.AUDIO_SERVICE);
        volumen.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        volumen.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
        volumen.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        final WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        brillo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress <= 10)
                {
                    layoutParams.screenBrightness = 0.1f;
                }
                else if(progress <= 20)
                {
                    layoutParams.screenBrightness = 0.2f;
                }
                else if(progress <= 30)
                {
                    layoutParams.screenBrightness = 0.3f;
                }
                else if(progress <= 40)
                {
                    layoutParams.screenBrightness = 0.4f;
                }
                else if(progress <= 50)
                {
                    layoutParams.screenBrightness = 0.5f;
                }
                else if(progress <= 60)
                {
                    layoutParams.screenBrightness = 0.6f;
                }
                else if(progress <= 70)
                {
                    layoutParams.screenBrightness = 0.7f;
                }
                else if(progress <= 80)
                {
                    layoutParams.screenBrightness = 0.8f;
                }
                else if(progress <= 90)
                {
                    layoutParams.screenBrightness = 0.9f;
                }
                else if(progress <= 100)
                {
                    layoutParams.screenBrightness = 1.0f;
                }
                getWindow().setAttributes(layoutParams);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.zoom_fowar_in, R.anim.zoom_foward_out);

    }
}
