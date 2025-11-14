package com.culturarte.web;



import com.culturarte.web.ws.cliente.*;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebServlet("/consultarPropuesta")
public class ConsultarPropServlet extends HttpServlet {


    private IPropuestaController propCtrl;


    @Override
    public void init() throws ServletException {
        ServletContext context = getServletContext();
        this.propCtrl = (IPropuestaController) context.getAttribute("ws.propuesta");

        if (this.propCtrl == null) {
            throw new ServletException("¡Error crítico! El cliente de Propuesta (ws.propuesta) no se pudo cargar desde ClienteInit.");
        }
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");

        String titulo = req.getParameter("titulo");
        if (titulo != null && !titulo.isEmpty()) {
            try {
                DtoPropuesta dto = propCtrl.consultarPropuesta(titulo);
                req.setAttribute("propuestaSeleccionada", dto);
                req.setAttribute("colaboradores", dto.getColaboradores());
                req.setAttribute("montoRecaudado", dto.getMontoRecaudado());

                List<ETipoRetorno> tiposRetorno = Arrays.asList(ETipoRetorno.values());
                req.setAttribute("tiposRetorno", tiposRetorno);

            } catch (Exception e) {
                req.setAttribute("error", "No se pudo cargar la propuesta: " + e.getMessage());
            }
        }

        try {
            req.setAttribute("publicadas", propCtrl.listarPorEstado(EEstadoPropuesta.PUBLICADA));
            req.setAttribute("enFinanciacion", propCtrl.listarPorEstado(EEstadoPropuesta.EN_FINANCIACION));
            req.setAttribute("financiadas", propCtrl.listarPorEstado(EEstadoPropuesta.FINANCIADA));
            req.setAttribute("noFinanciadas", propCtrl.listarPorEstado(EEstadoPropuesta.NO_FINANCIADA));
            req.setAttribute("canceladas", propCtrl.listarPorEstado(EEstadoPropuesta.CANCELADA));
        } catch (Exception e) {
            System.err.println("Error cargando listas: " + e.getMessage());
        }

        req.getRequestDispatcher("/consultarPropuesta.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");
        String titulo = req.getParameter("titulo");

        try{
            DtoPropuesta dto = propCtrl.consultarPropuesta(titulo);
            req.setAttribute("propuestaSeleccionada", dto);
            req.setAttribute("colaboradores", dto.getColaboradores());
            req.setAttribute("montoRecaudado", dto.getMontoRecaudado());

        } catch (Exception e) {
            req.setAttribute("error", e.getMessage());
        }
        List<ETipoRetorno> tiposRetorno = Arrays.asList(ETipoRetorno.values());
        req.setAttribute("tiposRetorno", tiposRetorno);
        doGet(req, resp); // recarga tod pero mantiene el detalle de la propuesta seleccionada
    }
}