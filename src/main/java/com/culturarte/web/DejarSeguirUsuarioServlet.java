package com.culturarte.web;

import com.culturarte.web.ws.cliente.ISeguimientoController;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/dejarSeguirUsuario")
public class DejarSeguirUsuarioServlet extends HttpServlet {

    private ISeguimientoController segCtrl;

    @Override
    public void init() throws ServletException {
        ServletContext context = getServletContext();
        this.segCtrl = (ISeguimientoController) context.getAttribute("ws.seguimiento");

        if (this.segCtrl == null) {
            throw new ServletException("¡Error crítico! El cliente de Seguimiento (ws.seguimiento) no se pudo cargar desde ClienteInit.");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession sesion = req.getSession(false);
        if (sesion == null || sesion.getAttribute("nick") == null) {
            resp.sendRedirect("inicioSesion.jsp");
            return;
        }

        String nickActual = (String) sesion.getAttribute("nick");

        try {
            List<String> usuariosSeguidos = segCtrl.listarSeguidosDeNick(nickActual);
            req.setAttribute("usuariosSeguidos", usuariosSeguidos);
        } catch (Exception e) {
            req.setAttribute("error", "Error al cargar seguidos: " + e.getMessage());
        }

        req.getRequestDispatcher("/dejarSeguirUsuario.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession sesion = req.getSession(false);
        if (sesion == null || sesion.getAttribute("nick") == null) {
            resp.sendRedirect("inicioSesion.jsp");
            return;
        }

        String nickSeguidor = (String) sesion.getAttribute("nick");
        String nickSeguido = req.getParameter("usuarioSeguido");
        String esAjax = req.getParameter("ajax");

        if (nickSeguido == null || nickSeguido.isEmpty()) {
            if ("true".equals(esAjax)) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("Usuario a seguir no puede ser nulo.");
            } else {
                req.setAttribute("mensajeError", "Debe seleccionar un usuario a dejar de seguir.");
                doGet(req, resp);
            }
            return;
        }

        try {
            int idSegui = segCtrl.conseguirId(nickSeguidor, nickSeguido);
            if (idSegui > 0) {
                segCtrl.cancelarSeguimiento(idSegui);
                req.setAttribute("mensajeExito", "Has dejado de seguir a " + nickSeguido + ".");
            } else {
                req.setAttribute("mensajeError", "No se encontró seguimiento para cancelar.");
            }
        } catch (Exception e) {
            req.setAttribute("mensajeError", "Error al intentar dejar de seguir: " + e.getMessage());

            if ("true".equals(esAjax)) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write(e.getMessage());
                return;
            }
        }

        if ("true".equals(esAjax)) {
            resp.setStatus(HttpServletResponse.SC_OK);
        } else {
            String tipoUsr = req.getParameter("tipoUsr");
            resp.sendRedirect("consultaPerfil?nick=" + nickSeguido + "&tipoUsr=" + tipoUsr);
        }
    }
}
