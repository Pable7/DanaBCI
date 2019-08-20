package cornejo.luis.bci.Dialogs;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

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
                        if(progressBar.getProgress() >=270)
                        {
                            int idaccion = 1;//Aquí va el valor que va a controlar en el telefono 1.SubirBrillo 2.BajarBrillo 3.SubirVol 4.BajarVol//
                            //Si los datos se suben correctamente, si el metodo Rest devuelve un true//
                            final CRestful cRestful = new CRestful(context, "insertar", usuarioLogeado, lecturas, idaccion, linearLayout );
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
                        else
                        {
                            Snackbar.make(linearLayoutDatos, "Debe realizar la carga completa antes de enviar", Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setProgress(0);
        textView = view.findViewById(R.id.Jbl_cargaDatos);
        button = view.findViewById(R.id.Btn_registrar);
        avisos = view.findViewById(R.id.Jbl_Aviso);
        linearLayoutDatos = view.findViewById(R.id.Ll_CargaDatos);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(progressBar.getProgress() >= 270)
                {
                    Snackbar.make(linearLayoutDatos, "Maximo de datos guardados", Snackbar.LENGTH_SHORT).show();
                    //avisos.setText("Maximo de datos guardados");
                }
                else
                {
                    ObjectAnimator animation = ObjectAnimator.ofInt(progressBar, "progress",
                            progressBar.getProgress(), progressBar.getProgress() + 31);
                    animation.setDuration(500);
                    animation.start();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            cont++;
                            textView.setText("Progreso: " + cont + "/9.");

                        }
                    },500);

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
