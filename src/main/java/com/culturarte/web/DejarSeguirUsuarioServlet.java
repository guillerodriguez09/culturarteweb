package com.culturarte.web;

import com.culturarte.logica.dtos.DTOSeguimiento;
import com.culturarte.logica.dtos.DTOColaborador;
import com.culturarte.logica.dtos.DTOProponente;
import com.culturarte.logica.clases.Usuario;
import com.culturarte.logica.controllers.IColaboradorController;
import com.culturarte.logica.controllers.IProponenteController;
import com.culturarte.logica.controllers.ISeguimientoController;
import com.culturarte.logica.fabrica.Fabrica;
import com.culturarte.web.fabrica.FabricaWeb;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/dejarSeguirUsuario")
public class DejarSeguirUsuarioServlet extends HttpServlet {

    private final ISeguimientoController segCtrl = FabricaWeb.getInstancia().getSeguimientoController();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession sesion = req.getSession(false);
        if (sesion == null || sesion.getAttribute("nick") == null) {
            resp.sendRedirect("inicioSesion.jsp");
            return;
        }

        String nickActual = (String) sesion.getAttribute("nick");

        List<String> usuariosSeguidos = segCtrl.listarSeguidosDeNick(nickActual);

        req.setAttribute("usuariosSeguidos", usuariosSeguidos);
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
        //revisa q sea ajax
        String esAjax = req.getParameter("ajax");

        if (nickSeguido == null || nickSeguido.isEmpty()) {
            if ("true".equals(esAjax)) {
                // Si es ajax, responde con error
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400 Bad Request
                resp.getWriter().write("Usuario a seguir no puede ser nulo.");
            } else {
                // comportamiento normal (no ajax)
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
            // obtiene el tipo de usuario
            String tipoUsr = req.getParameter("tipoUsr");
            // eedirige de vuelta al perfil correcto
            resp.sendRedirect("consultaPerfil?nick=" + nickSeguido + "&tipoUsr=" + tipoUsr);
        }
    }
}
