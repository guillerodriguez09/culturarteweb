<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="header.jsp" %>

<div class="container mt-4">

    <h2>Registrar Colaboración</h2>

    <c:if test="${not empty mensaje}">
        <div class="alert alert-success">${mensaje}</div>
    </c:if>
    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>

    <form action="registrarColaboracion" method="post" onsubmit="return validarRegistrarColaboracion();">
        <div class="mb-3">
            <label for="propuestaTitulo" class="form-label">Seleccionar Propuesta:</label>
            <select class="form-select" id="propuestaTitulo" name="propuestaTitulo">
                <option value="">--Seleccione una propuesta--</option>
                <c:forEach var="prop" items="${propuestas}">
                    <option value="${prop.titulo}">${prop.titulo} (Proponente: ${prop.proponenteNick})</option>
                </c:forEach>
            </select>
        </div>

        <div class="mb-3">
            <label for="tipoRetorno" class="form-label">Tipo de Retorno:</label>
            <select class="form-select" id="tipoRetorno" name="tipoRetorno">
                <c:forEach var="retorno" items="${T(com.culturarte.logica.enums.ETipoRetorno).values()}">
                    <option value="${retorno}">${retorno}</option>
                </c:forEach>
            </select>
        </div>

        <div class="mb-3">
            <label for="monto" class="form-label">Monto:</label>
            <input type="number" class="form-control" id="monto" name="monto" min="1" step="1">
        </div>

        <button type="submit" class="btn btn-primary">Registrar Colaboración</button>
    </form>

</div>

<script src="js/registrarColaboracion.js"></script>
<%@ include file="footer.jsp" %>
