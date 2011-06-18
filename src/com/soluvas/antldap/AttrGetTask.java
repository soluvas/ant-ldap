/*
 * Copyright 2011 Soluvas.
 * All Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License (GPLv2 only)
 * or the terms of the GNU Lesser General Public License (LGPLv2.1 only)
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 */
package com.soluvas.antldap;

import java.security.GeneralSecurityException;

import javax.net.ssl.SSLSocketFactory;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.SearchResultEntry;
import com.unboundid.util.ssl.SSLUtil;
import com.unboundid.util.ssl.TrustAllTrustManager;

/**
 * @author ceefour
 * Get an LDAP entry's attribute value.
 * To echo, use 'echo' task attribute.
 * To set a property, use 'propname' task attribute. 
 */
public class AttrGetTask extends Task {
	
	private String host = "localhost";
	private Integer port;
	private boolean ssl;
	private String bindDn;
	private String dn;
	private String attribute;
	private String password;
	private SearchResultEntry entry;
	private String propName;
	private boolean echo;

	private void connectLdapWith(Runnable runnable) throws BuildException {
		String uri = (ssl ? "ldaps" : "ldap") + "://" + host + ":" + getPort();
		log("Connecting to LDAP " + uri, 3);
		LDAPConnection ldapConnection;
		try {
			if (ssl) {
				SSLUtil sslUtil = new SSLUtil(new TrustAllTrustManager());
				SSLSocketFactory socketFactory = sslUtil.createSSLSocketFactory();
				ldapConnection = new LDAPConnection(socketFactory, host, getPort());
			} else {
				ldapConnection = new LDAPConnection(host, getPort());
			}
			try {
				try {
					ldapConnection.bind(bindDn, password);
				} catch (LDAPException e) {
					throw new BuildException("Cannot bind to " + uri + " using DN "+ bindDn, e);
				}
				try {
					entry = ldapConnection.getEntry(dn);
					log("("+ dn +") " + attribute + ": " + entry.getAttributeValue(attribute), 3);
					runnable.run();
				} catch (LDAPException e) {
					throw new BuildException("Cannot find LDAP entry " + dn + " on "+ uri, e);
				}
			} finally {
				ldapConnection.close();
			}
		} catch (LDAPException e) {
			throw new BuildException("Cannot connect to LDAP Server " + uri, e);
		} catch (GeneralSecurityException e) {
			throw new BuildException("Cannot create SSL Socket Factory", e);
		}
	}
	
	@Override
	public void execute() throws BuildException {
		connectLdapWith(new Runnable() {
			@Override
			public void run() {
				if (echo)
					System.out.println(attribute + ": " + entry.getAttributeValue(attribute));
				if (propName != null && !propName.isEmpty()) {
					log("Set property "+ propName +"="+ entry.getAttributeValue(attribute));
					getProject().setProperty(propName, entry.getAttributeValue(attribute));
				}
			}
		});
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port != null ? port.intValue() : (ssl ? 636 : 389);
	}

	public void setPort(int port) {
		this.port = port;
	}

	public boolean isSsl() {
		return ssl;
	}

	public void setSsl(boolean ssl) {
		this.ssl = ssl;
	}

	public String getBindDn() {
		return bindDn;
	}

	public void setBindDn(String bindDn) {
		this.bindDn = bindDn;
	}

	public String getDn() {
		return dn;
	}

	public void setDn(String dn) {
		this.dn = dn;
	}

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public void setPropName(String propName) {
		this.propName = propName;
	}

	public String getPropName() {
		return propName;
	}

	public void setEcho(boolean echo) {
		this.echo = echo;
	}

	public boolean isEcho() {
		return echo;
	}

}
