package logica;

import java.util.Date;

/**
 * Created by Luis Adrian on 16/12/2015.
 */
public class Conversacion implements Comparable<Conversacion> {

    public Usuario usuario;
    private String id;
    private Date ultimaActividad;


    public Conversacion(){
        this.usuario = new Usuario();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getUltimaActividad() {
        return ultimaActividad;
    }

    public void setUltimaActividad(Date ultimaActividad) {
        this.ultimaActividad = ultimaActividad;
    }


    @Override
    public int compareTo(Conversacion another) {
        if(this.ultimaActividad.before(another.getUltimaActividad()))
            return 1;
        else if(this.ultimaActividad.after(another.getUltimaActividad()))
            return -1;
        else
            return 0;
    }
}