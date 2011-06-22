package com.soluvas.antldap.test;

import static org.junit.Assert.assertTrue;

import org.apache.tools.ant.BuildException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.soluvas.antldap.AttrGetTask;

public class AttrGetTaskTest {

	private AttrGetTask task;

	@Before
	public void setUp() {
		task = new AttrGetTask();
		task.setPort(10389);
		task.setBindDn("uid=admin,ou=system");
		task.setPassword("password");
	}
	
	@Test
	public void testExecuteEntryNotFound() {
		task.setDn("o=something");
		task.setAttribute("uid");
		task.setEcho(true);
		try {
			task.execute();
		} catch (BuildException e) {
			Assert.assertTrue("'"+ e.getMessage() +"' should start with 'Cannot get LDAP entry'",
					e.getMessage().startsWith("Cannot get LDAP entry"));
		}
	}

	@Test
	public void testExecuteAttributeNotFound() {
		task.setDn("uid=admin,ou=system");
		task.setAttribute("aneh");
		task.setEcho(true);
		try {
			task.execute();
		} catch (BuildException e) {
			Assert.assertTrue("'"+ e.getMessage() +"' should start with 'Cannot find attribute'", e.getMessage().startsWith("Cannot find attribute"));
		}
	}

	@Test
	public void testExecuteEntryFound() {
		task.setDn("uid=admin,ou=system");
		task.setAttribute("uid");
		task.setEcho(true);
		task.execute();
	}

}
