package cornejo.luis.bci.Clases.DeserializarJson;

public class Respuesta {

    public String id_lectura;
    public String id_usuario;
    public String id_accion;
    public String dato1;
    public String dato2;
    public String dato3;

    public Respuesta() { }

    public Respuesta(String id_lectura, String id_usuario, String id_accion, String dato1, String dato2, String dato3) {
        this.id_lectura = id_lectura;
        this.id_usuario = id_usuario;
        this.id_accion = id_accion;
        this.dato1 = dato1;
        this.dato2 = dato2;
        this.dato3 = dato3;
    }

    public String getId_lectura() {
        return id_lectura;
    }

    public void setId_lectura(String id_lectura) {
        this.id_lectura = id_lectura;
    }

    public String getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(String id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getId_accion() {
        return id_accion;
    }

    public void setId_accion(String id_accion) {
        this.id_accion = id_accion;
    }

    public String getDato1() {
        return dato1;
    }

    public void setDato1(String dato1) {
        this.dato1 = dato1;
    }

    public String getDato2() {
        return dato2;
    }

    public void setDato2(String dato2) {
        this.dato2 = dato2;
    }

    public String getDato3() {
        return dato3;
    }

    public void setDato3(String dato3) {
        this.dato3 = dato3;
    }
}
