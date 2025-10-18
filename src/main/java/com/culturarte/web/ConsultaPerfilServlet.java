package com.culturarte.web;


import com.culturarte.logica.controllers.IColaboradorController;
import com.culturarte.logica.controllers.IProponenteController;
import com.culturarte.logica.controllers.ISeguimientoController;

import com.culturarte.logica.controllers.IColaboracionController;
import com.culturarte.logica.dtos.DTOColaborador;
import com.culturarte.logica.dtos.DTOProponente;
import com.culturarte.logica.dtos.DTOPropuesta;

import com.culturarte.logica.dtos.DTOColabConsulta;
import javax.servlet.http.HttpSession;
import com.culturarte.logica.fabrica.Fabrica;

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

    private final IProponenteController propController = Fabrica.getInstancia().getProponenteController();
    private final IColaboradorController colaController = Fabrica.getInstancia().getColaboradorController();
    private final ISeguimientoController seguiController = Fabrica.getInstancia().getSeguimientoController();
    private final IColaboracionController colabController = Fabrica.getInstancia().getColaboracionController();

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

        // usuario en sesion
        HttpSession session = req.getSession(false);
        String nickSession = null;
        if (session != null) {
            nickSession = (String) session.getAttribute("nick");
        }

        String tipoUsr = req.getParameter("tipoUsr");
        String nick = req.getParameter("nick");
        List<String> seguidooresNick;
        List<String> seguidoosDeNick;

        List<DTOProponente> listaProponentes = propController.listarTodos();
        List<DTOColaborador> listaColaboradores = colaController.listarTodos();

        req.setAttribute("proponentes", listaProponentes);
        req.setAttribute("colaboradores", listaColaboradores);

        // ver si es perfil propio y pasarlo al jsp
        boolean esPropioPerfil = (nickSession != null && nickSession.equals(nick));
        req.setAttribute("esPropioPerfil", esPropioPerfil);

        try{

            if("proponente".equals(tipoUsr)){
                List<Object[]> propConPropu = propController.obtenerTodPropConPropu(nick);
                DTOProponente dtoProponente = new DTOProponente();
                List<DTOPropuesta> dtoPropuestas = new ArrayList<>();

                if(propConPropu != null && !propConPropu.isEmpty()) {
                    for (Object[] fila : propConPropu) {
                        DTOProponente prop = (DTOProponente) fila[0];
                        DTOPropuesta propu = (DTOPropuesta) fila[1];

                        dtoProponente = prop;
                        dtoPropuestas.add(propu);
                    }
                }else{
                    dtoProponente = propController.obtenerProponente(nick);
                }
                req.setAttribute("proponenteSeleccionado", dtoProponente);
                req.setAttribute("propuestasDeProponente", dtoPropuestas);
                seguidooresNick = seguiController.listarSeguidoresDeNick(nick);
                req.setAttribute("seguidooresNick", seguidooresNick);
                seguidoosDeNick = seguiController.listarSeguidosDeNick(nick);
                req.setAttribute("seguidoosDeNick", seguidoosDeNick);

            }else if("colaborador".equals(tipoUsr)){

                // lista simple para perfil ajeno
                List<Object[]> colConPropu = colaController.obtenerTodColConPropu(nick);
                DTOColaborador dtoColaborador = new DTOColaborador();
                List<DTOPropuesta> dtoPropuestas = new ArrayList<>();
                if(colConPropu != null && !colConPropu.isEmpty()) {
                    for (Object[] fila : colConPropu) {
                        DTOColaborador col = (DTOColaborador) fila[0];
                        DTOPropuesta propu = (DTOPropuesta) fila[1];

                        dtoColaborador = col;
                        dtoPropuestas.add(propu);
                    }
                }else{
                    dtoColaborador = colaController.obtenerColaborador(nick);
                }
                req.setAttribute("colaboradorSeleccionado", dtoColaborador);
                req.setAttribute("colaboracionesDeColaborador", dtoPropuestas); // Lista simple

                // PERFIL PROPIO DE COLABORADOR
                if (esPropioPerfil) {
                    // usamos ColaboracionController para obtener la lista detallada
                    List<DTOColabConsulta> detalles = colabController.consultarColaboracionesPorColaborador(nick);
                    req.setAttribute("colaboracionesDetalladas", detalles);
                }

                seguidooresNick = seguiController.listarSeguidoresDeNick(nick);
                req.setAttribute("seguidooresNick", seguidooresNick);
                seguidoosDeNick = seguiController.listarSeguidosDeNick(nick);
                req.setAttribute("seguidoosDeNick", seguidoosDeNick);

            }

        }catch(Exception e){
            req.setAttribute("error", e.getMessage());
        }

        req.getRequestDispatcher("/consultaPerfil.jsp").forward(req, resp);
    }
}
