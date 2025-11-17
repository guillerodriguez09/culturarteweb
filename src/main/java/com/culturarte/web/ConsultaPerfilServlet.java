package com.culturarte.web;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import com.culturarte.web.ws.cliente.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/consultaPerfil")
@MultipartConfig
public class ConsultaPerfilServlet extends HttpServlet {

    private IProponenteController propController;
    private IColaboradorController colaController;
    private ISeguimientoController seguiController;
    private IColaboracionController colabController;

    @Override
    public void init() throws ServletException {
        ServletContext context = getServletContext();
        this.propController = (IProponenteController) context.getAttribute("ws.proponente");
        this.colaController = (IColaboradorController) context.getAttribute("ws.colaborador");
        this.seguiController = (ISeguimientoController) context.getAttribute("ws.seguimiento");
        this.colabController = (IColaboracionController) context.getAttribute("ws.colaboracion");

        if (propController == null || colaController == null || seguiController == null || colabController == null) {
            throw new ServletException("Error crítico: los clientes WS no se pudieron cargar desde ClienteInit.");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");

        try {
            req.setAttribute("proponentes", propController.listarTodosProponente());
            req.setAttribute("colaboradores", colaController.listarTodos());
        } catch (Exception e) {
            req.setAttribute("error", "Error al listar usuarios: " + e.getMessage());
        }

        String tipoUsr = req.getParameter("tipoUsr");
        String nick = req.getParameter("nick");

        if (nick == null || tipoUsr == null || nick.isEmpty() || tipoUsr.isEmpty()) {
            req.getRequestDispatcher("/consultaPerfil.jsp").forward(req, resp);
            return;
        }

        HttpSession session = req.getSession(false);
        String nickSession = (session != null) ? (String) session.getAttribute("nick") : null;
        boolean esPropioPerfil = (nickSession != null && nickSession.equals(nick));
        req.setAttribute("esPropioPerfil", esPropioPerfil);

        boolean loSigo = false;
        if (nickSession != null && !esPropioPerfil) {
            try {
                int idRelacion = seguiController.conseguirId(nickSession, nick); // nickSession sigue a nick?

                if (idRelacion > 0) {
                    loSigo = true;
                }
            } catch (Exception e) {
                System.err.println("Info: No se encontró seguimiento (o error de conexión): " + e.getMessage());
                loSigo = false;
            }
        }
        req.setAttribute("loSigo", loSigo);

        List<String> seguidooresNick;
        List<String> seguidoosDeNick;

        try {
            if ("proponente".equalsIgnoreCase(tipoUsr)) {

                DtoProponente dtoProponente = propController.obtenerProponente(nick);
                List<DtoPropoPropu> propConPropuList = propController.obtenerTodPropConPropu(nick);

                List<DtoPropuesta> propuestas = new ArrayList<>();
                if (propConPropuList != null && !propConPropuList.isEmpty()) {
                    for (DtoPropoPropu pack : propConPropuList) {
                        if (pack.getPropuestas() != null) {
                            propuestas.addAll(pack.getPropuestas());
                        }
                    }
                }

                req.setAttribute("proponenteSeleccionado", dtoProponente);
                req.setAttribute("propuestasDeProponente", propuestas);
                req.setAttribute("colaboradorSeleccionado", null);

            } else if ("colaborador".equalsIgnoreCase(tipoUsr)) {

            DtoColaborador dtoColaborador = colaController.obtenerColaborador(nick);
            List<DtoColPropu> colConPropuList = colaController.obtenerTodColConPropu(nick);

            List<DtoPropuesta> propuestas = new ArrayList<>();
            if (colConPropuList != null && !colConPropuList.isEmpty()) {
                for (DtoColPropu pack : colConPropuList) {
                    if (pack.getPropuestas() != null) {
                        propuestas.addAll(pack.getPropuestas());
                    }
                }
            }

            req.setAttribute("colaboradorSeleccionado", dtoColaborador);
            req.setAttribute("colaboracionesDeColaborador", propuestas);
            req.setAttribute("proponenteSeleccionado", null);

            if (esPropioPerfil) {
                List<DtoColabConsulta> detalles = colabController.consultarColaboracionesPorColaborador(nick);
                req.setAttribute("colaboracionesDetalladas", detalles);
            }
        }

        seguidooresNick = seguiController.listarSeguidoresDeNick(nick);
            seguidoosDeNick = seguiController.listarSeguidosDeNick(nick);
            req.setAttribute("seguidooresNick", seguidooresNick);
            req.setAttribute("seguidoosDeNick", seguidoosDeNick);

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Error al cargar perfil: " + e.getMessage());
        }

        req.getRequestDispatcher("/consultaPerfil.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req, resp);
    }
}
