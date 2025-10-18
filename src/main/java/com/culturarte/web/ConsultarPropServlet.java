package com.culturarte.web;

import com.culturarte.logica.controllers.IPropuestaController;
import com.culturarte.logica.dtos.DTOPropuesta;
import com.culturarte.logica.enums.EEstadoPropuesta;
import com.culturarte.logica.fabrica.Fabrica;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/consultarPropuesta")
public class ConsultarPropServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");
        IPropuestaController propCtrl = Fabrica.getInstancia().getPropuestaController();

        // Cargar las propuestas por estado
        req.setAttribute("publicadas", propCtrl.listarPorEstado(EEstadoPropuesta.PUBLICADA));
        req.setAttribute("enFinanciacion", propCtrl.listarPorEstado(EEstadoPropuesta.EN_FINANCIACION));
        req.setAttribute("financiadas", propCtrl.listarPorEstado(EEstadoPropuesta.FINANCIADA));
        req.setAttribute("noFinanciadas", propCtrl.listarPorEstado(EEstadoPropuesta.NO_FINANCIADA));
        req.setAttribute("canceladas", propCtrl.listarPorEstado(EEstadoPropuesta.CANCELADA));

        req.getRequestDispatcher("/consultarPropuesta.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");
        IPropuestaController propCtrl = Fabrica.getInstancia().getPropuestaController();
        String titulo = req.getParameter("titulo");

      try{
            DTOPropuesta dto = propCtrl.consultarPropuesta(titulo);
            req.setAttribute("propuestaSeleccionada", dto);
            req.setAttribute("colaboradores", dto.getColaboradores());
            req.setAttribute("montoRecaudado", dto.getMontoRecaudado());

        } catch (Exception e) {
            req.setAttribute("error", e.getMessage());
        }

        doGet(req, resp); // recarga tod pero mantiene el detalle de la propuesta seleccionada
    }
}
