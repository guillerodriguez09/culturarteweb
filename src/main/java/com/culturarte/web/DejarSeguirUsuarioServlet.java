package com.culturarte.web;

import com.culturarte.web.ws.cliente.DtoPropuesta;
import com.culturarte.web.ws.cliente.IPropuestaController;
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
            throw new ServletException("No se cargó ws.seguimiento");
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
            List<String> seguidos = segCtrl.listarSeguidosDeNick(nickActual);
            req.setAttribute("usuariosSeguidos", seguidos);
        } catch (Exception e) {
            req.setAttribute("error", "Error cargando seguidos: " + e.getMessage());
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
        String tipoUsr = req.getParameter("tipoUsr");

        try {
            int idSegui = segCtrl.conseguirId(nickSeguidor, nickSeguido);

            if (idSegui > 0) {
                segCtrl.cancelarSeguimiento(idSegui);

                IPropuestaController propCtrl =
                        (IPropuestaController) getServletContext().getAttribute("ws.propuesta");

                List<DtoPropuesta> nuevas = propCtrl.recomendarPropuestas(nickSeguidor);
                sesion.setAttribute("recomendaciones", nuevas);
            }

        } catch (Exception e) {
            req.setAttribute("mensajeError", "Error al dejar de seguir: " + e.getMessage());
        }

        resp.sendRedirect("consultaPerfil?nick=" + nickSeguido +
                "&tipoUsr=" + (tipoUsr != null ? tipoUsr : "proponente"));
    }
}

