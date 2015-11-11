package logica;

import android.graphics.Bitmap;

/**
 * Created by Usuario on 29/10/2015.
 */
public class Mensaje {
    private Usuario usuario;
    private String mensaje;
    private Bitmap imagen;
    private float latitud;
    private float longitud;
    private String horaPublicacion;
    private String fechaPublicacion;

    public String getHoraPublicacion() {
        return horaPublicacion;
    }

    public void setHoraPublicacion(String horaPublicacion) {
        this.horaPublicacion = horaPublicacion;
    }

    public Mensaje(Usuario usuario, String mensaje, Bitmap imagen/*, float latitud, float longitud, String horaPublicacion, String fechaPublicacion*/) {
        this.usuario = usuario;
        this.mensaje = mensaje;
        this.imagen = imagen;
        /*this.latitud = latitud;
        this.longitud = longitud;
        this.horaPublicacion = horaPublicacion;
        this.fechaPublicacion = fechaPublicacion;*/
    }

    public String getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(String fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public Mensaje(){

    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Bitmap getImagen() {
        return imagen;
    }

    public void setImagen(Bitmap imagen) {
        this.imagen = imagen;
    }

    public float getLatitud() {
        return latitud;
    }

    public void setLatitud(float latitud) {
        this.latitud = latitud;
    }

    public float getLongitud() {
        return longitud;
    }

    public void setLongitud(float longitud) {
        this.longitud = longitud;
    }
}
