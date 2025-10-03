package com.culturarte.web;

import com.culturarte.logica.controllers.IPropuestaController;
import com.culturarte.logica.dtos.DTOPropuesta;
import com.culturarte.logica.fabrica.Fabrica;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/prueba")
public class PruebaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        try {
            // Obtener el controller desde el JAR del central
            IPropuestaController ctrl = Fabrica.getInstancia().getPropuestaController();
            List<DTOPropuesta> propuestas = ctrl.listarPropuestasConProponente();

            out.println("<html><body>");
            out.println("<h1> Servlet funcionando con dependencia al central</h1>");
            out.println("<p>Cantidad de propuestas registradas: " + propuestas.size() + "</p>");

            if (!propuestas.isEmpty()) {
                out.println("<ul>");
                for (DTOPropuesta p : propuestas) {
                    out.println("<li>" + p.getTitulo() + " (" + p.getEstadoActual() + ")</li>");
                }
                out.println("</ul>");
            }

            out.println("</body></html>");
        } catch (Exception e) {
            out.println("<p style='color:red'>⚠ Error: " + e.getMessage() + "</p>");
            e.printStackTrace();
        }
    }
}

