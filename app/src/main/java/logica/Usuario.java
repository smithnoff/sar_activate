package logica;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;


public class Usuario {

    private String id;
    private String username;
    private String nombre;
    private String apellido;
    private String cargo;
    private Integer rol;
    private String estado;
    private String municipio;
    private String parroquia;
    private String comite;
    private String email;



    // Class Constructor
    public Usuario(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getParroquia() {
        return parroquia;
    }

    public void setParroquia(String parroquia) {
        this.parroquia = parroquia;
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
