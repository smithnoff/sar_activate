package logica;

/**
 * Created by Luis Adrian on 26/01/2016.
 */
public class Entidad {
    private String id;
    private String nombre;
    private int posicion;
    private int puntos;
    private int usuarios;


    public Entidad(){}

    public int getPosicion() {
        return posicion;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    public int getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(int usuarios) {
        this.usuarios = usuarios;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
