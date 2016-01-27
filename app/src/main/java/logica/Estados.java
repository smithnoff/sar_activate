package logica;

/**
 * Created by Luis Adrian on 26/01/2016.
 */
public class Estados {
    private String nombreEstado;
    private int Posicion;

    public Estados(String nombreEstado, int posicion) {
        this.nombreEstado = nombreEstado;
        Posicion = posicion;
    }

    public String getNombreEstado() {
        return nombreEstado;
    }

    public void setNombreEstado(String nombreEstado) {
        this.nombreEstado = nombreEstado;
    }

    public int getPosicion() {
        return Posicion;
    }

    public void setPosicion(int posicion) {
        Posicion = posicion;
    }
}
