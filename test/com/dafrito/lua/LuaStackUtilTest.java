package com.dafrito.lua;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.util.List;

import lua.LuaLibrary;
import lua.LuaLibrary.lua_State;

import org.junit.Before;
import org.junit.Test;

import com.dafrito.lua.script.LuaBindings;


public class LuaStackUtilTest {
	private LuaLibrary lua;
	private LuaStackUtil u;
	private lua_State s;
	private LuaBindings b;

	@Before
	public void setup() {
		lua = LuaLibrary.INSTANCE;
		u = LuaStackUtil.INSTANCE;
		b = new LuaBindings();
		s = b.getState();
	}
	
	@Test
	public void testEmptyStackIsEmpty() throws Exception {
		assertEquals(0, u.asList(b).size());
		assertTrue(u.isEmpty(s));
	}
	
	@Test
	public void testGettingAnElement() throws Exception {
		lua.lua_pushstring(s, "No time");
		assertEquals("No time", u.get(b, 1));
	}
	
	@Test
	public void testAsList() throws Exception {
		lua.lua_pushstring(s, "No time");
		List<Object> values = u.asList(b);
		assertEquals(1, values.size());
		assertEquals("No time", values.get(0));
	}
	
	@Test
	public void testGetIsIdempotent() throws Exception {
		lua.lua_pushstring(s, "No time");
		assertEquals("No time", u.get(b, 1));
		assertEquals("No time", u.get(b, 1));
	}
	
	@Test
	public void testAsListReturnsEqualLists() throws Exception {
		lua.lua_pushstring(s, "No time");
		assertEquals(u.asList(b), u.asList(b));
	}
	
	@Test
	public void testPrintIsEqual() throws Exception {
		lua.lua_pushstring(s, "No time");
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		u.print(b, os);
		assertEquals("Stack (size:1)\n[1] No time\n", os.toString());
		ByteArrayOutputStream os2 = new ByteArrayOutputStream();
		u.print(b, os2);
		assertEquals(os.toString(), os2.toString());
	}
}
