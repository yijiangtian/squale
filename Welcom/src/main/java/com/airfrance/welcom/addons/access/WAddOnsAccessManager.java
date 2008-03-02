/*
 * Cr�� le 26 juil. 05
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package com.airfrance.welcom.addons.access;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.config.ModuleConfig;

import com.airfrance.welcom.addons.access.excel.UpdateAccessManager;
import com.airfrance.welcom.addons.access.excel.filereader.AccessKeyReaderException;
import com.airfrance.welcom.addons.config.AddonsException;
import com.airfrance.welcom.addons.config.WAddOns;
import com.airfrance.welcom.addons.config.WIAddOns;
import com.airfrance.welcom.outils.WelcomConfigurator;

/**
 * @author M327837 Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class WAddOnsAccessManager
    extends WAddOns
{
    /** logger */
    private static Log logStartup = LogFactory.getLog( "Welcom" );

    /** Singleton */
    private static WAddOnsAccessManager me = null;

    /** Addons AccessManagement */
    public final static String ADDONS_ACCESSMANAGEMENT_NAME = "ADDONS_ACCESSMANAGEMENT";

    /** Addons AccessManagement */
    private final static String ADDONS_ACCESSMANAGEMENT_DISPLAYNAME = "Gestion des droits d'acc�s";

    /** Addons AccessManagement */
    private final static String ADDONS_ACCESSMANAGEMENT_VERSION = "2.3";

    /**
     * @return Affichage
     */
    public String getDisplayName()
    {
        return ADDONS_ACCESSMANAGEMENT_DISPLAYNAME;
    }

    /**
     * @return nom de l'addon
     */
    public String getName()
    {
        return ADDONS_ACCESSMANAGEMENT_NAME;
    }

    /**
     * @return Version
     */
    public String getVersion()
    {
        return ADDONS_ACCESSMANAGEMENT_VERSION;
    }

    /**
     * @see com.airfrance.welcom.addons.config.WIAddOns#init(org.apache.struts.action.ActionServlet,
     *      org.apache.struts.config.ModuleConfig)
     */
    public void init( final ActionServlet servlet, final ModuleConfig config )
        throws AddonsException
    {

        final String excelFile =
            WelcomConfigurator.getMessage( WelcomConfigurator.ADDONS_ACCESS_MANAGER_CONFIG_EXCEL_PATH );

        // Mise a jour de la base de donn�e si necessaire
        try
        {
            Class.forName( "jxl.read.biff.BiffException" );
            UpdateAccessManager.update( excelFile );
        }
        catch ( final AccessKeyReaderException e1 )
        {
            throw new AddonsException( e1.getMessage() );
        }
        catch ( final ClassNotFoundException e )
        {
            throw new AddonsException( "L'addon accessManager n�cessite la librairie jxl" );
        }

        // Ajout des mappings
        initMappings( servlet, config );

        setLoaded( true );
    }

    /**
     * @see com.airfrance.welcom.addons.config.WIAddOns#destroy()
     */
    public void destroy()
    {
    }

    /**
     * Initialise le mapping du profil et du profilsaccesskey
     * 
     * @param servlet : Servlet
     * @param config : Module Config
     * @throws AddonsException : Probleme sur l'ajout des mapping ...
     */
    public void initMappings( final ActionServlet servlet, final ModuleConfig config )
        throws AddonsException
    {

        // Liste des profils
        initMappings( servlet, config, null, "addons.accessManager.jsp.profiles", "/wProfileList",
                      "com.airfrance.welcom.addons.access.action.WProfileListeAction" );
        logStartup.info( "Ajout du mapping '/wProfileList.do' pour afficher la liste des profils" );

        // Liste des droits acc�s pour un profil
        initFormBeanConfig( config, "wProfileBean", "com.airfrance.welcom.addons.access.bean.ProfileBean" );
        logStartup.info( "Ajout du bean 'wProfileBean' pour l'action '/wProfile.do'" );
        initMappings( servlet, config, "wProfileBean", "addons.accessManager.jsp.profile", "/wProfile",
                      "com.airfrance.welcom.addons.access.action.WProfileAction" );
        logStartup.info( "Ajout du mapping '/wProfile.do' pour afficher la liste des profils" );

    }

    /**
     * @return Retourne le sigleton
     */
    public static WIAddOns getAddOns()
    {
        if ( me == null )
        {
            me = new WAddOnsAccessManager();
        }
        return me;
    }

}
