package com.culturarte.web;

import com.culturarte.web.ws.cliente.DtoConstanciaPago;
import com.culturarte.web.ws.cliente.IColaboracionController;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

@WebServlet("/descargarConstanciaPDF")
public class DescargarConstanciaPDFServlet extends HttpServlet {

    private IColaboracionController colaboracionCtrl;

    @Override
    public void init() throws ServletException {
        ServletContext ctx = getServletContext();
        Object attr = ctx.getAttribute("ws.colaboracion");
        if (attr == null) {
            throw new ServletException("Cliente WS de Colaboración no encontrado en ServletContext (ws.colaboracion).");
        }
        this.colaboracionCtrl = (IColaboracionController) attr;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String idStr = req.getParameter("id");
        if (idStr == null || idStr.isEmpty()) idStr = req.getParameter("idColab");
        if (idStr == null || idStr.isEmpty()) idStr = req.getParameter("idColaboracion");

        if (idStr == null || idStr.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Falta parámetro id de la colaboración.");
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de colaboración inválido.");
            return;
        }

        try {
            DtoConstanciaPago datos = colaboracionCtrl.emitirConstanciaPago(id);

            resp.setContentType("application/pdf");

            String nombreArchivo = "constancia_" + id + ".pdf";
            resp.setHeader("Content-Disposition", "attachment; filename=\"" + nombreArchivo + "\"");


            PdfWriter writer = new PdfWriter(resp.getOutputStream());
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf, PageSize.A4);
            document.setMargins(40, 40, 40, 40);


            document.add(new Paragraph("Constancia de Pago de Colaboración")
                    .setBold()
                    .setFontSize(18)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(16f));


            document.add(new Paragraph("Datos generales").setBold().setFontSize(12));
            Table tablaGeneral = new Table(new float[]{1, 2});
            tablaGeneral.setWidth(UnitValue.createPercentValue(100));
            agregarFila(tablaGeneral, "Plataforma:", datos.getPlataforma());
            agregarFila(tablaGeneral, "Fecha de emisión:", datos.getFechaEmision());
            document.add(tablaGeneral);
            document.add(new Paragraph("\n"));


            document.add(new Paragraph("Datos del colaborador").setBold().setFontSize(12));
            Table tablaColab = new Table(new float[]{1, 2});
            tablaColab.setWidth(UnitValue.createPercentValue(100));
            agregarFila(tablaColab, "Nombre completo:", datos.getColaboradorNombre());
            agregarFila(tablaColab, "Nickname:", datos.getColaboradorNick());
            agregarFila(tablaColab, "Email:", datos.getColaboradorEmail());
            document.add(tablaColab);
            document.add(new Paragraph("\n"));


            document.add(new Paragraph("Datos de la colaboración").setBold().setFontSize(12));
            Table tablaColaboracion = new Table(new float[]{1, 2});
            tablaColaboracion.setWidth(UnitValue.createPercentValue(100));
            agregarFila(tablaColaboracion, "Propuesta:", datos.getPropuestaNombre());
            agregarFila(tablaColaboracion, "Fecha de colaboración:", datos.getFechaColaboracion());
            agregarFila(tablaColaboracion, "Monto aportado:", datos.getMonto() != null ? ("$" + datos.getMonto()) : "");
            document.add(tablaColaboracion);
            document.add(new Paragraph("\n"));


            document.add(new Paragraph(
                    "La presente constancia certifica que el colaborador realizó la colaboración indicada " +
                            "en la plataforma Culturarte."
            ).setFontSize(11));

            document.close();


        } catch (Exception e) {
            e.printStackTrace();

            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al generar la constancia: " + e.getMessage());
        }
    }

    private void agregarFila(Table tabla, String etiqueta, String valor) {
        tabla.addCell(new Cell().add(new Paragraph(etiqueta).setBold()));
        tabla.addCell(new Cell().add(new Paragraph(valor != null ? valor : "")));
    }
}
