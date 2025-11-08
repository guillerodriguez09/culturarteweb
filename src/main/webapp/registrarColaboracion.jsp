<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="container mt-5">
    <div class="card shadow-lg">
        <div class="card-header bg-primary text-white">
            <h3 class="mb-0">Registrar Colaboración a Propuesta</h3>
        </div>
        <div class="card-body">
            <c:choose>
                <c:when test="${not empty propuesta}">
                    <div class="mb-3">
                        <h5>Título: ${propuesta.titulo}</h5>
                        <p><strong>Proponente:</strong> ${propuesta.proponente}</p>
                        <p><strong>Descripción:</strong> ${propuesta.descripcion}</p>
                        <p><strong>Monto requerido:</strong> ${propuesta.monto}</p>
                        <p><strong>Fecha de publicación:</strong> ${propuesta.fecha}</p>
                    </div>

                    <form method="post" action="RegistrarColaboracionServlet">
                        <input type="hidden" name="propuesta" value="${propuesta.titulo}">

                        <div class="mb-3">
                            <label for="tipoRetorno" class="form-label">Tipo de Retorno:</label>
                            <select class="form-select" id="tipoRetorno" name="tipoRetorno" required>
                                <option value="">-- Seleccione --</option>
                                <c:forEach var="retorno" items="${tiposRetorno}">
                                    <option value="${retorno}">${retorno}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="mb-3">
                            <label for="monto" class="form-label">Monto de la colaboración:</label>
                            <input type="number" step="0.01" class="form-control" id="monto" name="monto" required>
                        </div>

                        <button type="submit" class="btn btn-success">Confirmar Colaboración</button>
                        <a href="ConsultarPropuestaServlet" class="btn btn-secondary">Cancelar</a>
                    </form>

                    <c:if test="${not empty mensajeExito}">
                        <div class="alert alert-success mt-3">${mensajeExito}</div>
                    </c:if>
                    <c:if test="${not empty mensajeError}">
                        <div class="alert alert-danger mt-3">${mensajeError}</div>
                    </c:if>
                </c:when>

                <c:otherwise>
                    <div class="alert alert-warning">
                        No se ha seleccionado ninguna propuesta. Vuelva a la pantalla de consulta para elegir una.
                    </div>
                    <a href="ConsultarPropuestaServlet" class="btn btn-primary">Volver</a>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

