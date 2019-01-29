package cornejo.luis.bci;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

public class LogoInicio extends AppCompatActivity {


    ProgressBar barra;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo_inicio);

        barra = (ProgressBar) findViewById(R.id.PBAR_Progreso) ;


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                for (int i = 0, pera = 10; i < 3; i++)
                {
                    try {
                        Thread.sleep(1000);
                        barra.setProgress(pera);
                        pera += 50;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Intent intent = new Intent(LogoInicio.this , MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.zoom_forward_out,R.anim.zoom_forward_in);
                finish();
            }
        },3000);




    }
}

