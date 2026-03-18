package com.inmobiliaria.alquileres_temporarios.reservas.service;

import com.inmobiliaria.alquileres_temporarios.reservas.dto.ReporteLiquidacionDTO;
import com.inmobiliaria.alquileres_temporarios.reservas.dto.ReservaDetalleDTO;
import com.inmobiliaria.alquileres_temporarios.reservas.dto.GastoDetalleDTO;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.IOException;

@Service
public class PdfService {

    public void exportarLiquidacion(HttpServletResponse response, ReporteLiquidacionDTO data) throws IOException {
        Document document = new Document(PageSize.A4, 40, 40, 40, 40);
        PdfWriter.getInstance(document, response.getOutputStream());
        
        document.open();

        // --- PALETA DE COLORES SOBRIOS ---
        Color azulOscuro = new Color(44, 62, 80);   // Gris azulado para títulos
        Color grisCabecera = new Color(52, 73, 94); // Gris para encabezados
        Color grisFondo = new Color(248, 249, 250); // Fondo muy claro para datos
        Color grisBorde = new Color(220, 225, 230); // Color de líneas

        // --- FUENTES ---
        Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, azulOscuro);
        Font fontSubtitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, azulOscuro);
        Font fontHeaderTabla = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.WHITE);
        Font fontCuerpo = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.BLACK);
        Font fontNegrita = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.BLACK);

        // --- ENCABEZADO PRINCIPAL ---
        PdfPTable mainHeader = new PdfPTable(1);
        mainHeader.setWidthPercentage(100);
        
        PdfPCell titleCell = new PdfPCell(new Phrase("CHE ALQUILA - GESTIÓN INMOBILIARIA", fontTitulo));
        titleCell.setBorder(Rectangle.BOTTOM);
        titleCell.setBorderWidth(2f);
        titleCell.setBorderColor(azulOscuro);
        titleCell.setPaddingBottom(10);
        mainHeader.addCell(titleCell);
        document.add(mainHeader);
        
        document.add(new Paragraph(" "));

        // --- BLOQUE INFO PROPIETARIO ---
        PdfPTable infoTable = new PdfPTable(2);
        infoTable.setWidthPercentage(100);
        
        infoTable.addCell(createNoBorderCell("PROPIETARIO:\n" + "Luciano Bauer", fontNegrita, Element.ALIGN_LEFT));
        infoTable.addCell(createNoBorderCell("PERÍODO DE LIQUIDACIÓN:\n" + "Marzo 2026", fontNegrita, Element.ALIGN_RIGHT));
        
        document.add(infoTable);
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));

        // --- TABLA DE RESERVAS ---
        document.add(new Paragraph("INGRESOS POR ESTADÍAS", fontSubtitulo));
        document.add(new Paragraph(" "));
        
        PdfPTable tRes = new PdfPTable(4);
        tRes.setWidthPercentage(100);
        tRes.setWidths(new float[]{1.2f, 2.5f, 1.5f, 1.2f}); // Ajuste de anchos
        
        String[] hR = {"FECHA", "PROPIEDAD", "INQUILINO", "MONTO"};
        for (String h : hR) {
            PdfPCell c = new PdfPCell(new Phrase(h, fontHeaderTabla));
            c.setBackgroundColor(grisCabecera);
            c.setPadding(7);
            c.setBorderColor(grisBorde);
            c.setHorizontalAlignment(Element.ALIGN_CENTER);
            tRes.addCell(c);
        }

        for (ReservaDetalleDTO res : data.getDetalleReservas()) {
            tRes.addCell(createDataCell(res.getFecha().toString(), fontCuerpo, Element.ALIGN_CENTER, grisBorde));
            tRes.addCell(createDataCell(res.getPropiedad(), fontCuerpo, Element.ALIGN_LEFT, grisBorde));
            tRes.addCell(createDataCell(res.getInquilino(), fontCuerpo, Element.ALIGN_LEFT, grisBorde));
            tRes.addCell(createDataCell("$ " + res.getMonto(), fontCuerpo, Element.ALIGN_RIGHT, grisBorde));
        }
        document.add(tRes);

        // --- TABLA DE GASTOS (Si hay) ---
        if (!data.getDetalleGastos().isEmpty()) {
            document.add(new Paragraph(" "));
            document.add(new Paragraph("GASTOS DE MANTENIMIENTO Y DEDUCCIONES", fontSubtitulo));
            document.add(new Paragraph(" "));
            
            PdfPTable tGas = new PdfPTable(2);
            tGas.setWidthPercentage(100);
            tGas.setWidths(new float[]{4f, 1f});

            PdfPCell hG1 = new PdfPCell(new Phrase("CONCEPTO", fontHeaderTabla));
            hG1.setBackgroundColor(new Color(127, 140, 141)); // Gris intermedio
            hG1.setPadding(7);
            tGas.addCell(hG1);
            
            PdfPCell hG2 = new PdfPCell(new Phrase("MONTO", fontHeaderTabla));
            hG2.setBackgroundColor(new Color(127, 140, 141));
            hG2.setHorizontalAlignment(Element.ALIGN_RIGHT);
            hG2.setPadding(7);
            tGas.addCell(hG2);

            for (GastoDetalleDTO g : data.getDetalleGastos()) {
                tGas.addCell(createDataCell(g.getMotivo(), fontCuerpo, Element.ALIGN_LEFT, grisBorde));
                tGas.addCell(createDataCell("- $ " + g.getMonto(), fontCuerpo, Element.ALIGN_RIGHT, grisBorde));
            }
            document.add(tGas);
        }

        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));

        // --- RESUMEN FINAL ---
        PdfPTable resTable = new PdfPTable(2);
        resTable.setWidthPercentage(45);
        resTable.setHorizontalAlignment(Element.ALIGN_RIGHT);

        addSummaryRow(resTable, "Total Ingresos Brutos:", "$ " + data.getTotalIngresosBrutos(), fontCuerpo);
        addSummaryRow(resTable, "Comisión Agencia (20%):", "- $ " + data.getComisionAgencia(), fontCuerpo);
        addSummaryRow(resTable, "Gastos Mantenimiento:", "- $ " + data.getGastosMantenimiento(), fontCuerpo);
        
        PdfPCell totalLabel = new PdfPCell(new Phrase("NETO A TRANSFERIR:", fontNegrita));
        totalLabel.setPadding(8);
        totalLabel.setBackgroundColor(grisFondo);
        totalLabel.setBorder(Rectangle.TOP);
        resTable.addCell(totalLabel);

        PdfPCell totalVal = new PdfPCell(new Phrase("$ " + data.getTotalALiquidar(), fontNegrita));
        totalVal.setPadding(8);
        totalVal.setBackgroundColor(grisFondo);
        totalVal.setHorizontalAlignment(Element.ALIGN_RIGHT);
        totalVal.setBorder(Rectangle.TOP);
        resTable.addCell(totalVal);

        document.add(resTable);

        document.close();
    }

    // --- MÉTODOS AUXILIARES PARA LIMPIEZA DE CÓDIGO ---
    
    private PdfPCell createDataCell(String text, Font font, int align, Color borderColor) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(6);
        cell.setHorizontalAlignment(align);
        cell.setBorderColor(borderColor);
        return cell;
    }

    private PdfPCell createNoBorderCell(String text, Font font, int align) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(align);
        return cell;
    }

    private void addSummaryRow(PdfPTable table, String label, String value, Font font) {
        PdfPCell cLabel = new PdfPCell(new Phrase(label, font));
        cLabel.setBorder(Rectangle.NO_BORDER);
        cLabel.setPadding(4);
        table.addCell(cLabel);

        PdfPCell cVal = new PdfPCell(new Phrase(value, font));
        cVal.setBorder(Rectangle.NO_BORDER);
        cVal.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cVal.setPadding(4);
        table.addCell(cVal);
    }
}