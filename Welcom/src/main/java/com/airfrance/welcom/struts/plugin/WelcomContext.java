/*
 * Cr�� le 1 f�vr. 06
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package com.airfrance.welcom.struts.plugin;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.validator.GenericValidator;

/**
 * @author M327837 Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class WelcomContext
{

    /** Nom de context Welcom, resoud le pb de clonnage */
    private final static String DEFAULT_WELCOM_CONTEXT_NAME = "Welcom";

    /** Nombre d'initialisation du context */
    private static int nbInit = 0;

    /**
     * @return le nom du welcomContext (java:com/env) declar� sur le serveur
     */
    private static String lookupWelcomContextName()
    {
        InitialContext ic;
        String welcomContextName;
        try
        {
            ic = new InitialContext();
            welcomContextName = (String) ic.lookup( "java:comp/env/welcomContext" );
        }
        catch ( final NamingException e )
        {
            welcomContextName = DEFAULT_WELCOM_CONTEXT_NAME;
        }
        return welcomContextName;
    }

    /** Nom du contexte sur le threadLocal */
    private static ThreadLocal threadLocal = new ThreadLocal();

    /**
     * Initalise le WelcomContextName
     */
    static void initWelcomContextName()
    {
        nbInit++;
    }

    /**
     * Reinitialise le contexte de Welcom
     */
    public static void resetWelcomContextName()
    {
        synchronized ( threadLocal )
        {
            threadLocal.set( lookupWelcomContextName() );
        }
    }

    /**
     * Recuperele contextName de Welcom
     * 
     * @return le nom du contexte de welcom
     */
    public static String getWelcomContextName()
    {
        String welcomContextName = DEFAULT_WELCOM_CONTEXT_NAME;
        if ( nbInit > 1 )
        {
            welcomContextName = (String) threadLocal.get();
            if ( GenericValidator.isBlankOrNull( welcomContextName ) )
            {
                welcomContextName = lookupWelcomContextName();
                threadLocal.set( welcomContextName );
            }
        }
        return welcomContextName;
    }

}
