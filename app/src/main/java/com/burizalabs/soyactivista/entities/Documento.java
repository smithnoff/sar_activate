package com.burizalabs.soyactivista.entities;

import android.graphics.Bitmap;

import com.parse.ParseFile;

/**
 * Created by Luis Adrian on 24/02/2016.
 */
public class Documento {

    private String descripcion;
    private String titulo;
    private ParseFile documento;

    public Documento() {

    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public ParseFile getDocumento() {
        return documento;
    }

    public void setDocumento(ParseFile documento) {
        this.documento = documento;
    }
}

