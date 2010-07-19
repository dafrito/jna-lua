package com.dafrito.lua.script;

import static org.junit.Assert.*;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.SimpleBindings;

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
	
	@Test
	public void testBindingsCanBeIdenticallyShared() throws Exception {
		LuaScriptContext o = new LuaScriptContext();
		int E = ScriptContext.ENGINE_SCOPE;
		ctx.setBindings(o.getBindings(E), E);
		assertEquals(o.getBindings(E), ctx.getBindings(E)); 
	}
	
	@Test
	public void testContextDoesntUseUnusableBindings() throws Exception {
		int E = ScriptContext.ENGINE_SCOPE;
		Bindings b = new SimpleBindings();
		ctx.setBindings(b, E);
		assertNotSame(b, ctx.getBindings(E)); 
	}
}
