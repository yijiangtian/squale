package com.airfrance.squaleweb.transformer;

import java.util.Collection;

import com.airfrance.squalecommon.datatransfertobject.component.ComponentDTO;
import com.airfrance.squaleweb.applicationlayer.formbean.component.ProjectForm;
import com.airfrance.welcom.struts.bean.WActionForm;
import com.airfrance.welcom.struts.transformer.WITransformer;
import com.airfrance.welcom.struts.transformer.WTransformerException;

/**
 * Transformateurs DTO <-> Form pour les projets
 * 
 * @author M400842
 */
public class ProjectTransformer
    implements WITransformer
{
    /**
     * @param pObject l'objet � transformer
     * @throws WTransformerException si un pb apparait.
     * @return le formulaire.
     */
    public WActionForm objToForm( Object[] pObject )
        throws WTransformerException
    {
        ProjectForm form = new ProjectForm();
        objToForm( pObject, form );
        return form;
    }

    /**
     * @param pObject l'objet � transformer
     * @param pForm le formulaire � remplir.
     * @throws WTransformerException si un pb apparait.
     */
    public void objToForm( Object[] pObject, WActionForm pForm )
        throws WTransformerException
    {
        ComponentDTO dto = (ComponentDTO) pObject[0];
        ProjectForm form = (ProjectForm) pForm;
        form.setId( dto.getID() );
        form.setProjectName( dto.getName() );
        form.setApplicationId( "" + dto.getIDParent() );
        form.setHasTerminatedAudit( dto.getHasResults() );
        // Si le tableau d'objets pass� en param�tre contient 2 objets
        // alors le deuxi�me est la liste des applications stock�es en session.
        // Cela arrive dans les cas o� l'on veut r�cup�rer le nom de l'application
        // associ�e au projet.
        if ( pObject.length == 2 )
        {
            Collection applications = (Collection) pObject[1];
            String applicationName = TransformerUtils.getApplicationName( dto.getIDParent(), applications );
            form.setApplicationName( applicationName );
        }
    }

    /**
     * @param pForm le formulaire � lire.
     * @throws WTransformerException si un pb apparait.
     * @return le tableaux des objets.
     */
    public Object[] formToObj( WActionForm pForm )
        throws WTransformerException
    {
        Object[] obj = { new ComponentDTO() };
        formToObj( pForm, obj );
        return obj;
    }

    /**
     * @param pObject l'objet � remplir
     * @param pForm le formulaire � lire.
     * @throws WTransformerException si un pb apparait.
     */
    public void formToObj( WActionForm pForm, Object[] pObject )
        throws WTransformerException
    {
        ProjectForm form = (ProjectForm) pForm;
        ComponentDTO dto = (ComponentDTO) pObject[0];
        dto.setID( form.getId() );
        dto.setIDParent( form.getParentId() );
        dto.setName( form.getProjectName() );
    }
}
