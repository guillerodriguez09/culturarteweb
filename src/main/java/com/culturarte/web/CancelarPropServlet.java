package com.culturarte.web;

import com.culturarte.logica.controllers.IPropuestaController;
import com.culturarte.logica.fabrica.Fabrica;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/cancelarPropuesta")
public class CancelarPropServlet extends HttpServlet {

    private final IPropuestaController propCtrl = Fabrica.getInstancia().getPropuestaController();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("tipoUsuario") == null) {
            req.setAttribute("error", "Debes iniciar sesión como proponente para cancelar una propuesta.");
            req.getRequestDispatcher("/consultarPropuesta").forward(req, resp);
            return;
        }

        String tipoUsuario = (String) session.getAttribute("tipoUsuario");
        String titulo = req.getParameter("titulo");

        if (!"PROPONENTE".equals(tipoUsuario)) {
            req.setAttribute("error", "Solo un proponente puede cancelar propuestas.");
            req.getRequestDispatcher("/consultarPropuesta").forward(req, resp);
            return;
        }

        try {
            propCtrl.cancelarPropuesta(titulo);
            req.setAttribute("mensaje", "La propuesta '" + titulo + "' fue cancelada exitosamente.");
        } catch (Exception e) {
            req.setAttribute("error", e.getMessage());
        }

        // Redirige al listado
        req.getRequestDispatcher("/consultarPropuesta").forward(req, resp);
    }
}
