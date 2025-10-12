package com.culturarte.web;

import com.culturarte.logica.clases.Propuesta;
import com.culturarte.logica.controllers.IPropuestaController;
import com.culturarte.logica.dtos.DTOPropuesta;
import com.culturarte.logica.fabrica.Fabrica;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@WebServlet("/buscarPropuesta")
public class BuscarPropuestaServlet extends HttpServlet {

    private final IPropuestaController propCtrl = Fabrica.getInstancia().getPropuestaController();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");

        String filtro = req.getParameter("q");
        String estado = req.getParameter("estado");
        String orden = req.getParameter("orden");

        List<DTOPropuesta> resultados = propCtrl.buscarPropuestas(filtro);


        // Filtro por estado si se seleccionó uno
        if (estado != null && !estado.isBlank()) {
            resultados = resultados.stream()
                    .filter(p -> p.getEstadoActual().equalsIgnoreCase(estado))
                    .collect(Collectors.toList());
        }

        // Ordenamiento
        if ("alfabetico".equals(orden)) {
            resultados.sort(Comparator.comparing(DTOPropuesta::getTitulo, String.CASE_INSENSITIVE_ORDER));
        } else if ("fecha".equals(orden)) {
            resultados.sort(Comparator.comparing(DTOPropuesta::getFecha).reversed());
        }

        // Agrupar por estado para mostrar en pestañas
        Map<String, List<DTOPropuesta>> agrupadas = resultados.stream()
                .collect(Collectors.groupingBy(DTOPropuesta::getEstadoActual));

        // Para mantener orden de pestañas fijo
        LinkedHashMap<String, List<DTOPropuesta>> ordenadas = new LinkedHashMap<>();
        ordenadas.put("PUBLICADA", agrupadas.getOrDefault("PUBLICADA", List.of()));
        ordenadas.put("EN_FINANCIACION", agrupadas.getOrDefault("EN_FINANCIACION", List.of()));
        ordenadas.put("FINANCIADA", agrupadas.getOrDefault("FINANCIADA", List.of()));
        ordenadas.put("NO_FINANCIADA", agrupadas.getOrDefault("NO_FINANCIADA", List.of()));
        ordenadas.put("CANCELADA", agrupadas.getOrDefault("CANCELADA", List.of()));

        req.setAttribute("resultadosPorEstado", ordenadas);

        req.setAttribute("filtro", filtro);
        //req.setAttribute("resultados", resultados);
        req.getRequestDispatcher("/resultadosBusqueda.jsp").forward(req, resp);
    }
}

