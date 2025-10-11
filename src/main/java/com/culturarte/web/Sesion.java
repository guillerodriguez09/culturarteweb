package com.culturarte.web;

public class Sesion {

    String nickOMail;
    String contrasenia;
    String tipoUsuario;

    public Sesion(){}

    public Sesion(String nickOMail, String contrasenia, String tipoUsuario) {
        this.nickOMail = nickOMail;
        this.contrasenia = contrasenia;
        this.tipoUsuario = tipoUsuario;
    }

    public String getNickOMail() {
        return nickOMail;
    }

    public void setNickOMail(String nickOMail) {
        this.nickOMail = nickOMail;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public String getTipoUsuario() { return tipoUsuario; }

    public void setTipoUsuario(String tipoUsuario){ this.tipoUsuario = tipoUsuario; }
}
