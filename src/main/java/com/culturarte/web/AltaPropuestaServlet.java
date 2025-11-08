package com.culturarte.web;

import com.culturarte.web.ws.cliente.*;
import java.util.stream.Collectors; // Para el filtro de categorías

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/altaPropuesta")
@MultipartConfig
public class AltaPropuestaServlet extends HttpServlet {


    private IPropuestaController propCtrl;
    private ICategoriaController catCtrl;


    @Override
    public void init() throws ServletException {
        ServletContext context = getServletContext();
        this.propCtrl = (IPropuestaController) context.getAttribute("ws.propuesta");
        this.catCtrl = (ICategoriaController) context.getAttribute("ws.categoria");

        if (this.propCtrl == null || this.catCtrl == null) {
            throw new ServletException("¡Error crítico! Los clientes de AltaPropuestaServlet no se pudieron cargar desde ClienteInit.");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");

        HttpSession session = req.getSession(false);
        String tipoUsuario = (session != null) ? (String) session.getAttribute("tipoUsuario") : null;

        if (tipoUsuario == null || !tipoUsuario.equalsIgnoreCase("Proponente")) {
            resp.sendRedirect(req.getContextPath() + "/inicioSesion.jsp?loginRequired=1");
            return;
        }

        cargarCategorias(req);
        req.getRequestDispatcher("/altaPropuesta.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");

        HttpSession session = req.getSession(false);
        String tipoUsuario   = (session != null) ? (String) session.getAttribute("tipoUsuario") : null;
        String proponenteNick= (session != null) ? (String) session.getAttribute("nick") : null;

        if (tipoUsuario == null || !tipoUsuario.equalsIgnoreCase("Proponente") || proponenteNick == null) {
            resp.sendRedirect(req.getContextPath() + "/inicioSesion.jsp?loginRequired=1");
            return;
        }

        try {
            String titulo = req.getParameter("titulo");
            String descripcion = req.getParameter("descripcion");
            String lugar = req.getParameter("lugar");
            String fecha = req.getParameter("fecha");
            String categoria = req.getParameter("categoriaNombre");
            if (categoria == null || categoria.isBlank()) {
                categoria = "Categoría";
            }
            int precioEntrada = Integer.parseInt(req.getParameter("precioEntrada"));
            int montoAReunir = Integer.parseInt(req.getParameter("montoAReunir"));



            String[] retornosSeleccionados = req.getParameterValues("retornos");
            List<ETipoRetorno> retornos = new ArrayList<>();
            if (retornosSeleccionados != null) {
                for (String r : retornosSeleccionados) {
                    // ETipoRetorno ahora viene de 'com.culturarte.web.ws.cliente'
                    retornos.add(ETipoRetorno.fromValue(r)); // JAX-WS usa fromValue()
                }
            }


            Part filePart = req.getPart("imagen");
            String relativePath = null;
            if (filePart != null && filePart.getSize() > 0) {
                String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
                String uploadPath = getServletContext().getRealPath("/imagenes");
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) uploadDir.mkdirs();
                filePart.write(uploadPath + File.separator + fileName);
                relativePath = "imagenes/" + fileName;
            } else {
                relativePath = "imagenes/404.png";
            }


            DtoPropuesta dto = new DtoPropuesta();
            dto.setTitulo(titulo);
            dto.setDescripcion(descripcion);
            dto.setLugar(lugar);
            dto.setFecha(fecha);
            dto.setCategoriaNombre(categoria);
            dto.setProponenteNick(proponenteNick);
            dto.setPrecioEntrada(precioEntrada);
            dto.setMontoAReunir(montoAReunir);


            dto.getRetornos().addAll(retornos);

            dto.setImagen(relativePath);


            this.propCtrl.altaPropuesta(dto);

            session.setAttribute("mensaje", "Propuesta creada con éxito: " + titulo);
            resp.sendRedirect(req.getContextPath() + "/index.jsp");

        } catch (Exception e) {
            req.setAttribute("error", "Error al crear la propuesta: " + e.getMessage());
            cargarCategorias(req);
            req.getRequestDispatcher("/altaPropuesta.jsp").forward(req, resp);
        }
    }


    private void cargarCategorias(HttpServletRequest req) {
        try {
            List<String> categorias = this.catCtrl.listarCategorias()
                    .stream()
                    .filter(c -> !c.equalsIgnoreCase("Categoría"))
                    .collect(Collectors.toList()); // Usamos collect para compatibilidad

            req.setAttribute("categorias", categorias);
        } catch (Exception e) {
            System.err.println("Error al cargar categorías desde el WS: " + e.getMessage());
            req.setAttribute("error", "No se pudieron cargar las categorías.");
            req.setAttribute("categorias", new ArrayList<String>());
        }
    }
}