package cornejo.luis.bci.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.choosemuse.libmuse.Muse;
import com.choosemuse.libmuse.MuseFileWriter;
import com.choosemuse.libmuse.MuseManagerAndroid;
import com.choosemuse.libmuse.Accelerometer;
import com.choosemuse.libmuse.AnnotationData;
import com.choosemuse.libmuse.ConnectionState;
import com.choosemuse.libmuse.Eeg;
import com.choosemuse.libmuse.LibmuseVersion;
import com.choosemuse.libmuse.MessageType;
import com.choosemuse.libmuse.MuseArtifactPacket;
import com.choosemuse.libmuse.MuseConfiguration;
import com.choosemuse.libmuse.MuseConnectionListener;
import com.choosemuse.libmuse.MuseConnectionPacket;
import com.choosemuse.libmuse.MuseDataListener;
import com.choosemuse.libmuse.MuseDataPacket;
import com.choosemuse.libmuse.MuseDataPacketType;
import com.choosemuse.libmuse.MuseFileFactory;
import com.choosemuse.libmuse.MuseFileReader;
import com.choosemuse.libmuse.MuseListener;
import com.choosemuse.libmuse.MuseVersion;
import com.choosemuse.libmuse.Result;
import com.choosemuse.libmuse.ResultLevel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import cornejo.luis.bci.Clases.CRestful;
import cornejo.luis.bci.Clases.DeserializarJson.Registro;
import cornejo.luis.bci.Clases.ParentActivity;
import cornejo.luis.bci.Dialogs.DialogCargaDatos;
import cornejo.luis.bci.Dialogs.DialogControlValores;
import cornejo.luis.bci.R;

public class Principal extends AppCompatActivity implements View.OnClickListener {

    //<---------Variables Globales--------->//
    //SharedPreferences//
    private SharedPreferences sharedPreferences;
    //Layout Principal//
    private LinearLayout ll_principal, ll_muse;
    //Para saber quien esta loggeado//
    private String usuarioLogeado, contrasenaUsuario, jsonAux = "";
    //Para el Log//
    private final String TAG = "TestLibMuseAndroid";
    //Para el fucionamiento de la diadema//
    private MuseManagerAndroid manager;
    private Muse muse;
    private ConnectionListener connectionListener;
    private DataListener dataListener;
    private final double[] eegBuffer = new double[6];
    private boolean eegStale;
    private final double[] alphaBuffer = new double[6];
    private boolean alphaStale;
    private final double[] accelBuffer = new double[3];
    private boolean accelStale;
    private final Handler handler = new Handler();
    private ArrayAdapter<String> spinnerAdapter;
    private boolean dataTransmission = true;
    private final AtomicReference<MuseFileWriter> fileWriter = new AtomicReference<>();
    private final AtomicReference<Handler> fileHandler = new AtomicReference<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        setSupportActionBar(toolbar);

        initComponents();

        Snackbar.make(ll_principal, "Welcome " + usuarioLogeado, Snackbar.LENGTH_LONG).show();
        //Obtener ID Usuario//
        guardarIdUsuarioPreferences();
        //Obtener Telefono//
        guardarTelefonoPreferences();
        //Obtener Frecuencias//
        //guardarFrencuenciasPreferences();

        /*manager = MuseManagerAndroid.getInstance();
        manager.setContext(this);*/
        manager = MuseManagerAndroid.getInstance();
        manager.setContext( this);
        Log.i(TAG, "LibMuse version=" + LibmuseVersion.instance().getString());

        WeakReference<Principal> weakActivity = new WeakReference<Principal>(Principal.this);
        ConnectionListener connectionListener = new ConnectionListener(weakActivity);
        DataListener dataListener = new DataListener(weakActivity);
        manager.setMuseListener(new MuseL(weakActivity));

        ensurePermissions();
        fileThread.start();

