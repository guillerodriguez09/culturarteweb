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

<%
    Object usuarioObj = request.getAttribute("usuarioSeleccionado");
    String tipoPerfil = (String) request.getAttribute("tipoPerfil");

    boolean esPropioPerfil = (request.getAttribute("esPropioPerfil") != null)
            ? (Boolean) request.getAttribute("esPropioPerfil") : false;

    boolean loSigo = (request.getAttribute("loSigo") != null)
            ? (Boolean) request.getAttribute("loSigo") : false;

    String nickSesion = (String) session.getAttribute("nick");

    List<String> seguidoores = (List<String>) request.getAttribute("seguidooresNick");
    if (seguidoores == null) seguidoores = new ArrayList<>();
    List<String> seguidoos = (List<String>) request.getAttribute("seguidoosDeNick");
    if (seguidoos == null) seguidoos = new ArrayList<>();
    List<DtoPropuesta> propuestas = (List<DtoPropuesta>) request.getAttribute("propuestas");

    List<DtoProponente> listaProp = (List<DtoProponente>) request.getAttribute("proponentes");
    List<DtoColaborador> listaCol = (List<DtoColaborador>) request.getAttribute("colaboradores");


    String nickSeleccionado = "";
    String nombre = "";
    String apellido = "";
    String correo = "";
    String imagen = "";
    String biografia = "";
    String direccion = "";
    String tipoBadge = "";

    if ("PROPONENTE".equalsIgnoreCase(tipoPerfil) && usuarioObj instanceof DtoProponente) {
        DtoProponente p = (DtoProponente) usuarioObj;
        nickSeleccionado = p.getNickname();
        nombre = p.getNombre();
        apellido = p.getApellido();
        correo = p.getCorreo();
        biografia = p.getBiografia();
        direccion = p.getDireccion();
        imagen = p.getDirImagen();
        tipoBadge = "Proponente";
    } else if ("COLABORADOR".equalsIgnoreCase(tipoPerfil) && usuarioObj instanceof DtoColaborador) {
        DtoColaborador c = (DtoColaborador) usuarioObj;
        nickSeleccionado = c.getNickname();
        nombre = c.getNombre();
        apellido = c.getApellido();
        correo = c.getCorreo();
        imagen = c.getDirImagen();
        tipoBadge = "Colaborador";
    }
%>
<% System.out.println("TipoPerfil = " + tipoPerfil + " | Objeto real: " + (usuarioObj != null ? usuarioObj.getClass().getName() : "null")); %>


