package com.culturarte.web.fabrica; // O el paquete donde la hayas creado

// Imports de las INTERFACES (vienen de la dependencia de culturartepda)
import com.culturarte.logica.controllers.*;

// Imports para JAX-WS (consumir el servicio)
import java.net.URL;
import javax.xml.namespace.QName;
import jakarta.xml.ws.Service;

// Imports para leer el config.properties
import java.io.FileReader;
import java.util.Properties;

public class FabricaWeb {

    private static FabricaWeb instancia;

    //  de los controladores
    private IPropuestaController propuestaController;
    private IColaboradorController colaboradorController;
    private IProponenteController proponenteController;
    private IColaboracionController colaboracionController;
    private ICategoriaController categoriaController;
    private ISeguimientoController seguimientoController;

    /**
     * Carga las propiedades desde el archivo de configuración.
     * Lee de ~/.Culturarte/config.properties
     */
    private static Properties getConfig() throws Exception {
        String configDir = System.getProperty("user.home") + System.getProperty("file.separator") + ".Culturarte";
        Properties props = new Properties();
        props.load(new FileReader(configDir + System.getProperty("file.separator") + "config.properties"));
        return props;
    }

    // el constructor privado se encarga de conectar con los servicios
    private FabricaWeb() {
        try {
            // Carga la URL base desde el archivo de config
            Properties config = getConfig();
            String urlBase = config.getProperty("ws.urlPublicacion");

            if (urlBase == null) {
                throw new RuntimeException("Error");
            }

            //conecta con los servicios
            URL urlPropuesta = new URL(urlBase + "/propuesta?wsdl");
            QName qnamePropuesta = new QName("http://controllers.logica.culturarte.com/", "PropuestaControllerService");
            Service servicePropuesta = Service.create(urlPropuesta, qnamePropuesta);
            this.propuestaController = servicePropuesta.getPort(IPropuestaController.class);


            URL urlColab = new URL(urlBase + "/colaborador?wsdl");
            QName qnameColab = new QName("http://controllers.logica.culturarte.com/", "ColaboradorControllerService");
            Service serviceColab = Service.create(urlColab, qnameColab);
            this.colaboradorController = serviceColab.getPort(IColaboradorController.class);


            URL urlProp = new URL(urlBase + "/proponente?wsdl");
            QName qnameProp = new QName("http://controllers.logica.culturarte.com/", "ProponenteControllerService");
            Service serviceProp = Service.create(urlProp, qnameProp);
            this.proponenteController = serviceProp.getPort(IProponenteController.class);


            URL urlCol = new URL(urlBase + "/colaboracion?wsdl");
            QName qnameCol = new QName("http://controllers.logica.culturarte.com/", "ColaboracionControllerService");
            Service serviceCol = Service.create(urlCol, qnameCol);
            this.colaboracionController = serviceCol.getPort(IColaboracionController.class);


            URL urlCat = new URL(urlBase + "/categoria?wsdl");
            QName qnameCat = new QName("http://controllers.logica.culturarte.com/", "CategoriaControllerService");
            Service serviceCat = Service.create(urlCat, qnameCat);
            this.categoriaController = serviceCat.getPort(ICategoriaController.class);


            URL urlSeg = new URL(urlBase + "/seguimiento?wsdl");
            QName qnameSeg = new QName("http://controllers.logica.culturarte.com/", "SeguimientoControllerService");
            Service serviceSeg = Service.create(urlSeg, qnameSeg);
            this.seguimientoController = serviceSeg.getPort(ISeguimientoController.class);


        } catch (Exception e) {
            System.err.println("Error al conectar con los Web Services:");
            e.printStackTrace();
        }
    }

    public static FabricaWeb getInstancia() {
        if (instancia == null) {
            instancia = new FabricaWeb();
        }
        return instancia;
    }

    public IPropuestaController getPropuestaController() { return this.propuestaController; }
    public IColaboradorController getColaboradorController() { return this.colaboradorController; }
    public IProponenteController getProponenteController() { return this.proponenteController; }
    public IColaboracionController getColaboracionController() { return this.colaboracionController; }
    public ICategoriaController getCategoriaController() { return this.categoriaController; }
    public ISeguimientoController getSeguimientoController() { return this.seguimientoController; }
}
