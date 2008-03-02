package com.airfrance.squaleweb.applicationlayer.formbean.reference;

import java.util.ArrayList;
import java.util.List;

import com.airfrance.squaleweb.applicationlayer.formbean.RootForm;

/**
 * Regroupe l'ensemble des diff�rents ReferenceListForm
 */
public class SetOfReferencesListForm
    extends RootForm
{

    /**
     * Ensemble des listes des r�f�rences, liste de ReferenceGridForm
     */
    private List mList = new ArrayList( 0 );

    /**
     * @return la liste des grilles de r�f�rences
     */
    public List getList()
    {
        return mList;
    }

    /**
     * @param pList la liste des grille de r�f�rences
     */
    public void setList( List pList )
    {
        mList = pList;
    }

    /**
     * Ajoute la r�f�rence associ�e � la grille Si une grille porte le m�me nom et que la date est avant pGridForm alors
     * on supprime la grille et on ajoute pGridForm
     * 
     * @param pGridForm la grille de r�f�rence
     * @param pReferenceForm la r�f�rence a ajouter
     */
    public void add( ReferenceGridForm pGridForm, ReferenceForm pReferenceForm )
    {
        // On parcours la liste
        List refs = null;
        ReferenceGridForm current = null;
        boolean found = false;
        int compare = 0;
        for ( int i = 0; i < mList.size() && null == refs; i++ )
        {
            current = (ReferenceGridForm) mList.get( i );
            if ( current.getName().equals( pGridForm.getName() ) )
            {
                refs = (List) current.getReferenceListForm().getList(); // on sauvegarde la valeur associ�e
                compare = current.getUpdateDate().compareTo( pGridForm.getUpdateDate() );
                // Si une grille du m�me nom existe et que la date est avant
                // alors on la remplace par la grille en param�tre
                if ( compare < 0 )
                {
                    mList.remove( i ); // on supprime l'entr�e afin de la remplacer
                }
                else
                {
                    found = true;
                    if ( compare == 0 )
                    { // ce sont les m�mes grille, on ajoute la r�f�rence
                        refs.add( pReferenceForm );
                    }
                }
            }
        }
        // Si on a trouv� aucune entr�e portant le m�me nom, on ajoute la grille
        if ( !found )
        {
            refs = new ArrayList();
            refs.add( pReferenceForm );
            ReferenceListForm refsList = new ReferenceListForm();
            refsList.setList( refs );
            pGridForm.setReferenceListForm( refsList );
            mList.add( pGridForm );
        }
    }

}