/**
 * 
 */
package com.soluvas.antldap;

import java.util.logging.Level;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.SearchResultEntry;

/**
 * @author ceefour
 *
 */
public class LdapPropEchoTask extends Task {
	
	private String host;
	private int port = 389;
	private boolean ssl;
	private String bindDn;
	private String dn;
	private String attribute;
	private String password;
	private SearchResultEntry entry;

	private void doSomethingWith(Runnable runnable) throws BuildException {
		String uri = "ldap://" + host + ":" + port;
		log("Connecting to LDAP " + uri, Level.FINE.intValue());
		LDAPConnection ldapConnection = new LDAPConnection();
		try {
			ldapConnection.connect(host, port);
			try {
				try {
					ldapConnection.bind(bindDn, password);
				} catch (LDAPException e) {
					throw new BuildException("Cannot connect bind to " + uri + " using DN "+ bindDn, e);
				}
				try {
					entry = ldapConnection.getEntry(dn);
					runnable.run();
				} catch (LDAPException e) {
					throw new BuildException("Cannot find LDAP entry " + dn + " on "+ uri, e);
				}
			} finally {
				ldapConnection.close();
			}
		} catch (LDAPException e) {
			throw new BuildException("Cannot connect to LDAP Server " + uri, e);
		}
	}
	
	@Override
	public void execute() throws BuildException {
		doSomethingWith(new Runnable() {
			
			@Override
			public void run() {
				System.out.println(attribute + ": " + entry.getAttributeValue(attribute));
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
		return port;
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

}