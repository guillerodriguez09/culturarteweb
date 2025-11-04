package com.culturarte.web;

import com.culturarte.logica.controllers.IColaboracionController;
import com.culturarte.logica.controllers.IPropuestaController;
import com.culturarte.logica.dtos.DTOColaboracion;
import com.culturarte.logica.dtos.DTOPropuesta;
import com.culturarte.logica.enums.ETipoRetorno;
import com.culturarte.logica.fabrica.Fabrica;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@WebServlet("/registrarColaboracion")
public class RegistrarColaboracionServlet extends HttpServlet {

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

        IPropuestaController propuestaCtrl = Fabrica.getInstancia().getPropuestaController();

        req.setAttribute("publicadas", Collections.emptyList());
        req.setAttribute("enFinanciacion", Collections.emptyList());
        req.setAttribute("financiadas", Collections.emptyList());
        req.setAttribute("noFinanciadas", Collections.emptyList());
        req.setAttribute("canceladas", Collections.emptyList());

        DTOPropuesta propuesta = propuestaCtrl.consultarPropuesta(titulo);
        req.setAttribute("propuestaSeleccionada", propuesta);

        List<ETipoRetorno> tiposRetorno = Arrays.asList(ETipoRetorno.values());
        req.setAttribute("tiposRetorno", tiposRetorno);

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
                req.setAttribute("error", "No se ha seleccionado ninguna propuesta. Vuelva a la pantalla de consulta para elegir una.");
                req.getRequestDispatcher("/consultarPropuesta.jsp").forward(req, resp);
                return;
            }

            String tipoRetornoStr = req.getParameter("tipoRetorno");
            String montoStr = req.getParameter("monto");

            ETipoRetorno tipoRetorno = ETipoRetorno.valueOf(tipoRetornoStr);
            Integer monto = Integer.parseInt(montoStr);

            DTOColaboracion dto = new DTOColaboracion();
            dto.setColaboradorNick(colaboradorNick);
            dto.setPropuestaTitulo(propuestaTitulo);
            dto.setRetorno(tipoRetorno);
            dto.setMonto(monto);
            dto.setFecha(LocalDateTime.now());

            Fabrica.getInstancia().getColaboracionController().registrarColaboracion(dto);

            session.removeAttribute("propuestaSeleccionadaTitulo");

            resp.sendRedirect(req.getContextPath() + "/consultarPropuesta?titulo=" + propuestaTitulo + "&mensaje=Colaboracion registrada con exito");

        } catch (Exception e) {
            req.setAttribute("error", "Error al registrar colaboración: " + e.getMessage());
            doGet(req, resp);
        }
    }
}