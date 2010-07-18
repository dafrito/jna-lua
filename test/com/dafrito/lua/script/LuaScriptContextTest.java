package com.dafrito.lua.script;

import static org.junit.Assert.*;

import javax.script.ScriptContext;

import org.junit.Before;
import org.junit.Test;


public class LuaScriptContextTest {

	private LuaScriptContext ctx;

	@Before
	public void createContext() {
		this.ctx = new LuaScriptContext();
	}
	
	@Test
	public void testConstructor() throws Exception {
		assertNotNull(ctx);
		assertNotNull(ctx.getState());
	}
	
	@Test
	public void testContextBindings() throws Exception {
		assertNotNull(ctx.getBindings(ScriptContext.GLOBAL_SCOPE));
	}
	
}
