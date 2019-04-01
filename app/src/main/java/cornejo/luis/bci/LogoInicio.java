package cornejo.luis.bci;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import cornejo.luis.bci.Clases.CRestful;

public class LogoInicio extends AppCompatActivity {

    private ProgressBar progressBarSplash;
    private Button buttonConnection;
    private EditText Txt_User, Txt_Password;
    private TextView Jbl_Loading;
    private LinearLayout Ll_Data;
    private Handler handler;
    private int offset = 75;
    private CRestful restful;
    private boolean login;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo_inicio);

        initComponents();
        initAnimation();
        buttonConnection.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (v.getId()) {
                    case R.id.Btn_IniciarSesion2:
                        if(event.getAction() == MotionEvent.ACTION_DOWN)
                        {
                            Float x = (float) 0.96, y = (float) 0.98;
                            buttonConnection.setScaleX(x);
                            buttonConnection.setScaleY(y);
                            buttonConnection.setTranslationZ((float) 1);
                        }
                        else if(event.getAction() == MotionEvent.ACTION_UP)
                        {
                            Float x = (float) 1, y = (float) 1;
                            buttonConnection.setScaleX(x);
                            buttonConnection.setScaleY(y);
                        }
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        buttonConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restful = new CRestful(LogoInicio.this, "login", Txt_User.getText().toString(), Txt_Password.getText().toString());
                restful.execute();
                LogearseCorrecto();
            }
        });
    }

    public void initComponents()
    {
        progressBarSplash =  findViewById(R.id.Progress_splash);
        buttonConnection =  findViewById(R.id.Btn_IniciarSesion2);
        Txt_User =  findViewById(R.id.Txt_Usuario2);
        Txt_Password =  findViewById(R.id.Txt_Contrasena2);
        Jbl_Loading = findViewById(R.id.Jbl_Cargando);
        Ll_Data =  findViewById(R.id.Ll_Datos);
        handler =  new Handler(getApplication().getMainLooper());
    }
    public void initAnimation()
    {

        Animation animation = AnimationUtils.loadAnimation(LogoInicio.this, R.anim.alpha);
        buttonConnection.startAnimation(animation);
        Txt_User.startAnimation(animation);
        Txt_Password.startAnimation(animation);
        buttonConnection.setStateListAnimator(null);

        Txt_User.setVisibility(View.INVISIBLE);
        Txt_Password.setVisibility(View.INVISIBLE);
        buttonConnection.setVisibility(View.INVISIBLE);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBarSplash.setVisibility(View.INVISIBLE);
                Ll_Data.setVisibility(View.VISIBLE);
                Txt_User.startAnimation(loadAnimation());
                Txt_User.setVisibility(View.VISIBLE);
                Txt_Password.startAnimation(loadAnimation());
                Txt_Password.setVisibility(View.VISIBLE);
                buttonConnection.startAnimation(loadAnimation());
                buttonConnection.setVisibility(View.VISIBLE);
                Jbl_Loading.setVisibility(View.INVISIBLE);
            }
        }, 2500);



    }

    private Animation loadAnimation(){
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.item_fall_down);
        animation.setStartOffset(offset);
        offset += + 75;
        return  animation;
    }

    private void LogearseCorrecto()
    {
        final ProgressDialog progressDialog = new ProgressDialog(LogoInicio.this);;
        progressDialog.setMessage("Autenticando...");
        progressDialog.setCancelable(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
                login =  restful.getLogin();
                if (login)
                {
                    Intent intent =  new Intent(LogoInicio.this, InterfazUsuario.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 3000);
    }


}

