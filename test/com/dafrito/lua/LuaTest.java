package com.dafrito.lua;

import static org.junit.Assert.*;
import lua.LuaLibrary;
import lua.LuaLibrary.lua_State;

import org.junit.Before;
import org.junit.Test;

import com.dafrito.lua.script.LuaScriptEngine;

public class LuaTest {

	private LuaScriptEngine e;
	private LuaLibrary lua;
	private lua_State s;
	
	@Before
	public void setup() {
		this.e = new LuaScriptEngine();
		this.lua = LuaLibrary.INSTANCE;
		this.s = lua.luaL_newstate();
	}
	
	@Test
	public void testPointersAreIdentical() throws Exception {
		lua.lua_createtable(s, 0, 0);
		assertEquals(lua.lua_topointer(s, -1), lua.lua_topointer(s, -1));
	}
}
