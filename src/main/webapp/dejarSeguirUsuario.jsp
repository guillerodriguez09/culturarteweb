<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="compartidos/header.jsp" %>

<div class="container mt-5">
    <div class="card shadow-lg">
        <div class="card-header bg-primary text-white">
            <h3 class="mb-0">Dejar de Seguir Usuario</h3>
        </div>
        <div class="card-body">

            <form id="formDejarSeguir" method="post" action="dejarSeguirUsuario">
                <div class="mb-3">
                    <label for="usuarioSeguido" class="form-label">Seleccione el usuario a dejar de seguir:</label>
                    <select class="form-select" id="usuarioSeguido" name="usuarioSeguido">
                        <option value="">-- Seleccione --</option>
                        <c:forEach var="usuario" items="${usuariosSeguidos}">
                            <option value="${usuario}">${usuario}</option>
                        </c:forEach>
                    </select>
                </div>

                <button type="submit" class="btn btn-danger">Dejar de Seguir</button>
            </form>

            <c:if test="${not empty mensajeExito}">
                <div class="alert alert-success mt-3">${mensajeExito}</div>
            </c:if>
            <c:if test="${not empty mensajeError}">
                <div class="alert alert-danger mt-3">${mensajeError}</div>
            </c:if>

        </div>
    </div>
</div>

<script src="js/dejarSeguirUsuario.js"></script>
<%@ include file="compartidos/footer.jsp" %>
