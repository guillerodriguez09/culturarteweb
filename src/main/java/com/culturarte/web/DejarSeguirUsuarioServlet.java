package com.culturarte.web;

import com.culturarte.logica.dtos.DTOSeguimiento;
import com.culturarte.logica.dtos.DTOColaborador;
import com.culturarte.logica.dtos.DTOProponente;
import com.culturarte.logica.clases.Usuario;
import com.culturarte.logica.controllers.IColaboradorController;
import com.culturarte.logica.controllers.IProponenteController;
import com.culturarte.logica.controllers.ISeguimientoController;
import com.culturarte.logica.fabrica.Fabrica;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/dejarSeguirUsuario")
public class DejarSeguirUsuarioServlet extends HttpServlet {

    private final IProponenteController propCtrl = Fabrica.getInstancia().getProponenteController();
    private final IColaboradorController colCtrl = Fabrica.getInstancia().getColaboradorController();
    private final ISeguimientoController segCtrl = Fabrica.getInstancia().getSeguimientoController();

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

        if (nickSeguido == null || nickSeguido.isEmpty()) {
            req.setAttribute("mensajeError", "Debe seleccionar un usuario a dejar de seguir.");
            doGet(req, resp);
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
        }

        req.getRequestDispatcher("/consultaPerfil").forward(req, resp);
    }
}
