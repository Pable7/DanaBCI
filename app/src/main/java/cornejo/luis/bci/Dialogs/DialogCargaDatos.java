package cornejo.luis.bci.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import cornejo.luis.bci.Clases.CRestful;
import cornejo.luis.bci.R;

public class DialogCargaDatos  extends AppCompatDialogFragment {

    private Context context;
    private LinearLayout linearLayout, linearLayoutDatos;
    private ProgressBar progressBar;
    private TextView textView, avisos;
    private double lecturas[] =  new double[9];
    private String usuarioLogeado;
    private Button button;
    private int cont, progreso = 0;

    public void getContent(Context context, LinearLayout linearLayout, String usuarioLogeado){
        this.context = context;
        this.linearLayout = linearLayout;
        this.usuarioLogeado = usuarioLogeado;
    }
    public Dialog onCreateDialog(Bundle savedInstanceState){
        Snackbar.make(linearLayout, "Carga de datos", Snackbar.LENGTH_LONG);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.dialog_carga_datos, null);
        builder.setView(view)
                .setTitle("Carga de Datos")
                .setNegativeButton("Cancelar", null)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Si los datos se suben correctamente, si el metodo Rest devuelve un true//
                        final CRestful cRestful = new CRestful(context, "insertar", usuarioLogeado, lecturas, 1, linearLayout ); //checar el idAccion//
                        cRestful.execute();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if(cRestful.getInsertado())
                                    Snackbar.make(linearLayout, "Datos cargados correctamente", Snackbar.LENGTH_LONG).show();
                                else
                                    Snackbar.make(linearLayout, "Error al cargador datos\nIntentelo nuevamente", Snackbar.LENGTH_LONG).show();
                            }
                        }, 1500);
                    }
                });
        progressBar = view.findViewById(R.id.progressBar);
        textView = view.findViewById(R.id.Jbl_cargaDatos);
        button = view.findViewById(R.id.Btn_registrar);
        avisos = view.findViewById(R.id.Jbl_Aviso);
        linearLayoutDatos = view.findViewById(R.id.Ll_CargaDatos);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(linearLayoutDatos, "Click", Snackbar.LENGTH_SHORT).show();
                if(progreso == 90)
                {
                    avisos.setText("Maximo de datos guardados");
                }
                else
                {
                    progreso += 10;
                    progressBar.setProgress(progreso);
                }
                /*try
                {
                    for (int i = 0; i < lecturas.length ; i++)
                    {
                        if(lecturas[i] ==  0.0)
                        {
                            //Hacer que el valor de la diadema sea guardado aquí//
                            //lecturas[i] = ValorDiadema;//
                            //Que el progreso del ProgreseBar avance//
                        }
                    }
                    for (int i = 0, cont = 0; i <lecturas.length ; i++) {
                        if( lecturas[i] != 0.0)
                        {
                            cont++;
                        }
                    }
                    textView.setText("Progreso: " + cont + "/9.");
                }
                catch (ArrayIndexOutOfBoundsException kiwi)
                {
                    avisos.setText("Maximo de Datos Guardado");
                }*/
            }
        });

        return  builder.create();
    }

}
