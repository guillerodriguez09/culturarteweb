<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.culturarte.logica.dtos.DTOColaborador" %>
<%@ page import="com.culturarte.logica.dtos.DTOProponente" %>
<%@ page import="com.culturarte.logica.dtos.DTOPropuesta" %>
<%@ page import="com.culturarte.logica.dtos.DTOColabConsulta" %>
<%@ page import="java.util.*, com.culturarte.logica.dtos.DTOProponente" %>
<%@ page import="java.util.*, com.culturarte.logica.dtos.DTOColaborador" %>
<%@ page import="java.util.*, com.culturarte.logica.dtos.DTOPropuesta" %>

<%-- !! IMPORTAR ICONOS !! --%>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">

<html>
<head>
    <meta charset="UTF-8">
    <title>Consulta de Perfiles</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="css/general.css" rel="stylesheet">
</head>
<body>
<%@ include file="compartidos/header.jsp" %>

<%-- obtenemos DTO y flags primero para usarlos en toda la página --%>
<%
    boolean esPropioPerfil = (request.getAttribute("esPropioPerfil") != null) ?
            (Boolean) request.getAttribute("esPropioPerfil") : false;

    DTOProponente pro = (DTOProponente) request.getAttribute("proponenteSeleccionado");
    DTOColaborador col = (DTOColaborador) request.getAttribute("colaboradorSeleccionado");

    // Para saber quién está activo en la lista de la izquierda
    String nickSeleccionado = (pro != null) ? pro.getNick() : (col != null) ? col.getNick() : "";

    List<String> seguidoores = (List<String>) request.getAttribute("seguidooresNick");
    if(seguidoores == null) seguidoores = new ArrayList<>();

    List<String> seguidoos = (List<String>) request.getAttribute("seguidoosDeNick");
    if(seguidoos == null) seguidoos = new ArrayList<>();

    List<DTOPropuesta> propuestasDeProponente = (List<DTOPropuesta>) request.getAttribute("propuestasDeProponente");
    List<DTOPropuesta> colaboracionesDeColaborador = (List<DTOPropuesta>) request.getAttribute("colaboracionesDeColaborador");
    List<DTOColabConsulta> colabsDetalladas = (List<DTOColabConsulta>) request.getAttribute("colaboracionesDetalladas");
%>

