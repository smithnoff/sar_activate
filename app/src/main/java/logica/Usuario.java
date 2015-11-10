package logica;

/**
 * Created by Usuario on 19/10/2015.
 */
public class Usuario {
    private String nombre;
    private String Apellido;
    private String estado;
    private String identificador;
    private String Pertenencia;
    private String Correo;

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public Usuario() { }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return Apellido;
    }

    public void setApellido(String apellido) {
        Apellido = apellido;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getMuncipio() {
        return muncipio;
    }

    public void setMuncipio(String muncipio) {
        this.muncipio = muncipio;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getPetenencia() { return Pertenencia; }

    public  void setPertenencia(String pertenencia) { Pertenencia = pertenencia; }

    public String getCorreo() { return Correo; }

    public void setCorreo(String correo) { Correo = correo; }

    private String muncipio;
    private String cargo;

}
