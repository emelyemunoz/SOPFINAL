package com.so.pedidos.model;

public class CorreoBuzon {

    private int id;
    private String remitente;
    private String asunto;
    private String fecha;
    private String contenido;

    public CorreoBuzon() {
    }

    public CorreoBuzon(
            int id,
            String remitente,
            String asunto,
            String fecha,
            String contenido) {

        this.id = id;
        this.remitente = remitente;
        this.asunto = asunto;
        this.fecha = fecha;
        this.contenido = contenido;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRemitente() {
        return remitente;
    }

    public void setRemitente(String remitente) {
        this.remitente = remitente;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }
}