<div class="container mt-4">
    <h2 class="text-center mb-4">Consulta de Perfiles</h2>

    <div class="row">

        <%-- col izq, lista de users --%>

        <div class="col-md-5">
            <div class="list-group" style="max-height: 80vh; overflow-y: auto;">
                <%
                    List<DTOProponente> listaProp = (List<DTOProponente>) request.getAttribute("proponentes");
                    List<DTOColaborador> listaCol = (List<DTOColaborador>) request.getAttribute("colaboradores");
                %>

                <div class="list-group-item list-group-item-light fw-bold sticky-top bg-white">
                    <i class="bi bi-person-gear me-2"></i> Proponentes
                </div>
                <%
                    if(listaProp != null && !listaProp.isEmpty()){
                        for(DTOProponente p : listaProp){
                            String activeClass = (p.getNick().equals(nickSeleccionado)) ? "active" : "";
                %>
                <form action="consultaPerfil" method="post" class="list-group-item list-group-item-action d-flex justify-content-between align-items-center <%= activeClass %>">
                    <div>
                        <strong><%= p.getNick() %></strong><br>
                        <small class="text-muted"><%= p.getNombre() %> <%= p.getApellido() %></small>
                    </div>
                    <input type="hidden" name="tipoUsr" value="proponente">
                    <input type="hidden" name="nick" value="<%= p.getNick() %>">
                    <button type="submit" class="btn btn-outline-primary btn-sm">Ver</button>
                </form>
                <% } } else { %>
                <p class="list-group-item text-muted">No hay proponentes</p>
                <% } %>

                <div class="list-group-item list-group-item-light fw-bold sticky-top bg-white">
                    <i class="bi bi-person me-2"></i> Colaboradores
                </div>
                <%
                    if(listaCol != null && !listaCol.isEmpty()){
                        for(DTOColaborador c : listaCol){
                            String activeClass = (c.getNick().equals(nickSeleccionado)) ? "active" : "";
                %>
                <form action="consultaPerfil" method="post" class="list-group-item list-group-item-action d-flex justify-content-between align-items-center <%= activeClass %>">
                    <div>
                        <strong><%= c.getNick() %></strong><br>
                        <small class="text-muted"><%= c.getNombre() %> <%= c.getApellido() %></small>
                    </div>
                    <input type="hidden" name="tipoUsr" value="colaborador">
                    <input type="hidden" name="nick" value="<%= c.getNick() %>">
                    <button type="submit" class="btn btn-outline-primary btn-sm">Ver</button>
                </form>
                <% } } else { %>
                <p class="list-group-item text-muted">No hay colaboradores</p>
                <% } %>
            </div>
        </div>
        <%-- fin COLUMNA IZQUIERDA --%>



        <%-- col derecha (detalle del perfil) --%>

        <div class="col-md-7">

            <%-- hay un proponente seleccionado --%>
            <% if(pro != null){ %>
            <div class="card shadow-sm">
                <div class="card-body">
                    <div class="row">
                        <div class="col-md-4 text-center">
                            <% if(pro.getDirImagen() != null && !pro.getDirImagen().isEmpty()){  %>
                            <img src="<%= request.getContextPath() + "/" + pro.getDirImagen() %>" class="img-fluid rounded-circle shadow-sm" alt="Imagen perfil">
                            <% }else{ %>
                            <img src="<%= request.getContextPath() + "/imagenes/404.png" %>" class="img-fluid rounded-circle shadow-sm" alt="Sin imagen disponible">
                            <% } %>

                            <h5 class="mt-3"><%= pro.getNick() %></h5>
                            <span class="badge bg-primary">Proponente</span>

                            <div class="d-flex justify-content-around text-center mt-3 pt-3 border-top">
                                <div>
                                    <h5 class="mb-0"><%= seguidoores.size() %></h5>
                                    <small class="text-muted" data-bs-toggle="collapse" href="#listaSeguidores" style="cursor: pointer;">Seguidores</small>
                                </div>
                                <div>
                                    <h5 class="mb-0"><%= seguidoos.size() %></h5>
                                    <small class="text-muted" data-bs-toggle="collapse" href="#listaSeguidos" style="cursor: pointer;">Seguidos</small>
                                </div>
                            </div>
                        </div>

                        <div class="col-md-8">
                            <ul class="list-group list-group-flush">
                                <li class="list-group-item d-flex justify-content-between">
                                    <strong>Nombre:</strong>
                                    <span><%= pro.getNombre() %> <%= pro.getApellido() %></span>
                                </li>
                                <li class="list-group-item d-flex justify-content-between">
                                    <strong>Email:</strong>
                                    <span><%= pro.getCorreo() %></span>
                                </li>
                                <li class="list-group-item d-flex justify-content-between">
                                    <strong>Nacimiento:</strong>
                                    <span><%= pro.getFechaNac() %></span>
                                </li>
                                <li class="list-group-item">
                                    <strong>Dirección:</strong><br>
                                    <span class="text-muted"><%= pro.getDireccion() %></span>
                                </li>
                                <li class="list-group-item">
                                    <strong>Biografía:</strong><br>
                                    <small class="text-muted"><%= pro.getBiografia() %></small>
                                </li>
                            </ul>
                        </div>
                    </div>

                    <div class="collapse" id="listaSeguidores">
                        <div class="card card-body mt-3">
                            <h6 class="card-title">Seguidores</h6>
                            <ul><% for (String s : seguidoores){ %><li><%= s %></li><% } %></ul>
                        </div>
                    </div>
                    <div class="collapse" id="listaSeguidos">
                        <div class="card card-body mt-3">
                            <h6 class="card-title">Seguidos</h6>
                            <ul><% for (String s : seguidoos){ %>

                                <form class="d-flex ms-auto" action="dejarSeguirUsuario" method="post" novalidate>

                                    <div>
                                    <label for="usuarioSeguido" class="form-label"><%= s %></label>
                                    <input type="text" class="form-control" value="<%=s%>" id="usuarioSeguido" name="usuarioSeguido" style="display:none">

                                <%-- <li ><%= s %></li> --%>
                                    </div>
                                    <button type="submit" class="d-flex ms-auto btn btn-outline-danger shadow-sm py-3 bi bi-person-dash"> Dejar de seguir</button>
                                </form>

                                <% } %></ul>
                        </div>
                    </div>

                    <hr>
                    <h5 class="mt-3">Propuestas</h5>
                    <ul class="list-group list-group-flush">
                        <%
                            if(propuestasDeProponente != null && !propuestasDeProponente.isEmpty()){
                                boolean hayPropuestasVisibles = false;
                                for(DTOPropuesta p : propuestasDeProponente){
                                    boolean esIngresada = "INGRESADA".equalsIgnoreCase(p.getEstadoActual());
                                    boolean mostrar = esPropioPerfil || !esIngresada;

                                    if (mostrar) {
                                        hayPropuestasVisibles = true;
                                        String collapseId = "propuesta-" + Math.abs(p.getTitulo().hashCode());
                        %>
                        <li class="list-group-item">
                            <a class="btn btn-link p-0" data-bs-toggle="collapse" href="#<%= collapseId %>">
                                <%= p.getTitulo() %>
                                <% if(esPropioPerfil && esIngresada) { %>
                                <span class="badge bg-secondary ms-2">Ingresada</span>
                                <% } %>
                            </a>
                            <div class="collapse mt-2" id="<%= collapseId %>">
                                <div class="card card-body">
                                    <p><strong>Descripción: </strong><%= p.getDescripcion() %></p>
                                    <p><strong>Lugar:</strong> <%= p.getLugar() %></p>
                                    <p><strong>Estado:</strong> <%= p.getEstadoActual() %></p>
                                    <p><strong>Monto a reunir:</strong> $<%= p.getMontoAReunir() %></p>
                                    <p><strong>Monto recaudado:</strong> $<%= p.getMontoRecaudado() %></p>

                                    <% if (p.getImagen() != null && !p.getImagen().isEmpty()) { %>
                                    <img src="<%= request.getContextPath() + "/" + p.getImagen() %>"
                                         class="img-fluid rounded shadow-sm" alt="Imagen de la propuesta">
                                    <% } else { %>
                                    <img src="<%= request.getContextPath() + "/imagenes/404.png" %>"
                                         class="img-fluid rounded shadow-sm" alt="Sin imagen disponible">
                                    <% } %>
                                </div>
                            </div>
                        </li>
                        <% } } if (!hayPropuestasVisibles) { %>
                        <p class="text-muted p-2">No tiene propuestas visibles.</p>
                        <% } } else { %>
                        <p class="text-muted p-2">No tiene propuestas.</p>
                        <% } %>
                    </ul>
                </div>
            </div>

            <%-- hay colaborador seleccionado --%>
            <% } else if(col != null) { %>
            <div class="card shadow-sm">
                <div class="card-body">
                    <div class="row">
                        <div class="col-md-4 text-center">
                            <% if(col.getDirImagen() != null && !col.getDirImagen().isEmpty()){  %>
                            <img src="<%= request.getContextPath() + "/" + col.getDirImagen() %>" class="img-fluid rounded-circle shadow-sm" alt="Imagen perfil">
                            <% }else{ %>
                            <img src="<%= request.getContextPath() + "/imagenes/404.png" %>" class="img-fluid rounded-circle shadow-sm" alt="Sin imagen disponible">
                            <% } %>

                            <h5 class="mt-3"><%= col.getNick() %></h5>
                            <span class="badge bg-secondary">Colaborador</span>

                            <div class="d-flex justify-content-around text-center mt-3 pt-3 border-top">
                                <div>
                                    <h5 class="mb-0"><%= seguidoores.size() %></h5>
                                    <small class="text-muted" data-bs-toggle="collapse" href="#listaSeguidores" style="cursor: pointer;">Seguidores</small>
                                </div>
                                <div>
                                    <h5 class="mb-0"><%= seguidoos.size() %></h5>
                                    <small class="text-muted" data-bs-toggle="collapse" href="#listaSeguidos" style="cursor: pointer;">Seguidos</small>
                                </div>
                            </div>
                        </div>

                        <div class="col-md-8">
                            <ul class="list-group list-group-flush">
                                <li class="list-group-item d-flex justify-content-between">
                                    <strong>Nombre:</strong>
                                    <span><%= col.getNombre() %> <%= col.getApellido() %></span>
                                </li>
                                <li class="list-group-item d-flex justify-content-between">
                                    <strong>Email:</strong>
                                    <span><%= col.getCorreo() %></span>
                                </li>
                                <li class="list-group-item d-flex justify-content-between">
                                    <strong>Nacimiento:</strong>
                                    <span><%= col.getFechaNac() %></span>
                                </li>
                            </ul>
                        </div>
                    </div>

                    <div class="collapse" id="listaSeguidores">
                        <div class="card card-body mt-3">
                            <h6 class="card-title">Seguidores</h6>
                            <ul><% for (String s : seguidoores){ %><li><%= s %></li><% } %></ul>
                        </div>
                    </div>
                    <div class="collapse" id="listaSeguidos">
                        <div class="card card-body mt-3">
                            <h6 class="card-title">Seguidos</h6>
                            <ul><% for (String s : seguidoos){ %><li><%= s %></li><% } %></ul>
                        </div>
                    </div>

                    <hr>
                    <h5 class="mt-3">Colaboraciones</h5>
                    <ul class="list-group list-group-flush">
                        <%
                            // es perfil propio (muestra monto y fecha)
                            if (esPropioPerfil && colabsDetalladas != null && !colabsDetalladas.isEmpty()) {
                                for (DTOColabConsulta c : colabsDetalladas) {
                        %>
                        <li class="list-group-item">
                            <strong><%= c.getPropuestaNombre() %></strong>
                            <ul class="list-unstyled ms-3 mt-2 small text-muted">
                                <li><strong>Monto:</strong> $<%= c.getMonto() %></li>
                                <li><strong>Fecha:</strong> <%= c.getFecha() != null ? c.getFecha().toLocalDate() : "N/A" %></li>
                            </ul>
                        </li>
                        <% } } else if (colaboracionesDeColaborador != null && !colaboracionesDeColaborador.isEmpty()) {
                            // es perfil ajeno (muestra collapse con detalles)
                            for(DTOPropuesta p : colaboracionesDeColaborador){
                                String collapseId = "propuesta-" + Math.abs(p.getTitulo().hashCode());
                        %>
                        <li class="list-group-item">
                            <a class="btn btn-link p-0" data-bs-toggle="collapse" href="#<%= collapseId %>">
                                <%= p.getTitulo() %>
                            </a>
                            <div class="collapse mt-2" id="<%= collapseId %>">
                                <div class="card card-body">
                                    <p><strong>Descripción: </strong><%= p.getDescripcion() %></p>
                                    <p><strong>Lugar:</strong> <%= p.getLugar() %></p>
                                    <p><strong>Estado:</strong> <%= p.getEstadoActual() %></p>
                                    <p><strong>Monto a reunir:</strong> $<%= p.getMontoAReunir() %></p>
                                    <p><strong>Monto recaudado:</strong> $<%= p.getMontoRecaudado() %></p>

                                    <% if (p.getImagen() != null && !p.getImagen().isEmpty()) { %>
                                    <img src="<%= request.getContextPath() + "/" + p.getImagen() %>"
                                         class="img-fluid rounded shadow-sm" alt="Imagen de la propuesta"> <%-- Clase cambiada a img-fluid --%>
                                    <% } else { %>
                                    <img src="<%= request.getContextPath() + "/imagenes/404.png" %>"
                                         class="img-fluid rounded shadow-sm" alt="Sin imagen disponible"> <%-- Clase cambiada a img-fluid --%>
                                    <% } %>
                                </div>
                            </div>
                        </li>
                        <% } } else { %>
                        <p class="text-muted p-2">No tiene colaboraciones.</p>
                        <% } %>
                    </ul>
                </div>
            </div>

            <%-- si aun no selecciona nada --%>
            <% } else { %>
            <div class="d-flex align-items-center justify-content-center bg-light rounded-3 text-center" style="min-height: 50vh;">
                <div>
                    <i class="bi bi-people-fill text-muted" style="font-size: 4rem;"></i>
                    <h5 class="mt-3 text-muted">Selecciona un usuario</h5>
                    <p class="text-muted mb-0">Elige un perfil de la lista para ver sus detalles.</p>
                </div>
            </div>
            <% } %>

        </div>
        <%-- columna derecha--%>

    </div>

</div>

<%@ include file="compartidos/footer.jsp" %>
</body>
</html>
