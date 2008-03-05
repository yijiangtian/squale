/*
 * Cr�� le 6 sept. 05, par M400832.
 */
package com.airfrance.squalix.tools.clearcase;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.airfrance.jraf.commons.exception.JrafDaoException;
import com.airfrance.jraf.commons.exception.JrafPersistenceException;
import com.airfrance.squalecommon.SqualeTestCase;
import com.airfrance.squalecommon.daolayer.component.ProjectDAOImpl;
import com.airfrance.squalecommon.daolayer.component.ProjectParameterDAOImpl;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.ApplicationBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.AuditBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.ProjectBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.parameters.ListParameterBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.parameters.MapParameterBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.parameters.ParametersConstants;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.parameters.StringParameterBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.rule.QualityGridBO;
import com.airfrance.squalix.core.TaskData;
import com.airfrance.squalix.tools.clearcase.task.ClearCaseTask;

/**
 * Cette classe teste le montage de vues ClearCase.
 */
// UNIT_PENDING : montage de la vue au niveau windows --> configuration xml � changer
// Les test � �t� mis � jour pour les initialisations mais il faut se pencher sur le fonctionnel
// au niveau Windows.
public class ClearCaseTaskTest
    extends SqualeTestCase
{

    /**
     * Nom de la branche.
     */
    private static final String BRANCH_NAME = "squale_v0_0_act";

    /**
     * Nom de l'application au sens clearcase.
     */
    private static final String APPLI_NAME = "squale";

    /**
     * Nom de la vob.
     */
    private static final String VOB_NAME = "/vobs/squale/squaleCommon";

    /**
     * Nom du view path
     */
    private static final String VIEW_PATH = "data/viewPath";

    /** pour le view_path */
    private TaskData mDatas;

    /**
     * Audit.
     */
    private AuditBO mAudit;

    /**
     * Projet
     */
    private ProjectBO mProject;

    /**
     * Application.
     */
    private ApplicationBO mApplication;

    /**
     * HashMap de param�tres du projet.
     */
    private MapParameterBO mHashMap;

    /**
     * T�che ClearCase.
     */
    private ClearCaseTask mCct;

    /**
     * LOGGER.
     */
    private static final Log LOGGER = LogFactory.getLog( ClearCaseTaskTest.class );

    /**
     * Constructeur.
     * 
     * @param pArgs param�tre.
     */
    public ClearCaseTaskTest( String pArgs )
    {
        super( pArgs );
    }

    /**
     * Set-up.
     * 
     * @throws Exception en cas de probl�me lors de la r�initialisation
     */
    protected void setUp()
        throws Exception
    {
        super.setUp();
        getSession().beginTransaction();
        /* D�finitions des param�tres communs */
        // L'application
        mApplication = getComponentFactory().createApplication( getSession() );
        // Le projet avec sa grille associ�e
        QualityGridBO grid = getComponentFactory().createGrid( getSession() );
        mProject = getComponentFactory().createProject( getSession(), mApplication, grid );
        initProject();
        // L'audit
        mAudit = getComponentFactory().createAudit( getSession(), mProject );
        // Les param�tres temporaires
        mDatas = new TaskData();
        mDatas.putData( TaskData.VIEW_PATH, VIEW_PATH );
        getSession().commitTransactionWithoutClose();

    }

    /**
     * Montage d'un audit de suivi sur l'application SQUALE.
     * 
     * @see #doNormalTest(String, String)
     */
    public void testMountSqualeNormal()
        throws JrafPersistenceException
    {
        /* lancement de la t�che */
        launchTask();

        /* test de pr�sence de la vue */
        assertTrue( viewIsMounted() );

        /* d�montage de la vue */
        umountView();
    }

    /**
     * Montage d'un audit de jalon.
     * 
     * @see #doMilestoneTest(String, String)
     */
    public void testSqualeMilestone()
        throws JrafPersistenceException
    {
        // Audit de jalon
        mAudit.setType( AuditBO.MILESTONE );
        mAudit.setName( "SQUALE_V0_0_ACT" );
        testMountSqualeNormal();
    }

    /**
     * Initialise le projet
     * 
     * @throws JrafDaoException si exception
     */
    private void initProject()
        throws JrafDaoException
    {
        mHashMap = new MapParameterBO();
        MapParameterBO clearcaseMap = new MapParameterBO();
        StringParameterBO branchBO = new StringParameterBO();
        StringParameterBO appliBO = new StringParameterBO();
        ListParameterBO vobsBO = new ListParameterBO();
        StringParameterBO vobBO = new StringParameterBO();
        branchBO.setValue( BRANCH_NAME );
        appliBO.setValue( APPLI_NAME );
        vobBO.setValue( VOB_NAME );
        vobsBO.getParameters().add( vobBO );
        clearcaseMap.getParameters().put( ParametersConstants.BRANCH, branchBO );
        clearcaseMap.getParameters().put( ParametersConstants.APPLI, appliBO );
        clearcaseMap.getParameters().put( ParametersConstants.VOBS, vobsBO );
        mHashMap.getParameters().put( ParametersConstants.CLEARCASE, clearcaseMap );
        ProjectParameterDAOImpl.getInstance().save( getSession(), mHashMap );
        mProject.setParameters( mHashMap );
        ProjectDAOImpl.getInstance().save( getSession(), mProject );
    }

    /**
     * Lance la t�che de montage.
     */
    private void launchTask()
    {
        mCct = new ClearCaseTask();
        mCct.setApplicationId( new Long( mApplication.getId() ) );
        mCct.setProjectId( new Long( mProject.getId() ) );
        mCct.setAuditId( new Long( mAudit.getId() ) );
        mCct.setData( mDatas );
        mCct.run();
    }

    /**
     * Retourne <code>true</code> si la vue a �t� correctement mont�e, <code>false</code> sinon.
     * 
     * @return <code>true</code> en cas de succ�s, <code>
     * false</code> sinon.
     */
    private boolean viewIsMounted()
    {
        boolean success = false;

        File dir = new File( VIEW_PATH );

        /*
         * on v�rifie que le r�pertoire contenant les fichiers de la vue existe.
         */
        if ( dir.isDirectory() )
        {
            /*
             * si le r�pertoire existe, on v�rifie qu'il a bien �t� cr�� par mkview.
             */
            success = checkViewExistence();
        }
        return success;
    }

    /**
     * V�rifie si la vue a bien �t� mont�e, i.e. si le fichier <code>.vws</code> existe.
     * 
     * @return <code>true</code> en cas de succ�s, <code>
     * false</code> sinon.
     */
    private boolean checkViewExistence()
    {
        boolean alreadyMounted = false;
        try
        {
            /* initialisation du runtime. */
            Runtime runtime = Runtime.getRuntime();
            Process processViewExist = runtime.exec( mCct.getConfiguration().getVerifyViewExistenceCommand() );

            /* si la commande est ex�cut�e avec succ�s. */
            if ( 0 == processViewExist.waitFor() )
            {
                alreadyMounted = true;
            }

            processViewExist = null;
            runtime = null;
        }
        catch ( Exception e )
        {
            LOGGER.error( e, e );
            alreadyMounted = false;
        }
        return alreadyMounted;
    }

    /**
     * Supprime / d�monte la vue ClearCase.
     */
    protected void umountView()
    {
        try
        {
            Runtime runtime = Runtime.getRuntime();
            Process processCleanView = runtime.exec( mCct.getConfiguration().getUmountViewCommand() );

            /* le processus se termine correctement */
            processCleanView.waitFor();

            processCleanView = null;
            runtime = null;
        }
        catch ( Exception e )
        {
            LOGGER.error( e, e );
        }
    }

}