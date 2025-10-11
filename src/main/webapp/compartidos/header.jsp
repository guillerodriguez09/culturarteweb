<%--
  Created by IntelliJ IDEA.
  User: guillerodriguezz
  Date: 4/10/25
  Time: 19:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Culturarte</title>


    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="css/general.css" rel="stylesheet">
</head>

<body class="d-flex flex-column min-vh-100 bg-light">

<!-- NAVBAR -->
<nav class="navbar navbar-expand-lg navbar-dark bg-primary fixed-top shadow-sm">
    <div class="container-fluid">
        <a class="navbar-brand fw-bold" href="${pageContext.request.contextPath}/index.jsp">🎭 Culturarte</a>

        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarCulturarte">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarCulturarte">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                <li class="nav-item"><a class="nav-link" href="altaPropuesta">Tengo una Propuesta</a></li>
                <li class="nav-item"><a class="nav-link" href="consultarPropuesta">Ver Propuestas</a></li>
            </ul>

            <!-- Campo de búsqueda -->
            <form class="d-flex" action="buscarPropuesta" method="get">
                <input class="form-control me-2" type="search" name="q"
                       placeholder="Título, descripción, lugar" aria-label="Buscar">
                <button class="btn btn-light" type="submit">Buscar</button>
            </form>

            <!-- Sesión -->
            <ul class="navbar-nav ms-3">
                <c:choose>
                    <c:when test="${not empty sessionScope.sesion.nickOMail}">
                        <li class="nav-item"><a class="nav-link" href="perfil.jsp">${sessionScope.sesion.nickOMail}</a></li>
                        <li class="nav-item"><a class="nav-link" href="cerrarSesion">Salir</a></li>
                    </c:when>
                    <c:otherwise>
                        <li class="nav-item"><a class="nav-link" href="altaPerfil.jsp">Registrarse</a></li>
                        <li class="nav-item"><a class="nav-link" href="inicioSesion.jsp">Entrar</a></li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>
    </div>
</nav>

<!-- Margen superior para evitar que el contenido quede tapado por la navbar -->
<div style="margin-top: 80px;"></div>


