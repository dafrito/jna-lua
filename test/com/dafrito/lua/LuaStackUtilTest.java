package com.dafrito.lua;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.util.List;

import lua.LuaLibrary;
import lua.LuaLibrary.lua_State;

import org.junit.Before;
import org.junit.Test;

import com.dafrito.lua.script.PrimitiveLuaTranslator;


public class LuaStackUtilTest {
	private LuaLibrary lua;
	private LuaStackUtil u;
	private PrimitiveLuaTranslator t;
	private lua_State s;

	@Before
	public void setup() {
		lua = LuaLibrary.INSTANCE;
		s = lua.luaL_newstate();
		u = LuaStackUtil.INSTANCE;
		t = new PrimitiveLuaTranslator();
	}
	
	@Test
	public void testEmptyStackIsEmpty() throws Exception {
		assertEquals(0, u.asList(s, t).size());
		assertTrue(u.isEmpty(s));
	}
	
	@Test
	public void testGettingAnElement() throws Exception {
		lua.lua_pushstring(s, "No time");
		assertEquals("No time", u.get(s, t, 1));
	}
	
	@Test
	public void testAsList() throws Exception {
		lua.lua_pushstring(s, "No time");
		List<Object> values = u.asList(s, t);
		assertEquals(1, values.size());
		assertEquals("No time", values.get(0));
	}
	
	@Test
	public void testGetIsIdempotent() throws Exception {
		lua.lua_pushstring(s, "No time");
		assertEquals("No time", u.get(s, t, 1));
		assertEquals("No time", u.get(s, t, 1));
	}
	
	@Test
	public void testAsListReturnsEqualLists() throws Exception {
		lua.lua_pushstring(s, "No time");
		assertEquals(u.asList(s, t), u.asList(s, t));
	}
	
	@Test
	public void testPrintIsEqual() throws Exception {
		lua.lua_pushstring(s, "No time");
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		u.print(s, t, os);
		assertEquals("Stack (size:1)\n[1] No time\n", os.toString());
		ByteArrayOutputStream os2 = new ByteArrayOutputStream();
		u.print(s, t, os2);
		assertEquals(os.toString(), os2.toString());
	}
}
