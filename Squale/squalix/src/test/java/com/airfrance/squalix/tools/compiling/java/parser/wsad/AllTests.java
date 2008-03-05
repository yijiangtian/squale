package com.airfrance.squalix.tools.compiling.java.parser.wsad;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 *
 */
public class AllTests
{

    /**
     * Suite de tests
     * 
     * @return tests
     */
    public static Test suite()
    {
        TestSuite suite = new TestSuite( "Test for com.airfrance.squalix.tools.compiling.java.parser.wsad" );
        // $JUnit-BEGIN$
        suite.addTest( new TestSuite( JWSADParserTest.class ) );
        suite.addTest( new TestSuite( JWSADParserConfigurationTest.class ) );
        // $JUnit-END$
        return suite;
    }
}