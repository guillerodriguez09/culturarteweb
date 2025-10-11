<%--
  Created by IntelliJ IDEA.
  User: guillerodriguezz
  Date: 9/10/25
  Time: 22:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="compartidos/header.jsp"/>

<div class="container mt-5 mb-5">
    <div class="row justify-content-center">
        <div class="col-md-5">
            <div class="card shadow-lg border-0">
                <div class="card-header bg-primary text-white text-center">
                    <h4 class="mb-0">Iniciar Sesión</h4>
                </div>

                <div class="card-body">

                    <!-- Mostrar error si llega desde el servlet -->
                    <c:if test="${not empty error}">
                        <div class="alert alert-danger text-center">${error}</div>
                    </c:if>

                    <!-- Formulario -->
                    <form action="inicioSesion" method="post" class="needs-validation" novalidate>
                        <div class="mb-3">
                            <label for="nickOMail" class="form-label">Usuario o Correo</label>
                            <input type="text" class="form-control" id="nickOMail" name="nickOMail" required
                                   placeholder="Ej: guille">
                            <div class="invalid-feedback">Debe ingresar su usuario o correo.</div>
                        </div>

                        <div class="mb-3">
                            <label for="contrasenia" class="form-label">Contraseña</label>
                            <input type="password" class="form-control" id="contrasenia" name="contrasenia" required>
                            <div class="invalid-feedback">Debe ingresar su contraseña.</div>
                        </div>

                        <button type="submit" class="btn btn-primary w-100">Entrar</button>
                    </form>

                    <div class="text-center mt-3">
                        <p class="mb-1">¿No tienes cuenta?
                            <a href="altaPerfil.jsp" class="link-primary">Regístrate aquí</a>
                        </p>
                        <a href="index.jsp" class="small text-muted">Volver al inicio</a>
                    </div>

                </div>
            </div>
        </div>
    </div>

    <% if(request.getAttribute("mensaje") != null) { %>
    <div class="alert alert-info mt-3">
        <%= request.getAttribute("mensaje") %>
    </div>
    <% } %>

</div>

<script src="js/validacionesGral.js"></script>
<script src="js/inicioSesion.js" ></script>

<script>
    document.querySelector("form").onsubmit = validarInicioSesion;
</script>

<jsp:include page="compartidos/footer.jsp"/>

