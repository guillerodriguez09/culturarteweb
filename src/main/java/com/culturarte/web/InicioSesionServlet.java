package com.culturarte.web;

import com.culturarte.logica.fabrica.Fabrica;
import com.culturarte.logica.controllers.IProponenteController;
import com.culturarte.logica.controllers.IColaboradorController;
import com.culturarte.logica.dtos.DTOProponente;
import com.culturarte.logica.dtos.DTOColaborador;
import com.culturarte.web.fabrica.FabricaWeb;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/inicioSesion")
public class InicioSesionServlet extends HttpServlet {

    final IColaboradorController colaController = FabricaWeb.getInstancia().getColaboradorController();
    final IProponenteController  propController = FabricaWeb.getInstancia().getProponenteController();

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
                DTOProponente p = propController.obtenerProponente(nickOMail);
                if (p != null) {
                    tipoUsuario = "PROPONENTE";
                    contrReal = p.getContrasenia();
                    nickReal = p.getNick();
                }

                if (tipoUsuario == null) {
                    DTOColaborador c = colaController.obtenerColaborador(nickOMail);
                    if (c != null) {
                        tipoUsuario = "COLABORADOR";
                        contrReal = c.getContrasenia();
                        nickReal = c.getNick();
                    }
                }
            } else {
                DTOProponente p = propController.obtenerProponenteCorreo(nickOMail);
                if (p != null) { tipoUsuario = "PROPONENTE"; contrReal = p.getContrasenia(); nickReal = p.getNick(); }
                if (tipoUsuario == null) {
                    DTOColaborador c = colaController.obtenerColaboradorCorreo(nickOMail);
                    if (c != null) { tipoUsuario = "COLABORADOR"; contrReal = c.getContrasenia(); nickReal = c.getNick(); }
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

            // setear atributos que usa el resto del sitio si el login dio OK
            HttpSession sesion = req.getSession(true);


            sesion.setAttribute("tipoUsuario", tipoUsuario);  // "PROPONENTE" | "COLABORADOR"
            sesion.setAttribute("nick", nickReal);            // nick real
            sesion.setAttribute("password", contrasenia);

            sesion.setAttribute("sesion", new Sesion(nickReal, tipoUsuario, contrasenia));

            //redirige al index
            resp.sendRedirect(req.getContextPath() + "/index.jsp");


        } catch (Exception e) {
            req.setAttribute("error", "Error al iniciar sesión: " + e.getMessage());
            req.getRequestDispatcher("/inicioSesion.jsp").forward(req, resp);
        }
    }
}
