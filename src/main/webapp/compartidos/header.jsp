<%--
  Created by IntelliJ IDEA.
  User: guillerodriguezz
  Date: 4/10/25
  Time: 19:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<nav class="navbar navbar-expand-lg navbar-dark bg-primary fixed-top shadow-sm">
    <div class="container-fluid">
        <a class="navbar-brand fw-bold" href="index.jsp">🎭 Culturarte</a>

        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarCulturarte">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarCulturarte">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                <li class="nav-item"><a class="nav-link" href="altaPropuesta">Tengo una Propuesta</a></li>
                <li class="nav-item"><a class="nav-link" href="consultarPropuesta">Quiero ver Propuestas</a></li>
            </ul>

            <!-- búsqueda -->
            <form class="d-flex" action="buscarPropuesta" method="get">
                <input class="form-control me-2" type="search" name="q" placeholder="Título, descripción, lugar" aria-label="Buscar">
                <button class="btn btn-light" type="submit">Buscar</button>
            </form>

            <!-- sesión -->
            <ul class="navbar-nav ms-3">
                <li class="nav-item"><a class="nav-link" href="#">Registrarse</a></li>
                <li class="nav-item"><a class="nav-link" href="#">Entrar</a></li>
            </ul>
        </div>
    </div>
</nav>

<!-- Margen para que no tape el contenido -->
<div style="height: 70px;"></div>

