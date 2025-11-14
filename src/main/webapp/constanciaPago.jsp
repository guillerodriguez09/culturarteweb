<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.culturarte.web.ws.cliente.*" %>

<%
    DtoConstanciaPago dto = (DtoConstanciaPago) request.getAttribute("constancia");
%>

<div class="container mt-5">
    <div class="card shadow-sm p-4">

        <% if(dto == null) { %>
            <h3 class="text-center text-danger">No hay constancia para mostrar.</h3>
            <a href="consultaPerfil" class="btn btn-primary mt-3">Volver</a>
        <% } else { %>
            <h3 class="text-center">Constancia de Pago de Colaboración</h3>
            <hr>
            <p><strong>Plataforma:</strong> <%= dto.getPlataforma() %></p>
            <p><strong>Fecha de emisión:</strong> <%= dto.getFechaEmision() %></p>
            <hr>
            <p><strong>Colaborador:</strong> <%= dto.getColaboradorNombre() %> (<%= dto.getColaboradorNick() %>)</p>
            <p><strong>Email:</strong> <%= dto.getColaboradorEmail() %></p>
            <hr>
            <p><strong>Propuesta:</strong> <%= dto.getPropuestaNombre() %></p>
            <p><strong>Monto colaborado:</strong> $<%= dto.getMonto() %></p>
            <p><strong>Fecha de colaboración:</strong> <%= dto.getFechaColaboracion() %></p>
            <hr>
            <a href="consultaPerfil" class="btn btn-primary">Volver al perfil</a>
        <% } %>

    </div>
</div>
