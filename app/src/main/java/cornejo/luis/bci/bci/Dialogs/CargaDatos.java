package cornejo.luis.bci.bci.Dialogs;

import android.animation.ObjectAnimator;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import cornejo.luis.bci.R;

public class CargaDatos extends AppCompatDialogFragment {

    private Context context;
    private LinearLayout linearLayout, linearLayoutDatos;
    private ProgressBar progressBar;
    private TextView textView, avisos;
    private double lecturas[] =  new double[9];
    private String usuarioLogeado;
    private Button button;
    private int cont, progreso = 0;
    private String[] valores = {"Control Subir Brillo", "Control Bajar Brillo", "Control Subir Volumen", "Control Bajar Volumen", "Bloquear Pantalla"};
    private Spinner spinner;

    public void setContent(Context context, LinearLayout linearLayout, String usuarioLogeado){
        this.context = context;
        this.linearLayout = linearLayout;
        this.usuarioLogeado = usuarioLogeado;
    }
    public Dialog onCreateDialog(Bundle savedInstanceState){
        Snackbar.make(linearLayout, "Carga de datos", Snackbar.LENGTH_LONG);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.dialog_carga_datos, null);

        spinner = view.findViewById(R.id.Spinner_tipoValor);
        ArrayAdapter<String> adapter = new ArrayAdapter<>( context, R.layout.support_simple_spinner_dropdown_item, valores);
        spinner.setAdapter(adapter);

        builder.setView(view)
                .setTitle("Carga de Datos")
                .setNegativeButton("Cancelar", null)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(progressBar.getProgress() >=270)
                        {
                            Toast.makeText( getContext(), "Guardando...", Toast.LENGTH_LONG).show();
                            dismiss();
                        }
                        else
                        {
                            Toast.makeText( getContext(), "Debe realizar la carga completa antes de enviar", Toast.LENGTH_LONG).show();
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
                    Toast.makeText( getContext(), "Maximo de datos guardados", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    try
                    {
                        for (int i = 0; i < lecturas.length ; i++)
                        {
                            if( lecturas[i] ==  0.0)
                            {
                                //Hacer que el valor de la diadema sea guardado aquí//
                                lecturas[i] = 1;
                                ObjectAnimator animation = ObjectAnimator.ofInt(progressBar, "progress",
                                        progressBar.getProgress(), progressBar.getProgress() + 31);
                                animation.setDuration(500);
                                animation.start();
                                break;
                            }
                        }
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                cont = 0;
                                for (int i = 0 ; i <lecturas.length ; i++) {
                                    if( lecturas[i] != 0.0)
                                    {
                                        cont++;
                                    }
                                }
                                textView.setText("Progreso: " + cont + "/9.");
                            }
                        },500);
                    }
                    catch (ArrayIndexOutOfBoundsException kiwi)
                    {
                        avisos.setText("Maximo de Datos Guardado");
                    }

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
