package com.culturarte.web;

import com.culturarte.logica.fabrica.Fabrica;
import com.culturarte.logica.controllers.IProponenteController;
import com.culturarte.logica.controllers.IColaboradorController;
import com.culturarte.logica.dtos.DTOProponente;
import com.culturarte.logica.dtos.DTOColaborador;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/inicioSesion")
@MultipartConfig
public class InicioSesionServlet extends HttpServlet {

    final IColaboradorController colaController = Fabrica.getInstancia().getColaboradorController();
    final IProponenteController propController = Fabrica.getInstancia().getProponenteController();

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8"); //por un tema de compatibilidad con tildes

        HttpSession sesionActual = req.getSession(true);

        try {

            String nickOMail = req.getParameter("nickOMail");
            String contrasenia = req.getParameter("contrasenia");
            String tipoUsuario = "ODISEO";
            String contr = "Penelope";

            DTOProponente prop = new DTOProponente();
            DTOProponente propMail = new DTOProponente();

            DTOColaborador cola = new DTOColaborador();
            DTOColaborador colaMail = new DTOColaborador();

            if(nickOMail.isEmpty() || contrasenia.isEmpty()){
                req.setAttribute("error", "Por favor ingrese nickname/correo y contraseña");
            }

            if(!nickOMail.contains("@")){

                if(propController.obtenerProponente(nickOMail) != null ) {

                    prop = propController.obtenerProponente(nickOMail);
                    contr = prop.getContrasenia();
                    tipoUsuario = "PROPONENTE";

                }else if(colaController.obtenerColaborador(nickOMail) != null){

                    cola = colaController.obtenerColaborador(nickOMail);
                    contr = cola.getContrasenia();
                    tipoUsuario = "COLABORADOR";

                }
            }else{

                if(propController.obtenerProponenteCorreo(nickOMail) != null){

                    propMail = propController.obtenerProponenteCorreo(nickOMail);
                    contr = propMail.getContrasenia();
                    tipoUsuario = "PROPONENTE";

                }else if(colaController.obtenerColaboradorCorreo(nickOMail) != null){

                    colaMail = colaController.obtenerColaboradorCorreo(nickOMail);
                    contr = colaMail.getContrasenia();
                    tipoUsuario = "COLABORADOR";

                }
            }

            if(contr.equals(contrasenia)){

                Sesion sesion = new Sesion(nickOMail, contrasenia, tipoUsuario);
                sesionActual.setAttribute("sesion", sesion);
                req.setAttribute("mensaje", "Sesion iniciada correctamente");

            }else{
                req.setAttribute("error", "Usuario y/o contraseña incorrectos");
            }

        }catch(Exception e){
            req.setAttribute("error", " " + e.getMessage());
        }
        req.getRequestDispatcher("/inicioSesion.jsp").forward(req, resp);

    }

}