<div class="container mt-4">
    <h2 class="text-center mb-4">Consulta de Perfiles</h2>

    <div class="row">
        <!-- Lista izquierda -->
        <div class="col-md-5">
            <div class="list-group" style="max-height: 80vh; overflow-y: auto;">
                <div class="list-group-item list-group-item-light fw-bold sticky-top bg-white">
                    <i class="bi bi-person-gear me-2"></i> Proponentes
                </div>
                <% if (listaProp != null && !listaProp.isEmpty()) {
                    for (DtoProponente p : listaProp) {
                        String activeClass = (p.getNickname().equals(nickSeleccionado)) ? "active" : ""; %>
                <form action="consultaPerfil" method="post"
                      class="list-group-item list-group-item-action d-flex justify-content-between align-items-center <%= activeClass %>">
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

                <div class="list-group-item list-group-item-light fw-bold sticky-top bg-white mt-3">
                    <i class="bi bi-person me-2"></i> Colaboradores
                </div>
                <% if (listaCol != null && !listaCol.isEmpty()) {
                    for (DtoColaborador c : listaCol) {
                        String activeClass = (c.getNickname().equals(nickSeleccionado)) ? "active" : ""; %>
                <form action="consultaPerfil" method="post"
                      class="list-group-item list-group-item-action d-flex justify-content-between align-items-center <%= activeClass %>">
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

        <!-- Panel derecho -->
        <div class="col-md-7">
            <% if (usuarioObj != null) { %>
            <div class="card shadow-sm">
                <div class="card-body">
                    <div class="row">
                        <div class="col-md-4 text-center">
                            <% if (imagen != null && !imagen.isEmpty()) { %>
                            <img src="<%= request.getContextPath() + "/" + imagen %>"
                                 class="img-fluid rounded-circle shadow-sm" alt="Imagen perfil">
                            <% } else { %>
                            <img src="<%= request.getContextPath() + "/imagenes/404.png" %>"
                                 class="img-fluid rounded-circle shadow-sm" alt="Sin imagen">
                            <% } %>

                            <h5 class="mt-3"><%= nickSeleccionado %></h5>
                            <span class="badge bg-primary"><%= tipoBadge %></span>

                            <% if (nickSesion != null && !esPropioPerfil) { %>
                            <div class="mt-3">
                                <% if (loSigo) { %>
                                <form action="dejarSeguirUsuario" method="post">
                                    <input type="hidden" name="usuarioSeguido" value="<%= nickSeleccionado %>">
                                    <input type="hidden" name="tipoUsr" value="<%= tipoPerfil.toLowerCase() %>">
                                    <button type="submit" class="btn btn-outline-danger btn-sm">
                                        <i class="bi bi-person-dash"></i> Dejar de Seguir
                                    </button>
                                </form>
                                <% } else { %>
                                <form action="seguirUsuario" method="post">
                                    <input type="hidden" name="usuarioSeguido" value="<%= nickSeleccionado %>">
                                    <input type="hidden" name="tipoUsr" value="<%= tipoPerfil.toLowerCase() %>">
                                    <button type="submit" class="btn btn-primary btn-sm">
                                        <i class="bi bi-person-plus"></i> Seguir
                                    </button>
                                </form>
                                <% } %>
                            </div>
                            <% } %>
                        </div>

                        <div class="col-md-8">
                            <ul class="list-group list-group-flush">
                                <li class="list-group-item"><strong>Nombre:</strong> <%= nombre %> <%= apellido %></li>
                                <li class="list-group-item"><strong>Email:</strong> <%= correo %></li>
                                <% if (!direccion.isEmpty()) { %>
                                <li class="list-group-item"><strong>Dirección:</strong> <%= direccion %></li>
                                <% } %>
                                <% if (!biografia.isEmpty()) { %>
                                <li class="list-group-item"><strong>Biografía:</strong> <small class="text-muted"><%= biografia %></small></li>
                                <% } %>
                            </ul>
                        </div>
                    </div>

                    <hr>
                    <h5 class="mt-3"><%= tipoPerfil.equals("PROPONENTE") ? "Propuestas" : "Colaboraciones" %></h5>
                    <ul class="list-group list-group-flush">
                        <% if (propuestas != null && !propuestas.isEmpty()) {
                            for (DtoPropuesta p : propuestas) { %>
                        <li class="list-group-item">
                            <strong><%= p.getTitulo() %></strong>
                            <p class="text-muted mb-0"><%= p.getDescripcion() %></p>
                        </li>
                        <% } } else { %>
                        <p class="text-muted p-2">No tiene propuestas/colaboraciones.</p>
                        <% } %>
                    </ul>
                </div>
            </div>
            <% } else { %>
            <div class="d-flex align-items-center justify-content-center bg-light rounded-3 text-center" style="min-height:50vh;">
                <div>
                    <i class="bi bi-people-fill text-muted" style="font-size:4rem;"></i>
                    <h5 class="mt-3 text-muted">Selecciona un usuario</h5>
                    <p class="text-muted mb-0">Elige un perfil de la lista para ver sus detalles.</p>
                </div>
            </div>
            <% } %>
        </div>
    </div>
</div>

<%@ include file="compartidos/footer.jsp" %>
<script>
    document.addEventListener("DOMContentLoaded", function() {
        // Selecciona todos los botones de dejar de seguir que tengan la clase especial
        const botonesAjax = document.querySelectorAll('.btn-seguir-ajax, form[action="dejarSeguirUsuario"] button');

        botonesAjax.forEach(boton => {
            boton.addEventListener('click', function(event) {
                // Previene el comportamiento estándar del formulario
                event.preventDefault();

                // Encontramos el formulario correspondiente
                const form = this.closest('form');
                const formData = new FormData(form);

                const nickUsuario = formData.get('usuarioSeguido');
                const tipoUsr = formData.get('tipoUsr');
                const elementoId = `item-seguido-${nickUsuario}`;

                fetch('dejarSeguirUsuario', {
                    method: 'POST',
                    body: formData
                })
                    .then(response => {
                        if (response.ok) {
                            console.log("Dejó de seguir correctamente:", nickUsuario);

                            // Borrar el elemento visual si existe
                            const elemento = document.getElementById(elementoId);
                            if (elemento) {
                                elemento.remove();
                            }

                            // Actualizar el contador de seguidos
                            const contador = document.getElementById('contador-seguidos');
                            if (contador) {
                                let num = parseInt(contador.innerText);
                                if (!isNaN(num) && num > 0) {
                                    contador.innerText = num - 1;
                                }
                            }

                            // Cambiar el boton sin recargar
                            const contenedor = form.parentElement;
                            contenedor.innerHTML = `
                        <form action="seguirUsuario" method="post" style="display: inline;">
                            <input type="hidden" name="usuarioSeguido" value="${nickUsuario}">
                            <input type="hidden" name="tipoUsr" value="${tipoUsr}">
                            <button type="submit" class="btn btn-primary btn-sm">
                                <i class="bi bi-person-plus"></i> Seguir
                            </button>
                        </form>`;
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
