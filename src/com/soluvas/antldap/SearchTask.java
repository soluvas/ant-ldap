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

import org.apache.tools.ant.BuildException;

import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.SearchResultEntry;
import com.unboundid.ldap.sdk.SearchScope;

/**
 * @author ceefour
 * Get an LDAP entry's attribute value.
 * To echo, use 'echo' task attribute.
 * To set a property, use 'propname' task attribute. 
 */
public class SearchTask extends LdapTask {
	
	private String attribute;
	private String baseDn;
	private String propName;
	private String filter;
	private boolean echo;

	@Override
	public void execute() throws BuildException {
		connectLdapWith(new Runnable() {
			@Override
			public void run() {
				try {
					SearchResultEntry entry = connection.searchForEntry(getBaseDn(), SearchScope.SUB, getFilter());
					if (entry != null)
						log(filter + " matches ("+ entry.getDN() +")", 3);
					else 
						log(filter + " does not match", 2);
					// attribute value may be sensitive, such as userPassword
					if (echo)
						System.out.println(attribute + ": " + entry.getAttributeValue(attribute));
					if (propName != null && !propName.isEmpty()) {
						log("Set property "+ propName +"="+ entry.getAttributeValue(attribute), 4);
						getProject().setProperty(propName, entry.getAttributeValue(attribute));
					}
				} catch (LDAPException e) {
					throw new BuildException("Error searching " + filter + " with base "+ baseDn +" on "+ getUri(), e);
				}
			}
		});
	}

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
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

	public void setBaseDn(String baseDn) {
		this.baseDn = baseDn;
	}

	public String getBaseDn() {
		return baseDn;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public String getFilter() {
		return filter;
	}

}
