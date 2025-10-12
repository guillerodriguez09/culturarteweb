<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Culturarte Web</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="css/general.css" rel="stylesheet">
</head>
<%@ include file="compartidos/header.jsp" %>

<div class="container mt-4">
    <h1 class="mb-4">Bienvenido a Culturarte Web</h1>

    <div class="list-group">
        <a href="altaPropuesta" class="list-group-item list-group-item-action">
            Alta de Propuesta
        </a>
        <a href="consultarPropuesta" class="list-group-item list-group-item-action">
            Consulta de Propuesta
        </a>
        <a href="registrarColaboracion" class="list-group-item list-group-item-action">
            Registrar Colaboración
        </a>
        <a href="seguirUsuario" class="list-group-item list-group-item-action">
            Seguir Usuario
        </a>
    </div>
</div>

<%@ include file="compartidos/footer.jsp" %>
</html>
