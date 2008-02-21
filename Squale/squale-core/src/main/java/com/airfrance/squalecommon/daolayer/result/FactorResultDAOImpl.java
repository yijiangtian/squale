/*
 * Cr�� le 19 juil. 05
 *
 */
package com.airfrance.squalecommon.daolayer.result;

import com.airfrance.jraf.provider.persistence.hibernate.AbstractDAOImpl;
import com.airfrance.squalecommon.enterpriselayer.businessobject.result.FactorResultBO;

/**
 * @author M400843
 *
 */
public class FactorResultDAOImpl extends AbstractDAOImpl {
    /**
     * Instance singleton
     */
    private static FactorResultDAOImpl instance = null;

    /** initialisation du singleton */
    static {
        instance = new FactorResultDAOImpl();
    }

    /**
     * Constructeur prive
     * @throws JrafDaoException
     */
    private FactorResultDAOImpl() {
        initialize(FactorResultBO.class);
    }

    /**
     * Retourne un singleton du DAO
     * @return singleton du DAO
     * @deprecated
     */
    public static FactorResultDAOImpl getInstance() {
        return instance;
    }
}
