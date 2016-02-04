package logica;

/**
 * Created by Luis Adrian on 26/01/2016.
 */
public class Entidades {
    private String nombreEntidad;
    private int Posicion;
    private int puntos;
    private int cantidadUsuarios;
    private String idEntidad;

    public Entidades(){}

    public String getnombreEntidad() {
        return nombreEntidad;
    }

    public void setnombreEntidad(String nombreEntidad) {
        this.nombreEntidad = nombreEntidad;
    }

    public int getPosicion() {
        return Posicion;
    }

    public void setPosicion(int posicion) {
        Posicion = posicion;
    }

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    public int getCantidadUsuarios() {
        return cantidadUsuarios;
    }

    public void setCantidadUsuarios(int cantidadUsuarios) {
        this.cantidadUsuarios = cantidadUsuarios;
    }

    public String getNombreEntidad() {
        return nombreEntidad;
    }

    public void setNombreEntidad(String nombreEntidad) {
        this.nombreEntidad = nombreEntidad;
    }

    public String getIdEntidad() {
        return idEntidad;
    }

    public void setIdEntidad(String idEntidad) {
        this.idEntidad = idEntidad;
    }
}
