package com.culturarte.web;

import com.culturarte.web.ws.cliente.IColaboradorController;
import com.culturarte.web.ws.cliente.IProponenteController;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/verificarUsuario")
public class VerificarUsuServlet extends HttpServlet {

    private IProponenteController propController;
    private IColaboradorController colaController;

    @Override
    public void init() throws ServletException {
        ServletContext context = getServletContext();
        this.propController = (IProponenteController) context.getAttribute("ws.proponente");
        this.colaController = (IColaboradorController) context.getAttribute("ws.colaborador");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");

        String tipo = req.getParameter("tipo");
        String valor = req.getParameter("valor");

        if (valor == null || valor.trim().isEmpty()) {
            resp.getWriter().write("false");
            return;
        }

        boolean existe = false;

        try {
            if ("nick".equals(tipo)) {

                boolean enProponente = propController.obtenerProponente(valor) != null;
                boolean enColaborador = false;


                if (!enProponente) {
                    enColaborador = colaController.obtenerColaborador(valor) != null;
                }
                existe = enProponente || enColaborador;

            } else if ("email".equals(tipo)) {
                boolean enProponente = propController.obtenerProponenteCorreo(valor) != null;
                boolean enColaborador = false;

                if (!enProponente) {
                    enColaborador = colaController.obtenerColaboradorCorreo(valor) != null;
                }
                existe = enProponente || enColaborador;
            }
        } catch (Exception e) {
            System.err.println("Error verificando usuario: " + e.getMessage());
            existe = false;
        }

        resp.getWriter().write(String.valueOf(existe));
    }
}