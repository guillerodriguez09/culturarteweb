package com.culturarte.web;


import com.culturarte.logica.controllers.IColaboradorController;
import com.culturarte.logica.controllers.IProponenteController;
import com.culturarte.logica.dtos.DTOColaborador;
import com.culturarte.logica.dtos.DTOProponente;
import com.culturarte.logica.fabrica.Fabrica;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/consultaPerfil")
@MultipartConfig
public class ConsultaPerfilServlet extends HttpServlet {

    private final IProponenteController propController = Fabrica.getInstancia().getProponenteController();
    private final IColaboradorController colaController = Fabrica.getInstancia().getColaboradorController();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");

        req.setAttribute("proponentes", propController.listarTodos());
        req.setAttribute("colaboradores", colaController.listarTodos());

        req.getRequestDispatcher("/consultaPerfil.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");

        String tipoUsr = req.getParameter("tipoUsr");
        String nick = req.getParameter("nick");

        try{

            if("proponente".equals(tipoUsr)){

                DTOProponente dtoProponente = propController.obtenerProponente(nick);
                req.setAttribute("proponenteSeleccionado", dtoProponente);

            }else if("colaborador".equals(tipoUsr)){

                DTOColaborador dtoColaborador = colaController.obtenerColaborador(nick);
                req.setAttribute("colaboradorSeleccionado", dtoColaborador);

            }

        }catch(Exception e){
            req.setAttribute("error", e.getMessage());
        }

        doGet(req, resp);

    }

}
