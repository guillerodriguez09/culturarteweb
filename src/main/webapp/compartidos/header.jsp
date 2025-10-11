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
                <!-- Nuevo enlace siempre visible -->
                <li class="nav-item"><a class="nav-link" href="registrarColaboracion">Registrar Colaboración</a></li>
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
