package com.dafrito.lua.script;

import static org.junit.Assert.*;

import javax.script.Bindings;
import javax.script.ScriptContext;

import org.junit.Before;
import org.junit.Test;


public class LuaBindingsTest {
	
	private LuaScriptEngine e;

	@Before
	public void createEngine() {
		this.e = new LuaScriptEngine();
	}
	
	@Test
	public void testBindingGetAndPut() throws Exception {
		Bindings b = e.getBindings(ScriptContext.ENGINE_SCOPE);
		b.put("A", true);
		assertEquals(true, b.get("A"));
		assertTrue(b.containsKey("A"));
	}
	
}
