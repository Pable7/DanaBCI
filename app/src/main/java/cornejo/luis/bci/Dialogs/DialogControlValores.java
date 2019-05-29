package cornejo.luis.bci.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import cornejo.luis.bci.R;

public class DialogControlValores extends AppCompatDialogFragment {

    private SeekBar volumen, brillo;
    private Context context;
    private LinearLayout linearLayout, linearLayoutDialog;
    private AudioManager audioManager;

    public void getContent(Context context, LinearLayout linearLayout)
    {
        Snackbar.make(linearLayout, "Control de Acciones", Snackbar.LENGTH_LONG).show();
        this.context = context;
        this.linearLayout = linearLayout;

    }
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.dialog_control_valores, null);
        builder.setView(view)
                .setTitle("Control de valores")
                .setPositiveButton("Aceptar", null);

        volumen = view.findViewById(R.id.SkBr_Volumen);
        brillo =  view.findViewById(R.id.SkBr_Brillo);
        linearLayoutDialog = view.findViewById(R.id.Ll_Control);
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
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
        final AlertDialog dlg = builder.show();
        final WindowManager.LayoutParams layoutParams = dlg.getWindow().getAttributes();
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
                dlg.getWindow().setAttributes(layoutParams);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        return builder.create();
    }
}
