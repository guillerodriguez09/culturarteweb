<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.culturarte.web.ws.cliente.*" %>
<%@ page import="java.util.*" %>

<html>
<head>
    <meta charset="UTF-8">
    <title>Consulta de Perfiles</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">
    <link href="css/general.css" rel="stylesheet">
</head>
<body>
<%@ include file="compartidos/header.jsp" %>
<%-- obtenemos DTO y flags primero para usarlos en toda la página --%>
<%
    boolean esPropioPerfil = (request.getAttribute("esPropioPerfil") != null) ?
            (Boolean) request.getAttribute("esPropioPerfil") : false;

    Object proObj = request.getAttribute("proponenteSeleccionado");
    Object colObj = request.getAttribute("colaboradorSeleccionado");

    // 2. Inicializamos las variables como null
    DtoProponente pro = null;
    DtoColaborador col = null;

    // 3. Usamos 'instanceof' para verificar el tipo ANTES de hacer el cast
    if (proObj instanceof DtoProponente) {
        pro = (DtoProponente) proObj;
    }

    if (colObj instanceof DtoColaborador) {
        col = (DtoColaborador) colObj;
    }

    // Para saber quién está activo en la lista de la izquierda
    String nickSeleccionado = (pro != null) ? pro.getNickname() : (col != null) ? col.getNickname() : "";

    List<String> seguidoores = (List<String>) request.getAttribute("seguidooresNick");
    if(seguidoores == null) seguidoores = new ArrayList<>();

    List<String> seguidoos = (List<String>) request.getAttribute("seguidoosDeNick");
    if(seguidoos == null) seguidoos = new ArrayList<>();

    List<DtoPropuesta> propuestasDeProponente = (List<DtoPropuesta>) request.getAttribute("propuestasDeProponente");
    List<DtoPropuesta> colaboracionesDeColaborador = (List<DtoPropuesta>) request.getAttribute("colaboracionesDeColaborador");
    List<DtoColabConsulta> colabsDetalladas = (List<DtoColabConsulta>) request.getAttribute("colaboracionesDetalladas");

    boolean loSigo = (request.getAttribute("loSigo") != null) ? (Boolean) request.getAttribute("loSigo") : false;
    String nickSesion = (String) session.getAttribute("nick");
%>

