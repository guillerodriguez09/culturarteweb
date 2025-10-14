<%--
  Created by IntelliJ IDEA.
  User: Chorizo-Cosmico
  Date: 13/10/2025
  Time: 23:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.culturarte.logica.dtos.DTOColaborador" %>
<%@ page import="com.culturarte.logica.dtos.DTOProponente" %>
<%@ page import="java.util.*, com.culturarte.logica.dtos.DTOProponente" %>
<%@ page import="java.util.*, com.culturarte.logica.dtos.DTOColaborador" %>

<html>
<head>
    <meta charset="UTF-8">
    <title>Consulta de Perfiles</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="css/general.css" rel="stylesheet">

</head>
<body>
<%@ include file="compartidos/header.jsp" %>

<div class="container mt-4">
    <h2 class="text-center mb-4">Consulta de Perfiles</h2>

    <div class="mt-3">
        <%
            Map<String, List<DTOProponente>> listaProps = Map.of("proponentes", (List<DTOProponente>) request.getAttribute("proponentes"));
            Map<String, List<DTOColaborador>> listaColas = Map.of("colaboradores", (List<DTOColaborador>) request.getAttribute("colaboradores"));
        %>
        <div class="list-group">
            <%
                List<DTOProponente> listaProp = listaProps.get("proponentes");
                if(listaProp != null && !listaProp.isEmpty()){
                    for(DTOProponente prop : listaProp){
            %>
            <form action="consultaPerfil" method="post" class="list-group-item list-group-item-action d-flex justify-content-between align-items-center">
                <div>
                    <strong><%= prop.getNick() %></strong> — <%= prop.getNombre() %> <%= prop.getApellido() %>
                </div>
                <input type="hidden" name="tipoUsr" value="proponente">
                <input type="hidden" name="nick" value="<%= prop.getNick() %>">
                <button type="submit" class="btn btn-outline-primary btn-sm">Ver detalles</button>
            </form>
            <% }
            }else{
            %>
            <p class="text-muted p-3">No hay proponentes</p>
            <% } %>

            <%
                List<DTOColaborador> listaCol = listaColas.get("colaboradores");
                if(listaCol != null && !listaCol.isEmpty()){
                    for(DTOColaborador col : listaCol){
            %>
            <form action="consultaPerfil" method="post" class="list-group-item list-group-item-action d-flex justify-content-between align-items-center">
                <div>
                    <strong><%= col.getNick() %></strong> — <%= col.getNombre() %> <%= col.getApellido() %>
                </div>
                <input type="hidden" name="tipoUsr" value="colaborador">
                <input type="hidden" name="nick" value="<%= col.getNick() %>">
                <button type="submit" class="btn btn-outline-primary btn-sm">Ver detalles</button>
            </form>
            <% }
            }else{
            %>
            <p class="text-muted p-3">No hay colaboradores</p>
            <% } %>
        </div>
    </div>
    <%
        DTOProponente pro = (DTOProponente) request.getAttribute("proponenteSeleccionado");
        DTOColaborador col = (DTOColaborador) request.getAttribute("colaboradorSeleccionado");
        if(pro != null){
    %>
    <div class="card mt-5 shadow">
        <div class="card-body">
            <h4 class="card-title"><%= pro.getNick() %></h4>
            <p><strong>Nombre:</strong><%= pro.getNombre() %></p>
            <p><strong>Apellido:</strong><%= pro.getApellido() %></p>
            <p><strong>Contraseña:</strong><%= pro.getContrasenia() %></p>
            <p><strong>Correo:</strong><%= pro.getCorreo() %></p>
            <p><strong>Fecha de Nacimiento:</strong><%= pro.getFechaNac() %></p>

            <% if(pro.getDirImagen() != null && !pro.getDirImagen().isEmpty()){  %>
            <img src="<%= request.getContextPath() + "/" + pro.getDirImagen() %>" class="img-thumbnail shadow-sm" width="200" height="200" alt="Imagen perfil">
            <% }else{ %>
            <img src="<%= request.getContextPath() + "/imagenes/404.png" %>" class="img-thumbnail shadow-sm" width="200" height="200" alt="Sin imagen disponible">
            <% } %>
            <p><strong>Dirección</strong><%= pro.getDireccion() %></p>
            <p class="card-text"><strong>Biografia</strong><%= pro.getBiografia() %></p>
            <p><strong>Link</strong><%= pro.getLink() %></p>
        </div>
    </div>
    <% }else if(col != null){ %>
    <div class="card mt-5 shadow">
        <div class="card-body">
            <h4 class="card-title"><%= col.getNick() %></h4>
            <p><strong>Nombre:</strong><%= col.getNombre() %></p>
            <p><strong>Apellido:</strong><%= col.getApellido() %></p>
            <p><strong>Contraseña:</strong><%= col.getContrasenia() %></p>
            <p><strong>Correo:</strong><%= col.getCorreo() %></p>
            <p><strong>Fecha de Nacimiento:</strong><%= col.getFechaNac() %></p>

            <% if(col.getDirImagen() != null && !col.getDirImagen().isEmpty()){  %>
            <img src="<%= request.getContextPath() + "/" + col.getDirImagen() %>" class="img-thumbnail shadow-sm" width="200" height="200" alt="Imagen perfil">
            <% }else{ %>
            <img src="<%= request.getContextPath() + "/imagenes/404.png" %>" class="img-thumbnail shadow-sm" width="200" height="200" alt="Sin imagen disponible">
            <% } %>
        </div>
    </div>
    <% } %>

</div>

<%@ include file="compartidos/footer.jsp" %>

</body>
</html>
