package com.soluvas.antldap;

import java.security.GeneralSecurityException;

import javax.net.ssl.SSLSocketFactory;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.util.ssl.SSLUtil;
import com.unboundid.util.ssl.TrustAllTrustManager;

public abstract class LdapTask extends Task {
	
	private String host = "localhost";
	private Integer port;
	private boolean ssl;
	private String bindDn;
	private String password;
	protected LDAPConnection connection;

	protected void connectLdapWith(Runnable runnable) throws BuildException {
		log("Connecting to LDAP " + getUri(), 3);
		try {
			if (ssl) {
				SSLUtil sslUtil = new SSLUtil(new TrustAllTrustManager());
				SSLSocketFactory socketFactory = sslUtil.createSSLSocketFactory();
				connection = new LDAPConnection(socketFactory, host, getPort());
			} else {
				connection = new LDAPConnection(host, getPort());
			}
			try {
				try {
					connection.bind(bindDn, password);
				} catch (LDAPException e) {
					throw new BuildException("Cannot bind to " + getUri() + " using DN "+ bindDn, e);
				}
				runnable.run();
			} finally {
				connection.close();
			}
		} catch (LDAPException e) {
			throw new BuildException("Cannot connect to LDAP Server " + getUri(), e);
		} catch (GeneralSecurityException e) {
			throw new BuildException("Cannot create SSL Socket Factory", e);
		}
	}

	protected String getUri() {
		return (ssl ? "ldaps" : "ldap") + "://" + host + ":" + getPort();
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

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

}
