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

/**
 * @author ceefour
 * Get an LDAP entry's attribute value.
 * To echo, use 'echo' task attribute.
 * To set a property, use 'propname' task attribute. 
 */
public class AttrGetTask extends LdapTask {
	
	private String dn;
	private String attribute;
	private String propName;
	private boolean echo;

	@Override
	public void execute() throws BuildException {
		connectLdapWith(new Runnable() {
			@Override
			public void run() {
				try {
					SearchResultEntry entry = connection.getEntry(dn);
					if (entry == null)
						throw new BuildException("Cannot get LDAP entry with DN " + dn);
					if (!entry.hasAttribute(attribute))
						throw new BuildException("Cannot find attribute "+ attribute +" in DN " + dn);
					// attribute value may be sensitive, such as userPassword
					final String attrVal = entry.getAttributeValue(attribute);
					log("("+ dn +") " + attribute + ": " + attrVal, 4);
					if (echo)
						System.out.println(attribute + ": " + attrVal);
					if (propName != null && !propName.isEmpty()) {
						log("Set property "+ propName +"="+ attrVal, 4);
						getProject().setProperty(propName, attrVal);
					}
				} catch (LDAPException e) {
					throw new BuildException("Cannot find LDAP entry " + dn + " on "+ getUri(), e);
				}
			}
		});
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
