package cornejo.luis.bci.Clases;

import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;

public class CRestful extends AsyncTask<Void,Void,String[][]> {

    private String HTTP_RESTFUL;
    private String url_p="http://itqisc2019.com/Gatos/";
    private Context context;
    private String info, nombre, password, dato, tabla, condicion, datoPeticion, valor, accion, registroRespuesta;
    private boolean login, actualizado, insertado, respuesta;
    private double[] frecuencias, frecuenciasAccion;
    private int idAccion, idUsuario;
    private LinearLayout linearLayout;

    //Constructor para logearse//
    public CRestful(LinearLayout linearLayout, String info, String nombre, String password) {
        this.linearLayout = linearLayout;
        this.info = info; //login//
        this.nombre = nombre;
        this.password = password;
        this.login = false;
        HTTP_RESTFUL = getURL(info);
        Log.d("url","url:"+HTTP_RESTFUL);
    }
    //Contructor para guardar las frecuencias (Insertar)//
    public CRestful(Context context, String info, String nombre, double[] frecuencias, int idAccion, LinearLayout linearLayout){
        this.context = context;
        this.info = info; //insertar//
        this.nombre =  nombre;
        this.frecuencias =  frecuencias;
        this.idAccion = idAccion;
        this.linearLayout = linearLayout;
        this.insertado = false;
        HTTP_RESTFUL = getURL(info);
        Log.d("url","url:"+HTTP_RESTFUL);
    }
    //Constructor para consultar un dato//
    //Ej). SELECT 'dato'=id_usuarios FROM 'tabla'=Usuarios WHERE 'condicion'= nombre='luis'//
    public CRestful(Context context, String info, String dato, String tabla, String condicion, LinearLayout linearLayout)
    {
        this.context = context;
        this.info = info; //consultar//
        this.dato = dato;
        this.tabla = tabla;
        this.condicion = condicion;
        HTTP_RESTFUL = getURL(info);
        this.linearLayout = linearLayout;
        Log.d("url","url:"+HTTP_RESTFUL);
    }
    //Constructor para actualizarse//
    public  CRestful(String info, String dato, String tabla, String condicion, String valor, LinearLayout linearLayout){

        this.info = info;
        this.dato = dato;
        this.tabla = tabla;
        this.condicion = condicion;
        this.valor = valor;
        this.actualizado = false;
        this.linearLayout = linearLayout;
        HTTP_RESTFUL = getURL(info);
        Log.d("url","url:"+HTTP_RESTFUL);
    }
    //Constructor para consultar los datos(frecuencias) de las acciones por ID//
    public CRestful(String info, int idUsuario, LinearLayout linearLayout)
    {
        this.info = info; //consultarDatos//
        this.idUsuario = idUsuario;
        this.linearLayout = linearLayout;
        HTTP_RESTFUL = getURL(info);
        Log.d("url","url:"+HTTP_RESTFUL);
    }

    public String getURL(String info)
    {
        switch(info)
        {
            case "login": return url_p+"control.php?action=login&nombre="+nombre+"&password="+password;
            case "insertar": return url_p+"control.php?action=insertar&nombre="+nombre+"&dato1="+frecuencias[0]+"&dato2="+
                    frecuencias[1]+"&dato3="+frecuencias[2]+"&idaccion="+idAccion;
            case "consultar": return url_p+"control.php?action=consultar&dato="+dato+"&condicion="+condicion+"&tabla="+tabla;
            case "actualizar": return url_p+"control.php?action=actualizar&dato="+dato+"&condicion="+condicion+"&tabla="+tabla+"&valor="+valor;
            case "consultarDatos": return url_p+"control.php?action=consultarDatos&id_usuario="+idUsuario;
            default: return url_p+"control_p.php?action=error";// ToDo: checar esta Url en el php//
        }
    }

