package com.culturarte.web;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import com.culturarte.web.ws.cliente.*;


@WebServlet("/altaPerfil")
@MultipartConfig
public class AltaPerfilServlet extends HttpServlet {

    private IProponenteController propController;
    private IColaboradorController colaController;

    @Override
    public void init() throws ServletException {
        ServletContext context = getServletContext();

        this.propController = (IProponenteController) context.getAttribute("ws.proponente");
        this.colaController = (IColaboradorController) context.getAttribute("ws.colaborador");

        if (this.propController == null || this.colaController == null) {
            throw new ServletException("¡Error crítico! Los clientes de AltaPerfilServlet no se pudieron cargar desde ClienteInit.");
        }
    }


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
            String fechaNac = req.getParameter("fechaNac");


            Part filePart = req.getPart("dirImagen");
            String dirImagen = null;
            if (filePart != null && filePart.getSize() > 0) {
                String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
                String uploadPath = getServletContext().getRealPath("/imagenes");
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) uploadDir.mkdirs();
                filePart.write(uploadPath + File.separator + fileName);
                dirImagen = "imagenes/" + fileName;
            } else {
                dirImagen = "imagenes/404.png";
            }


            String tipoUsuario = req.getParameter("tipoUsuario");

            if("PROPONENTE".equals(tipoUsuario)){
                String direccion = req.getParameter("direccion");
                String biografia = req.getParameter("biografia");
                String link = req.getParameter("link");


                DtoProponente dtoProp = new DtoProponente();
                dtoProp.setNickname(nick);
                dtoProp.setNombre(nombre);
                dtoProp.setApellido(apellido);
                dtoProp.setContrasenia(contrasenia);
                dtoProp.setCorreo(correo);
                dtoProp.setFechaNac(fechaNac);
                dtoProp.setDirImagen(dirImagen);
                dtoProp.setDireccion(direccion);
                dtoProp.setBiografia(biografia);
                dtoProp.setLink(link);


                propController.altaProponente(dtoProp);
                req.setAttribute("mensaje", "Perfil " + nick + " creado con exito");

            } else if("COLABORADOR".equals(tipoUsuario)){


                DtoColaborador dtoCola = new DtoColaborador();
                dtoCola.setNickname(nick);
                dtoCola.setNombre(nombre);
                dtoCola.setApellido(apellido);
                dtoCola.setContrasenia(contrasenia);
                dtoCola.setCorreo(correo);
                dtoCola.setFechaNac(fechaNac);
                dtoCola.setDirImagen(dirImagen);


                colaController.altaColaborador(dtoCola);
                req.setAttribute("mensaje", "Perfil " + nick + " creado con exito");
            }


            HttpSession sesion = req.getSession(true);
            sesion.setAttribute("tipoUsuario", tipoUsuario);
            sesion.setAttribute("nick", nick);
            sesion.setAttribute("password", contrasenia);
            sesion.setAttribute("sesion", new Sesion(nick, tipoUsuario, contrasenia));

        } catch(Exception e){
            req.setAttribute("error", e.getMessage());
            req.getRequestDispatcher("/altaPerfil.jsp").forward(req, resp);
            return;
        }

        req.getRequestDispatcher("/index.jsp").forward(req, resp);
    }
}
