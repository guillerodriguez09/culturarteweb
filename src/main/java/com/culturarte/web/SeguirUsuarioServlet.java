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
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession sesion = req.getSession(false);
        if (sesion == null || sesion.getAttribute("sesion") == null) {
            resp.sendRedirect("inicioSesion.jsp");
            return;
        }

        Sesion usuario = (Sesion) sesion.getAttribute("sesion");
        String nickActual = usuario.getNickOMail();

        try {
            List<String> lista = new ArrayList<>();
            lista.addAll(propCtrl.listarProponentes());
            lista.addAll(colCtrl.listarColaboradores());
            lista.removeIf(u -> u.equalsIgnoreCase(nickActual));

            req.setAttribute("usuarios", lista);

        } catch (Exception e) {
            req.setAttribute("mensajeError", "Error cargando usuarios.");
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

        Sesion usuario = (Sesion) sesion.getAttribute("sesion");
        String nickSeguidor = usuario.getNickOMail();
        String nickSeguido = req.getParameter("usuarioSeguido");

        try {
            DtoUsuario dtoSeg = new DtoUsuario();
            dtoSeg.setNickname(nickSeguidor);

            DtoSeguimiento dto = new DtoSeguimiento();
            dto.setUsuarioSeguidor(dtoSeg);
            dto.setUsuarioSeguido(nickSeguido);

            segCtrl.registrarSeguimiento(dto);

            IPropuestaController propCtrl =
                    (IPropuestaController) getServletContext().getAttribute("ws.propuesta");

            List<DtoPropuesta> nuevas = propCtrl.recomendarPropuestas(nickSeguidor);
            sesion.setAttribute("recomendaciones", nuevas);

        } catch (Exception e) {
            req.setAttribute("mensajeError", "Error: " + e.getMessage());
        }

        resp.sendRedirect("consultaPerfil?nick=" + nickSeguido +
                "&tipoUsr=" + req.getParameter("tipoUsr"));
    }
}
