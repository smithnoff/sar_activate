package logica;

/**
 * Created by Brahyam on 29/11/2015.
 */
public class TipoActividad {

    private String nombre;
    private Integer puntaje;
    private String descripcion;
    private Usuario creador;

    public TipoActividad (){}

    public TipoActividad (String nombre, Integer puntaje, String descripcion){
        this.nombre = nombre;
        this.puntaje = puntaje;
        this.descripcion = descripcion;
    }

    public TipoActividad (String nombre, Integer puntaje, String descripcion, Usuario creador){
        this.nombre = nombre;
        this.puntaje = puntaje;
        this.descripcion = descripcion;
        this.creador = creador;
    }

    // GETTERS

    public String getNombre() {
        return nombre;
    }

    public Integer getPuntaje() {
        return puntaje;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Usuario getCreador() {
        return creador;
    }

    // SETTERS

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setPuntaje(Integer puntaje) {
        this.puntaje = puntaje;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setCreador(Usuario creador) {
        this.creador = creador;
    }

}
