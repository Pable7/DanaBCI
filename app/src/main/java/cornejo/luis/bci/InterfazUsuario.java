package cornejo.luis.bci;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class InterfazUsuario extends AppCompatActivity {

    TextView Bienvenida;
    String letrero;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interfaz_usuario);

        Bienvenida = (TextView) findViewById(R.id.Jbl_BienvenidoUsuario) ;
        Bundle bundle = getIntent().getExtras();

        letrero = bundle.getString("Usuario2");
        Bienvenida.setText("Bienvenido "+letrero);

        Toast.makeText(InterfazUsuario.this,"Bienvenido",Toast.LENGTH_SHORT).show();
        startService(new Intent(InterfazUsuario.this,Servicio2Plano.class));


    }
}
