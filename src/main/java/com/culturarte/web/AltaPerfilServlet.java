package com.culturarte.web;

import com.culturarte.logica.controllers.IProponenteController;
import com.culturarte.logica.controllers.IColaboradorController;
import com.culturarte.logica.dtos.DTOProponente;
import com.culturarte.logica.dtos.DTOColaborador;
import com.culturarte.logica.fabrica.Fabrica;
import com.culturarte.web.fabrica.FabricaWeb;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;

@WebServlet("/altaPerfil")
@MultipartConfig
public class AltaPerfilServlet extends HttpServlet {

    final IProponenteController propController = FabricaWeb.getInstancia().getProponenteController();
    final IColaboradorController colaController = FabricaWeb.getInstancia().getColaboradorController();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");

        resp.sendRedirect(req.getContextPath() + "/altaPerfil.jsp");

    }

    @Override
    protected void  doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");

        try{

            String nick = req.getParameter("nick");
            String nombre = req.getParameter("nombre");
            String apellido = req.getParameter("apellido");
            String contrasenia = req.getParameter("contrasenia");
            String correo = req.getParameter("correo");
            String fechaNacStr = req.getParameter("fechaNac");

            Part filePart = req.getPart("dirImagen");
            String fileName = null;
            String dirImagen = null;

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
                dirImagen = "imagenes/" + fileName;
            } else {
                // Si no se sube imagen, usar una genérica (si querés tener un placeholder)
                dirImagen = "imagenes/404.png";
            }

            String tipoUsuario = req.getParameter("tipoUsuario");

            String direccion = null;
            String biografia = null;
            String link = null;

            if("PROPONENTE".equals(tipoUsuario)){

                direccion = req.getParameter("direccion");
                biografia = req.getParameter("biografia");
                link = req.getParameter("link");

                DTOProponente dtoProp = new DTOProponente();
                dtoProp.setNick(nick);
                dtoProp.setNombre(nombre);
                dtoProp.setApellido(apellido);
                dtoProp.setContrasenia(contrasenia);
                dtoProp.setCorreo(correo);
                dtoProp.setFechaNac(LocalDate.parse(fechaNacStr));
                dtoProp.setDirImagen(dirImagen);
                dtoProp.setDireccion(direccion);
                dtoProp.setBiografia(biografia);
                dtoProp.setLink(link);

                propController.altaProponente(dtoProp);

                req.setAttribute("mensaje", "Perfil " + nick + " creado con exito");

            }else if("COLABORADOR".equals(tipoUsuario)){

                DTOColaborador dtoCola = new DTOColaborador();
                dtoCola.setNick(nick);
                dtoCola.setNombre(nombre);
                dtoCola.setApellido(apellido);
                dtoCola.setContrasenia(contrasenia);
                dtoCola.setCorreo(correo);
                dtoCola.setFechaNac(LocalDate.parse(fechaNacStr));
                dtoCola.setDirImagen(dirImagen);

                colaController.altaColaborador(dtoCola);

                req.setAttribute("mensaje", "Perfil " + nick + " creado con exito");

            }

            HttpSession sesion = req.getSession(true);

            sesion.setAttribute("tipoUsuario", tipoUsuario);  // "PROPONENTE" | "COLABORADOR"
            sesion.setAttribute("nick", nick);            // nick real
            sesion.setAttribute("password", contrasenia);

            sesion.setAttribute("sesion", new Sesion(nick, tipoUsuario, contrasenia));

        }catch(Exception e){
            req.setAttribute("error", e.getMessage());
        }

        req.getRequestDispatcher("/index.jsp").forward(req, resp);

    }

}
