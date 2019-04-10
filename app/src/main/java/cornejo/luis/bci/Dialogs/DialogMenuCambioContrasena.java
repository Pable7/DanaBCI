package cornejo.luis.bci.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import cornejo.luis.bci.Clases.CRestful;
import cornejo.luis.bci.R;

public class DialogMenuCambioContrasena extends AppCompatDialogFragment {

    private EditText viejaContrasena, nuevaContrasena, confirmarNuevaContrasena;
    private Context context;
    private DialogMenuCambioContrasena listener;
    private LinearLayout Perfil;
    private String contrasenaVieja, usuario;

    public void getContent(Context context, LinearLayout container, String contrasenaVieja, String usuario){
        Snackbar.make(container, "Asegurese de tener conexión a Internet", Snackbar.LENGTH_LONG).show();
        this.context = context;
        this.Perfil = container;
        this.contrasenaVieja = contrasenaVieja;
        this.usuario = usuario;
    }
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_menu_cambio_contrasena, null);
        builder.setView(view)
                .setTitle("Cambio de Contraseña")
                .setNegativeButton("Cancelar",null)
                .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (!viejaContrasena.getText().toString().equals(contrasenaVieja))
                            Snackbar.make(Perfil, "Error: Contraseña actual no coincide",Snackbar.LENGTH_LONG).show();
                        else if(!nuevaContrasena.getText().toString().equals(confirmarNuevaContrasena.getText().toString()))
                            Snackbar.make(Perfil, "Error: Contraseñas nuevas no coinciden",Snackbar.LENGTH_SHORT).show();
                        else
                        {
                            CRestful cRestful = new CRestful("actualizar", "contrasena", "Usuarios", usuario, nuevaContrasena.getText().toString(), Perfil);
                            cRestful.execute();
                        }
                    }
                });
        viejaContrasena = view.findViewById(R.id.contrasenaVieja);
        nuevaContrasena =  view.findViewById(R.id.nuevaContrasena);
        confirmarNuevaContrasena = view.findViewById(R.id.confirmarContrasena);
        return  builder.create();
    }


}