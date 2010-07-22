package com.dafrito.lua.script;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import lua.LuaLibrary;

import org.junit.Before;
import org.junit.Test;

public class LuaTableTest {
	LuaLibrary lua = LuaLibrary.INSTANCE;
	private LuaBindings b;
	private LuaScriptContext ctx;

	@Test
	public void getAndPutAValueIntoATable() throws Exception {
		lua.lua_createtable(b.getState(), 0, 0);
		LuaTable t = new LuaTable(new LuaReference(b));
		t.set(1, "No time");
		assertEquals("No time", t.get(1));
	}

	@Test
	public void sizeOfOneElementTableIsOne() throws Exception {
		lua.lua_createtable(b.getState(), 0, 0);
		LuaTable t = new LuaTable(new LuaReference(b));
		t.add("No time");
		assertEquals(1, t.size());
	}
	
	@Test
	public void testRemovingAnElementMakesTheTableEmpty() throws Exception {
		lua.lua_createtable(b.getState(), 0, 0);
		LuaTable t = new LuaTable(new LuaReference(b));
		t.add("No time");
		t.remove(0);
		assertTrue(t.isEmpty());
	}
	
	@Before
	public void setup() {
		ctx = new LuaScriptContext();
		b = ctx.getGlobals();
	}

}
