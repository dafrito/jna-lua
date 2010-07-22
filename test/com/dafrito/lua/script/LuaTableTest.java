package com.dafrito.lua.script;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

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
	
	@Test
	public void iterateOverAnEmptyTable() throws Exception {
		lua.lua_createtable(b.getState(), 0, 0);
		LuaTable t = new LuaTable(new LuaReference(b));
		assertFalse(t.iterator().hasNext());
	}
	
	@Test
	public void iterateOverAnOneElementTable() throws Exception {
		lua.lua_createtable(b.getState(), 0, 0);
		LuaTable t = new LuaTable(new LuaReference(b));
		t.add("No time");
		t.add(42.0d);
		Iterator<Object> i = t.iterator();
		assertTrue(i.hasNext());
		assertEquals("No time", i.next());	
		assertEquals(42.0d, i.next());	
		assertFalse(i.hasNext());
	}

	@Test
	public void removeAnElementUsingAnIterator() throws Exception {
		lua.lua_createtable(b.getState(), 0, 0);
		LuaTable t = new LuaTable(new LuaReference(b));
		t.add("A");
		t.add("B");
		t.add("C");
		int counter=0;
		for(Iterator<Object> i = t.iterator(); i.hasNext();) {
			counter++;
			Object v = i.next();
			if(v.equals("B")) {
				i.remove();
			}
		}
		assertEquals(3, counter);
		assertEquals(2, t.size());
	}
	
	
	@Before
	public void setup() {
		ctx = new LuaScriptContext();
		b = ctx.getGlobals();
	}
	
}
