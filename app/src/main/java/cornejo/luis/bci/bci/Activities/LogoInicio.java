package cornejo.luis.bci.bci.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
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

import cornejo.luis.bci.R;
import cornejo.luis.bci.bci.Clases.ParentActivity;


public class LogoInicio extends AppCompatActivity {

    private ProgressBar progressBarSplash;
    private Button buttonConnection;
    private EditText Txt_User, Txt_Password;
    private LinearLayout Ll_Data;
    private Handler handler;
    private int offset = 75;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

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
                if( !Txt_User.getText().toString().equals("") && !Txt_Password.getText().toString().equals(""))
                    LogearseCorrecto();
                else
                    Toast.makeText( LogoInicio.this, "Usuario o Contraseña vacios, favor de ingresarlos.", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void initComponents()
    {
        progressBarSplash =  findViewById(R.id.Progress_splash);
        buttonConnection =  findViewById(R.id.Btn_IniciarSesion2);
        Txt_User =  findViewById(R.id.Txt_Usuario2);
        Txt_Password =  findViewById(R.id.Txt_Contrasena2);
        Ll_Data =  findViewById(R.id.Ll_Datos);
        handler =  new Handler(getApplication().getMainLooper());

        preferences = getSharedPreferences( "Datos", Context.MODE_PRIVATE);
        editor = preferences.edit();
        editor.putString( "usuario1", "luis");
        editor.putString( "password1", "1234");

        ParentActivity parentActivity = new ParentActivity();
        parentActivity.addActiviy(LogoInicio.this);
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
        Log.i( "BCI-DANA", preferences.getString("usuario1", "null").equals(Txt_User.getText().toString()) + "-----" + preferences.getString( "password1", "null").equals(Txt_Password.getText().toString()));
        //if( preferences.getString("usuario1", "").equals(Txt_User.getText().toString()) && preferences.getString( "password1", "").equals(Txt_Password.getText().toString()))
       // {
            Intent intent =  new Intent(LogoInicio.this, Principal.class);
            startActivity(intent);
            finish();
        //}
        //else
            //Toast.makeText( LogoInicio.this, "Usuario o Contraseña incorrectos", Toast.LENGTH_LONG).show();
    }


}

