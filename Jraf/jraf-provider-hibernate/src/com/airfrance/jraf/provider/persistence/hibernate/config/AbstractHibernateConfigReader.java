/*
 * Created on Dec 29, 2004
 */
package com.airfrance.jraf.provider.persistence.hibernate.config;

import java.io.File;
import java.net.URL;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.HibernateException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.airfrance.jraf.commons.exception.JrafConfigException;
import com.airfrance.jraf.commons.exception.JrafRuntimeException;

/**
 * <p>Title : AbstractHibernateConfigReader.java</p>
 * <p>Description : </p>
 * <p>Copyright : Copyright (c) 2004</p>
 * <p>Company : AIRFRANCE</p>
 */
public abstract class AbstractHibernateConfigReader
	implements IHibernateConfigReader {

	private String configFileName;

	/** log */
	private static final Log log =
		LogFactory.getLog(AbstractHibernateConfigReader.class);

	/**
	 * Lecture du fichier de configuration
	 */
	public abstract SessionFactory readConfig(Configuration configuration) throws JrafConfigException;

	/**
	 * Lecture du fichier de configuration a partir d'une URL
	 * @param in_url url
	 * @return session factory hibernate
	 */
	public SessionFactory readConfig(URL in_url, Configuration configutration) {

		SessionFactory sf = null;
		Configuration configuration = new Configuration();
		try {
			sf = configuration.configure(in_url).buildSessionFactory();
		} catch (HibernateException e) {
			String hibernateProblem = "Probleme d'initialisation hibernate.";
			log.fatal(hibernateProblem, e);
			throw new JrafRuntimeException(hibernateProblem, e);
		}
		// depuis JRAF 2.2 : permet d'afficher un message en cas d'exception hibernate speciale:
		// probleme de cache...
		catch (Throwable t) {
			String hibernateProblem = "Probleme d'initialisation hibernate.";
			log.fatal(hibernateProblem, t);
			throw new JrafRuntimeException(hibernateProblem, t);
		}

		return sf;

	}

	/**
	 * Lecture du fichier de configuration a partir d'un fichier
	 * @param in_file fichier
	 * @return session factory hibernate
	 */
	public SessionFactory readConfig(File in_file, Configuration configuration) {

		SessionFactory sf = null;
		try {
			sf = configuration.configure(in_file).buildSessionFactory();
		} catch (HibernateException e) {
			String hibernateProblem = "Probleme d'initialisation hibernate.";
			log.fatal(hibernateProblem, e);
			throw new JrafRuntimeException(hibernateProblem, e);
		}
		// depuis JRAF 2.2 : permet d'afficher un message en cas d'exception hibernate speciale:
		// probleme de cache...
		catch (Throwable t) {
			String hibernateProblem = "Probleme d'initialisation hibernate.";
			log.fatal(hibernateProblem, t);
			throw new JrafRuntimeException(hibernateProblem, t);
		}
		return sf;
	}

	/**
	 * Renvoie le fichier de config.
	 * @return configFileName
	 */
	public String getConfigFileName() {
		return configFileName;
	}

	/**
	 * Ajoute le nom du fichier de config.
	 * @param string
	 */
	public void setConfigFileName(String string) {
		configFileName = string;
	}
}