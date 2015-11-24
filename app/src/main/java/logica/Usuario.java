package logica;

/**
 * Created by Usuario on 19/10/2015.
 */
public class Usuario {
    private String identificador;
    private String nombre;
    private String apellido;
    private String estado;
    private String pertenencia;
    private String correo;
    private String municipio;
    private String cargo;
    private Integer rol;

    public Usuario(){}

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEstado() {
        return this.estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getPetenencia() {
        return this.pertenencia;
    }

    public  void setPertenencia(String pertenencia) {
        this.pertenencia = pertenencia;
    }

    public String getCorreo() {
        return this.correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Integer getRol() {
        return this.rol;
    }

    public void setRol(Integer rol) {
        this.rol = rol;
    }

    public String getRolName() {
        if(this.rol == 0)
            return "Activista";
        else
            return "Registrante";
    }
}