    @Override
    protected  String[][] doInBackground(Void... arg0){
        String[][] resul = getRestFul();
        return resul;
    }
    //Metodo que se conecta al RESTFUL para obtener un resultado
    public String[][] getRestFul()
    {
        Log.i("Inicio getRestful","holis");
        String jsonResult="";
        String[][] list3 = new String[1][5];
        String[][] listE = new String[1][5];
        listE[0][0] = "0";
        try {
            Log.i("Url dentro try",HTTP_RESTFUL);

            URL url = new URL(HTTP_RESTFUL);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();

            InputStream inputStream = httpURLConnection.getInputStream();
            //Log.i("Input",String.valueOf(inputStream));
            jsonResult = inputStreamToString(inputStream).toString();//response.getEntity().getContent()).toString();
            Log.i("Input jason",String.valueOf(jsonResult));
            int pos = jsonResult.indexOf('{');
            int pos2 = jsonResult.lastIndexOf('}');
            if(pos == -1)
            {
                list3[0][0]="0";
                return list3;
            }
            String jsnres = jsonResult.substring(pos, pos2 + 1);
            //Usare hasta aquí -> Valor real de Json
            JSONObject object = new JSONObject(jsnres);
            //obtiene el status
            String status = object.getString("status");
            if( status.equals("50") )           //50 -> todo esta bien
            {
                //extrae los registros
                JSONArray array = new JSONArray(object.getString("Registros"));
                Log.i("Length array", String.valueOf(array.length()));
                list3 = new String[array.length()+1][10];
                for (int i = 1; i <= array.length(); i++)
                {
                    //recorre cada registro y concatena el resultado
                    JSONObject row = array.getJSONObject(i-1);
                    switch(this.info)
                    {
                        case "login":
                            list3[0][0]= status;
                            list3[0][1]=row.getString("respuesta");
                            if (list3[0][1].toString().equals("ok"))
                            {
                                this.login = true;
                            }
                            break;
                        case "consultar":
                            list3[0][0] =  status;
                            list3[0][1] = row.getString("respuesta");
                            this.datoPeticion = list3[0][1];
                            break;

                        case "actualizar":
                            list3[0][0] =  status;
                            list3[0][1] = row.getString("respuesta");
                            if (list3[0][1].toString().equals("actualizado"))
                            {
                                mensajeSnack(linearLayout, "Cambio de contraseña exitoso");
                                this.actualizado = true;
                            }
                            break;
                        case "insertar":
                            list3[0][0] =  status;
                            list3[0][1] = row.getString("respuesta");
                            if (list3[0][1].toString().equals("insertado"))
                            {
                                this.insertado = true;
                                mensajeSnack(linearLayout, "Datos insertados correctamente");
                            }
                            break;
                        case "consultarDatos":
                            list3[0][0] =  status;
                            this.registroRespuesta = list3[0][1];
                            break;
                    }
                }
                return list3;
            }
            else if( status.equals("500") )
                list3[0][0]="500";
            else
                list3[0][0]="0";

            return list3;
        }catch (UnknownHostException e)
        {
            listE[0][0] = e.getMessage();
        }
        //Checar esta excepcion
        /*catch (ClientProtocolException e) {
            listE[0][0] = e.getMessage();
            e.printStackTrace();
        }*/ catch (IOException e) {
            listE[0][0] = e.getMessage();
            e.printStackTrace();
        } catch (JSONException e) {
            listE[0][0] = e.getMessage();
            e.printStackTrace();
        }

        return listE;
    }

    private StringBuilder inputStreamToString(InputStream is)
    {
        String line = "";
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader rd = new BufferedReader( new InputStreamReader(is) );
        try
        {
            while( (line = rd.readLine()) != null )
                stringBuilder.append(line);
        }
        catch( IOException e)
        {
            e.printStackTrace();
        }
        return stringBuilder;
    }
    @Override
    protected void onPostExecute(String resul[][]) {
        Log.d("onPostExecute","res:"+resul[0][0].toString());
        if (resul[0][0].equals("50"))
        {
            //pass//
        }
        else
        {
            if (resul[0][0].equals("500"))
            {
                switch (this.info)
                {
                    case "login": break;
                    case "consultar": break;
                    case "insertar": break;
                    case "consultarDatos": break;
                    case "actualizar": mensajeSnack(linearLayout, "Fallo al actualizar, compruebe sus datos");
                        break;
                }
            }
            else
                mensajeSnack(linearLayout, "Ocurrio un error: Verifique su conexión");
        }
    }

    public void mensajesToast (Context context, String mensaje) {
        Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show();
    }
    public void mensajeSnack(LinearLayout linearLayout, String mensaje){
        Snackbar.make(linearLayout, mensaje, Snackbar.LENGTH_LONG).show();
    }
    public boolean getLogin() {
        return this.login;
    } //Para el login//
    public boolean getInsertado() { return this.insertado; }//Para el insertado//
    public boolean getActualizado() { return  this.actualizado; }//Para el actualizado//
    public String getDatoPeticion() { return  this.datoPeticion; } //El dato que regresa la petición//
    public String getRegistrosRespuesta() { return this.registroRespuesta; } //Los registros de las acciones//
}
