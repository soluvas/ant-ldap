package com.soluvas.antldap.test;

import static org.junit.Assert.assertTrue;

import org.apache.tools.ant.BuildException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.soluvas.antldap.SearchTask;

public class SearchTaskTest {

	private SearchTask task;

	@Before
	public void setUp() {
		task = new SearchTask();
		task.setPort(10389);
		task.setBindDn("uid=admin,ou=system");
		task.setPassword("password");
	}
	
	@Test
	public void testExecuteEntryNotFound() {
		task.setBaseDn("ou=system");
		task.setFilter("uid=admino");
		task.setAttribute("uid");
		task.setEcho(true);
		try {
			task.execute();
		} catch (BuildException e) {
			Assert.assertTrue("'"+ e.getMessage() +"' should start with 'Cannot find LDAP entry'",
					e.getMessage().startsWith("Cannot find LDAP entry"));
		}
	}

	@Test
	public void testExecuteAttributeNotFound() {
		task.setBaseDn("ou=system");
		task.setFilter("uid=admin");
		task.setAttribute("aneh");
		task.setEcho(true);
		try {
			task.execute();
		} catch (Exception e) {
			Assert.assertTrue("'"+ e.getMessage() +"' start with 'Cannot find attribute'", e.getMessage().startsWith("Cannot find attribute"));
		}
	}

	@Test
	public void testExecuteEntryFound() {
		task.setBaseDn("ou=system");
		task.setFilter("uid=admin");
		task.setAttribute("uid");
		task.setEcho(true);
		task.execute();
		assertTrue(true);
	}

}
