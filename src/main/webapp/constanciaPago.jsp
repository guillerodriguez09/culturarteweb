<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.culturarte.web.ws.cliente.*" %>
<%@ include file="/compartidos/header.jsp" %>

<%
    DtoConstanciaPago dto = (DtoConstanciaPago) request.getAttribute("constancia");
%>

<div class="container mt-5 mb-5" style="max-width: 750px;">
    <div class="card shadow-lg border-0 rounded-4">

        <% if(dto == null) { %>

            <div class="card-body text-center p-5">
                <h3 class="text-danger mb-3">
                    <i class="bi bi-exclamation-triangle-fill"></i> No hay constancia para mostrar
                </h3>
                <a href="consultaPerfil" class="btn btn-primary px-4">
                    <i class="bi bi-arrow-left"></i> Volver
                </a>
            </div>

        <% } else { %>

            <div class="card-header bg-primary text-white text-center py-4 rounded-top-4">
                <h3 class="mb-0">Constancia de Pago de Colaboración</h3>
            </div>

            <div class="card-body p-5">

                <div class="mb-4 text-center">
                    <span class="fw-bold fs-5"><%= dto.getPlataforma() %></span><br>
                    <small class="text-muted">Emitida el <%= dto.getFechaEmision() %></small>
                </div>

                <hr class="my-4">

                <h5 class="fw-bold mb-3">Datos del Colaborador</h5>
                <p class="mb-1"><strong>Nombre:</strong> <%= dto.getColaboradorNombre() %></p>
                <p class="mb-1"><strong>Usuario:</strong> @<%= dto.getColaboradorNick() %></p>
                <p><strong>Email:</strong> <%= dto.getColaboradorEmail() %></p>

                <hr class="my-4">

                <h5 class="fw-bold mb-3">Detalles de la Propuesta</h5>
                <p class="mb-1"><strong>Título:</strong> <%= dto.getPropuestaNombre() %></p>
                <p class="mb-1"><strong>Monto aportado:</strong> $<%= dto.getMonto() %></p>
                <p><strong>Fecha de colaboración:</strong> <%= dto.getFechaColaboracion() %></p>

                <hr class="my-4">

                <div class="text-center mt-4">
                    <a href="consultaPerfil" class="btn btn-primary px-4">
                        <i class="bi bi-arrow-left"></i> Volver al perfil
                    </a>
                </div>

            </div>

        <% } %>

    </div>
</div>
<%@ include file="/compartidos/footer.jsp" %>