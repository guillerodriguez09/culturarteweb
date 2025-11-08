package com.culturarte.web;


import com.culturarte.web.ws.cliente.*;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Esta clase es un "ServletContextListener".
 * Se ejecuta automáticamente UNA VEZ cuando la aplicación web (Tomcat) arranca.
 * Su trabajo es leer la URL del web.xml y crear los clientes de Web Service.
 */
@WebListener
public class ClienteWS implements ServletContextListener {

    // Este metod se ejecuta cuando la aplicación arranca
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("Inicializando clientes de Web Service...");

        ServletContext context = sce.getServletContext();

        String baseUrl = context.getInitParameter("ws.urlBase");

        if (baseUrl == null) {
            System.err.println("no se encontro ws.urlBase en web.xml");
            return;
        }

        try {

            // Cliente para Proponente
            URL proponenteWSDL = new URL(baseUrl + "/proponente?wsdl");
            ProponenteControllerService propService = new ProponenteControllerService(proponenteWSDL);
            IProponenteController propController = propService.getProponenteControllerPort();

            // Cliente para Colaborador
            URL colaboradorWSDL = new URL(baseUrl + "/colaborador?wsdl");
            ColaboradorControllerService colaService = new ColaboradorControllerService(colaboradorWSDL);
            IColaboradorController colaController = colaService.getColaboradorControllerPort();

            // Cliente para Propuesta
            URL propuestaWSDL = new URL(baseUrl + "/propuesta?wsdl");
            PropuestaControllerService propuService = new PropuestaControllerService(propuestaWSDL);
            IPropuestaController propuController = propuService.getPropuestaControllerPort();

            // Cliente para Colaboracion
            URL colaboracionWSDL = new URL(baseUrl + "/colaboracion?wsdl");
            ColaboracionControllerService colabService = new ColaboracionControllerService(colaboracionWSDL);
            IColaboracionController colabController = colabService.getColaboracionControllerPort();

            // Cliente para Seguimiento
            URL seguimientoWSDL = new URL(baseUrl + "/seguimiento?wsdl");
            SeguimientoControllerService seguiService = new SeguimientoControllerService(seguimientoWSDL);
            ISeguimientoController seguiController = seguiService.getSeguimientoControllerPort();

            // Cliente para Categoria
            URL categoriaWSDL = new URL(baseUrl + "/categoria?wsdl");
            CategoriaControllerService catService = new CategoriaControllerService(categoriaWSDL);
            ICategoriaController catController = catService.getCategoriaControllerPort();


            // Guardamos los clientes en el ServletContext
            context.setAttribute("ws.proponente", propController);
            context.setAttribute("ws.colaborador", colaController);
            context.setAttribute("ws.propuesta", propuController);
            context.setAttribute("ws.colaboracion", colabController);
            context.setAttribute("ws.seguimiento", seguiController);
            context.setAttribute("ws.categoria", catController);

            System.out.println("¡Clientes de Web Service inicializados con éxito!");

        } catch (MalformedURLException e) {
            System.err.println("Error al inicializar clientes WS: " + e.getMessage());
        }
    }

    // cuando se baja
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("Deteniendo clientes de Web Service...");
    }
}
