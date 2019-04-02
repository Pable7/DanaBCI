package cornejo.luis.bci.Clases;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.util.Log;
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
    private String info, nombre, password;
    private boolean login;
    private double[] frecuencias;
    private int idAccion;

    //Constructor para logearse
    public CRestful(Context context, String info, String nombre, String password) {
        this.context = context;
        this.info = info;
        this.nombre = nombre;
        this.password = password;
        this.login = false;
        HTTP_RESTFUL = getURL(info);
        Log.d("url","url:"+HTTP_RESTFUL);
    }
    public CRestful(Context context, String info, String nombre, double[] frecuencias, int idAccion){
        this.context = context;
        this.info = info;
        this.nombre =  nombre;
        this.frecuencias =  frecuencias;
        this.idAccion = idAccion;
        HTTP_RESTFUL = getURL(info);
        Log.d("url","url:"+HTTP_RESTFUL);
    }

    public String getURL(String info)
    {
        switch(info)
        {
            case "login": return url_p+"control.php?action=login&nombre="+nombre+"&password="+password;
            case "registrar": return url_p+"control.php?action=insertar&nombre="+nombre+"&dato1="+frecuencias[0]+"&dato2="+
                    frecuencias[1]+"&dato3="+frecuencias[2]+"&idaccion="+idAccion;
            default: return url_p+"control_p.php?action=error";// checar este metodo
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
            Log.i("Input",String.valueOf(inputStream));
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
            Log.i("status, object lenght",status+" "+String.valueOf(object.length()));
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
                            list3[0][0]=status;
                            list3[0][1]=row.getString("respuesta");
                            Log.d("getRestful","res:"+list3[0][0].toString());
                            Log.d("getRestful","res:"+list3[0][1].toString());
                            if (list3[0][1].toString().equals("ok"))
                            {
                                this.login = true;
                            }
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
            switch (this.info)
            {
                case "login":
                    if(resul[0][1].toString().equals("ok"))
                    {
                        mensajes(context,"Bienvenido");
                    }
                    else
                        mensajes(context, "Verifica tus datos");
                    break;
            }
        }
        else
        {
            if (resul[0][0].equals("500"))
            {
                switch (this.info)
                {
                    case "login": //mensajes(context,"Error: Verifique sus datos de Sesion");
                        break;
                }
            }
            else
                mensajes(context, "Ocurrio un error \n Verifique su conexión");
        }
    }

    public void mensajes (Context context, String mensaje) {
        Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show();
    }
    public boolean getLogin() {
        return this.login;
    }
}
