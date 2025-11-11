package com.culturarte.web;

import java.io.Serializable;
import java.time.LocalDateTime;

public class RegistroAcceso implements Serializable {
    private String ip;
    private String url;
    private String browser;
    private String so;
    private LocalDateTime fecha;

    public RegistroAcceso() {}

    public RegistroAcceso(String ip, String url, String browser, String so, LocalDateTime fecha) {
        this.ip = ip;
        this.url = url;
        this.browser = browser;
        this.so = so;
        this.fecha = fecha;
    }

    public String getIp() { return ip; }
    public void setIp(String ip) { this.ip = ip; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getBrowser() { return browser; }
    public void setBrowser(String browser) { this.browser = browser; }

    public String getSo() { return so; }
    public void setSo(String so) { this.so = so; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
}

