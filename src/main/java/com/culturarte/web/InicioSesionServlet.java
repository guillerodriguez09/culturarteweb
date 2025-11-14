package com.culturarte.web;

import com.culturarte.web.ws.cliente.*;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/inicioSesion")
public class InicioSesionServlet extends HttpServlet {


    private IProponenteController propController;
    private IColaboradorController colaController;
    private IPropuestaController propuController;

    @Override
    public void init() throws ServletException {
        ServletContext context = getServletContext();

        this.propController = (IProponenteController) context.getAttribute("ws.proponente");
        this.colaController = (IColaboradorController) context.getAttribute("ws.colaborador");
        this.propuController = (IPropuestaController) context.getAttribute("ws.propuesta");

        if (this.propController == null || this.colaController == null || this.propuController == null) {
            throw new ServletException("¡Error crítico! Uno de los clientes WS no se pudo cargar desde ClienteInit.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");

        String nickOMail   = req.getParameter("nickOMail");
        String contrasenia = req.getParameter("contrasenia");

        if (nickOMail == null || nickOMail.isBlank() || contrasenia == null || contrasenia.isBlank()) {
            req.setAttribute("error", "Por favor ingrese nickname/correo y contraseña");
            req.getRequestDispatcher("/inicioSesion.jsp").forward(req, resp);
            return;
        }

        try {
            String tipoUsuario = null;
            String contrReal   = null;
            String nickReal    = null;

            // Buscar por nick o correo
            if (!nickOMail.contains("@")) {

                DtoProponente p = propController.obtenerProponente(nickOMail);
                if (p != null) {
                    tipoUsuario = "PROPONENTE";
                    contrReal = p.getContrasenia();
                    nickReal = p.getNickname();
                }

                if (tipoUsuario == null) {
                    DtoColaborador c = colaController.obtenerColaborador(nickOMail);
                    if (c != null) {
                        tipoUsuario = "COLABORADOR";
                        contrReal = c.getContrasenia();
                        nickReal = c.getNickname();
                    }
                }
            } else {
                DtoProponente p = propController.obtenerProponenteCorreo(nickOMail);
                if (p != null) { tipoUsuario = "PROPONENTE"; contrReal = p.getContrasenia(); nickReal = p.getNickname(); }
                if (tipoUsuario == null) {
                    DtoColaborador c = colaController.obtenerColaboradorCorreo(nickOMail);
                    if (c != null) { tipoUsuario = "COLABORADOR"; contrReal = c.getContrasenia(); nickReal = c.getNickname(); }
                }
            }

            if (tipoUsuario == null) {
                req.setAttribute("error", "Usuario no encontrado.");
                req.getRequestDispatcher("/inicioSesion.jsp").forward(req, resp);
                return;
            }

            if (!contrasenia.equals(contrReal)) {
                req.setAttribute("error", "Usuario y/o contraseña incorrectos");
                req.getRequestDispatcher("/inicioSesion.jsp").forward(req, resp);
                return;
            }

            HttpSession sesion = req.getSession(true);
            sesion.setAttribute("tipoUsuario", tipoUsuario);
            sesion.setAttribute("nick", nickReal);
            sesion.setAttribute("password", contrasenia);
            sesion.setAttribute("sesion", new Sesion(nickReal, tipoUsuario, contrasenia));

            if (tipoUsuario.equals("COLABORADOR")) {
                try {
                    List<DtoPropuesta> recomendaciones = this.propuController.recomendarPropuestas(nickReal);

                    if (recomendaciones != null && !recomendaciones.isEmpty()) {
                        sesion.setAttribute("recomendaciones", recomendaciones);
                    }
                } catch (Exception e) {
                    System.err.println("Error al cargar recomendaciones: " + e.getMessage());
                }
            }

            resp.sendRedirect(req.getContextPath() + "/index.jsp");

        } catch (Exception e) {
            req.setAttribute("error", "Error al iniciar sesión: " + e.getMessage());
            req.getRequestDispatcher("/inicioSesion.jsp").forward(req, resp);
        }
    }
}
