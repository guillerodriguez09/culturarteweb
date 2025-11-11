package com.culturarte.web;

import com.culturarte.web.ws.cliente.IProponenteController;
import com.culturarte.web.ws.cliente.LocalDate;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/bajaProponente")
public class BajaProponenteServlet extends HttpServlet {

    private IProponenteController propController;

    @Override
    public void init() throws ServletException {
        ServletContext context = getServletContext();
        this.propController = (IProponenteController) context.getAttribute("ws.proponente");

        if (this.propController == null) {
            throw new ServletException("¡Error crítico! El cliente de Proponente (ws.proponente) no se pudo cargar desde ClienteInit.");
        }
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession sesion = req.getSession(false);
        if (sesion == null || sesion.getAttribute("nick") == null) {
            resp.sendRedirect("inicioSesion.jsp");
            return;
        }

        String nickProponente = (String) sesion.getAttribute("nick");
        String esAjax = req.getParameter("ajax");

        if (nickProponente == null || nickProponente.isEmpty()) {
            if ("true".equals(esAjax)) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("Proponente a dar de baja no puede ser nulo.");
            } else {
                req.setAttribute("mensajeError", "Debe seleccionar un proponente a dar de baja.");
                doGet(req, resp);
            }
            return;
        }

        try {
                propController.eliminarProponente(nickProponente);
                sesion.invalidate();
                req.setAttribute("mensajeExito", "Se dio de baja al proponente " + nickProponente + ".");

                req.setAttribute("mensajeError", "No se encontro proponente para dar de baja.");

        } catch (Exception e) {
            req.setAttribute("mensajeError", "Error al intentar dar de baja: " + e.getMessage());

            if ("true".equals(esAjax)) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write(e.getMessage());
                return;
            }
        }

        if ("true".equals(esAjax)) {
            resp.setStatus(HttpServletResponse.SC_OK);
        } else {
            req.getRequestDispatcher("/consultaPerfil").forward(req, resp);
        }

    }

}
