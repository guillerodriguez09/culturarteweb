package com.culturarte.web;

import com.culturarte.web.ws.cliente.IColaboracionController;
import com.culturarte.web.ws.cliente.DtoConstanciaPago;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/emitirConstancia")
public class EmitirConstanciaServlet extends HttpServlet {

    private IColaboracionController colabController;

    @Override
    public void init() throws ServletException {
        ServletContext context = getServletContext();
        this.colabController =
                (IColaboracionController) context.getAttribute("ws.colaboracion");

        if (colabController == null) {
            throw new ServletException(
                    "Error crítico: el cliente WS de Colaboración no se pudo cargar."
            );
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");

        String idColabStr = req.getParameter("idColab");
        if (idColabStr == null) {
            idColabStr = req.getParameter("idColaboracion");
        }

        if (idColabStr == null || idColabStr.isEmpty()) {
            req.setAttribute("error", "Falta ID de colaboración.");
            req.getRequestDispatcher("/consultaPerfil.jsp").forward(req, resp);
            return;
        }

        try {
            int idColab = Integer.parseInt(idColabStr);

            DtoConstanciaPago constancia = colabController.emitirConstanciaPago(idColab);

            req.setAttribute("constancia", constancia);

            req.getRequestDispatcher("/constanciaPago.jsp").forward(req, resp);
            return;

        } catch (NumberFormatException e) {
            req.setAttribute("error", "ID de colaboración inválido.");
        } catch (Exception e) {
            req.setAttribute("error", "Error al emitir constancia: " + e.getMessage());
            e.printStackTrace();
        }

        req.getRequestDispatcher("/consultaPerfil.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req, resp);
    }
}