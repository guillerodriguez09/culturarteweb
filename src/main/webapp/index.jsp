<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="compartidos/header.jsp" %>

<main class="container mt-5 mb-5 flex-grow-1">

    <%
        String mensaje = (String) session.getAttribute("mensaje");
        if (mensaje != null) {
    %>
    <div class="container mt-3">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            <%= mensaje %>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </div>
    <%
            session.removeAttribute("mensaje");
        }
    %>

    <div class="text-center mb-5">
        <h1 class="fw-bold text-primary">🎭 Bienvenido a Culturarte</h1>
        <p class="lead text-muted">Descubrí, apoyá y compartí propuestas culturales.</p>
    </div>

    <div class="row justify-content-center mb-5">
        <div class="col-md-3 col-sm-6 mb-3">
            <a href="altaPropuesta" class="btn btn-outline-primary w-100 shadow-sm py-3">
                <i class="bi bi-plus-circle me-2"></i> Alta de Propuesta
            </a>
        </div>
        <div class="col-md-3 col-sm-6 mb-3">
            <a href="consultarPropuesta" class="btn btn-outline-primary w-100 shadow-sm py-3">
                <i class="bi bi-search me-2"></i> Consultar Propuestas
            </a>
        </div>
    </div>
</main>

<%@ include file="compartidos/footer.jsp" %>

