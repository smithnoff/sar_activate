package logica;

import android.graphics.Bitmap;

/**
 * Created by Luis Adrian on 16/12/2015.
 */
public class MensajesDirectos {
    private Usuario autor;
    private String texto;
    private Bitmap adjunto;
    private GeoLocalizacion localizacion;
    private Conversacion conversation;

    public MensajesDirectos(Usuario autor, String texto, Bitmap adjunto, GeoLocalizacion localizacion, Conversacion conversation) {
        this.autor = autor;
        this.texto = texto;
        this.adjunto = adjunto;
        this.localizacion = localizacion;
        this.conversation = conversation;
    }

    public MensajesDirectos(){

    }

    public Usuario getAutor() {
        return autor;
    }

    public void setAutor(Usuario autor) {
        this.autor = autor;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public Bitmap getAdjunto() {
        return adjunto;
    }

    public void setAdjunto(Bitmap adjunto) {
        this.adjunto = adjunto;
    }

    public GeoLocalizacion getLocalizacion() {
        return localizacion;
    }

    public void setLocalizacion(GeoLocalizacion localizacion) {
        this.localizacion = localizacion;
    }
}
