package logica;

public class Usuario {
    private String identificador;
    private String nombre;
    private String apellido;
    private String cargo;
    private Integer rol;
    private String estado;
    private String muncipio;
    private String comite;
    private String correo;



    // Class Constructor
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

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getComite() {
        return this.comite;
    }

    public  void setComite(String comite) {
        this.comite = comite;
    }

    public String getCorreo() {
        return this.correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getMuncipio() {
        return muncipio;
    }

    public void setMuncipio(String muncipio) {
        this.muncipio = muncipio;
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
