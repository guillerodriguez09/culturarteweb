package com.culturarte.web;

import com.culturarte.web.ws.cliente.*;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@WebServlet("/registrarColaboracion")
public class RegistrarColaboracionServlet extends HttpServlet {

    private IPropuestaController propuestaCtrl;
    private IColaboracionController colaboracionCtrl;

    @Override
    public void init() throws ServletException {
        ServletContext context = getServletContext();
        this.propuestaCtrl = (IPropuestaController) context.getAttribute("ws.propuesta");
        this.colaboracionCtrl = (IColaboracionController) context.getAttribute("ws.colaboracion");

        if (this.propuestaCtrl == null || this.colaboracionCtrl == null) {
            throw new ServletException("¡Error crítico! Los clientes de RegistrarColaboracionServlet no se pudieron cargar desde ClienteInit.");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        String tipoUsuario = (session != null) ? (String) session.getAttribute("tipoUsuario") : null;

        if (tipoUsuario == null || !tipoUsuario.equalsIgnoreCase("Colaborador")) {
            resp.sendRedirect(req.getContextPath() + "/inicioSesion.jsp?loginRequired=1");
            return;
        }

        String titulo = req.getParameter("titulo");
        if (titulo != null && !titulo.isEmpty()) {
            session.setAttribute("propuestaSeleccionadaTitulo", titulo);
        } else {
            titulo = (String) session.getAttribute("propuestaSeleccionadaTitulo");
        }

        if (titulo == null || titulo.isEmpty()) {
            req.setAttribute("error", "No se ha seleccionado ninguna propuesta. Vuelva a la pantalla de consulta para elegir una.");
            req.getRequestDispatcher("/consultarPropuesta.jsp").forward(req, resp);
            return;
        }


        req.setAttribute("publicadas", Collections.emptyList());
        req.setAttribute("enFinanciacion", Collections.emptyList());
        req.setAttribute("financiadas", Collections.emptyList());
        req.setAttribute("noFinanciadas", Collections.emptyList());
        req.setAttribute("canceladas", Collections.emptyList());

        try {
            DtoPropuesta propuesta = this.propuestaCtrl.consultarPropuesta(titulo);
            req.setAttribute("propuestaSeleccionada", propuesta);

            List<ETipoRetorno> tiposRetorno = Arrays.asList(ETipoRetorno.values());
            req.setAttribute("tiposRetorno", tiposRetorno);

        } catch (Exception e) {
            req.setAttribute("error", "Error al cargar propuesta: " + e.getMessage());
        }

        req.getRequestDispatcher("/consultarPropuesta.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession(false);
        String colaboradorNick = (session != null) ? (String) session.getAttribute("nick") : null;
        String tipoUsuario = (session != null) ? (String) session.getAttribute("tipoUsuario") : null;

        if (colaboradorNick == null || tipoUsuario == null || !tipoUsuario.equalsIgnoreCase("Colaborador")) {
            resp.sendRedirect(req.getContextPath() + "/inicioSesion.jsp?loginRequired=1");
            return;
        }

        try {
            String propuestaTitulo = req.getParameter("propuestaTitulo");
            if (propuestaTitulo == null || propuestaTitulo.isEmpty()) {
                propuestaTitulo = (String) session.getAttribute("propuestaSeleccionadaTitulo");
            }

            if (propuestaTitulo == null || propuestaTitulo.isEmpty()) {
                req.setAttribute("error", "No se ha seleccionado ninguna propuesta.");
                req.getRequestDispatcher("/consultarPropuesta.jsp").forward(req, resp);
                return;
            }

            String tipoRetornoStr = req.getParameter("tipoRetorno");
            String montoStr = req.getParameter("monto");


            ETipoRetorno tipoRetorno = ETipoRetorno.fromValue(tipoRetornoStr);
            Integer monto = Integer.parseInt(montoStr);


            String fechaStr = LocalDateTime.now().toString();


            DtoColaboracion dto = new DtoColaboracion();
            dto.setColaboradorNick(colaboradorNick);
            dto.setPropuestaTitulo(propuestaTitulo);
            dto.setRetorno(tipoRetorno);
            dto.setMonto(monto);
            dto.setFecha(fechaStr);


            this.colaboracionCtrl.registrarColaboracion(dto); // <- SE USA

            session.removeAttribute("propuestaSeleccionadaTitulo");
            resp.sendRedirect(req.getContextPath() + "/consultarPropuesta?titulo=" + propuestaTitulo + "&mensaje=Colaboracion registrada con exito");

        } catch (Exception e) {
            req.setAttribute("error", "Error al registrar colaboración: " + e.getMessage());
            doGet(req, resp); // Recarga la página con el error
        }
    }
