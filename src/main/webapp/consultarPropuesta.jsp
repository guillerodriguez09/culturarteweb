<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*" %>
<%@ page import="com.culturarte.web.ws.cliente.DtoPropuesta" %>
<%@ page import="com.culturarte.web.ws.cliente.DtoUsuario" %>
<%@ page import="com.culturarte.web.ws.cliente.ETipoRetorno" %>

<%
    if (request.getParameter("mensaje") != null) {
        request.setAttribute("mensaje", request.getParameter("mensaje"));
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Consulta de Propuestas</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">
    <link href="css/general.css" rel="stylesheet">
</head>

<%@ include file="compartidos/header.jsp" %>

<%
    // esto es para que se quede en la pestana que seleccionamos

    DtoPropuesta sel = (DtoPropuesta) request.getAttribute("propuestaSeleccionada");

    String activeTabId = "publicadas"; //  por defecto esta en publicadas

    if (sel != null) {
        String estado = sel.getEstadoActual();
        if (estado == null) {
        } else if (estado.equalsIgnoreCase("EN_FINANCIACION")) {
            activeTabId = "financiacion";
        } else if (estado.equalsIgnoreCase("FINANCIADA")) {
            activeTabId = "financiadas";
        } else if (estado.equalsIgnoreCase("NO_FINANCIADA")) {
            activeTabId = "noFinanciadas";
        } else if (estado.equalsIgnoreCase("CANCELADA")) {
            activeTabId = "canceladas";
        }

    }

%>


<div class="container mt-4">
    <h2 class="text-center mb-4">Consulta de Propuestas</h2>

    <% if (request.getAttribute("mensaje") != null) { %>
    <div class="alert alert-success alert-dismissible fade show" role="alert">
        <%= request.getAttribute("mensaje") %>
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
    <% } else if (request.getAttribute("error") != null) { %>
    <div class="alert alert-danger alert-dismissible fade show" role="alert">
        <%= request.getAttribute("error") %>
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
    <% } %>


    <ul class="nav nav-tabs" id="estadoTabs" role="tablist">
        <li class="nav-item">
            <a class="nav-link <%= "publicadas".equals(activeTabId) ? "active" : "" %>" data-bs-toggle="tab" href="#publicadas">
                <i class="bi bi-eye-fill me-1"></i> Publicadas
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link <%= "financiacion".equals(activeTabId) ? "active" : "" %>" data-bs-toggle="tab" href="#financiacion">
                <i class="bi bi-cash-coin me-1"></i> En financiación
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link <%= "financiadas".equals(activeTabId) ? "active" : "" %>" data-bs-toggle="tab" href="#financiadas">
                <i class="bi bi-check-circle-fill me-1"></i> Financiadas
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link <%= "noFinanciadas".equals(activeTabId) ? "active" : "" %>" data-bs-toggle="tab" href="#noFinanciadas">
                <i class="bi bi-x-circle-fill me-1"></i> No financiadas
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link <%= "canceladas".equals(activeTabId) ? "active" : "" %>" data-bs-toggle="tab" href="#canceladas">
                <i class="bi bi-slash-circle-fill me-1"></i> Canceladas
            </a>
        </li>
    </ul>

    <div class="tab-content mt-3">
        <%
            Map<String, List<DtoPropuesta>> grupos = Map.of(
                    "publicadas", (List<DtoPropuesta>) request.getAttribute("publicadas"),
                    "financiacion", (List<DtoPropuesta>) request.getAttribute("enFinanciacion"),
                    "financiadas", (List<DtoPropuesta>) request.getAttribute("financiadas"),
                    "noFinanciadas", (List<DtoPropuesta>) request.getAttribute("noFinanciadas"),
                    "canceladas", (List<DtoPropuesta>) request.getAttribute("canceladas")
            );
            for (String key : grupos.keySet()) {
        %>
        <div class="tab-pane fade <%= key.equals(activeTabId) ? "show active" : "" %>" id="<%= key %>">
            <div class="list-group">
                <%
                    List<DtoPropuesta> lista = grupos.get(key);
                    if (lista != null && !lista.isEmpty()) {
                        for (DtoPropuesta p : lista) {
                %>
                <form action="consultarPropuesta" method="post"
                      class="list-group-item list-group-item-action d-flex justify-content-between align-items-center">
                    <div>
                        <strong><%= p.getTitulo() %></strong> — <%= p.getLugar() %>
                    </div>
                    <button type="submit" name="titulo" value="<%= p.getTitulo() %>"
                            class="btn btn-outline-primary btn-sm">Ver detalle</button>
                </form>
                <% } } else { %>
                <p class="text-muted p-3">No hay propuestas en este estado.</p>
                <% } %>
            </div>

        </div>
        <% } %>
    </div>

    <%
        if (sel != null) {
    %>
    <div class="card mt-5 shadow">
        <div class="card-body">
            <div class="row">

                <div class="col-md-4">

                    <% if (sel.getImagen() != null && !sel.getImagen().isEmpty()) { %>
                    <img src="<%= request.getContextPath() + "/" + sel.getImagen() %>"
                         class="img-fluid rounded shadow-sm mb-3" alt="Imagen de la propuesta">
                    <% } else { %>
                    <div class="d-flex align-items-center justify-content-center bg-light rounded shadow-sm mb-3"
                         style="width: 100%; height: 200px;">
                        <i class="bi bi-image-fill text-secondary" style="font-size: 3rem;"></i>
                    </div>
                    <% } %>

                    <div class="card bg-light mb-3">
                        <div class="card-body">
                            <h5 class="card-title">Financiación</h5>
                            <%
                                // calculo del porcentaje para mostrar en la barrita de progreso
                                double montoAReunir = sel.getMontoAReunir();
                                double montoRecaudado = sel.getMontoRecaudado();
                                int porcentaje = 0;
                                if (montoAReunir > 0) {
                                    porcentaje = (int) ((montoRecaudado / montoAReunir) * 100);
                                }
                            %>

                            <div class="progress mb-2" style="height: 25px;">
                                <% if (porcentaje > 0) { %>
                                <div class="progress-bar progress-bar-striped bg-success" role="progressbar"
                                     style="width: <%= porcentaje %>%;" aria-valuenow="<%= porcentaje %>"
                                     aria-valuemin="0" aria-valuemax="100">
                                    <strong><%= porcentaje %>%</strong>
                                </div>
                                <% } else { %>
                                <div class="progress-bar bg-light text-dark" role="progressbar"
                                     style="width: 100%;" aria-valuenow="0"
                                     aria-valuemin="0" aria-valuemax="100">
                                    <strong>0%</strong>
                                </div>
                                <% } %>
                            </div>

                            <p class="small text-center mb-0">
                                <strong>Recaudado:</strong> $<%= String.format("%.0f", montoRecaudado) %>
                            </p>
                            <p class="small text-center text-muted">
                                <strong>Meta:</strong> $<%= String.format("%.0f", montoAReunir) %>
                            </p>
                        </div>
                    </div>

                    <h5 class="mt-4">Colaboradores</h5>
                    <ul class="list-group list-group-flush" style="max-height: 200px; overflow-y: auto;">
                        <% if (sel.getColaboradores() != null && !sel.getColaboradores().isEmpty()) {
                            for (String c : sel.getColaboradores()) { %>
                        <li class="list-group-item"><i class="bi bi-person-circle me-2"></i> <%= c %></li>
                        <%  }
                        } else { %>
                        <li class="list-group-item text-muted">Aún no hay colaboradores.</li>
                        <% } %>
                    </ul>

                </div>


                <div class="col-md-8">
                    <h4 class="card-title"><%= sel.getTitulo() %></h4>

                    <span class="badge bg-primary fs-6 mb-3"><%= sel.getEstadoActual() %></span>

                    <p class="card-text"><strong>Proponente creador:</strong><br> <%= sel.getProponenteNick() %></p>
                    <p class="card-text"><strong>Descripción:</strong><br> <%= sel.getDescripcion() %></p>
                    <p><strong>Lugar:</strong> <%= sel.getLugar() %></p>

                    <div class="mt-4 pt-3 border-top">
                        <%
                            String tipoUsuario = (String) session.getAttribute("tipoUsuario");
                            String usuario = (String) session.getAttribute("nick");

                            if (usuario != null && sel != null && tipoUsuario != null) {
                                // Si es PROPONENTE y dueño de la propuesta
                                if (tipoUsuario.equals("PROPONENTE") && usuario.equals(sel.getProponenteNick())) {
                                    // Comprobamos si está financiada para mostrar el botón de cancelar, agregue un modal de confirmacion para que elija si o no
                                    if ("FINANCIADA".equalsIgnoreCase(sel.getEstadoActual())) {
                        %>
                        <button type="button" class="btn btn-danger" data-bs-toggle="modal" data-bs-target="#confirmarCancelacionModal">
                            <i class="bi bi-trash-fill me-1"></i> Cancelar Propuesta
                        </button>

                        <div class="modal fade" id="confirmarCancelacionModal" tabindex="-1" aria-labelledby="modalLabel" aria-hidden="true">
                            <div class="modal-dialog">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <h5 class="modal-title" id="modalLabel">Confirmar Cancelación</h5>
                                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                    </div>
                                    <div class="modal-body">
                                        ¿Estás seguro de que quieres cancelar la propuesta <strong>'<%= sel.getTitulo() %>'</strong>?
                                        <p class="text-danger mt-2">Esta acción no se puede deshacer.</p>
                                    </div>
                                    <div class="modal-footer">
                                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cerrar</button>

                                        <form action="cancelarPropuesta" method="post" style="display: inline;">
                                            <input type="hidden" name="titulo" value="<%= sel.getTitulo() %>">
                                            <button type="submit" class="btn btn-danger">Sí, cancelar propuesta</button>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <%
                            }
                        }
                        // Si es COLABORADOR y no colaboró aun en esa prop
                        else if (tipoUsuario.equals("COLABORADOR")
                                && !sel.getColaboradores().contains(usuario)
                                && ("PUBLICADA".equalsIgnoreCase(sel.getEstadoActual())
                                || "EN_FINANCIACION".equalsIgnoreCase(sel.getEstadoActual()))) {
                        %>
                        <button type="button" id="btnMostrarColaboracion" class="btn btn-success btn-lg">
                            <i class="bi bi-heart-fill me-1"></i> ¡Quiero Colaborar!
                        </button>

                        <div id="formColaboracion" style="display:none; margin-top:20px;">
                            <form action="registrarColaboracion" method="post">
                                <input type="hidden" name="propuestaTitulo" value="<%= sel.getTitulo() %>">

                                <div class="mb-3">
                                    <label for="monto" class="form-label">Monto a colaborar</label>
                                    <input type="number" class="form-control" id="monto" name="monto" required min="1">
                                </div>

                                <div class="mb-3">
                                    <label for="tipoRetorno" class="form-label">Tipo de retorno</label>
                                    <select class="form-select" id="tipoRetorno" name="tipoRetorno" required>
                                        <%
                                            List<ETipoRetorno> tipos = (List<ETipoRetorno>) request.getAttribute("tiposRetorno");

                                            if (tipos != null) {
                                                for (ETipoRetorno t : tipos) {
                                        %>
                                        <option value="<%= t.name() %>"><%= t.name() %></option>
                                        <%
                                                }
                                            }
                                        %>
                                    </select>
                                </div>


                                <div class="d-flex gap-2">
                                    <button type="submit" class="btn btn-primary">Confirmar colaboración</button>
                                    <button type="button" id="btnCancelarColab" class="btn btn-secondary">Cancelar</button>
                                </div>
                            </form>
                        </div>

                        <%
                            }
                        }
                        else {
                        %>
                        <p class="text-muted">Iniciá sesión para colaborar o cancelar una propuesta.</p>
                        <%
                            }
                        %>
                    </div>
                </div>

            </div>
        </div>


    </div>
    <% } %>
</div>

<%@ include file="compartidos/footer.jsp" %>
<script>
    document.addEventListener("DOMContentLoaded", () => {
        const btnMostrar = document.getElementById("btnMostrarColaboracion");
        const formDiv = document.getElementById("formColaboracion");
        const btnCancelar = document.getElementById("btnCancelarColab");

        if (btnMostrar) {
            btnMostrar.addEventListener("click", () => {
                formDiv.style.display = "block";
                btnMostrar.style.display = "none";
            });
        }

        if (btnCancelar) {
            btnCancelar.addEventListener("click", () => {
                formDiv.style.display = "none";
                btnMostrar.style.display = "inline-block";
            });
        }
    });
</script>
</html>