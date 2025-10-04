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
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/altaPropuesta")
@MultipartConfig
public class AltaPropuestaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8"); //por un tema de compatibilidad con tildes
        /*
        String tipoUsuario = (String) req.getSession().getAttribute("tipoUsuario");
        if (tipoUsuario == null || !tipoUsuario.equals("PROPONENTE")) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Solo los proponentes pueden acceder a esta página.");
            return;
        }*/ //esto es para cuando este el inicio de sesion, para que solo lo hagan los proponentes
        cargarListas(req);
        req.getRequestDispatcher("/altaPropuesta.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");
/*
        String tipoUsuario = (String) req.getSession().getAttribute("tipoUsuario");
        if (tipoUsuario == null || !tipoUsuario.equals("PROPONENTE")) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "No tiene permisos para crear propuestas.");
            return;
        }
        */
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

            String proponente = req.getParameter("proponenteNick");
            int precioEntrada = Integer.parseInt(req.getParameter("precioEntrada"));
            int montoAReunir = Integer.parseInt(req.getParameter("montoAReunir"));


            String[] retornosSeleccionados = req.getParameterValues("retornos");
            List<ETipoRetorno> retornos = new ArrayList<>();
            if (retornosSeleccionados != null) {
                for (String r : retornosSeleccionados) {
                    retornos.add(ETipoRetorno.valueOf(r));
                }
            }

            // Obtener el archivo subido
            Part filePart = req.getPart("imagen");
            String fileName = null;
            String relativePath = null;

            if (filePart != null && filePart.getSize() > 0) {
                fileName = java.nio.file.Paths.get(filePart.getSubmittedFileName()).getFileName().toString();

                // Ruta absoluta a la carpeta 'imagenes'
                String uploadPath = getServletContext().getRealPath("") + "imagenes";
                java.io.File uploadDir = new java.io.File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdir();
                }

                // Guardar archivo en la carpeta
                filePart.write(uploadPath + java.io.File.separator + fileName);

                // Guardar ruta relativa para BD
                relativePath = "imagenes/" + fileName;
            } else {
                relativePath = ""; // sin imagen
            }


            // Armar DTO
            DTOPropuesta dto = new DTOPropuesta();
            dto.setTitulo(titulo);
            dto.setDescripcion(descripcion);
            dto.setLugar(lugar);
            dto.setFecha(LocalDate.parse(fechaStr));
            dto.setCategoriaNombre(categoria);
            dto.setProponenteNick(proponente);
            dto.setPrecioEntrada(precioEntrada);
            dto.setMontoAReunir(montoAReunir);
            dto.setRetornos(retornos);
            dto.setImagen(relativePath);

            //llama al controlador
            IPropuestaController ctrl = Fabrica.getInstancia().getPropuestaController();
            ctrl.altaPropuesta(dto);

            req.setAttribute("mensaje", "Propuesta creada con éxito: " + titulo);

        } catch (Exception e) {
            req.setAttribute("error", " " + e.getMessage());
        }

        //cargar combos y reenviar
        cargarListas(req);
        req.getRequestDispatcher("/altaPropuesta.jsp").forward(req, resp);
    }

    private void cargarListas(HttpServletRequest req) {

        //luego solo sera listar las categorias, no proponentes
        ICategoriaController catCtrl = Fabrica.getInstancia().getCategoriaController();
        IProponenteController propCtrl = Fabrica.getInstancia().getProponenteController();

        List<String> categorias = catCtrl.listarCategorias()
                .stream()
                .filter(c -> !c.equalsIgnoreCase("Categoría")) // 👈 no mostramos la raíz
                .toList();

        List<String> proponentes = propCtrl.listarProponentes(); //esto luego se va

        req.setAttribute("categorias", categorias);
        req.setAttribute("proponentes", proponentes);
    }

}
