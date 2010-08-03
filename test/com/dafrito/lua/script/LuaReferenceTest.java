package com.dafrito.lua.script;

import static org.junit.Assert.*;
import lua.LuaLibrary;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class LuaReferenceTest {

	private LuaBindings b;
	private final LuaLibrary lua = LuaLibrary.INSTANCE;
	
	@Before
	public void createBindings() {
		b = new LuaBindings();
	}

	@Test
	public void getAndSetAReference() throws Exception {
		Variable ref = new ReferenceVariable(b, false);
		ref.set("test");
		assertEquals("test", ref.get());;
	}

	@After
	public void checkStack() {
		assertEquals(0, lua.lua_gettop(b.getState()));
	}
}
