<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="compartidos/header.jsp" %>

<%
    // recomendaciones al entrar al index
    HttpSession ses = request.getSession(false);
    if (ses != null && ses.getAttribute("nick") != null) {

        com.culturarte.web.ws.cliente.IPropuestaController propCtrl =
                (com.culturarte.web.ws.cliente.IPropuestaController)
                        application.getAttribute("ws.propuesta");

        String nickActual = (String) ses.getAttribute("nick");

        try {
            java.util.List<com.culturarte.web.ws.cliente.DtoPropuesta> nuevas =
                    propCtrl.recomendarPropuestas(nickActual);

            ses.setAttribute("recomendaciones", nuevas);

        } catch (Exception e) {
            // (opcional) podés loguearlo si querés
            System.out.println("No se pudieron recargar recomendaciones: " + e.getMessage());
        }
    }
%>

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

    <c:if test="${not empty sessionScope.recomendaciones}">
        <h3 class="text-center mb-4">Propuestas Recomendadas para ti</h3>

        <div class="row">
            <c:forEach var="prop" items="${sessionScope.recomendaciones}">
                <div class="col-md-4 mb-4">
                    <div class="card h-100">

                        <c:if test="${not empty prop.imagen}">
                            <img src="${prop.imagen}" class="card-img-top" alt="${prop.titulo}" style="object-fit: cover; height: 200px;">
                        </c:if>
                        <c:if test="${empty prop.imagen}">
                            <img src="imagenes/404.png" class="card-img-top" alt="Sin imagen" style="object-fit: cover; height: 200px;">
                        </c:if>

                        <div class="card-body d-flex flex-column">
                            <h5 class="card-title">${prop.titulo}</h5>
                            <span class="badge bg-warning text-dark" title="Puntaje de recomendación">
                                ★ ${prop.puntaje}
                            </span>

                            <a href="consultarPropuesta?titulo=${prop.titulo}" class="btn btn-primary mt-auto">
                                Ver la Propuesta
                            </a>
                        </div>
                        <div class="card-footer">
                            <small class="text-muted">Monto recaudado: $${prop.montoRecaudado}</small>
                        </div>
                    </div>
                </div>

            </c:forEach>
        </div>
    </c:if>

</main>

<%@ include file="compartidos/footer.jsp" %>
