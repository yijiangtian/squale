package com.airfrance.squalecommon.enterpriselayer.facade.message;

import java.io.InputStream;
import java.util.Collection;
import java.util.TreeSet;

import org.apache.commons.digester.Digester;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.airfrance.squalecommon.enterpriselayer.businessobject.message.MessageBO;
import com.airfrance.squalecommon.util.xml.XmlImport;

/**
 * Importation des messages
 */
public class MessageImport extends XmlImport {
    /** Log */
    private static Log LOG = LogFactory.getLog(MessageImport.class);
    /** Nom publique de la DTD */
    final static String PUBLIC_DTD = "-//Squale//DTD Message Configuration 1.0//EN";
    /** Localisation de la DTD */
    final static String DTD_LOCATION = "/com/airfrance/squalecommon/dtd/message-1.0.dtd";
    
    /**
     * Constructeur
     *
     */
    public MessageImport() {
        super(LOG);
    }
    /**
     * Importation de messages
     * @param pStream flux de messages
     * @param pErrors erreurs de traitement ou vide si aucune erreur n'est rencontr�e
     * @return collection de messages import�s sous la forme de MessageBO
     */
    public Collection importMessages(InputStream pStream, StringBuffer pErrors) {
        Digester configDigester = setupDigester(pErrors);
        TreeSet messages = new TreeSet();
        configDigester.push(messages);
        parse(configDigester, pStream, pErrors);
        return messages;
    }

    /**
     * Configuration du digester
     * Le digester est utilis� pour le chargement du fichier XML de r�gles
     * @param pErrors erreurs de traitement
     * @return digester
     */
    private Digester setupDigester(StringBuffer pErrors) {
        Digester configDigester = preSetupDigester(PUBLIC_DTD, DTD_LOCATION, pErrors);
        // Traitement des messages
        configDigester.addObjectCreate("messages/message", MessageBO.class);
        configDigester.addSetProperties("messages/message");
        configDigester.addCallMethod("messages/message", "setText", 1);
        configDigester.addCallParam("messages/message", 0);
        configDigester.addSetNext("messages/message", "add");
        return configDigester;
    }

}
