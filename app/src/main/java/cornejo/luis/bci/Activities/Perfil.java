package cornejo.luis.bci.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import cornejo.luis.bci.Dialogs.DialogMenuCambioContrasena;
import cornejo.luis.bci.R;

public class Perfil extends AppCompatActivity implements View.OnClickListener {

    private TextView userName,
            completeUserName,
            titlePlace,
            place,
            pass,
            titlePass;
    private LinearLayout contrasena,
            Ll_Perfil;
    private ImageView userImage;
    private int offset = 25;
    private Context context;
    private  String usuarioLogeado, contrasenaUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);


        Toolbar toolbar = findViewById(R.id.toolbarPerfil);
        setSupportActionBar(toolbar);

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
                AlertDialog.Builder builder = new AlertDialog.Builder(Perfil.this);
                builder.setTitle("¿Cerrar Sesión?")
                        .setMessage("Está a punto de salir.")
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(getApplicationContext(), LogoInicio.class);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton("Cancelar", null)
                        .create()
                        .show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void initComponents()
    {
        //usuarioLogeado = getIntent().getExtras().getString("usuario");
        //contrasenaUsuario = getIntent().getExtras().getString("contrasena");
        completeUserName = findViewById(R.id.profileUsername);
        userName = findViewById(R.id.profileAlias);
        userImage = findViewById(R.id.profileImage);
        titlePlace = findViewById(R.id.profilePlace);
        place = findViewById(R.id.titleProfilePlace);
        pass = findViewById(R.id.profilePass);
        titlePass = findViewById(R.id.titleProfilePass);
        contrasena = findViewById(R.id.campo_password);
        contrasena.setOnClickListener(this);
        Ll_Perfil = findViewById(R.id.container_perfil);

        context = Perfil.this;

        userName.setText(usuarioLogeado);
    }

    private void initAnimations(){
        Ll_Perfil = findViewById(R.id.container_perfil);

        completeUserName.setVisibility(View.INVISIBLE);
        userName.setVisibility(View.INVISIBLE);
        userImage.setVisibility(View.INVISIBLE);
        titlePlace.setVisibility(View.INVISIBLE);
        place.setVisibility(View.INVISIBLE);
        titlePass.setVisibility(View.INVISIBLE);
        pass.setVisibility(View.INVISIBLE);

        completeUserName.startAnimation(loadAnimation(true));
        completeUserName.setVisibility(View.VISIBLE);
        userName.startAnimation(loadAnimation(true));
        userName.setVisibility(View.VISIBLE);
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
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.campo_password:
                DialogMenuCambioContrasena dialogMenuCambioContrasena =  new DialogMenuCambioContrasena();
                dialogMenuCambioContrasena.getContent(this, Ll_Perfil, contrasenaUsuario, usuarioLogeado);
                dialogMenuCambioContrasena.show(getSupportFragmentManager(),"Cambio Contrasena");
                break;
        }
    }
}
