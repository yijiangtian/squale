/**
 * Copyright (C) 2008-2010, Squale Project - http://www.squale.org
 *
 * This file is part of Squale.
 *
 * Squale is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or any later version.
 *
 * Squale is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Squale.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.squale.squaleweb.gwt.distributionmap.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.squale.gwt.distributionmap.widget.data.Child;
import org.squale.gwt.distributionmap.widget.data.Parent;
import org.squale.jraf.commons.exception.JrafEnterpriseException;
import org.squale.squalecommon.datatransfertobject.component.AuditDTO;
import org.squale.squalecommon.datatransfertobject.component.ComponentDTO;
import org.squale.squalecommon.datatransfertobject.result.MarkDTO;
import org.squale.squalecommon.enterpriselayer.facade.component.ComponentFacade;
import org.squale.squalecommon.enterpriselayer.facade.quality.QualityResultFacade;
import org.squale.squalecommon.enterpriselayer.facade.rule.QualityGridFacade;
import org.squale.squaleweb.gwt.distributionmap.client.DataService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service that provides data to the distribution map.
 * 
 * @author Fabrice BELLINGARD
 */
@SuppressWarnings( "serial" )
public class DataServiceImpl
    extends RemoteServiceServlet
    implements DataService
{
    /**
     * Logger
     */
    private static Log log = LogFactory.getLog( DataServiceImpl.class );

    /**
     * Date used for logging purposes
     */
    private Date initialDate;

    /**
     * {@inheritDoc}
     */
    public ArrayList<Parent> getData( long auditId, long projectId, long practiceId )
    {
        handleTime();

        Map<Long, Parent> parentMap = new HashMap<Long, Parent>();

        AuditDTO audit = new AuditDTO();
        audit.setID( auditId );
        ComponentDTO project = new ComponentDTO();
        project.setID( projectId );

        try
        {
            String componentLevel = QualityGridFacade.getComponentLevelForPractice( practiceId );

            // Find all the components for the level of the practice
            Collection<ComponentDTO> components = getComponentsOfSpecificLevel( audit, project, componentLevel );

            // Find all the components for the level above the practice level
            Map<Long, ComponentDTO> potentialParentComponentMap =
                getComponentsOfUpperLevel( audit, project, componentLevel );

            // Find all the mark for the practice
            Map<Long, Float> markMap = getMarksForSpecificPractice( auditId, practiceId );

            // and now, build the parent-child tree for the DistributionMap
            for ( ComponentDTO component : components )
            {
                Float componentMark = markMap.get( component.getID() );
                if ( componentMark != null )
                {
                    Child child = new Child( component.getID(), component.getName(), componentMark );
                    // attach the child to its parent
                    long idParent = component.getIDParent();
                    Parent parent = parentMap.get( idParent );
                    if ( parent == null )
                    {
                        ComponentDTO parentComponent = potentialParentComponentMap.get( idParent );
                        if ( parentComponent != null )
                        {
                            parent = new Parent( parentComponent.getName() );
                            parentMap.put( idParent, parent );
                        }
                    }
                    if ( parent != null )
                    {
                        parent.addChild( child );
                    }
                }
            }
        }
        catch ( JrafEnterpriseException e )
        {
            log.error( "Error while building the data needed to display the Dsitribution Map", e );
            // we do not need to do more, we return an empty array
        }

        handleTime();

        return new ArrayList<Parent>( parentMap.values() );
    }

    /**
     * For a specific audit, returns all the marks of a specific practice.
     * 
     * @param auditId the audit id
     * @param practiceId the practice is
     * @return a map containing the value of the marks with the component ID for these marks as the key
     * @throws JrafEnterpriseException if there's a problem while retrieving this collection
     */
    private Map<Long, Float> getMarksForSpecificPractice( long auditId, long practiceId )
        throws JrafEnterpriseException
    {
        Map<Long, Float> markMap = new HashMap<Long, Float>();
        Collection<MarkDTO> marks = QualityResultFacade.getPracticeByAuditRule( auditId, practiceId );
        for ( MarkDTO markDTO : marks )
        {
            markMap.put( markDTO.getComponent().getID(), markDTO.getValue() );
        }
        return markMap;
    }

    /**
     * For a specific audit and a specific project, returns all the components of the upper level of a specific level.
     * 
     * @param audit the audit
     * @param project the project
     * @param componentLevel the lower level of the components we want to get
     * @return a map containing the components of the upper level with their IDs as keys of the map
     * @throws JrafEnterpriseException if there's a problem while retrieving this collection
     */
    private Map<Long, ComponentDTO> getComponentsOfUpperLevel( AuditDTO audit, ComponentDTO project,
                                                               String componentLevel )
        throws JrafEnterpriseException
    {
        Map<Long, ComponentDTO> potentialParentComponentMap = new HashMap<Long, ComponentDTO>();
        String upperComponentLevel = upperLevel( componentLevel );
        Collection<ComponentDTO> potentialParentComponents =
            getComponentsOfSpecificLevel( audit, project, upperComponentLevel );
        for ( ComponentDTO potentialParentDTO : potentialParentComponents )
        {
            potentialParentComponentMap.put( potentialParentDTO.getID(), potentialParentDTO );
        }
        return potentialParentComponentMap;
    }

    /**
     * For a specific audit and a specific project, returns all the components of a specific level.
     * 
     * @param audit the audit
     * @param project the project
     * @param componentLevel the level of the components we want to get
     * @return the collection of components of the specific level
     * @throws JrafEnterpriseException if there's a problem while retrieving this collection
     */
    @SuppressWarnings( "unchecked" )
    private Collection<ComponentDTO> getComponentsOfSpecificLevel( AuditDTO audit, ComponentDTO project,
                                                                   String componentLevel )
        throws JrafEnterpriseException
    {
        Collection<ComponentDTO> components =
            ComponentFacade.getProjectChildren( project, "component." + componentLevel, audit );
        return components;
    }

    /**
     * Returns the name of the upper level of the given level. For instance, for "method", "class" is returned. Note:
     * this is really basic, and should be handled better somewhere else...
     * 
     * @param componentLevel the level for which we want to know the upper level
     * @return the name of the upper level
     */
    private String upperLevel( String componentLevel )
    {
        String upperLevel = "package";
        if ( "method".equals( componentLevel ) )
        {
            upperLevel = "class";
        }
        else if ( "class".equals( componentLevel ) )
        {
            upperLevel = "package";
        }
        return upperLevel;
    }

    /**
     * Used for logging purposes only: prints the execution time
     */
    private void handleTime()
    {
        if ( log.isDebugEnabled() )
        {
            if ( initialDate == null )
            {
                initialDate = new Date();
            }
            else
            {
                Date current = new Date();
                log.debug( "Distribution Map data service execution time: "
                    + ( current.getTime() - initialDate.getTime() ) );
                initialDate = null;
            }
        }
    }
}