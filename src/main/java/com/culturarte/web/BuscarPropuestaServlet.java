package com.culturarte.web;

import com.culturarte.web.ws.cliente.DtoPropuesta;
import com.culturarte.web.ws.cliente.IPropuestaController;


import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet("/buscarPropuesta")
public class BuscarPropuestaServlet extends HttpServlet {

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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");

        String filtro = req.getParameter("q");
        String estado = req.getParameter("estado");
        String orden = req.getParameter("orden");

        List<DtoPropuesta> resultados;
        try {
            resultados = propCtrl.buscarPropuestas(filtro);
        } catch (Exception e) {
            req.setAttribute("error", "Error al buscar propuestas: " + e.getMessage());
            req.setAttribute("resultadosPorEstado", new LinkedHashMap<String, List<DtoPropuesta>>());
            req.getRequestDispatcher("/resultadosBusqueda.jsp").forward(req, resp);
            return;
        }

        // Filtro por estado (no cambia)
        if (estado != null && !estado.isBlank()) {
            resultados = resultados.stream()
                    .filter(p -> p.getEstadoActual().equalsIgnoreCase(estado))
                    .collect(Collectors.toList());
        }

        // Ordenamiento
        if ("alfabetico".equals(orden)) {
            resultados.sort(Comparator.comparing(DtoPropuesta::getTitulo, String.CASE_INSENSITIVE_ORDER));

        } else if ("fecha".equals(orden)) {
            try {
                resultados.sort(Comparator.comparing(
                        (DtoPropuesta dto) -> LocalDate.parse(dto.getFecha()) // Convierte String a LocalDate
                ).reversed());
            } catch (Exception e) {
                System.err.println("Error al parsear fecha en sort: " + e.getMessage());
            }
        }

        Map<String, List<DtoPropuesta>> agrupadas = resultados.stream()
                .collect(Collectors.groupingBy(DtoPropuesta::getEstadoActual));

        LinkedHashMap<String, List<DtoPropuesta>> ordenadas = new LinkedHashMap<>();
        ordenadas.put("PUBLICADA", agrupadas.getOrDefault("PUBLICADA", List.of()));
        ordenadas.put("EN_FINANCIACION", agrupadas.getOrDefault("EN_FINANCIACION", List.of()));
        ordenadas.put("FINANCIADA", agrupadas.getOrDefault("FINANCIADA", List.of()));
        ordenadas.put("NO_FINANCIADA", agrupadas.getOrDefault("NO_FINANCIADA", List.of()));
        ordenadas.put("CANCELADA", agrupadas.getOrDefault("CANCELADA", List.of()));

        req.setAttribute("resultadosPorEstado", ordenadas);
        req.setAttribute("filtro", filtro);

        req.getRequestDispatcher("/resultadosBusqueda.jsp").forward(req, resp);
    }
}