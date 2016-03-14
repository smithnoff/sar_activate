package com.burizalabs.soyactivista.entities;

import android.graphics.Bitmap;

import java.util.Date;

/**
 * Created by Joisacris on 25/11/2015.
 */
public class Actividad {
    private String objectId;
    private String nombre;
    private String puntaje;
    private String descripcion;
    private String objetivo;
    private String ubicacion;
    private String municipio;
    private String parroquia;
    private String encargado;
    private String creador;
    private String estatus;
    private Date inicio;
    private Date fin;
    private Bitmap imagen1;
    private Bitmap imagen2;
    private Bitmap imagen3;
    private Bitmap imagen4;

    public Actividad(){ }

    public Actividad(String nombre, String estatus, String creador, Date inicio, Date fin){
        this.nombre = nombre;
        this.estatus = estatus;
        this.creador = creador;
        this.inicio = inicio;
        this.fin = fin;

    }

    public String getObjectId() { return objectId; }

    public void setObjectId(String objectId) { this.objectId = objectId; }

    public String getNombre() { return nombre; }

    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getPuntaje() { return puntaje; }

    public void setPuntaje(String puntaje) { this.puntaje = puntaje; }

    public String getDescripcion() { return descripcion; }

    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getObjetivo() { return objetivo; }

    public void setObjetivo(String objetivo) { this.objetivo = objetivo; }

    public String getUbicacion() { return ubicacion; }

    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }

    public String getMunicipio() { return municipio; }

    public void setMunicipio(String municipio) { this.municipio = municipio; }

    public String getParroquia() { return parroquia;}

    public void setParroquia(String parroquia) { this.parroquia = parroquia; }

    public String getEncargado() { return encargado; }

    public void setEncargado(String encargado) { this.encargado = encargado; }

    public String getCreador() { return creador; }

    public void setCreador(String creador) { this.creador = creador; }

    public String getEstatus() { return estatus; }

    public void setEstatus(String estatus) { this.estatus = estatus; }

    public Date getInicio() { return inicio; }

    public void setInicio(Date inicio) { this.inicio = inicio; }

    public Date getFin() { return fin; }

    public void setFin(Date objectId) { this.fin = fin; }

    public Bitmap getImagen1() { return imagen1; }

    public void setImagen1(Bitmap imagen1) { this.imagen1 = imagen1; }

    public Bitmap getImagen2() { return imagen2; }

    public void setImagen2(Bitmap imagen2) { this.imagen2 = imagen2; }

    public Bitmap getImagen3() { return imagen3; }

    public void setImagen3(Bitmap imagen3) { this.imagen3 = imagen3; }

    public Bitmap getImagen4() {
        return imagen4;
    }

    public void setImagen4(Bitmap imagen4) { this.imagen4 = imagen4; }

}