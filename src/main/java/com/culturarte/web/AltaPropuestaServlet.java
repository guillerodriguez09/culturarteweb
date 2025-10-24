package com.culturarte.web;

import com.culturarte.logica.controllers.ICategoriaController;
import com.culturarte.logica.controllers.IPropuestaController;
import com.culturarte.logica.controllers.IProponenteController;
import com.culturarte.logica.dtos.DTOPropuesta;
import com.culturarte.logica.enums.ETipoRetorno;
import com.culturarte.logica.fabrica.Fabrica;

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

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");

        HttpSession session = req.getSession(false);
        String tipoUsuario = (session != null) ? (String) session.getAttribute("tipoUsuario") : null;

        if (tipoUsuario == null || !tipoUsuario.equalsIgnoreCase("Proponente")) {
            //redirige al login en vez de 403
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
            // redirige al login en vez de 403
            resp.sendRedirect(req.getContextPath() + "/inicioSesion.jsp?loginRequired=1");
            return;
        }

        try {
            //Recibir datos del form
            String titulo = req.getParameter("titulo");
            String descripcion = req.getParameter("descripcion");
            String lugar = req.getParameter("lugar");
            String fechaStr = req.getParameter("fecha");

            String categoria = req.getParameter("categoriaNombre");
            if (categoria == null || categoria.isBlank()) {
                categoria = "Categoría"; // raíz por defecto
            }

            int precioEntrada = Integer.parseInt(req.getParameter("precioEntrada"));
            int montoAReunir = Integer.parseInt(req.getParameter("montoAReunir"));


            String[] retornosSeleccionados = req.getParameterValues("retornos");
            List<ETipoRetorno> retornos = new ArrayList<>();
            if (retornosSeleccionados != null) {
                for (String r : retornosSeleccionados) {
                    retornos.add(ETipoRetorno.valueOf(r));
                }
            }

            // Manejo de la imagen subida
            Part filePart = req.getPart("imagen");
            String fileName = null;
            String relativePath = null;

            if (filePart != null && filePart.getSize() > 0) {
                // Nombre limpio del archivo subido
                fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();

                // Ruta absoluta a la carpeta imagenes dentro del WAR desplegado
                String uploadPath = getServletContext().getRealPath("/imagenes");

                // Crear la carpeta si no existe
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }

                // Guardar el archivo
                filePart.write(uploadPath + File.separator + fileName);

                // Guardar ruta relativa para la BD
                relativePath = "imagenes/" + fileName;
            } else {
                // Si no se sube imagen, usar una genérica
                relativePath = "imagenes/404.png";
            }


            // Armar DTO
            DTOPropuesta dto = new DTOPropuesta();
            dto.setTitulo(titulo);
            dto.setDescripcion(descripcion);
            dto.setLugar(lugar);
            dto.setFecha(LocalDate.parse(fechaStr));
            dto.setCategoriaNombre(categoria);
            dto.setProponenteNick(proponenteNick);
            dto.setPrecioEntrada(precioEntrada);
            dto.setMontoAReunir(montoAReunir);
            dto.setRetornos(retornos);
            dto.setImagen(relativePath);


            IPropuestaController ctrl = Fabrica.getInstancia().getPropuestaController();
            ctrl.altaPropuesta(dto);
            session.setAttribute("mensaje", "Propuesta creada con éxito: " + titulo);

            resp.sendRedirect(req.getContextPath() + "/index.jsp");
        }
        catch (Exception e) {
            req.setAttribute("error", "Error al crear la propuesta: " + e.getMessage());
            cargarCategorias(req);
            req.getRequestDispatcher("/altaPropuesta.jsp").forward(req, resp);
        }
    }

    private void cargarCategorias(HttpServletRequest req) {
        ICategoriaController catCtrl = Fabrica.getInstancia().getCategoriaController();
        List<String> categorias = catCtrl.listarCategorias()
                .stream()
                .filter(c -> !c.equalsIgnoreCase("Categoría"))
                .toList();

        req.setAttribute("categorias", categorias);
    }
}

