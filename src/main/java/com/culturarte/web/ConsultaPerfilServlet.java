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
            req.setAttribute("proponentes", propController.listarTodos());
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
                List<String> seguidosPorMi = seguiController.listarSeguidosDeNick(nickSession);
                if (seguidosPorMi != null) loSigo = seguidosPorMi.contains(nick);
            } catch (Exception e) {
                System.err.println("Error al verificar seguimiento: " + e.getMessage());
            }
        }
        req.setAttribute("loSigo", loSigo);


        List<String> seguidooresNick;
        List<String> seguidoosDeNick;
        List<DtoPropuesta> propuestas = new ArrayList<>();

        try {
            if ("proponente".equalsIgnoreCase(tipoUsr)) {

                DtoProponente dtoProponente = propController.obtenerProponente(nick);
                List<AnyTypeArray> propConPropuWrappers = propController.obtenerTodPropConPropu(nick);

                if (propConPropuWrappers != null) {
                    for (AnyTypeArray filaWrapper : propConPropuWrappers) {
                        List<Object> fila = filaWrapper.getItem();
                        if (fila != null && fila.size() >= 2 && fila.get(1) instanceof DtoPropuesta) {
                            propuestas.add((DtoPropuesta) fila.get(1));
                        }
                    }
                }

                req.setAttribute("usuarioSeleccionado", dtoProponente);
                req.setAttribute("tipoPerfil", "PROPONENTE");
                req.setAttribute("propuestas", propuestas);

            } else if ("colaborador".equalsIgnoreCase(tipoUsr)) {

                DtoColaborador dtoColaborador = colaController.obtenerColaborador(nick);
                List<AnyTypeArray> colConPropuWrappers = colaController.obtenerTodColConPropu(nick);

                if (colConPropuWrappers != null) {
                    for (AnyTypeArray filaWrapper : colConPropuWrappers) {
                        List<Object> fila = filaWrapper.getItem();
                        if (fila != null && fila.size() >= 2 && fila.get(1) instanceof DtoPropuesta) {
                            propuestas.add((DtoPropuesta) fila.get(1));
                        }
                    }
                }

                req.setAttribute("usuarioSeleccionado", dtoColaborador);
                req.setAttribute("tipoPerfil", "COLABORADOR");
                req.setAttribute("propuestas", propuestas);

                if (esPropioPerfil) {
                    List<DtoColabConsulta> detalles = colabController.consultarColaboracionesPorColaborador(nick);
                    req.setAttribute("colaboracionesDetalladas", detalles);
                }
            }

            // Seguidores
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

