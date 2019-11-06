package cornejo.luis.bci.Activities;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import cornejo.luis.bci.Clases.Lock;
import cornejo.luis.bci.R;

public class ControlValores extends AppCompatActivity {

    private SeekBar volumen, brillo;
    private Button bloquear;
    private AudioManager audioManager;
    private DevicePolicyManager devicePolicyManager;
    private ActivityManager activityManager;
    private ComponentName compName;
    private final int RESULT_ENABLE = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_valores);


        initComponents();
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
        bloquear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean active = devicePolicyManager.isAdminActive(compName);
                if (active) {
                    devicePolicyManager.lockNow();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder( ControlValores.this);
                    builder.setTitle( "Activar permisos")
                            .setMessage( "Para poder usar el bloqueo de pantalla necesita activar los permisos necesarios. ¿Desea habilitarlos?.")
                            .setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                                    intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, compName);
                                    intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Additional text explaining why we need this permission");
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton( "CANCELAR", null)
                            .create()
                            .show();
                }
            }
        });
    }

    private void initComponents(){

        bloquear = findViewById( R.id.Btn_Bloquear_Pantalla);
        devicePolicyManager = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        compName = new ComponentName(ControlValores.this, Lock.class);

        volumen = findViewById(R.id.SkBr_Volumen);
        audioManager = (AudioManager) ControlValores.this.getSystemService(Context.AUDIO_SERVICE);
        volumen.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        volumen.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));

        brillo =  findViewById(R.id.SkBr_Brillo);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.zoom_fowar_in, R.anim.zoom_foward_out);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch(requestCode) {
            case RESULT_ENABLE :
                if ( resultCode == Activity.RESULT_OK) {
                    Toast.makeText( ControlValores.this, "You have enabled the Admin Device features", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText( ControlValores.this, "Problem to enable the Admin Device features", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