        handler.post(tickUi);
    }
    private void initComponents(){
        sharedPreferences = getSharedPreferences("Datos", Context.MODE_PRIVATE);

        ll_principal = findViewById(R.id.Ll_Principal);
        ll_muse = findViewById(R.id.Ll_Muse);
        usuarioLogeado = sharedPreferences.getString("user", "nadie");
        contrasenaUsuario = sharedPreferences.getString("password", "nadie");
        Log.i("PrincipalU",usuarioLogeado+" "+contrasenaUsuario);
        //Botones con su Evento//
        Button refreshButton = findViewById(R.id.refresh);
        refreshButton.setOnClickListener(this);
        Button connectButton = findViewById(R.id.connect);
        connectButton.setOnClickListener(this);
        Button disconnectButton = findViewById(R.id.disconnect);
        disconnectButton.setOnClickListener(this);
        Button pauseButton = findViewById(R.id.pause);
        pauseButton.setOnClickListener(this);
        //Spinner//
        spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        Spinner musesSpinner = findViewById(R.id.muses_spinner);
        musesSpinner.setAdapter(spinnerAdapter);
        //Agregar actividad//
        ParentActivity parentActivity = new ParentActivity();
        parentActivity.addActiviy(Principal.this);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_perfil:
                Snackbar.make(ll_principal, "Perfil", Snackbar.LENGTH_LONG).show();
                Intent intent = new Intent(Principal.this, Perfil.class);
                startActivity(intent);
                overridePendingTransition(R.anim.zoom_fowar_in, R.anim.zoom_foward_out);
                break;
            case R.id.action_plus:
                Snackbar.make(ll_principal, "Agregar", Snackbar.LENGTH_LONG).show();
                DialogCargaDatos dialogCargaDatos = new DialogCargaDatos();
                dialogCargaDatos.getContent(Principal.this, ll_principal, usuarioLogeado);
                dialogCargaDatos.show(getSupportFragmentManager(), "Carga datos");
                break;
            case R.id.action_valores:
                Snackbar.make(ll_principal, "Valores", Snackbar.LENGTH_LONG).show();
                /*DialogControlValores dialogControlValores = new DialogControlValores();
                dialogControlValores.getContent(Principal.this, ll_principal);
                dialogControlValores.show(getSupportFragmentManager(), "Control valores");*/
                Intent intentValores = new Intent(Principal.this, ControlValores.class);
                startActivity(intentValores);
                overridePendingTransition(R.anim.zoom_fowar_in, R.anim.zoom_foward_out);
        }
        return super.onOptionsItemSelected(item);
    }
    protected void onPause() {
        super.onPause();
        manager.stopListening();
    }

    public boolean isBluetoothEnabled()
    {
        return BluetoothAdapter.getDefaultAdapter().isEnabled();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.refresh:
                manager.stopListening();
                manager.startListening();
                Snackbar.make(ll_principal, "Refresh Buttom", Snackbar.LENGTH_LONG).show();
                break;
            case R.id.connect:
                Snackbar.make(ll_principal, "Connect Buttom", Snackbar.LENGTH_LONG).show();

                manager.stopListening();
                List<Muse> availableMuses = manager.getMuses();
                Spinner musesSpinner = (Spinner) findViewById(R.id.muses_spinner);

                if (availableMuses.size() < 1 || musesSpinner.getAdapter().getCount() < 1)
                {
                    Log.w(TAG, "There is nothing to connect to");
                }
                else
                {
                    muse = availableMuses.get(musesSpinner.getSelectedItemPosition());

                    muse.unregisterAllListeners();
                    muse.registerConnectionListener(connectionListener);
                    muse.registerDataListener(dataListener, MuseDataPacketType.EEG);
                    muse.registerDataListener(dataListener, MuseDataPacketType.ALPHA_RELATIVE);
                    muse.registerDataListener(dataListener, MuseDataPacketType.ACCELEROMETER);
                    muse.registerDataListener(dataListener, MuseDataPacketType.BATTERY);
                    muse.registerDataListener(dataListener, MuseDataPacketType.DRL_REF);
                    muse.registerDataListener(dataListener, MuseDataPacketType.QUANTIZATION);

                    muse.runAsynchronously();
                }
                break;
            case R.id.disconnect:
                Snackbar.make(ll_principal, "Disconnect Buttom", Snackbar.LENGTH_LONG).show();
                if (muse != null) {
                    muse.disconnect();
                }
                break;
            case R.id.pause:
                Snackbar.make(ll_principal, "Pause Buttom", Snackbar.LENGTH_LONG).show();
                if (muse != null) {
                    dataTransmission = !dataTransmission;
                    muse.enableDataTransmission(dataTransmission);
                }
                break;
        }
    }
    private void guardarIdUsuarioPreferences(){
        final CRestful restful = new CRestful(Principal.this, "consultar", "id_usuarios", "Usuarios", usuarioLogeado, ll_principal);
        restful.execute();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("idUsuario", restful.getDatoPeticion());
                editor.apply();
            }
        }, 2500);

    }
    private void guardarTelefonoPreferences(){
        final CRestful restful = new CRestful(Principal.this, "consultar", "telefono", "Usuarios", usuarioLogeado, ll_principal);
        restful.execute();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("telefono", restful.getDatoPeticion());
                editor.apply();
            }
        }, 1500);
    }
    private void guardarFrencuenciasPreferences(){
        final CRestful restful = new CRestful("consultarDatos", 1, ll_principal);
        restful.execute();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                //Banderas en False por si no tiene registros, cambian a true si los hay//
                editor.putBoolean("FlagBB", false); //Bandera para bajar brillo//
                editor.putBoolean("FlagSB", false); //Bandera para subir brillo//
                editor.putBoolean("FlagBV", false); //Bandera para bajar volumen//
                editor.putBoolean("FlagSV", false); //Bandera para subir volumen//
                editor.apply();
                deserializarJson(restful.getRegistrosRespuesta());
            }
        }, 3500);
    }
    private void ensurePermissions() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            DialogInterface.OnClickListener buttonListener =
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which){
                            dialog.dismiss();
                            ActivityCompat.requestPermissions(Principal.this,
                                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                    0);
                        }
                    };
            AlertDialog introDialog = new AlertDialog.Builder(this)
                    .setTitle(R.string.permission_dialog_title)
                    .setMessage(R.string.permission_dialog_description)
                    .setPositiveButton(R.string.permission_dialog_understand, buttonListener)
                    .create();
            introDialog.show();
        }
    }
    public void museListChanged() {
        final List<Muse> list = manager.getMuses();
        spinnerAdapter.clear();
        for (Muse m : list) {
            spinnerAdapter.add(m.getName() + " - " + m.getMacAddress());
        }
    }
    public void receiveMuseConnectionPacket(final MuseConnectionPacket p, final Muse muse) {

        final ConnectionState current = p.getCurrentConnectionState();

        final String status = p.getPreviousConnectionState() + " -> " + current;
        Log.i(TAG, status);

        handler.post(new Runnable() {
            @Override
            public void run() {

                final TextView statusText = findViewById(R.id.con_status);
                statusText.setText(status);

                final MuseVersion museVersion = muse.getMuseVersion();
                final TextView museVersionText = findViewById(R.id.version);
                if (museVersion != null)
                {
                    final String version = museVersion.getFirmwareType() + " - "
                            + museVersion.getFirmwareVersion() + " - "
                            + museVersion.getProtocolVersion();
                    museVersionText.setText(version);
                }
                else
                {
                    museVersionText.setText(R.string.undefined);
                }
            }
        });
        if (current == ConnectionState.DISCONNECTED) {
            Log.i(TAG, "Muse disconnected:" + muse.getName());
            saveFile();
            this.muse = null;
        }
    }
    public void receiveMuseDataPacket(final MuseDataPacket p, final Muse muse) {
        writeDataPacketToFile(p);

        final long n = p.valuesSize();
        switch (p.packetType()) {
            case EEG:
                assert(eegBuffer.length >= n);
                getEegChannelValues(eegBuffer,p);
                eegStale = true;
                break;
            case ACCELEROMETER:
                assert(accelBuffer.length >= n);
                getAccelValues(p);
                accelStale = true;
                break;
            case ALPHA_RELATIVE:
                assert(alphaBuffer.length >= n);
                getEegChannelValues(alphaBuffer,p);
                alphaStale = true;
                break;
        }
    }
    public void receiveMuseArtifactPacket(final MuseArtifactPacket p, final Muse muse) {
    }
    private void getEegChannelValues(double[] buffer, MuseDataPacket p) {
        buffer[0] = p.getEegChannelValue(Eeg.EEG1);
        buffer[1] = p.getEegChannelValue(Eeg.EEG2);
        buffer[2] = p.getEegChannelValue(Eeg.EEG3);
        buffer[3] = p.getEegChannelValue(Eeg.EEG4);
        buffer[4] = p.getEegChannelValue(Eeg.AUX_LEFT);
        buffer[5] = p.getEegChannelValue(Eeg.AUX_RIGHT);
    }

    private void getAccelValues(MuseDataPacket p) {
        accelBuffer[0] = p.getAccelerometerValue(Accelerometer.X);
        accelBuffer[1] = p.getAccelerometerValue(Accelerometer.Y);
        accelBuffer[2] = p.getAccelerometerValue(Accelerometer.Z);
    }
    private final Runnable tickUi = new Runnable() {
        @Override
        public void run() {
            if (eegStale) {
                updateEeg();
            }
            if (accelStale) {
                updateAccel();
            }
            if (alphaStale) {
                updateAlpha();
            }
            handler.postDelayed(tickUi, 1000 / 60);
        }
    };
    private void updateAccel() {
        TextView acc_x = findViewById(R.id.acc_x);
        TextView acc_y = findViewById(R.id.acc_y);
        TextView acc_z = findViewById(R.id.acc_z);
        acc_x.setText(String.format("%6.2f", accelBuffer[0]));
        acc_y.setText(String.format("%6.2f", accelBuffer[1]));
        acc_z.setText(String.format("%6.2f", accelBuffer[2]));
    }

    private void updateEeg() {
        TextView tp9 = findViewById(R.id.eeg_tp9);
        TextView fp1 = findViewById(R.id.eeg_af7);
        TextView fp2 = findViewById(R.id.eeg_af8);
        TextView tp10 = findViewById(R.id.eeg_tp10);
        tp9.setText(String.format("%6.2f", eegBuffer[0]));
        fp1.setText(String.format("%6.2f", eegBuffer[1]));
        fp2.setText(String.format("%6.2f", eegBuffer[2]));
        tp10.setText(String.format("%6.2f", eegBuffer[3]));
    }

    private void updateAlpha() {
        TextView elem1 = findViewById(R.id.elem1);
        elem1.setText(String.format("%6.2f", alphaBuffer[0]));
        TextView elem2 = findViewById(R.id.elem2);
        elem2.setText(String.format("%6.2f", alphaBuffer[1]));
        TextView elem3 = findViewById(R.id.elem3);
        elem3.setText(String.format("%6.2f", alphaBuffer[2]));
        TextView elem4 = findViewById(R.id.elem4);
        elem4.setText(String.format("%6.2f", alphaBuffer[3]));
    }
    private final Thread fileThread = new Thread() {
        @Override
        public void run() {
            Looper.prepare();
            fileHandler.set(new Handler());
            final File dir = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
            final File file = new File(dir, "new_muse_file.muse" );
            if (file.exists()) {
                file.delete();
            }
            Log.i(TAG, "Writing data to: " + file.getAbsolutePath());
            fileWriter.set(MuseFileFactory.getMuseFileWriter(file));
            Looper.loop();
        }
    };
    private void writeDataPacketToFile(final MuseDataPacket p) {
        Handler h = fileHandler.get();
        if (h != null) {
            h.post(new Runnable() {
                @Override
                public void run() {
                    fileWriter.get().addDataPacket(0, p);
                }
            });
        }
    }
    private void saveFile() {
        Handler h = fileHandler.get();
        if (h != null) {
            h.post(new Runnable() {
                @Override public void run() {
                    MuseFileWriter w = fileWriter.get();
                    w.addAnnotationString(0, "Disconnected");
                    w.flush();
                    w.close();
                }
            });
        }
    }
    private void playMuseFile(String name) {

        File dir = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(dir, name);

        final String tag = "Muse File Reader";

        if (!file.exists()) {
            Log.w(tag, "file doesn't exist");
            return;
        }

        MuseFileReader fileReader = MuseFileFactory.getMuseFileReader(file);
        Result res = fileReader.gotoNextMessage();
        while (res.getLevel() == ResultLevel.R_INFO && !res.getInfo().contains("EOF")) {

            MessageType type = fileReader.getMessageType();
            int id = fileReader.getMessageId();
            long timestamp = fileReader.getMessageTimestamp();

            Log.i(tag, "type: " + type.toString() +
                    " id: " + Integer.toString(id) +
                    " timestamp: " + String.valueOf(timestamp));

            switch(type)
            {
                case EEG:
                case BATTERY:
                case ACCELEROMETER:
                case QUANTIZATION:
                case GYRO:
                case MUSE_ELEMENTS:
                    MuseDataPacket packet = fileReader.getDataPacket();
                    Log.i(tag, "data packet: " + packet.packetType().toString());
                    break;
                case VERSION:
                    MuseVersion version = fileReader.getVersion();
                    Log.i(tag, "version" + version.getFirmwareType());
                    break;
                case CONFIGURATION:
                    MuseConfiguration config = fileReader.getConfiguration();
                    Log.i(tag, "config" + config.getBluetoothMac());
                    break;
                case ANNOTATION:
                    AnnotationData annotation = fileReader.getAnnotation();
                    Log.i(tag, "annotation" + annotation.getData());
                    break;
                default:
                    break;
            }
            res = fileReader.gotoNextMessage();
        }
    }
    private  void deserializarJson(String json){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Log.i("respuesta", json);
        for (int i = 1; i < json.length() -1 ; i++)
        {
            jsonAux = jsonAux + json.charAt(i);
        }
        Log.i("dato", json + " " +json.length());
        jsonAux = jsonAux.replace("},", "};");
        String [] arreglo = json.split(";");
        for (int i = 0; i <arreglo.length ; i++) {
            Gson gson = new GsonBuilder().create();
            Registro registro = gson.fromJson(arreglo[i], Registro.class);
            String accion = registro.getRespuesta().getId_accion();
            float frecuencia = (Float.parseFloat(registro.getRespuesta().getDato1()) + Float.parseFloat(registro.getRespuesta().getDato2()) + Float.parseFloat(registro.getRespuesta().getDato3())) / 3;
            if( accion.equals("1"))
            {
                //Bandera para subir brillo//
                editor.putBoolean("FlagSB", true);
                editor.putFloat("accionSB", frecuencia);
            }
            else if(accion.equals("2"))
            {
                //Bandera para bajar brillo//
                editor.putBoolean("FlagBB", true);
                editor.putFloat("accionBB", frecuencia);

            }
            else if(accion.equals("3"))
            {
                //Bandera para bajar brillo//
                editor.putBoolean("FlagSV", true);
                editor.putFloat("accionSV", frecuencia);

            }
            else if(accion.equals("4"))
            {
                //Bandera para bajar brillo//
                editor.putBoolean("FlagBV", true);
                editor.putFloat("accionBV", frecuencia);

            }

        }

    }
}

class MuseL extends MuseListener {
    final WeakReference<Principal> activityRef;

    MuseL(final WeakReference<Principal> activityRef) {
        this.activityRef = activityRef;
    }

    @Override
    public void museListChanged() {
        activityRef.get().museListChanged();
    }
}

class ConnectionListener extends MuseConnectionListener {
    final WeakReference<Principal> activityRef;

    ConnectionListener(final WeakReference<Principal> activityRef) {
        this.activityRef = activityRef;
    }

    @Override
    public void receiveMuseConnectionPacket(final MuseConnectionPacket p, final Muse muse) {
        activityRef.get().receiveMuseConnectionPacket(p, muse);
    }
}

class DataListener extends MuseDataListener {
    final WeakReference<Principal> activityRef;

    DataListener(final WeakReference<Principal> activityRef) {
        this.activityRef = activityRef;
    }

    @Override
    public void receiveMuseDataPacket(final MuseDataPacket p, final Muse muse) {
        activityRef.get().receiveMuseDataPacket(p, muse);
    }

    @Override
    public void receiveMuseArtifactPacket(final MuseArtifactPacket p, final Muse muse) {
        activityRef.get().receiveMuseArtifactPacket(p, muse);
    }
}
