package logica;

import android.graphics.Bitmap;

/**
 * Created by Usuario on 29/10/2015.
 */
public class Mensaje {
    private Usuario autor;
    private String texto;
    private Bitmap adjunto;
    private GeoLocalizacion localizacion;
    private String horaPublicacion;
    private String fechaPublicacion;

    // Class COnstructors
    public Mensaje(){}

    public Mensaje(Usuario autor, String texto, Bitmap adjunto/*, float latitud, float longitud, String horaPublicacion, String fechaPublicacion*/) {
        this.autor = autor;
        this.texto = texto;
        this.adjunto = adjunto;
        /*this.latitud = latitud;
        this.longitud = longitud;
        this.horaPublicacion = horaPublicacion;
        this.fechaPublicacion = fechaPublicacion;*/
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getHoraPublicacion() {
        return horaPublicacion;
    }

    public void setHoraPublicacion(String horaPublicacion) {
        this.horaPublicacion = horaPublicacion;
    }

    public String getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(String fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public Usuario getAutor() {
        return autor;
    }

    public void setAutor(Usuario autor) {
        this.autor = autor;
    }

    public Bitmap getAdjunto() {
        return adjunto;
    }

    public void setAdjunto(Bitmap adjunto) {
        this.adjunto = adjunto;
    }

    public void setLocalizacion(GeoLocalizacion localizacion){
        this.localizacion = localizacion;
    }

    public GeoLocalizacion getLocalizacion(){
        return this.localizacion;
    }

}
