# Soluvas LDAP Ant Task Library

Copyright 2011 Soluvas (http://www.soluvas.com). All Rights Reserved.

Installation
============
Requires UnboundID LDAP SDK version 2.2.0 or later (licensed under LGPLv2.1).

Usage
=====

	<project name="ant-ldap-sample1" xmlns:ldap="urn:com.soluvas.antldap">
		<taskdef uri="urn:com.soluvas.antldap" resource="com/soluvas/antldap/antlib.xml"
			classpath="lib/com.soluvas.antldap-1.0.0.jar:lib/unboundid-ldapsdk-se.jar"/>
		<target name="do">
			<ldap:attrget propname="his.email" host="localhost" port="10389" binddn="uid=admin,ou=system"
					dn="cn=Horatio Nelson,ou=people,o=sevenSeas" attribute="mail" password="password" />
			<echo message="His email is ${his.email}"/>
			<ldap:attrget port="10636" ssl="true" binddn="uid=admin,ou=system" password="password"
					dn="cn=Horatio Nelson,ou=people,o=sevenSeas" attribute="mail" echo="true" />
			<ldap:search port="10636" ssl="true" binddn="uid=admin,ou=system" password="password" 
					basedn="o=sevenSeas" filter="mail=cbuckley@royalnavy.mod.uk" attribute="description" echo="true" />
		</target>
	</project>

License
=======
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
