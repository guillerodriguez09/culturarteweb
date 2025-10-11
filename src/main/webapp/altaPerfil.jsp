<%--
  Created by IntelliJ IDEA.
  User: Chorizo-Cosmico
  Date: 11/10/2025
  Time: 11:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Alta de Perfil</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="css/general.css" rel="stylesheet">
    <link href="css/altaPropuesta.css" rel="stylesheet">
</head>
<body>

<jsp:include page="compartidos/header.jsp"/>
<div class="container mt-5">
    <div class="card shadow-lg">
        <div class="card-header bg-primary text-white">
            <h3 class="mb-0">Alta de Perfil</h3>
        </div>
        <div class ="card-body">
            <form action=altaPerfil" method="post" enctype="multipart/form-data">
                <div class="mb-3">
                    <label for="nick" class="form-label">Nickname</label>
                    <input type="text" class="form-control" id="nick" name="nick" required>
                </div>
                <div class="mb-3">
                    <label for="nombre" class="form-label">Nombre</label>
                    <input type="text" class="form-control" id="nombre" name="nombre" required>
                </div>
                <div class="mb-3">
                    <label for="apellido" class="form-label">Apellido</label>
                    <input type="text" class="form-control" id="apellido" name="apellido" required>
                </div>
                <div class="mb-3">
                    <label for="contrasenia" class="form-label">Contraseña</label>
                    <input type="text" class="form-control" id="contrasenia" name="contrasenia" required>
                </div>
                <div class="mb-3">
                    <label for="correo" class="form-label">Correo</label>
                    <input type="text" class="form-control" id="correo" name="correo" required>
                </div>
                    <div class="mb-3">
                        <label for="fechaNac" class="form-label">Fecha de Nacimiento</label>
                        <input type="date" class="form-control" id="fechaNac" name="fechaNac" required>
                    </div>
                <div class="mb-3">
                    <label for="dirImagen" class="form-label">Imagen (Opcional)</label>
                    <input type="file" class="form-control" id="dirImagen" name="dirImagen" accept="image/*">
                </div>

                <button type="submit" class="btn btn-success w-100">Crear Perfil</button>
            </form>
        </div>
    </div>

    <% if (request.getAttribute("mensaje") != null){ %>
    <div class="alert alert-info mt-3">
        <%= request.getAttribute("mensaje")%>
    </div>
    <% } %>

</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="js/validacionesGral.js"></script>
<script src="js/altaPerfil.js"></script>

<script>
    document.querySelector("form").onsubmit = validarAltaPerfil;
</script>

</body>
</html>
