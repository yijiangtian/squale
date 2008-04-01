package com.airfrance.squaleweb.transformer.component.parameters;

import java.util.HashMap;
import java.util.Map;

import com.airfrance.squalecommon.datatransfertobject.component.parameters.MapParameterDTO;
import com.airfrance.squalecommon.datatransfertobject.component.parameters.StringParameterDTO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.parameters.ParametersConstants;
import com.airfrance.squaleweb.applicationlayer.formbean.component.parameters.ScmForm;
import com.airfrance.welcom.struts.bean.WActionForm;
import com.airfrance.welcom.struts.transformer.WITransformer;
import com.airfrance.welcom.struts.transformer.WTransformerException;

/**
 * Transformation of Scm parameters
 */
public class ScmConfTransformer
    implements WITransformer
{

    /**
     * Transformer from an object to a form
     * 
     * @see com.airfrance.welcom.struts.transformer.WITransformer#objToForm(java.lang.Object[])
     * @param pObject array of objects
     * @return form transformed
     * @throws WTransformerException if an error occurs
     */
    public WActionForm objToForm( Object[] pObject )
        throws WTransformerException
    {
        ScmForm scmForm = new ScmForm();
        objToForm( pObject, scmForm );
        return scmForm;
    }

    /**
     * Transformer from an object to the form
     * 
     * @see com.airfrance.welcom.struts.transformer.WITransformer#objToForm(java.lang.Object[],
     *      com.airfrance.welcom.struts.bean.WActionForm)
     * @param pObject array of objects
     * @param pForm the form
     * @throws WTransformerException if an error occurs
     */
    public void objToForm( Object[] pObject, WActionForm pForm )
        throws WTransformerException
    {
        MapParameterDTO projectParams = (MapParameterDTO) pObject[0];
        MapParameterDTO params = (MapParameterDTO) projectParams.getParameters().get( ParametersConstants.SCM );
        if ( params != null )
        {
            // Fill the form
            ScmForm scmForm = (ScmForm) pForm;
            Map ccParams = params.getParameters();

            // Path to audit
            StringParameterDTO pathToAudit = (StringParameterDTO) ccParams.get( ParametersConstants.SCMPATH );
            scmForm.setPathToAudit( pathToAudit.getValue() );
            // User profile to connect to the remote repository
            StringParameterDTO login = (StringParameterDTO) ccParams.get( ParametersConstants.SCMLOGIN );
            scmForm.setLogin( login.getValue() );
            // Password
            StringParameterDTO password = (StringParameterDTO) ccParams.get( ParametersConstants.SCMPASSWORD );
            scmForm.setPassword( password.getValue() );
        }
    }

    /**
     * Transformer from the form to an object
     * 
     * @see com.airfrance.welcom.struts.transformer.WITransformer#formToObj(com.airfrance.welcom.struts.bean.WActionForm)
     * @param pForm the form
     * @return array of objects that are transformed
     * @throws WTransformerException if an error occurs
     */
    public Object[] formToObj( WActionForm pForm )
        throws WTransformerException
    {
        Object[] obj = { new MapParameterDTO() };
        formToObj( pForm, obj );
        return obj;
    }

    /**
     * Transformer from the form to an object
     * 
     * @see com.airfrance.welcom.struts.transformer.WITransformer#formToObj(com.airfrance.welcom.struts.bean.WActionForm,
     *      java.lang.Object[])
     * @param pForm the form
     * @param pObject array of objects
     * @throws WTransformerException if an error occurs
     */
    public void formToObj( WActionForm pForm, Object[] pObject )
        throws WTransformerException
    {
        MapParameterDTO params = (MapParameterDTO) pObject[0];
        ScmForm scmForm = (ScmForm) pForm;
        // Specific parameters of Scm are added to general project's
        Map scmParams = new HashMap();
        // pathToAudit:
        StringParameterDTO pathToAudit = new StringParameterDTO();
        pathToAudit.setValue( scmForm.getPathToAudit() );
        scmParams.put( ParametersConstants.SCMPATH, pathToAudit );
        // user profile
        StringParameterDTO login = new StringParameterDTO();
        login.setValue( scmForm.getLogin() );
        scmParams.put( ParametersConstants.SCMLOGIN, login );
        // password
        StringParameterDTO password = new StringParameterDTO();
        password.setValue( scmForm.getPassword() );
        scmParams.put( ParametersConstants.SCMPASSWORD, password );

        MapParameterDTO scmMap = new MapParameterDTO();
        scmMap.setParameters( scmParams );
        params.getParameters().put( ParametersConstants.SCM, scmMap );
    }
}