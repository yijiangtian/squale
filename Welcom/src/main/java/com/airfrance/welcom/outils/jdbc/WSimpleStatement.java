package com.airfrance.welcom.outils.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.airfrance.welcom.outils.jdbc.wrapper.SimpleStatement;

/**
 * 
 * @author M327837
 *
 * Surcouche du @link com.airfrance.welcom.outils.jdbc.wrapper.SimpleStatement
 * Effectue le Tracing SQL dans l'appender SQLLoggin
 * Dump au format Oracle
 * Memorise si la connexion est deja ferm�
 */
public class WSimpleStatement extends SimpleStatement {
    /** logger */
    private static Log log = LogFactory.getLog("SQLLogging");
    /** Taille max pour l'affichage du nom de l'utlisateur */
    private static final int MAX_LENGTH_USER = 10;

    /** Nom du l'utilisateur pour tracer les acc�s BD */
    private String user = WStatement.DEFAULTUSER;

    /** Test si la connexion est ferm� */
    private boolean isclose = true;

    /**
     * Constructeur
     * @param pSQL : Requete SQL
     * @param pConnection : Connexion sur laquelle elle est effectu�
     * @throws SQLException : Probleme SQL
     */
    public WSimpleStatement(final String pSQL, final Connection pConnection) throws SQLException {
        super(pSQL, pConnection);
        isclose = false;
    }

    /**
     * Constructeur
     * @param pSQL : Requete SQL
     * @param pConnection : Connexion sur laquelle elle est effectu�
     * @param pUser : Nom de l'ulisateur pour la tracage
     * @throws SQLException : Probleme SQL
     */
    public WSimpleStatement(final String pSQL, final Connection pConnection, final String pUser) throws SQLException {
        super(pSQL, pConnection);

        if (user != null) {
            this.user = pUser;
        }

        isclose = false;
    }

    /**
     * Execute la requete de consultation
     * @throws SQLException : Probleme SQL
     * @return ResultSet : Resultset de la requete
     * */
    public ResultSet executeQuery() throws SQLException {
        LocationInfo info;
        info = new LocationInfo(new Throwable(), "WStatement");

        final String className = info.getClassName();
        final String lineNumber = info.getLineNumber();
        String status = "Ok";
        final long start = new Date().getTime();

        try {
            final ResultSet rs = super.executeQuery();

            return rs;
        } catch (final SQLException sqle) {
            status = "Erreur [" + sqle.getMessage() + "]";
            throw sqle;
        } finally {
            final long end = new Date().getTime();

            if (WJdbc.isEnabledTrace()) {
                if ((end - start) < WStatement.LIMITSPEED) {
                    log.info("-> QUERY (" + (end - start) + " ms )  :" + getUser() + ": " + getSQL() + " - (" + className + "," + lineNumber + ") - (" + status + ")");
                } else {
                    log.warn("-> QUERY (" + (end - start) + " ms )  :" + getUser() + ": " + getSQL() + " - (" + className + "," + lineNumber + ") - (" + status + ")");
                }
            }
        }
    }

    /**
     * Execute la requete de modification
     * @throws SQLException : Probleme SQL
     * @return int : Nombre d'element modifi�s
     * */
    public int executeUpdate() throws SQLException {
        LocationInfo info;
        info = new LocationInfo(new Throwable(), "WStatement");

        final String className = info.getClassName();
        final String lineNumber = info.getLineNumber();
        String status = "Ok";

        final long start = new Date().getTime();

        try {
            return super.executeUpdate();
        } catch (final SQLException sqle) {
            status = "Erreur [" + sqle.getMessage() + "]";
            throw sqle;
        } finally {
            final long end = new Date().getTime();
            if (WJdbc.isEnabledTrace()) {
                if ((end - start) < WStatement.LIMITSPEED) {
                    log.info("-> UPDATE (" + (end - start) + " ms )  :" + getUser() + ": " + getSQL() + " - (" + className + "," + lineNumber + ") - (" + status + ")");
                } else {
                    log.warn("-> UPDATE (" + (end - start) + " ms )  :" + getUser() + ": " + getSQL() + " - (" + className + "," + lineNumber + ") - (" + status + ")");
                }
            }
        }
    }

    /**
     * Trace et retourne le resultSet
     * @return : retourne le resusultSet du stament
     * @throws SQLException : Probleme SQL
     */
    public ResultSet getResultSet() throws SQLException {
        //LocationInfo info;
        //info = new LocationInfo(new Throwable(), "WSimpleStatement");

        //String className = info.getClassName ();
        //String lineNumber = info.getLineNumber ();

        return super.getStatement().getResultSet();
    }

    /** 
     * Fermeture de la connexion 
     * @throws SQLException : Probleme SQL
     */
    public void close() throws SQLException {
        if (!isClose()) {
            user = WStatement.DEFAULTUSER;
            super.close();
            isclose = true;
        }
    }

    /**
     * @return Retourn Vrai Si la connexion est deja ferm�
     */
    public boolean isClose() {
        return isclose;
    }

    /**
     * Gets the user
     * @return Returns a String
     */
    public String getUser() {
        if (user.length() < MAX_LENGTH_USER) {
            while (user.length() < MAX_LENGTH_USER) {
                user += " ";
            }
        }

        return user;
    }

    /**
     * Sets the user
     * @param pUser The user to set
     */
    public void setUser(final String pUser) {
        this.user = pUser;
    }
}