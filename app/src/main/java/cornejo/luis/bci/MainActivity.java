package cornejo.luis.bci;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button IniciarSesion,Registrarse;
    String Usuario,Contrasena;
    TextView NombreUsuario,ContrasenaUsuario;
    MediaPlayer mediaPlayer;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //mediaPlayer = MediaPlayer.create(MainActivity.this,R.raw.madre);
        //mediaPlayer.start();
        //Materializar los Txt,Btn,...
        IniciarSesion = (Button) findViewById(R.id.Btn_IniciarSesion);
        Registrarse = (Button) findViewById(R.id.Btn_Registrarse);
        NombreUsuario = (TextView) findViewById(R.id.Txt_Usuario);
        ContrasenaUsuario = (TextView) findViewById(R.id.Txt_Contrasena);

        //Traer Datos de Registro si hay
        Bundle bundle = getIntent().getExtras();
        if(bundle != null)
        {
            Usuario = bundle.getString("Usuario1");
            Contrasena = bundle.getString("Contrasena1");
        }

        //Click en Iniciar Para cambio de MainActivity a InterfazUsuario
        IniciarSesion.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(NombreUsuario.getText().toString().equals(Usuario))
                {
                    if(ContrasenaUsuario.getText().toString().equals(Contrasena))
                    {
                        Intent intent = new Intent(MainActivity.this,InterfazUsuario.class) ;
                        intent.putExtra("Usuario2",Usuario);
                        startActivity(intent);
                    }
                    else
                        Toast.makeText(MainActivity.this, "Contraseña Invalida, Verificar",Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(MainActivity.this,"Usuario Invalido,Verificar",Toast.LENGTH_LONG).show();
            }
        });

        //Click en Registrar para cambio de MainActivity a InterfazRegitrar
        Registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this,InterfazRegistro.class);
                startActivity(intent);
            }
        });

        //Click en Campo Nombre de Usuario para que se limpie
        NombreUsuario.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(NombreUsuario.getText().toString().equals("Nombre Usuario"))
                {
                    NombreUsuario.setText("");

                }

            }
        });

        //Click en Campo Contraseña para que se limpie
        ContrasenaUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContrasenaUsuario.getText().toString().equals("asdf1234"))
                {
                    ContrasenaUsuario.setText("");
                }
            }
        });

    }
}
