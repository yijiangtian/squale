/*
 * Cr�� le 17 janv. 06
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package com.airfrance.welcom.addons.config;

import java.io.File;

import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.config.FormBeanConfig;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.util.RequestUtils;

import com.airfrance.welcom.outils.WelcomConfigurator;

/**
 * @author M327837
 *
 * Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public abstract class WAddOns implements WIAddOns {

    /** Memorise si le plufin est charg� */
    private boolean loaded = false;

    /**
     * @see com.airfrance.welcom.addons.config.WIAddOns#destroy()
     */
    public abstract void destroy();

    /**
     * @see com.airfrance.welcom.addons.config.WIAddOns#init(org.apache.struts.action.ActionServlet, org.apache.struts.config.ModuleConfig)
     */
    public abstract void init(ActionServlet servlet, ModuleConfig config) throws AddonsException;

    /**
     * @see com.airfrance.welcom.addons.config.WIAddOns#getDisplayName()
     */
    public abstract String getDisplayName();

    /**
     * @see com.airfrance.welcom.addons.config.WIAddOns#getName()
     */
    public abstract String getName();

    /**
     * @see com.airfrance.welcom.addons.config.WIAddOns#getVersion()
     */
    public abstract String getVersion();

    /**
     * Ajoute un bean
     * @param config : config struts
     * @param name : nom du bean
     * @param type : classe du bean
     */
    public void initFormBeanConfig(final ModuleConfig config, final String name, final String type) {

        final FormBeanConfig fbc = new FormBeanConfig();
        fbc.setName(name);
        fbc.setType(type);
        fbc.setModuleConfig(config);

        config.addFormBeanConfig(fbc);
    }

    /**
     * Initialise le mapping, si un bean est sp�cfi� le stocke en session
     * @param servlet : Servlet
     * @param config : Module Config
     * @param name : Nom du bean
     * @param welcomKey  : key
     * @param path : path
     * @param type : type
     * @throws AddonsException : Probleme du 'linstanciation de la classe
     */
    public void initMappings(final ActionServlet servlet, final ModuleConfig config, final String name, final String welcomKey, final String path, final String type) throws AddonsException {
        initMappings(servlet, config, name, welcomKey, path, type, "session");
    }

    /**
     * Initialise le mapping
     * @param servlet : Servlet
     * @param config : Module Config
     * @param name : Nom du bean
     * @param welcomKey  : key
     * @param path : path
     * @param type : type
     * @param scope : scope de staockage du bean
     * @throws AddonsException : Probleme du 'linstanciation de la classe
     */
    public void initMappings(final ActionServlet servlet, final ModuleConfig config, final String name, final String welcomKey, final String path, final String type, final String scope)
        throws AddonsException {
        // Verification config
        final String jsp = WelcomConfigurator.getMessage(welcomKey);
        if (GenericValidator.isBlankOrNull(jsp)) {
            throw new AddonsException("ATTENTION Verifier la presence d'une valeur pour la key '" + welcomKey + "' de votre WelcomResources");
        }

        final String jspFullPath = servlet.getServletContext().getRealPath(jsp);
        final File f = new File(jspFullPath);
        if (!f.exists() && !GenericValidator.isBlankOrNull(jspFullPath)) {
            throw new AddonsException("ATTENTION page non trouv� : " + jspFullPath);
        } else {
            try {
                // Table forward
                final ActionMapping mapping = (ActionMapping) RequestUtils.applicationInstance(config.getActionMappingClass());
                mapping.setPath(path);
                mapping.setParameter("action");
                mapping.setType(type);
                if (!GenericValidator.isBlankOrNull(name)) {
                    mapping.setName(name);
                    mapping.setScope(scope);
                }
                mapping.addForwardConfig(new ActionForward("success", jsp, false));

                config.addActionConfig(mapping);
            } catch (final Exception e) {
                throw new AddonsException(e.getMessage());
            }
        }
    }

    /** 
     * @see com.airfrance.welcom.addons.config.WIAddOns#isLoaded()
     */
    public boolean isLoaded() {
        return loaded;
    }

    /** 
     * Sp�cifie si l'objet est charg�
     * @param b : stocke sur l'addons a �t�t initalis�
     */
    public void setLoaded(final boolean b) {
        loaded = b;
    }

}