<div class="container mt-4">
    <h2 class="text-center mb-4">Consulta de Perfiles</h2>

    <div class="row">


        <div class="col-md-5">
            <div class="list-group" style="max-height: 80vh; overflow-y: auto;">
                <%
                    List<DtoProponente> listaProp = (List<DtoProponente>) request.getAttribute("proponentes");
                    List<DtoColaborador> listaCol = (List<DtoColaborador>) request.getAttribute("colaboradores");
                %>

                <div class="list-group-item list-group-item-light fw-bold sticky-top bg-white">
                    <i class="bi bi-person-gear me-2"></i> Proponentes
                </div>
                <%
                    if(listaProp != null && !listaProp.isEmpty()){
                        for(DtoProponente p : listaProp){
                            String activeClass = (p.getNickname().equals(nickSeleccionado)) ? "active" : "";
                %>
                <form action="consultaPerfil" method="post" class="list-group-item list-group-item-action d-flex justify-content-between align-items-center <%= activeClass %>">
                    <div>
                        <strong><%= p.getNickname() %></strong><br>
                        <small class="text-muted"><%= p.getNombre() %> <%= p.getApellido() %></small>
                    </div>
                    <input type="hidden" name="tipoUsr" value="proponente">
                    <input type="hidden" name="nick" value="<%= p.getNickname() %>">
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
                        for(DtoColaborador c : listaCol){
                            String activeClass = (c.getNickname().equals(nickSeleccionado)) ? "active" : "";
                %>
                <form action="consultaPerfil" method="post" class="list-group-item list-group-item-action d-flex justify-content-between align-items-center <%= activeClass %>">
                    <div>
                        <strong><%= c.getNickname() %></strong><br>
                        <small class="text-muted"><%= c.getNombre() %> <%= c.getApellido() %></small>
                    </div>
                    <input type="hidden" name="tipoUsr" value="colaborador">
                    <input type="hidden" name="nick" value="<%= c.getNickname() %>">
                    <button type="submit" class="btn btn-outline-primary btn-sm">Ver</button>
                </form>
                <% } } else { %>
                <p class="list-group-item text-muted">No hay colaboradores</p>
                <% } %>
            </div>
        </div>


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

                            <h5 class="mt-3"><%= pro.getNickname() %></h5>
                            <span class="badge bg-primary">Proponente</span>

                            <% if (nickSesion != null && !esPropioPerfil) { %>
                            <div class="mt-3">
                                <% if (loSigo) { %>
                                <form action="dejarSeguirUsuario" method="post" style="display: inline;">
                                    <input type="hidden" name="usuarioSeguido" value="<%= pro.getNickname() %>">
                                    <input type="hidden" name="tipoUsr" value="proponente">
                                    <button type="submit" class="btn btn-outline-danger btn-sm">
                                        <i class="bi bi-person-dash"></i> Dejar de Seguir
                                    </button>
                                </form>
                                <% } else { %>
                                <form action="seguirUsuario" method="post" style="display: inline;">
                                    <input type="hidden" name="usuarioSeguido" value="<%= pro.getNickname() %>">
                                    <input type="hidden" name="tipoUsr" value="proponente">
                                    <button type="submit" class="btn btn-primary btn-sm">
                                        <i class="bi bi-person-plus"></i> Seguir
                                    </button>
                                </form>
                                <% } %>
                            </div>
                            <% } %>

                            <div class="d-flex justify-content-around text-center mt-3 pt-3 border-top">
                                <div>
                                    <h5 class="mb-0"><%= seguidoores.size() %></h5>
                                    <small class="text-muted" data-bs-toggle="collapse" href="#listaSeguidores" style="cursor: pointer;">Seguidores</small>
                                </div>
                                <div>
                                    <h5 class="mb-0" id="contador-seguidos"><%= seguidoos.size() %></h5>
                                    <small class="text-muted" data-bs-toggle="collapse" href="#listaSeguidos" ...>Seguidos</small>
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
                            <ul class="list-group list-group-flush">
                                <% for (String s : seguidoos) { %>
                                <li class="list-group-item d-flex justify-content-between align-items-center" id="item-seguido-<%= s %>">
                                    <span><%= s %></span>
                                    <% if (esPropioPerfil) { %>
                                    <a href="#" class="btn btn-outline-danger btn-sm btn-seguir-ajax"
                                       data-action="dejar"
                                       data-nick="<%= s %>"
                                       data-remove-on-success="item-seguido-<%= s %>">
                                        Dejar de seguir
                                    </a>
                                    <% } %>
                                </li>
                                <% } %>
                            </ul>
                        </div>
                    </div>

                    <hr>
                    <h5 class="mt-3">Propuestas</h5>
                    <ul class="list-group list-group-flush">
                        <%
                            if(propuestasDeProponente != null && !propuestasDeProponente.isEmpty()){
                                boolean hayPropuestasVisibles = false;
                                for(DtoPropuesta p : propuestasDeProponente){
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

                            <h5 class="mt-3"><%= col.getNickname() %></h5>
                            <span class="badge bg-secondary">Colaborador</span>


                            <% if (nickSesion != null && !esPropioPerfil) { %>
                            <div class="mt-3">
                                <% if (loSigo) { %>
                                <form action="dejarSeguirUsuario" method="post" style="display: inline;">
                                    <input type="hidden" name="usuarioSeguido" value="<%= col.getNickname() %>">
                                    <input type="hidden" name="tipoUsr" value="colaborador">
                                    <button type="submit" class="btn btn-outline-danger btn-sm">
                                        <i class="bi bi-person-dash"></i> Dejar de Seguir
                                    </button>
                                </form>
                                <% } else { %>
                                <form action="seguirUsuario" method="post" style="display: inline;">
                                    <input type="hidden" name="usuarioSeguido" value="<%= col.getNickname() %>">
                                    <input type="hidden" name="tipoUsr" value="colaborador">
                                    <button type="submit" class="btn btn-primary btn-sm">
                                        <i class="bi bi-person-plus"></i> Seguir
                                    </button>
                                </form>
                                <% } %>
                            </div>
                            <% } %>

                            <div class="d-flex justify-content-around text-center mt-3 pt-3 border-top">
                                <div>
                                    <h5 class="mb-0"><%= seguidoores.size() %></h5>
                                    <small class="text-muted" data-bs-toggle="collapse" href="#listaSeguidores" style="cursor: pointer;">Seguidores</small>
                                </div>
                                <div>
                                    <h5 class="mb-0" id="contador-seguidos"><%= seguidoos.size() %></h5>
                                    <small class="text-muted" data-bs-toggle="collapse" href="#listaSeguidos" ...>Seguidos</small>
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
                            <ul class="list-group list-group-flush">
                                <% for (String s : seguidoos) { %>
                                <li class="list-group-item d-flex justify-content-between align-items-center" id="item-seguido-<%= s %>">
                                    <span><%= s %></span>
                                    <% if (esPropioPerfil) { %>
                                    <a href="#" class="btn btn-outline-danger btn-sm btn-seguir-ajax"
                                       data-action="dejar"
                                       data-nick="<%= s %>"
                                       data-remove-on-success="item-seguido-<%= s %>">
                                        Dejar de seguir
                                    </a>
                                    <% } %>
                                </li>
                                <% } %>
                            </ul>
                        </div>
                    </div>

                    <hr>
                    <h5 class="mt-3">Colaboraciones</h5>
                    <ul class="list-group list-group-flush">
                        <%
                            // es perfil propio (muestra monto y fecha)
                            if (esPropioPerfil && colabsDetalladas != null && !colabsDetalladas.isEmpty()) {
                                for (DtoColabConsulta c : colabsDetalladas) {
                        %>
                        <li class="list-group-item">
                            <strong><%= c.getPropuestaNombre() %></strong>
                            <ul class="list-unstyled ms-3 mt-2 small text-muted">
                                <li><strong>Monto:</strong> $<%= c.getMonto() %></li>
                                <li><strong>Fecha:</strong> <%= c.getFecha() != null ? c.getFecha(): "N/A" %></li>
                            </ul>
                        </li>
                        <% } } else if (colaboracionesDeColaborador != null && !colaboracionesDeColaborador.isEmpty()) {
                            // es perfil ajeno (muestra collapse con detalles)
                            for(DtoPropuesta p : colaboracionesDeColaborador){
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
                                         class="img-fluid rounded shadow-sm" alt="Imagen de la propuesta">
                                    <% } else { %>
                                    <img src="<%= request.getContextPath() + "/imagenes/404.png" %>"
                                         class="img-fluid rounded shadow-sm" alt="Sin imagen disponible">
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

<script>
    //agrego ajax para logica del seguir/dejardeseguir
    document.addEventListener("DOMContentLoaded", function() {

        const botonesAjax = document.querySelectorAll('.btn-seguir-ajax');

        botonesAjax.forEach(boton => {
            boton.addEventListener('click', function(event) {
                event.preventDefault();

                const nickUsuario = this.dataset.nick;
                const accion = this.dataset.action;
                const idElementoABorrar = this.dataset.removeOnSuccess;

                if (accion !== 'dejar') return;

                const formData = new FormData();
                formData.append('usuarioSeguido', nickUsuario);
                formData.append('ajax', 'true');

                fetch('dejarSeguirUsuario', {
                    method: 'POST',
                    body: formData
                })
                    .then(response => {
                        if (response.ok) {
                            // borrar de la lista
                            const elemento = document.getElementById(idElementoABorrar);
                            if (elemento) {
                                elemento.remove();
                            }

                            // actualizar el contador
                            const contador = document.getElementById('contador-seguidos');
                            if (contador) {
                                let num = parseInt(contador.innerText);
                                if (!isNaN(num) && num > 0) {
                                    contador.innerText = num - 1;
                                }
                            }
                        } else {
                            alert('Error al intentar dejar de seguir.');
                        }
                    })
                    .catch(error => {
                        console.error('Error de red:', error);
                        alert('Error de conexión.');
                    });
            });
        });
    });
</script>
</body>
</html>
