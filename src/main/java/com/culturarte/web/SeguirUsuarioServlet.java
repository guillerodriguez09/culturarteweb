package com.culturarte.web;

import com.culturarte.logica.dtos.DTOSeguimiento;
import com.culturarte.logica.dtos.DTOColaborador;
import com.culturarte.logica.dtos.DTOProponente;
import com.culturarte.logica.clases.Usuario;
import com.culturarte.logica.clases.Proponente;
import com.culturarte.logica.clases.Colaborador;
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

@WebServlet("/seguirUsuario")
public class SeguirUsuarioServlet extends HttpServlet {

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

        IProponenteController propCtrl = FabricaWeb.getInstancia().getProponenteController();
        IColaboradorController colCtrl = FabricaWeb.getInstancia().getColaboradorController();

        List<String> todosUsuarios = new ArrayList<>();
        todosUsuarios.addAll(propCtrl.listarProponentes());
        todosUsuarios.addAll(colCtrl.listarColaboradores());

        // saca al propio usuario de la lista
        todosUsuarios.removeIf(u -> u.equalsIgnoreCase(nickActual));

        req.setAttribute("usuarios", todosUsuarios);
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
        String nickSeguido = req.getParameter("usuarioSeguido");

        if (nickSeguido == null || nickSeguido.isEmpty()) {
            req.setAttribute("mensajeError", "Debe seleccionar un usuario a seguir.");
            doGet(req, resp);
            return;
        }

        IProponenteController propCtrl = FabricaWeb.getInstancia().getProponenteController();
        IColaboradorController colCtrl = FabricaWeb.getInstancia().getColaboradorController();
        ISeguimientoController segCtrl = FabricaWeb.getInstancia().getSeguimientoController();

        // usuario del seguidor puede ser proponente o colaborador
        Usuario usuarioSeguidor = null;

        DTOProponente dtoP = propCtrl.obtenerProponente(nickSeguidor);
        if (dtoP != null) {
            usuarioSeguidor = new Proponente(
                    dtoP.getNick(), dtoP.getNombre(), dtoP.getApellido(),
                    dtoP.getContrasenia(), dtoP.getCorreo(),
                    dtoP.getFechaNac(), dtoP.getDirImagen(),
                    dtoP.getDireccion(), dtoP.getBiografia(), dtoP.getLink()
            );
        } else {
            DTOColaborador dtoC = colCtrl.obtenerColaborador(nickSeguidor);
            if (dtoC != null) {
                usuarioSeguidor = new Colaborador(
                        dtoC.getNick(), dtoC.getNombre(), dtoC.getApellido(),
                        dtoC.getContrasenia(), dtoC.getCorreo(),
                        dtoC.getFechaNac(), dtoC.getDirImagen()
                );
            }
        }

        if (usuarioSeguidor != null) {
            DTOSeguimiento dtoSeg = new DTOSeguimiento(usuarioSeguidor, nickSeguido);
            segCtrl.registrarSeguimiento(dtoSeg);
            req.setAttribute("mensajeExito", "Ahora sigues a " + nickSeguido + ".");
        } else {
            req.setAttribute("mensajeError", "No se pudo identificar tu usuario actual.");
        }

        String tipoUsr = req.getParameter("tipoUsr"); // Obtiene el tipo de usuario

        // redirige de vuelta al perfil correcto
        resp.sendRedirect("consultaPerfil?nick=" + nickSeguido + "&tipoUsr=" + tipoUsr);
    }
}