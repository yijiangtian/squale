package com.airfrance.welcom.outils.pdf.jasperreports;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.fill.JRFileVirtualizer;

import com.airfrance.welcom.outils.pdf.PDFData;
import com.airfrance.welcom.outils.pdf.PDFDataJasperReports;
import com.airfrance.welcom.outils.pdf.PDFGenerateur;
import com.airfrance.welcom.outils.pdf.PDFGenerateurException;
import com.airfrance.welcom.outils.pdf.advanced.WPdfDecoration;
import com.airfrance.welcom.struts.message.WResourceBundle;

/**
 * @author user
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class PDFJasperReportsWrapper implements PDFGenerateur {

    /** Le pdfdata */
    private PDFData pdfData;

    /** Stream de sortie pour la generation */
    private OutputStream os = null;

    /** Taille de la stream */
    protected int outputSize = 0;

    /** Le template */
    private InputStream template = null;

    /** La data source de Jasper */
    private JRDataSource dataSource = null;

    /** nb de page pour le virtualiser */
    private final static int NB_PAGE= 10;    

    /**
     * @see com.airfrance.welcom.outils.pdf.PDFGenerateur#open(java.io.OutputStream)
     */
    public void open(final OutputStream pOs) throws PDFGenerateurException {
        setOutputStream(pOs);
    }

    /**
     * Chargement du template
     * @param is : Liste des InputStream
     * @throws PDFGenerateurException : Leve l'exception si le fichier est null
     */
    public void loadTemplate(final InputStream is) throws PDFGenerateurException {
        template = is;
    }

    /**
     * @see com.airfrance.welcom.outils.pdf.PDFGenerateur#close()
     */
    public void close() throws PDFGenerateurException {
        printPDF();
    }

    /**
     * Sp�cifie le flux de sortie
     * @param pOs : Flux
     */
    public void setOutputStream(final OutputStream pOs) {
        this.os = pOs;
    }

    /**
     * fabrication du rapport et envoi du rapport dans le outputStream
     * @throws PDFGenerateurException : Verifie si le template a charger ou stream nulle
     */
    public void printPDF() throws PDFGenerateurException {
        if (template == null) {
            throw new PDFGenerateurException("Aucun template de charg�, effectuer un loadTemplate avant");
        }
        if (os == null) {
            throw new PDFGenerateurException("Le stream de sortie est null sur le print PDF");
        }
        if (dataSource == null) {
            throw new PDFGenerateurException("Aucune data source n'a �t� sp�cifi�e");
        }
        JRFileVirtualizer virtualizer = null;
        try {
            Map parameters = null;
            if (pdfData != null) {
                final WResourceBundle bundle = new WResourceBundle(pdfData.getMessageResources(), pdfData.getLocale());
                parameters = new HashMap();
                if (pdfData instanceof PDFDataJasperReports) {
                    final PDFDataJasperReports myPdfData = (PDFDataJasperReports) pdfData;
                    if (myPdfData.getParameters() != null) {
                        parameters = myPdfData.getParameters();
                    }
                    if (myPdfData.isVirtualize()) {
                        virtualizer = new JRFileVirtualizer(NB_PAGE);
                        parameters.put(JRParameter.REPORT_VIRTUALIZER, virtualizer);
                    }
                }
                if (!parameters.containsKey("REPORT_RESOURCE_BUNDLE")) {
                    parameters.put("REPORT_RESOURCE_BUNDLE", bundle);
                }

            }
            final JasperPrint jasperPrint = JasperFillManager.fillReport(template, parameters, dataSource);
            final byte[] output = JasperExportManager.exportReportToPdf(jasperPrint);
            os.write(output);
        } catch (final JRException jre) {
            jre.printStackTrace();
        } catch (final IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if (virtualizer != null) {
                virtualizer.cleanup();
            }
        }
    }

    /**
     * @return Report ...
     */
    public Object getReport() {
        return null;
    }

    /**
     * @see com.airfrance.welcom.outils.pdf.PDFGenerateur#getPDFWriter()
     */
    public Object getPDFWriter() {
        return null;
    }

    /**
     * @see com.airfrance.welcom.outils.pdf.PDFGenerateur#setDecoration(com.airfrance.welcom.outils.pdf.advanced.WPdfDecoration)
     */
    public void setDecoration(final WPdfDecoration pdecoration) throws PDFGenerateurException {
        throw new PDFGenerateurException("Attention, la m�thode setDecoration() n'est utilisable qu'avec un moteur de type Itext");
    }

    /**
     * Sp�cifie la data source pour les rapports g�n�r�s avec JasperReports (exclusivement)
     * @param pDataSource : la data source
     */
    public void setDataSource(final JRDataSource pDataSource) {
        dataSource = pDataSource;
    }

    /**
     * @param data le PDFdata
     */
    public void setPdfData(final PDFData data) {
        pdfData = data;
    }

}