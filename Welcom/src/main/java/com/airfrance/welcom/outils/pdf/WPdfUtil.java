/*
 * Cr�� le 10 mars 05
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package com.airfrance.welcom.outils.pdf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfWriter;

/**
 * @author M327837 Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class WPdfUtil
{
    /**
     * Ajoute dans les parametres du report les paramtres pour l'ouverture en plein ecran Utilise itext.jar
     * 
     * @param report : report
     * @return flux PDF
     * @throws DocumentException : Probleme a la lecture du document
     * @throws IOException : Probleme a l'ecriture dans la stream
     */
    public static byte[] fullScreen( final byte report[] )
        throws DocumentException, IOException
    {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        fullScreen( report, out );

        return out.toByteArray();
    }

    /**
     * Ajoute dans les parametres du report les paramtres pour l'ouverture en plein ecran Utilise itext.jar
     * 
     * @param report : report
     * @param out : flux pour ecrire
     * @throws DocumentException : Probleme a la lecture du document
     * @throws IOException : Probleme a l'ecriture dans la stream
     */
    public static void fullScreen( final byte report[], final OutputStream out )
        throws DocumentException, IOException
    {
        final PdfStamper stamper = new PdfStamper( new PdfReader( report ), out );

        stamper.setViewerPreferences( PdfWriter.PageLayoutTwoColumnRight | PdfWriter.PageModeFullScreen
            | PdfWriter.NonFullScreenPageModeUseThumbs );

        stamper.close();
    }

    /**
     * Ajoute dans les parametres du report l'impression au chargement du document Utilise itext.jar
     * 
     * @param report : report
     * @return flux PDF
     * @throws DocumentException : Probleme a la lecture du document
     * @throws IOException : Probleme a l'ecriture dans la stream
     */
    public static byte[] autoPrint( final byte report[] )
        throws DocumentException, IOException
    {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        autoPrint( report, out );

        return out.toByteArray();
    }

    /**
     * Ajoute dans les parametres du report l'impression au chargement du document Utilise itext.jar
     * 
     * @param report : report
     * @param out : flux pour ecrire
     * @throws DocumentException : Probleme a la lecture du document
     * @throws IOException : Probleme a l'ecriture dans la stream
     */
    public static void autoPrint( final byte report[], final OutputStream out )
        throws DocumentException, IOException
    {
        final PdfStamper stamper = new PdfStamper( new PdfReader( report ), out );

        stamper.addJavaScript( "this.print(false);" );

        stamper.close();
    }

    /**
     * Ajoute dans les parametres du report l'impression au chargement du document avec choix pour l'imprimante Utilise
     * itext.jar
     * 
     * @param report : report
     * @return flux PDF
     * @throws DocumentException : Probleme a la lecture du document
     * @throws IOException : Probleme a l'ecriture dans la stream
     */
    public static byte[] autoPrintPopup( final byte report[] )
        throws DocumentException, IOException
    {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        autoPrintPopup( report, out );

        return out.toByteArray();
    }

    /**
     * Ajoute dans les parametres du report l'impression au chargement du document avec choix pour l'imprimante Utilise
     * itext.jar
     * 
     * @param report : report
     * @param out : flux pour ecrire
     * @throws DocumentException : Probleme a la lecture du document
     * @throws IOException : Probleme a l'ecriture dans la stream
     */
    public static void autoPrintPopup( final byte report[], final OutputStream out )
        throws DocumentException, IOException
    {
        final PdfStamper stamper = new PdfStamper( new PdfReader( report ), out );

        stamper.addJavaScript( "this.print(true);" );

        stamper.close();
    }
}