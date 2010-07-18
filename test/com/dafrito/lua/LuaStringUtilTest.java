package com.dafrito.lua;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;


public class LuaStringUtilTest {
	
	private LuaStringUtil u;

	@Before
	public void setup() {
		this.u = LuaStringUtil.INSTANCE; 
	}
	
	@Test
	public void testSingleLetterEscapes() throws Exception {
		assertEquals("\\\"", u.escape("\""));
	}
	
	@Test
	public void testEscape() throws Exception {
		assertEquals("Trivial string is unchanged", "notime", u.escape("notime"));
		assertEquals("String with quotes is escaped", "\\\"notime\\\"", u.escape("\"notime\""));
	}
	
	@Test
	public void testQuote() throws Exception {
		assertEquals("Trivial string is simply quoted", "\"notime\"", u.quote("notime"));
		assertEquals("String with quotes is escaped", "\" \\\" \"", u.quote(" \" "));
	}
}
