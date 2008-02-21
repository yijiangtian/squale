package com.airfrance.squalecommon.enterpriselayer.businessobject.rulechecking;

import java.io.Serializable;

/**
 * R�gle de rulechecking
 * @hibernate.class 
 * table="Rule"
 * mutable="true"
 * lazy="true"
 * discriminator-value="Rule"
 * @hibernate.discriminator 
 *   column="subclass"
 *
 **/
public class RuleBO implements Serializable {

    /**
     * Identifiant (au sens technique) de l'objet
     */
    protected long mId = -1;
    
    /**
     * Code de la R�gle
     */
    protected String mCode;
    
    
    
    /**
     * Categorie de la R�gle
     */
    protected String mCategory;
    
       
    
    /**
     * Version de la R�gle
     */
    protected RuleSetBO mRuleSet;
    
   /**
     * Severite de la R�gle
     */
    protected String mSeverity;
    
    /**
     * Constucteur par d�faut
     *
     */
    public RuleBO() {
        mId = -1;
    
    }
   
    /**
     * Access method for the mCategory property.
     * 
     * @return   the current value of the mCategory property
     * 
     * @hibernate.property 
     * name="Category" 
     * column="Category" 
     * type="string" 
     * not-null="false" 
     * unique="false"
     **/ 

    public String getCategory() {
        return mCategory;
    }

    /**
     * Sets the value of the mCategory property.
     * 
     * @param pCategory the new value of the mCategory property
     **/
    
    public void setCategory(String pCategory) {
        mCategory = pCategory;
    }

    /**
     * Access method for the mCode property.
     * 
     * @return   the current value of the mCode property
     * 
     * @hibernate.property 
     * name="Code" 
     * column="Code" 
     * type="string" 
     * not-null="false" 
     * unique="false"
     **/ 
    
    
    public String getCode() {
        return mCode;
    }

    /**
     * Sets the value of the mCode property.
     * 
     * @param pCode the new value of the mCode property
     **/
    public void setCode(String pCode) {
        mCode = pCode;
    }

     /**
     * Access method for the mId property.
     * @return   the current value of the mId property
     * 
     * Note: unsaved-value An identifier property value that indicates that an instance 
     * is newly instantiated (unsaved), distinguishing it from transient instances that 
     * were saved or loaded in a previous session.  If not specified you will get an exception like this:
     * another object associated with the session has the same identifier
     * 
     * @hibernate.id generator-class="native" 
     * type="long" 
     * column="RuleId" 
     * unsaved-value="-1" 
     * length="19"
     * @hibernate.generator-param name="sequence" value="Rule_sequence" 
     */
    public long getId() {
        return mId;
    }

        
   /**
     * Access method for the mVersion property.
     * 
     * @return   the current Version
     * 
     * @hibernate.many-to-one 
     * name="version" 
     * column="RuleSetId" 
     * class="com.airfrance.squalecommon.enterpriselayer.businessobject.rulechecking.RuleSetBO"
     * cascade="save-update"
     * not-null="true"
     */
    public RuleSetBO getRuleSet() {
        return mRuleSet;
    }

    /**
     * Sets the value of the mVersion property.
     * 
     * @param pVersion the new value of the mVersion property
     **/
    public void setRuleSet(RuleSetBO pVersion) {
        mRuleSet = pVersion;
    }
    
    
  
    /**
     * Sets the value of the mId property
     * @param pId The new value of the mId property
     */
    public void setId(long pId) {
        mId = pId;
    } 
    
    /**
        * Access method for the mSeverity property.
        * 
        * @return   the current value of the mSeverity property
        * 
        * @hibernate.property 
        * name="Severity" 
        * column="Severity" 
        * type="string" 
        * not-null="false" 
        * unique="false"
        **/ 
       public String getSeverity() {
           return mSeverity;
       }

       /**
        * Sets the value of the mSeverity property.
        * 
        * @param pSeverity the new value of the mSeverity property
        **/
       public void setSeverity(String pSeverity) {
           mSeverity = pSeverity;
       }
}



    
