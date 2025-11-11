package com.culturarte.web.filtros;

import com.culturarte.web.ws.cliente.DtoAcceso;
import com.culturarte.web.ws.cliente.IAccesoController;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Filtro que registra automáticamente cada acceso al sitio web.
 * Envía la información al WS AccesoController del Servidor Central
 */

@WebFilter("/*")
public class AccesoFiltro implements Filter {

    private IAccesoController accesoCtrl;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        ServletContext context = filterConfig.getServletContext();
        this.accesoCtrl = (IAccesoController) context.getAttribute("ws.acceso");

        if (this.accesoCtrl == null) {
            throw new ServletException("El cliente WS 'ws.acceso' no fue inicializado en ClienteWS.");
        }

        System.out.println("AccesoFiltro inicializado.");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;

        try {
            String ip = req.getRemoteAddr();
            String url = req.getRequestURL().toString();
            String browser = req.getHeader("User-Agent");
            String so = System.getProperty("os.name");

            DtoAcceso dto = new DtoAcceso();
            dto.setIp(ip);
            dto.setUrl(url);
            dto.setBrowser(browser);
            dto.setSo(so);
            dto.setFecha(String.valueOf(LocalDateTime.now()));

            // Llama al WS remoto
            accesoCtrl.registrarAcceso(dto);
            System.out.println("Acceso registrado: " + ip + " → " + url);

        } catch (Exception e) {
            System.err.println("Error registrando acceso: " + e.getMessage());
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        System.out.println(" AccesoFiltro destruido.");
    }
}

