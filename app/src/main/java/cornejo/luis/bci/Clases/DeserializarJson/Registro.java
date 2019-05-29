package cornejo.luis.bci.Clases.DeserializarJson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Registro {

    private Respuesta respuesta;

    public Registro(){}

    public Registro(Respuesta respuesta) {
        this.respuesta = respuesta;
    }

    public Respuesta getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(Respuesta respuesta) {
        this.respuesta = respuesta;
    }

    public static Respuesta parseJSON(String response){
        Gson gson = new GsonBuilder().create();
        Respuesta respuesta = gson.fromJson(response, Respuesta.class);

        return  respuesta;
    }
}
