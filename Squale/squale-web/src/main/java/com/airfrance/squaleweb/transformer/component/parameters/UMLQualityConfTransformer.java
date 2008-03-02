package com.airfrance.squaleweb.transformer.component.parameters;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.airfrance.squalecommon.datatransfertobject.component.parameters.ListParameterDTO;
import com.airfrance.squalecommon.datatransfertobject.component.parameters.MapParameterDTO;
import com.airfrance.squalecommon.datatransfertobject.component.parameters.StringParameterDTO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.parameters.ParametersConstants;
import com.airfrance.squaleweb.applicationlayer.formbean.component.parameters.UMLQualityForm;
import com.airfrance.welcom.struts.bean.WActionForm;
import com.airfrance.welcom.struts.transformer.WITransformer;
import com.airfrance.welcom.struts.transformer.WTransformerException;

/**
 * Transformation des param�tres de configuration UMLQuality
 */
public class UMLQualityConfTransformer
    implements WITransformer
{

    /**
     * @see com.airfrance.welcom.struts.transformer.WITransformer#objToForm(java.lang.Object[]) {@inheritDoc}
     */
    public WActionForm objToForm( Object[] pObject )
        throws WTransformerException
    {
        UMLQualityForm umlQualityForm = new UMLQualityForm();
        objToForm( pObject, umlQualityForm );
        return umlQualityForm;
    }

    /**
     * @see com.airfrance.welcom.struts.transformer.WITransformer#objToForm(java.lang.Object[],
     *      com.airfrance.welcom.struts.bean.WActionForm) {@inheritDoc}
     */
    public void objToForm( Object[] pObject, WActionForm pForm )
        throws WTransformerException
    {
        MapParameterDTO params = (MapParameterDTO) pObject[0];
        UMLQualityForm umlQualityForm = (UMLQualityForm) pForm;
        // On remplit le formulaire avec l'emplacement
        // du fichier XMI du mod�le
        MapParameterDTO umlQualityParams =
            (MapParameterDTO) params.getParameters().get( ParametersConstants.UMLQUALITY );
        if ( null != umlQualityParams )
        {
            StringParameterDTO xmiFileParam =
                (StringParameterDTO) umlQualityParams.getParameters().get( ParametersConstants.UMLQUALITY_SOURCE_XMI );
            String xmiFilePath = xmiFileParam.getValue();
            umlQualityForm.setXmiFile( xmiFilePath );
            // excludedClasses
            ListParameterDTO classesDTO =
                (ListParameterDTO) umlQualityParams.getParameters().get( ParametersConstants.MODEL_EXCLUDED_CLASSES );
            if ( classesDTO != null )
            {
                List classesParam = classesDTO.getParameters();
                Iterator classesIt = classesParam.iterator();
                String[] classes = new String[classesParam.size()];
                int classesIndex = 0;
                while ( classesIt.hasNext() )
                {
                    StringParameterDTO currClass = (StringParameterDTO) classesIt.next();
                    classes[classesIndex] = currClass.getValue();
                    classesIndex++;
                }
                umlQualityForm.setExcludeUMLPatterns( classes );
            }
        }
    }

    /**
     * @see com.airfrance.welcom.struts.transformer.WITransformer#formToObj(com.airfrance.welcom.struts.bean.WActionForm)
     *      {@inheritDoc}
     */
    public Object[] formToObj( WActionForm pForm )
        throws WTransformerException
    {
        Object[] obj = { new MapParameterDTO() };
        formToObj( pForm, obj );
        return obj;
    }

    /**
     * @see com.airfrance.welcom.struts.transformer.WITransformer#formToObj(com.airfrance.welcom.struts.bean.WActionForm,
     *      java.lang.Object[]) {@inheritDoc}
     */
    public void formToObj( WActionForm pForm, Object[] pObject )
        throws WTransformerException
    {
        MapParameterDTO params = (MapParameterDTO) pObject[0];
        UMLQualityForm umlQualityForm = (UMLQualityForm) pForm;
        // Insertion des param�tres dans la map
        MapParameterDTO umlQualityParams = new MapParameterDTO();
        StringParameterDTO xmiFileParam = new StringParameterDTO();
        xmiFileParam.setValue( umlQualityForm.getXmiFile() );
        umlQualityParams.getParameters().put( ParametersConstants.UMLQUALITY_SOURCE_XMI, xmiFileParam );
        params.getParameters().put( ParametersConstants.UMLQUALITY, umlQualityParams );
        // modelExcludedClasses si non vide:
        String[] excludedPatternsTab = umlQualityForm.getExcludeUMLPatterns();
        ListParameterDTO excludedPatterns = new ListParameterDTO();
        ArrayList paramsList = new ArrayList();
        for ( int i = 0; i < excludedPatternsTab.length; i++ )
        {
            StringParameterDTO strParam = new StringParameterDTO();
            strParam.setValue( excludedPatternsTab[i] );
            paramsList.add( strParam );
        }
        excludedPatterns.setParameters( paramsList );
        umlQualityParams.getParameters().put( ParametersConstants.MODEL_EXCLUDED_CLASSES, excludedPatterns );
        if ( excludedPatternsTab.length == 0 )
        {
            umlQualityParams.getParameters().remove( ParametersConstants.MODEL_EXCLUDED_CLASSES );
        }
    }

}
