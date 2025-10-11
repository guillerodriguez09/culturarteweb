<%--
  Created by IntelliJ IDEA.
  User: guillerodriguezz
  Date: 3/10/25
  Time: 20:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<html>
<head>
    <title>Alta de Propuesta</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="css/general.css" rel="stylesheet">
    <link href="css/altaPropuesta.css" rel="stylesheet">
</head>


<jsp:include page="compartidos/header.jsp"/>
<div class="container mt-5">
    <div class="card shadow-lg">
        <div class="card-header bg-primary text-white">
            <h3 class="mb-0">Alta de Propuesta</h3>
        </div>
        <div class="card-body">
            <form action="altaPropuesta" method="post" enctype="multipart/form-data">
                <!-- Categoría -->
                <div class="mb-3">
                    <select class="form-select" id="categoriaNombre" name="categoriaNombre">
                        <option value="">Seleccione una categoría...</option>
                        <%
                            List<String> cats = (List<String>) request.getAttribute("categorias");
                            if (cats != null) {
                                for (String c : cats) {
                                    if (!"Categoría".equalsIgnoreCase(c)) { // filtramos la raíz
                        %>
                        <option value="<%= c %>"><%= c %></option>
                        <%
                                    }
                                }
                            }
                        %>
                    </select>
                </div>

                <!-- Datos básicos -->
                <div class="mb-3">
                    <label for="titulo" class="form-label">Título</label>
                    <input type="text" class="form-control" id="titulo" name="titulo" required>
                </div>

                <div class="mb-3">
                    <label for="descripcion" class="form-label">Descripción</label>
                    <textarea class="form-control" id="descripcion" name="descripcion" rows="3"></textarea>
                </div>

                <div class="mb-3">
                    <label for="lugar" class="form-label">Lugar</label>
                    <input type="text" class="form-control" id="lugar" name="lugar" required>
                </div>

                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label for="fecha" class="form-label">Fecha</label>
                        <input type="date" class="form-control" id="fecha" name="fecha" required>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label for="precioEntrada" class="form-label">Precio Entrada</label>
                        <input type="number" class="form-control" id="precioEntrada" name="precioEntrada" min="1" required>
                    </div>
                </div>

                <div class="mb-3">
                    <label for="montoAReunir" class="form-label">Monto a Reunir</label>
                    <input type="number" class="form-control" id="montoAReunir" name="montoAReunir" min="1" required>
                </div>

                <!-- Retornos -->
                <div class="mb-3">
                    <label class="form-label">Retornos</label><br>
                    <div class="form-check">
                        <input class="form-check-input" type="checkbox" name="retornos" value="ENTRADAS_GRATIS" id="retorno1">
                        <label class="form-check-label" for="retorno1">Entradas Gratis</label>
                    </div>
                    <div class="form-check">
                        <input class="form-check-input" type="checkbox" name="retornos" value="PORCENTAJE_GANANCIAS" id="retorno2">
                        <label class="form-check-label" for="retorno2">Porcentaje de Ganancias</label>
                    </div>
                </div>

                <div class="mb-3">
                    <label for="imagen" class="form-label">Imagen (opcional)</label>
                    <input type="file" class="form-control" id="imagen" name="imagen" accept="image/*">
                </div>

                <button type="submit" class="btn btn-success w-100">Crear Propuesta</button>
            </form>
        </div>
    </div>

    <% if (request.getAttribute("mensaje") != null) { %>
    <div class="alert alert-info mt-3">
        <%= request.getAttribute("mensaje") %>
    </div>
    <% } %>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="js/validacionesGral.js"></script>
<script src="js/altaPropuesta.js"></script>
<script>
    document.querySelector("form").onsubmit = validarAltaPropuesta;
</script>
<%@ include file="compartidos/footer.jsp" %>
</html>
