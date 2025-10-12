<%@ page import="com.culturarte.logica.dtos.DTOPropuesta" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %><%--
  Created by IntelliJ IDEA.
  User: guillerodriguezz
  Date: 12/10/25
  Time: 19:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="compartidos/header.jsp"/>

<div class="container mt-4">
    <h2 class="text-center mb-4">
        Resultados de búsqueda: "<%= request.getAttribute("filtro") %>"
    </h2>

    <!-- filtros y orden -->
    <form class="d-flex justify-content-center mb-3" method="get">
        <input type="hidden" name="q" value="<%= request.getAttribute("filtro") %>">

        <select name="estado" class="form-select me-2" style="width: 200px;">
            <option value="">Todos los estados</option>
            <option value="PUBLICADA">Publicada</option>
            <option value="EN_FINANCIACION">En financiación</option>
            <option value="FINANCIADA">Financiada</option>
            <option value="NO_FINANCIADA">No financiada</option>
            <option value="CANCELADA">Cancelada</option>
        </select>

        <select name="orden" class="form-select me-2" style="width: 200px;">
            <option value="">Sin orden</option>
            <option value="alfabetico">Alfabético (A-Z)</option>
            <option value="fecha">Por fecha (recientes primero)</option>
        </select>

        <button class="btn btn-primary">Aplicar</button>
    </form>

    <%
        Map<String, List<DTOPropuesta>> resultadosPorEstado =
                (Map<String, List<DTOPropuesta>>) request.getAttribute("resultadosPorEstado");

        if (resultadosPorEstado == null || resultadosPorEstado.values().stream().allMatch(List::isEmpty)) {
    %>
    <p class="text-muted text-center">No se encontraron propuestas.</p>
    <% } else { %>

    <!-- Pestañas por estado -->
    <ul class="nav nav-tabs" role="tablist">
        <% boolean first = true;
            for (String estado : resultadosPorEstado.keySet()) { %>
        <li class="nav-item">
            <a class="nav-link <%= first ? "active" : "" %>" data-bs-toggle="tab" href="#<%= estado %>">
                <%= estado.replace("_", " ") %>
            </a>
        </li>
        <% first = false; } %>
    </ul>

    <div class="tab-content mt-3">
        <% first = true;
            for (Map.Entry<String, List<DTOPropuesta>> entry : resultadosPorEstado.entrySet()) {
                String estado = entry.getKey();
                List<DTOPropuesta> lista = entry.getValue(); %>

        <div class="tab-pane fade <%= first ? "show active" : "" %>" id="<%= estado %>">
            <div class="row">
                <% if (lista.isEmpty()) { %>
                <p class="text-muted p-3">No hay propuestas en este estado.</p>
                <% } else {
                    for (DTOPropuesta p : lista) { %>
                <div class="col-md-4 mb-4">
                    <div class="card h-100 shadow-sm">
                        <img src="<%= request.getContextPath() + "/" +
                                    (p.getImagen() != null && !p.getImagen().isEmpty()
                                        ? p.getImagen()
                                        : "imagenes/404.png") %>"
                             class="card-img-top" height="180" alt="Imagen de propuesta">
                        <div class="card-body">
                            <h5 class="card-title"><%= p.getTitulo() %></h5>
                            <p class="card-text"><%= p.getDescripcion() %></p>
                            <p><strong>Lugar:</strong> <%= p.getLugar() %></p>
                            <p><strong>Monto recaudado:</strong> $<%= p.getMontoRecaudado() %></p>
                            <form action="consultarPropuesta" method="post">
                                <button type="submit" name="titulo" value="<%= p.getTitulo() %>"
                                        class="btn btn-outline-primary btn-sm">
                                    Ver detalle
                                </button>
                            </form>
                        </div>
                    </div>
                </div>
                <% } } %>
            </div>
        </div>
        <% first = false; } %>
    </div>

    <% } %>
</div>


<jsp:include page="compartidos/footer.jsp"/>
