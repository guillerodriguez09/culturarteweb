package com.culturarte.web;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.culturarte.web.ws.cliente.*;

@WebServlet("/seguirUsuario")
public class SeguirUsuarioServlet extends HttpServlet {

    private IProponenteController propCtrl;
    private IColaboradorController colCtrl;
    private ISeguimientoController segCtrl;

    @Override
    public void init() throws ServletException {
        ServletContext context = getServletContext();

        this.propCtrl = (IProponenteController) context.getAttribute("ws.proponente");
        this.colCtrl = (IColaboradorController) context.getAttribute("ws.colaborador");
        this.segCtrl = (ISeguimientoController) context.getAttribute("ws.seguimiento");

        if (this.propCtrl == null || this.colCtrl == null || this.segCtrl == null) {
            throw new ServletException("¡Error crítico! Los clientes de SeguirUsuarioServlet no se pudieron cargar desde ClienteInit.");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession sesion = req.getSession(false);
        if (sesion == null || sesion.getAttribute("sesion") == null) {
            resp.sendRedirect("inicioSesion.jsp");
            return;
        }

        Sesion usuarioSesion = (Sesion) sesion.getAttribute("sesion");
        String nickActual = usuarioSesion.getNickOMail();

        try {
            List<String> todosUsuarios = new ArrayList<>();
            todosUsuarios.addAll(this.propCtrl.listarProponentes());
            todosUsuarios.addAll(this.colCtrl.listarColaboradores());
            todosUsuarios.removeIf(u -> u.equalsIgnoreCase(nickActual));
            req.setAttribute("usuarios", todosUsuarios);
        } catch (Exception e) {
            req.setAttribute("usuarios", new ArrayList<String>()); // Lista vacía en caso de error
            req.setAttribute("mensajeError", "Error al cargar usuarios: " + e.getMessage());
        }

        req.getRequestDispatcher("/seguirUsuario.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession sesion = req.getSession(false);
        if (sesion == null || sesion.getAttribute("sesion") == null) {
            resp.sendRedirect("inicioSesion.jsp");
            return;
        }

        Sesion usuarioSesion = (Sesion) sesion.getAttribute("sesion");
        String nickSeguidor = usuarioSesion.getNickOMail();
        String tipoSeguidor = usuarioSesion.getTipoUsuario(); // Proponente o Colaborador
        String nickSeguido = req.getParameter("usuarioSeguido");

        if (nickSeguido == null || nickSeguido.isEmpty()) {
            req.setAttribute("mensajeError", "Debe seleccionar un usuario a seguir.");
            doGet(req, resp); // recarga la página
            return;
        }

        try {
            // Armamos el stub del seguidor (DtoUsuario)
            DtoUsuario dtoSeguidor = new DtoUsuario();
            dtoSeguidor.setNickname(nickSeguidor);

            // Armamos el seguimiento completo
            DtoSeguimiento dtoSeg = new DtoSeguimiento();
            dtoSeg.setUsuarioSeguidor(dtoSeguidor);
            dtoSeg.setUsuarioSeguido(nickSeguido);

            // Llamamos al WS remoto
            segCtrl.registrarSeguimiento(dtoSeg);

        } catch (Exception e) {
            System.err.println("Error al registrar seguimiento: " + e.getMessage());
            req.setAttribute("mensajeError", "Error: " + e.getMessage());
        }

        // Redirigimos al perfil del seguido
        String tipoUsr = req.getParameter("tipoUsr");
        resp.sendRedirect("consultaPerfil?nick=" + nickSeguido + "&tipoUsr=" + tipoUsr);
    }

}