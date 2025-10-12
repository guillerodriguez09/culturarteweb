<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="compartidos/header.jsp" %>

<div class="container mt-5">
    <h2>Seguir Usuario</h2>

    <c:if test="${not empty mensajeExito}">
        <div class="alert alert-success">${mensajeExito}</div>
    </c:if>
    <c:if test="${not empty mensajeError}">
        <div class="alert alert-danger">${mensajeError}</div>
    </c:if>

    <form id="formSeguirUsuario" action="seguirUsuario" method="post">
        <div class="mb-3">
            <label for="usuarioSeguido" class="form-label">Seleccione usuario a seguir:</label>
            <select class="form-select" id="usuarioSeguido" name="usuarioSeguido">
                <option value="">-- Seleccione --</option>
                <c:forEach var="u" items="${usuarios}">
                    <option value="${u}">${u}</option>
                </c:forEach>
            </select>
        </div>

        <button type="submit" class="btn btn-primary">Seguir</button>
    </form>
</div>

<script src="js/seguirUsuario.js"></script>
<%@ include file="compartidos/footer.jsp" %>