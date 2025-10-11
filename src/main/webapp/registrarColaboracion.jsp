<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/compartidos/header.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="container mt-4">
    <h2 class="mb-4">Registrar Colaboración</h2>

    <c:if test="${not empty mensaje}">
        <div class="alert alert-success">${mensaje}</div>
    </c:if>

    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>

    <form id="formColaboracion" action="registrarColaboracion" method="post"
          onsubmit="return validarRegistrarColaboracion()">

        <!-- Selección de propuesta -->
        <div class="mb-3">
            <label for="propuestaTitulo" class="form-label">Seleccione la propuesta:</label>
            <select class="form-select" id="propuestaTitulo" name="propuestaTitulo"
                    required onchange="cargarPropuesta()">
                <option value="">-- Seleccione --</option>
                <c:forEach var="p" items="${propuestas}">
                    <option value="${p.titulo}"
                        data-descripcion="${p.descripcion}"
                        data-lugar="${p.lugar}"
                        data-fecha="${p.fecha}"
                        data-precioentrada="${p.precioEntrada}"
                        data-montoareunir="${p.montoAReunir}"
                        data-proponentenick="${p.proponenteNick}"
                        data-estado="${p.estadoActual}"
                        data-montorecaudado="${p.montoRecaudado}">
                        ${p.titulo} (Proponente: ${p.proponenteNick})
                    </option>
                </c:forEach>
            </select>
        </div>

        <!-- Información de la propuesta -->
        <div id="infoPropuesta" class="border p-3 mb-3" style="display:none;">
            <p><strong>Descripción:</strong> <span id="descripcion"></span></p>
            <p><strong>Lugar:</strong> <span id="lugar"></span></p>
            <p><strong>Fecha:</strong> <span id="fecha"></span></p>
            <p><strong>Precio de entrada:</strong> $<span id="precioEntrada"></span></p>
            <p><strong>Monto a reunir:</strong> $<span id="montoAReunir"></span></p>
            <p><strong>Estado actual:</strong> <span id="estado"></span></p>
            <p><strong>Monto recaudado:</strong> $<span id="montoRecaudado"></span></p>
        </div>

        <!-- Monto de la colaboración -->
        <div class="mb-3">
            <label for="monto" class="form-label">Monto de la colaboración:</label>
            <input type="number" class="form-control" id="monto" name="monto" min="1" required>
        </div>

        <!-- Tipo de retorno -->
        <div class="mb-3">
            <label for="tipoRetorno" class="form-label">Tipo de retorno:</label>
            <select class="form-select" id="tipoRetorno" name="tipoRetorno" required>
                <option value="">-- Seleccione --</option>
                <c:forEach var="r" items="${tiposRetorno}">
                    <option value="${r}">${r}</option>
                </c:forEach>
            </select>
        </div>

        <!-- Botones -->
        <button type="submit" class="btn btn-primary">Registrar Colaboración</button>
        <a href="index.jsp" class="btn btn-secondary">Cancelar</a>
    </form>
</div>

<script src="js/registrarColaboracion.js"></script>
<%@ include file="/compartidos/footer.jsp" %>
