package com.dafrito.lua;

import static org.junit.Assert.assertEquals;
import lua.LuaLibrary;
import lua.LuaLibrary.lua_State;

import org.junit.Before;
import org.junit.Test;

public class LuaTest {

	private LuaLibrary lua;
	private lua_State s;
	
	@Before
	public void setup() {
		this.lua = LuaLibrary.INSTANCE;
		this.s = lua.luaL_newstate();
	}
	
	@Test
	public void testPointersAreIdentical() throws Exception {
		lua.lua_createtable(s, 0, 0);
		assertEquals(lua.lua_topointer(s, -1), lua.lua_topointer(s, -1));
	}
}
