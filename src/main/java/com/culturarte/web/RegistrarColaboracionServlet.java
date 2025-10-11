package com.culturarte.web;

import com.culturarte.logica.controllers.IColaboracionController;
import com.culturarte.logica.controllers.IPropuestaController;
import com.culturarte.logica.dtos.DTOColaboracion;
import com.culturarte.logica.dtos.DTOPropuesta;
import com.culturarte.logica.enums.ETipoRetorno;
import com.culturarte.logica.fabrica.Fabrica;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet("/registrarColaboracion")
public class RegistrarColaboracionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        String tipoUsuario = (session != null) ? (String) session.getAttribute("tipoUsuario") : null;

        if (tipoUsuario == null || !tipoUsuario.equalsIgnoreCase("Colaborador")) {
            resp.sendRedirect(req.getContextPath() + "/inicioSesion.jsp?loginRequired=1");
            return;
        }

        IPropuestaController propuestaCtrl = Fabrica.getInstancia().getPropuestaController();
        List<DTOPropuesta> propuestas = propuestaCtrl.listarPropuestasConProponente();
        req.setAttribute("propuestas", propuestas);

        req.getRequestDispatcher("/registrarColaboracion.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession(false);
        String colaboradorNick = (session != null) ? (String) session.getAttribute("nick") : null;
        String tipoUsuario = (session != null) ? (String) session.getAttribute("tipoUsuario") : null;

        if (colaboradorNick == null || tipoUsuario == null || !tipoUsuario.equalsIgnoreCase("Colaborador")) {
            resp.sendRedirect(req.getContextPath() + "/inicioSesion.jsp?loginRequired=1");
            return;
        }

        try {
            String propuestaTitulo = req.getParameter("propuestaTitulo");
            String tipoRetornoStr = req.getParameter("tipoRetorno");
            String montoStr = req.getParameter("monto");

            ETipoRetorno tipoRetorno = ETipoRetorno.valueOf(tipoRetornoStr);
            Integer monto = Integer.parseInt(montoStr);

            DTOColaboracion dto = new DTOColaboracion();
            dto.setColaboradorNick(colaboradorNick);
            dto.setPropuestaTitulo(propuestaTitulo);
            dto.setRetorno(tipoRetorno);
            dto.setMonto(monto);
            dto.setFecha(LocalDateTime.now());

            IColaboracionController colaboracionCtrl = Fabrica.getInstancia().getColaboracionController();
            colaboracionCtrl.registrarColaboracion(dto);

            req.setAttribute("mensaje", "Colaboración registrada con éxito para la propuesta: " + propuestaTitulo);

        } catch (Exception e) {
            req.setAttribute("error", "Error al registrar colaboración: " + e.getMessage());
        }

        doGet(req, resp); // Recarga la página con la lista de propuestas
    }
}
