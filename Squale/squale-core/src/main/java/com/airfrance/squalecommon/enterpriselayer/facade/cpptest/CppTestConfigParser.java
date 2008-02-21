package com.airfrance.squalecommon.enterpriselayer.facade.cpptest;

import java.io.InputStream;

import org.apache.commons.digester.Digester;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.airfrance.squalecommon.enterpriselayer.businessobject.rulechecking.RuleBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.rulechecking.RuleSetBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.rulechecking.cpptest.CppTestRuleSetBO;
import com.airfrance.squalecommon.util.xml.XmlImport;

/**
 * Parser de configuration de CppTest
 */
public class CppTestConfigParser extends XmlImport {
    
    /** Log */
    private static Log LOG = LogFactory.getLog(CppTestConfigParser.class);

    /** Nom publique de la DTD */
    final static String PUBLIC_DTD = "-//Squale//DTD CppTest Configuration 1.0//EN";
    /** Localisation de la DTD */
    final static String DTD_LOCATION = "/com/airfrance/squalecommon/dtd/cpptest-1.0.dtd";
    
    /**
     * Constructeur
     *
     */
    public CppTestConfigParser() {
        super(LOG);
    }
    /**
     * Parsing du fichier de configuration CppTest
     * @param pStream flux
     * @param pErrors erreurs rencontr�es
     * @return donn�es lues
     */
    public CppTestRuleSetBO parseFile(InputStream pStream, StringBuffer pErrors) {
        // R�sultat
        CppTestRuleSetBO result = new CppTestRuleSetBO();

        Digester configDigester = preSetupDigester(PUBLIC_DTD, DTD_LOCATION, pErrors);
        configDigester.push(result);
        configDigester.addSetProperties("ruleset");
        configDigester.addObjectCreate("ruleset/rule", RuleBO.class);
        configDigester.addSetProperties("ruleset/rule");
        configDigester.addCallMethod("ruleset/rule", "setRuleSet", 1, new Class[]{RuleSetBO.class});
        configDigester.addCallParam("ruleset/rule", 0, 1);
        configDigester.addSetNext("ruleset/rule", "addRule");
        parse(configDigester, pStream, pErrors);
        return result;
    }
    
    
    
}
