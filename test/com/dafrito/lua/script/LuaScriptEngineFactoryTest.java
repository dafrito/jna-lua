package com.dafrito.lua.script;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import javax.script.ScriptEngine;

import org.junit.Before;
import org.junit.Test;

public class LuaScriptEngineFactoryTest {

	private LuaScriptEngineFactory f;

	@Before
	public void setUp() {
		this.f = new LuaScriptEngineFactory();
	}

	@Test
	public void testFactorySettings() throws Exception {
		assertEquals("Lua", f.getLanguageName());
		assertNotNull("Engine name must not be null", f.getEngineName());
		assertNotNull("Engine version must not be null", f.getEngineVersion());
		assertTrue("Factory must support 'lua' extension", f.getExtensions().contains("lua"));

	}

	@Test
	public void testMimeTypes() {
		assertTrue(f.getMimeTypes().contains("application/x-lua"));
		assertTrue(f.getMimeTypes().contains("text/x-lua"));
	}

	@Test
	public void testNames() {
		assertTrue(f.getNames().contains("lua"));
		assertTrue(f.getNames().contains("Lua"));
	}

	@Test
	public void testScriptEngineOwnsFactory() throws Exception {
		assertEquals(f.getScriptEngine().getFactory(), f);
	}

	@Test
	public void testParameter() {
		// getParameter()
		assertEquals(f.getEngineName(), f.getParameter(ScriptEngine.ENGINE));
		assertEquals(f.getEngineVersion(), f.getParameter(ScriptEngine.ENGINE_VERSION));
		assertTrue(f.getNames().contains(f.getParameter(ScriptEngine.NAME)));
		assertEquals(f.getLanguageName(), f.getParameter(ScriptEngine.LANGUAGE));
		assertEquals(f.getLanguageVersion(), f.getParameter(ScriptEngine.LANGUAGE_VERSION));
	}

	@Test
	public void testGetProgram() {
		assertEquals("a = 1\nreturn b\n", f.getProgram("a = 1", "return b"));
	}

	@Test
	public void testMethodCallSyntax() {
		assertEquals("process:execute(a, b)", f.getMethodCallSyntax("process", "execute", "a", "b"));
	}

	@Test
	public void testOutputStatement() {
		assertEquals("print(\"test\")", f.getOutputStatement("test"));
		assertEquals("print(\"\\\"quoted\\\"\")", f.getOutputStatement("\"quoted\""));
	}

	@Test
	public void testGetScriptEngine() {
		assertNotNull(f.getScriptEngine());
	}
	
	@Test
	public void testCreatedScriptEnginesAreDifferent() throws Exception {
		assertNotSame(f.getScriptEngine(), f.getScriptEngine());
	}
	
}
