package logica;

/**
 * Created by Brahyam on 27/11/2015.
 */
public class GeoLocalizacion {

    private double latitud;
    private double longitud;

    public GeoLocalizacion(){}

    public void setLatitud(Double latitud){
        this.latitud = latitud;
    }

    public Double getLatitud(){
        return this.latitud;
    }

    public void setLongitud(Double longitud){
        this.longitud = longitud;
    }

    public Double getLongitud(){
        return this.longitud;
    }

}
