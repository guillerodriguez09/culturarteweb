<%@ page import="com.culturarte.logica.dtos.DTOPropuesta" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, com.culturarte.logica.dtos.DTOPropuesta" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Consulta de Propuestas</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="css/general.css" rel="stylesheet">
</head>

<%@ include file="compartidos/header.jsp" %>

<div class="container mt-4">
    <h2 class="text-center mb-4">Consulta de Propuestas</h2>

    <!-- Mensajes de éxito o error -->
    <% if (request.getAttribute("mensaje") != null) { %>
    <div class="alert alert-success alert-dismissible fade show" role="alert">
        <%= request.getAttribute("mensaje") %>
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
    <% } else if (request.getAttribute("error") != null) { %>
    <div class="alert alert-danger alert-dismissible fade show" role="alert">
        <%= request.getAttribute("error") %>
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
    <% } %>

    <!-- Tabs por estado -->
    <ul class="nav nav-tabs" id="estadoTabs" role="tablist">
        <li class="nav-item"><a class="nav-link active" data-bs-toggle="tab" href="#publicadas">Publicadas</a></li>
        <li class="nav-item"><a class="nav-link" data-bs-toggle="tab" href="#financiacion">En financiación</a></li>
        <li class="nav-item"><a class="nav-link" data-bs-toggle="tab" href="#financiadas">Financiadas</a></li>
        <li class="nav-item"><a class="nav-link" data-bs-toggle="tab" href="#noFinanciadas">No financiadas</a></li>
        <li class="nav-item"><a class="nav-link" data-bs-toggle="tab" href="#canceladas">Canceladas</a></li>
    </ul>

    <div class="tab-content mt-3">
        <%
            Map<String, List<DTOPropuesta>> grupos = Map.of(
                    "publicadas", (List<DTOPropuesta>) request.getAttribute("publicadas"),
                    "financiacion", (List<DTOPropuesta>) request.getAttribute("enFinanciacion"),
                    "financiadas", (List<DTOPropuesta>) request.getAttribute("financiadas"),
                    "noFinanciadas", (List<DTOPropuesta>) request.getAttribute("noFinanciadas"),
                    "canceladas", (List<DTOPropuesta>) request.getAttribute("canceladas")
            );
            for (String key : grupos.keySet()) {
        %>
        <div class="tab-pane fade <%= key.equals("publicadas") ? "show active" : "" %>" id="<%= key %>">
            <div class="list-group">
                <%
                    List<DTOPropuesta> lista = grupos.get(key);
                    if (lista != null && !lista.isEmpty()) {
                        for (DTOPropuesta p : lista) {
                %>
                <form action="consultarPropuesta" method="post"
                      class="list-group-item list-group-item-action d-flex justify-content-between align-items-center">
                    <div>
                        <strong><%= p.getTitulo() %></strong> — <%= p.getLugar() %>
                    </div>
                    <button type="submit" name="titulo" value="<%= p.getTitulo() %>"
                            class="btn btn-outline-primary btn-sm">Ver detalle</button>
                </form>
                <% } } else { %>
                <p class="text-muted p-3">No hay propuestas en este estado.</p>
                <% } %>
            </div>

        </div>
        <% } %>
    </div>

    <!-- Detalle de la propuesta seleccionada -->
    <%
        DTOPropuesta sel = (DTOPropuesta) request.getAttribute("propuestaSeleccionada");
        if (sel != null) {
    %>
    <div class="card mt-5 shadow">
        <div class="card-body">
            <h4 class="card-title"><%= sel.getTitulo() %></h4>
            <p class="card-text"><strong>Descripción:</strong> <%= sel.getDescripcion() %></p>
            <p><strong>Lugar:</strong> <%= sel.getLugar() %></p>
            <p><strong>Estado:</strong> <%= sel.getEstadoActual() %></p>
            <p><strong>Monto a reunir:</strong> $<%= sel.getMontoAReunir() %></p>
            <p><strong>Monto recaudado:</strong> $<%= sel.getMontoRecaudado() %></p>

            <% if (sel.getImagen() != null && !sel.getImagen().isEmpty()) { %>
            <img src="<%= request.getContextPath() + "/" + sel.getImagen() %>"
                 class="img-thumbnail shadow-sm" width="200" height="200" alt="Imagen de la propuesta">
            <% } else { %>
            <img src="<%= request.getContextPath() + "/imagenes/404.png" %>"
                 class="img-thumbnail shadow-sm" width="200" height="200" alt="Sin imagen disponible">
            <% } %>


            <p class="mt-3"><strong>Colaboradores:</strong></p>
            <ul>
                <% for (String c : sel.getColaboradores()) { %>
                <li><%= c %></li>
                <% } %>
            </ul>


            <div class="mt-3">
                <%
                    String tipoUsuario = (String) session.getAttribute("tipoUsuario");
                    String usuario = (String) session.getAttribute("nick");


                    if (usuario != null && sel != null && tipoUsuario != null) {
                        // Si es PROPONENTE y dueño de la propuesta
                        if (tipoUsuario.equals("PROPONENTE") && usuario.equals(sel.getProponenteNick())) {
                            if ("FINANCIADA".equalsIgnoreCase(sel.getEstadoActual())) {
                %>

                <form action="cancelarPropuesta" method="post">
                    <input type="hidden" name="titulo" value="<%= sel.getTitulo() %>">
                    <button class="btn btn-danger">Cancelar Propuesta</button>
                </form>
                <%
                    }
                }
                // Si es COLABORADOR y no colaboró todavía
                    else if (tipoUsuario.equals("COLABORADOR")
                            && !sel.getColaboradores().contains(usuario)
                            && ("PUBLICADA".equalsIgnoreCase(sel.getEstadoActual())
                            || "EN_FINANCIACION".equalsIgnoreCase(sel.getEstadoActual()))) {
                %>
                <a href="registrarColaboracion?titulo=<%= sel.getTitulo() %>" class="btn btn-success">Colaborar</a>
                <%
                }
                }
                else {
                %>
                <p class="text-muted">Iniciá sesión para colaborar o cancelar una propuesta.</p>
                <%
                    }
                %>
            </div>
        </div>
    </div>
    <% } %>
</div>

<%@ include file="compartidos/footer.jsp" %>
</html>
