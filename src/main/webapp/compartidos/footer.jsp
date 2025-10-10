<%--
  Created by IntelliJ IDEA.
  User: guillerodriguezz
  Date: 4/10/25
  Time: 19:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<footer class="footer mt-auto py-4 bg-primary text-white shadow-lg">
    <div class="container text-center">
        <div class="mb-2">
            <a href="index.jsp" class="text-white text-decoration-none fw-semibold mx-2">Inicio</a> |
            <a href="consultarPropuesta" class="text-white text-decoration-none fw-semibold mx-2">Ver Propuestas</a> |
            <a href="altaPropuesta" class="text-white text-decoration-none fw-semibold mx-2">Alta de Propuesta</a>
        </div>

        <p class="small mb-0">
            &copy; <%= java.time.Year.now() %> <strong>Culturarte</strong> — Programación de Aplicaciones - UTEC 💫
        </p>
    </div>
</footer>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>


