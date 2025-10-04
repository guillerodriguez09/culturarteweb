<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Culturarte Web</title>
    <link href="css/general.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="container py-4">
<%@ include file="compartidos/header.jsp" %>
<h1 class="mb-4">Bienvenido a Culturarte Web</h1>

<div class="list-group">
    <a href="altaPropuesta" class="list-group-item list-group-item-action">
        Alta de Propuesta
    </a>
    <a href="consultarPropuesta" class="list-group-item list-group-item-action">
        Consulta de Propuesta
    </a>
</div>

</body>
</html>
