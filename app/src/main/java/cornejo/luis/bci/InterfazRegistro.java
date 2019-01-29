package cornejo.luis.bci;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class InterfazRegistro extends AppCompatActivity {

    Button Registrar;
    String Usuario, Contrasena;
    TextView UsuarioRegistro, ContrasenaRegistro;

    @SuppressLint("WrongViewCast")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interfaz_registro);

        Toast.makeText(InterfazRegistro.this,"Favor de Registrarse",Toast.LENGTH_LONG).show();

        Registrar = (Button) findViewById(R.id.Btn_RegistroUsuario);
        UsuarioRegistro = (TextView) findViewById(R.id.Txt_UsuarioRegistro);
        ContrasenaRegistro = (TextView) findViewById(R.id.Txt_ContrasenaRegistro);

        Registrar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Usuario = UsuarioRegistro.getText().toString();
                Contrasena = ContrasenaRegistro.getText().toString();
                //Toast.makeText(InterfazRegistro.this,"Usuario Registrado\n"+Usuario+"\n"+Contrasena,Toast.LENGTH_LONG).show();
                Intent intent = new Intent(InterfazRegistro.this,MainActivity.class);
                intent.putExtra("Usuario1",Usuario);
                intent.putExtra("Contrasena1",Contrasena);
                startActivity(intent);
            }
        });

        UsuarioRegistro.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(UsuarioRegistro.getText().toString().equals("Nombre Usuario"));
                {
                    UsuarioRegistro.setText("");
                }
            }
        });

        ContrasenaRegistro.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(ContrasenaRegistro.getText().toString().equals("asdf1234"))
                {
                    ContrasenaRegistro.setText("");
                }

            }
        });



    }
}
