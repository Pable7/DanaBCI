package cornejo.luis.bci.bci.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cornejo.luis.bci.R;
import cornejo.luis.bci.bci.Clases.ParentActivity;

public class PerfilU extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private TextView completeUserName,
            titlePlace,
            place,
            pass,
            titlePass,
            telefono;
    private LinearLayout contrasena;
    private ImageView userImage;
    private int offset = 25;
    private Context context;
    private  String usuarioLogeado, contrasenaUsuario, telefonoUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_u);

        Toolbar tooblar = findViewById(R.id.toolbar_profile);
        setSupportActionBar(tooblar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        initComponents();
        initAnimations();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_perfil, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.cerrar_sesion:
                AlertDialog.Builder builder = new AlertDialog.Builder(PerfilU.this);
                builder.setTitle("¿Cerrar Sesión?")
                        .setMessage("Está a punto de salir.")
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sharedPreferences.edit().clear().apply();
                                Intent intent = new Intent(getApplicationContext(), LogoInicio.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("Cancelar", null)
                        .create()
                        .show();
                break;
            case android.R.id.home:
                Intent intent = new Intent( PerfilU.this, Principal.class);
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void initComponents()
    {
        //Obteniendo datos de Usuarios de la BD//
        sharedPreferences = getSharedPreferences("Datos", Context.MODE_PRIVATE);
        usuarioLogeado = sharedPreferences.getString("user", "");
        contrasenaUsuario = sharedPreferences.getString("password", "");
        telefonoUsuario = sharedPreferences.getString("telefono","");
        //Inicializar Objetos
        completeUserName = findViewById(R.id.profileUsername);
        userImage = findViewById(R.id.profileImage);
        titlePlace = findViewById(R.id.profilePlace);
        place = findViewById(R.id.titleProfilePlace);
        pass = findViewById(R.id.profilePass);
        titlePass = findViewById(R.id.titleProfilePass);
        contrasena = findViewById(R.id.campo_password);
        telefono = findViewById(R.id.profilePhone);

        context = PerfilU.this;

        //Agregar datos a las etiquetas correspondientes//
        //telefono.setText(sharedPreferences.getString("telefono",""));
        ParentActivity parentActivity = new ParentActivity();
        parentActivity.addActiviy(PerfilU.this);
    }

    private void initAnimations(){

        completeUserName.setVisibility(View.INVISIBLE);
        userImage.setVisibility(View.INVISIBLE);
        titlePlace.setVisibility(View.INVISIBLE);
        place.setVisibility(View.INVISIBLE);
        titlePass.setVisibility(View.INVISIBLE);
        pass.setVisibility(View.INVISIBLE);

        completeUserName.startAnimation(loadAnimation(true));
        completeUserName.setVisibility(View.VISIBLE);
        userImage.startAnimation(loadAnimation(true));
        userImage.setVisibility(View.VISIBLE);
        titlePlace.startAnimation(loadAnimation(true));
        titlePlace.setVisibility(View.VISIBLE);
        place.startAnimation(loadAnimation(true));
        place.setVisibility(View.VISIBLE);
        titlePass.startAnimation(loadAnimation(true));
        titlePass.setVisibility(View.VISIBLE);
        pass.startAnimation(loadAnimation(true));
        pass.setVisibility(View.VISIBLE);
    }

    private Animation loadAnimation(boolean aumento){
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.item_fall_down);
        animation.setStartOffset(offset);
        if(aumento) offset = offset + 25;
        return  animation;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
